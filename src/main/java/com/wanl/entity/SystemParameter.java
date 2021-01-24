/**
 * @Yang
 *
 * Copyright (c) 2020 yang www.theaic.com 344295704@qq.com
 *
 */

package com.wanl.entity;

public class SystemParameter {
    private Integer websiteId;
    private String websiteName;
    private String websiteVersion;
    private String websiteAuthor;
    private String websiteHomePage;
    private String websiteServer;
    private String websiteDataBase;
    private String websiteMaxUpload;
    private String websitePowerby;
    private String websiteDescription;
    private String websiteRecord;

    public Integer getWebsiteId() {
        return this.websiteId;
    }

    public void setWebsiteId(Integer websiteId) {
        this.websiteId = websiteId;
    }

    public String getWebsiteName() {
        return this.websiteName;
    }

    public void setWebsiteName(String websiteName) {
        this.websiteName = websiteName;
    }

    public String getWebsiteVersion() {
        return this.websiteVersion;
    }

    public void setWebsiteVersion(String websiteVersion) {
        this.websiteVersion = websiteVersion;
    }

    public String getWebsiteAuthor() {
        return this.websiteAuthor;
    }

    public void setWebsiteAuthor(String websiteAuthor) {
        this.websiteAuthor = websiteAuthor;
    }

    public String getWebsiteHomePage() {
        return this.websiteHomePage;
    }

    public void setWebsiteHomePage(String websiteHomePage) {
        this.websiteHomePage = websiteHomePage;
    }

    public String getWebsiteServer() {
        return this.websiteServer;
    }

    public void setWebsiteServer(String websiteServer) {
        this.websiteServer = websiteServer;
    }

    public String getWebsiteDataBase() {
        return this.websiteDataBase;
    }

    public void setWebsiteDataBase(String websiteDataBase) {
        this.websiteDataBase = websiteDataBase;
    }

    public String getWebsiteMaxUpload() {
        return this.websiteMaxUpload;
    }

    public void setWebsiteMaxUpload(String websiteMaxUpload) {
        this.websiteMaxUpload = websiteMaxUpload;
    }

    public String getWebsitePowerby() {
        return this.websitePowerby;
    }

    public void setWebsitePowerby(String websitePowerby) {
        this.websitePowerby = websitePowerby;
    }

    public String getWebsiteDescription() {
        return this.websiteDescription;
    }

    public void setWebsiteDescription(String websiteDescription) {
        this.websiteDescription = websiteDescription;
    }

    public String getWebsiteRecord() {
        return this.websiteRecord;
    }

    public void setWebsiteRecord(String websiteRecord) {
        this.websiteRecord = websiteRecord;
    }

    public String toString() {
        return "SystemParam [websiteId=" + this.websiteId + ", websiteName=" + this.websiteName + ", websiteVersion="
                + this.websiteVersion + ", websiteAuthor=" + this.websiteAuthor + ", websiteHomePage="
                + this.websiteHomePage + ", websiteServer=" + this.websiteServer + ", websiteDataBase="
                + this.websiteDataBase + ", websiteMaxUpload=" + this.websiteMaxUpload + ", websitePowerby="
                + this.websitePowerby + ", websiteDescription=" + this.websiteDescription + ", websiteRecord="
                + this.websiteRecord + "]";
    }
}
