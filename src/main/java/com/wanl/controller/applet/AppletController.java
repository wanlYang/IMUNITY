/**
 * @Yang
 *
 * Copyright (c) 2020 yang www.theaic.com 344295704@qq.com
 *
 */

package com.wanl.controller.applet;

import com.wanl.entity.*;
import com.wanl.service.CategoryService;
import com.wanl.service.ProductService;
import com.wanl.service.PropertyValueService;
import com.wanl.service.ReviewService;
import com.wanl.utils.RegexUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 小程序接口控制器
 */
@Controller
@RestController
public class AppletController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private PropertyValueService propertyValueService;
    @Autowired
    private ReviewService reviewService;

    /**
     * 小程序首页
     * @return
     */
    @RequestMapping({ "/applet/index" })
    public Result getIndex() {
        List<Category> categories = categoryService.getTopCateGory();
        for (Category category:categories) {
            category.setProducts(productService.getProductByCate(category.getId()));
        }
        Result result = new Result();
        result.setMessage("获取成功!");
        result.setStatus(Integer.valueOf(200));
        result.setCount(Integer.valueOf(categories.size()));
        Category category = new Category();
        category.setId(0);
        category.setTitle("首页");
        category.setProducts(productService.getAppletHotproduct(0,4));
        categories.add(0,category);
        result.setData(categories);
        return result;
    }

    /**
     * 小程序首页
     * @return
     */
    @RequestMapping({ "/applet/getmore" })
    public Result getMore(Integer index,Integer page) {
        Result result = new Result();
        //判断是否是首页获取数据* limit.intValue()
        if (Integer.valueOf(index) == 0){
            List<Product> appletHotproduct = productService.getAppletHotproduct( page * 4, 4);
            result.setStatus(200);
            result.setData(appletHotproduct);
            result.setMessage("获取成功!");
            result.setStatus(Integer.valueOf(200));
            return result;
        }
        List<Product> productByCate = productService.getAppletProductByCate(index,page);


        result.setMessage("获取成功!");
        result.setStatus(Integer.valueOf(200));
        result.setData(productByCate);
        result.setCount(productByCate.size());
        return result;
    }

    /**
     * 小程序商品详细信息
     * @return
     */
    @RequestMapping(value = { "/applet/product" })
    public Result detail(String product_id) {
        Result result = new Result();
        if (!RegexUtils.isNumber(product_id)) {
            result.setMessage("获取失败!参数异常!");
            result.setStatus(-1);
        }
        int parseInt = Integer.parseInt(product_id);
        Product product = this.productService.getProduct(Integer.valueOf(parseInt));

        List<PropertyValue> propertyValues = this.propertyValueService.getPropertyValue(Integer.valueOf(parseInt));

        List<Review> reviews = this.reviewService.getReviews(Integer.valueOf(parseInt));
        Map<String,Object> data = new HashMap<>();
        data.put("product",product);
        data.put("propertyValues",propertyValues);
        data.put("reviews",reviews);
        result.setMessage("获取成功!");
        result.setStatus(Integer.valueOf(200));
        result.setData(data);
        return result;
    }
}
