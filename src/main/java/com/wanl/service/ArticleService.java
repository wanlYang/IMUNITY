/**
 * @Yang
 *
 * Copyright (c) 2020 yang www.theaic.com 344295704@qq.com
 *
 */

package com.wanl.service;

import com.wanl.entity.Article;
import java.util.List;

public interface ArticleService {
  Integer addArticle(Article paramArticle);
  
  List<Article> getArticleList(Integer paramInteger1, Integer paramInteger2);
  
  Integer getAllCount();
  
  Integer editArticle(Article paramArticle);
  
  Integer delArticle(Integer paramInteger);
  
  Integer delArticleBatch(Integer[] paramArrayOfInteger);
  
  Integer changeIsTop(Integer paramInteger);
}


/* Location:              D:\java\web.war!\WEB-INF\lib\service-1.0-SNAPSHOT.jar!\com\wanl\service\ArticleService.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.0.7
 */
