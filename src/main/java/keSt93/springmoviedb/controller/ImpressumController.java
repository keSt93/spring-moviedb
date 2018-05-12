package keSt93.springmoviedb.controller;


import keSt93.springmoviedb.entities.Movie;
import keSt93.springmoviedb.entities.NotificationUserRelation;
import keSt93.springmoviedb.entities.Series;
import keSt93.springmoviedb.repository.*;
import keSt93.springmoviedb.utils.NotificationHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.persistence.EntityManager;
import java.security.Principal;

/**
 * Created by stein on 14.02.2018.
 */

@Controller
@RequestMapping("/")
public class ImpressumController {

    @Autowired
    MovieRepository movieRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    NotificationUserRelationRepository notificationUserRelationRepository;

    @Autowired
    NotificationTypeRepository notificationTypeRepository;


    @GetMapping("/impressum")
    public ModelAndView index(Principal principal) {
        ModelAndView m = new ModelAndView("impressum");

        // Calculate wasted Time
        int wastedMinutes = movieRepository.getTotalWastedMinutes();
        int wastedHours = wastedMinutes / 60;
        wastedMinutes = wastedMinutes % 60;

        // get Notifications from User, if logged in
        Iterable<NotificationUserRelation> notifications;
        NotificationHelper notificationHelper = new NotificationHelper(userRepository, notificationUserRelationRepository, notificationTypeRepository);
        notifications = notificationHelper.getNotificationsFromUser(principal);

        m.addObject("wastedMinutes", wastedMinutes);
        m.addObject("wastedHours", wastedHours);
        m.addObject("notifications", notifications);
        return m;
    }

}
