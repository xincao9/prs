package com.xincao9.prs.logminer.controller;

import com.google.gson.Gson;
import com.xincao9.prs.api.constant.ConfigConsts;
import com.xincao9.prs.api.model.Article;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author xincao9@gmail.com
 */
@RestController
@RequestMapping("/raw")
public class RawController {

    private static final Logger LOGGER = LoggerFactory.getLogger(RawController.class);

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;
    @Autowired
    private Gson gson;

    /**
     * 上传文本格式内容
     *
     * @param article
     * @return
     */
    @PostMapping("text/article")
    public ResponseEntity<String> textArticle(@RequestBody Article article) {
        if (article == null) {
            return ResponseEntity.status(400).build();
        }
        try {
            String data = gson.toJson(article);
            kafkaTemplate.send(ConfigConsts.ARTICLE_TOPIC, String.valueOf(data.hashCode()), data);
            return ResponseEntity.ok().body("ok");
        } catch (Throwable e) {
            LOGGER.error(e.getMessage());
            return ResponseEntity.status(500).build();
        }
    }
}
