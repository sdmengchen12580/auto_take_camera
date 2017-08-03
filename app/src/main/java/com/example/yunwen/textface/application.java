package com.example.yunwen.textface;

import android.app.Application;

/**
 * Created by yunwen on 2017/8/2.
 */

public class application extends Application {

    private String imgurl;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }
}
