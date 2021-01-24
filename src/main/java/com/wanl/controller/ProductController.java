/**
 * @Yang
 *
 * Copyright (c) 2020 yang www.theaic.com 344295704@qq.com
 *
 */

package com.wanl.controller;

import com.wanl.controller.ProductController;
import com.wanl.entity.Product;
import com.wanl.entity.PropertyValue;
import com.wanl.entity.Result;
import com.wanl.entity.Review;
import com.wanl.service.ProductService;
import com.wanl.service.PropertyValueService;
import com.wanl.service.ReviewService;
import com.wanl.utils.RegexUtils;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping({ "/product" })
public class ProductController {
    @Autowired
    private ProductService productService;
    @Autowired
    private PropertyValueService propertyValueService;
    @Autowired
    private ReviewService reviewService;

    @RequestMapping(value = { "/{id}" }, method = { RequestMethod.GET })
    public ModelAndView detail(@PathVariable("id") String id, ModelAndView modelAndView) {
        if (!RegexUtils.isNumber(id)) {
            modelAndView.setViewName("redirect:/");
            return modelAndView;
        }
        int parseInt = Integer.parseInt(id);
        Product product = this.productService.getProduct(Integer.valueOf(parseInt));

        List<PropertyValue> propertyValues = this.propertyValueService.getPropertyValue(Integer.valueOf(parseInt));

        List<Review> reviews = this.reviewService.getReviews(Integer.valueOf(parseInt));
        modelAndView.setViewName("templates/item_show");
        modelAndView.addObject("product", product);
        modelAndView.addObject("propertyValues", propertyValues);
        modelAndView.addObject("reviews", reviews);
        return modelAndView;
    }

    @RequestMapping(value = { "/recommend" }, method = { RequestMethod.POST })
    @ResponseBody
    public Result getSkirtProduct(String id) {
        if (!RegexUtils.isNumber(id)) {
            return new Result(Integer.valueOf(-1), "参数异常!");
        }
        List<Product> products = this.productService.getRecommendProduct(Integer.valueOf(Integer.parseInt(id)));
        Result result = new Result();
        result.setMessage("获取成功!");
        result.setStatus(Integer.valueOf(200));
        result.setCount(Integer.valueOf(products.size()));
        result.setData(products);
        try {
            Thread.sleep(2000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return result;
    }
}
