package com.example.stelios.ergasia1;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.CompoundButton;
import android.widget.ToggleButton;


public class OnlineMode extends AppCompatActivity{
    ToggleButton toggle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.online_mode);

        toggle = (ToggleButton)findViewById(R.id.toggleButton);

        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                    //start service?
                    //online
                } else {
                    // The toggle is disabled
                }
            }
        });
    }
}
