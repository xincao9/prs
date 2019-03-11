package com.xincao9.prs.api.model;

/**
 *
 * @author xincao9@gmail.com
 * @param <T>
 */
public class Request<T> {

    private String oid;
    private T data;

    public Request() {
    }

    public Request(String oid, T data) {
        this.oid = oid;
        this.data = data;
    }

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

}
