package com.example.server;

import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.boot.autoconfigure.jdbc.*;
import org.springframework.boot.builder.*;
import org.springframework.boot.web.servlet.support.*;
import org.springframework.context.annotation.*;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
public class ServerApplication extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(ServerApplication.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(ServerApplication.class, args);
    }
}
