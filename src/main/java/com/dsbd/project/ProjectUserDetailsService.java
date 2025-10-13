package com.dsbd.project;

import com.dsbd.project.entity.User;
import com.dsbd.project.entity.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class ProjectUserDetailsService implements UserDetailsService {
    @Autowired
    UserRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        // 1. Estrai l'utente dall'Optional.
        //    Se l'Optional è vuoto, lancia l'eccezione richiesta da Spring Security.
        User user = repository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Utente non trovato per l'email: " + email));

        // 2. Continua con la costruzione dell'oggetto UserDetails standard di Spring Security.
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                true,
                true,
                true,
                true,
                getAuth(user.getRoles())
        );
    }

    // Costruisce la lista dei privilegi concessi all'utente, sulla base dei ruoli ad esso associati
    private List<GrantedAuthority> getAuth(List<String> roles){
        List<GrantedAuthority> authorities = new ArrayList<>();

        for (final String role : roles)
            authorities.add(new SimpleGrantedAuthority(role));

        return authorities;
    }
}
