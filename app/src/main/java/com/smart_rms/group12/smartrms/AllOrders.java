package com.smart_rms.group12.smartrms;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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

import Beans.MenuItems;
import Beans.Order;
import Beans.OrderItem;
import Beans.User;

public class AllOrders extends AppCompatActivity {
    RequestQueue requestQueue;
    URL url = new URL("getAllOrders.php");
    String tableNo;
    User user;

    ArrayList<MenuItems> menu = new ArrayList<>();

    ArrayAdapter<Order> adapter;
    ListView list;

    ArrayList<Order> orderArray = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_orders);
        requestQueue = Volley.newRequestQueue(this);
        list = (ListView)findViewById(R.id.OrderListView);
        adapter = new AllOrders.MyListAdapter();

        Intent prIntent = getIntent();
        tableNo = prIntent.getStringExtra("tableNo");
        menu = (ArrayList<MenuItems>)prIntent.getSerializableExtra("menu");
        user = (User) prIntent.getSerializableExtra("user");
        getOrders();
    }
    public void getOrders(){
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url.getUrl(), new Response.Listener<JSONObject>() {
            @Override
            //response --> json array
            public void onResponse(JSONObject response) {
                try {
                    JSONArray allOrders = response.getJSONArray("orders");
                    int size = allOrders.length();


                    for (int i = 0; i < size; i++) {
                        JSONObject order = allOrders.getJSONObject(i);
                        String status = order.getString("status");
                        
                        JSONArray orderItems = order.getJSONArray("order");
                        String orderNo = "";

                        ArrayList<OrderItem> orderItemArray = new ArrayList<>();

                        int size1 = order.length();
                        for(int j = 0; j<size1; j++){
                            JSONObject orderItem = orderItems.getJSONObject(j);

                            String orderItemId = orderItem.getString("order_item_id");
                            String itemID = orderItem.getString("item_id");
                            String quantity = orderItem.getString("quantity");

                            OrderItem orderItem1 = new OrderItem(itemID,quantity,orderItemId);
                            orderItem1.setItemName(menu);

                            orderItemArray.add(orderItem1);
                            orderNo = orderItem.getString("order_no");
                        }
                        Order allOrder = new Order(orderNo,orderItemArray,status);
                        orderArray.add(allOrder);
                    }
                    list.setAdapter(adapter);

                } catch (JSONException e) {
                    PopUpMsg("e",e+"");
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                PopUpMsg("er",""+error);
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<String, String>();
                hashMap.put("table_no",tableNo);
                return hashMap;
            }
        };
        requestQueue.add(jsonObjectRequest);
    }

    public void PopUpMsg(String title,String msg) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AllOrders.this);

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

    private class MyListAdapter extends ArrayAdapter<Order> {

        public MyListAdapter() {
            super(AllOrders.this, R.layout.list_view_layout5, orderArray);
        }
        @Override
        public View getView(final int position, View convertView, ViewGroup parent){
            View itemView = convertView;
            if(itemView==null){
                itemView = getLayoutInflater().inflate(R.layout.list_view_layout5,parent,false);
            }
            final Order tempItem = orderArray.get(position);
            TextView orderNo = (TextView)itemView.findViewById(R.id.OrderNoTv);
            TextView statusTv  = (TextView)itemView.findViewById(R.id.StatusTv);
            Button ViewBtn = (Button)itemView.findViewById(R.id.ViewOrderBtn1);

            orderNo.setText(""+(position+1));
            if(tempItem.getStatus().equals("0")){
                statusTv.setText("Order is not accepted");
            }
            else if(tempItem.getStatus().equals("1")){
                statusTv.setText("Order is accepted");
            }
            ViewBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(AllOrders.this,ViewAndCancel.class);
                    intent.putExtra("order",tempItem);
                    intent.putExtra("tableNo",tableNo);
                    startActivity(intent);
                }
            });
            return itemView;
        }
    }

}
