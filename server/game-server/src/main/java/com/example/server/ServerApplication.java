package com.example.server;

import com.example.util.*;
import org.slf4j.*;
import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.boot.autoconfigure.jdbc.*;
import org.springframework.boot.builder.*;
import org.springframework.boot.web.servlet.support.*;

import java.util.*;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
public class ServerApplication extends SpringBootServletInitializer {

    private static final Logger logger;

    static {
        PrintUtils.red(String.format("STATIC BLOCK IN  args = %s", ServerApplication.class.toString()));
        logger = LoggerFactory.getLogger(ServerApplication.class);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        SpringApplicationBuilder builder = application.sources(ServerApplication.class);

        PrintUtils.green(String.format(
                        "ServerApplication.configure: SpringApplicationBuilder application.application() = %s",
                        application.application()
                )
        );
        return builder;
    }

    public static void main(String[] args) {
        SpringApplication.run(ServerApplication.class, args);

        PrintUtils.red(String.format("ServerApplication.main: args = %s", Arrays.toString(args)));
    }
}
