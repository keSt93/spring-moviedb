package keSt93.springmoviedb.controller;


import keSt93.springmoviedb.entities.*;
import keSt93.springmoviedb.repository.*;
import keSt93.springmoviedb.utils.NotificationHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.management.Query;
import javax.persistence.EntityManager;
import java.security.Principal;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

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
    UserRepository userRepository;

    @Autowired
    NotificationUserRelationRepository notificationUserRelationRepository;

    @Autowired
    NotificationTypeRepository notificationTypeRepository;

    @Autowired
    EntityManager em;

    @GetMapping("/")
    public ModelAndView index(Principal principal) {
        ModelAndView m = new ModelAndView("index");

        // Movie Lists
        Iterable<Movie> recentMovies = movieRepository.findFirst15ByOrderByRegisteredDateDesc();
        Iterable<Movie> bestRatedMovies = movieRepository.findFirst11ByOrderByRatingDesc();

        // Calculate wasted Time
        int wastedMinutes = movieRepository.getTotalWastedMinutes();
        int wastedHours = wastedMinutes / 60;
        wastedMinutes = wastedMinutes % 60;

        // Get all Movies count
        int totalMovies = movieRepository.getTotalMovies();

        // Find Last Two Series
        Iterable<Series> seriesList = seriesRepository.findFirst4ByOrderByLastUsedDesc();

        // get Notifications from User, if logged in
        Iterable<NotificationUserRelation> notifications;
        NotificationHelper notificationHelper = new NotificationHelper(userRepository, notificationUserRelationRepository, notificationTypeRepository);
        notifications = notificationHelper.getNotificationsFromUser(principal);


        m.addObject("recentMovies",recentMovies);
        m.addObject("bestRatedMovies",bestRatedMovies);
        m.addObject("seriesList",seriesList);
        m.addObject("wastedMinutes", wastedMinutes);
        m.addObject("wastedHours", wastedHours);
        m.addObject("totalMovies", totalMovies);
        m.addObject("notifications", notifications);
        return m;
    }

}
