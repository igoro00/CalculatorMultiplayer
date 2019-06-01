package com.example.calculatormultiplayer;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class chooseSide extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_side);

    }

    public void toSingle(View v){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void toGuest(View v){
        Intent intent = new Intent(this, InitGuestActivity.class);
        startActivity(intent);
    }

    public void toHost(View v){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
