package com.xincao9.prs.spider.worker;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.xincao9.prs.api.model.RawTextArticle;
import java.net.URI;
import java.util.Iterator;
import javax.annotation.PostConstruct;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author xincao9@gmail.com
 */
@Component
public class ZhiHuWorker extends Thread {

    private static final Logger LOGGER = LoggerFactory.getLogger(ZhiHuWorker.class);

    @Autowired
    private RestTemplate restTemplate;
    private static final String ZHIHU_URL_PATTERN = "https://www.zhihu.com/api/v4/members/wo-yan-chen-mo/followees?include=data[*].educations,employments,answer_count,business,locations,articles_count,follower_count,gender,following_count,question_count,voteup_count,thanked_count,is_followed,is_following,badge[?(type=best_answerer)].topics&offset=%d&limit=1";
    private static final String RAW_TEXT_ARTICLE_URL = "http://localhost:9000/raw/text/article";

    @PostConstruct
    public void initMethod() {
        start();
    }

    @Override
    public void run() {
        for (int offset = 0; offset <= 5000; offset += 20) {
            try {
                String str = restTemplate.getForObject(String.format(ZHIHU_URL_PATTERN, offset), String.class);
                str = StringEscapeUtils.unescapeJava(str);
                LOGGER.info(str);
                if (StringUtils.isEmpty(str)) {
                    continue;
                }
                JSONObject jsono = JSONObject.parseObject(str);
                if (jsono == null) {
                    continue;
                }
                JSONArray data = jsono.getJSONArray("data");
                if (data == null) {
                    continue;
                }
                Iterator<Object> iter = data.iterator();
                while (iter.hasNext()) {
                    JSONObject item = (JSONObject) iter.next();
                    JSONObject business = item.getJSONObject("business");
                    RawTextArticle rawTextArticle = new RawTextArticle();
                    rawTextArticle.setAuthor(business.getString("name"));
                    rawTextArticle.setTitle(business.getString("name"));
                    rawTextArticle.setSummary(business.getString("excerpt"));
                    rawTextArticle.setText(business.getString("introduction"));
                    HttpHeaders headers = new HttpHeaders();
                    headers.setContentType(MediaType.APPLICATION_JSON);
                    headers.set("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/72.0.3626.121 Safari/537.36");
                    HttpEntity<String> entity = new HttpEntity(JSONObject.toJSONString(rawTextArticle, SerializerFeature.DisableCircularReferenceDetect), headers);
                    ResponseEntity<String> response = restTemplate.exchange(new URI(RAW_TEXT_ARTICLE_URL), HttpMethod.POST, entity, String.class);
//                    ResponseEntity<String> response = restTemplate.postForEntity(new URI(RAW_TEXT_ARTICLE_URL), rawTextArticle, String.class);
                    LOGGER.info(response.getBody());
                }
                Thread.sleep(RandomUtils.nextInt(200, 2000));
            } catch (Throwable e) {
                LOGGER.error(e.getMessage());
            }

        }
    }

}
