/**
 * @Yang
 *
 * Copyright (c) 2020 yang www.theaic.com 344295704@qq.com
 *
 */

package com.wanl.entity;

import java.util.Date;
import org.springframework.format.annotation.DateTimeFormat;

public class Article {
    private Integer id;
    private User user;
    private Integer replyCount;
    private String title;
    private String contents;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date time;
    private Integer clickCount;
    private Integer flag;
    private User lastReplyUser;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date lastReplyTime;
    private CommunityTheme theme;
    private String thumbnail;
    private Integer isTop;

    public Integer getIsTop() {
        return this.isTop;
    }

    public void setIsTop(Integer isTop) {
        this.isTop = isTop;
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Integer getReplyCount() {
        return this.replyCount;
    }

    public void setReplyCount(Integer replyCount) {
        this.replyCount = replyCount;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContents() {
        return this.contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public Date getTime() {
        return this.time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public Integer getClickCount() {
        return this.clickCount;
    }

    public void setClickCount(Integer clickCount) {
        this.clickCount = clickCount;
    }

    public Integer getFlag() {
        return this.flag;
    }

    public void setFlag(Integer flag) {
        this.flag = flag;
    }

    public User getLastReplyUser() {
        return this.lastReplyUser;
    }

    public void setLastReplyUser(User lastReplyUser) {
        this.lastReplyUser = lastReplyUser;
    }

    public Date getLastReplyTime() {
        return this.lastReplyTime;
    }

    public void setLastReplyTime(Date lastReplyTime) {
        this.lastReplyTime = lastReplyTime;
    }

    public CommunityTheme getTheme() {
        return this.theme;
    }

    public void setTheme(CommunityTheme theme) {
        this.theme = theme;
    }

    public String getThumbnail() {
        return this.thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String toString() {
        return "Article [id=" + this.id + ", user=" + this.user + ", replyCount=" + this.replyCount + ", title="
                + this.title + ", contents=" + this.contents + ", time=" + this.time + ", clickCount=" + this.clickCount
                + ", flag=" + this.flag + ", lastReplyUser=" + this.lastReplyUser + ", lastReplyTime="
                + this.lastReplyTime + ", theme=" + this.theme + ", thumbnail=" + this.thumbnail + ", isTop="
                + this.isTop + "]";
    }
}
