package keSt93.springmoviedb.controller;

import keSt93.springmoviedb.entities.Movie;
import keSt93.springmoviedb.repository.MovieRepository;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.Date;
import java.util.Optional;

@Controller
public class MovieListController {

    @Autowired
    private MovieRepository movieRepository;

    @RequestMapping("/movies")
    public ModelAndView allMovies(){
        ModelAndView modelAndView = new ModelAndView("movieList");

        Iterable<Movie> movies = movieRepository.findAll();
        modelAndView.addObject("movies", movies);
        return modelAndView;
    }

}
