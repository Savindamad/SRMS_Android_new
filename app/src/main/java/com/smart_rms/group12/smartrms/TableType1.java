package com.smart_rms.group12.smartrms;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

import Beans.*;

public class TableType1 extends AppCompatActivity {

    ArrayList<MenuItems> menu = new ArrayList<>();
    User user;
    URL url = new URL("getTableInfo.php");
    URL url1 = new URL("checkTable.php");

    String table[] = new String[19];

    StringRequest request;

    RequestQueue requestQueue;
    RequestQueue requestQueue1;

    Drawable btn1;
    Drawable btn2;
    Drawable btn3;

    int flag=0;

    Button table1,table2,table3,table4,table5,table6,table7,table8;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table_type1);

        Intent prIntent = getIntent();
        menu = (ArrayList<MenuItems>)prIntent.getSerializableExtra("menu");
        user = (User) prIntent.getSerializableExtra("user");

        requestQueue = Volley.newRequestQueue(this);
        requestQueue1 = Volley.newRequestQueue(this);

        table1 = (Button)findViewById(R.id.T1Table1Btn);
        table2 = (Button)findViewById(R.id.T1Table2Btn);
        table3 = (Button)findViewById(R.id.T1Table3Btn);
        table4 = (Button)findViewById(R.id.T1Table4Btn);
        table5 = (Button)findViewById(R.id.T1Table5Btn);
        table6 = (Button)findViewById(R.id.T1Table6Btn);
        table7 = (Button)findViewById(R.id.T1Table7Btn);
        table8 = (Button)findViewById(R.id.T1Table8Btn);

        btn1 = getResources().getDrawable(R.drawable.button_style4);
        btn2 = getResources().getDrawable(R.drawable.button_style5);
        btn3 = getResources().getDrawable(R.drawable.button_style6);

        getTableInfo();
    }


    public void T1Table1(View view) {
        checkTable("1");
    }

    public void T1Table2(View view) {
        checkTable("2");
    }

    public void T1Table3(View view) {
        checkTable("3");
    }

    public void T1Table4(View view) {
        checkTable("4");
    }

    public void T1Table5(View view) {
        checkTable("5");
    }

    public void T1Table6(View view) {
        checkTable("6");
    }

    public void T1Table7(View view) {
        checkTable("7");
    }

    public void T1Table8(View view) {
        checkTable("8");
    }

    private void getTableInfo() {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url.getUrl(), new Response.Listener<JSONObject>() {
            @Override
            //response --> json array
            public void onResponse(JSONObject response) {
                try {
                    JSONArray tableInfo = response.getJSONArray("table_info");
                    int size = tableInfo.length();

                    for (int i = 0; i < size; i++) {

                        JSONObject table = tableInfo.getJSONObject(i);
                        String tableNo = table.getString("table_no");
                        String status = table.getString("status");


                        if(Integer.parseInt(tableNo)<9){
                            changeButtonColor(Integer.parseInt(tableNo),status);
                        }
                    }

                } catch (JSONException e) {
                    PopUpMsg("Network error",""+e);
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                PopUpMsg("Login fail",""+error);
            }
        });
        requestQueue1.add(jsonObjectRequest);
    }
    public void PopUpMsg(String title,String msg) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(TableType1.this);

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
    public void selectTable(String tableNo){
        Intent intent = new Intent(TableType1.this,Table.class);
        intent.putExtra("user",user);
        intent.putExtra("menu",menu);
        intent.putExtra("tableNo",tableNo);
        startActivity(intent);
    }

    public void checkTable(final String tableNo){
        request = new StringRequest(Request.Method.POST, url1.getUrl(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (!(jsonObject.names().get(0).equals("fail"))) {

                        //add username and name
                        String status = jsonObject.getString("status");
                        if(status.equals("0") || status.equals(user.getUserID())){
                            selectTable(tableNo);
                        }
                        else{
                            PopUpMsg("You can not access this table","Table is already received");
                        }
                    }
                    else {
                        PopUpMsg("Login fail","Invalid username or password..");
                        flag = 0;
                    }
                }
                //Network error
                catch (JSONException e) {
                    PopUpMsg("Login fail","Network error..\n"+e);
                    flag = 0;

                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                PopUpMsg("Login fail","Network error..\n"+error);
                flag = 0;
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
    public void changeButtonColor(int tableNo, String status){
        switch (tableNo) {
            case 1:
                table[0]=status;
                if(status.equals("0")){
                    table1.setBackground(btn1);
                }
                else if(status.equals(user.getUserID())){
                    table1.setBackground(btn2);
                }
                else{
                    table1.setBackground(btn3);
                }
                break;
            case 2:
                table[1]=status;
                if(status.equals("0")){
                    table1.setBackground(btn1);
                }
                else if(status.equals(user.getUserID())){
                    table1.setBackground(btn2);
                }
                else{
                    table1.setBackground(btn3);
                }
                break;
            case 3:
                table[2]=status;
                if(status.equals("0")){
                    table1.setBackground(btn1);
                }
                else if(status.equals(user.getUserID())){
                    table1.setBackground(btn2);
                }
                else{
                    table1.setBackground(btn3);
                }
                break;
            case 4:
                table[3]=status;
                if(status.equals("0")){
                    table1.setBackground(btn1);
                }
                else if(status.equals(user.getUserID())){
                    table1.setBackground(btn2);
                }
                else{
                    table1.setBackground(btn3);
                }
                break;
            case 5:
                table[4]=status;
                if(status.equals("0")){
                    table1.setBackground(btn1);
                }
                else if(status.equals(user.getUserID())){
                    table1.setBackground(btn2);
                }
                else{
                    table1.setBackground(btn3);
                }
                break;
            case 6:
                table[5]=status;
                if(status.equals("0")){
                    table1.setBackground(btn1);
                }
                else if(status.equals(user.getUserID())){
                    table1.setBackground(btn2);
                }
                else{
                    table1.setBackground(btn3);
                }
                break;
            case 7:
                table[6]=status;
                if(status.equals("0")){
                    table1.setBackground(btn1);
                }
                else if(status.equals(user.getUserID())){
                    table1.setBackground(btn2);
                }
                else{
                    table1.setBackground(btn3);
                }
                break;
            case 8:
                table[7]=status;
                if(status.equals("0")){
                    table1.setBackground(btn1);
                }
                else if(status.equals(user.getUserID())){
                    table1.setBackground(btn2);
                }
                else{
                    table1.setBackground(btn3);
                }
                break;
        }

    }
}
