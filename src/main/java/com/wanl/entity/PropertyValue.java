/**
 * @Yang
 *
 * Copyright (c) 2020 yang www.theaic.com 344295704@qq.com
 *
 */

package com.wanl.entity;

public class PropertyValue {
    private Integer id;
    private Product product;
    private Property property;
    private String value;

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

    public Property getProperty() {
        return this.property;
    }

    public void setProperty(Property property) {
        this.property = property;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String toString() {
        return "PropertyValue{id=" + this.id + ", product=" + this.product + ", property=" + this.property + ", value='"
                + this.value + '\'' + '}';
    }
}
