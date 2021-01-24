/**
 * @Yang
 *
 * Copyright (c) 2020 yang www.theaic.com 344295704@qq.com
 *
 */

package com.wanl.mapper;

import com.github.pagehelper.Page;
import com.wanl.entity.Product;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ProductMapper {
    Product findProductById(Integer paramInteger);

    Integer update(Product paramProduct);

    List<Product> findAll(@Param("start") int paramInt, @Param("limit") Integer paramInteger);

    Integer getCount();

    Integer create(Product paramProduct);

    Integer del(@Param("id") Integer paramInteger1, @Param("status") Integer paramInteger2);

    List<Product> findAllDetails(@Param("start") int paramInt, @Param("limit") Integer paramInteger);

    Integer updates(Product paramProduct);

    Integer recovery(@Param("productId")Integer paramInteger);

    List<Product> findHotProduct();

    List<Product> findProductByCategoryId(@Param("id") Integer id);

    List<Product> findAppletHotProduct(@Param("index") Integer index, @Param("limit") Integer limit);

    List<Product> findAppletProductByCategoryId(@Param("id") Integer id, @Param("index") Integer index, @Param("limit") Integer limit);

    Integer findProductCountByCategory(@Param("cate_id") Integer itemId);

    /**
     * @Author Administrator
     * @Description //商品分类页面 根据分类页面获取分页数据
     * @Date 15:41 2020-12-12
     * @Param [itemId, i, limit]
     * @return java.util.List<com.wanl.entity.Product>
     **/
    List<Product> findProductByCategoryIdPage(@Param("itemId") Integer itemId, @Param("start") int index, @Param("limit") Integer limit);

    /*
     * @Author Administrator
     * @Description //mybatis分页插件分页
     * @Date 13:07 2020-12-31
     * @Param []
     * @return com.github.pagehelper.Page<com.wanl.entity.Product>
     **/
    Page<Product> findAllHelper();
}

