/**
 * @Yang
 *
 * Copyright (c) 2020 yang www.theaic.com 344295704@qq.com
 *
 */

package com.wanl.service.impl;

import com.wanl.annotation.SwitchingDataSource;
import com.wanl.entity.Review;
import com.wanl.mapper.EsmUserMapper;
import com.wanl.mapper.ProductMapper;
import com.wanl.mapper.ReviewMapper;
import com.wanl.service.ReviewService;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

@Service
@SwitchingDataSource
public class ReviewServiceImpl implements ReviewService {
    @Autowired
    private ReviewMapper reviewMapper;
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private EsmUserMapper userMapper;

    public List<Review> getReviews(Integer id) {
        List<Review> reviews = this.reviewMapper.findReviewList(id);
        if (reviews != null) {
            for (Review review : reviews) {
                String anonymousName = review.getUser().getAnonymousName();
                review.getUser().setAnonymousName(anonymousName);
            }
        }
        return reviews;
    }

    @CacheEvict(value = { "product_detail" }, key = "#root.args[0]")
    public Integer review(String productId, String content, String userId) {
        Review review = new Review();
        review.setContent(content);
        review.setProduct(this.productMapper.findProductById(Integer.valueOf(Integer.parseInt(productId))));
        review.setTime(new Date());
        review.setUser(this.userMapper.findUserById(userId));
        return this.reviewMapper.insert(review);
    }
}
