/**
 * @Yang
 *
 * Copyright (c) 2020 yang www.theaic.com 344295704@qq.com
 *
 */

package com.wanl.entity;

import java.io.Serializable;

public class ProductImage implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private Integer id;
    private Product product;
    private String img;

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Product getProduct() {
        return this.product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public String getImg() {
        return this.img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String toString() {
        return "ProductImage{id=" + this.id + ", product=" + this.product + ", img='" + this.img + '\'' + '}';
    }
}
