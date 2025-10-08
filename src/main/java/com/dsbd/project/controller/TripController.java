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
@RequestMapping(path = "/trips")
public class TripController {
    @GetMapping
    public @ResponseBody Iterable<User> getAll() {
        return tripService.getAllUsers();
    }
}

