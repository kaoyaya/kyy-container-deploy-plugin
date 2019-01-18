package com.kaoyaya.jenkins.cd.cs;

public class Cert {
    private String Url;
    private String Ca;
    private String Cert;
    private String Key;

    public String getUrl() {
        return Url;
    }

    public void setUrl(String url) {
        Url = url;
    }

    public String getCa() {
        return Ca;
    }

    public void setCa(String ca) {
        Ca = ca;
    }

    public String getCert() {
        return Cert;
    }

    public void setCert(String cert) {
        Cert = cert;
    }

    public String getKey() {
        return Key;
    }

    public void setKey(String key) {
        Key = key;
    }

    public Cert(String url, String ca, String cert, String key) {
        Url = url;
        Ca = ca;
        Cert = cert;
        Key = key;
    }
}
