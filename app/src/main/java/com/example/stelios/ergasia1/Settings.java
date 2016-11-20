package com.example.stelios.ergasia1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.SeekBar;
import android.widget.TextView;


public class Settings extends AppCompatActivity {  //seekbar settings
    SeekBar mSeekBarL, mSeekBarA; //declare seekbar
    static int light_value=40, acceleration_value=10; //default threshold values
    TextView textL,textA;
  //  Proximity sensor is interrupt-based (not Poll-based), cant be changed

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        textL=(TextView)findViewById(R.id.textView5);
        textA=(TextView)findViewById(R.id.textView6);
        mSeekBarL = (SeekBar)findViewById(R.id.seekBarL);
        mSeekBarA = (SeekBar)findViewById(R.id.seekBarA);
        mSeekBarA.setProgress(acceleration_value); textA.setText("Threshold: " + acceleration_value); //sets progress to default
        mSeekBarL.setProgress(light_value); textL.setText("Threshold: " + light_value); //sets progress to default
        mSeekBarL.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {   //light sensor seekbar
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                light_value=progress; //update progress
                textL.setText("Threshold: " + seekBar.getProgress());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        mSeekBarA.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {  //accelerometer
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                acceleration_value=progress;//update progress
                textA.setText("Threshold: " + seekBar.getProgress());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });


}
}

