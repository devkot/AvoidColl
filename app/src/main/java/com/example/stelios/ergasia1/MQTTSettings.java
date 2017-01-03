package com.example.stelios.ergasia1;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;



public class MQTTSettings extends AppCompatActivity{
    TextView textViewmqtt;
    EditText edittext;
    Button btn;
    static String str="tcp://192.168.1.9:1883";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mqtt_settings);

        textViewmqtt=(TextView)findViewById(R.id.textView4);
        textViewmqtt.setText("MQTT Broker IP");
        edittext=(EditText)findViewById(R.id.editText);
        btn=(Button)findViewById(R.id.button2);


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                str = edittext.getText().toString();
                edittext.setText(str);
            }
        });
    }





    @Override
    protected void onResume() {
        super.onResume();
        edittext.setText(str);
    }
}
