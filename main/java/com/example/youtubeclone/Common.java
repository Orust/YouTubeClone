package com.example.youtubeclone;

import android.app.Application;

public class Common extends Application {

    private static final String API_KEY = "API key";
    private final String clientId = "101466230350-tkhroar87369giah0gv49k4qhcic4fsq.apps.googleusercontent.com";
    private String code = "";
    private String access_token = "aaa";
    private Boolean authorized = false;

    public void init() {
        this.code = "";
        this.access_token = "bbb";
        this.authorized = false;
    }

    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return this.code;
    }

    public void setAccessToken(String access_token) {
        this.access_token = access_token;
    }

    public String getAccessToken() {
        return this.access_token;
    }

    public void setAuthorized(Boolean flag) {
        this.authorized = flag;
    }

    public Boolean getAuthorized() {
        return this.authorized;
    }

}
