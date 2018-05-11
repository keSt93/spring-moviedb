package keSt93.springmoviedb.controller;

import keSt93.springmoviedb.entities.Movie;
import keSt93.springmoviedb.entities.MovieRating;
import keSt93.springmoviedb.entities.NotificationUserRelation;
import keSt93.springmoviedb.models.PageModel;
import keSt93.springmoviedb.repository.*;
import keSt93.springmoviedb.utils.NotificationHelper;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller
public class MovieListController {

    @Autowired
    private MovieRepository movieRepository;
    @Autowired
    private MovieRatingRepository movieRatingRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    NotificationUserRelationRepository notificationUserRelationRepository;
    @Autowired
    NotificationTypeRepository notificationTypeRepository;



    // Settings for pagination
    private static final int BUTTONS_TO_SHOW = 51;
    private static final int INITIAL_PAGE = 0;
    private static final int INITIAL_PAGE_SIZE = 6;

    // Movie List
    @RequestMapping("/movies")
    public ModelAndView allMovies(Principal principal,
                                  @RequestParam("pageSize") Optional<Integer> pageSize,
                                  @RequestParam("page") Optional<Integer> page){
        ModelAndView modelAndView = new ModelAndView("movieList");

        int evalPageSize = pageSize.orElse(INITIAL_PAGE_SIZE);
        int evalPage = (page.orElse(0) < 1) ? INITIAL_PAGE : page.get() - 1;

        Page<Movie> movieList = movieRepository.findAllByOrderByIdDesc(new PageRequest(evalPage, evalPageSize));
        PageModel pageModel = new PageModel(movieList.getTotalPages(),movieList.getNumber(),BUTTONS_TO_SHOW );

        // get Notifications from User, if logged in
        Iterable<NotificationUserRelation> notifications;
        NotificationHelper notificationHelper = new NotificationHelper(userRepository, notificationUserRelationRepository, notificationTypeRepository);
        notifications = notificationHelper.getNotificationsFromUser(principal);

        // Calculate wasted Time for Footer
        int wastedMinutes = movieRepository.getTotalWastedMinutes();
        int wastedHours = wastedMinutes / 60;
        wastedMinutes = wastedMinutes % 60;

        modelAndView.addObject("wastedMinutes", wastedMinutes);
        modelAndView.addObject("wastedHours", wastedHours);
        modelAndView.addObject("movies", movieList);
        modelAndView.addObject("selectedPageSize", evalPageSize);
        modelAndView.addObject("pager", pageModel);
        modelAndView.addObject("notifications", notifications);

        return modelAndView;
    }

}
