/**
 * @Yang
 *
 * Copyright (c) 2020 yang www.theaic.com 344295704@qq.com
 *
 */

package com.wanl.service;

import com.wanl.entity.Review;
import java.util.List;

public interface ReviewService {
  List<Review> getReviews(Integer paramInteger);
  
  Integer review(String paramString1, String paramString2, String paramString3);
}


/* Location:              D:\java\web.war!\WEB-INF\lib\service-1.0-SNAPSHOT.jar!\com\wanl\service\ReviewService.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.0.7
 */
