package com.example.stelios.ergasia1;

import android.os.Bundle;
        import android.support.v7.app.AppCompatActivity;
        import android.view.View;
        import android.widget.AdapterView;
        import android.widget.ArrayAdapter;
        import android.widget.Spinner;
        import android.widget.TextView;

public class DelaySettings extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    TextView textView1;
    Spinner spinnerA;
    String paths[] = {"Ultra","Fast","Normal","Slow"};
    static int value=3;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delay);

        textView1=(TextView)findViewById(R.id.textView1);
        textView1.setText("Accelerometer delay preferences");

        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(DelaySettings.this, android.R.layout.simple_spinner_item, paths);
        spinnerA = (Spinner)findViewById(R.id.spinnerA);
        spinnerA.setAdapter(adapter1);
        spinnerA.setOnItemSelectedListener(this);
        spinnerA.setSelection(value);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        value = spinnerA.getSelectedItemPosition();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
