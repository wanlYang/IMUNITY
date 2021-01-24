/**
 * @Yang
 *
 * Copyright (c) 2020 yang www.theaic.com 344295704@qq.com
 *
 */

package com.wanl.entity;

public class Account {
    private Integer accountId;
    private Double accountBalance;
    private Integer accountStatus;
    private User user;

    public Integer getAccountId() {
        return this.accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    public Double getAccountBalance() {
        return this.accountBalance;
    }

    public void setAccountBalance(Double accountBalance) {
        this.accountBalance = accountBalance;
    }

    public Integer getAccountStatus() {
        return this.accountStatus;
    }

    public void setAccountStatus(Integer accountStatus) {
        this.accountStatus = accountStatus;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String toString() {
        return "Account [accountId=" + this.accountId + ", accountBalance=" + this.accountBalance + ", user="
                + this.user + "]";
    }
}
