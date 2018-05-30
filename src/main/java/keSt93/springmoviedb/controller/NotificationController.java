package keSt93.springmoviedb.controller;


import keSt93.springmoviedb.entities.NotificationUserRelation;
import keSt93.springmoviedb.repository.*;
import keSt93.springmoviedb.utils.MovieDbUtils;
import keSt93.springmoviedb.utils.NotificationHelper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;

@Controller
public class NotificationController {

    private NotificationUserRelationRepository notificationUserRelationRepository;
    private UserRepository userRepository;
    private NotificationTypeRepository notificationTypeRepository;
    private MovieRepository movieRepository;

    public NotificationController(
            NotificationUserRelationRepository notificationUserRelationRepository,
            UserRepository userRepository,
            NotificationTypeRepository notificationTypeRepository,
            MovieRepository movieRepository
    ) {
        this.notificationUserRelationRepository = notificationUserRelationRepository;
        this.userRepository = userRepository;
        this.notificationTypeRepository = notificationTypeRepository;
        this.movieRepository = movieRepository;
    }

    @GetMapping("/m/notifications/markasread/{id}")
    public String index(@PathVariable int id) {
        NotificationUserRelation notification = notificationUserRelationRepository.findOne(id);

        if (!notification.getSeen()) {
            notification.setSeen(true);
            notificationUserRelationRepository.save(notification);
        }
        return "notification_markAsRead";
    }

    @GetMapping("/m/notifications/new")
    public ModelAndView notificationList(Principal principal) {
        ModelAndView notificationList = new ModelAndView("notificationList");
        NotificationHelper notificationHelper = new NotificationHelper(userRepository, notificationUserRelationRepository, notificationTypeRepository);

        // get Notifications from User, if logged in
        Iterable<NotificationUserRelation> notifications;
        notifications = notificationHelper.getNotificationsFromUser(principal);

        notificationList.addObject("wastedMinutes", MovieDbUtils.getCalculatedMovieTime(movieRepository)[1]);
        notificationList.addObject("wastedHours", MovieDbUtils.getCalculatedMovieTime(movieRepository)[1]);
        notificationList.addObject("notifications", notifications);
        return notificationList;
    }

}
