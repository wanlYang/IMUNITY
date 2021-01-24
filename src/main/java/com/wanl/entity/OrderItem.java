/**
 * @Yang
 *
 * Copyright (c) 2020 yang www.theaic.com 344295704@qq.com
 *
 */

package com.wanl.entity;

import java.io.Serializable;

public class OrderItem implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private int id;
    private Product product;
    private Order order;
    private EsmUser user;
    private String number;

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Product getProduct() {
        return this.product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Order getOrder() {
        return this.order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public EsmUser getUser() {
        return this.user;
    }

    public void setUser(EsmUser user) {
        this.user = user;
    }

    public String getNumber() {
        return this.number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String toString() {
        return "OrderItem{id=" + this.id + ", product=" + this.product + ", order=" + this.order + ", user=" + this.user
                + ", number='" + this.number + '\'' + '}';
    }
}
