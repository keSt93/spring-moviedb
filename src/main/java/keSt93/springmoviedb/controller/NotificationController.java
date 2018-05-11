package keSt93.springmoviedb.controller;


import keSt93.springmoviedb.entities.Movie;
import keSt93.springmoviedb.entities.NotificationUserRelation;
import keSt93.springmoviedb.entities.Series;
import keSt93.springmoviedb.repository.*;
import keSt93.springmoviedb.utils.NotificationHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.persistence.EntityManager;
import java.security.Principal;

/**
 * Created by stein on 14.02.2018.
 */

@Controller
public class NotificationController {

    @Autowired
    NotificationUserRelationRepository notificationUserRelationRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    NotificationTypeRepository notificationTypeRepository;
    @Autowired
    MovieRepository movieRepository;

    @GetMapping("/notifications/markasread/{id}")
    public String index(@PathVariable int id) {
        NotificationUserRelation notification = notificationUserRelationRepository.findOne(id);

        if(!notification.getSeen()) {
            notification.setSeen(true);
            notificationUserRelationRepository.save(notification);
        }
        return "notification_markAsRead";
    }

    @GetMapping("/notifications/new")
    public ModelAndView notificationList(Principal principal) {
        ModelAndView notificationList = new ModelAndView("notificationList");
        NotificationHelper notificationHelper = new NotificationHelper(userRepository, notificationUserRelationRepository, notificationTypeRepository);

        // get Notifications from User, if logged in
        Iterable<NotificationUserRelation> notifications;
        notifications = notificationHelper.getNotificationsFromUser(principal);

        // Calculate wasted Time for Footer
        int wastedMinutes = movieRepository.getTotalWastedMinutes();
        int wastedHours = wastedMinutes / 60;
        wastedMinutes = wastedMinutes % 60;

        notificationList.addObject("wastedMinutes", wastedMinutes);
        notificationList.addObject("wastedHours", wastedHours);
        notificationList.addObject("notifications", notifications);
        return notificationList;
    }

}
