package com.wanl.entity;

/**
 * @ClassName QueryResponseResult
 * @Description TODO
 * @Author Administrator
 * @Date 2020-12-31 12:59
 * @Version 2.0
 **/
public class QueryResponseResult<T> {

    private Integer status;
    private String message;
    private Integer count;
    private Object data;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public QueryResponseResult(Integer status, String message, Integer count, Object data) {
        this.status = status;
        this.message = message;
        this.count = count;
        this.data = data;
    }
}
