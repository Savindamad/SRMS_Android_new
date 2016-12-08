package com.smart_rms.group12.smartrms;

import com.android.volley.Response;

/**
 * Created by savinda on 11/3/16.
 */

public class URL{
    String url = "http://192.168.1.2/smart_rms/";
    URL(String name){
        url+=name;
    }
    public String getUrl() {
        return url;
    }
}
