package com.handheld.upsizeuhf.model;

public class QueryService {
    public int uid;
    public String path;

    public QueryService(int uid, String path) {
        this.path = path;
        this.uid = uid;
    }

    public QueryService() {
        this.path = "";
        this.uid = 0;
    }
}
