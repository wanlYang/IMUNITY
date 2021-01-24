/**
 * @Yang
 *
 * Copyright (c) 2020 yang www.theaic.com 344295704@qq.com
 *
 */

package com.wanl.service;

import com.wanl.entity.Address;
import com.wanl.entity.Order;
import com.wanl.entity.Result;
import java.util.List;

public interface OrderService {
  Result createOrder(String[] paramArrayOfString);
  
  Result getOrderInfo(String paramString);
  
  List<Address> getAddress(String paramString);
  
  Result payOrder(String paramString1, String paramString2);
  
  Result confirm(String paramString);
  
  List<Order> orderList(String paramString);
  
  Result del(String paramString);
  
  Integer addAddress(Address paramAddress, String paramString1, String paramString2);
}

