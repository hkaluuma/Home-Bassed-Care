package com.example.HBC;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

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
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class CheckupActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    //global variables
    String selected_patient, selected_chills, patienttemperature, selected_chestpain, selected_headache, selected_cough, selected_difficultbreathing,
            selected_fatigue, selected_runnynose, selected_diarrhea, selected_throat;

    Spinner spinnerPatient;
    ArrayList<String> patientList = new ArrayList<>();
    ArrayAdapter<String> patientAdapter;
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkup);

        displaySpinnerPatient();

        //referencing the variables in xml
        Button buttonsubmitcheckup = findViewById(R.id.buttonsubmitcheckup);
        EditText editTexttemperature = findViewById(R.id.editTexttemperature);

        //calling method to display spinner
        displaySpinnerChills();
        displaySpinnerChestpain();
        displaySpinnerHeadache();
        displaySpinnerCough();
        spinnerdifficultybreathing();
        displaySpinnerfatigue();
        displaySpinnerrunnynose();
        displaySpinnerdiarrhea();
        displaySpinnerthroat();


        buttonsubmitcheckup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                patienttemperature = editTexttemperature.getText().toString();


                if (patienttemperature.isEmpty()) {
                    editTexttemperature.setError("Patient temperature needed.");
                }else if (selected_patient.equals("Not Specified")) {
                    Toast.makeText(CheckupActivity.this, "Patient not selected", Toast.LENGTH_SHORT).show();
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
                }else if (selected_throat.equals("Not Specified")) {
                    Toast.makeText(CheckupActivity.this, "Sore throat status not specified", Toast.LENGTH_SHORT).show();
                }
                else {

                    if (haveNetworkConnection()) {
                        // connected
                        new CheckupActivity.CheckupClass().execute();
                    } else {
                        // not connected
                        Toast.makeText(CheckupActivity.this, "No internet Connection", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });

    }

    private void displaySpinnerthroat() {
        //referencing the spiner in java
        Spinner spinnerthroat = findViewById(R.id.spinnerthroat);
        //disease
        String[] throatStatus = {"Do you have a sore throat?","Yes","No"};
        //create array adapter
        ArrayAdapter<String> spinneradaptera = new ArrayAdapter<String>(CheckupActivity.this,R.layout.spinneritemdesign2,R.id.textViewSpinneritem2,throatStatus) {
            @Override
            public boolean isEnabled(int position) {
                TextView tv = findViewById(R.id.textViewSpinneritem2);
                if (position == 0) {
                    // Disable the first item from Spinner
                    tv.setTextColor(Color.GRAY);
                    return false;
                } else {
                    tv.setTextColor(Color.BLACK);
                    return true;
                }
            }
        };

        //assign array adapter to a spinner
        spinnerthroat.setAdapter(spinneradaptera);
        //spinner item listener for disease
        spinnerthroat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selected_throat=throatStatus[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selected_throat = "Not Specified";

            }
        });

    }


    public void displaySpinnerPatient(){

        requestQueue = Volley.newRequestQueue(this);
        spinnerPatient = findViewById(R.id.spinnerpatient);

        String url = "http://192.168.43.20:8081/hbc/populate_patient.php";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    JSONArray jsonArray = response.getJSONArray("patients");
                    for(int i = 0; i<jsonArray.length(); i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String patientName = jsonObject.optString("p_fullnames");
                        patientList.add(patientName);
                        patientAdapter = new ArrayAdapter<>(CheckupActivity.this,
                                android.R.layout.simple_spinner_item, patientList);
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
        spinnerPatient.setOnItemSelectedListener(this);

        /*

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
            }); */
        }



        public void displaySpinnerChills() {
            //referencing the spiner in java
            Spinner spinnerchills = findViewById(R.id.spinnerchills);
            //disease
            String[] chillsStatus = {"Do you have Chills?","Yes","No"};
            //create array adapter
            ArrayAdapter<String> spinneradaptera = new ArrayAdapter<String>(CheckupActivity.this,R.layout.spinneritemdesign2,R.id.textViewSpinneritem2,chillsStatus) {
                @Override
                public boolean isEnabled(int position) {
                    TextView tv = findViewById(R.id.textViewSpinneritem2);
                    if (position == 0) {
                        // Disable the first item from Spinner
                        // First item will be use for hint
                        tv.setTextColor(Color.GRAY);
                        return false;
                    } else {
                        tv.setTextColor(Color.BLACK);
                        return true;
                    }
                }

            };
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
        String[] chestpainStatus = {"Do you have chest pain?","Yes","No"};
        //create array adapter
        ArrayAdapter<String> spinneradaptera = new ArrayAdapter<String>(CheckupActivity.this,R.layout.spinneritemdesign2,R.id.textViewSpinneritem2,chestpainStatus) {
            @Override
            public boolean isEnabled(int position) {
                TextView tv = findViewById(R.id.textViewSpinneritem2);
                if (position == 0) {
                    // Disable the first item from Spinner
                    tv.setTextColor(Color.GRAY);
                    return false;
                } else {
                    tv.setTextColor(Color.BLACK);
                    return true;
                }
            }
        };

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
        String[] headacheStatus = {"Do you have headache?","Yes","No"};
        //create array adapter
        ArrayAdapter<String> spinneradaptera = new ArrayAdapter<String>(CheckupActivity.this,R.layout.spinneritemdesign2,R.id.textViewSpinneritem2,headacheStatus){
            @Override
            public boolean isEnabled(int position) {
                TextView tv = findViewById(R.id.textViewSpinneritem2);
                if (position == 0) {
                    // Disable the first item from Spinner
                    tv.setTextColor(Color.GRAY);
                    return false;
                } else {
                    tv.setTextColor(Color.BLACK);
                    return true;
                }
            }
        };
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
        String[] CoughStatus = {"Do you have cough?","Yes","No"};
        //create array adapter
        ArrayAdapter<String> spinneradaptera = new ArrayAdapter<String>(CheckupActivity.this,R.layout.spinneritemdesign2,R.id.textViewSpinneritem2,CoughStatus){
            @Override
            public boolean isEnabled(int position) {
                TextView tv = findViewById(R.id.textViewSpinneritem2);
                if (position == 0) {
                    // Disable the first item from Spinner
                    tv.setTextColor(Color.GRAY);
                    return false;
                } else {
                    tv.setTextColor(Color.BLACK);
                    return true;
                }
            }
        };
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
        String[] breathingStatus = {"Do you have any difficulty breathing?","Yes","No"};
        //create array adapter
        ArrayAdapter<String> spinneradaptera = new ArrayAdapter<String>(CheckupActivity.this,R.layout.spinneritemdesign2,R.id.textViewSpinneritem2,breathingStatus){
            @Override
            public boolean isEnabled(int position) {
                TextView tv = findViewById(R.id.textViewSpinneritem2);
                if (position == 0) {
                    // Disable the first item from Spinner
                    tv.setTextColor(Color.GRAY);
                    return false;
                } else {
                    tv.setTextColor(Color.BLACK);
                    return true;
                }
            }
        };
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
        String[] fatigueStatus = {"Are you feeling any fatigue?","Yes", "No"};
        //create array adapter
        ArrayAdapter<String> spinneradaptera = new ArrayAdapter<String>(CheckupActivity.this, R.layout.spinneritemdesign2, R.id.textViewSpinneritem2, fatigueStatus)
        {
            @Override
            public boolean isEnabled(int position) {
                TextView tv = findViewById(R.id.textViewSpinneritem2);
                if (position == 0) {
                    // Disable the first item from Spinner
                    tv.setTextColor(Color.GRAY);
                    return false;
                } else {
                    tv.setTextColor(Color.BLACK);
                    return true;
                }
            }
        };
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
        String[] noseStatus = {"Are you having a running nose?","Yes", "No"};
        //create array adapter
        ArrayAdapter<String> spinneradaptera = new ArrayAdapter<String>(CheckupActivity.this, R.layout.spinneritemdesign2, R.id.textViewSpinneritem2, noseStatus){
            @Override
            public boolean isEnabled(int position) {
                TextView tv = findViewById(R.id.textViewSpinneritem2);
                if (position == 0) {
                    // Disable the first item from Spinner
                    tv.setTextColor(Color.GRAY);
                    return false;
                } else {
                    tv.setTextColor(Color.BLACK);
                    return true;
                }
            }
        };
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
        String[] diarrheaStatus = {"Do you have diarrhea?","Yes", "No"};
        //create array adapter
        ArrayAdapter<String> spinneradaptera = new ArrayAdapter<String>(CheckupActivity.this, R.layout.spinneritemdesign2, R.id.textViewSpinneritem2, diarrheaStatus){
            @Override
            public boolean isEnabled(int position) {
                TextView tv = findViewById(R.id.textViewSpinneritem2);
                if (position == 0) {
                    // Disable the first item from Spinner
                    tv.setTextColor(Color.GRAY);
                    return false;
                } else {
                    tv.setTextColor(Color.BLACK);
                    return true;
                }
            }
        };
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

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
            selected_patient = adapterView.getSelectedItem().toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        selected_patient = "Not Specified";

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

                    HttpPost httppost = new HttpPost("http://192.168.43.20:8081/hbc/checkup.php");

                    ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);

                    nameValuePairs.add(new BasicNameValuePair("biz_patient", selected_patient));
                    nameValuePairs.add(new BasicNameValuePair("biz_chills", selected_chills));
                    nameValuePairs.add(new BasicNameValuePair("biz_temperature", patienttemperature));
                    nameValuePairs.add(new BasicNameValuePair("biz_chestpain", selected_chestpain));
                    nameValuePairs.add(new BasicNameValuePair("biz_headache", selected_headache));
                    nameValuePairs.add(new BasicNameValuePair("biz_cough", selected_cough));
                    nameValuePairs.add(new BasicNameValuePair("biz_breathing", selected_difficultbreathing));
                    nameValuePairs.add(new BasicNameValuePair("biz_fatigue", selected_fatigue));
                    nameValuePairs.add(new BasicNameValuePair("biz_nose", selected_runnynose));
                    nameValuePairs.add(new BasicNameValuePair("biz_diarrhea", selected_diarrhea));
                    nameValuePairs.add(new BasicNameValuePair("biz_throat", selected_throat));

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
                    Toast.makeText(CheckupActivity.this, "Checkup Submission Successful", Toast.LENGTH_SHORT).show();
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