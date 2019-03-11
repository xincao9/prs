package com.xincao9.prs.logminer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PathVariable;
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

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    /**
     * 上传文本格式内容
     * 
     * @param id 文本主键
     * @param data 内容格式
     * @return 
     */
    @PostMapping("text/{id}")
    public ResponseEntity<String> post(@PathVariable String id, @RequestBody String data) {
        try {
            kafkaTemplate.send("", id, data);
            return ResponseEntity.ok().body("ok");
        } catch (Throwable e) {
        }
        return ResponseEntity.badRequest().body("failure");
    }
}
