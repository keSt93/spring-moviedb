package keSt93.springmoviedb.controller;


import keSt93.springmoviedb.entities.*;
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
public class UserController {

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
    MovieRatingRepository movieRatingRepository;

    @GetMapping("/user/current")
    public ModelAndView index(Principal principal) {
        ModelAndView m = new ModelAndView("userPage");
        User currentUser = userRepository.findByUsernameEquals(principal.getName());

        int avgUserRating = movieRatingRepository.getAverageRatingForUser(currentUser);

        Iterable<NotificationUserRelation> top5notifications = notificationUserRelationRepository.findTop5ByUserOrderByIdDesc(currentUser);

        // get Notifications from User, if logged in
        Iterable<NotificationUserRelation> notifications;
        NotificationHelper notificationHelper = new NotificationHelper(userRepository, notificationUserRelationRepository, notificationTypeRepository);
        notifications = notificationHelper.getNotificationsFromUser(principal);

        // Calculate wasted Time for Footer
        int wastedMinutes = movieRepository.getTotalWastedMinutes();
        int wastedHours = wastedMinutes / 60;
        wastedMinutes = wastedMinutes % 60;

        m.addObject("wastedMinutes", wastedMinutes);
        m.addObject("wastedHours", wastedHours);
        m.addObject("currentUser", currentUser);
        m.addObject("avgUserRating", avgUserRating);
        m.addObject("notifications", notifications);
        m.addObject("top5notifications", top5notifications);
        return m;
    }

}
