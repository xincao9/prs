package com.xincao9.prs.recommend.repository;

import com.xincao9.prs.recommend.entity.RawTextArticleDO;
import java.util.List;
import java.util.Set;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 *
 * @author xincao9@gmail.com
 */
public interface RawTextArticleRepository extends ElasticsearchRepository<RawTextArticleDO, String> {
    
    List<RawTextArticleDO> findByTextKeywordsIn (Set<String> textKeywords);
}
