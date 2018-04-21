package keSt93.springmoviedb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class UserLoginController {

    @GetMapping(value = "/login")
    public String login() {
        ModelAndView m = new ModelAndView("userLogin");
        return "userLogin";
    }
}
