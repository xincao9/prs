package com.xincao9.prs.logminer.config;

import com.google.gson.Gson;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author xincao9@gmail.com
 */
@Configuration
public class RootConfig {

    /**
     *
     * @return
     */
    @Bean
    public Gson gson() {
        return new Gson();
    }
}
