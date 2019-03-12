package com.xincao9.prs.logminer.controller;

import com.xincao9.prs.api.constant.ConfigConsts;
import org.apache.commons.lang.StringUtils;
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
     * @param data
     * @return
     */
    @PostMapping("text")
    public ResponseEntity<String> post(@RequestBody String data) {
        if (StringUtils.isBlank(data)) {
            return ResponseEntity.status(400).build();
        }
        try {
            kafkaTemplate.send(ConfigConsts.RAW_TEXT_TOPIC, String.valueOf(data.hashCode()), data);
            return ResponseEntity.ok().body("ok");
        } catch (Throwable e) {
            LOGGER.error(e.getMessage());
            return ResponseEntity.status(500).build();
        }
    }
}
