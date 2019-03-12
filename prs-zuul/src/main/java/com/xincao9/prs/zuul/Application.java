package com.xincao9.prs.zuul;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

/**
 *
 * @author xincao9@gmail.com
 */
@SpringBootApplication
@EnableZuulProxy
@EnableDiscoveryClient
public class Application {

    public static void main(String... args) {
        SpringApplication.run(Application.class, args);
    }
}
