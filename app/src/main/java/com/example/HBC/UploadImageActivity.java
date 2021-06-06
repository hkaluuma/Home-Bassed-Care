package com.example.HBC;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UploadImageActivity extends AppCompatActivity implements View.OnClickListener {
    private Button UploadBn, ChooseBn;
    private EditText NAME;
    private ImageView imgView;
    private final int IMG_REQUEST = 1;
    private Bitmap bitmap;

    //private session string variables
    private String username, phonenumber, email, fullname, location, id;

   //Staging URLS
    protected String UploadUrl = "http://192.168.43.20:80/hbc/uploadimage.php";
    //variables of the dropdown list
    private String urlx = "http://192.168.43.20:80/hbc/images/";
    //String url = "http://192.168.43.20:80/hbc/populate_patient.php";


   /*//Production
    protected String UploadUrl = "https://home-based-care.herokuapp.com/uploadimage.php";
    private String urlx = "https://home-based-care.herokuapp.com/images/";
    String url = "https://home-based-care.herokuapp.com/populate_patient.php"; */

    String selected_patient;

    //string for URL//codes for the new drop down code
    Spinner spinnerPatient;
    ArrayList<String> patientList = new ArrayList<>();
    ArrayAdapter<String> patientAdapter;
    RequestQueue requestQueue;
    //progress bar
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_image);

        UploadBn = (Button)findViewById(R.id.uploadBn);
        ChooseBn = (Button)findViewById(R.id.chooseBn);
        //NAME = (EditText) findViewById(R.id.name);
        imgView = (ImageView) findViewById(R.id.imageView);

        ChooseBn.setOnClickListener(this);
        UploadBn.setOnClickListener(this);

        //to show progress bar when there is internet connection
        progressBar = (ProgressBar) findViewById(R.id.my_progressBar);
        progressBar.setVisibility(View.VISIBLE);

        if (haveNetworkConnection()) {
            progressBar.setVisibility(View.VISIBLE);
            progressBar.setIndeterminate(true);
        } else {
            progressBar.setIndeterminate(false);
            progressBar.setVisibility(View.GONE);
            //Toast.makeText(this, "No internet Connection", Toast.LENGTH_SHORT).show();
            StyleableToast.makeText(UploadImageActivity.this, "No internet Connection", R.style.exampleToast).show();
        }

        //code for the drop down
        displaySpinnerPatient();

    }

    private void displaySpinnerPatient() {
        requestQueue = Volley.newRequestQueue(this);
        spinnerPatient = findViewById(R.id.spinnerpatient);

        //calling the session method
        function_get_shared_preferences();
        String url = "http://192.168.43.20:80/hbc/api/location/read_patients.php?p_location="+location;

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
                        patientAdapter = new ArrayAdapter<>(UploadImageActivity.this,
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



    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.chooseBn:
                selectImage();
                break;

            case R.id.uploadBn:
                uploadImage();
                break;
        }

    }

    private void selectImage(){
        Intent intent =new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, IMG_REQUEST);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==IMG_REQUEST && resultCode == RESULT_OK && data !=null){
            Uri path = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),path);
                imgView.setImageBitmap(bitmap);
                imgView.setVisibility(View.VISIBLE);
                //NAME.setVisibility(View.VISIBLE);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void uploadImage(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UploadUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String Response = jsonObject.getString("response");
                            //create progress dialog class
                            ProgressDialog pdialog;
                            //show dialog while registering Business
                            pdialog = new ProgressDialog(UploadImageActivity.this);
                            pdialog.setMessage("Please wait...");
                            pdialog.setIndeterminate(false);//hold till procees is done
                            pdialog.setCancelable(false);// set screen in freez
                            pdialog.show();

                            if(Response.equals("Image Uploaded Successfully")){
                                StyleableToast.makeText(UploadImageActivity.this, Response,R.style.exampleToast).show();
                                Intent uploadedintent = new Intent(UploadImageActivity.this, MainActivity.class);
                                startActivity(uploadedintent);
                            }else{
                                StyleableToast.makeText(UploadImageActivity.this, Response,R.style.exampleToast).show();
                                //Toast.makeText(UploadImageActivity.this, Response,Toast.LENGTH_LONG).show();
                            }
                            imgView.setImageResource(0);
                            imgView.setVisibility(View.GONE);
                            //NAME.setText("");
                            //NAME.setVisibility(View.GONE);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                //params.put("name",NAME.getText().toString().trim());
                params.put("name",selected_patient);
                params.put("image",imageToString(bitmap));
                params.put("url", urlx);
                return params;
            }
        };
        MySingleton.getInstance(UploadImageActivity.this).addToRequestQue(stringRequest);

    }

    private String imageToString(Bitmap bitmap){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100, byteArrayOutputStream);
        byte[] imgBytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imgBytes,Base64.DEFAULT);
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

    //mehtod for get session function
    private void function_get_shared_preferences(){
        //variables caputring session in shared preferences
        SharedPreferences sharedpreferences = getSharedPreferences(LoginActivity.MYPREFERENCES_LOGIN, Context.MODE_PRIVATE);
        username = sharedpreferences.getString("username", null);
        phonenumber = sharedpreferences.getString("phonenumber", null);
        email = sharedpreferences.getString("email", null);
        fullname = sharedpreferences.getString("fullname", null);
        location = sharedpreferences.getString("location", null);
        id = sharedpreferences.getString("id", null);
        //end shared preferences
    }
}