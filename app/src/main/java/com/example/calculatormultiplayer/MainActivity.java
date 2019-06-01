package com.example.calculatormultiplayer;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.udojava.evalex.Expression;

import org.apache.commons.lang3.StringUtils;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    String text;
    String intentip;
    TextView output;
    Thread myThread;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = getIntent();
        intentip = intent.getStringExtra("ip");
        //toast(intentip);
        myThread = new Thread(new MyServer());
        myThread.start();
        output = findViewById(R.id.textView);
    }


    @Override
    protected void onResume(){
        super.onResume();
        Log.d("xd", "okrutnik no");
        //myThread.start();
    }

    @Override
    protected void onPause(){
        super.onPause();
        Log.d("xd", "closing");
        //myThread.interrupt();
    }
    public void onClick(View v) {
        text = output.getText().toString();
        if(text.isEmpty()){
            text = "";
        }
        commaReformat();
        switch (v.getId()) {
            case  R.id.button1: {
                text=text+"1";
                break;
            }

            case R.id.button2: {
                text=text+"2";
                break;
            }

            case R.id.button3: {
                text=text+"3";
                break;
            }

            case R.id.button4: {
                text=text+"4";
                break;
            }

            case R.id.button5: {
                text=text+"5";
                break;
            }

            case R.id.button6: {
                text=text+"6";
                break;
            }

            case R.id.button7: {
                text=text+"7";
                break;
            }

            case R.id.button8: {
                text=text+"8";
                break;
            }

            case R.id.button9: {
                text=text+"9";
                break;
            }

            case R.id.button0: {
                text=text+"0";
                break;
            }

            case R.id.buttonPlus: {
                if(text.endsWith("+")||text.endsWith("-")||text.endsWith("*")||text.endsWith("/")){
                    text = text.substring(0,text.length()-1)
                            + "+"
                            + text.substring(text.length());
                }
                else
                {
                    text=text+"+";
                }
                break;
            }

            case R.id.buttonMinus: {
                if(text.endsWith("+")||text.endsWith("-")||text.endsWith("*")||text.endsWith("/")){
                    text = text.substring(0,text.length()-1)
                            + "-"
                            + text.substring(text.length());
                }
                else
                {
                    text=text+"-";
                }
                break;
            }

            case R.id.buttonDiv: {
                if(text.endsWith("+")||text.endsWith("-")||text.endsWith("*")||text.endsWith("/")){
                    text = text.substring(0,text.length()-1)
                            + "/"
                            + text.substring(text.length());
                }
                else
                {
                    text=text+"/";
                }
                break;
            }

            case R.id.buttonMult: {
                if(text.endsWith("+")||text.endsWith("-")||text.endsWith("*")||text.endsWith("/")){
                    text = text.substring(0,text.length()-1)
                            + "*"
                            + text.substring(text.length());
                }
                else
                {
                    text=text+"*";
                }
                break;
            }

            case R.id.buttonClear: {
                text = "";
                output.setText(text);
                break;
            }

            case R.id.buttonComma: {
                if(!text.endsWith(".")){
                    text=text+".";
                }
                commaReformat();
                break;
            }

            case R.id.buttonPapa: {
                text=text+"\uD83D\uDE02";
                break;
            }

            case R.id.buttonDel: {
                if(text.length()>0) {
                    int i = text.length()-1;
                    if(text.endsWith("\uD83D\uDE02")){
                        text = text.replace(text.substring(text.length()-1), "");
                    }
                    text = text.replace(text.substring(text.length()-1), "");
                }
                break;
            }

            case R.id.buttonEquals: {
                if(text.contains("\uD83D\uDE02")){
                    text = text.replaceAll("\uD83D\uDE02", "");
                    BackgroundTask b = new BackgroundTask();
                    b.execute(intentip, "play");
                    nwm();
                    break;
                }
                if(text.endsWith("+")||text.endsWith("-")){
                    text = text + "0";
                }
                if(text.endsWith("*")||text.endsWith("/")){
                    text = text + "1";
                }
                commaReformat();
                if(!text.equals("")) {
                    calculate();
                }
                break;
            }
        }
        //SKALOWANIE TEKSTU
        if(text.length()>10){
            double d = 500/text.length();
            output.setTextSize((float) d);
        }
        else {
            output.setTextSize(50);
        }
        output.setText(text);
        BackgroundTask b = new BackgroundTask();
        b.execute(intentip, text);
        Log.d("xd", output.getText().toString());
    }

    static boolean isInteger(BigDecimal bigDecimal) {
        return bigDecimal.setScale(0, RoundingMode.HALF_UP).compareTo(bigDecimal) == 0;
    }

    void toast(String message) {
        Toast.makeText(getApplication(), message, Toast.LENGTH_LONG).show();
    }

    void calculate(){
        try {
            BigDecimal result = new Expression(text).eval();
            if (text.length() < 7) {
                if (isInteger(result)) {
                    long resultI = result.intValue();
                    text = String.valueOf(resultI);
                } else {
                    double resultD = result.doubleValue();
                    text = String.valueOf(resultD);
                }

            } else {
                text = String.valueOf(result);
            }
        }
        catch (NumberFormatException nE){
            toast(nE.toString());
        }
    }

    void commaReformat(){
        boolean wasThereComma = false;
        //idzie przez kazdy znak w stringu
        for(int i=0; i<text.length(); i++){
            //jesli widzi kropke pierwszy raz tylko zmienia zmienna na true
            //
            //jesli widzi kropke a zmienna juz jest na true to znaczy ze
            //kropka juz byla wiec obecna usuwa
            if(text.charAt(i)=='.'){
                if(wasThereComma){
                    //zamiania na fire emoji. musi byc jakis charakterystyczny znak
                    //ktory nie zmieni dlugosci zmiennej ale pozwoli na latwe usuniecie pozniej
                    //to jest jedyne polecenie ktore skopiowalem z neta
                    text = text.substring(0,i)
                                   + "\uD83D\uDD25"
                                   + text.substring(i+1);
                }
                wasThereComma = true;
            }
            //jesli napotka +-*/ to znaczy ze liczba sie skonczyla
            if(text.charAt(i)=='+'||text.charAt(i)=='-'||text.charAt(i)=='*'||text.charAt(i)=='/'){
                wasThereComma = false;
            }
        }
        //usuwa wszystkie fire emoji ktore byly wczesniej niepotrzebnymi kropkami
        text = text.replaceAll("\uD83D\uDD25", "");
    }

    void nwm(){
        final MediaPlayer nwmPlayer = MediaPlayer.create(this, R.raw.nwm);
        nwmPlayer.start();
    }

    class BackgroundTask extends AsyncTask<String,Void,String> {
        Socket s;
        DataOutputStream dos;
        String ip, message;
        @Override
        protected String doInBackground(String... params){
            ip=params[0];
            message = params [1];
            try{
                s = new Socket(ip,2137);
                dos = new DataOutputStream(s.getOutputStream());
                dos.writeUTF(message);
                dos.close();
                s.close();
            }
            catch (IOException e){
                e.printStackTrace();
            }
            return null;
        }
    }

    class MyServer implements Runnable{
        ServerSocket ss;
        Socket mysocket;
        DataInputStream dis;
        String message;
        Handler handler = new Handler();
        @Override
        public void run(){
            Log.d("xd", "setup started");
            try{
                ss= new ServerSocket();
                ss.setReuseAddress(true);
                ss.bind(new InetSocketAddress(2137));
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplication(), "Łączenie", Toast.LENGTH_LONG).show();
                    }
                });
                Log.d("xd", "setup done");
                while(true){
                    Log.d("xd", "loop");
                    mysocket =ss.accept();
                    dis = new DataInputStream(mysocket.getInputStream());
                    message= dis.readUTF();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            if(message.equals("play")){
                                nwm();
                            }
                            else {
                                output.setText(message);
                            }
                        }
                    });
                }
            }
            catch(IOException e){
                e.printStackTrace();
                Log.d("xd", e.toString());
            }
        }
    }

}
