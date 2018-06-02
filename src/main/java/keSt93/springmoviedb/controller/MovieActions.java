package keSt93.springmoviedb.controller;

import keSt93.springmoviedb.entities.Movie;
import keSt93.springmoviedb.entities.User;
import keSt93.springmoviedb.repository.*;
import keSt93.springmoviedb.utils.DataUriHelper;
import keSt93.springmoviedb.utils.ImdbApi;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.net.URL;
import java.security.Principal;

@Controller
public class MovieActions {

    private MovieRepository movieRepository;
    private UserRepository userRepository;


    public MovieActions(
            MovieRepository movieRepository,
            UserRepository userRepository
    ) {
        this.movieRepository = movieRepository;
        this.userRepository = userRepository;
    }

    // EditMovie
    @PostMapping(value = "/actions/editMovieAction/{id}")
    public String editMovie(Movie editedMovie, @PathVariable int id) {

        Movie currentMovie = movieRepository.findById(id);

        if (!currentMovie.getTitle().equals(editedMovie.getTitle())) {
            currentMovie.setTitle(editedMovie.getTitle());
        }

        /*
        if(currentMovie.getReleaseDate() != editedMovie.getReleaseDate()) {
            currentMovie.setReleaseDate(editedMovie.getReleaseDate());
        }
        */

        if (!currentMovie.getCoverImage().equals(editedMovie.getCoverImage())) {
            try {
                URL url = new URL(editedMovie.getCoverImage());
                currentMovie.setCoverImage(DataUriHelper.getDataURIForURL(url).toString());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        if (currentMovie.getSeries() != editedMovie.getSeries()) {
            currentMovie.setSeries(editedMovie.getSeries());
        }
        if (currentMovie.getGenre() != editedMovie.getGenre()) {
            currentMovie.setGenre(editedMovie.getGenre());
        }
        if (currentMovie.getLength() != editedMovie.getLength()) {
            // #26 - movie length cannot be negative anymore
            if(editedMovie.getLength() < 1) {
                return "redirect:/m/movies/" + id + "?editingerror=true";
            } else {
                currentMovie.setLength(editedMovie.getLength());
            }
        }
        if (!currentMovie.getPlot().equals(editedMovie.getPlot())) {
            currentMovie.setPlot(editedMovie.getPlot());
        }

        movieRepository.save(currentMovie);

        return "redirect:/m/movies/" + id + "?successfullyedited=true";
    }

    // Replace Movie
    @PostMapping(value = "/actions/replaceMovieAction/{id}")
    public String replaceMovie(Movie replaceMovie, Principal principal, @PathVariable int id) {
        Movie currentMovie = movieRepository.findById(id);
        User user = userRepository.findByUsernameEquals(principal.getName());

        String movieName = replaceMovie.getTitle();
        if (movieName.startsWith("imdbID: ")) {
            movieName = movieName.replace("imdbID: ", "");
            replaceMovie = ImdbApi.getMovieByImdbId(movieName);
        } else {
            replaceMovie = ImdbApi.getMovieByName(movieName);
        }
        replaceMovie.setId(currentMovie.getId());
        replaceMovie.setRegisteredDate(currentMovie.getRegisteredDate());
        replaceMovie.setRating(currentMovie.getRating());
        replaceMovie.setSeries(currentMovie.getSeries());
        replaceMovie.setGenre(currentMovie.getGenre());
        replaceMovie.setUser(user);

        currentMovie = replaceMovie;
        movieRepository.save(currentMovie);
        return "redirect:/m/movies/" + id;
    }

    @PostMapping(value = "/actions/deleteMovieAction/{id}")
    public String deleteMovie(@PathVariable int id) {
        Movie currentMovie = movieRepository.findById(id);
        currentMovie.setGenre(null);
        currentMovie.setSeries(null);
        currentMovie.setUser(null);
        movieRepository.delete(currentMovie);

        return "redirect:/m/";
    }

}
