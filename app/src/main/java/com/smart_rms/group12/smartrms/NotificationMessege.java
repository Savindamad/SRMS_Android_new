package com.smart_rms.group12.smartrms;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by savinda on 12/2/16.
 */

public class NotificationMessege {
    private static NotificationMessege mInstance;
    private static Context mCtx;
    private RequestQueue requestQueue;

    private NotificationMessege(Context context){
        mCtx = context;
        requestQueue = getRequestQueue();
    }

    private RequestQueue getRequestQueue(){
        if(requestQueue==null){
            requestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
        }
        return requestQueue;
    }

    public static synchronized NotificationMessege getmInstance(Context context){
         if(mInstance==null){
             mInstance = new NotificationMessege(context);
         }
        return mInstance;
    }

    public<T> void addToRequestque(Request<T> request){
        getRequestQueue().add(request);
    }
}
