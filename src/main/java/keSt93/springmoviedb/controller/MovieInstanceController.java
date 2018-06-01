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

import javax.swing.text.DateFormatter;
import javax.transaction.Transactional;
import java.net.URL;
import java.security.Principal;
import java.text.DateFormat;
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
    private GenreRepository genreRepository;
    private SeriesRepository seriesRepository;

    public MovieInstanceController(
            MovieRepository movieRepository,
            MovieRatingRepository movieRatingRepository,
            UserRepository userRepository,
            NotificationUserRelationRepository notificationUserRelationRepository,
            NotificationTypeRepository notificationTypeRepository,
            MovieCommentsRepository movieCommentsRepository,
            GenreRepository genreRepository,
            SeriesRepository seriesRepository
    ) {
        this.movieRepository = movieRepository;
        this.movieRatingRepository = movieRatingRepository;
        this.userRepository = userRepository;
        this.notificationUserRelationRepository = notificationUserRelationRepository;
        this.notificationTypeRepository = notificationTypeRepository;
        this.movieCommentsRepository = movieCommentsRepository;
        this.genreRepository = genreRepository;
        this.seriesRepository = seriesRepository;
    }


    // Movie Detail Page
    @GetMapping("/m/movies/{id}")
    public ModelAndView singleMovie(Principal principal, @PathVariable int id) {
        ModelAndView m = new ModelAndView("movieInstance");

        Movie requestedMovie = movieRepository.findOne(id);
        Iterable<MovieRating> requestedMovieRating = movieRatingRepository.findAllByMovie(requestedMovie);
        MovieRating newRating = new MovieRating();

        // Get all Genres and Series for Dropdown
        Iterable<Genre> genres = genreRepository.findAll();
        Iterable<Series> series = seriesRepository.findAll();

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
        m.addObject("genres_", genres);
        m.addObject("series_", series);
        m.addObject("editedMovie", new Movie());
        m.addObject("replaceMovie", new Movie());

        return m;
    }
}
