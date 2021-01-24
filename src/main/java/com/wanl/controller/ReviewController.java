/**
 * @Yang
 *
 * Copyright (c) 2020 yang www.theaic.com 344295704@qq.com
 *
 */

package com.wanl.controller;

import com.wanl.constant.ImunityConstant;
import com.wanl.controller.ReviewController;
import com.wanl.entity.EsmUser;
import com.wanl.entity.Product;
import com.wanl.entity.Result;
import com.wanl.service.ProductService;
import com.wanl.service.ReviewService;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping({ "/review" })
public class ReviewController {
    @Autowired
    private ProductService productService;
    @Autowired
    private ReviewService reviewService;

    @RequestMapping(value = { "/{id}" }, method = { RequestMethod.GET })
    public ModelAndView orderDetail(@PathVariable("id") String id, ModelAndView modelAndView, HttpSession session) {
        Product product = this.productService.getProduct(Integer.valueOf(Integer.parseInt(id)));
        modelAndView.setViewName("templates/product_review");
        modelAndView.addObject("product", product);

        return modelAndView;
    }

    @RequestMapping(value = { "/submit" }, method = { RequestMethod.POST })
    @ResponseBody
    public Result submit(String productId, String content, HttpSession session) {
        EsmUser user = (EsmUser) session.getAttribute(ImunityConstant.USER_SESSION);
        Integer review = this.reviewService.review(productId, content, user.getId());
        if (review.intValue() <= 0) {
            return new Result(Integer.valueOf(-1), "数据有误!");
        }
        return new Result(Integer.valueOf(200), "评论成功!", Integer.valueOf(0), productId);
    }
}
