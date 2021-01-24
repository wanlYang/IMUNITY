/**
 * @Yang
 *
 * Copyright (c) 2020 yang www.theaic.com 344295704@qq.com
 *
 */

package com.wanl.entity;

import java.util.List;

public class Permission {
    private int id;
    private String name;
    private String descritpion;
    private String url;
    private int pid;
    private List<Role> roles;

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescritpion() {
        return this.descritpion;
    }

    public void setDescritpion(String descritpion) {
        this.descritpion = descritpion;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getPid() {
        return this.pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public String toString() {
        return "Permission [id=" + this.id + ", name=" + this.name + ", descritpion=" + this.descritpion + ", url="
                + this.url + ", pid=" + this.pid + ", roles=" + this.roles + "]";
    }

    public List<Role> getRoles() {
        return this.roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }
}
