/**
 * @Yang
 *
 * Copyright (c) 2020 yang www.theaic.com 344295704@qq.com
 *
 */

package com.wanl.entity;

import java.io.Serializable;
import java.util.List;

public class Category implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private Integer id;
    private Integer parentId;
    private String title;
    private String description;
    private Integer display;
    private List<Category> children;
    private List<Product> products;

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getParentId() {
        return this.parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getDisplay() {
        return this.display;
    }

    public void setDisplay(Integer display) {
        this.display = display;
    }

    public List<Category> getChildren() {
        return this.children;
    }

    public void setChildren(List<Category> children) {
        this.children = children;
    }

    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", parentId=" + parentId +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", display=" + display +
                ", children=" + children +
                ", products=" + products +
                '}';
    }
}
