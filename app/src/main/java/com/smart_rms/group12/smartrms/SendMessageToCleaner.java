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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import Beans.DB;
import Beans.User;

public class SendMessageToCleaner extends AppCompatActivity {

    User user;

    EditText message;
    URL url = new URL("sendMessage.php");

    StringRequest request;
    RequestQueue requestQueue;

    DB database;
    SQLiteDatabase sqLiteDatabase;
    SQLiteDatabase sqLiteDatabase1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_message_to_cleaner);

        database = new DB(getApplicationContext());
        sqLiteDatabase = database.getReadableDatabase();
        sqLiteDatabase1 = database.getWritableDatabase();

        user = database.getUser(sqLiteDatabase);

        requestQueue = Volley.newRequestQueue(this);

        message = (EditText) findViewById(R.id.MessageET);
    }

    public void BackToMainMenu(View view) {
        Intent intent = new Intent(SendMessageToCleaner.this,UserArea.class);
        startActivity(intent);
    }

    public void SendMessage(View view) {
        if(!message.getText().toString().equals("")){
            request = new StringRequest(Request.Method.POST, url.getUrl(), new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);

                        String jsonMessage = jsonObject.getString("message");

                        if(!jsonMessage.equals("There is not available waiter")){
                            message.setText("");
                        }
                        PopUpMsg("Notification",jsonMessage);

                    }
                    //Network error
                    catch (JSONException e) {

                        e.printStackTrace();
                    }
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
                    hashMap.put("user_id",user.getUserID());
                    hashMap.put("message",message.getText().toString());
                    return hashMap;
                }
            };
            requestQueue.add(request);
        }
        //empty text
        else {

        }
    }

    public void PopUpMsg(String title,String msg) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(SendMessageToCleaner.this);

        // set title
        alertDialogBuilder.setTitle(title);

        // set dialog message
        alertDialogBuilder
                .setMessage(msg)
                .setNegativeButton("Okay",new DialogInterface.OnClickListener() {
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
