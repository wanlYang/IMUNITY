/**
 * @Yang
 *
 * Copyright (c) 2020 yang www.theaic.com 344295704@qq.com
 *
 */

package com.wanl.service;

import com.wanl.entity.Product;
import com.wanl.entity.QueryResponseResult;
import com.wanl.entity.Result;
import java.util.List;
import java.util.Map;


public interface ProductService {
  List<Product> getProductList(Integer paramInteger1, Integer paramInteger2);
  
  Integer getProductCount();
  
  Result addProduct(Product paramProduct, String paramString);
  
  Result delProduct(String paramString);
  
  Result update(Product paramProduct, String paramString);
  
  Result recovery(String paramString);
  
  List<Product> getHotproduct();
  
  List<Product> getSkirtProduct();
  
  List<Product> getClothesProduct();
  
  List<Product> getBootiesProduct();
  
  Product getProduct(Integer paramInteger);
  
  List<Product> getRecommendProduct(Integer paramInteger);

  List<Product> getProductByCate(Integer id);

  List<Product> getAppletHotproduct(Integer index,Integer limit);

  List<Product> getAppletProductByCate(Integer index, Integer page);

  List<Product> getProductByCategoryPage(Integer itemId,Integer curr,Integer limit);
  /**
   * @Author Administrator
   * @Description //根据分类获取对应产品数量
   * @Date 15:04 2020-12-12
   * @Param [itemId]
   * @return java.lang.Integer
   **/
  Integer getProductCountByCategory(Integer itemId);

  /**
   * @Author Administrator
   * @Description //获取商品分页数据
   * @Date 12:52 2020-12-31
   * @Param [page, size]
   * @return
   **/
  Map<String,Object> findProductPages(int page, int size);
}

