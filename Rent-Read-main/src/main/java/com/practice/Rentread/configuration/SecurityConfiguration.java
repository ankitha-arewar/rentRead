package com.practice.Rentread.configuration;

import com.practice.Rentread.Entities.Role;
import com.practice.Rentread.Entities.User;
import com.practice.Rentread.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.List;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration{

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    private final UserService userService;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws  Exception{
        http.csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(request -> request.requestMatchers("/api/v1/auth/**")
            .permitAll()
            .requestMatchers("/api/v1/user/forAdmin",
                    "/api/v1/books/getAllBooks",
                    "/api/v1/books/getBookById/**",
                    "/api/v1/books/available/**",
                    "/api/v1/books/available/**",
                    "/api/v1/books/updateBook/**",
                    "/api/v1/books/addBook",
                    "/api/v1/books/deleteBook/**"

                ).hasAnyAuthority(Role.ADMIN.name())
            .requestMatchers("/api/v1/user/forUser",
                    "/api/v1/books/getAllBooks",
                    "/api/v1/books/getBookById/**",
                    "/api/v1/books/available/**",
                    "/api/v1/books/library/**"
                ).hasAnyAuthority(Role.USER.name())
            .anyRequest().authenticated())

            .sessionManagement(manager -> manager.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authenticationProvider(authenticationProvider()).addFilterBefore(
                    jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class
        );
        return http.build();

    }

    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider authenticationProvider= new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userService.userDetailsService());
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception{
        return config.getAuthenticationManager();
    }


}
