package keSt93.springmoviedb.controller;

import keSt93.springmoviedb.entities.NotificationUserRelation;
import keSt93.springmoviedb.entities.Series;
import keSt93.springmoviedb.models.PageModel;
import keSt93.springmoviedb.repository.*;
import keSt93.springmoviedb.utils.MovieDbUtils;
import keSt93.springmoviedb.utils.NotificationHelper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.Optional;

@Controller
public class SeriesListController {

    // Settings for pagination
    private static final int BUTTONS_TO_SHOW = 51;
    private static final int INITIAL_PAGE = 0;
    private static final int INITIAL_PAGE_SIZE = 12;

    private SeriesRepository seriesRepository;
    private UserRepository userRepository;
    private NotificationUserRelationRepository notificationUserRelationRepository;
    private NotificationTypeRepository notificationTypeRepository;
    private MovieRepository movieRepository;

    public SeriesListController(
            SeriesRepository seriesRepository,
            UserRepository userRepository,
            NotificationUserRelationRepository notificationUserRelationRepository,
            NotificationTypeRepository notificationTypeRepository,
            MovieRepository movieRepository
    ) {
        this.seriesRepository = seriesRepository;
        this.userRepository = userRepository;
        this.notificationUserRelationRepository = notificationUserRelationRepository;
        this.notificationTypeRepository = notificationTypeRepository;
        this.movieRepository = movieRepository;
    }


    @RequestMapping("/m/series")
    public ModelAndView allMovies(Principal principal,
                                  @RequestParam("pageSize") Optional<Integer> pageSize,
                                  @RequestParam("page") Optional<Integer> page) {
        ModelAndView modelAndView = new ModelAndView("seriesList");

        int evalPageSize = pageSize.orElse(INITIAL_PAGE_SIZE);
        int evalPage = (page.orElse(0) < 1) ? INITIAL_PAGE : page.get() - 1;

        Page<Series> seriesList = seriesRepository.findAllByIdNotOrderByIdDesc(-1, new PageRequest(evalPage, evalPageSize));
        PageModel pageModel = new PageModel(seriesList.getTotalPages(), seriesList.getNumber(), BUTTONS_TO_SHOW);

        // get Notifications from User, if logged in
        Iterable<NotificationUserRelation> notifications;
        NotificationHelper notificationHelper = new NotificationHelper(userRepository, notificationUserRelationRepository, notificationTypeRepository);
        notifications = notificationHelper.getNotificationsFromUser(principal);

        modelAndView.addObject("wastedMinutes", MovieDbUtils.getCalculatedMovieTime(movieRepository)[1]);
        modelAndView.addObject("wastedHours", MovieDbUtils.getCalculatedMovieTime(movieRepository)[0]);
        modelAndView.addObject("series", seriesList);
        modelAndView.addObject("selectedPageSize", evalPageSize);
        modelAndView.addObject("pager", pageModel);
        modelAndView.addObject("notifications", notifications);

        return modelAndView;
    }

}
