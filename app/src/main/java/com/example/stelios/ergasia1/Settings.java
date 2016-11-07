package com.example.stelios.ergasia1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.SeekBar;
import android.widget.TextView;


public class Settings extends AppCompatActivity {
    SeekBar mSeekBarL, mSeekBarA, mSeekBarP;
    static int light_value=40, acceleration_value=10, proximity_value;
    TextView textL,textA,textP;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        textL=(TextView)findViewById(R.id.textView5);
        textA=(TextView)findViewById(R.id.textView6);
        textP=(TextView)findViewById(R.id.textView7);
        mSeekBarL = (SeekBar)findViewById(R.id.seekBarL);
        mSeekBarA = (SeekBar)findViewById(R.id.seekBarA);
        mSeekBarP = (SeekBar)findViewById(R.id.seekBarP);
        mSeekBarL.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                light_value=progress;
                textL.setText("Threshold: " + (seekBar.getProgress()+2));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        mSeekBarA.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                acceleration_value=progress;
                textA.setText("Threshold: " + (seekBar.getProgress()+2));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        mSeekBarP.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                proximity_value=progress;
                textP.setText("Threshold: " + seekBar.getProgress());
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

