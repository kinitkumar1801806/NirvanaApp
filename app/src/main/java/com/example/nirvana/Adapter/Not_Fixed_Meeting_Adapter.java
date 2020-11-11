package com.example.nirvana.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.nirvana.R;
import java.util.ArrayList;

public class Not_Fixed_Meeting_Adapter extends RecyclerView.Adapter<Not_Fixed_Meeting_Adapter.ViewHolder> {
    Context mContext;
    private OnItemClickListener mListener;
    private ArrayList<String> ImageArray, NameList,Username_List,Expand_List,ProblemList,DateList,TimeList;
    public interface OnItemClickListener
    {
        void onItemClick(int position);
        void onExpand(int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener)
    {
        mListener=listener;
    }

    public Not_Fixed_Meeting_Adapter(Context mContext,ArrayList<String>ImageArray,ArrayList<String>NameList,ArrayList<String>Username_List,ArrayList<String>
            Expand_List,ArrayList<String>ProblemList,ArrayList<String>DateList,ArrayList<String>TimeList) {
        this.mContext = mContext;
        this.ImageArray=ImageArray;
        this.NameList=NameList;
        this.Username_List=Username_List;
        this.Expand_List=Expand_List;
        this.DateList=DateList;
        this.TimeList= TimeList;
        this.ProblemList=ProblemList;
    }

    @NonNull
    @Override
    public Not_Fixed_Meeting_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.not_fixedd_meeting_list_view, parent, false);
        return new Not_Fixed_Meeting_Adapter.ViewHolder(view,mListener);
    }


    @Override
    public void onBindViewHolder(@NonNull Not_Fixed_Meeting_Adapter.ViewHolder holder, int position) {
        holder.name.setText(NameList.get(position));
        holder.username.setText(Username_List.get(position));
        Glide.with(mContext).load(ImageArray.get(position)).into(holder.User_image);
        holder.problem.setText(ProblemList.get(position));
        holder.date.setText(DateList.get(position));
        holder.time.setText(TimeList.get(position));
        if(Expand_List.get(position).equals("1"))
        {
            LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,400);
            holder.linearLayout1.setLayoutParams(layoutParams);
            holder.Expand_btn.setImageResource(R.drawable.ic_baseline_keyboard_arrow_up_24);

        }
        else
        {
            LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,275);
            holder.linearLayout1.setLayoutParams(layoutParams);
            holder.Expand_btn.setImageResource(R.drawable.ic_baseline_expand_more_24);

        }
    }

    @Override
    public int getItemCount() {
        return NameList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView name,username,problem,date,time;
        public ImageView User_image,Expand_btn;
        public LinearLayout linearLayout,linearLayout1;
        public ViewHolder(@NonNull View itemView,OnItemClickListener listener) {
            super(itemView);
            name=itemView.findViewById(R.id.patient_name);
            username=itemView.findViewById(R.id.patient_username);
            User_image=itemView.findViewById(R.id.patient_image);
            Expand_btn=itemView.findViewById(R.id.show_more);
            linearLayout=itemView.findViewById(R.id.show_head_layout);
            linearLayout1=itemView.findViewById(R.id.linearLayout1);
            problem=itemView.findViewById(R.id.show_problem);
            date=itemView.findViewById(R.id.submission_date);
            time=itemView.findViewById(R.id.submission_time);
            itemView.setOnClickListener(new View.OnClickListener() {
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
            Expand_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener!=null)
                    {
                        int position=getAdapterPosition();
                        if(position!=RecyclerView.NO_POSITION)
                        {
                            listener.onExpand(position);
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
