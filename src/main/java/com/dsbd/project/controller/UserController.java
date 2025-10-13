package com.dsbd.project.controller;

import com.dsbd.project.entity.User;
import com.dsbd.project.security.AuthResponse;
import com.dsbd.project.service.ProjectUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Optional;


@Controller
@RequestMapping(path = "/")
public class UserController {

    @Autowired
    ProjectUserService userService;

    public static class CreditRequest {
        private BigDecimal credit;

        public BigDecimal getCredit() {
            return credit;
        }

        public void setCredit(BigDecimal credit) {
            this.credit = credit;
        }
    }

    //POST http://localhost:8080/register
    @PostMapping(path = "register")
    public @ResponseBody User register(@RequestBody User user) {
        return userService.addUser(user);
    }

    //GET http://localhost:8080/login
    @PostMapping(path = "login")
    public @ResponseBody AuthResponse login(@RequestBody User user) {
        return userService.login(user);
    }

    @PostMapping(path = "re-auth")
    public @ResponseBody AuthResponse reAuth(@RequestHeader String refreshToken) throws Exception {
        return userService.reAuth(refreshToken);
    }

    @GetMapping(path = "me")
    public @ResponseBody User me(Authentication authentication) {

        Object principal = authentication.getPrincipal();

        if (principal instanceof UserDetails) {

            String email = ((UserDetails) principal).getUsername();
            return userService.getUserDetailsByEmail(email);
        }

        throw new RuntimeException("Errore di autenticazione: impossibile recuperare l'utente loggato.");
    }


    @PatchMapping(path = "/me/credit/toup")
    public @ResponseBody Optional<User> creditToUp(Authentication authentication, @RequestBody CreditRequest request) {
        BigDecimal credit = request.getCredit();
        Object principal = authentication.getPrincipal();


        if (principal instanceof UserDetails) {

            String email = ((UserDetails) principal).getUsername();

            return userService.addCreditByEmail(email, credit);
        }
        throw new RuntimeException("Errore di autenticazione: impossibile recuperare l'utente loggato.");
    }
}
