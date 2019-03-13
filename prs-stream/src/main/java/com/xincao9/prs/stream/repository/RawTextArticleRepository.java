package com.xincao9.prs.stream.repository;

import com.xincao9.prs.stream.entity.RawTextArticleDO;
import java.util.List;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 *
 * @author xincao9@gmail.com
 */
public interface RawTextArticleRepository extends ElasticsearchRepository<RawTextArticleDO, String> {

    List<RawTextArticleDO> findByTitle (String title);
}
