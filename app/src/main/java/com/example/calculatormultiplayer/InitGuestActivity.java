package com.example.calculatormultiplayer;

import android.content.Intent;
import android.graphics.Rect;
import android.net.wifi.WifiManager;
import android.support.constraint.ConstraintHelper;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.Formatter;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.TextView;

import android.widget.Toast;
import org.w3c.dom.Text;

public class InitGuestActivity extends AppCompatActivity {
    private ConstraintLayout layout;
    private ConstraintSet constraintSetOld = new ConstraintSet();
    private ConstraintSet constraintSetNew = new ConstraintSet();
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

        layout = findViewById(R.id.init_Long);
        constraintSetOld.clone(layout);
        constraintSetNew.clone(this, R.layout.activity_init_guest_short);

        KeyboardUtils.addKeyboardToggleListener(this, new KeyboardUtils.SoftKeyboardToggleListener()
        {
            @Override
            public void onToggleSoftKeyboard(boolean isVisible)
            {
                TransitionManager.beginDelayedTransition(layout);
                if(isVisible){
                    Log.d("xd", "short");
                    constraintSetNew.applyTo(layout);

                }
                else{
                    Log.d("xd", "long");
                    constraintSetOld.applyTo(layout);
                }

            }
        });
    }

    public void toGameGuest(View v){
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("ip", ipInput.getText().toString());
        startActivity(intent);
    }

    public void fill(View v){
        IpToFillFormat ipFormatter = new IpToFillFormat();
        ipInput.setText(ipFormatter.format(myIP));
    }
}
