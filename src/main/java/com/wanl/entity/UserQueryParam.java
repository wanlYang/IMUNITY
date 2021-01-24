/**
 * @Yang
 *
 * Copyright (c) 2020 yang www.theaic.com 344295704@qq.com
 *
 */

package com.wanl.entity;

public class UserQueryParam {
    private String keyType;
    private String keyWord;

    public String getKeyType() {
        return this.keyType;
    }

    public void setKeyType(String keyType) {
        this.keyType = keyType;
    }

    public String getKeyWord() {
        return this.keyWord;
    }

    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
    }

    public String toString() {
        return "UserQueryParam [keyType=" + this.keyType + ", keyWord=" + this.keyWord + "]";
    }
}
