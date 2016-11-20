package com.example.stelios.ergasia1;

import android.os.Bundle;
        import android.support.v7.app.AppCompatActivity;
        import android.view.View;
        import android.widget.AdapterView;
        import android.widget.ArrayAdapter;
        import android.widget.Spinner;
        import android.widget.TextView;

public class DelaySettings extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    TextView textView1, textView2, textView3;
    Spinner spinnerA, spinnerP, spinnerL;
    String paths[] = {"No delay","Normal(5 times per second)","Once per second","Once per 5 seconds"}; //populate spinner
    static int value=1, valuep=1, valuel=1; //spinner default values=normal
    static int delayA=200, delayP=200, delayL=200; //default values to 200ms as used in SENSOR_DELAY_NORMAL



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delay);

        textView1=(TextView)findViewById(R.id.textView1);
        textView1.setText("Accelerometer delay preferences");
        textView2=(TextView)findViewById(R.id.textView2);
        textView2.setText("Proximity sensor delay preferences");
        textView3=(TextView)findViewById(R.id.textView3);
        textView3.setText("Lighting sensor delay preferences");

        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(DelaySettings.this, android.R.layout.simple_spinner_item, paths);//spinner array
        spinnerA = (Spinner)findViewById(R.id.spinnerA); //accelerometer spinner
        spinnerA.setAdapter(adapter1);
        spinnerA.setOnItemSelectedListener(this); //enable accelerometer listener
        spinnerA.setSelection(value); //set value

        spinnerP =(Spinner)findViewById(R.id.spinnerP); //proximity spinner
        spinnerP.setAdapter(adapter1);
        spinnerP.setOnItemSelectedListener(this); //enable proximity listener
        spinnerP.setSelection(valuep); //set value

        spinnerL = (Spinner)findViewById(R.id.spinnerL); //light spinner
        spinnerL.setAdapter(adapter1);
        spinnerL.setOnItemSelectedListener(this); //enable light listener
        spinnerL.setSelection(valuel); //set value
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch(parent.getId()) { //get spinner id and match it
            case R.id.spinnerA:
                value = spinnerA.getSelectedItemPosition();  //get position

                /*--------------------------------------*/
                switch (value) { //switch for acceleration delay
                    case 0:
                        delayA=0;
                        break;
                    case 1:
                        delayA=200;
                        break;
                    case 2:
                        delayA=1000;
                        break;
                    case 3:
                        delayA=5000;
                        break;
                }
                /*--------------------------------------*/
                break;

            case R.id.spinnerP:
                valuep = spinnerP.getSelectedItemPosition(); //get position

                /*--------------------------------------*/
                switch (value) { //switch for acceleration delay
                    case 0:
                        delayP=0;
                        break;
                    case 1:
                        delayP=200;
                        break;
                    case 2:
                        delayP=1000;
                        break;
                    case 3:
                        delayP=5000;
                        break;
                }
                /*--------------------------------------*/
                break;
            case R.id.spinnerL:
                valuel = spinnerL.getSelectedItemPosition(); //get position

                /*--------------------------------------*/
                switch (value) { //switch for acceleration delay
                    case 0:
                        delayL=0;
                        break;
                    case 1:
                        delayL=200;
                        break;
                    case 2:
                        delayL=1000;
                        break;
                    case 3:
                        delayL=5000;
                        break;
                }
                /*--------------------------------------*/
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
