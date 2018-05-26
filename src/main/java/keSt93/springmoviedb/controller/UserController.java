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

    @Autowired
    MovieCommentsRepository movieCommentsRepository;

    @GetMapping("/m/user/current")
    public ModelAndView index(Principal principal) {
        ModelAndView m = new ModelAndView("userPage");
        User currentUser = userRepository.findByUsernameEquals(principal.getName());

        String avgUserRatingString = movieRatingRepository.getAverageRatingForUser(currentUser);
        double avgUserRating = 0;
        if (avgUserRatingString != null) {
            avgUserRating = Double.parseDouble(avgUserRatingString);
        }

        // get Notifications from User, if logged in
        Iterable<NotificationUserRelation> notifications;
        NotificationHelper notificationHelper = new NotificationHelper(userRepository, notificationUserRelationRepository, notificationTypeRepository);
        notifications = notificationHelper.getNotificationsFromUser(principal);

        // Calculate wasted Time for Footer
        int wastedMinutes = movieRepository.getTotalWastedMinutes();
        int wastedHours = wastedMinutes / 60;
        wastedMinutes = wastedMinutes % 60;

        Iterable<MovieComments> userComments = movieCommentsRepository.findFirst5ByUserOrderByCreationDateDesc(currentUser);
        Iterable<MovieRating> movieRatings = movieRatingRepository.findFirst5ByUserOrderByIdDesc(currentUser);

        m.addObject("wastedMinutes", wastedMinutes);
        m.addObject("wastedHours", wastedHours);
        m.addObject("currentUser", currentUser);
        m.addObject("avgUserRating", avgUserRating);
        m.addObject("notifications", notifications);
        m.addObject("userComments", userComments);
        m.addObject("movieRatings", movieRatings);
        return m;
    }

}
