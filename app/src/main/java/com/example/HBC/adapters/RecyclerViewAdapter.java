package com.example.HBC.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
//import com.example.HBC.activities.AnimeActivity;
import com.example.HBC.activites.AnimeActivity;
import com.example.HBC.model.Anime;
import com.example.HBC.R;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {
    private Context mContext;
    private List<Anime> mData;
    RequestOptions option;

    public RecyclerViewAdapter(Context mContext, List<Anime> mData) {
        this.mContext = mContext;
        this.mData = mData;
        //Request option for Glide
        option = new RequestOptions().centerCrop().placeholder(R.drawable.loading_shape).error(R.drawable.loading_shape);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        view = inflater.inflate(R.layout.anime_row_item,parent,false);
        MyViewHolder viewHolder = new MyViewHolder(view);
        viewHolder.view_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mContext, AnimeActivity.class);
                i.putExtra("anime_fullnames", mData.get(viewHolder.getAdapterPosition()).getFullnames());
                i.putExtra("anime_comments", mData.get(viewHolder.getAdapterPosition()).getComments());
                i.putExtra("anime_patient_id", mData.get(viewHolder.getAdapterPosition()).getPatient_id());
                i.putExtra("anime_test_status", mData.get(viewHolder.getAdapterPosition()).getTest_status());
                i.putExtra("anime_location", mData.get(viewHolder.getAdapterPosition()).getLocation());
                i.putExtra("anime_age", mData.get(viewHolder.getAdapterPosition()).getAge());
                i.putExtra("anime_phonenumber", mData.get(viewHolder.getAdapterPosition()).getPhonenumber());
                i.putExtra("anime_disease", mData.get(viewHolder.getAdapterPosition()).getDisease());
                i.putExtra("anime_img", mData.get(viewHolder.getAdapterPosition()).getImage_url());
                mContext.startActivity(i);

            }
        });

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.tv_fullname.setText(mData.get(position).getFullnames());
        //holder.tv_comments.setText(mData.get(position).getComments());
        //holder.tv_patient_id.setText(mData.get(position).getPatient_id());
        holder.tv_test_status.setText(mData.get(position).getTest_status());
        holder.tv_location.setText(mData.get(position).getLocation());
        holder.tv_age.setText(mData.get(position).getAge());
        holder.tv_phonenumber.setText(mData.get(position).getPhonenumber());
        holder.tv_disease.setText(mData.get(position).getDisease());

        //load image from teh internet and set it into imageview using Glide
        Glide.with(mContext).load(mData.get(position).getImage_url()).apply(option).into(holder.img_thumbnail);

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public  static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView tv_fullname;
        //TextView tv_comments;
        //TextView tv_patient_id;
        TextView tv_test_status;
        TextView tv_location;
        TextView tv_age;
        TextView tv_phonenumber;
        TextView tv_disease;
        ImageView img_thumbnail;
        LinearLayout view_container;



        public MyViewHolder (View itemView){
            super(itemView);
            view_container = itemView.findViewById(R.id.container);
            tv_fullname = itemView.findViewById(R.id.patient_fullname);
            tv_age = itemView.findViewById(R.id.patient_age);
            tv_test_status = itemView.findViewById(R.id.test_status);
            tv_location = itemView.findViewById(R.id.location);
            tv_phonenumber = itemView.findViewById(R.id.phonenumber);
            tv_disease = itemView.findViewById(R.id.disease);
            img_thumbnail = itemView.findViewById(R.id.thumbnail);
        }
    }
}
