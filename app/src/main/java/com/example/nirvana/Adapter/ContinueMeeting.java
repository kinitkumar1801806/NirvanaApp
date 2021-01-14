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
import java.util.ArrayList;
import java.util.List;

public class ContinueMeeting extends RecyclerView.Adapter<ContinueMeeting.ViewHolder> {
    Context mContext;
    private  OnItemClickListener mListener;
    private final ArrayList<String> Patient_Name;
    private final ArrayList<String> Problem;
    public interface OnItemClickListener
    {
        void onItemClick(int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener)
    {
        mListener=listener;
    }

    public ContinueMeeting(Context mContext,ArrayList<String>Patient_Name,ArrayList<String>Problem) {
        this.mContext = mContext;
        this.Patient_Name=Patient_Name;
        this.Problem=Problem;
    }

    @NonNull
    @Override
    public ContinueMeeting.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.continueas_lists, parent, false);
        return new ContinueMeeting.ViewHolder(view,mListener);
    }


    @Override
    public void onBindViewHolder(@NonNull ContinueMeeting.ViewHolder holder, int position) {
        holder.name.setText(Patient_Name.get(position));
        holder.problem.setText(Problem.get(position));
    }

    @Override
    public int getItemCount() {
        return Problem.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView name,problem;
        public Button Continue;
        public ViewHolder(@NonNull View itemView,OnItemClickListener listener) {
            super(itemView);
            name=itemView.findViewById(R.id.name);
            problem=itemView.findViewById(R.id.problem);
            Continue=itemView.findViewById(R.id.button);
            Continue.setOnClickListener(new View.OnClickListener() {
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
