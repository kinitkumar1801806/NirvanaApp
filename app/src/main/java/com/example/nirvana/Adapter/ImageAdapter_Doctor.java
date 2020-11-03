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

public class ImageAdapter_Doctor extends RecyclerView.Adapter<ImageAdapter_Doctor.ViewHolder> {
    Context mContext;
    private  OnItemClickListener mListener;
    private ArrayList<String> ImageArray, NameList,Username_List,Expand_List,BioList;
    public interface OnItemClickListener
    {
        void onItemClick(int position);
        void onExpand(int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener)
    {
        mListener=listener;
    }

    public ImageAdapter_Doctor(Context mContext,ArrayList<String>ImageArray,ArrayList<String>NameList,ArrayList<String>Username_List,ArrayList<String>
                               Expand_List,ArrayList<String>BioList) {
        this.mContext = mContext;
        this.ImageArray=ImageArray;
        this.NameList=NameList;
        this.Username_List=Username_List;
        this.Expand_List=Expand_List;
        this.BioList=BioList;
    }

    @NonNull
    @Override
    public ImageAdapter_Doctor.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.doctor_list_view, parent, false);
        return new ImageAdapter_Doctor.ViewHolder(view,mListener);
    }


    @Override
    public void onBindViewHolder(@NonNull ImageAdapter_Doctor.ViewHolder holder, int position) {
        holder.name.setText(NameList.get(position));
        holder.username.setText(Username_List.get(position));
        Glide.with(mContext).load(ImageArray.get(position)).into(holder.User_image);
        holder.bio.setText(BioList.get(position));
        if(Expand_List.get(position).equals("1"))
        {
            LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,430);
            holder.linearLayout1.setLayoutParams(layoutParams);
            holder.Expand_btn.setImageResource(R.drawable.ic_baseline_keyboard_arrow_up_24);

        }
        else
        {
            LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,260);
            holder.linearLayout1.setLayoutParams(layoutParams);
            holder.Expand_btn.setImageResource(R.drawable.ic_baseline_expand_more_24);

        }
    }

    @Override
    public int getItemCount() {
        return NameList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView name,username,bio;
        public ImageView User_image,Expand_btn;
        public LinearLayout linearLayout,linearLayout1;
        public ViewHolder(@NonNull View itemView,OnItemClickListener listener) {
            super(itemView);
            name=itemView.findViewById(R.id.doctor_name);
            username=itemView.findViewById(R.id.doctor_username);
            User_image=itemView.findViewById(R.id.doctor_image);
            Expand_btn=itemView.findViewById(R.id.show_more);
            linearLayout=itemView.findViewById(R.id.show_head_layout);
            linearLayout1=itemView.findViewById(R.id.linearLayout1);
            bio=itemView.findViewById(R.id.show_bio);
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
