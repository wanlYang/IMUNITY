/**
 * @Yang
 *
 * Copyright (c) 2020 yang www.theaic.com 344295704@qq.com
 *
 */

package com.wanl.mapper;

import com.wanl.entity.Review;
import java.util.List;

public interface ReviewMapper {
    Integer getCount(Integer paramInteger);

    List<Review> findReviewList(Integer paramInteger);

    Integer insert(Review paramReview);
}

