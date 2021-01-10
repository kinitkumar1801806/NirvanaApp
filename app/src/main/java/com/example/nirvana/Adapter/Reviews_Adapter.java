package com.example.nirvana.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.nirvana.Model.Chat;
import com.example.nirvana.R;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class Reviews_Adapter extends RecyclerView.Adapter<Reviews_Adapter.ViewHolder> {
    Context mContext;
    private final ArrayList<String> PatientNameArray;
    private final ArrayList<String> RatingsArray;
    private final ArrayList<String> ReviewsArray;
    private final ArrayList<String> DateArray;
    private final ArrayList<String> TimeArray;

    public Reviews_Adapter(Context  context,ArrayList<String> patientNameArray, ArrayList<String> ratingsArray, ArrayList<String> reviewsArray, ArrayList<String> dateArray, ArrayList<String> timeArray) {
        this.mContext=context;
        this.PatientNameArray = patientNameArray;
        this.RatingsArray = ratingsArray;
        this.ReviewsArray = reviewsArray;
        this.DateArray = dateArray;
        this.TimeArray = timeArray;
    }

    @NonNull
    @Override
    public Reviews_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.ratings_grid_view, parent, false);
        return new Reviews_Adapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
          holder.Review.setText(ReviewsArray.get(position));
          holder.patient_name.setText(PatientNameArray.get(position));
          holder.date.setText(DateArray.get(position));
          holder.time.setText(TimeArray.get(position));
          int count=Integer.parseInt(RatingsArray.get(position));
          if(count==1)
          {
              holder.rate2.setVisibility(View.GONE);
              holder.rate3.setVisibility(View.GONE);
              holder.rate4.setVisibility(View.GONE);
              holder.rate5.setVisibility(View.GONE);
          }
          else if(count==2)
          {
              holder.rate3.setVisibility(View.GONE);
              holder.rate4.setVisibility(View.GONE);
              holder.rate5.setVisibility(View.GONE);
          }
          else if(count==3)
          {
              holder.rate4.setVisibility(View.GONE);
              holder.rate5.setVisibility(View.GONE);
          }
          else if(count==4)
          {
              holder.rate5.setVisibility(View.GONE);
          }
    }

    @Override
    public int getItemCount() {
        return PatientNameArray.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView Review,date,time,patient_name;
        public ImageView rate1,rate2,rate3,rate4,rate5;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            Review=itemView.findViewById(R.id.review);
            patient_name=itemView.findViewById(R.id.patient_name);
            date=itemView.findViewById(R.id.date);
            time=itemView.findViewById(R.id.time);
            rate1=itemView.findViewById(R.id.rate1);
            rate2=itemView.findViewById(R.id.rate2);
            rate3=itemView.findViewById(R.id.rate3);
            rate4=itemView.findViewById(R.id.rate4);
            rate5=itemView.findViewById(R.id.rate5);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return 1;
    }
}
