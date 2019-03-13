package com.xincao9.prs.logminer.controller;

import com.xincao9.prs.api.constant.ConfigConsts;
import io.micrometer.core.instrument.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author xincao9@gmail.com
 */
@Controller
@RequestMapping("/user")
public class UserController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @GetMapping("{userId}/raw_text_article/{title}")
    public ResponseEntity<String> rawTextArticle(@PathVariable String userId, @PathVariable String title) {
        if (StringUtils.isBlank(userId) || StringUtils.isBlank(title)) {
            return ResponseEntity.status(400).build();
        }
        try {
            kafkaTemplate.send(ConfigConsts.USER_RAW_TEXT_ARTICLE_TOPIC, userId, title);
            return ResponseEntity.ok("ok");
        } catch (Throwable e) {
            LOGGER.error(e.getMessage());
            return ResponseEntity.status(500).build();
        }

    }
}
