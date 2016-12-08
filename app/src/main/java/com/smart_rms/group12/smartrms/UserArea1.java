package com.smart_rms.group12.smartrms;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import Beans.DB;
import Beans.MenuItems;
import Beans.User;

public class UserArea1 extends AppCompatActivity {
    User user;

    DB database;
    SQLiteDatabase sqLiteDatabase;
    SQLiteDatabase sqLiteDatabase1;

    URL url = new URL("setToken.php");
    URL url1 = new URL("logoutCleaner.php");

    StringRequest request;
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_area1);

        database = new DB(getApplicationContext());
        sqLiteDatabase = database.getReadableDatabase();
        sqLiteDatabase1 = database.getWritableDatabase();

        //Intent prIntent = getIntent();
        //user = (User) prIntent.getSerializableExtra("user");
        user = database.getUser(sqLiteDatabase);

        TextView name = (TextView)findViewById(R.id.Name1TV);
        name.setText(user.getFullName());

        requestQueue = Volley.newRequestQueue(this);

        setToken();
    }

    public void ViewHistory(View view) {
        Intent intent = new Intent(UserArea1.this,ViewHistory.class);
        startActivity(intent);
    }

    public void PendingWork(View view) {
        Intent intent = new Intent(UserArea1.this,PendingWork.class);
        startActivity(intent);

    }

    public void Logout(View view) {
        database.logout(sqLiteDatabase1);

        request = new StringRequest(Request.Method.POST, url1.getUrl(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Intent intent = new Intent(UserArea1.this,Login.class);
                startActivity(intent);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<String, String>();
                hashMap.put("user_id", user.getUserID());
                return hashMap;
            }
        };
        requestQueue.add(request);

    }

    private void setToken() {

        //SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(getString(R.string.FCM_PREF), Context.MODE_PRIVATE);
        String resent_token = FirebaseInstanceId.getInstance().getToken();
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(getString(R.string.FCM_PREF), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(getString(R.string.FCM_TOKEN),resent_token);
        editor.commit();
        final String token = sharedPreferences.getString(getString(R.string.FCM_TOKEN),"");

        final String finalToken = token;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url.getUrl(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("token", finalToken);
                params.put("userId", user.getUserID());

                return params;
            }
        };
        NotificationMessege.getmInstance(UserArea1.this).addToRequestque(stringRequest);
    }

}
