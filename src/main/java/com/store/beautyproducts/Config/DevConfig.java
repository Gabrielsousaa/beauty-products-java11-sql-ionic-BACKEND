package com.store.beautyproducts.Config;

import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;


import com.store.beautyproducts.services.DBService;
import com.store.beautyproducts.services.EmailService;
import com.store.beautyproducts.services.SmtpEmailService;

@Configuration
@Profile("dev")
public class DevConfig {
    
    @Autowired
    private DBService dbservice;

    @Value("${spring.jpa.hibernate.ddl-auto}")
    private String strategy;

    @Bean
    public boolean instantiateDatabase() throws ParseException{
        if(!"create".equals(strategy)){
            return false;
        }
        dbservice.instantiateTestDatabase();
        return true;   
    }
    @Bean
    public EmailService emailService() {
        return new SmtpEmailService();
    }
}
