/**
 * @Yang
 *
 * Copyright (c) 2020 yang www.theaic.com 344295704@qq.com
 *
 */

package com.wanl.controller;

import com.wanl.controller.CateGoryController;
import com.wanl.entity.Category;
import com.wanl.entity.Product;
import com.wanl.entity.QueryResponseResult;
import com.wanl.entity.Result;
import com.wanl.service.CategoryService;
import java.util.List;
import java.util.Map;

import com.wanl.service.ProductService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Author Administrator
 * @Description 商品分类控制类
 * @Date 15:45 2020-12-11
 * @Param
 * @return
 **/
@Controller
@RequestMapping({ "/category" })
@Validated
public class CateGoryController {
    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ProductService productService;

    @RequestMapping(value = "/item/page")
    public String itemPage(){

        return "templates/category/category_item";
    }

    @ResponseBody
    @RequestMapping({ "/list" })
    public Result getCateGory() {
        List<Category> categories = this.categoryService.getCateGory();
        try {
            Thread.sleep(1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return new Result(Integer.valueOf(200), "获取成功!", Integer.valueOf(0), categories);
    }

    @ResponseBody
    @RequestMapping(value="/item/data",method = RequestMethod.POST)
    public Result getItemData(Integer itemId,Integer curr,Integer limit) {
        if(itemId == null){
            return new Result(-1,"获取失败!参数异常!");
        }
        List<Product> products = productService.getProductByCategoryPage(itemId,curr,limit);
        return new Result(Integer.valueOf(200), "获取成功!", products.size(), products);
    }

    /**
     * @Author Administrator
     * @Description //获取导航标题
     * @Date 18:19 2020-12-12
     * @Param [itemId, curr, limit]
     * @return com.wanl.entity.Result
     **/
    @ResponseBody
    @RequestMapping(value="/item/title",method = RequestMethod.POST)
    public Result getItemTitle(Integer itemId) {
        if(itemId == null){
            return new Result(-1,"获取失败!参数异常!");
        }
        Category category = categoryService.getCateGory(itemId);
        return new Result(Integer.valueOf(200), "获取成功!", 1, category);
    }

    @ResponseBody
    @RequestMapping(value="/item/count",method = RequestMethod.POST)
    public Result getItemCount(Integer itemId) {
        if(itemId == null){
            return new Result(-1,"获取失败!参数异常!");
        }
        Integer count = productService.getProductCountByCategory(itemId);
        return new Result(Integer.valueOf(200), "获取成功!",count, null);
    }

    @ResponseBody
    @RequestMapping({ "/list/skirt" })
    public Result getSkirt() {
        List<Category> categories = this.categoryService.getSkirt();

        return new Result(Integer.valueOf(200), "获取成功!", Integer.valueOf(0), categories);
    }

    @ResponseBody
    @RequestMapping({ "/list/clothes" })
    public Result getClothes() {
        List<Category> categories = this.categoryService.getClothes();

        return new Result(Integer.valueOf(200), "获取成功!", Integer.valueOf(0), categories);
    }

    @ResponseBody
    @RequestMapping({ "/list/booties" })
    public Result getBooties() {
        List<Category> categories = this.categoryService.getBooties();

        return new Result(Integer.valueOf(200), "获取成功!", Integer.valueOf(0), categories);
    }

    @RequestMapping({"/product/{page}/{size}"})
    @ResponseBody
    public Map<String, Object> findProductPage(@PathVariable("page")int page, @PathVariable("size") int size){


        return productService.findProductPages(page,size);
    }
}
