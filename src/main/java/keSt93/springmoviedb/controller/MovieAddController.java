package keSt93.springmoviedb.controller;

import keSt93.springmoviedb.entities.*;
import keSt93.springmoviedb.repository.*;
import keSt93.springmoviedb.utils.DataUriHelper;
import keSt93.springmoviedb.utils.ImdbApi;
import keSt93.springmoviedb.utils.MovieDbUtils;
import keSt93.springmoviedb.utils.NotificationHelper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import java.net.URL;
import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
public class MovieAddController {

    private MovieRepository movieRepository;
    private GenreRepository genreRepository;
    private SeriesRepository seriesRepository;
    private NotificationTypeRepository notificationTypeRepository;
    private NotificationUserRelationRepository notificationUserRelationRepository;
    private UserRepository userRepository;

    public MovieAddController(
            MovieRepository movieRepository,
            GenreRepository genreRepository,
            SeriesRepository seriesRepository,
            NotificationTypeRepository notificationTypeRepository,
            NotificationUserRelationRepository notificationUserRelationRepository,
            UserRepository userRepository
    ) {
        this.movieRepository = movieRepository;
        this.genreRepository = genreRepository;
        this.seriesRepository = seriesRepository;
        this.notificationTypeRepository = notificationTypeRepository;
        this.notificationUserRelationRepository = notificationUserRelationRepository;
        this.userRepository = userRepository;
    }


    // Add movie page
    @GetMapping(value = "/m/addmovie")
    public ModelAndView showMovies(Principal principal) {
        ModelAndView modelAndView = new ModelAndView("movieAdd");

        // Get all Genres and Series for Dropdown
        Iterable<Genre> genres = genreRepository.findAll();
        Iterable<Series> series = seriesRepository.findAll();

        // get Notifications from User, if logged in
        Iterable<NotificationUserRelation> notifications;
        NotificationHelper notificationHelper = new NotificationHelper(userRepository, notificationUserRelationRepository, notificationTypeRepository);
        notifications = notificationHelper.getNotificationsFromUser(principal);

        modelAndView.addObject("wastedMinutes", MovieDbUtils.getCalculatedMovieTime(movieRepository)[1]);
        modelAndView.addObject("wastedHours", MovieDbUtils.getCalculatedMovieTime(movieRepository)[0]);
        modelAndView.addObject("movie", new Movie());
        modelAndView.addObject("genres_", genres);
        modelAndView.addObject("series_", series);
        modelAndView.addObject("notifications", notifications);


        return modelAndView;
    }

    // Add Movie Action
    @PostMapping(value = "/actions/addMovieAction")
    private String addMovie(Movie movie, Principal currentUser) {
        ImdbApi imdbApi = new ImdbApi(movie.getTitle());
        NotificationHelper notificationHelper = new NotificationHelper(userRepository, notificationUserRelationRepository, notificationTypeRepository);

        User user = userRepository.findByUsernameEquals(currentUser.getName());

        movie.setTitle(imdbApi.getTitle());

        movie.setRegisteredDate(new Date());
        movie.setRating(0);
        movie.setSeries(movie.getSeries());
        movie.setGenre(movie.getGenre());
        movie.setLength(imdbApi.getLength());
        movie.setPlot(imdbApi.getPlot());
        movie.setUser(user);

        try {
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
            Date releasedate = formatter.parse(imdbApi.getReleased().replace(" ", "-"));
            movie.setReleaseDate(releasedate);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        try {
            movie.setCoverImage(DataUriHelper.getDataURIForURL(new URL(imdbApi.getCoverUrl())).toString());
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        movieRepository.save(movie);

        String notificationUrl = "/m/movies/" + movie.getId();
        String notificationImage = movie.getCoverImage();
        StringBuilder notificationText = new StringBuilder();
        notificationText
                .append(user.getUsername())
                .append(" hat gerade ")
                .append(movie.getTitle())
                .append(" hinzugefügt.")
                .append(" Schau doch mal rein!");

        // notifications for everyone not involved in this process
        notificationHelper.createNotification(
                user,
                notificationText.toString(),
                notificationUrl,
                notificationImage);
        return "redirect:/m/";
    }
}
