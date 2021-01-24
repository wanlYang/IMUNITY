/**
 * @Yang
 *
 * Copyright (c) 2020 yang www.theaic.com 344295704@qq.com
 *
 */

package com.wanl.service.impl;

import com.wanl.annotation.SwitchingDataSource;
import com.wanl.constant.EsmConstant;
import com.wanl.entity.Category;
import com.wanl.mapper.CategoryMapper;
import com.wanl.service.CategoryService;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@SwitchingDataSource
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryMapper categoryMapper;

    public List<Category> treeCategory(List<Category> categories, Integer parentId) {
        List<Category> childCateList = new ArrayList<Category>();
        Iterator<Category> iterator = categories.iterator();
        while (iterator.hasNext()) {
            Category category = (Category) iterator.next();
            if (category.getParentId().intValue() == parentId.intValue()) {
                childCateList.add(category);
            }
        }

        for (Category category : childCateList) {

            if (category.getParentId().intValue() != 0) {
                category.setChildren(treeCategory(categories, category.getId()));
            }
        }

        if (childCateList.size() == 0) {
            return null;
        }
        return childCateList;
    }

    public List<Category> getCateGories() {
        return this.categoryMapper.findAll();
    }

    public Category getCateGory(Integer id) {
        return this.categoryMapper.findCateById(id);
    }

    public List<Category> getTopCateGory() {
        return this.categoryMapper.findCateByParentId(Integer.valueOf(0));
    }

    public Integer addCateGory(Category category) {
        return this.categoryMapper.add(category);
    }

    public Integer editCateGory(Category category) {
        return this.categoryMapper.update(category);
    }

    public Integer delCateGory(Integer id) {
        List<Category> categories = this.categoryMapper.findAll();
        List<Category> child = treeCategory(categories, id);
        if (child != null) {
            return Integer.valueOf(-2);
        }
        return this.categoryMapper.del(id);
    }

    public Integer getCateGoryCount() {
        return this.categoryMapper.getCount();
    }

    public List<Category> getAllCateGory() {
        return this.categoryMapper.findAll();
    }

    public List<Category> getCateGory() {
        List<Category> categories = this.categoryMapper.findAll();
        List<Category> categoriesList = new ArrayList<Category>();
        for (int i = 0; i < categories.size(); i++) {
            if (((Category) categories.get(i)).getParentId().intValue() == 0) {
                categoriesList.add(categories.get(i));
            }
        }
        for (Category category : categoriesList) {
            category.setChildren(treeCategory(categories, category.getId()));
        }

        return categoriesList;
    }

    public List<Category> getSkirt() {
        return getCategory(EsmConstant.CATE_SKIRT);
    }

    public List<Category> getClothes() {
        return getCategory(EsmConstant.CATE_CLOTHES);
    }

    public List<Category> getCategory(Integer id) {
        List<Category> categories = this.categoryMapper.findAll();
        List<Category> categoriesList = new ArrayList<Category>();
        for (int i = 0; i < categories.size(); i++) {
            if (((Category) categories.get(i)).getParentId().intValue() == id.intValue()) {
                categoriesList.add(categories.get(i));
            }
        }
        for (Category category : categoriesList) {
            category.setChildren(treeCategory(categories, category.getId()));
        }
        return categoriesList;
    }

    public List<Category> getBooties() {
        return getCategory(EsmConstant.CATE_BOOTIES);
    }
}
