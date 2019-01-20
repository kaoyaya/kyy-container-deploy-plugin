package com.kaoyaya.jenkins.cd.cs;

public class ReturnMsg {
    private Integer code;
    private String body;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return "ReturnMsg{" +
                "code=" + code +
                ", body='" + body + '\'' +
                '}';
    }
}
