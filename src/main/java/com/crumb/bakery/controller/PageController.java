package com.crumb.bakery.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    /** Serve the CRUMB 2.0 frontend at / */
    @GetMapping("/")
    public String index() {
        return "forward:/index.html";
    }
}
