package keSt93.springmoviedb.controller;

import keSt93.springmoviedb.entities.Movie;
import keSt93.springmoviedb.entities.MovieRating;
import keSt93.springmoviedb.entities.User;
import keSt93.springmoviedb.repository.MovieRatingRepository;
import keSt93.springmoviedb.repository.MovieRepository;
import keSt93.springmoviedb.repository.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;

@Controller
public class RatingController {

    private MovieRepository movieRepository;
    private UserRepository userRepository;
    private MovieRatingRepository movieRatingRepository;

    public RatingController(
            MovieRepository movieRepository,
            UserRepository userRepository,
            MovieRatingRepository movieRatingRepository
    ) {
        this.movieRepository = movieRepository;
        this.userRepository = userRepository;
        this.movieRatingRepository = movieRatingRepository;
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

        // #25 - check if rating is a valid number
        if(movieRating.getRating() > 10 || movieRating.getRating() < 1) {
            return "redirect:/m/movies/" + id + "?ratingerror=true";
        }

        if (ratingByUserAndMovie == null) {
            movieRating.setRating(movieRating.getRating());
            movieRating.setMovie(currentMovie);
            movieRating.setUser(currentUser);
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

}
