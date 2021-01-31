package com.example.HBC;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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

public class AddPatientActivity extends AppCompatActivity {

    //global variables
    String selectedlocation, patientage, patientfullname, patientphonenumber, patientemail, selected_disease, selected_status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_patient);

        //making reference to the other widjets
        final EditText edittextpatientfullname =  findViewById(R.id.edittextpatientfullname);
        final EditText editTextpatientphone =  findViewById(R.id.editTextpatientphone);
        final EditText editTextpateintemail =  findViewById(R.id.editTextpateintemail);
        final EditText editTextpateintage =  findViewById(R.id.editTextpateintage);
        //adding reference to the button in xml
        Button buttonccregister = findViewById(R.id.buttonpatientregister);

        //calling method to display spinner
        displaySpinnerlocation();
        displaySpinnerdisease();
        displaySpinnerstatus();

        buttonccregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                patientfullname = edittextpatientfullname.getText().toString();
                patientphonenumber = editTextpatientphone.getText().toString();
                patientemail = editTextpateintemail.getText().toString();
                patientage = editTextpateintage.getText().toString();

                if (patientfullname.isEmpty()) {
                    edittextpatientfullname.setError("Patient Full names are required");
                }else if (patientphonenumber.isEmpty()) {
                    editTextpatientphone.setError("Phone number is required");
                }else if (patientemail.isEmpty()) {
                    editTextpateintemail.setError("Email is required");
                }else if (selectedlocation.equals("Not Specified")) {
                    Toast.makeText(AddPatientActivity.this, "Select the location", Toast.LENGTH_SHORT).show();
                }else if (patientage.isEmpty()) {
                    editTextpateintage.setError("User Name is Required");
                }else if (selected_disease.isEmpty()) {
                    Toast.makeText(AddPatientActivity.this, "Passwords not matching", Toast.LENGTH_SHORT).show();
                }else if (selected_status.isEmpty()) {
                    Toast.makeText(AddPatientActivity.this, "Passwords not matching", Toast.LENGTH_SHORT).show();
                }else {

                    if (haveNetworkConnection()) {
                        // connected
                        new AddPatientActivity.RegisterClass().execute();
                    } else {
                        // not connected
                        Toast.makeText(AddPatientActivity.this, "No internet Connection", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });
    }


    public void displaySpinnerdisease() {
        //referencing the spiner in java
        Spinner spinnerdisease = findViewById(R.id.spinnerpatientdisease);
        //disease
        String[] diseaseNames = {"Covid19","Others"};
        //create array adapter
        ArrayAdapter<String> spinneradapterz = new ArrayAdapter<String>(AddPatientActivity.this,R.layout.spinneritemdesgin,R.id.textViewSpinneritem,diseaseNames);
        //assign array adapter to a spinner
        spinnerdisease.setAdapter(spinneradapterz);
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


    public void displaySpinnerlocation() {
        //referencing the spiner in java
        final Spinner spinnerlocation = findViewById(R.id.spinnerpatientlocation);
        //data source
        String[] patientlocationNames = {"Kampala","Wakiso","Mukono","Jinja","Kiryandongo","Karamonja"};
        //create array adapter
        //create array adapter
        ArrayAdapter<String> spinneradapters = new ArrayAdapter<String>(AddPatientActivity.this,R.layout.spinneritemdesgin,R.id.textViewSpinneritem,patientlocationNames);
        //assign array adapter to a spinner
        spinnerlocation.setAdapter(spinneradapters);

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

    }

    public void displaySpinnerstatus() {
        //referencing the spiner in java
        Spinner spinnerstatus = findViewById(R.id.spinnerpatientstatus);
        //disease
        String[] statusNames = {"Positive","Negative"};
        //create array adapter
        ArrayAdapter<String> spinneradapterxx = new ArrayAdapter<String>(AddPatientActivity.this,R.layout.spinneritemdesgin,R.id.textViewSpinneritem,statusNames);
        //assign array adapter to a spinner
        spinnerstatus.setAdapter(spinneradapterxx);
        //spinner item listener for disease
        spinnerstatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selected_status=statusNames[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selected_status = "Not Specified";

            }
        });

    }

    //creating async inner class
    class RegisterClass extends AsyncTask<String, String, String> {

        String responcefromphp;
        //create progress dialog class
        ProgressDialog pdialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //show dialog while registering Business
            pdialog = new ProgressDialog(AddPatientActivity.this);
            pdialog.setMessage("Please wait...");
            pdialog.setIndeterminate(false);//hold till procees is done
            pdialog.setCancelable(false);// set screen in freez
            pdialog.show();

        }

        @Override
        protected String doInBackground(String... strings) {
            //upload data to the database
            try {
                /* seting up the connection and send data with url */
                // create a http default client - initialize the HTTp client

                DefaultHttpClient httpclient = new DefaultHttpClient();

                HttpPost httppost = new HttpPost("http://192.168.43.20:8081/hbc/addpatient.php");

                ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);

                nameValuePairs.add(new BasicNameValuePair("biz_disease", selected_disease));
                nameValuePairs.add(new BasicNameValuePair("biz_age", patientage));
                nameValuePairs.add(new BasicNameValuePair("biz_fullname", patientfullname));
                nameValuePairs.add(new BasicNameValuePair("biz_email", patientemail));
                nameValuePairs.add(new BasicNameValuePair("biz_phonenumber", patientphonenumber));
                nameValuePairs.add(new BasicNameValuePair("biz_location", selectedlocation));
                nameValuePairs.add(new BasicNameValuePair("biz_status", selected_status));


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
                Toast.makeText(AddPatientActivity.this, "Submission Successful", Toast.LENGTH_SHORT).show();
                Intent createaccountintent = new Intent(AddPatientActivity.this, MainActivity.class);
                startActivity(createaccountintent);

            } else if(responcefromphp.equals("0")){

                Toast.makeText(AddPatientActivity.this, "Submission failed.", Toast.LENGTH_SHORT).show();

            } else {
                Toast.makeText(AddPatientActivity.this, "Submission failed due to technical failure, Please contact Admin.", Toast.LENGTH_SHORT).show();
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