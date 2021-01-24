/**
 * @Yang
 *
 * Copyright (c) 2020 yang www.theaic.com 344295704@qq.com
 *
 */

package com.wanl.service.impl;

import com.wanl.annotation.SwitchingDataSource;
import com.wanl.constant.EsmConstant;
import com.wanl.entity.Address;
import com.wanl.entity.EsmAccount;
import com.wanl.entity.EsmUser;
import com.wanl.entity.Order;
import com.wanl.entity.OrderItem;
import com.wanl.entity.Product;
import com.wanl.entity.Result;
import com.wanl.entity.ShopCart;
import com.wanl.mapper.AccountMapper;
import com.wanl.mapper.EsmUserMapper;
import com.wanl.mapper.OrderMapper;
import com.wanl.mapper.ProductMapper;
import com.wanl.mapper.ShopCartMapper;
import com.wanl.service.OrderService;
import com.wanl.utils.CreationNumber;
import com.wanl.utils.UUIDUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@SwitchingDataSource
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private ShopCartMapper shopCartMapper;
    @Autowired
    private EsmUserMapper userMapper;
    @Autowired
    private AccountMapper accountMapper;
    @Autowired
    private ProductMapper productMapper;

    @SuppressWarnings("unused")
    @Transactional(rollbackFor = { Exception.class })
    public Result createOrder(String[] cartId) {
        List<ShopCart> shopCarts = new ArrayList<ShopCart>();
        for (String id : cartId) {
            ShopCart shopCart = this.shopCartMapper.getShopCartById(Integer.valueOf(Integer.parseInt(id)));
            shopCarts.add(shopCart);
            this.shopCartMapper.del(id);
        }
        if (shopCarts == null) {
            return new Result(Integer.valueOf(0), "数据有误!", Integer.valueOf(0), Integer.valueOf(0));
        }
        Order order = new Order();
        order.setId(CreationNumber.makeOrderCode(UUIDUtils.getCode()));
        EsmUser user = this.userMapper.findUserById(((ShopCart) shopCarts.get(0)).getUser().getId());
        order.setUser(user);
        order.setCreateDate(new Date());
        order.setPayDate(null);
        order.setDeliveryDate(null);
        order.setConfirmDate(null);
        order.setStatus(EsmConstant.ORDER_NOTPAY);
        order.setAddress(null);
        Double total = Double.valueOf(0.0D);
        for (ShopCart shopCart : shopCarts) {
            total = Double.valueOf(total.doubleValue()
                    + shopCart.getAmount().intValue() * Double.parseDouble(shopCart.getProduct().getPrice()));
            OrderItem orderItem = new OrderItem();
            orderItem.setNumber(shopCart.getAmount().toString());
            orderItem.setOrder(order);
            orderItem.setProduct(shopCart.getProduct());
            orderItem.setUser(user);
            order.getOrderItems().add(orderItem);
        }
        order.setOrderTotal(total.toString());
        this.orderMapper.create(order);
        for (OrderItem o : order.getOrderItems()) {
            this.orderMapper.createOrderItem(o);
        }

        return new Result(Integer.valueOf(200), "创建成功!", Integer.valueOf(0), order.getId());
    }

    public Result getOrderInfo(String id) {
        if (!StringUtils.isNotBlank(id)) {
            return new Result(Integer.valueOf(0), "数据有误!", Integer.valueOf(0), Integer.valueOf(0));
        }
        Order order = this.orderMapper.getOrder(id);
        if (order == null) {
            return new Result(Integer.valueOf(0), "数据有误!", Integer.valueOf(0), Integer.valueOf(0));
        }
        List<OrderItem> orderItems = this.orderMapper.getOrderItem(id);
        Double total = Double.valueOf(0.0D);
        for (OrderItem orderItem : orderItems) {
            total = Double.valueOf(total.doubleValue()
                    + Integer.parseInt(orderItem.getNumber()) * Double.parseDouble(orderItem.getProduct().getPrice()));
        }
        order.setOrderTotal(total.toString());
        order.setOrderItems(orderItems);
        return new Result(Integer.valueOf(200), "数据成功!", Integer.valueOf(0), order);
    }

    public List<Address> getAddress(String id) {
        return this.orderMapper.getAddressList(id);
    }

    @Transactional(rollbackFor = { Exception.class })
    public Result payOrder(String address, String orderId) {
        Order chorder = this.orderMapper.getOrder(orderId);
        if (chorder == null) {
            return new Result(Integer.valueOf(0), "数据有误!", Integer.valueOf(0), Integer.valueOf(0));
        }
        Result orderInfo = getOrderInfo(orderId);
        Order order = (Order) orderInfo.getData();
        EsmAccount account = this.accountMapper.get(order.getUser().getId());
        if (account.getBalance().doubleValue() < Double.parseDouble(order.getOrderTotal())) {
            return new Result(Integer.valueOf(-1), "对不起余额不足!", Integer.valueOf(0), Integer.valueOf(0));
        }
        Address orderAddress = this.orderMapper.getAddress(address);
        order.setAddress(orderAddress);
        order.setStatus(EsmConstant.ORDER_SHIPPED);
        order.setPayDate(new Date());
        order.setDeliveryDate(new Date());
        this.orderMapper.update(order);
        account.setBalance(
                Double.valueOf(account.getBalance().doubleValue() - Double.parseDouble(order.getOrderTotal())));
        this.accountMapper.update(account);
        List<OrderItem> orderItems = this.orderMapper.getOrderItem(orderId);
        for (OrderItem orderItem : orderItems) {
            Product product = orderItem.getProduct();
            product.setBuyCount(Integer.valueOf(Integer.parseInt(orderItem.getNumber())));
            product.setStock(Integer.valueOf(product.getStock().intValue() - Integer.parseInt(orderItem.getNumber())));
            this.productMapper.update(product);
        }
        return new Result(Integer.valueOf(200), "付款成功!");
    }

    public Result confirm(String orderId) {
        Result orderInfo = getOrderInfo(orderId);
        Order order = (Order) orderInfo.getData();
        if (order == null) {
            return new Result(Integer.valueOf(0), "数据有误!", Integer.valueOf(0), Integer.valueOf(0));
        }
        order.setStatus(EsmConstant.ORDER_SUCCESS);
        order.setConfirmDate(new Date());
        this.orderMapper.update(order);
        return new Result(Integer.valueOf(200), "确认成功!");
    }

    public List<Order> orderList(String id) {
        return this.orderMapper.getOrderList(id);
    }

    public Result del(String id) {
        Result orderInfo = getOrderInfo(id);
        if (orderInfo.getStatus().intValue() == 0) {
            return new Result(Integer.valueOf(0), "数据有误!", Integer.valueOf(0), Integer.valueOf(0));
        }
        Order order = (Order) orderInfo.getData();
        for (OrderItem o : order.getOrderItems()) {
            this.orderMapper.delOrderItem(o.getId());
        }
        this.orderMapper.del(id);
        return new Result(Integer.valueOf(200), "删除成功!");
    }

    public Integer addAddress(Address address, String city, String id) {
        Integer row = Integer.valueOf(0);
        if (address != null && StringUtils.isNotBlank(city) && StringUtils.isNotBlank(id)) {
            EsmUser user = this.userMapper.findUserById(id);
            String[] split = city.split("/");
            address.setProvince(split[0]);
            address.setCity(split[1]);
            address.setCounty(split[2]);
            address.setUser(user);
            row = this.orderMapper.createAddress(address);
        }
        return row;
    }

    public static void main(String[] args) {
        String a = "北京市";
        String[] split = a.split("/");
        System.out.println(Arrays.toString(split));
    }
}
