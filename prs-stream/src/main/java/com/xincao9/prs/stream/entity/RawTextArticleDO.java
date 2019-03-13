package com.xincao9.prs.stream.entity;

import com.xincao9.prs.api.model.RawTextArticle;
import org.springframework.data.elasticsearch.annotations.Document;

/**
 *
 * @author xincao9@gmail.com
 */
@Document(indexName = "article")
public class RawTextArticleDO extends RawTextArticle {

}
