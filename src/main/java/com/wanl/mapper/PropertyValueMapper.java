/**
 * @Yang
 *
 * Copyright (c) 2020 yang www.theaic.com 344295704@qq.com
 *
 */

package com.wanl.mapper;

import com.wanl.entity.PropertyValue;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface PropertyValueMapper {
    List<PropertyValue> getPropertyValue(Integer paramInteger);

    PropertyValue getPropertyValues(@Param("id") Integer paramInteger1, @Param("productId") Integer paramInteger2);

    Integer create(PropertyValue paramPropertyValue);

    PropertyValue getPropertyValueById(String paramString);

    Integer update(PropertyValue paramPropertyValue);

    Integer del(Integer paramInteger);
}

