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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import Beans.DB;
import Beans.MenuItems;
import Beans.User;

public class UserArea extends AppCompatActivity {

    ArrayList<MenuItems> menu = new ArrayList<>();
    User user;

    TextView name;
    URL url = new URL("setToken.php");

    DB database;
    SQLiteDatabase sqLiteDatabase;
    SQLiteDatabase sqLiteDatabase1;

    URL url1 = new URL("logoutWaiter.php");

    StringRequest request;
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_area);

        database = new DB(getApplicationContext());
        sqLiteDatabase = database.getReadableDatabase();
        sqLiteDatabase1 = database.getWritableDatabase();

        name =(TextView)findViewById(R.id.Name1TV);

        //Intent prIntent = getIntent();
        //menu = (ArrayList<MenuItems>)prIntent.getSerializableExtra("menu");
        menu = database.getMenu(sqLiteDatabase);
        user = database.getUser(sqLiteDatabase);
        //user = (User) prIntent.getSerializableExtra("user");

        //set employee name
        name.setText(user.getFullName());
        requestQueue = Volley.newRequestQueue(this);

        setToken();
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
            protected Map<String, String> getParams() throws AuthFailureError{
                Map<String, String> params = new HashMap<String, String>();
                params.put("token", finalToken);
                params.put("userId", user.getUserID());

                return params;
            }
        };
        NotificationMessege.getmInstance(UserArea.this).addToRequestque(stringRequest);
    }

    public void Logout(View view) {
        database.logout(sqLiteDatabase1);
        request = new StringRequest(Request.Method.POST, url1.getUrl(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Intent intent = new Intent(UserArea.this,Login.class);
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

    public void ViewMenu(View view) {
    }

    public void PlaceAnOrder(View view) {
        Intent intent = new Intent(UserArea.this, TableTypes.class);
        //intent.putExtra("user",user);
        //intent.putExtra("menu",menu);
        startActivity(intent);
    }
}
