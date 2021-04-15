package com.example.HBC;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ContactUsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);

        // make reference of widgets from XML TO Java
        Button btnsocial = findViewById(R.id.buttonsocialmediahandle);
        Button btnfacebook = findViewById(R.id.buttonFacebook);
        Button btntwitter = findViewById(R.id.buttonTwitter);
        Button btninstagram = findViewById(R.id.buttonInstagran);
        Button btnphone = findViewById(R.id.buttonPhone);
       //Button btnlocator = findViewById(R.id.buttonIDIlocation);

        btnsocial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(ContactsActivity.this, "social button pressed", Toast.LENGTH_SHORT).show();
                Intent Emailintent = new Intent(Intent.ACTION_SEND);
                Emailintent.setType("text/html");
                Emailintent.putExtra(Intent.EXTRA_EMAIL, "kaluumahillary@idi.co.ug" +
                        "" +
                        "" +
                        "    ");
                Emailintent.putExtra(Intent.EXTRA_SUBJECT, "Subject");
                //  Emailintent.putExtra(Intent.EXTRA_TEXT, "I'm email body.");

                startActivity(Intent.createChooser(Emailintent, "Send Email"));

            }
        });

        btnfacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent Facebookintent = new Intent();
                //startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://m.facebook.com")));
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com")));
            }
        });

        btntwitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent twitterintent = new Intent();
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.twitter.com")));
            }
        });


        btninstagram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent instagramintent = new Intent();
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com")));
            }
        });

        btnphone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(ContactsActivity.this, "Phone button pressed", Toast.LENGTH_SHORT).show();
                Intent Phoneintent = new Intent();
                Phoneintent.setAction(Intent.ACTION_DIAL);
                Phoneintent.setData(Uri.parse("tel:+256726046629"));
                startActivity(Phoneintent);
            }
        });

       /* btnlocator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(ContactsActivity.this, "Location button pressed", Toast.LENGTH_SHORT).show();
                //String uri = "https://www.google.com/maps/@-3.3994739,36.7968178,15z/data=!4m5!3m4!1s0x0:0x704668ef05d49867!8m2!3d-3.3994739!4d36.7968178\n";
                String uri = "https://www.google.com/maps/d/u/0/viewer?mid=1dJjQ4jCUPR89umU3AIBPn59R9Pqp77Ms&ll=0.40092934615056425%2C32.57792117264799&z=12\n";
                Intent locintent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                startActivity(locintent);

            }
        }); */


    }
}