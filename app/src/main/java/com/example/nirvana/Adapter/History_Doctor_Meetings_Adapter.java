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

public class History_Doctor_Meetings_Adapter extends RecyclerView.Adapter<History_Doctor_Meetings_Adapter.ViewHolder> {
    Context mContext;
    private  OnItemClickListener mListener;
    private final ArrayList<String> NameList,ProblemList,DateList,TimeList,LinkList;
    public interface OnItemClickListener
    {
        void onItemClick(int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener)
    {
        mListener=listener;
    }

    public History_Doctor_Meetings_Adapter(Context mContext,ArrayList<String>NameList,ArrayList<String>ProblemList,ArrayList<String>DateList,ArrayList<String>
            TimeList,ArrayList<String>LinkList) {
        this.mContext = mContext;
        this.NameList=NameList;
        this.ProblemList=ProblemList;
        this.DateList=DateList;
        this.TimeList=TimeList;
        this.LinkList=LinkList;
    }

    @NonNull
    @Override
    public History_Doctor_Meetings_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.history_doctorlist, parent, false);
        return new History_Doctor_Meetings_Adapter.ViewHolder(view,mListener);
    }


    @Override
    public void onBindViewHolder(@NonNull History_Doctor_Meetings_Adapter.ViewHolder holder, int position) {
        holder.name_view.setText(NameList.get(position));
        holder.problem_view.setText(ProblemList.get(position));
        holder.date_view.setText(DateList.get(position));
        holder.time_view.setText(TimeList.get(position));
        if(!LinkList.get(position).equals("None"))
        {
            Glide.with(mContext).load(LinkList.get(position)).into(holder.imageView);
        }
    }

    @Override
    public int getItemCount() {
        return NameList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView name_view,problem_view,date_view,time_view;
        Button details;
        public ViewHolder(@NonNull View itemView,OnItemClickListener listener) {
            super(itemView);
            imageView=itemView.findViewById(R.id.patient);
            name_view=itemView.findViewById(R.id.patient_name);
            problem_view=itemView.findViewById(R.id.patient_problem);
            date_view=itemView.findViewById(R.id.date);
            time_view=itemView.findViewById(R.id.time);
            details=itemView.findViewById(R.id.details);
            details.setOnClickListener(new View.OnClickListener() {
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
