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
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import Beans.DB;
import Beans.NotificationMessage;
import Beans.User;

public class ViewHistory extends AppCompatActivity {

    ArrayList<NotificationMessage> workHistory = new ArrayList<NotificationMessage>();

    ArrayAdapter<NotificationMessage> adapter;
    ListView list;

    URL url = new URL("getWorkHistory.php");
    RequestQueue requestQueue;

    DB database;
    SQLiteDatabase sqLiteDatabase;
    SQLiteDatabase sqLiteDatabase1;

    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_history);

        requestQueue = Volley.newRequestQueue(this);
        database = new DB(getApplicationContext());
        sqLiteDatabase = database.getReadableDatabase();
        sqLiteDatabase1 = database.getWritableDatabase();

        user = database.getUser(sqLiteDatabase);
        list = (ListView)findViewById(R.id.workHistoryLv);
        getWorkHistory();
    }

    public void getWorkHistory(){
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
                        String time = work.getString("message_time");

                        // add to menu array list
                        NotificationMessage temp = new NotificationMessage(messageId,message,sendUserId,sendUserName,receivedUserId,receivedUserName,status,tableNo);
                        temp.setTime(time);
                        workHistory.add(temp);
                    }
                    adapter = new ViewHistory.MyListAdapter();
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
        Intent intent = new Intent(ViewHistory.this,UserArea1.class);
        startActivity(intent);
    }

    private class MyListAdapter extends ArrayAdapter<NotificationMessage> {

        public MyListAdapter() {
            super(ViewHistory.this, R.layout.list_view_layout8, workHistory);
        }
        @Override
        public View getView(final int position, View convertView, ViewGroup parent){
            View itemView = convertView;
            if(itemView==null){
                itemView = getLayoutInflater().inflate(R.layout.list_view_layout8,parent,false);
            }
            final NotificationMessage work = workHistory.get(position);

            TextView messageTitle = (TextView)itemView.findViewById(R.id.MessageTitleTV2);
            TextView senderName  = (TextView)itemView.findViewById(R.id.SendByTv1);
            TextView message  = (TextView)itemView.findViewById(R.id.MessageTv2);
            TextView time = (TextView)itemView.findViewById(R.id.TimeTv);
            TextView status  = (TextView)itemView.findViewById(R.id.StatusTv1);


            if(work.getTableNo().equals("0")){
                messageTitle.setText("Need a help");
            }
            else{
                messageTitle.setText("Table "+work.getTableNo());
            }
            message.setText(work.getMessage());
            senderName.setText("Message sent by "+work.getSentUserName());
            time.setText(work.getTime());

            if(work.getStatus().equals("2")){
                status.setText("accepted");
            }
            else if(work.getStatus().equals("3")){
                status.setText("Rejected");
            }

            return itemView;
        }
    }
}
