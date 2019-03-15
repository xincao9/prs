package com.xincao9.prs.spider.worker;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.xincao9.prs.api.model.RawTextArticle;
import java.net.URI;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import javax.annotation.PostConstruct;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.util.Strings;
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
    private static final String ZHIHU_URL_PATTERN = "https://www.zhihu.com/api/v4/members/wo-yan-chen-mo/followees?include=data[*].educations,employments,answer_count,business,locations,articles_count,follower_count,gender,following_count,question_count,voteup_count,thanked_count,is_followed,is_following,badge[?(type=best_answerer)].topics&offset=%d&limit=20";
    private static final String RAW_TEXT_ARTICLE_URL = "http://localhost:9000/raw/text/article";
    @Autowired
    private HttpClient httpClient;
    private static final Set<HttpHost> PROXIES = new HashSet();

    static {
        PROXIES.add(new HttpHost("107.0.141.230", 3128));
        PROXIES.add(new HttpHost("186.151.31.54", 59819));
        PROXIES.add(new HttpHost("181.49.24.126", 8081));
        PROXIES.add(new HttpHost("195.80.140.212", 8081));
        PROXIES.add(new HttpHost("1.179.183.86", 8080));
        PROXIES.add(new HttpHost("103.76.50.182", 8080));
        PROXIES.add(new HttpHost("220.229.150.155", 8088));
        PROXIES.add(new HttpHost("173.214.169.77", 3128));
        PROXIES.add(new HttpHost("187.94.217.69", 3128));
        PROXIES.add(new HttpHost("122.155.222.98", 3128));
        PROXIES.add(new HttpHost("159.224.83.100", 8080));
        PROXIES.add(new HttpHost("177.8.216.106", 8080));
        PROXIES.add(new HttpHost("101.50.1.2", 80));
    }

    public String get(String url) {

        for (HttpHost proxy : PROXIES) {
            try {
                HttpGet httpGet = new HttpGet(url);
                RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(2000).setConnectTimeout(2000).setProxy(proxy).build();
                httpGet.setConfig(requestConfig);
                HttpResponse response = httpClient.execute(httpGet);
                if (response.getStatusLine().getStatusCode() != 200) {
                    continue;
                }
                String str = EntityUtils.toString(response.getEntity());
                return StringEscapeUtils.unescapeJava(str);
            } catch (Throwable e) {
                LOGGER.error(e.getMessage());
            }
        }
        return Strings.EMPTY;
    }

    @PostConstruct
    public void initMethod() {
        start();
    }

    @Override
    public void run() {
        for (int offset = 0; offset <= 5000; offset += 20) {
            try {
                String str = get(String.format(ZHIHU_URL_PATTERN, offset));
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
                    LOGGER.info(response.getBody());
                }
                Thread.sleep(RandomUtils.nextInt(200, 2000));
            } catch (Throwable e) {
                LOGGER.error(e.getMessage());
            }

        }
    }

}
