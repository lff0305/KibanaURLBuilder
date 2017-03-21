package org.lff.http;

/**
 * Created by liuff on 2016/6/13 16:02
 */
public class HttpResponse {
    private int code;
    private String response;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }
}
