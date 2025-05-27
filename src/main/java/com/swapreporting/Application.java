#package com.swapreporting;

#import org.springframework.boot.SpringApplication;
#import org.springframework.boot.autoconfigure.SpringBootApplication;

#@SpringBootApplication
#public class Application {

#	public static void main(String[] args) {
#		SpringApplication.run(Application.class, args);
#	}

#}
package com.swapreport;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SwapReportApplication {
    public static void main(String[] args) {
        SpringApplication.run(SwapReportApplication.class, args);
    }
}
