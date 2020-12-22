package com.example.HBC;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.content.Intent;
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

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;
    //variables
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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



        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

      /* //so that when you open the app navigation drawer, this activity loads
        if(savedInstanceState == null){
        //getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new PatientsFragment()).commit();
        //startActivity(new Intent(getApplicationContext(), PatientsFragment.class));
           startActivity(new Intent(MainActivity.this, CheckupActivity.class));
            //Intent navpatientsintent = new Intent(MainActivity.this, PatientsFragment.class);
           // startActivity(navpatientsintent);
        navigationView.setCheckedItem(R.id.nav_patients);
        } */
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
                //getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ProfileFragment()).commit();
               //startActivity(new Intent(getApplicationContext(), ProfileFragment.class));
                startActivity(new Intent(MainActivity.this, ProfileActivity.class));
                //Intent navprofileintent = new Intent(MainActivity.this, ProfileFragment.class);
                //startActivity(navprofileintent);

                break;
            case R.id.nav_patients:
                //getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new PatientsFragment()).commit();
                //startActivity(new Intent(getApplicationContext(), PatientsFragment.class));
                startActivity(new Intent(MainActivity.this, PatientsActivity.class));
                //Intent navpatientsintent = new Intent(MainActivity.this, PatientsFragment.class);
                //startActivity(navpatientsintent);

                break;
            case R.id.nav_addpatient:
                //getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new AddPatientFragment()).commit();
                //startActivity(new Intent(getApplicationContext(), AddPatientFragment.class));
                startActivity(new Intent(MainActivity.this, AddPatientActivity.class));
                //Intent navaddpatientintent = new Intent(MainActivity.this, AddPatientFragment.class);
                //startActivity(navaddpatientintent);
                break;
            case R.id.nav_call:
                Toast.makeText(this,"Call", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_chat:
                Toast.makeText(this,"Message", Toast.LENGTH_SHORT).show();
                break;

        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void onBackPressed(){
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }else{
            super.onBackPressed();
        }
    }
}