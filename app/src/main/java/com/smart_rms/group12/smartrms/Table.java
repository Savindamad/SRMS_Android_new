package com.smart_rms.group12.smartrms;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import Beans.*;

public class Table extends AppCompatActivity {

    User user;
    //ArrayList<MenuItems> menu = new ArrayList<>();
    String tableNo;

    StringRequest request;
    RequestQueue requestQueue;

    URL url = new URL("updateTable.php");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table);

        requestQueue = Volley.newRequestQueue(this);

        Intent prIntent = getIntent();
        //menu = (ArrayList<MenuItems>)prIntent.getSerializableExtra("menu");
        user = (User) prIntent.getSerializableExtra("user");
        tableNo = prIntent.getStringExtra("tableNo");

    }

    public void AddNewOrder(View view) {
        Intent intent = new Intent(Table.this,AddNewOrder.class);
        intent.putExtra("user",user);
        //intent.putExtra("menu",menu);
        intent.putExtra("tableNo",tableNo);
        startActivity(intent);
    }

    public void EditAndCancel(View view) {
        Intent intent = new Intent(Table.this,AllOrders.class);
        intent.putExtra("user",user);
        intent.putExtra("tableNo",tableNo);
        //intent.putExtra("menu",menu);
        startActivity(intent);
    }

    public void ViewMenu1(View view) {
    }

    public void NotifyCleaner(View view) {
        Intent intent = new Intent(Table.this,AllOrders.class);
        intent.putExtra("tableNo",tableNo);
        //intent.putExtra("menu",menu);
        startActivity(intent);
    }

    public void FinishOrder(View view) {

        request = new StringRequest(Request.Method.POST, url.getUrl(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                    //JSONObject jsonObject = new JSONObject(response);
                    Intent intent = new Intent(Table.this, TableTypes.class);
                    intent.putExtra("user",user);
                    //intent.putExtra("menu",menu);
                    startActivity(intent);

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
                hashMap.put("tableNum", tableNo); //
                hashMap.put("userID", user.getUserID());
                return hashMap;
            }
        };
        requestQueue.add(request);

    }

    public void PopUpMsg(String title,String msg) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Table.this);

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
