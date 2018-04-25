package keSt93.springmoviedb.controller;


import keSt93.springmoviedb.entities.Movie;
import keSt93.springmoviedb.entities.MovieRating;
import keSt93.springmoviedb.entities.User;
import keSt93.springmoviedb.repository.MovieRatingRepository;
import keSt93.springmoviedb.repository.MovieRepository;
import keSt93.springmoviedb.repository.UserRepository;
import keSt93.springmoviedb.utils.DataUriHelper;
import keSt93.springmoviedb.utils.ImdbApi;
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
@RequestMapping("/")
public class MovieInstanceController {

    @Autowired
    MovieRepository movieRepository;
    @Autowired
    MovieRatingRepository movieRatingRepository;
    @Autowired
    UserRepository userRepository;


    @GetMapping("/movies/{id}")
    public ModelAndView singleMovie(@PathVariable int id) {
        ModelAndView m = new ModelAndView("movieInstance");

        Movie requestedMovie = movieRepository.findOne(id);
        Iterable<MovieRating> requestedMovieRating = movieRatingRepository.findAllByMovie(requestedMovie);
        MovieRating newRating = new MovieRating();

        m.addObject("movie", requestedMovie);
        m.addObject("ratings", requestedMovieRating);
        m.addObject("newRating", newRating);

        return m;
    }


    @PostMapping(value = "/addRatingAction/{id}")
    private String saveView(MovieRating movieRating, Principal principal,@PathVariable int id)  {

        Movie currentMovie = movieRepository.findById(id);
        User currentUser = userRepository.findByUsernameEquals(principal.getName());

        if(movieRatingRepository.findByUserAndAndMovie(currentUser, currentMovie) == null) {
            // Generate MovieRatingEntry
            movieRating.setMovie(currentMovie);
            movieRating.setUser(currentUser);
            movieRating.setRating(movieRating.getRating());
            movieRatingRepository.save(movieRating);

            // Calculate new Rating
            double ratingSum = movieRatingRepository.getCalculatedMovieRatingForMovie(currentMovie);
            double ratingCount = movieRatingRepository.getCountOfMovieRatingsForMovie(currentMovie);
            double finalRating = 0;
            if(ratingCount > 1) {
                finalRating = ratingSum / ratingCount;
            }
            if(ratingCount == 1) {
                finalRating = ratingSum;
            }
            movieRepository.updateMovieRating(finalRating, id);

            return "redirect:/movies/"+id+"?successfullyrated=true";
        } else {
            return "redirect:/movies/"+id+"?alreadyrated=true";
        }




    }

}
