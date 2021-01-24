/**
 * @Yang
 *
 * Copyright (c) 2020 yang www.theaic.com 344295704@qq.com
 *
 */

package com.wanl.service;

import com.wanl.entity.Category;
import java.util.List;

public interface CategoryService {
  List<Category> getCateGories();
  
  Category getCateGory(Integer paramInteger);
  
  List<Category> getTopCateGory();
  
  Integer addCateGory(Category paramCategory);
  
  Integer editCateGory(Category paramCategory);
  
  Integer delCateGory(Integer paramInteger);
  
  Integer getCateGoryCount();
  
  List<Category> getAllCateGory();
  
  List<Category> getCateGory();
  
  List<Category> getSkirt();
  
  List<Category> getClothes();
  
  List<Category> getBooties();
}


/* Location:              D:\java\web.war!\WEB-INF\lib\service-1.0-SNAPSHOT.jar!\com\wanl\service\CategoryService.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.0.7
 */
