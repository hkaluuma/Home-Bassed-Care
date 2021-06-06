package com.example.HBC;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.muddzdev.styleabletoast.StyleableToast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DischargeActivity extends AppCompatActivity {

    private Button dischargeBn, rechargeBtn;


    //Staging URLS
    protected String discharge_url = "http://192.168.43.20:80/hbc/discharge_patient.php";
    protected String readmit_url = "http://192.168.43.20:80/hbc/readmit_patient.php";
    String url = "http://192.168.43.20:80/hbc/populate_patient.php";
    String url2 = "http://192.168.43.20:80/hbc/populate_patient2.php";

   /* //Production
    protected String discharge_url = "https://home-based-care.herokuapp.com/discharge_patient.php";
    protected String readmit_url = "https://home-based-care.herokuapp.com/readmit_patient.php";
    String url = "https://home-based-care.herokuapp.com/populate_patient.php";
    String url2 = "https://home-based-care.herokuapp.com/populate_patient2.php"; */


    String selected_patient;
    String selected_patient2;

    //string for URL//codes for the new drop down code
    Spinner spinnerPatient, spinnerPatient2;
    ArrayList<String> patientList1 = new ArrayList<>();
    ArrayList<String> patientList2 = new ArrayList<>();
    ArrayAdapter<String> patientAdapter;
    ArrayAdapter<String> patientAdapter2;
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discharge);

        dischargeBn = (Button)findViewById(R.id.DischargeBn);
        rechargeBtn= (Button)findViewById(R.id.rechargeBtn);

        rechargeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (haveNetworkConnection()) {
                    // connected
                    new DischargeActivity.Readmitupdate().execute();
                } else {
                    // not connected
                    //.makeText(LoginActivity.this, "No internet Connection", Toast.LENGTH_SHORT).show();
                    StyleableToast.makeText(DischargeActivity.this, "No internet Connection", R.style.exampleToast).show();

                }

            }
        });


        dischargeBn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (haveNetworkConnection()) {
                    // connected
                    new DischargeActivity.Createupdate().execute();
                } else {
                    // not connected
                    //.makeText(LoginActivity.this, "No internet Connection", Toast.LENGTH_SHORT).show();
                    StyleableToast.makeText(DischargeActivity.this, "No internet Connection", R.style.exampleToast).show();

                }
            }
        });

        //code for the drop down
        displaySpinnerPatient();
        displaySpinnerPatient2();
    }


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



    //async task class to login
    class Createupdate extends AsyncTask<String, String, String> {

        String responcefromphp;
        //create progress dialog class
        ProgressDialog pdialog;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //show dialog while registering Business
            pdialog = new ProgressDialog(DischargeActivity.this);


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
                HttpPost httppost = new HttpPost(discharge_url);
                //HttpPost httppost = new HttpPost("http://192.168.43.20:8081/hbc/login.php");

                ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);

                nameValuePairs.add(new BasicNameValuePair("biz_fullnames", selected_patient));
                //nameValuePairs.add(new BasicNameValuePair("biz_pass", pass));


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
                Toast.makeText(getApplicationContext(), "Try Again", Toast.LENGTH_LONG).show();

            }

            return responcefromphp;


        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            // dismiss dialog and perform other tasks
            pdialog.dismiss();

            if (responcefromphp.equals("1")) {

                //.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                StyleableToast.makeText(DischargeActivity.this, "Patient Discharge Successful", R.style.exampleToast).show();
                Intent loginintent = new Intent(DischargeActivity.this, MainActivity.class);
                startActivity(loginintent);

            } else if(responcefromphp.equals("0")){

                //Toast.makeText(LoginActivity.this, "Login Failed customer not Registered.", Toast.LENGTH_SHORT).show();
                StyleableToast.makeText(DischargeActivity.this, "Patient Discharge Failed", R.style.exampleToast).show();

            } else {
                //Toast.makeText(LoginActivity.this, "Login Failed, contact admin or Try Again", Toast.LENGTH_SHORT).show();
                StyleableToast.makeText(DischargeActivity.this, "Discharge Failed, contact admin or Try Again", R.style.exampleToast).show();
            }

        }

    }//end of asnck task


    //async task class to login
    class Readmitupdate extends AsyncTask<String, String, String> {

        String responcefromphp;
        //create progress dialog class
        ProgressDialog pdialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //show dialog while registering Business
            pdialog = new ProgressDialog(DischargeActivity.this);

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
                HttpPost httppost = new HttpPost(readmit_url);

                ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);

                nameValuePairs.add(new BasicNameValuePair("biz_fullnames", selected_patient2));

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
                Toast.makeText(getApplicationContext(), "Try Again", Toast.LENGTH_LONG).show();

            }
            return responcefromphp;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            // dismiss dialog and perform other tasks
            pdialog.dismiss();

            if (responcefromphp.equals("1")) {

                //.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                StyleableToast.makeText(DischargeActivity.this, "Patient Re-admission Successful", R.style.exampleToast).show();
                Intent loginintent = new Intent(DischargeActivity.this, MainActivity.class);
                startActivity(loginintent);

            } else if(responcefromphp.equals("0")){

                //Toast.makeText(LoginActivity.this, "Login Failed customer not Registered.", Toast.LENGTH_SHORT).show();
                StyleableToast.makeText(DischargeActivity.this, "Patient Re-admission Failed", R.style.exampleToast).show();

            } else {
                //Toast.makeText(LoginActivity.this, "Login Failed, contact admin or Try Again", Toast.LENGTH_SHORT).show();
                StyleableToast.makeText(DischargeActivity.this, "Re-admission Failed, contact admin or Try Again", R.style.exampleToast).show();
            }

        }

    }//end of asnck task


    private void displaySpinnerPatient() {
        requestQueue = Volley.newRequestQueue(this);
        spinnerPatient = findViewById(R.id.spinnerpatient);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    JSONArray jsonArray = response.getJSONArray("patients");
                    for(int i = 0; i<jsonArray.length(); i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String patientName = jsonObject.optString("p_fullnames");
                        patientList1.add(patientName);
                        patientAdapter = new ArrayAdapter<>(DischargeActivity.this,
                                android.R.layout.simple_spinner_item, patientList1);
                        patientAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinnerPatient.setAdapter(patientAdapter);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(jsonObjectRequest);

        //spinner item listener for patient
        spinnerPatient.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                selected_patient = adapterView.getSelectedItem().toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selected_patient = "Not Specified";

            }

        });
    }




    private void displaySpinnerPatient2() {
        requestQueue = Volley.newRequestQueue(this);
        spinnerPatient2 = findViewById(R.id.spinnerpatient2);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                url2, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    JSONArray jsonArray = response.getJSONArray("patients");
                    for(int i = 0; i<jsonArray.length(); i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String patientName = jsonObject.optString("p_fullnames");
                        patientList2.add(patientName);
                        patientAdapter2 = new ArrayAdapter<>(DischargeActivity.this,
                                android.R.layout.simple_spinner_item, patientList2);
                        patientAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinnerPatient2.setAdapter(patientAdapter2);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(jsonObjectRequest);

        //spinner item listener for patient
        spinnerPatient2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                selected_patient2 = adapterView.getSelectedItem().toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selected_patient2 = "Not Specified";

            }

        });
    }


}