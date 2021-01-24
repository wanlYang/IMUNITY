/**
 * @Yang
 *
 * Copyright (c) 2020 yang www.theaic.com 344295704@qq.com
 *
 */

package com.wanl.mapper;

import com.wanl.entity.Address;
import com.wanl.entity.Order;
import com.wanl.entity.OrderItem;
import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface OrderMapper {
    Integer create(Order paramOrder);

    Integer createOrderItem(OrderItem paramOrderItem);

    Order getOrder(@Param("id")String paramString);

    Address getAddress(@Param("id")String paramString);

    List<OrderItem> getOrderItem(@Param("id")String paramString);

    List<Address> getAddressList(@Param("id")String paramString);

    Integer update(Order paramOrder);

    List<Order> getOrderList(@Param("id")String paramString);

    Integer delOrderItem(@Param("id")int paramInt);

    Integer del(@Param("id")String paramString);

    Integer createAddress(Address paramAddress);
}

