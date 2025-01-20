package com.dataquadinc.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


@Configuration
public class AppConfig {

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {

         return new BCryptPasswordEncoder();
    }




//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();  // This will be used to encrypt passwords
//    }
}
