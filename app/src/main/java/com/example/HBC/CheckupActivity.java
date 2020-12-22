package com.example.HBC;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class CheckupActivity extends AppCompatActivity {

    //global variables
    String selected_patient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkup);

        Spinner spinnerpatient = findViewById(R.id.spinnerpatient);
        //data source
        String[] patientNames = {"HILLARY","MOSES","EDWARD","KENNETH","TOMMY","JEAN","ALLAN"};

        ArrayAdapter<String> spinneradapters = new ArrayAdapter<String>(CheckupActivity.this,R.layout.spinneritemdesgin,R.id.textViewSpinneritem,patientNames);
        //assign array adapter to a spinner
        spinnerpatient.setAdapter(spinneradapters);

        //spinner item listener
        spinnerpatient.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selected_patient = patientNames[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selected_patient = "Not Specified";
            }
        });


    }
}