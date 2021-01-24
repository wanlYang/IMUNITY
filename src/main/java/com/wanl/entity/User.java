/**
 * @Yang
 *
 * Copyright (c) 2020 yang www.theaic.com 344295704@qq.com
 *
 */

package com.wanl.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import org.apache.ibatis.type.Alias;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Alias("User")
@JsonIgnoreProperties
public class User implements UserDetails {
    private static final long serialVersionUID = 1L;
    private Integer id;
    private String username;
    private String password;
    private String bindemail;
    private String age;
    private String sex;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date birthday;
    private String phone;
    private String headImg;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date regDate;
    private String profile;
    private Integer status;
    private List<Role> roles;
    private Collection<? extends GrantedAuthority> authorities;

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
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

    public String getBindemail() {
        return this.bindemail;
    }

    public void setBindemail(String bindemail) {
        this.bindemail = bindemail;
    }

    public String getAge() {
        return this.age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public void Phone(String age) {
        this.age = age;
    }

    public String getSex() {
        return this.sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public Date getBirthday() {
        return this.birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getPhone() {
        return this.phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getHeadImg() {
        return this.headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    public Date getRegDate() {
        return this.regDate;
    }

    public void setRegDate(Date regDate) {
        this.regDate = regDate;
    }

    public String getProfile() {
        return this.profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public Integer getStatus() {
        return this.status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public List<Role> getRoles() {
        return this.roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public void setAuthorities(Collection<? extends GrantedAuthority> authorities) {
        this.authorities = authorities;
    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    public boolean isAccountNonExpired() {
        return true;
    }

    public boolean isAccountNonLocked() {
        return true;
    }

    public boolean isCredentialsNonExpired() {
        return true;
    }

    public boolean isEnabled() {
        if (this.status != null) {
            return (this.status.intValue() == 1);
        }
        return false;
    }

    public User() {
    }

    public User(Integer id, String username, String password, String bindemail, String age, String sex, Date birthday,
            String phone, String headImg, Date regDate, String profile, Integer status, List<Role> roles,
            Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.bindemail = bindemail;
        this.age = age;
        this.sex = sex;
        this.birthday = birthday;
        this.phone = phone;
        this.headImg = headImg;
        this.regDate = regDate;
        this.profile = profile;
        this.status = status;
        this.roles = roles;
        this.authorities = authorities;
    }

    public String toString() {
        return this.username;
    }

    public int hashCode() {
        return this.username.hashCode();
    }

    public boolean equals(Object obj) {
        return toString().equals(obj.toString());
    }
}
