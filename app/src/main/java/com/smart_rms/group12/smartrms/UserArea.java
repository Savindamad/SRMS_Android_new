package com.smart_rms.group12.smartrms;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import Beans.MenuItems;
import Beans.User;

public class UserArea extends AppCompatActivity {

    ArrayList<MenuItems> menu = new ArrayList<>();
    User user;

    TextView name;
    String token;

    StringRequest request;
    RequestQueue requestQueue;

    URL url = new URL("setToken.php");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_area);

        name =(TextView)findViewById(R.id.NameTV);

        Intent prIntent = getIntent();
        menu = (ArrayList<MenuItems>)prIntent.getSerializableExtra("menu");
        user = (User) prIntent.getSerializableExtra("user");

        //set employee name
        name.setText(user.getFullName());

        requestQueue = Volley.newRequestQueue(this);

        //FirebaseMessaging.getInstance().subscribeToTopic("test");
        //FirebaseInstanceId.getInstance().getToken();

    }

    private void setToken() {
        request = new StringRequest(com.android.volley.Request.Method.POST, url.getUrl(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }) {
            @Override
            //map user name and password
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<String, String>();
                hashMap.put("username", user.getUserID()); //todo encrypt username and password
                hashMap.put("token", token);
                return hashMap;
            }
        };
        requestQueue.add(request);

    }

    public void Logout(View view) {
        Intent intent = new Intent(UserArea.this,Login.class);
        startActivity(intent);
    }

    public void ViewMenu(View view) {
    }

    public void PlaceAnOrder(View view) {
        Intent intent = new Intent(UserArea.this, TableTypes.class);
        intent.putExtra("user",user);
        intent.putExtra("menu",menu);
        startActivity(intent);
    }
}
