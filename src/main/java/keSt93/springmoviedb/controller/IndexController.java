package keSt93.springmoviedb.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by stein on 14.02.2018.
 */

@Controller
@RequestMapping("/")
public class IndexController {

    @GetMapping("/")
    public ModelAndView index() {
        ModelAndView m = new ModelAndView("index");

        return m;
    }

}
