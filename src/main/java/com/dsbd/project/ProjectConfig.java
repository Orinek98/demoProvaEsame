package com.dsbd.project;

import com.dsbd.project.security.AuthTokenFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class ProjectConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    ProjectUserDetailsService userDetailsService;

    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .authorizeRequests()

                // PUBLIC
                .antMatchers("/v2/**", "/swagger-ui/**").permitAll()// Consenti a tutti l'accesso all'endpoint /user/register
                .antMatchers(HttpMethod.GET,"/trips").permitAll() // Consenti a tutti l'accesso all'endpoint /user/register
                .antMatchers("/trips/register").permitAll()
                .antMatchers("/register").permitAll()
                .antMatchers("/login/**").permitAll()
                .antMatchers("/re-auth/**").permitAll()

                // AUTH
                .antMatchers("/me").authenticated()
                .antMatchers("/me/**").authenticated()
                .antMatchers(HttpMethod.POST, "/trips/{tripId}/buy").authenticated()

                // ADMIN
                .antMatchers(HttpMethod.POST,"/trips").hasAuthority("ADMIN")
                .anyRequest().authenticated() // Tutte le altre richieste devono essere autenticate
                .and()
                .csrf().disable(); // Nel caso di REST API stateless, la protezione Cross-Site-Request-Forgery non serve

        // Ogni richiesta in ingresso deve essere prima filtrata per la validazione del token
        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);

    }
}
