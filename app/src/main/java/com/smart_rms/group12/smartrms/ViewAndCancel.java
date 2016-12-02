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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import Beans.MenuItems;
import Beans.Order;
import Beans.OrderItem;
import Beans.User;

public class ViewAndCancel extends AppCompatActivity {

    String tableNo;
    String status;
    String orderId;

    ArrayList<OrderItem> orderItems = new ArrayList<>();

    ArrayAdapter<OrderItem> adapter;
    ListView list;

    URL url = new URL("cancelOrder.php");
    RequestQueue requestQueue;
    StringRequest request;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_and_cancel);

        Button cancelBtn = (Button)findViewById(R.id.CancelOrderBtn);
        requestQueue = Volley.newRequestQueue(this);

        Intent prIntent = getIntent();
        tableNo = prIntent.getStringExtra("tableNo");
        Order order = (Order) prIntent.getSerializableExtra("order");

        status = order.getStatus();
        orderId = order.getOrderId();
        orderItems = order.getOrderItems();

        if(status.equals("1")){
            cancelBtn.setClickable(false);
        }

        list = (ListView)findViewById(R.id.MenuItemListView);
        adapter =new ViewAndCancel.MyListAdapter();
        list.setAdapter(adapter);
    }

    public void CancelOrder(View view) {
        checkOrderStatus();
    }

    public void checkOrderStatus(){
        request = new StringRequest(Request.Method.POST, url.getUrl(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    if(status.equals("success")){

                    }
                    else if(status.equals("not success")){
                        PopUpMsg("not success","order");
                    }
                    else if(status.equals("fail")){
                        PopUpMsg("not success","fail");
                    }
                }
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
                hashMap.put("order_no", orderId);
                return hashMap;
            }
        };
        requestQueue.add(request);
    }

    private class MyListAdapter extends ArrayAdapter<OrderItem> {

        public MyListAdapter() {
            super(ViewAndCancel.this, R.layout.list_view_layout6, orderItems);
        }
        @Override
        public View getView(final int position, View convertView, ViewGroup parent){
            View itemView = convertView;
            if(itemView==null){
                itemView = getLayoutInflater().inflate(R.layout.list_view_layout6,parent,false);
            }
            final OrderItem tempItem = orderItems.get(position);
            TextView itemCode = (TextView)itemView.findViewById(R.id.ItemCodeTv1);
            TextView itemName = (TextView)itemView.findViewById(R.id.ItemNameTv1);
            TextView itemQuantity = (TextView)itemView.findViewById(R.id.ItemQtyTv1);

            itemCode.setText(tempItem.getItemId());
            itemName.setText(tempItem.getItemName());
            itemQuantity.setText(tempItem.getItemQty());
            return itemView;
        }
    }
    public void PopUpMsg(String title,String msg) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ViewAndCancel.this);

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
