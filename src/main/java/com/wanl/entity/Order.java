/**
 * @Yang
 *
 * Copyright (c) 2020 yang www.theaic.com 344295704@qq.com
 *
 */

package com.wanl.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Order implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String id;
    private EsmUser user;
    private Date createDate;
    private Date payDate;
    private Date deliveryDate;
    private Date confirmDate;
    private Integer status;
    private String orderTotal;
    private Address address;
    private List<OrderItem> orderItems = new ArrayList<OrderItem>();

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public EsmUser getUser() {
        return this.user;
    }

    public void setUser(EsmUser user) {
        this.user = user;
    }

    public Date getCreateDate() {
        return this.createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getPayDate() {
        return this.payDate;
    }

    public void setPayDate(Date payDate) {
        this.payDate = payDate;
    }

    public Date getDeliveryDate() {
        return this.deliveryDate;
    }

    public void setDeliveryDate(Date deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public Date getConfirmDate() {
        return this.confirmDate;
    }

    public void setConfirmDate(Date confirmDate) {
        this.confirmDate = confirmDate;
    }

    public Integer getStatus() {
        return this.status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getOrderTotal() {
        return this.orderTotal;
    }

    public void setOrderTotal(String orderTotal) {
        this.orderTotal = orderTotal;
    }

    public Address getAddress() {
        return this.address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public List<OrderItem> getOrderItems() {
        return this.orderItems;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    public String toString() {
        return "Order{id='" + this.id + '\'' + ", user=" + this.user + ", createDate=" + this.createDate + ", payDate="
                + this.payDate + ", deliveryDate=" + this.deliveryDate + ", confirmDate=" + this.confirmDate
                + ", status=" + this.status + ", orderTotal='" + this.orderTotal + '\'' + ", address=" + this.address
                + ", orderItems=" + this.orderItems + '}';
    }
}
