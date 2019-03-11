package com.xincao9.prs.logminer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 *
 * @author xincao9@gmail.com
 */
@SpringBootApplication
@EnableDiscoveryClient
public class Application {

    public static void main(String... args) {
        SpringApplication.run(Application.class, args);
    }
}
