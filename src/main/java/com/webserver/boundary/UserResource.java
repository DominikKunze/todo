package com.webserver.boundary;

import com.webserver.control.HashToSha512;
import com.webserver.model.User;
import com.webserver.services.data.TaskRepository;
import com.webserver.services.data.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
    public String userLogin(@RequestParam("username") String username, @RequestParam("password") String password, Model model){
        HashToSha512 hashToSha512 = new HashToSha512();
        String hashPassword = hashToSha512.hash(password);
        //System.out.println("Sha512: " + hashPassword);

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
