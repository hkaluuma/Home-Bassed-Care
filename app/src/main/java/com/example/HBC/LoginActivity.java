package com.example.HBC;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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

import android.view.View;

public class LoginActivity extends AppCompatActivity {

    String username, pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        final EditText editTextusername = findViewById(R.id.editTextusername);
        final EditText editTextpassword = findViewById(R.id.editTextpassword);
        final TextView textViewcreateacc = findViewById(R.id.textViewcreateacc);
        final TextView textViewforgotpassword = findViewById(R.id.textViewforgotpassword);

        textViewcreateacc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent textViewcreateaccintent= new Intent(LoginActivity.this, CreateAccountActivity.class);
                startActivity(textViewcreateaccintent);
            }
        });

        textViewforgotpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent forgotpasswordintent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                startActivity(forgotpasswordintent);
            }
        });


        Button buttonlogin = findViewById(R.id.buttonlogin);
        buttonlogin.setOnClickListener(new View.OnClickListener() {
            //new code segment
          /*  public void onClick(View view) {
                Intent mainactivityintent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(mainactivityintent);
            }// end of new code segment
            */
           @Override
            public void onClick(View view) {
                pass = editTextpassword.getText().toString();
                username = editTextusername.getText().toString();

                if (username.isEmpty()) {
                    editTextusername.setError("User Name is Required");
                }

                if (pass.isEmpty()) {
                    editTextpassword.setError("Password is required");

                } else {

                    if (haveNetworkConnection()) {
                        // connected
                        new LoginActivity.CreateLogin().execute();
                    } else {
                        // not connected
                        Toast.makeText(LoginActivity.this, "No internet Connection", Toast.LENGTH_SHORT).show();

                    }

                }
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


    //async task class to login
    class CreateLogin extends AsyncTask<String, String, String> {

        String responcefromphp;
        //create progress dialog class
        ProgressDialog pdialog;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //show dialog while registering Business
            pdialog = new ProgressDialog(LoginActivity.this);


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

                HttpPost httppost = new HttpPost("http://192.168.43.21:8081/hbc/login.php");
               //HttpPost httppost = new HttpPost("http://44.236.122.133/hbc/login.php");

                ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);

                nameValuePairs.add(new BasicNameValuePair("biz_username", username));
                nameValuePairs.add(new BasicNameValuePair("biz_pass", pass));


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

                Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                Intent loginintent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(loginintent);

            } else if(responcefromphp.equals("0")){

                Toast.makeText(LoginActivity.this, "Login Failed customer not Registered.", Toast.LENGTH_SHORT).show();

            } else {
                Toast.makeText(LoginActivity.this, "Login Failed, contact admin or Try Again", Toast.LENGTH_SHORT).show();
            }

        }

    }//end of asnck task




}
