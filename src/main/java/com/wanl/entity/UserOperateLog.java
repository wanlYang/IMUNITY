/**
 * @Yang
 *
 * Copyright (c) 2020 yang www.theaic.com 344295704@qq.com
 *
 */

package com.wanl.entity;

import java.util.Date;

public class UserOperateLog {
    private Long id;
    private String requestUrl;
    private String requestMethod;
    private String ipAddress;
    private Long timeConsuming;
    private String isAbnormal;
    private String operator;
    private String modelName;
    private Date operatingTime;

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRequestUrl() {
        return this.requestUrl;
    }

    public void setRequestUrl(String requestUrl) {
        this.requestUrl = requestUrl;
    }

    public String getRequestMethod() {
        return this.requestMethod;
    }

    public void setRequestMethod(String requestMethod) {
        this.requestMethod = requestMethod;
    }

    public String getIpAddress() {
        return this.ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public Long getTimeConsuming() {
        return this.timeConsuming;
    }

    public void setTimeConsuming(Long timeConsuming) {
        this.timeConsuming = timeConsuming;
    }

    public String getIsAbnormal() {
        return this.isAbnormal;
    }

    public void setIsAbnormal(String isAbnormal) {
        this.isAbnormal = isAbnormal;
    }

    public String getOperator() {
        return this.operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getModelName() {
        return this.modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public Date getOperatingTime() {
        return this.operatingTime;
    }

    public void setOperatingTime(Date operatingTime) {
        this.operatingTime = operatingTime;
    }

    public String toString() {
        return "UserOperateLog [id=" + this.id + ", requestUrl=" + this.requestUrl + ", requestMethod="
                + this.requestMethod + ", ipAddress=" + this.ipAddress + ", timeConsuming=" + this.timeConsuming
                + ", isAbnormal=" + this.isAbnormal + ", operator=" + this.operator + ", modelName=" + this.modelName
                + ", operatingTime=" + this.operatingTime + "]";
    }
}
