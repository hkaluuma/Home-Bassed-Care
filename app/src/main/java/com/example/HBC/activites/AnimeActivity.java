package com.example.HBC.activites;

        import androidx.appcompat.app.AppCompatActivity;

        import android.os.Bundle;
        import android.widget.ImageView;
        import android.widget.TextView;

        import com.bumptech.glide.Glide;
        import com.bumptech.glide.request.RequestOptions;
        import com.example.HBC.R;
        import com.google.android.material.appbar.CollapsingToolbarLayout;

public class AnimeActivity extends AppCompatActivity {


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anime);
        //hide the default actionbar
        getSupportActionBar().hide();

        //Receieve data
        String fullnames = getIntent().getExtras().getString("anime_fullnames");
        String comments = getIntent().getExtras().getString("anime_comments");
        String patient_id = getIntent().getExtras().getString("anime_patient_id");
        String test_status = getIntent().getExtras().getString("anime_test_status");
        String location = getIntent().getExtras().getString("anime_location");
        String age = getIntent().getExtras().getString("anime_age");
        String phonenumber = getIntent().getExtras().getString("anime_phonenumber");
        String disease = getIntent().getExtras().getString("anime_disease");
        String image_url = getIntent().getExtras().getString("anime_img");

        CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.collapsingtoobar_id);
        collapsingToolbarLayout.setTitleEnabled(true);

        TextView tv_comments = findViewById(R.id.aa_comments);
        TextView tv_patient_id = findViewById(R.id.aa_patient_id);
        TextView tv_test_status = findViewById(R.id.aa_test_status);
        TextView tv_location = findViewById(R.id.aa_location);
        TextView tv_age = findViewById(R.id.aa_age);
        TextView tv_phonenumber = findViewById(R.id.aa_phonenumber);
        TextView tv_disease = findViewById(R.id.aa_disease);
        ImageView img = findViewById(R.id.aa_thumbnail);

        //setting values to each view
        tv_comments.setText(comments);
        tv_patient_id.setText(patient_id);
        tv_test_status.setText(test_status);
        tv_location.setText(location);
        tv_age.setText(age);
        tv_phonenumber.setText(phonenumber);
        tv_disease.setText(disease);

        collapsingToolbarLayout.setTitle(fullnames);
        RequestOptions requestOptions = new RequestOptions().centerCrop().placeholder(R.drawable.loading_shape).error(R.drawable.loading_shape);


        //setting image using glide
        Glide.with(this).load(image_url).apply(requestOptions).into(img);




    }
}