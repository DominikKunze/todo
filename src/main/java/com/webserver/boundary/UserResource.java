package com.webserver.boundary;

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
        String generatedPassword = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            md.update("todo".getBytes(StandardCharsets.UTF_8));
            byte[] bytes = md.digest(password.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for(int i=0; i< bytes.length ;i++){
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            generatedPassword = sb.toString();
        }
        catch (NoSuchAlgorithmException e){
            e.printStackTrace();
        }
        //System.out.println("Sha512: " + generatedPassword);

        boolean found = false;
        Iterable<User> allUser = userRepository.findAll();
        for(User u : allUser){
            if(u.getName().equalsIgnoreCase(username)){
                found = true;
                if(u.passwordEquils(generatedPassword)){
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
