/**
 * @Yang
 *
 * Copyright (c) 2020 yang www.theaic.com 344295704@qq.com
 *
 */

package com.wanl.controller;
import com.wanl.entity.Category;
import com.wanl.entity.Product;
import com.wanl.entity.Result;
import com.wanl.service.CategoryService;
import com.wanl.service.ProductService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 * @Author Administrator
 * @Description //商城首页
 * @Date 13:42 2020-12-11
 * @Param
 * @return
 **/
@Controller
public class IndexController {
    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;
    
    @RequestMapping(value = { "/" }, method = { RequestMethod.GET })
    public ModelAndView index() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("index");
        return modelAndView;
    }


    @RequestMapping({ "/index/hot/product" })
    @ResponseBody
    public Result getHotProduct() {
        List<Product> products = this.productService.getHotproduct();
        Result result = new Result();
        result.setMessage("获取成功!");
        try {
            Thread.sleep(1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        result.setStatus(Integer.valueOf(200));
        result.setCount(Integer.valueOf(products.size()));
        result.setData(products);
        return result;
    }

    @RequestMapping({ "/index/product/skirt" })
    @ResponseBody
    public Result getSkirtProduct() {
        List<Product> products = this.productService.getSkirtProduct();
        Result result = new Result();
        result.setMessage("获取成功!");
        result.setStatus(Integer.valueOf(200));
        result.setCount(Integer.valueOf(products.size()));
        result.setData(products);
        try {
            Thread.sleep(1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return result;
    }

    @RequestMapping({ "/index/product/clothes" })
    @ResponseBody
    public Result getClothesProduct() {
        List<Product> products = this.productService.getClothesProduct();
        Result result = new Result();
        result.setMessage("获取成功!");
        result.setData(products);
        result.setStatus(Integer.valueOf(200));
        result.setCount(Integer.valueOf(products.size()));
        try {
            Thread.sleep(1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return result;
    }

    @RequestMapping({ "/index/product/booties" })
    @ResponseBody
    public Result getBootiesProduct() {
        List<Product> products = this.productService.getBootiesProduct();
        Result result = new Result();
        result.setMessage("获取成功!");
        result.setData(products);
        result.setStatus(Integer.valueOf(200));
        result.setCount(Integer.valueOf(products.size()));
        try {
            Thread.sleep(1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return result;
    }


}
