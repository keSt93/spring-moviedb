package keSt93.springmoviedb.controller;

import keSt93.springmoviedb.entities.Movie;
import keSt93.springmoviedb.entities.NotificationUserRelation;
import keSt93.springmoviedb.models.PageModel;
import keSt93.springmoviedb.repository.*;
import keSt93.springmoviedb.utils.MovieDbUtils;
import keSt93.springmoviedb.utils.NotificationHelper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.Optional;

@Controller
public class MovieListController {

    // Settings for pagination
    private static final int BUTTONS_TO_SHOW = 51;
    private static final int INITIAL_PAGE = 0;
    private static final int INITIAL_PAGE_SIZE = 6;

    private MovieRepository movieRepository;
    private UserRepository userRepository;
    private NotificationUserRelationRepository notificationUserRelationRepository;
    private NotificationTypeRepository notificationTypeRepository;

    public MovieListController(
            MovieRepository movieRepository,
            UserRepository userRepository,
            NotificationUserRelationRepository notificationUserRelationRepository,
            NotificationTypeRepository notificationTypeRepository
    ) {
        this.movieRepository = movieRepository;
        this.userRepository = userRepository;
        this.notificationUserRelationRepository = notificationUserRelationRepository;
        this.notificationTypeRepository = notificationTypeRepository;
    }


    // Movie List
    @RequestMapping("/m/movies")
    public ModelAndView allMovies(Principal principal,
                                  @RequestParam("pageSize") Optional<Integer> pageSize,
                                  @RequestParam("page") Optional<Integer> page
    ) {
        ModelAndView modelAndView = new ModelAndView("movieList");

        int evalPageSize = pageSize.orElse(INITIAL_PAGE_SIZE);
        int evalPage = (page.orElse(0) < 1) ? INITIAL_PAGE : page.get() - 1;

        Page<Movie> movieList = movieRepository.findAllByOrderByIdDesc(new PageRequest(evalPage, evalPageSize));
        PageModel pageModel = new PageModel(movieList.getTotalPages(), movieList.getNumber(), BUTTONS_TO_SHOW);

        // get Notifications from User, if logged in
        Iterable<NotificationUserRelation> notifications;
        NotificationHelper notificationHelper = new NotificationHelper(userRepository, notificationUserRelationRepository, notificationTypeRepository);
        notifications = notificationHelper.getNotificationsFromUser(principal);

        modelAndView.addObject("wastedMinutes", MovieDbUtils.getCalculatedMovieTime(movieRepository)[1]);
        modelAndView.addObject("wastedHours", MovieDbUtils.getCalculatedMovieTime(movieRepository)[0]);
        modelAndView.addObject("movies", movieList);
        modelAndView.addObject("selectedPageSize", evalPageSize);
        modelAndView.addObject("pager", pageModel);
        modelAndView.addObject("notifications", notifications);

        return modelAndView;
    }

}
