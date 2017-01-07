package com.smart_rms.group12.smartrms;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import Beans.DB;
import Beans.MenuItems;
import Beans.User;

public class Login extends AppCompatActivity {
    EditText username;
    EditText password;

    StringRequest request;
    JsonObjectRequest request1;

    RequestQueue requestQueue;
    RequestQueue requestQueue1;

    URL url = new URL("loginNew.php");
    URL url1 = new URL("getMenu.php");

    ArrayList<MenuItems> menu = new ArrayList<>();
    User user;
    DB database;
    SQLiteDatabase sqLiteDatabase;
    SQLiteDatabase sqLiteDatabase1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        database = new DB(this);
        sqLiteDatabase = database.getWritableDatabase();
        sqLiteDatabase1 = database.getReadableDatabase();


        username = (EditText) findViewById(R.id.UsernameET);
        password = (EditText) findViewById(R.id.PasswordET);

        requestQueue = Volley.newRequestQueue(this);
        requestQueue1 = Volley.newRequestQueue(this);

        user = database.getUser(sqLiteDatabase1);
        if(user!=null){
            if(user.getType().equals("WAITER")){
                getMenu();
            }
            else if(user.getType().equals("CLEANER")){
                Intent intent = new Intent(Login.this, UserArea1.class);
                startActivity(intent);
            }
        }
    }

    public void Login(View view) {
        if (Validate()) {

            request = new StringRequest(Request.Method.POST, url.getUrl(), new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        //login success
                        if (!(jsonObject.names().get(0).equals("fail"))) {

                            //add username and name
                            String userID = jsonObject.getString("user_id");
                            String fname = jsonObject.getString("f_name");
                            String lname = jsonObject.getString("l_name");
                            String type = jsonObject.getString("user_type");

                            user = new User(fname,lname,userID,type);
                            database.setUser(user,sqLiteDatabase);
                            if (type.equals("WAITER")) {
                                getMenu();
                                 //start user area activity
                            }
                            else if (type.equals("CLEANER")){
                                Intent intent = new Intent(Login.this, UserArea1.class);
                                username.setText("");
                                password.setText("");
                                startActivity(intent);
                            }
                            else {
                                //invalid user
                                PopUpMsg("Login fail","Invalid user");
                            }

                        }
                        //login fail
                        else {
                            PopUpMsg("Login fail","Invalid username or password..");
                        }
                    }
                    //Network error
                    catch (JSONException e) {
                        PopUpMsg("Login fail","Network error..\n"+e);

                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    PopUpMsg("Login fail","Network error..\n"+error);
                }
            }) {
                @Override
                //map user name and password
                protected Map<String, String> getParams() throws AuthFailureError {
                    HashMap<String, String> hashMap = new HashMap<String, String>();
                    hashMap.put("username", username.getText().toString()); //todo encrypt username and password
                    hashMap.put("password", password.getText().toString());
                    return hashMap;
                }
            };
            requestQueue.add(request);

        }

    }

    void getMenu(){

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url1.getUrl(), new Response.Listener<JSONObject>() {
            @Override
            //response --> json array
            public void onResponse(JSONObject response) {
                try {
                    JSONArray menuItems = response.getJSONArray("menu_items");
                    int size = menuItems.length();

                    for (int i = 0; i < size; i++) {

                        JSONObject menuItem = menuItems.getJSONObject(i);
                        String itemCode = menuItem.getString("item_id");
                        String itemName = menuItem.getString("item_name");
                        String itemType = menuItem.getString("item_type");
                        String itemDescription = menuItem.getString("description");
                        String itemPrice = menuItem.getString("price");

                        // add to menu array list
                        menu.add(new MenuItems(itemCode,itemName,itemType,itemDescription,itemPrice));

                    }
                    database.addMenuItem(menu,sqLiteDatabase);
                    Intent intent = new Intent(Login.this, UserArea.class);
                    //intent.putExtra("user",user);
                    //intent.putExtra("menu",menu);
                    username.setText("");
                    password.setText("");

                    startActivity(intent);

                } catch (JSONException e) {
                    PopUpMsg("Login fail","Network error("+e+")");
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                PopUpMsg("Login fail","Network error("+error+")");
            }
        });
        requestQueue1.add(jsonObjectRequest);
    }

    public boolean Validate() {
        if (username.getText().toString().equals("")) {
            PopUpMsg("Login fail","Required username field");
            return false;
        } else if (password.getText().toString().equals("")) {
            PopUpMsg("Login fail","Required password field");
            return false;
        }
        return true;
    }

    public void PopUpMsg(String title,String msg) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Login.this);

        // set title
        alertDialogBuilder.setTitle(title);

        // set dialog message
        alertDialogBuilder
                .setMessage(msg)
                .setNegativeButton("Retry",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        dialog.cancel();
                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }
}
