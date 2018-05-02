package keSt93.springmoviedb.controller;


import keSt93.springmoviedb.entities.*;
import keSt93.springmoviedb.repository.*;
import keSt93.springmoviedb.utils.NotificationHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;

/**
 * Created by stein on 14.02.2018.
 */

@Controller
@RequestMapping("/")
public class SeriesInstanceController {

    @Autowired
    MovieRepository movieRepository;
    @Autowired
    MovieRatingRepository movieRatingRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    NotificationUserRelationRepository notificationUserRelationRepository;
    @Autowired
    NotificationTypeRepository notificationTypeRepository;
    @Autowired
    SeriesRepository seriesRepository;

    @GetMapping("/series/{id}")
    public ModelAndView singleMovie(Principal principal, @PathVariable int id) {
        ModelAndView m = new ModelAndView("seriesInstance");

        Series currentSeries = seriesRepository.findOne(id);
        Iterable<Movie> moviesFromSeries = movieRepository.findAllBySeries(currentSeries);

        // get Notifications from User, if logged in
        Iterable<NotificationUserRelation> notifications;
        NotificationHelper notificationHelper = new NotificationHelper(userRepository, notificationUserRelationRepository, notificationTypeRepository);
        notifications = notificationHelper.getNotificationsFromUser(principal);

        m.addObject("series", currentSeries);
        m.addObject("moviesFromSeries", moviesFromSeries);
        m.addObject("notifications", notifications);

        return m;
    }
}
