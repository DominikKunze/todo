package com.webserver.boundary;

import com.webserver.config.CookieConfigure;
import com.webserver.services.data.TaskRepository;
import com.webserver.services.data.UserCookies;
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

    UserCookies userCookies = new UserCookies();

    @RequestMapping(path = {"index.html","index","/"})
    public String index(Model model, @CookieValue(name="Login_Hash_Value",defaultValue = "0") Optional<String> cookieHash){
        if(!userCookies.cookieHash.contains(cookieHash.get())) return "pages/login.html";
        model.addAttribute("allToDos", taskRepository.findAll());
        return "pages/main";
    }

    @RequestMapping("add")
    public String todohinzufuegen(@CookieValue(name="Login_Hash_Value",defaultValue = "0") Optional<String> cookieHash){
        if(!userCookies.cookieHash.contains(cookieHash.get())) return "pages/login.html";
        return "pages/newtodo.html";
    }
    @RequestMapping("login")
    public String loginpage(@CookieValue(name="Login_Hash_Value",defaultValue = "0") Optional<String> cookieHash){
        if(!userCookies.cookieHash.contains(cookieHash.get())) return "pages/login.html";
        return "pages/login.html";
    }
}
