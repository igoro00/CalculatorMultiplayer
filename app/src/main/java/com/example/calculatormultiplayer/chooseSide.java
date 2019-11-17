package com.example.calculatormultiplayer;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;

public class chooseSide extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_side);

    }

    public void toSP(View v){
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("isMP", false);
        startActivity(intent);
    }

    public void toMP(View v){
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("isMP", true);
        startActivity(intent);
    }

}
