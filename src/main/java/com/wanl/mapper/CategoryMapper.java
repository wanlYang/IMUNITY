/**
 * @Yang
 *
 * Copyright (c) 2020 yang www.theaic.com 344295704@qq.com
 *
 */

package com.wanl.mapper;

import com.wanl.entity.Category;
import java.util.List;

public interface CategoryMapper {
    List<Category> findAll();

    Category findCateById(Integer paramInteger);

    List<Category> findCateByParentId(Integer paramInteger);

    Integer add(Category paramCategory);

    Integer update(Category paramCategory);

    Integer del(Integer paramInteger);

    Integer getCount();
}

