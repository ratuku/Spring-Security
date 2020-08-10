package com.example.Amigoscode.Spring.Security.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.concurrent.RecursiveTask;

@Controller
@RequestMapping
public class TemplateController {

    @GetMapping("/login")
    public String getLoginView(){
        return "login";
    }

    @GetMapping("/courses")
    public String getCourses(){
        return "courses";
    }
}
