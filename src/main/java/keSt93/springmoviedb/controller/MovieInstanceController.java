package keSt93.springmoviedb.controller;


import keSt93.springmoviedb.entities.Movie;
import keSt93.springmoviedb.entities.MovieRating;
import keSt93.springmoviedb.repository.MovieRatingRepository;
import keSt93.springmoviedb.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

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


    @GetMapping("/movies/{id}")
    public ModelAndView singleMovie(@PathVariable int id) {
        ModelAndView m = new ModelAndView("movieInstance");

        Movie requestedMovie = movieRepository.findOne(id);
        Iterable<MovieRating> requestedMovieRating = movieRatingRepository.findAllByMovie(requestedMovie);

        m.addObject("movie", requestedMovie);
        m.addObject("ratings", requestedMovieRating);

        return m;
    }

}
