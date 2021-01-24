/**
 * @Yang
 *
 * Copyright (c) 2020 yang www.theaic.com 344295704@qq.com
 *
 */

package com.wanl.mapper;

import com.wanl.entity.Article;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ArticleMapper {
    Integer create(Article paramArticle);

    List<Article> findList(@Param("start") int paramInt, @Param("limit") Integer paramInteger);

    Integer getCount();

    Integer update(Article paramArticle);

    Integer del(Integer paramInteger);

    Integer updateTop(@Param("id") Integer paramInteger1, @Param("value") Integer paramInteger2);

    Article getArticle(Integer paramInteger);
}

