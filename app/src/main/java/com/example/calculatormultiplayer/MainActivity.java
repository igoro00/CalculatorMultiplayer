package com.example.calculatormultiplayer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;

import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.udojava.evalex.Expression;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URISyntaxException;
import java.util.UUID;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private String text;
    private TextView output;
    private Utils formatters = new Utils();
    private Socket socket;
    private static String uniqueID = null;
    private static final String PREF_UNIQUE_ID = "PREF_UNIQUE_ID";
    private boolean isMP;
    private ImageButton buttonDel;
    private Dialog sendTxtDialog;

    public synchronized static String id(Context context) {
        if (uniqueID == null) {
            SharedPreferences sharedPrefs = context.getSharedPreferences(
                    PREF_UNIQUE_ID, Context.MODE_PRIVATE);
            uniqueID = sharedPrefs.getString(PREF_UNIQUE_ID, null);
            if (uniqueID == null) {
                uniqueID = UUID.randomUUID().toString();
                SharedPreferences.Editor editor = sharedPrefs.edit();
                editor.putString(PREF_UNIQUE_ID, uniqueID);
                editor.commit();
            }
        }
        return uniqueID;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        output = findViewById(R.id.textView);
        isMP = getIntent().getBooleanExtra("isMP", false);
        if(isMP) {
            SharedPreferences sharedPreferences =
                    PreferenceManager.getDefaultSharedPreferences(this);
            String serverURL = sharedPreferences.getString("serverURL", "example.com");



            try {
                socket = IO.socket(formatURL(serverURL));
                Log.d("xd", "connect");
                socket.connect();
                socket.emit("join", id(this));
            } catch (URISyntaxException e) {
                e.printStackTrace();
                toast("NIE UDAŁO SIĘ NAWIĄZAĆ POŁĄCZENIA!");
            }
            socket.on("userjoinedthechat", new Emitter.Listener() {
                @Override
                public void call(final Object... args) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            toast("Ktoś dołączył!");
                        }
                    });
                }
            });
            socket.on("message", new Emitter.Listener() {
                @Override
                public void call(final Object... args) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            JSONObject data = (JSONObject) args[0];
                            try {
                                String message = data.getString("message");
                                 if (!message.equals("play") && !message.startsWith("txt: ")) {
                                     output.setText(message);
                                     if (message.length() > 10) {
                                         double d = 500 / message.length();
                                         output.setTextSize((float) d);
                                     }
                                     else {
                                         output.setTextSize(50);
                                     }
                                 }
                                 else if(message.equals("play")){
                                     nwm();
                                 }
                                 else{
                                     message = message.substring(5);
                                     toast(message);
                                 }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            });
            Button buttonPapa =(Button)findViewById(R.id.buttonPapa);
            buttonPapa.setOnLongClickListener(new View.OnLongClickListener() {
                public boolean onLongClick(View v) {
                    sendTxtDialog = new Dialog(MainActivity.this);
                    sendTxtDialog.setContentView(R.layout.dialog_template);
                    final EditText Write = (EditText)sendTxtDialog.findViewById(R.id.write);
                    Button SendTxt = (Button)sendTxtDialog.findViewById(R.id.sendButton);
                    Write.setEnabled(true);
                    SendTxt.setEnabled(true);
                    SendTxt.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            send("txt: " + Write.getText().toString());
                            sendTxtDialog.cancel();
                            send(output.getText().toString());
                        }
                    });
                    sendTxtDialog.show();
                    return true;
                }
            });
        }
        buttonDel = findViewById(R.id.buttonDel);
        buttonDel.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                text = "";
                output.setText(text);
                return true;
            }
        });
    }

    public void onClick(View v) {
        text = output.getText().toString();
        if (text.isEmpty()) {
            text = "";
        }
        text = formatters.commaReformat(text);
        switch (v.getId()) {
            case R.id.button1: {
                text = text + "1";
                break;
            }

            case R.id.button2: {
                text = text + "2";
                break;
            }

            case R.id.button3: {
                text = text + "3";
                break;
            }

            case R.id.button4: {
                text = text + "4";
                break;
            }

            case R.id.button5: {
                text = text + "5";
                break;
            }

            case R.id.button6: {
                text = text + "6";
                break;
            }

            case R.id.button7: {
                text = text + "7";
                break;
            }

            case R.id.button8: {
                text = text + "8";
                break;
            }

            case R.id.button9: {
                text = text + "9";
                break;
            }

            case R.id.button0: {
                text = text + "0";
                break;
            }

            case R.id.buttonPlus: {
                if (text.endsWith("+") || text.endsWith("-") || text.endsWith("*") || text.endsWith("/")) {
                    text = text.substring(0, text.length() - 1)
                            + "+"
                            + text.substring(text.length());
                } else {
                    text = text + "+";
                }
                break;
            }

            case R.id.buttonMinus: {
                if (text.endsWith("+") || text.endsWith("-")) {
                    text = text.substring(0, text.length() - 1)
                            + "-"
                            + text.substring(text.length());
                } else {
                    text = text + "-";
                }
                break;
            }

            case R.id.buttonDiv: {
                if (text.endsWith("+") || text.endsWith("-") || text.endsWith("*") || text.endsWith("/")) {
                    text = text.substring(0, text.length() - 1)
                            + "/"
                            + text.substring(text.length());
                } else {
                    text = text + "/";
                }
                break;
            }

            case R.id.buttonMult: {
                if (text.endsWith("+") || text.endsWith("-") || text.endsWith("*") || text.endsWith("/")) {
                    text = text.substring(0, text.length() - 1)
                            + "*"
                            + text.substring(text.length());
                } else {
                    text = text + "*";
                }
                break;
            }

            case R.id.buttonSqrt: {
                //text = text + "√";
                //text = text + "sqrt(9)";
                //output.setText(text);
                break;
            }

            case R.id.buttonComma: {
                if (!text.endsWith(".")) {
                    text = text + ".";
                }
                text = formatters.commaReformat(text);
                break;
            }

            case R.id.buttonPapa: {
                text += "\uD83D\uDE02";
                break;
            }

            case R.id.buttonDel: {
                text = formatters.formatBackspace(text);
                break;
            }

            case R.id.buttonEquals: {
                if (text.contains("\uD83D\uDE02")) {
                    text = text.replaceAll("\uD83D\uDE02", "");
                    send("play");
                    nwm();
                    break;
                }
                if (text.endsWith("+") || text.endsWith("-")) {
                    text = text + "0";
                }
                if (text.endsWith("*") || text.endsWith("/")) {
                    text = text + "1";
                }
                text = formatters.commaReformat(text);
                if (!text.equals("")) {
                    calculate();
                }
                break;
            }
        }
        if (text.length() > 10) {
            double d = 500 / text.length();
            output.setTextSize((float) d);
        } else {
            output.setTextSize(50);
        }
        output.setText(text);
        send(text);
        Log.d("xd", output.getText().toString());
    }

    static boolean isInteger(BigDecimal bigDecimal) {
        return bigDecimal.setScale(0, RoundingMode.HALF_UP).compareTo(bigDecimal) == 0;
    }

    void toast(String message) {
        Toast.makeText(getApplication(), message, Toast.LENGTH_LONG).show();
    }

    void send(String message) {
        if(isMP) {
            socket.emit("messagedetection", id(this), message);
            Log.d("xd", "send");
        }
    }

    void calculate() {
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
        } catch (NumberFormatException nE) {
            toast(nE.toString());
        }
    }

    void nwm() {
        final MediaPlayer nwmPlayer = MediaPlayer.create(this, R.raw.nwm);
        nwmPlayer.start();
    }

    String formatURL(String input){
        String portchck = input;
        //usun wszystkie cyfry na koncu i sprawdz czy jest : przed nimi
        //jeśli nie ma to znaczy ze nie ma portu i trzeba dodać domyslny
        while(Character.isDigit(portchck.charAt(portchck.length()-1))){
            portchck = portchck.substring(0, portchck.length() - 1);
        }
        if(portchck.charAt(portchck.length()-1)!=':'){
            input = input + ":2137";
        }


        if(input.startsWith("https://")){
            input=input.substring(0,5)+input.substring(5+1);
        }
        if(!input.startsWith("http://")){
            input = "http://" + input;
        }


        return input;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(isMP) {
            socket.disconnect();
        }
    }
}