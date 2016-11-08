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
    String paths[] = {"No delay","Normal","Once per second","Once per 5 seconds"}; //populate spinner
    static int value=1, valuep=1, valuel=1; //spinner default values=normal



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
                break;
            case R.id.spinnerP:
                valuep = spinnerP.getSelectedItemPosition(); //get position
                break;
            case R.id.spinnerL:
                valuel = spinnerL.getSelectedItemPosition(); //get position
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
