package com.smart_rms.group12.smartrms;

/**
 * Created by savinda on 11/3/16.
 */

public class URL {
    String url = "http://192.168.1.3/smart_rms/";
    URL(String name){
        url+=name;
    }
    public String getUrl() {
        return url;
    }
}
