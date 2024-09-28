package com.taskmanager.model;

import java.util.Map;

public class Request {
    private Map<String, String> headers;
    private Object body;

    public Request(Map<String, String> headers, Object body) {
        this.headers = headers;
        this.body = body;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public Object getBody() {
        return body;
    }
}