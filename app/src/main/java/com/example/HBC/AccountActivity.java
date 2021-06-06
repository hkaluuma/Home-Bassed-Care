package com.example.HBC;

import android.app.ActionBar;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.muddzdev.styleabletoast.StyleableToast;

public class AccountActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        TextView textViewname = findViewById(R.id.textViewname);
        TextView textViewphone = findViewById(R.id.textViewphone);
        TextView textViewusername = findViewById(R.id.textViewusername);
        TextView textViewemail = findViewById(R.id.textViewemail);
        TextView textViewlocation = findViewById(R.id.textViewlocation);
        Button buttonlogout = findViewById(R.id.buttonlogout);


        SharedPreferences sharedpreferences = getSharedPreferences(LoginActivity.MYPREFERENCES_LOGIN, Context.MODE_PRIVATE);
        String username = sharedpreferences.getString("username", null);
        String phonenumber = sharedpreferences.getString("phonenumber", null);
        String email = sharedpreferences.getString("email", null);
        String fullname = sharedpreferences.getString("fullname", null);
        String location = sharedpreferences.getString("location", null);
        String id = sharedpreferences.getString("id", null);

        textViewname.setText(fullname);
        textViewphone.setText(phonenumber);
        textViewemail.setText(email);
        textViewlocation.setText(location);
        textViewusername.setText(username);

        buttonlogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog rDialog = new Dialog(AccountActivity.this);
                rDialog.requestWindowFeature(Window.FEATURE_NO_TITLE); //before
                rDialog.setContentView(R.layout.layout_dialog);
                rDialog.getWindow().setLayout(android.app.ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT);
                TextView textviewCancel = rDialog.findViewById(R.id.textviewCancel);
                TextView textviewLogout = rDialog.findViewById(R.id.textviewOKAY);
                textviewCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        rDialog.dismiss();
                    }
                });
                textviewLogout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        rDialog.dismiss();
                        SharedPreferences sharedpreferences = getSharedPreferences(LoginActivity.MYPREFERENCES_LOGIN, Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.clear();
                        editor.commit();
                        moveTaskToBack(true);
                        finish();
                        //Toasty.warning(getApplicationContext(), "User Logout", Toast.LENGTH_LONG, true).show();
                        StyleableToast.makeText(getApplicationContext(), "Account logout Successfull", R.style.exampleToast).show();
                        //StyleableToast.makeText(LoginActivity.this, "No internet Connection", R.style.exampleToast).show();
                        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                    }
                });
                rDialog.show();
            }
        });

    }
}