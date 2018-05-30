package keSt93.springmoviedb.controller;


import keSt93.springmoviedb.entities.*;
import keSt93.springmoviedb.repository.*;
import keSt93.springmoviedb.utils.DataUriHelper;
import keSt93.springmoviedb.utils.ImdbApi;
import keSt93.springmoviedb.utils.MovieDbUtils;
import keSt93.springmoviedb.utils.NotificationHelper;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.transaction.Transactional;
import java.net.URL;
import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by stein on 14.02.2018.
 */

@Controller
public class MovieInstanceController {


    private MovieRepository movieRepository;
    private MovieRatingRepository movieRatingRepository;
    private UserRepository userRepository;
    private NotificationUserRelationRepository notificationUserRelationRepository;
    private NotificationTypeRepository notificationTypeRepository;
    private MovieCommentsRepository movieCommentsRepository;

    public MovieInstanceController(
            MovieRepository movieRepository,
            MovieRatingRepository movieRatingRepository,
            UserRepository userRepository,
            NotificationUserRelationRepository notificationUserRelationRepository,
            NotificationTypeRepository notificationTypeRepository,
            MovieCommentsRepository movieCommentsRepository
    ) {
        this.movieRepository = movieRepository;
        this.movieRatingRepository = movieRatingRepository;
        this.userRepository = userRepository;
        this.notificationUserRelationRepository = notificationUserRelationRepository;
        this.notificationTypeRepository = notificationTypeRepository;
        this.movieCommentsRepository = movieCommentsRepository;
    }


    // Movie Detail Page
    @GetMapping("/m/movies/{id}")
    public ModelAndView singleMovie(Principal principal, @PathVariable int id) {
        ModelAndView m = new ModelAndView("movieInstance");

        Movie requestedMovie = movieRepository.findOne(id);
        Iterable<MovieRating> requestedMovieRating = movieRatingRepository.findAllByMovie(requestedMovie);
        MovieRating newRating = new MovieRating();


        // get Related Movies
        Iterable<Movie> relatedMovies;
        if (requestedMovie.getSeries().getId() == -1) {
            relatedMovies = null;
        } else {
            relatedMovies = movieRepository.findAllBySeriesAndIdIsNot(requestedMovie.getSeries(), id);
        }

        // get Notifications from User, if logged in
        Iterable<NotificationUserRelation> notifications;
        NotificationHelper notificationHelper = new NotificationHelper(userRepository, notificationUserRelationRepository, notificationTypeRepository);
        notifications = notificationHelper.getNotificationsFromUser(principal);

        //getAllCommentsForMovie
        Iterable<MovieComments> comments = movieCommentsRepository.findAllByMovieOrderByCreationDateDesc(requestedMovie);

        m.addObject("wastedMinutes", MovieDbUtils.getCalculatedMovieTime(movieRepository)[1]);
        m.addObject("wastedHours", MovieDbUtils.getCalculatedMovieTime(movieRepository)[0]);
        m.addObject("movie", requestedMovie);
        m.addObject("ratings", requestedMovieRating);
        m.addObject("newRating", newRating);
        m.addObject("notifications", notifications);
        m.addObject("comments", comments);
        m.addObject("newComment", new MovieComments());
        m.addObject("relatedMovies", relatedMovies);

        return m;
    }


    // Add Rating
    @PostMapping(value = "/actions/addRatingAction/{id}")
    private String addRating(MovieRating movieRating, Principal principal, @PathVariable int id) {

        Movie currentMovie = movieRepository.findById(id);
        User currentUser = userRepository.findByUsernameEquals(principal.getName());

        MovieRating ratingByUserAndMovie = movieRatingRepository.findByUserAndAndMovie(currentUser, currentMovie);

        //if movie is from the old world, we wont rate it.
        if (currentMovie.getId() <= 70) {
            return "redirect:/m/movies/" + id + "?oldworld=true";
        }

        if (ratingByUserAndMovie == null) {
            // Generate MovieRatingEntry
            movieRating.setMovie(currentMovie);
            movieRating.setUser(currentUser);
            movieRating.setRating(movieRating.getRating());
            movieRatingRepository.save(movieRating);
        } else {
            ratingByUserAndMovie.setRating(movieRating.getRating());
            movieRatingRepository.save(ratingByUserAndMovie);
        }
        recalculateMovieRating(currentMovie, id);
        return "redirect:/m/movies/" + id + "?successfullyrated=true";
    }

    // Calculate new Rating
    private void recalculateMovieRating(Movie currentMovie, int id) {

        double ratingSum = movieRatingRepository.getCalculatedMovieRatingForMovie(currentMovie);
        double ratingCount = movieRatingRepository.getCountOfMovieRatingsForMovie(currentMovie);
        double finalRating = 0;
        if (ratingCount > 1) {
            finalRating = ratingSum / ratingCount;
        }
        if (ratingCount == 1) {
            finalRating = ratingSum;
        }
        movieRepository.updateMovieRating(finalRating, id);
    }

    // Add Comment
    @PostMapping(value = "/actions/addCommentAction/{id}")
    private String saveComment(MovieComments newComment, Principal principal, @PathVariable int id) {
        Movie currentMovie = movieRepository.findById(id);
        User currentUser = userRepository.findByUsernameEquals(principal.getName());


        if (StringUtils.isNotEmpty(newComment.getComment()) && currentMovie != null) {
            newComment.setUser(currentUser);
            newComment.setComment(newComment.getComment());
            newComment.setCreationDate(new Date());
            newComment.setMovie(currentMovie);
            movieCommentsRepository.save(newComment);
            return "redirect:/m/movies/" + id;
        } else {
            return "redirect:/m/movies/" + id + "?commentfail=true";
        }
    }

    @RequestMapping(value = "/actions/deleteCommentAction/{movieId}/{commentId}")
    private String deleteComment(Principal principal, @PathVariable int movieId, @PathVariable int commentId) {
        MovieComments currentComment = movieCommentsRepository.findOne(commentId);
        User currentUser = userRepository.findByUsernameEquals(principal.getName());

        String currentUsername = currentUser.getUsername();
        String commentUsername = currentComment.getUser().getUsername();

        if (commentUsername.equals(currentUsername)) {
            currentComment.setMovie(null);
            currentComment.setUser(null);
            movieCommentsRepository.delete(currentComment);
            return "redirect:/m/movies/" + movieId;
        } else {
            return "redirect:/m/movies/" + movieId + "?commentfail=true";
        }
    }
}
