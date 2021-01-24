/**
 * @Yang
 *
 * Copyright (c) 2020 yang www.theaic.com 344295704@qq.com
 *
 */

package com.wanl.service;

import com.wanl.entity.Product;
import com.wanl.entity.PropertyValue;
import com.wanl.entity.Result;
import java.util.List;

public interface PropertyValueService {
  List<PropertyValue> getPropertyValue(Integer paramInteger);
  
  void init(Product paramProduct);
  
  void add(PropertyValue paramPropertyValue);
  
  Result update(String paramString1, String paramString2);
  
  Integer del(Integer paramInteger);
}


/* Location:              D:\java\web.war!\WEB-INF\lib\service-1.0-SNAPSHOT.jar!\com\wanl\service\PropertyValueService.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.0.7
 */
