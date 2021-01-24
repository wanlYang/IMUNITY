/**
 * @Yang
 *
 * Copyright (c) 2020 yang www.theaic.com 344295704@qq.com
 *
 */

package com.wanl.entity;

import java.util.Date;
import org.springframework.format.annotation.DateTimeFormat;

public class CommunityTheme {
    private Integer themeId;
    private String themeTitle;
    private User user;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    private Integer status;

    public Integer getThemeId() {
        return this.themeId;
    }

    public void setThemeId(Integer themeId) {
        this.themeId = themeId;
    }

    public String getThemeTitle() {
        return this.themeTitle;
    }

    public void setThemeTitle(String themeTitle) {
        this.themeTitle = themeTitle;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getStatus() {
        return this.status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String toString() {
        return "CommunityTheme [themeId=" + this.themeId + ", themeTitle=" + this.themeTitle + ", user=" + this.user
                + ", createTime=" + this.createTime + ", status=" + this.status + "]";
    }
}
