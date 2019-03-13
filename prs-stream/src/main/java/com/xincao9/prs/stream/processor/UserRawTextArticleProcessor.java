package com.xincao9.prs.stream.processor;

import com.xincao9.prs.api.constant.CacheConsts;
import com.xincao9.prs.api.constant.ConfigConsts;
import com.xincao9.prs.stream.entity.RawTextArticleDO;
import com.xincao9.prs.stream.repository.RawTextArticleRepository;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import javax.annotation.PostConstruct;
import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.ForeachAction;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.KeyValueMapper;
import org.apache.kafka.streams.kstream.Predicate;
import org.apache.kafka.streams.kstream.Reducer;
import org.apache.kafka.streams.kstream.TimeWindows;
import org.apache.kafka.streams.kstream.ValueMapper;
import org.apache.kafka.streams.kstream.Windowed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**
 * 计算用户的短期标签
 *
 * @author xincao9@gmail.com
 */
@Component
public class UserRawTextArticleProcessor extends Thread {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserRawTextArticleProcessor.class);

    @Autowired
    private RawTextArticleRepository articleRepository;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @PostConstruct
    public void initMethod() {
        start();
    }

    @Override
    public void run() {
        Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, UserRawTextArticleProcessor.class.getSimpleName());
        props.put(StreamsConfig.CLIENT_ID_CONFIG, UserRawTextArticleProcessor.class.getSimpleName());
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        props.put(StreamsConfig.COMMIT_INTERVAL_MS_CONFIG, 10000);
        props.put(StreamsConfig.CACHE_MAX_BYTES_BUFFERING_CONFIG, 0);
        StreamsBuilder builder = new StreamsBuilder();
        KStream<String, String> kStream = builder.stream(ConfigConsts.USER_RAW_TEXT_ARTICLE_TOPIC);
        KTable<Windowed<String>, Integer> kTable = kStream.filter(
                new Predicate<String, String>() {
            @Override
            public boolean test(String key, String value) {
                return StringUtils.isNotBlank(key) && StringUtils.isNotBlank(value);
            }
        }
        ).flatMapValues(new ValueMapper<String, Set<String>>() {
            @Override
            public Set<String> apply(String value) {
                List<RawTextArticleDO> articleDOs = articleRepository.findByTitle(value);
                if (articleDOs == null || articleDOs.isEmpty()) {
                    return null;
                }
                Set<String> keywords = new HashSet();
                articleDOs.forEach((articleDO) -> {
                    keywords.addAll(articleDO.getTextKeywords());
                });
                return keywords;
            }
        }).map(new KeyValueMapper<String, String, KeyValue<String, Integer>>() {

            @Override
            public KeyValue<String, Integer> apply(String key, String value) {
                return new KeyValue(String.format("%s:%s", key, value), 1);
            }
        }).groupBy(new KeyValueMapper<String, Integer, String>() {
            @Override
            public String apply(String key, Integer value) {
                return key;
            }
        }).windowedBy(TimeWindows.of(10000)).reduce(new Reducer<Integer>() {
            @Override
            public Integer apply(Integer value1, Integer value2) {
                return value1 + value2;
            }
        });
        kTable.toStream().foreach(new ForeachAction<Windowed<String>, Integer>() {
            @Override
            public void apply(Windowed<String> key, Integer value) {
                LOGGER.info("key = {}, value = {}", key, value);
                String[] as = key.key().split(":");
                redisTemplate.opsForZSet().add(String.format(CacheConsts.USER_SHORT_PROFILE_RAW_TEXT_ARTICLE, as[0]), as[1], value);
            }
        });
        KafkaStreams kafkaStreams = new KafkaStreams(builder.build(), props);
        kafkaStreams.start();
        Runtime.getRuntime().addShutdownHook(new Thread(kafkaStreams::close));
    }

}
