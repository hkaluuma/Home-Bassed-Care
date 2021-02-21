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
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;
    //variables
    ListView listView;

    //new variables
    private final String JSON_URL="http://192.168.43.20:8081/hbc/patients.php";
    private JsonArrayRequest request;
    private RequestQueue requestQueue;
    private List<Anime> listAnime;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*
        //new codes for list view
        listView=(ListView)findViewById(R.id.mylistview2);
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("HILLARY");
        arrayList.add("MOSES");
        arrayList.add("EDWARD");
        arrayList.add("KENNETH");
        arrayList.add("TOMMY");
        arrayList.add("JEAN");
        arrayList.add("ALLAN");

        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,arrayList);
        //listView.setAdapter(arrayAdapter);
        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MainActivity.this,"Clicked item"+ position +" "+ arrayList.get(position).toString(), Toast.LENGTH_SHORT).show();
            }
        });//end of the new code
        */



        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
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
                //getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new CheckupFragment()).commit();
                //startActivity(new Intent(getApplicationContext(), CheckupFragment.class));
               startActivity(new Intent(MainActivity.this, CheckupActivity.class));
                //Intent nav_messageintent = new Intent(MainActivity.this, MessageFragment.class);
               // startActivity(nav_messageintent);
                break;
            case R.id.nav_profile:
                startActivity(new Intent(MainActivity.this, ProfileActivity.class));
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

          /*  case R.id.nav_chat:
                Toast.makeText(this,"Message", Toast.LENGTH_SHORT).show();
                break; */

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
}