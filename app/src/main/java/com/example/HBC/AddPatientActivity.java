package com.example.HBC;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class AddPatientActivity extends AppCompatActivity {

    //global variables
    String selectedlocation, selected_disease;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_patient);

        //new codes
        //referencing the spiner in java
        Spinner spinnerlocation = findViewById(R.id.spinnerpatientlocation);
        Spinner spinnerdisease = findViewById(R.id.spinnerpatientdisease);
        //data source
        String[] patientlocationNames = {"Kampala","Wakiso","Mukono","Jinja","Kiryandongo","Karamonja"};
        //disease
        String[] diseaseNames = {"Covid19","Others"};



        //create array adapter
        ArrayAdapter<String> spinneradapters = new ArrayAdapter<String>(AddPatientActivity.this,R.layout.spinneritemdesgin,R.id.textViewSpinneritem,patientlocationNames);
        //assign array adapter to a spinner
        spinnerlocation.setAdapter(spinneradapters);

        //create array adapter
        ArrayAdapter<String> spinneradapterz = new ArrayAdapter<String>(AddPatientActivity.this,R.layout.spinneritemdesgin,R.id.textViewSpinneritem,diseaseNames);
        //assign array adapter to a spinner
        spinnerdisease.setAdapter(spinneradapterz);



        //spinner item listener
        spinnerlocation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedlocation=patientlocationNames[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedlocation = "Not Specified";
            }
        });


        //spinner item listener for disease
        spinnerdisease.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selected_disease=diseaseNames[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selected_disease = "Not Specified";

            }
        });

    }
}