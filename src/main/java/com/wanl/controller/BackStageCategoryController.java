/**
 * @Yang
 *
 * Copyright (c) 2020 yang www.theaic.com 344295704@qq.com
 *
 */

package com.wanl.controller;

import com.wanl.controller.BackStageCategoryController;
import com.wanl.entity.Category;
import com.wanl.entity.Result;
import com.wanl.service.CategoryService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping({ "/admin/manager/shop/category" })
public class BackStageCategoryController {
    @Autowired
    private CategoryService categoryService;

    @RequestMapping(value = { "/list" }, method = { RequestMethod.POST })
    @ResponseBody
    public Result categoryList() {
        Result result = new Result();
        List<Category> categories = this.categoryService.getCateGories();
        result.setMessage("添加成功!");
        result.setStatus(Integer.valueOf(200));
        result.setData(categories);
        return result;
    }

    @RequestMapping(value = { "/get" }, method = { RequestMethod.POST })
    @ResponseBody
    public Result category(Integer id) {
        Result result = new Result();
        Category category = this.categoryService.getCateGory(id);
        result.setMessage("添加成功!");
        result.setStatus(Integer.valueOf(200));
        result.setData(category);
        return result;
    }

    @RequestMapping(value = { "/top" }, method = { RequestMethod.POST })
    @ResponseBody
    public Result top() {
        Result result = new Result();
        List<Category> categories = this.categoryService.getTopCateGory();
        result.setMessage("获取成功!");
        result.setStatus(Integer.valueOf(200));
        result.setData(categories);
        return result;
    }

    @RequestMapping(value = { "/submit/add" }, method = { RequestMethod.POST })
    @ResponseBody
    public Result add(Category category) {
        Result result = new Result();
        Integer integer = this.categoryService.addCateGory(category);
        result.setMessage("添加成功!");
        result.setStatus(Integer.valueOf(200));
        result.setData(integer);
        return result;
    }

    @RequestMapping(value = { "/submit/edit" }, method = { RequestMethod.POST })
    @ResponseBody
    public Result edit(Category category) {
        Result result = new Result();
        Integer integer = this.categoryService.editCateGory(category);
        result.setMessage("编辑成功!");
        result.setStatus(Integer.valueOf(200));
        result.setData(integer);
        return result;
    }

    @RequestMapping(value = { "/submit/del" }, method = { RequestMethod.POST })
    @ResponseBody
    public Result del(Integer id) {
        Result result = new Result();
        Integer integer = this.categoryService.delCateGory(id);
        if (integer.intValue() == -2) {
            result.setMessage("删除失败!包含子导航!");
            result.setStatus(Integer.valueOf(-1));
            result.setData(integer);
            return result;
        }
        result.setMessage("删除成功!");
        result.setStatus(Integer.valueOf(200));
        result.setData(integer);
        return result;
    }

    @RequestMapping(value = { "/count" }, method = { RequestMethod.GET })
    @ResponseBody
    public Result count() {
        Result result = new Result();
        Integer integer = this.categoryService.getCateGoryCount();

        result.setMessage("获取成功!");
        result.setStatus(Integer.valueOf(200));
        result.setCount(integer);
        return result;
    }
}
