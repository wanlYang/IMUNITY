/**
 * @Yang
 *
 * Copyright (c) 2020 yang www.theaic.com 344295704@qq.com
 *
 */

package com.wanl.mapper;

import com.wanl.entity.ProductImage;
import java.util.List;

public interface ProductImageMapper {
    List<ProductImage> findImagesByProductId(Integer paramInteger);

    ProductImage findImageById(Integer paramInteger);

    Integer create(ProductImage paramProductImage);

    Integer delByProductId(Integer paramInteger);
}

