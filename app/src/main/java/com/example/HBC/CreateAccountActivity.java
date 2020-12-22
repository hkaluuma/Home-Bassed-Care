package com.example.HBC;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

public class CreateAccountActivity extends AppCompatActivity {

    //global variables
    String selectedlocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

       //referencing the spiner in java
        Spinner spinnerlocation = findViewById(R.id.spinnerlocation);
        //data source
        String[] locationNames = {"Kampala","Wakiso","Mukono","Jinja","Kiryandongo","Karamonja"};
        //create array adapter
        ArrayAdapter<String> spinneradapter = new ArrayAdapter<String>(CreateAccountActivity.this,R.layout.spinneritemdesgin,R.id.textViewSpinneritem,locationNames);
        //assign array adapter to a spinner
        spinnerlocation.setAdapter(spinneradapter);
        //spinner item listener
        spinnerlocation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedlocation=locationNames[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedlocation = "Not Specified";
            }
        });

        Button buttonccregister = findViewById(R.id.buttonccregister);
        buttonccregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainactivityintent = new Intent(CreateAccountActivity.this, LoginActivity.class);
                startActivity(mainactivityintent);
            }
        });
    }

}