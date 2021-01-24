/**
 * @Yang
 *
 * Copyright (c) 2020 yang www.theaic.com 344295704@qq.com
 *
 */

package com.wanl.entity;

public class Address {
    private int id;
    private String consignee;
    private String province;
    private String city;
    private String county;
    private String street;
    private String tel;
    private String postcode;
    private EsmUser user;

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getConsignee() {
        return this.consignee;
    }

    public void setConsignee(String consignee) {
        this.consignee = consignee;
    }

    public String getProvince() {
        return this.province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return this.city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCounty() {
        return this.county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getStreet() {
        return this.street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getTel() {
        return this.tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getPostcode() {
        return this.postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public EsmUser getUser() {
        return this.user;
    }

    public void setUser(EsmUser user) {
        this.user = user;
    }

    public String toString() {
        return "Address{id=" + this.id + ", consignee='" + this.consignee + '\'' + ", province='" + this.province + '\''
                + ", city='" + this.city + '\'' + ", county='" + this.county + '\'' + ", street='" + this.street + '\''
                + ", tel='" + this.tel + '\'' + ", postcode='" + this.postcode + '\'' + ", user=" + this.user + '}';
    }
}
