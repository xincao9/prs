package com.xincao9.prs.recommend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

/**
 *
 * @author xincao9@gmail.com
 */
@SpringBootApplication
@EnableEurekaClient
@EnableElasticsearchRepositories
public class Application {

    public static void main(String... args) {
        SpringApplication.run(Application.class, args);
    }
}
