package com.xndr.velaroute.config;

import com.xndr.velaroute.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {

    private final UserRepository userRepository;
    //    private final DataSource dataSource;

    @Bean
    public UserDetailsService userDetailsService(UserRepository userRepository) {
        return username -> userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    // DEBUG: Testing to see why Auth is not recognizing BCrypt password.
//    @Bean
//    public UserDetailsService userDetailsService() {
//        return username -> {
//            User user = userRepository.findByUsername(username)
//                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));
//            System.out.println("DEBUG: DB Password for " + username + " is: [" + user.getPassword() + "]");
//            return user;
//        };
//    }

    // DEGUB: This sees if we are connected to the correct DB.
//    @Bean
//    public UserDetailsService userDetailsService() {
//        return username -> {
//            // This will print the actual database URL the app is using
//            try (java.sql.Connection conn = dataSource.getConnection()) {
//                System.out.println("DEBUG: Connected to: " + conn.getMetaData().getURL());
//            } catch (Exception e) {
//
//            }
//
//            User user = userRepository.findByUsername(username)
//                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));
//
//            System.out.println("DEBUG: DB password for " + username + " is: [" + user.getPassword() +"]");
//            return user;
//        };
//    }

    @Bean
    public AuthenticationProvider authenticationProvider(UserDetailsService userDetailsService) {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
