package com.smart_rms.group12.smartrms;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class ChangeIpAddress extends AppCompatActivity {
    URL url;
    EditText Ip;
    TextView IpAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_ip_address);

        Ip = (EditText)findViewById(R.id.IpEt);
        IpAd = (TextView)findViewById(R.id.IpAddTv);
        IpAd.setText(url.getIp());
    }

    public void DoneIp(View view) {
        if(!Ip.getText().toString().equals("")){
            url.setIp(Ip.getText().toString());
        }
    }

    public void BackToLogin(View view) {
        Intent intent = new Intent(ChangeIpAddress.this,Login.class);
        startActivity(intent);
    }
}
