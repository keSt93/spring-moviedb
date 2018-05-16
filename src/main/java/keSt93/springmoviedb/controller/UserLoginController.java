package keSt93.springmoviedb.controller;

import keSt93.springmoviedb.entities.NotificationUserRelation;
import keSt93.springmoviedb.repository.MovieRepository;
import keSt93.springmoviedb.repository.NotificationTypeRepository;
import keSt93.springmoviedb.repository.NotificationUserRelationRepository;
import keSt93.springmoviedb.repository.UserRepository;
import keSt93.springmoviedb.utils.NotificationHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;

@Controller
public class UserLoginController {

    @Autowired
    UserRepository userRepository;
    @Autowired
    NotificationUserRelationRepository notificationUserRelationRepository;
    @Autowired
    NotificationTypeRepository notificationTypeRepository;
    @Autowired
    MovieRepository movieRepository;

    @RequestMapping(value = "/login")
    public String login(Principal principal) {

        return "userLogin";
    }
}
