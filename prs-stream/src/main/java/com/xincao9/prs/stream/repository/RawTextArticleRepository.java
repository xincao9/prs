package com.xincao9.prs.stream.repository;

import com.xincao9.prs.stream.entity.RawTextArticleDO;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 *
 * @author xincao9@gmail.com
 */
public interface RawTextArticleRepository extends ElasticsearchRepository<RawTextArticleDO, String> {

}
