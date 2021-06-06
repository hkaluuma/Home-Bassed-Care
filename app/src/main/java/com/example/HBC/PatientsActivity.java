package com.example.HBC;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.os.Bundle;
import android.os.StrictMode;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.view.*;

import androidx.appcompat.app.AppCompatActivity;

import com.example.HBC.R;

public class PatientsActivity extends AppCompatActivity {

    @SuppressLint("NewApi")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patients);
// Permission StrictMode
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        final Button btn1 = (Button)findViewById(R.id.Searchbtn);
// Perform action on click
        btn1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                SearchData();
            }
        });
    }

    public void SearchData()
    {
// listView1
        final ListView lisView1 = (ListView)findViewById(R.id.listView1);
// editText1
        final EditText inputText = (EditText)findViewById(R.id.editText1);
        //production
        //String url = "https://home-based-care.herokuapp.com/displaycheckup.php";
        //staging
       String url = "http://192.168.43.20:80/hbc/displaycheckup.php";


// Paste Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("txtKeyword", inputText.getText().toString()));

        try {
            JSONArray data = new JSONArray(getJSONUrl(url,params));
            final ArrayList<HashMap<String, String>> MyArrList = new ArrayList<HashMap<String, String>>();
            HashMap<String, String> map;

            for(int i = 0; i < data.length(); i++){
                JSONObject c = data.getJSONObject(i);

                map = new HashMap<String, String>();
                map.put("check_up_id", c.getString("check_up_id"));
                map.put("patient_names", c.getString("patient_names"));
                map.put("patient_temperature", c.getString("patient_temperature"));
                map.put("cough", c.getString("cough"));
                map.put("chills", c.getString("chills"));
                map.put("new_chest_pain", c.getString("new_chest_pain"));
                map.put("headache", c.getString("headache"));
                map.put("difficulty_breathing", c.getString("difficulty_breathing"));
                map.put("fatigue", c.getString("fatigue"));
                map.put("runny_nose", c.getString("runny_nose"));
                map.put("diarrhea", c.getString("diarrhea"));
                map.put("sore_throat", c.getString("sore_throat"));
                map.put("check_up_time", c.getString("check_up_time"));
                MyArrList.add(map);
            }

            SimpleAdapter sAdap;
            sAdap = new SimpleAdapter(PatientsActivity.this, MyArrList, R.layout.activity_column,
                    new String[] {"patient_names", "patient_temperature", "check_up_time"}, new int[] {R.id.ColPatientName, R.id.ColTemperature, R.id.ColCheckupDate});
            lisView1.setAdapter(sAdap);

            final AlertDialog.Builder viewDetail = new AlertDialog.Builder(this);
// OnClick Item
            lisView1.setOnItemClickListener(new OnItemClickListener() {

                public void onItemClick(AdapterView<?> myAdapter, View myView,

                                        int position, long mylng) {
                    String strCheckupid = MyArrList.get(position).get("check_up_id")
                            .toString();
                    String strPatientname = MyArrList.get(position).get("patient_names")
                            .toString();
                    String strTemperature = MyArrList.get(position).get("patient_temperature")
                            .toString();
                    String strCough = MyArrList.get(position).get("cough")
                            .toString();
                    String strChills = MyArrList.get(position).get("chills")
                            .toString();
                    String strNew_Chest_Pain = MyArrList.get(position).get("new_chest_pain")
                            .toString();
                    String strHeadache = MyArrList.get(position).get("headache")
                            .toString();
                    String strDifficulty_Breathing = MyArrList.get(position).get("difficulty_breathing")
                            .toString();
                    String strFatigue = MyArrList.get(position).get("fatigue")
                            .toString();
                    String strRunny_Nose = MyArrList.get(position).get("runny_nose")
                            .toString();
                    String strDiarrhea = MyArrList.get(position).get("diarrhea")
                            .toString();
                    String strSore_Throat = MyArrList.get(position).get("sore_throat")
                            .toString();
                    String strCheckup_Time = MyArrList.get(position).get("check_up_time")
                            .toString();

                    //viewDetail.setIcon(android.R.drawable.btn_star_big_on);
                    viewDetail.setTitle("Checkup Details");
                    viewDetail.setMessage("Checkup ID : " + strCheckupid + "\n"
                            + "Patient Names : " + strPatientname + "\n"
                            + "Temperature : " + strTemperature + "\n"
                            + "Cough : " + strCough + "\n"
                            + "Chills : " + strChills + "\n"
                            + "New Chest Pain : " + strNew_Chest_Pain+ "\n"
                            + "Headache : " + strHeadache + "\n"
                            + "Difficulty Breathing : " + strDifficulty_Breathing + "\n"
                            + "Fatigue : " + strFatigue + "\n"
                            + "Runny Nose : " + strRunny_Nose + "\n"
                            + "Diarrhea : " + strDiarrhea + "\n"
                            + "Sore Throat : " + strSore_Throat + "\n"
                            + "Checkup Time : " + strCheckup_Time);
                    viewDetail.setPositiveButton("OK",

                            new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface dialog,
                                                    int which) {

// TODO Auto-generated method stub
                                    dialog.dismiss();
                                }
                            });
                    viewDetail.show();
                }

            });

        } catch (JSONException e) {

// TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public String getJSONUrl(String url,List<NameValuePair> params) {
        StringBuilder str = new StringBuilder();
        HttpClient client = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(url);

        try {
            httpPost.setEntity(new UrlEncodedFormEntity(params));
            HttpResponse response = client.execute(httpPost);
            StatusLine statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            if (statusCode == 200) { // Download OK
                HttpEntity entity = response.getEntity();
                InputStream content = entity.getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(content));
                String line;
                while ((line = reader.readLine()) != null) {
                    str.append(line);
                }
            } else {
                Log.e("Log", "Failed to download file..");
            }

        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return str.toString();
    }

   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);

        return true;
    }*/

}