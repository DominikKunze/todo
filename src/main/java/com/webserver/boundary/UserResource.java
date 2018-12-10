package com.webserver.boundary;

import com.webserver.config.CookieConfigure;
import com.webserver.control.GenerateCookieHashCode;
import com.webserver.control.HashToSha512;
import com.webserver.model.User;
import com.webserver.services.data.TaskRepository;
import com.webserver.services.data.UserCookies;
import com.webserver.services.data.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

@Controller
@RequestMapping("/user")
public class UserResource {

    @Autowired
    UserRepository userRepository;
    @Autowired
    TaskRepository taskRepository;

    @RequestMapping("/login")
    public String userLogin(@RequestParam("username") String username, @RequestParam("password") String password,
                            Model model, HttpServletResponse httpServletResponse){
        HashToSha512 hashToSha512 = new HashToSha512();
        String hashPassword = hashToSha512.hash(password);
        System.out.println("Sha512: " + hashPassword);

        GenerateCookieHashCode generateCookieHashCode = new GenerateCookieHashCode();
        String cookieHash = generateCookieHashCode.generateHash(CookieConfigure.cookieHashLength);
        Cookie cookie = new Cookie("Login_Hash_Value",cookieHash);
        cookie.setDomain("localhost");
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        System.out.printf("Cookie: " + cookie.toString());
        httpServletResponse.addCookie(cookie);
        UserCookies userCookies = new UserCookies();
        userCookies.cookieHash.add(cookieHash);

        boolean found = false;
        Iterable<User> allUser = userRepository.findAll();
        for(User u : allUser){
            if(u.getName().equalsIgnoreCase(username)){
                found = true;
                if(u.passwordEquils(hashPassword)){
                    model.addAttribute("allToDos", taskRepository.findAll());
                    model.addAttribute("success","Erfolgreich eingeloggt!");
                    return "pages/main.html";
                }else{
                    model.addAttribute("failed", "Falsches Passwort!");
                }
                break;
            }
        }
        if(!found)model.addAttribute("failed","User nicht gefunden!");
        return "pages/login.html";
    }
}
