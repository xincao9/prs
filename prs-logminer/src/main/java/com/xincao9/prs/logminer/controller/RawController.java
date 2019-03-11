package com.xincao9.prs.logminer.controller;

import com.xincao9.prs.api.model.Request;
import com.xincao9.prs.logminer.constant.ConfigConsts;
import java.net.URLDecoder;
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

    /**
     * 上传文本格式内容
     *
     * @param request
     * @return
     */
    @PostMapping("text")
    public ResponseEntity<String> post(@RequestBody Request<String> request) {
        if (request == null) {
            return ResponseEntity.status(400).build();
        }
        try {
            kafkaTemplate.send(ConfigConsts.RAW_TEXT_TOPIC, request.getOid (), URLDecoder.decode(request.getData(), "UTF-8"));
            return ResponseEntity.ok().body("ok");
        } catch (Throwable e) {
            LOGGER.error(e.getMessage());
            return ResponseEntity.status(500).build();
        }
    }
}
