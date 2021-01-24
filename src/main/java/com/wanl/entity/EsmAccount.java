/**
 * @Yang
 *
 * Copyright (c) 2020 yang www.theaic.com 344295704@qq.com
 *
 */

package com.wanl.entity;

public class EsmAccount {
    private String id;
    private EsmUser user;
    private Double balance;

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

    public Double getBalance() {
        return this.balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public String toString() {
        return "Account{id='" + this.id + '\'' + ", EsmUser=" + this.user + ", balance=" + this.balance + '}';
    }
}
