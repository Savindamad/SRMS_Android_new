package com.smart_rms.group12.smartrms;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;

import Beans.MenuItems;
import Beans.User;

public class TableTypes extends AppCompatActivity {

    ArrayList<MenuItems> menu = new ArrayList<>();
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table_types);

        Intent prIntent = getIntent();
        menu = (ArrayList<MenuItems>)prIntent.getSerializableExtra("menu");
        user = (User) prIntent.getSerializableExtra("user");
    }
    public void TableType1(View view) {
        Intent intent = new Intent(TableTypes.this, TableType1.class);
        intent.putExtra("user",user);
        intent.putExtra("menu",menu);
        startActivity(intent);
    }

    public void TableType2(View view) {
        Intent intent = new Intent(TableTypes.this, TableType2.class);
        intent.putExtra("user",user);
        intent.putExtra("menu",menu);
        startActivity(intent);
    }

    public void TableType3(View view) {
        Intent intent = new Intent(TableTypes.this, TableType3.class);
        intent.putExtra("user",user);
        intent.putExtra("menu",menu);
        startActivity(intent);
    }

}
