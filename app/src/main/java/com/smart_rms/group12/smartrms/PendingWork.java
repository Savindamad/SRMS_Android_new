package com.smart_rms.group12.smartrms;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

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

public class PendingWork extends AppCompatActivity {

    ArrayList<NotificationMessage> pendingWork = new ArrayList<NotificationMessage>();
    
    ArrayAdapter<NotificationMessage> adapter;
    ListView list;

    URL url = new URL("getPendingWorks.php");
    URL url1 = new URL("workStatus.php");
    RequestQueue requestQueue;
    RequestQueue requestQueue1;

    DB database;
    SQLiteDatabase sqLiteDatabase;
    SQLiteDatabase sqLiteDatabase1;

    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pendding_work);

        requestQueue = Volley.newRequestQueue(this);
        requestQueue1 = Volley.newRequestQueue(this);
        database = new DB(getApplicationContext());
        sqLiteDatabase = database.getReadableDatabase();
        sqLiteDatabase1 = database.getWritableDatabase();

        user = database.getUser(sqLiteDatabase);
        list = (ListView)findViewById(R.id.PendingWorkLv);
        getPendingWork();

    }
    public void getPendingWork(){
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url.getUrl(), new Response.Listener<JSONObject>() {
            @Override
            //response --> json array
            public void onResponse(JSONObject response) {
                try {
                    JSONArray allPendingWorks = response.getJSONArray("pending_works");
                    int size = allPendingWorks.length();

                    for (int i = 0; i < size; i++) {

                        JSONObject work = allPendingWorks.getJSONObject(i);
                        String messageId = work.getString("id");
                        String message = work.getString("message");
                        String sendUserId = work.getString("sent_user_id");
                        String sendUserName = work.getString("sent_user_name");
                        String receivedUserId = work.getString("received_user_id");
                        String receivedUserName = work.getString("received_user_name");
                        String status = work.getString("status");
                        String tableNo = work.getString("table_no");

                        // add to menu array list
                        NotificationMessage notificationMsg = new NotificationMessage(messageId,message,sendUserId,sendUserName,receivedUserId,receivedUserName,status,tableNo);
                        pendingWork.add(notificationMsg);

                    }
                    adapter = new PendingWork.MyListAdapter();
                    list.setAdapter(adapter);

                } catch (JSONException e) {
                    System.out.println(e);
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error);
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", user.getUserID());
                return params;
            }
        };
        requestQueue.add(jsonObjectRequest);
    }

    public void BackToMainMenu(View view) {
        Intent intent = new Intent(PendingWork.this,UserArea1.class);
        startActivity(intent);
    }

    private class MyListAdapter extends ArrayAdapter<NotificationMessage> {

        public MyListAdapter() {
            super(PendingWork.this, R.layout.list_view_layout7, pendingWork);
        }
        @Override
        public View getView(final int position, View convertView, ViewGroup parent){
            View itemView = convertView;
            if(itemView==null){
                itemView = getLayoutInflater().inflate(R.layout.list_view_layout7,parent,false);
            }
            final NotificationMessage work = pendingWork.get(position);

            TextView messageTitle = (TextView)itemView.findViewById(R.id.MessageTitleTV2);
            TextView senderName  = (TextView)itemView.findViewById(R.id.SendByTv1);
            TextView message  = (TextView)itemView.findViewById(R.id.MessageTv2);
            final Button accept = (Button)itemView.findViewById(R.id.AcceptBtn2);
            final Button decline = (Button)itemView.findViewById(R.id.DeclineBtn);
            final Button finishWork = (Button)itemView.findViewById(R.id.WorkFinishedBtn);

            if(work.getTableNo().equals("0")){
                messageTitle.setText("Need a help");
            }
            else{
                messageTitle.setText("Table "+work.getTableNo());
            }
            message.setText(work.getMessage());
            senderName.setText("Message sent by "+work.getSentUserName());


            if(work.getStatus().equals("0")){
                finishWork.setVisibility(View.GONE);
            }
            else if(work.getStatus().equals("1")){
                accept.setVisibility(View.GONE);
                decline.setVisibility(View.GONE);

            }
            accept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    acceptWork(work);

                    accept.setVisibility(View.GONE);
                    decline.setVisibility(View.GONE);
                    finishWork.setVisibility(View.VISIBLE);
                }
            });
            decline.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    declineWork(work);

                    accept.setVisibility(View.GONE);
                    decline.setVisibility(View.GONE);
                }
            });
            finishWork.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finishWork(work);
                    pendingWork.remove(position);
                    adapter.notifyDataSetChanged();
                }
            });
            return itemView;
        }
    }

    public void acceptWork(final NotificationMessage message){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url1.getUrl(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //message accepted
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
                params.put("status", "accept");
                params.put("message_id", message.getMessageId());
                params.put("sent_user_id", message.getSentUserId());
                params.put("received_user_name", message.getReceivedUserName());

                return params;
            }
        };
        requestQueue1.add(stringRequest);
    }
    public void declineWork(final NotificationMessage message){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url1.getUrl(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //message accepted
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
                params.put("status", "decline");
                params.put("message_id", message.getMessageId());
                params.put("sent_user_id", message.getSentUserId());
                params.put("received_user_name", message.getReceivedUserName());
                return params;
            }
        };
        requestQueue1.add(stringRequest);
    }
    public void finishWork(final NotificationMessage message){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url1.getUrl(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //message accepted
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
                params.put("status", "finish");
                params.put("message_id", message.getMessageId());
                params.put("sent_user_id", message.getSentUserId());
                params.put("received_user_name", message.getReceivedUserName());

                return params;
            }
        };
        requestQueue1.add(stringRequest);
    }
}
