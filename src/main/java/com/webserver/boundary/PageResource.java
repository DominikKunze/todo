package com.webserver.boundary;

import com.webserver.services.data.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Controller
public class PageResource {

    @Autowired
    TaskRepository taskRepository;

    @RequestMapping(path = {"index.html","index","/"})
    public String index(Model model){
        model.addAttribute("allToDos", taskRepository.findAll());
        return "pages/main";
    }

    @RequestMapping("add")
    public String todohinzufuegen(@CookieValue("bla")Optional<String> cookie){
        return "pages/newtodo.html";
    }
    @RequestMapping("login")
    public String loginpage(){
        return "pages/login.html";
    }
}
