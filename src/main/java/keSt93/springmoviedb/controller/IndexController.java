package keSt93.springmoviedb.controller;


import keSt93.springmoviedb.entities.Genre;
import keSt93.springmoviedb.entities.Movie;
import keSt93.springmoviedb.entities.Series;
import keSt93.springmoviedb.repository.GenreRepository;
import keSt93.springmoviedb.repository.MovieRepository;
import keSt93.springmoviedb.repository.SeriesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.management.Query;
import javax.persistence.EntityManager;

/**
 * Created by stein on 14.02.2018.
 */

@Controller
@RequestMapping("/")
public class IndexController {

    @Autowired
    MovieRepository movieRepository;

    @Autowired
    SeriesRepository seriesRepository;

    @Autowired
    GenreRepository genreRepository;

    @Autowired
    EntityManager em;

    @GetMapping("/")
    public ModelAndView index() {
        ModelAndView m = new ModelAndView("index");

        Iterable<Movie> recentMovies = movieRepository.findFirst8ByOrderByRegisteredDateDesc();
        Iterable<Movie> bestRatedMovies = movieRepository.findFirst8ByOrderByRatingDesc();

        int wastedMinutes = movieRepository.getTotalWastedMinutes();
        int wastedHours = wastedMinutes / 60;
        wastedMinutes = wastedMinutes % 60;

        int totalMovies = movieRepository.getTotalMovies();

        m.addObject("recentMovies",recentMovies);
        m.addObject("bestRatedMovies",bestRatedMovies);
        m.addObject("wastedMinutes", wastedMinutes);
        m.addObject("wastedHours", wastedHours);
        m.addObject("totalMovies", totalMovies);
        return m;
    }

}
