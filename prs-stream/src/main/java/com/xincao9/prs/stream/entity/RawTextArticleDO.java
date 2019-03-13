package com.xincao9.prs.stream.entity;

import com.xincao9.prs.api.constant.ConfigConsts;
import com.xincao9.prs.api.model.RawTextArticle;
import java.util.List;
import java.util.UUID;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

/**
 *
 * @author xincao9@gmail.com
 */
@Document(indexName = ConfigConsts.RAW_TEXT_ARTICLE_TOPIC)
public class RawTextArticleDO extends RawTextArticle {

    @Id
    private String id = UUID.randomUUID().toString();
    private List<String> summarykeywords;
    private List<String> textKeywords;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<String> getSummarykeywords() {
        return summarykeywords;
    }

    public void setSummarykeywords(List<String> summarykeywords) {
        this.summarykeywords = summarykeywords;
    }

    public List<String> getTextKeywords() {
        return textKeywords;
    }

    public void setTextKeywords(List<String> textKeywords) {
        this.textKeywords = textKeywords;
    }

}
