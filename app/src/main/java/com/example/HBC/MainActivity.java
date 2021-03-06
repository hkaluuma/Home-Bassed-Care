package com.example.HBC;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;


import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.HBC.adapters.RecyclerViewAdapter;
import com.example.HBC.model.Anime;
import com.google.android.material.navigation.NavigationView;
import com.muddzdev.styleabletoast.StyleableToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;
    //variables
    ListView listView;
    //private session string variables
    private String username, phonenumber, email, fullname, location, id;

    //staging URL
    //private final String JSON_URL="http://192.168.43.20:80/hbc/patients.php";
    //Production URL
    //private final String JSON_URL="https://home-based-care.herokuapp.com/patients.php";
    //private final String JSON_URL="https://maendeleotech.com/hbc/patients.php";
    private JsonArrayRequest request;
    private RequestQueue requestQueue;
    private List<Anime> listAnime;
    private RecyclerView recyclerView;

    ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
            StyleableToast.makeText(MainActivity.this, "No internet Connection", R.style.exampleToast).show();
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

       /* //variables caputring session in shared preferences
        SharedPreferences sharedpreferences = getSharedPreferences(LoginActivity.MYPREFERENCES_LOGIN, Context.MODE_PRIVATE);
        String username = sharedpreferences.getString("username", null);
        String phonenumber = sharedpreferences.getString("phonenumber", null);
        String email = sharedpreferences.getString("email", null);
        String fullname = sharedpreferences.getString("fullname", null);
        String location = sharedpreferences.getString("location", null);
        String id = sharedpreferences.getString("id", null);
        //end shared preferences

        String JSON_URL="http://localhost:80/hbc/api/location/read_single.php?p_location="+location; */

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setItemIconTintList(null);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

//new codes for recycler
        listAnime = new ArrayList<>();
        recyclerView = findViewById(R.id.recyclerviewid);
        jsonrequest();

    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.nav_home:
                startActivity(new Intent(MainActivity.this, MainActivity.class));
                break;
            case R.id.nav_checkup:
               startActivity(new Intent(MainActivity.this, CheckupActivity.class));
                break;
            case R.id.nav_maps:
                String uri = "https://www.google.com/maps/d/u/0/viewer?mid=1dJjQ4jCUPR89umU3AIBPn59R9Pqp77Ms&ll=0.40092934615056425%2C32.57792117264799&z=12\n";
                Intent locintent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                startActivity(locintent);
                break;
            case R.id.nav_patients:
                startActivity(new Intent(MainActivity.this, PatientsActivity.class));
                break;
            case R.id.nav_addpatient:
                startActivity(new Intent(MainActivity.this, AddPatientActivity.class));
                break;
            case R.id.nav_call:
                //makecall();
                startActivity(new Intent(MainActivity.this, ContactUsActivity.class));
                break;
            case R.id.nav_discharge:
                //makecall();
                startActivity(new Intent(MainActivity.this, DischargeActivity.class));
                break;
            case R.id.nav_photo:
                startActivity(new Intent(MainActivity.this, UploadImageActivity.class));
                break;
            case R.id.nav_logout:
                //Toast.makeText(MainActivity.this, "Logging out ... ", Toast.LENGTH_SHORT).show();
                //StyleableToast.makeText(MainActivity.this, "Logging out ...", R.style.exampleToast).show();
                startActivity(new Intent(MainActivity.this, AccountActivity.class));
                break;

        }
        drawer.closeDrawer(GravityCompat.START);
        return true;

    }


    //another function for drawer
    public void onBackPressed(){
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }else{
            super.onBackPressed();
        }
    }

    private void jsonrequest() {
        //getting the session and url
        function_get_shared_preferences();
        //String JSON_URL="http://192.168.1.150:8080/hbc/api/location/read_single.php?p_location="+location;
        String JSON_URL="https://maendeleotech.com/hbc/api/location/read_single.php?p_location="+location;

        /******************** try catch needed here ******************/
        request = new JsonArrayRequest(JSON_URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;
                for(int i=0; i <response.length(); i++) {
                    try {
                        jsonObject = response.getJSONObject(i);
                        Anime anime = new Anime();
                        anime.setFullnames(jsonObject.getString("fullnames"));
                        anime.setComments(jsonObject.getString("comments"));
                        anime.setPatient_id(jsonObject.getString("patient_id"));
                        anime.setTest_status(jsonObject.getString("test_status"));
                        anime.setLocation(jsonObject.getString("location"));
                        anime.setAge(jsonObject.getString("age"));
                        anime.setPhonenumber(jsonObject.getString("phonenumber"));
                        anime.setDisease(jsonObject.getString("disease"));
                        anime.setImage_url(jsonObject.getString("img"));
                        listAnime.add(anime);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                setuprecycleerview(listAnime);
            }


        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        requestQueue = Volley.newRequestQueue(MainActivity.this);
        requestQueue.add(request);
    }

    private void setuprecycleerview(List<Anime> listAnime) {
        RecyclerViewAdapter myadapter = new RecyclerViewAdapter(this,listAnime);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.setAdapter(myadapter);
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