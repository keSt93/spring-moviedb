package keSt93.springmoviedb.controller;


import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
public class UserLoginController {

    @RequestMapping(value = "/")
    public String login(Principal principal) {
        if (principal != null) {
            return "redirect:/m/";
        }
        return "userLogin";
    }
}
