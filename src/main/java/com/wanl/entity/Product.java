/**
 * @Yang
 *
 * Copyright (c) 2020 yang www.theaic.com 344295704@qq.com
 *
 */

package com.wanl.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class Product implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private Integer id;
    private Category category;
    private String mainTitle;
    private String subTitle;
    private String price;
    private String oldPrice;
    private Integer buyCount;
    private String img;
    private String detail;
    private Integer status;
    private Integer isHot;
    private Integer stock;
    private Date shelfTime;
    private ProductImage firstProductImage;
    private List<ProductImage> productSingleImages;
    private Integer reviewCount;
    private String cateIds;

    public String getCateIds() {
        return cateIds;
    }

    public void setCateIds(String cateIds) {
        this.cateIds = cateIds;
    }

    public Integer getReviewCount() {
        return this.reviewCount;
    }

    public void setReviewCount(Integer reviewCount) {
        this.reviewCount = reviewCount;
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Category getCategory() {
        return this.category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getMainTitle() {
        return this.mainTitle;
    }

    public void setMainTitle(String mainTitle) {
        this.mainTitle = mainTitle;
    }

    public String getSubTitle() {
        return this.subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public String getPrice() {
        return this.price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getOldPrice() {
        return this.oldPrice;
    }

    public void setOldPrice(String oldPrice) {
        this.oldPrice = oldPrice;
    }

    public Integer getBuyCount() {
        return this.buyCount;
    }

    public void setBuyCount(Integer buyCount) {
        this.buyCount = buyCount;
    }

    public String getImg() {
        return this.img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getDetail() {
        return this.detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public Integer getStatus() {
        return this.status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getIsHot() {
        return this.isHot;
    }

    public void setIsHot(Integer isHot) {
        this.isHot = isHot;
    }

    public Integer getStock() {
        return this.stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Date getShelfTime() {
        return this.shelfTime;
    }

    public void setShelfTime(Date shelfTime) {
        this.shelfTime = shelfTime;
    }

    public ProductImage getFirstProductImage() {
        return this.firstProductImage;
    }

    public void setFirstProductImage(ProductImage firstProductImage) {
        this.firstProductImage = firstProductImage;
    }

    public List<ProductImage> getProductSingleImages() {
        return this.productSingleImages;
    }

    public void setProductSingleImages(List<ProductImage> productSingleImages) {
        this.productSingleImages = productSingleImages;
    }

    public String toString() {
        return "Product{id=" + this.id + ", category=" + this.category + ", mainTitle='" + this.mainTitle + '\''
                + ", subTitle='" + this.subTitle + '\'' + ", price='" + this.price + '\'' + ", oldPrice='"
                + this.oldPrice + '\'' + ", buyCount=" + this.buyCount + ", img='" + this.img + '\'' + ", detail='"
                + this.detail + '\'' + ", status=" + this.status + ", isHot=" + this.isHot + ", stock=" + this.stock
                + ", shelfTime=" + this.shelfTime + ", firstProductImage=" + this.firstProductImage
                + ", productSingleImages=" + this.productSingleImages + ", reviewCount=" + this.reviewCount + '}';
    }
}
