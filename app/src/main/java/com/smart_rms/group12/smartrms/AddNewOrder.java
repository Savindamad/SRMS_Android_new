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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import Beans.MenuItems;
import Beans.Order;
import Beans.User;

public class AddNewOrder extends AppCompatActivity {

    User user;
    String tableNo;

    private ArrayList<MenuItems> menu = new ArrayList<MenuItems>();
    private List<MenuItems> menuTemp = new ArrayList<MenuItems>();
    private ArrayList<MenuItems> order =new ArrayList<MenuItems>();
    private ArrayList<Order> AllOders = new ArrayList<Order>();

    ArrayAdapter<MenuItems> adapter;
    ArrayAdapter<MenuItems> adapter1;

    StringRequest request;

    private RequestQueue requestQueue;
    private RequestQueue requestQueue1;

    URL url = new URL("addNewOrder.php");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_order);

        requestQueue = Volley.newRequestQueue(this);
        requestQueue1 = Volley.newRequestQueue(this);

        TabHost tab = (TabHost)findViewById(R.id.tabHost);
        tab.setup();

        TabHost.TabSpec tab1 = tab.newTabSpec("TAB 1");
        tab1.setIndicator("Add Items");
        tab1.setContent(R.id.add);
        tab.addTab(tab1);

        TabHost.TabSpec tab2 = tab.newTabSpec("TAB 2");
        tab2.setIndicator("Accept Order");
        tab2.setContent(R.id.accept);
        tab.addTab(tab2);

        for(int i=0;i<tab.getTabWidget().getChildCount();i++)
        {
            TextView tv = (TextView) tab.getTabWidget().getChildAt(i).findViewById(android.R.id.title); //Unselected Tabs
            tv.setTextColor(Color.parseColor("#ffffff"));
        }

        //Get data from previous intent
        Intent prIntent = getIntent();
        user =(User)prIntent.getSerializableExtra("user");
        tableNo = prIntent.getStringExtra("tableNo");
        menu = (ArrayList<MenuItems>)prIntent.getSerializableExtra("menu");
        menuTemp.addAll(menu);


        final ListView list = (ListView)findViewById(R.id.MenuList);
        adapter = new AddNewOrder.MyListAdapter();
        list.setAdapter(adapter);

        final ListView list1 = (ListView)findViewById(R.id.OrderList);
        adapter1 = new AddNewOrder.MyListAdapter1();
        list1.setAdapter(adapter1);

        final EditText search = (EditText)findViewById(R.id.etSearch1);
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                String text = search.getText().toString().toLowerCase(Locale.getDefault());

                menuTemp.clear();
                if (text.length() == 0) {
                    menuTemp.addAll(menu);
                } else {
                    for (int i = 0; i < menu.size(); i++) {
                        if (menu.get(i).getItemName().toLowerCase(Locale.getDefault()).contains(text)) {
                            menuTemp.add(menu.get(i));
                        }
                    }
                }
                adapter.notifyDataSetChanged();
                list.setAdapter(adapter);
            }
        });

    }

    public void AcceptOrder(View view) {
        if(!(order.isEmpty())){
            PlaceAnOrder();
        }
    }

    private class MyListAdapter extends ArrayAdapter<MenuItems>{

        public MyListAdapter() {
            super(AddNewOrder.this, R.layout.list_view_layout, menuTemp);
        }
        @Override
        public View getView(final int position, View convertView,ViewGroup parent){
            View itemView = convertView;
            if(itemView==null){
                itemView = getLayoutInflater().inflate(R.layout.list_view_layout,parent,false);
            }
            MenuItems tempItem = menuTemp.get(position);

            TextView code = (TextView)itemView.findViewById(R.id.tvMealCode);
            TextView name = (TextView)itemView.findViewById(R.id.tvMealName1);
            TextView type = (TextView)itemView.findViewById(R.id.tvMealDes);
            Button Add = (Button)itemView.findViewById(R.id.bAdd);

            code.setText(""+(position+1));
            name.setText(tempItem.getItemName());
            type.setText(tempItem.getItemType());
            Add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    order.add(menuTemp.get(position));
                    for(int i = 0; i<menu.size(); i++){
                        if(menuTemp.get(position).getItemCode().equals(menu.get(i).getItemCode())){
                            menu.remove(i);
                            menuTemp.remove(position);
                            adapter.notifyDataSetChanged();
                            adapter1.notifyDataSetChanged();
                            break;
                        }
                    }
                }
            });
            return itemView;
        }
    }
    private class MyListAdapter1 extends ArrayAdapter<MenuItems>{

        public MyListAdapter1() {
            super(AddNewOrder.this, R.layout.list_view_layout1, order);
        }
        @Override
        public View getView(final int position, View convertView,ViewGroup parent){
            View itemView = convertView;
            if(itemView==null){
                itemView = getLayoutInflater().inflate(R.layout.list_view_layout1,parent,false);
            }
            MenuItems tempItem = order.get(position);
            TextView code = (TextView)itemView.findViewById(R.id.tvMealCode1);
            TextView name = (TextView)itemView.findViewById(R.id.tvMealName1);
            TextView type = (TextView)itemView.findViewById(R.id.tvMealDes1);

            code.setText("0"+(position+1));
            name.setText(tempItem.getItemName());
            type.setText(tempItem.getItemType());

            Button remove = (Button)itemView.findViewById(R.id.bRemove);
            remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    menu.add(order.get(position));
                    menuTemp.add(order.get(position));
                    order.remove(position);
                    adapter.notifyDataSetChanged();
                    adapter1.notifyDataSetChanged();
                }
            });

            final EditText itemQty = (EditText)itemView.findViewById(R.id.etItemQty);

            Button bPlus =(Button)itemView.findViewById(R.id.bPlus);
            Button bMinus = (Button)itemView.findViewById(R.id.bMinus);

            bPlus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(Integer.parseInt(itemQty.getText().toString())<10){
                        itemQty.setText("" + (Integer.parseInt(itemQty.getText().toString()) + 1));
                        int tempInt = order.get(position).getItemQty();
                        order.get(position).setItemQty(tempInt + 1);
                    }
                }
            });

            bMinus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(Integer.parseInt(itemQty.getText().toString())>1){
                        itemQty.setText(""+(Integer.parseInt(itemQty.getText().toString())-1));
                        int tempInt = order.get(position).getItemQty();
                        order.get(position).setItemQty(tempInt-1);
                    }
                }
            });

            return itemView;

        }
    }

    public void PlaceAnOrder(){

        JSONArray orderArray = new JSONArray();
        for(int i = 0; i<order.size(); i++){
            MenuItems item = order.get(i);
            JSONObject orderItem =new JSONObject();
            try {
                orderItem.put("itemId",item.getItemCode());
                orderItem.put("itemQty",item.getItemQty());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            orderArray.put(orderItem);
        }
        final String orderStringArray = orderArray.toString();


        request = new StringRequest(Request.Method.POST, url.getUrl(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
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
                hashMap.put("userId", user.getUserID());
                hashMap.put("tableNum", tableNo);
                hashMap.put("order", orderStringArray);
                return hashMap;
            }
        };
        requestQueue.add(request);

        Intent intent = new Intent(AddNewOrder.this,Table.class);
        intent.putExtra("user",user);
        intent.putExtra("menu",menu);
        intent.putExtra("tableNo",tableNo);
        startActivity(intent);
    }

    public void PopUpMsg(String title,String msg) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AddNewOrder.this);

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
