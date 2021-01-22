package com.example.nirvana.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
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
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

public class History_Patient_Meetings_Adapter extends RecyclerView.Adapter<History_Patient_Meetings_Adapter.ViewHolder> {
    Context mContext;
    private  OnItemClickListener mListener;
    private final ArrayList<String> NameList,TypeList,DateList,TimeList,LinkList;
    public interface OnItemClickListener
    {
        void onItemClick(int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener)
    {
        mListener=listener;
    }

    public History_Patient_Meetings_Adapter(Context mContext,ArrayList<String>NameList,ArrayList<String>TypeList,ArrayList<String>DateList,ArrayList<String>
            TimeList,ArrayList<String>LinkList) {
        this.mContext = mContext;
        this.NameList=NameList;
        this.TypeList=TypeList;
        this.DateList=DateList;
        this.TimeList=TimeList;
        this.LinkList=LinkList;
    }

    @NonNull
    @Override
    public History_Patient_Meetings_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.history_patientlists, parent, false);
        return new History_Patient_Meetings_Adapter.ViewHolder(view,mListener);
    }


    @Override
    public void onBindViewHolder(@NonNull History_Patient_Meetings_Adapter.ViewHolder holder, int position) {
        holder.name_view.setText(NameList.get(position));
        holder.type_view.setText(TypeList.get(position));
        holder.date_time_view.setText(DateList.get(position)+" "+TimeList.get(position));
        Glide.with(mContext).load(LinkList.get(position)).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return NameList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView name_view,type_view,date_time_view;
        Button Details;
        public ViewHolder(@NonNull View itemView,OnItemClickListener listener) {
            super(itemView);
            imageView=itemView.findViewById(R.id.doctor_image);
            name_view=itemView.findViewById(R.id.doctor_name);
            type_view=itemView.findViewById(R.id.doctor_degree);
            date_time_view=itemView.findViewById(R.id.date_time);
            Details=itemView.findViewById(R.id.details);
            Details.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener!=null)
                    {
                        int position=getAdapterPosition();
                        if(position!=RecyclerView.NO_POSITION)
                        {
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        return 1;
    }
}
