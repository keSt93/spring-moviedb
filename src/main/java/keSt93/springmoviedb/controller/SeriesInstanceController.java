package keSt93.springmoviedb.controller;


import keSt93.springmoviedb.entities.*;
import keSt93.springmoviedb.repository.*;
import keSt93.springmoviedb.utils.MovieDbUtils;
import keSt93.springmoviedb.utils.NotificationHelper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;

@Controller
@RequestMapping("/")
public class SeriesInstanceController {

    private MovieRepository movieRepository;
    private UserRepository userRepository;
    private NotificationUserRelationRepository notificationUserRelationRepository;
    private NotificationTypeRepository notificationTypeRepository;
    private SeriesRepository seriesRepository;

    public SeriesInstanceController(
            MovieRepository movieRepository,
            UserRepository userRepository,
            NotificationUserRelationRepository notificationUserRelationRepository,
            NotificationTypeRepository notificationTypeRepository,
            SeriesRepository seriesRepository
    ) {
        this.movieRepository = movieRepository;
        this.userRepository = userRepository;
        this.notificationUserRelationRepository = notificationUserRelationRepository;
        this.notificationTypeRepository = notificationTypeRepository;
        this.seriesRepository = seriesRepository;
    }

    // Series Detail Page
    @GetMapping("/m/series/{id}")
    public ModelAndView singleMovie(Principal principal, @PathVariable int id) {
        ModelAndView m = new ModelAndView("seriesInstance");

        Series currentSeries = seriesRepository.findOne(id);
        Iterable<Movie> moviesFromSeries = movieRepository.findAllBySeries(currentSeries);

        // get Notifications from User, if logged in
        Iterable<NotificationUserRelation> notifications;
        NotificationHelper notificationHelper = new NotificationHelper(userRepository, notificationUserRelationRepository, notificationTypeRepository);
        notifications = notificationHelper.getNotificationsFromUser(principal);

        // Get a few series stats
        int wastedSeriesTime = movieRepository.getTotalSeriesWastedTime(currentSeries);
        int totalMoviesForSeries = movieRepository.countAllBySeriesEquals(currentSeries);
        double averageSeriesRating = movieRepository.getAverageSeriesRating(currentSeries);

        m.addObject("wastedMinutes", MovieDbUtils.getCalculatedMovieTime(movieRepository)[1]);
        m.addObject("wastedHours", MovieDbUtils.getCalculatedMovieTime(movieRepository)[0]);
        m.addObject("wastedSeriesTime", wastedSeriesTime);
        m.addObject("totalMoviesForSeries", totalMoviesForSeries);
        m.addObject("averageSeriesRating", averageSeriesRating);
        m.addObject("series", currentSeries);
        m.addObject("moviesFromSeries", moviesFromSeries);
        m.addObject("notifications", notifications);

        return m;
    }
}
