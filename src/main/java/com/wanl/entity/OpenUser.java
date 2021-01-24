/**
 * @Yang
 *
 * Copyright (c) 2020 yang www.theaic.com 344295704@qq.com
 *
 */

package com.wanl.entity;

public class OpenUser {
    private Integer id;
    private User user;
    private String openId;
    private String openType;
    private String nickname;
    private String avatar;

    public Integer getId() {
        return this.id;
    }

    public User getUser() {
        return this.user;
    }

    public String getOpenId() {
        return this.openId;
    }

    public String getOpenType() {
        return this.openType;
    }

    public String getNickname() {
        return this.nickname;
    }

    public String getAvatar() {
        return this.avatar;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public void setOpenType(String openType) {
        this.openType = openType;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String toString() {
        return "OpenUser{id=" + this.id + ", user=" + this.user + ", openId='" + this.openId + '\'' + ", openType='"
                + this.openType + '\'' + ", nickname='" + this.nickname + '\'' + ", avatar='" + this.avatar + '\''
                + '}';
    }
}
