package com.dsbd.project.controller;

import com.dsbd.project.entity.User;
import com.dsbd.project.security.AuthResponse;
import com.dsbd.project.service.ProjectUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping(path = "/user")
public class UserController {

    @Autowired
    ProjectUserService userService;

    //POST http://localhost:8080/user/register
    @PostMapping(path = "/register")
    public @ResponseBody User register(@RequestBody User user) {
        return userService.addUser(user);
    }

    //GET http://localhost:8080/user/all
    @GetMapping(path = "/all")
    public @ResponseBody Iterable<User> getAll() {
        return userService.getAllUsers();
    }

    @PostMapping(path = "/login")
    public @ResponseBody AuthResponse login(@RequestBody User user) {
        return userService.login(user);
    }

    @PostMapping(path = "/re-auth")
    public @ResponseBody AuthResponse reAuth (@RequestHeader String refreshToken) throws Exception {
        return userService.reAuth(refreshToken);
    }

    //GET http://localhost:8080/user/<mail>

//    @GetMapping(path = "/{id}")
//    public @ResponseBody
//    Optional<User> getUserById(Authentication auth, @PathVariable String id) {
//        Object principal = auth.getPrincipal();
//        //String comp = (((User) principal).getId());
//
//        if (principal instanceof User && id.equals(((User) principal).getId())) {
//            System.out.println(((User) principal).getId());
//            return userService.getByEmail(((User) principal).getEmail());
//        }
//        else {
//            System.out.println("nada id:" + id + " comp:" + principal);
//            return Optional.empty();
//        }
//    }

    @GetMapping(path = "/{id}")
    public @ResponseBody
    Optional<User> getUserById(@PathVariable Integer id) {
            return userService.getById(id);

    }

//    @GetMapping(path = "/{email}")
//    public @ResponseBody
//    Optional<User> getUserByEmail(Authentication auth, @PathVariable String email) {
//        if (email.equalsIgnoreCase(auth.getName()))
//            return userService.getByEmail(email);
//        else return Optional.empty();
//    }



    //DELETE http://localhost:8080/user/1
    @DeleteMapping(path = "/{id}")
    public @ResponseBody String deleteUser (@PathVariable Integer id) {
        return userService.deleteUser(id);
    }
}
