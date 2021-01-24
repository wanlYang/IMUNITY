/**
 * @Yang
 *
 * Copyright (c) 2020 yang www.theaic.com 344295704@qq.com
 *
 */

package com.wanl.entity;

import org.apache.ibatis.type.Alias;

@Alias("esm_user")
public class EsmUser {
    private String id;
    private String username;
    private String password;
    private String phone;
    private String email;
    private String headImg;
    private Integer age;
    @SuppressWarnings("unused")
    private String anonymousName;

    public void setAnonymousName(String anonymousName) {
        this.anonymousName = anonymousName;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return this.phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getHeadImg() {
        return this.headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    public Integer getAge() {
        return this.age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String toString() {
        return "User{id=" + this.id + ", username='" + this.username + '\'' + ", password='" + this.password + '\''
                + ", phone='" + this.phone + '\'' + ", email='" + this.email + '\'' + ", headImg='" + this.headImg
                + '\'' + ", age=" + this.age + '}';
    }

    public String getAnonymousName() {
        if (null == this.username) {
            return null;
        }
        if (this.username.length() <= 1) {
            return "*";
        }
        if (this.username.length() == 2) {
            return this.username.substring(0, 1) + "*";
        }
        char[] cs = this.username.toCharArray();
        for (int i = 1; i < cs.length - 1; i++) {
            cs[i] = '*';
        }
        return new String(cs);
    }
}
