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

public class CreateAccountActivity extends AppCompatActivity {

    //global variables
    String selectedlocation, username, password, confirmpassword, fullname, phonenumber, email, location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

       //referencing the spiner in java
        Spinner spinnerlocation = findViewById(R.id.spinnerlocation);
       //making reference to the other widjets
        EditText edittextfullname =  findViewById(R.id.editTextfullname);
        EditText edittextphone =  findViewById(R.id.editTextphoneno);
        EditText editTextemail =  findViewById(R.id.editTextemail);
        EditText editTextusername =  findViewById(R.id.editTextusername);
        EditText editTextccpassword =  findViewById(R.id.editTextccpassword);
        EditText edtxconfirmpassword =  findViewById(R.id.edtxconfirmpassword);

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

        //adding reference to the button in xml
        Button buttonccregister = findViewById(R.id.buttonregister);
        buttonccregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                password = editTextccpassword.getText().toString();
                username = editTextusername.getText().toString();
                fullname = edittextfullname.getText().toString();
                confirmpassword = edtxconfirmpassword.getText().toString();
                email = editTextemail.getText().toString();
                phonenumber = edittextphone.getText().toString();
               // selectedlocation = edtxconfirmpassword.getText().toString();


                if (username.isEmpty()) {
                    editTextusername.setError("User Name is Required");
                }
                if (password.isEmpty()) {
                    editTextccpassword.setError("Password is required");
                }
                if (confirmpassword.isEmpty()) {
                    editTextccpassword.setError("Password confirmation is required");
                }
                if (fullname.isEmpty()) {
                    editTextccpassword.setError("Full namee are required");
                }
                if (email.isEmpty()) {
                    editTextccpassword.setError("Email is required");
                }
                if (phonenumber.isEmpty()) {
                editTextccpassword.setError("Phone number is required");
                }else {

                    if (haveNetworkConnection()) {
                        // connected
                        new CreateAccountActivity.RegisterClass().execute();
                    } else {
                        // not connected
                        Toast.makeText(CreateAccountActivity.this, "No internet Connection", Toast.LENGTH_SHORT).show();
                    }
                }

                //Intent checkemptyintent = new Intent(CreateAccountActivity.this, LoginActivity.class);
                //startActivity(checkemptyintent);
            }
        });
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

    //creating async inner class
    class RegisterClass extends AsyncTask<String, String, String> {

        String responcefromphp;
        //create progress dialog class
        ProgressDialog pdialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //show dialog while registering Business
            pdialog = new ProgressDialog(CreateAccountActivity.this);
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

                HttpPost httppost = new HttpPost("http://192.168.43.21:8081/hbc/register.php");

                ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);

                nameValuePairs.add(new BasicNameValuePair("biz_username", username));
                nameValuePairs.add(new BasicNameValuePair("biz_password", password));
                nameValuePairs.add(new BasicNameValuePair("biz_fullname", fullname));
                nameValuePairs.add(new BasicNameValuePair("biz_email", email));
                nameValuePairs.add(new BasicNameValuePair("biz_phonenumber", phonenumber));
                nameValuePairs.add(new BasicNameValuePair("biz_selectedlocation", selectedlocation));


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
                Toast.makeText(CreateAccountActivity.this, "Submission Successful", Toast.LENGTH_SHORT).show();
                Intent createaccountintent = new Intent(CreateAccountActivity.this, LoginActivity.class);
                startActivity(createaccountintent);

            } else if(responcefromphp.equals("0")){

                Toast.makeText(CreateAccountActivity.this, "Submission failed.", Toast.LENGTH_SHORT).show();

            } else {
                Toast.makeText(CreateAccountActivity.this, "Submission failed due to technical failure, Please contact Admin.", Toast.LENGTH_SHORT).show();
            }

        }

    }//ending the async class

}