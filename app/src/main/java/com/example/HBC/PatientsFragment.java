package com.example.HBC;

import android.os.Bundle;
import android.view.*;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;


import java.util.ArrayList;

/*public class PatientsFragment extends Fragment {
    //variables
   ListView listview;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {

      View view = inflater.inflate(R.layout.fragment_patients, container, false);



        return view;
    }

    Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        listview = view.findViewById(R.id.mylistview);

        ArrayList<String> arraysList = new ArrayList<>();
        arraysList.add("Hillary");
        arraysList.add("Musoke");
        arraysList.add("Edward");
        arraysList.add("Jimmy");
        arraysList.add("Albert");
        arraysList.add("Patrick");

        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, arraysList);
        listview.setAdapter(arrayAdapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(this,"click")
            }
        });

    }

    }*/


public class PatientsFragment extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_patients);

        //variables
        ListView listview;


    }

}




