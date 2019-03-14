package com.xincao9.prs.spider.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author xincao9@gmail.com
 */
@Configuration
public class RootConfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
