package keSt93.springmoviedb.controller;


import keSt93.springmoviedb.entities.*;
import keSt93.springmoviedb.repository.*;
import keSt93.springmoviedb.utils.MovieDbUtils;
import keSt93.springmoviedb.utils.NotificationHelper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;

@Controller
public class StartPageController {

    private MovieRepository movieRepository;
    private SeriesRepository seriesRepository;
    private UserRepository userRepository;
    private NotificationUserRelationRepository notificationUserRelationRepository;
    private NotificationTypeRepository notificationTypeRepository;


    public StartPageController(
            MovieRepository movieRepository,
            SeriesRepository seriesRepository,
            UserRepository userRepository,
            NotificationUserRelationRepository notificationUserRelationRepository,
            NotificationTypeRepository notificationTypeRepository
    ) {
        this.movieRepository = movieRepository;
        this.seriesRepository = seriesRepository;
        this.userRepository = userRepository;
        this.notificationUserRelationRepository = notificationUserRelationRepository;
        this.notificationTypeRepository = notificationTypeRepository;
    }

    @GetMapping("/m/")
    public ModelAndView index(Principal principal) {
        ModelAndView m = new ModelAndView("index");

        // Movie Lists
        Iterable<Movie> recentMovies = movieRepository.findFirst11ByOrderByRegisteredDateDesc();
        Iterable<Movie> bestRatedMovies = movieRepository.findFirst6ByOrderByRatingDesc();

        // Get all Movies count
        int totalMovies = movieRepository.countAllBy();

        // Find Last Two Series
        Iterable<Series> seriesList = seriesRepository.findFirst3ByOrderByLastUsedDesc();

        // Get Notifications from User, if logged in
        Iterable<NotificationUserRelation> notifications;
        NotificationHelper notificationHelper = new NotificationHelper(userRepository, notificationUserRelationRepository, notificationTypeRepository);
        notifications = notificationHelper.getNotificationsFromUser(principal);

        // Add Objects to Model
        m.addObject("wastedHours", MovieDbUtils.getCalculatedMovieTime(movieRepository)[0]);
        m.addObject("wastedMinutes", MovieDbUtils.getCalculatedMovieTime(movieRepository)[1]);
        m.addObject("recentMovies", recentMovies);
        m.addObject("bestRatedMovies", bestRatedMovies);
        m.addObject("seriesList", seriesList);
        m.addObject("totalMovies", totalMovies);
        m.addObject("notifications", notifications);
        return m;
    }

}
