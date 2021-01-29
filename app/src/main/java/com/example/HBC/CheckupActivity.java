package com.example.HBC;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class CheckupActivity extends AppCompatActivity {

    //global variables
    String selected_patient, selected_chills, patienttemperature, selected_chestpain, selected_headache, selected_cough, selected_difficultbreathing,
            selected_fatigue, selected_runnynose, selected_diarrhea;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkup);

        //referencing the variables in xml
        Button buttonsubmitcheckup = findViewById(R.id.buttonsubmitcheckup);
        EditText editTexttemperature = findViewById(R.id.editTexttemperature);

        //calling method to display spinner
        displaySpinnerPatient();
        displaySpinnerChills();
        displaySpinnerChestpain();
        displaySpinnerHeadache();
        displaySpinnerCough();
        spinnerdifficultybreathing();
        displaySpinnerfatigue();
        displaySpinnerrunnynose();
        displaySpinnerdiarrhea();


        buttonsubmitcheckup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                patienttemperature = editTexttemperature.getText().toString();


                if (patienttemperature.isEmpty()) {
                    editTexttemperature.setError("Patient Full names are required");
                } else if (selected_chestpain.equals("Not Specified")) {
                    Toast.makeText(CheckupActivity.this, "Chest Pain not specified", Toast.LENGTH_SHORT).show();
                } else if (selected_chills.equals("Not Specified")){
                    Toast.makeText(CheckupActivity.this, "Chills not specified", Toast.LENGTH_SHORT).show();
                } else if (selected_cough.equals("Not Specified")) {
                    Toast.makeText(CheckupActivity.this, "Cough not specified", Toast.LENGTH_SHORT).show();
                } else if (selected_diarrhea.equals("Not Specified")) {
                    Toast.makeText(CheckupActivity.this, "Diarrhea not specified", Toast.LENGTH_SHORT).show();
                } else if (selected_difficultbreathing.equals("Not Specified")) {
                    Toast.makeText(CheckupActivity.this, "Breathing difficulties not specified", Toast.LENGTH_SHORT).show();
                } else if (selected_fatigue.equals("Not Specified")) {
                    Toast.makeText(CheckupActivity.this, "Fatigue status not specified", Toast.LENGTH_SHORT).show();
                } else if (selected_headache.equals("Not Specified")){
                    Toast.makeText(CheckupActivity.this, "Headache status not specified", Toast.LENGTH_SHORT).show();
                } else if (selected_runnynose.equals("Not Specified")) {
                    Toast.makeText(CheckupActivity.this, "Running nose status not specified", Toast.LENGTH_SHORT).show();
                } else {

                    if (haveNetworkConnection()) {
                        // connected
                       // new CheckupActivity.CheckupClass().execute();
                    } else {
                        // not connected
                        Toast.makeText(CheckupActivity.this, "No internet Connection", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });

    }



        public void displaySpinnerPatient(){
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



        public void displaySpinnerChills() {
            //referencing the spiner in java
            Spinner spinnerchills = findViewById(R.id.spinnerchills);
            //disease
            String[] chillsStatus = {"Yes","No"};
            //create array adapter
            ArrayAdapter<String> spinneradaptera = new ArrayAdapter<String>(CheckupActivity.this,R.layout.spinneritemdesign2,R.id.textViewSpinneritem2,chillsStatus);
            //assign array adapter to a spinner
            spinnerchills.setAdapter(spinneradaptera);
            //spinner item listener for disease
            spinnerchills.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    selected_chills=chillsStatus[position];
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    selected_chills = "Not Specified";

                }
            });

        }

    public void displaySpinnerChestpain() {
        //referencing the spiner in java
        Spinner spinnerchestpain = findViewById(R.id.spinner_new_chest_pain);
        //disease
        String[] chestpainStatus = {"Yes","No"};
        //create array adapter
        ArrayAdapter<String> spinneradaptera = new ArrayAdapter<String>(CheckupActivity.this,R.layout.spinneritemdesign2,R.id.textViewSpinneritem2,chestpainStatus);
        //assign array adapter to a spinner
        spinnerchestpain.setAdapter(spinneradaptera);
        //spinner item listener for disease
        spinnerchestpain.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selected_chestpain=chestpainStatus[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selected_chestpain = "Not Specified";

            }
        });

    }

    public void displaySpinnerHeadache() {
        //referencing the spiner in java
        Spinner spinnerheadache = findViewById(R.id.spinnerHeadache);
        //disease
        String[] headacheStatus = {"Yes","No"};
        //create array adapter
        ArrayAdapter<String> spinneradaptera = new ArrayAdapter<String>(CheckupActivity.this,R.layout.spinneritemdesign2,R.id.textViewSpinneritem2,headacheStatus);
        //assign array adapter to a spinner
        spinnerheadache.setAdapter(spinneradaptera);
        //spinner item listener for disease
        spinnerheadache.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selected_headache=headacheStatus[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selected_headache = "Not Specified";

            }
        });

    }


    public void displaySpinnerCough() {
        //referencing the spiner in java
        Spinner spinnercough = findViewById(R.id.spinnerCough);
        //disease
        String[] CoughStatus = {"Yes","No"};
        //create array adapter
        ArrayAdapter<String> spinneradaptera = new ArrayAdapter<String>(CheckupActivity.this,R.layout.spinneritemdesign2,R.id.textViewSpinneritem2,CoughStatus);
        //assign array adapter to a spinner
        spinnercough.setAdapter(spinneradaptera);
        //spinner item listener for disease
        spinnercough.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selected_cough=CoughStatus[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selected_cough = "Not Specified";

            }
        });

    }

    public void spinnerdifficultybreathing() {
        //referencing the spiner in java
        Spinner spinnerbreathing = findViewById(R.id.spinnerdifficulty_breathing);
        //disease
        String[] breathingStatus = {"Yes","No"};
        //create array adapter
        ArrayAdapter<String> spinneradaptera = new ArrayAdapter<String>(CheckupActivity.this,R.layout.spinneritemdesign2,R.id.textViewSpinneritem2,breathingStatus);
        //assign array adapter to a spinner
        spinnerbreathing.setAdapter(spinneradaptera);
        //spinner item listener for disease
        spinnerbreathing.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selected_difficultbreathing=breathingStatus[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selected_difficultbreathing = "Not Specified";

            }
        });
    }


    public void displaySpinnerfatigue() {
        //referencing the spiner in java
        Spinner spinnerfatigue = findViewById(R.id.spinnerFatigue);
        //disease
        String[] fatigueStatus = {"Yes", "No"};
        //create array adapter
        ArrayAdapter<String> spinneradaptera = new ArrayAdapter<String>(CheckupActivity.this, R.layout.spinneritemdesign2, R.id.textViewSpinneritem2, fatigueStatus);
        //assign array adapter to a spinner
        spinnerfatigue.setAdapter(spinneradaptera);
        //spinner item listener for disease
        spinnerfatigue.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selected_fatigue = fatigueStatus[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selected_fatigue = "Not Specified";

            }
        });
    }

    public void displaySpinnerrunnynose() {
        //referencing the spiner in java
        Spinner spinnernose = findViewById(R.id.spinnerRunny_nose);
        //disease
        String[] noseStatus = {"Yes", "No"};
        //create array adapter
        ArrayAdapter<String> spinneradaptera = new ArrayAdapter<String>(CheckupActivity.this, R.layout.spinneritemdesign2, R.id.textViewSpinneritem2, noseStatus);
        //assign array adapter to a spinner
        spinnernose.setAdapter(spinneradaptera);
        //spinner item listener for disease
        spinnernose.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selected_runnynose = noseStatus[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selected_runnynose = "Not Specified";

            }
        });
    }

    public void displaySpinnerdiarrhea() {
        //referencing the spiner in java
        Spinner spinnerdiarrhea = findViewById(R.id.spinnerdiarrhea);
        //disease
        String[] diarrheaStatus = {"Yes", "No"};
        //create array adapter
        ArrayAdapter<String> spinneradaptera = new ArrayAdapter<String>(CheckupActivity.this, R.layout.spinneritemdesign2, R.id.textViewSpinneritem2, diarrheaStatus);
        //assign array adapter to a spinner
        spinnerdiarrhea.setAdapter(spinneradaptera);
        //spinner item listener for disease
        spinnerdiarrhea.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selected_diarrhea = diarrheaStatus[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selected_diarrhea = "Not Specified";

            }
        });
    }


    //creating the asck class and details
        //creating async inner class
        class CheckupClass extends AsyncTask<String, String, String> {


            String responcefromphp;
           ///create progress dialog class
            ProgressDialog pdialog;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                //show dialog while registering Business
                pdialog = new ProgressDialog(CheckupActivity.this);
                pdialog.setMessage("Please wait...");
                pdialog.setIndeterminate(false);//hold till procees is done
                pdialog.setCancelable(false);// set screen in freez
                pdialog.show();

            }

            @Override
            protected String doInBackground(String... strings) {
                //upload data to the database
                try {
                    //seting up the connection and send data with url
                    // create a http default client - initialize the HTTp client

                    DefaultHttpClient httpclient = new DefaultHttpClient();

                    HttpPost httppost = new HttpPost("http://192.168.43.21:8081/hbc/addpatient.php");

                    ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);

                  /*  nameValuePairs.add(new BasicNameValuePair("biz_disease", selected_disease));
                    nameValuePairs.add(new BasicNameValuePair("biz_age", patientage));
                    nameValuePairs.add(new BasicNameValuePair("biz_fullname", patientfullname));
                    nameValuePairs.add(new BasicNameValuePair("biz_email", patientemail));
                    nameValuePairs.add(new BasicNameValuePair("biz_phonenumber", patientphonenumber));
                    nameValuePairs.add(new BasicNameValuePair("biz_location", selectedlocation));
                    nameValuePairs.add(new BasicNameValuePair("biz_status", selected_status));
                    */


                    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                    HttpResponse response = httpclient.execute(httppost);

                    InputStream inputStream = response.getEntity().getContent();

                    BufferedReader rd = new BufferedReader(new InputStreamReader(inputStream), 4096);
                    String line;
                    StringBuilder sb = new StringBuilder();
                    while ((line = rd.readLine()) != null) {
                        sb.append(line);
                    }
                    rd.close();
                    responcefromphp = sb.toString();
                    inputStream.close();

                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Try Again, Unexpected Error", Toast.LENGTH_LONG).show();
                }

                return responcefromphp;


            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);

                // dismiss dialog and perform other tasks
                pdialog.dismiss();

                if (responcefromphp.equals("1")) {
                    Toast.makeText(CheckupActivity.this, "Submission Successful", Toast.LENGTH_SHORT).show();
                    Intent createaccountintent = new Intent(CheckupActivity.this, MainActivity.class);
                    startActivity(createaccountintent);

                } else if(responcefromphp.equals("0")){

                    Toast.makeText(CheckupActivity.this, "Submission failed.", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(CheckupActivity.this, "Submission failed due to technical failure, Please contact Admin.", Toast.LENGTH_SHORT).show();
                }

            }

        }//ending the async class



    //method to check internet availability(WiFi and MobileData)
    private boolean haveNetworkConnection() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }
}