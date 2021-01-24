/**
 * @Yang
 *
 * Copyright (c) 2020 yang www.theaic.com 344295704@qq.com
 *
 */

package com.wanl.mapper;

import com.wanl.entity.Property;
import java.util.List;

public interface PropertyMapper {
    Property findPropertyById(Integer paramInteger);

    List<Property> find(Integer paramInteger);
}

