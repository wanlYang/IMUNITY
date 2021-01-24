/**
 * @Yang
 *
 * Copyright (c) 2020 yang www.theaic.com 344295704@qq.com
 *
 */

package com.wanl.service.impl;

import com.wanl.annotation.SwitchingDataSource;
import com.wanl.entity.Product;
import com.wanl.entity.Property;
import com.wanl.entity.PropertyValue;
import com.wanl.entity.Result;
import com.wanl.mapper.PropertyMapper;
import com.wanl.mapper.PropertyValueMapper;
import com.wanl.service.PropertyValueService;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@SwitchingDataSource
public class PropertyValueServiceImpl implements PropertyValueService {
    @Autowired
    private PropertyValueMapper propertyValueMapper;
    @Autowired
    private PropertyMapper propertyMapper;

    public List<PropertyValue> getPropertyValue(Integer id) {
        return this.propertyValueMapper.getPropertyValue(id);
    }

    public void init(Product product) {
        List<Property> properties = this.propertyMapper.find(product.getCategory().getId());
        for (Property property : properties) {
            PropertyValue propertyValue = this.propertyValueMapper.getPropertyValues(property.getId(), product.getId());
            if (propertyValue == null) {
                propertyValue = new PropertyValue();
                propertyValue.setProduct(product);
                propertyValue.setProperty(property);
                add(propertyValue);
            }
        }
    }

    public void add(PropertyValue propertyValue) {
        this.propertyValueMapper.create(propertyValue);
    }

    public Result update(String propertyValueId, String value) {
        PropertyValue propertyValue = this.propertyValueMapper.getPropertyValueById(propertyValueId);
        if (!StringUtils.isNotBlank(value)) {
            return new Result(Integer.valueOf(-1), "数据有误!", Integer.valueOf(0), propertyValue);
        }
        propertyValue.setValue(value);
        Integer row = this.propertyValueMapper.update(propertyValue);
        return new Result(Integer.valueOf(200), "修改成功!", Integer.valueOf(0), row);
    }

    public Integer del(Integer id) {
        return this.propertyValueMapper.del(id);
    }
}
