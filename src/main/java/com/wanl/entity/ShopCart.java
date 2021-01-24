/**
 * @Yang
 *
 * Copyright (c) 2020 yang www.theaic.com 344295704@qq.com
 *
 */

package com.wanl.entity;
public class ShopCart{
    private Integer id;
    private EsmUser user;
    private Product product;
    private Integer amount;
    private String subPrice;
    
    public Integer getId() { 
        return this.id; 
        }
    public void setId(Integer id) { 
        this.id = id; 
    }
    public EsmUser getUser() { 
        return this.user; 
    }
    public void setUser(EsmUser user) { 
        this.user = user; 
    }
    public Product getProduct() { 
        return this.product; 
    }
    public void setProduct(Product product) { 
        this.product = product; 
    }
    public Integer getAmount() { 
        return this.amount; 
    }
    public void setAmount(Integer amount) { 
        this.amount = amount; 
    }
    public String getSubPrice() { 
        return this.subPrice; 
    }
    public void setSubPrice(String subPrice) { 
        this.subPrice = subPrice; 
    }
    public String toString() { 
        return "ShopCart{id=" + this.id + ", "
                + "user=" + this.user + ","
                + " product=" + this.product + ", amount=" + this.amount + ", subPrice='" + this.subPrice + '\'' + '}'; }
}


