package com.lucky.web_app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class WebAppApplication {

    public static void main(final String[] args) {
        SpringApplication.run(WebAppApplication.class, args);
        System.out.println("server started");
    }

}
