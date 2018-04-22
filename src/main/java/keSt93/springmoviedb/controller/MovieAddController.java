package keSt93.springmoviedb.controller;

import keSt93.springmoviedb.entities.Genre;
import keSt93.springmoviedb.entities.Movie;
import keSt93.springmoviedb.entities.Series;
import keSt93.springmoviedb.repository.GenreRepository;
import keSt93.springmoviedb.repository.MovieRepository;
import keSt93.springmoviedb.repository.SeriesRepository;
import keSt93.springmoviedb.utils.DataUriHelper;
import keSt93.springmoviedb.utils.ImdbApi;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import java.net.URL;
import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@Controller
public class MovieAddController {

    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private SeriesRepository seriesRepository;

    @Autowired
    private MovieRepository movieRepository;

    @GetMapping(value = "/addmovie")
    public ModelAndView showView() {
        ModelAndView modelAndView = new ModelAndView("movieAdd");

        Iterable<Genre> genres = genreRepository.findAll();
        Iterable<Series> series = seriesRepository.findAll();

        modelAndView.addObject("movie", new Movie());
        modelAndView.addObject("genres_", genres);
        modelAndView.addObject("series_", series);


        return modelAndView;
    }

    @PostMapping(value = "/addMovieAction")
    private String saveView(Movie movie, Principal currentUser)  {
        ImdbApi imdbApi = new ImdbApi(movie.getTitle());
        DataUriHelper dataUriHelper = new DataUriHelper();

        movie.setTitle(imdbApi.getTitle());

        movie.setRegisteredDate(new Date());
        movie.setRating(0);
        movie.setSeries(movie.getSeries());
        movie.setGenre(movie.getGenre());
        movie.setLength(imdbApi.getLength());
        movie.setPlot(imdbApi.getPlot());

        try {
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
            Date releasedate = formatter.parse(imdbApi.getReleased().replace(" ", "-"));
            movie.setReleaseDate(releasedate);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        try {
            movie.setCoverImage(dataUriHelper.getDataURIForURL(new URL(imdbApi.getCoverUrl())).toString());
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        movieRepository.save(movie);

        return "redirect:/";
    }
}
