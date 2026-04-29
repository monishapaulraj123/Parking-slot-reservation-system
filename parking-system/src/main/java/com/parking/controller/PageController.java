package com.parking.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Serves HTML pages via Thymeleaf.
 */
@Controller
public class PageController {
    @GetMapping("/") public String index()    { return "index"; }
    @GetMapping("/login") public String login() { return "login"; }
    @GetMapping("/register") public String register() { return "register"; }
    @GetMapping("/booking") public String booking() { return "booking"; }
    @GetMapping("/history") public String history() { return "history"; }
    @GetMapping("/admin") public String admin() { return "admin"; }
}
