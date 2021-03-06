package com.webserver.boundary;

import com.webserver.config.CookieConfigure;
import com.webserver.model.Task;
import com.webserver.services.data.TaskRepository;
import com.webserver.services.data.UserCookies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/api")
public class ApiResource {
    @Autowired
    TaskRepository taskRepository;

    UserCookies userCookies = new UserCookies();

    @RequestMapping("/{todoId}/show")
    public String auft(@PathVariable("todoId")Long id, Model model, @CookieValue("Login_Hash_Value") Optional<String> cookieHash){
        if(!userCookies.cookieHash.contains(cookieHash.get())) return "pages/login.html";

        Task task = taskRepository.findById(id).get();
        model.addAttribute("todo", task);
        return "pages/todo.html";
    }

    @RequestMapping("/{todoId}/edit")
    public String todoEditieren(@PathVariable("todoId")Long id, Model model, @CookieValue("Login_Hash_Value") Optional<String> cookieHash){
        if(!userCookies.cookieHash.contains(cookieHash.get())) return "pages/login.html";

        Task task = taskRepository.findById(id).get();
        model.addAttribute("todo", task);
        return "pages/edit.html";
    }

    @RequestMapping(value = "/edit",method = RequestMethod.POST)
    public String editieren(@RequestParam(name = "id") Long id, @RequestParam(name = "title") String title, @RequestParam(name = "inhalt") String inhalt, @RequestParam(name = "status") int status, Model model, @CookieValue("Login_Hash_Value") Optional<String> cookieHash){
        if(!userCookies.cookieHash.contains(cookieHash.get())) return "pages/login.html";

        if(id >= 0 && title.length()>=3 && inhalt.length()>=3 && status >= 0) {
            Task task = taskRepository.findById(id).get();
            task.setTitle(title);
            task.setInhalt(inhalt);
            task.setStatus(status);
            taskRepository.deleteById(id);
            taskRepository.save(task);

            model.addAttribute("success","Aufgabe wurde erfolgreich bearbeitet!");
        }else{
            model.addAttribute("failed","Auftrag konnte nicht bearbeitet werden!");
        }
        model.addAttribute("allToDos", taskRepository.findAll());
        return "pages/main.html";
    }

    @RequestMapping("/{todoId}/delete")
    public String editieren(@PathVariable("todoId") Long id, Model model, @CookieValue("Login_Hash_Value") Optional<String> cookieHash){
        if(!userCookies.cookieHash.contains(cookieHash.get())) return "pages/login.html";

        taskRepository.deleteById(id);

        model.addAttribute("allToDos", taskRepository.findAll());
        model.addAttribute("success","Aufgabe wurde erfolgreich entfernt!");
        return "pages/main.html";
    }

    @RequestMapping(value = "/add",method = RequestMethod.POST)
    public String addTodo(@RequestParam("title") String title, @RequestParam("inhalt") String inhalt, Model model, @CookieValue("Login_Hash_Value") Optional<String> cookieHash){
        if(!userCookies.cookieHash.contains(cookieHash.get())) return "pages/login.html";

        if(title.length()>=3 && inhalt.length()>=3) {
            Task task = new Task(title,inhalt,0);
            taskRepository.save(task);
            model.addAttribute("success","Aufgabe wurde erfolgreich hinzugefügt!");
        }else{
            model.addAttribute("failed","Auftrag konnte nicht erstellt werden!");
        }
        model.addAttribute("allToDos", taskRepository.findAll());
        return "pages/main.html";
    }
}
