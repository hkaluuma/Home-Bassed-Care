package com.example.HBC;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class PatientsActivity extends AppCompatActivity {

    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patients);

        listView=(ListView)findViewById(R.id.mylistview);
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("HILLARY");
        arrayList.add("MOSES");
        arrayList.add("EDWARD");
        arrayList.add("KENNETH");
        arrayList.add("TOMMY");
        arrayList.add("JEAN");
        arrayList.add("ALLAN");

        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,arrayList);
        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(PatientsActivity.this,"Clicked item"+ position +" "+ arrayList.get(position).toString(), Toast.LENGTH_SHORT).show();
            }
        });



    }
}