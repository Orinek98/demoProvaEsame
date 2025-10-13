package com.dsbd.project.service;

import com.dsbd.project.security.JwtUtils;
import com.dsbd.project.entity.User;
import com.dsbd.project.entity.UserRepository;
import com.dsbd.project.security.AuthResponse;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Optional;

@Service
@Transactional
public class ProjectUserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    JwtUtils jwtUtils;

    @Value("${jwt.secret}")
    private String jwtSecret;

    public Iterable<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getById(Integer id) {
        return userRepository.findById(id);
    }



    public User addUser(User user) {
        user.setRoles(Collections.singletonList("USER"));
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public String deleteUser(Integer userId) {
        userRepository.deleteById(userId);
        return "User with id: " + userId + " has been deleted!";
    }

    public Optional<User> addCreditByEmail(String email, BigDecimal creditToAdd) {

        Optional<User> userOptional = userRepository.findByEmail(email);

        if (userOptional.isPresent()) {
            User user = userOptional.get();

            BigDecimal currentCredit = user.getCredit() != null ? user.getCredit() : BigDecimal.ZERO;
            BigDecimal newCredit = currentCredit.add(creditToAdd);

            user.setCredit(newCredit);
            User updatedUser = userRepository.save(user);

            return Optional.of(updatedUser);
        }

        // Se l'utente non viene trovato
        return Optional.empty();
    }

    public AuthResponse login(User user) {
        // 1. Usa .orElse(null) per estrarre l'utente (User) dall'Optional
        User u = userRepository.findByEmail(user.getEmail()).orElse(null);

        if (u != null) {
            if (passwordEncoder.matches(user.getPassword(), u.getPassword())) {
                AuthResponse authResponse = new AuthResponse(
                        jwtUtils.generateJwtToken(u.getEmail()),
                        jwtUtils.generateRefreshToken(u.getEmail())
                );
                return authResponse;
            }
        }
        return null;
    }

    public AuthResponse reAuth(String refreshToken) throws Exception {
        AuthResponse authResponse = new AuthResponse();
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(jwtSecret)
                    .parseClaimsJws(refreshToken)
                    .getBody();

            // Estrai l'utente (User) dall'Optional. Se non trovato, 'user' sarà null.
            User user = userRepository.findByEmail(claims.getSubject()).orElse(null);

            if (user != null) {
                authResponse.setAccessToken(jwtUtils.generateJwtToken(user.getEmail()));
                authResponse.setRefreshToken(jwtUtils.generateRefreshToken(user.getEmail()));
            } else {
                // Token valido, ma utente non più presente nel DB
                throw new Exception("Utente associato al token non trovato.");
            }
        } catch (Exception e) {
            authResponse.setMsg(String.valueOf(e));
            throw new Exception("Errore durante la decodifica o l'uso del token", e);
        }

        return authResponse;
    }


    public AuthResponse refresh(User user) {
        User u = userRepository.findByEmail(user.getEmail()).orElse(null);
        if (u != null) {
            if (passwordEncoder.matches(user.getPassword(), u.getPassword())) {
                AuthResponse authResponse = new AuthResponse(jwtUtils.generateJwtToken(u.getEmail()), jwtUtils.generateRefreshToken(u.getEmail()));
                return authResponse;
            }
        }
        return null;
    }

    public User getUserDetailsByEmail(String email) {

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Utente loggato non trovato nel DB, email: " + email));
    }
}
