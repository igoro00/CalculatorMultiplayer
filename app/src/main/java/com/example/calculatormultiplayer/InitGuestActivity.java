package com.example.calculatormultiplayer;

import android.content.Intent;
import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.Formatter;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

public class InitGuestActivity extends AppCompatActivity {
    EditText ipInput;
    String myIP;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_init_guest);
        ipInput = (EditText)findViewById(R.id.editText);
        TextView myipdisplay = findViewById(R.id.textViewMyIp);
        WifiManager wm = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        myIP = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());
        myipdisplay.setText("Twój adres IP to:\n" + myIP);

    }

    public void toGameGuest(View v){
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("ip", ipInput.getText().toString());
        startActivity(intent);
    }

    public void fill(View v){
        ipInput.setText("192.168.");
    }
}
