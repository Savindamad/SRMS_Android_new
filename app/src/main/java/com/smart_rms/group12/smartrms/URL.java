package com.smart_rms.group12.smartrms;

import com.android.volley.Response;

/**
 * Created by savinda on 11/3/16.
 */

public class URL{
    static String ip = "192.168.1.5";
    String url = "http://192.168.1.3/smart_rms/";
    URL(String name){
        url+=name;
    }
    public String getUrl() {
        return url;
    }
    public void setIp(String ip){
        this.ip=ip;
    }
    public String getIp(){
        return ip;
    }
}
