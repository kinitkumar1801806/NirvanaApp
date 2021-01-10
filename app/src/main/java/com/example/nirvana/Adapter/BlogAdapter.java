package com.example.nirvana.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
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

public class BlogAdapter extends RecyclerView.Adapter<BlogAdapter.ViewHolder> {
    Context mContext;
    private OnItemClickListener mListener;
    private final ArrayList<String>like_list;
    private final ArrayList<String> like_count;
    private final ArrayList<String> Doctor_list;
    private final ArrayList<String> body_list;
    private final ArrayList<String> title_list;
    private final ArrayList<String> time_list;
    private final ArrayList<String> date_list;
    private final ArrayList<String> Link_list;
    private final ArrayList<String> Doctor_type;
    public interface OnItemClickListener
    {
        void onItemClick(int position);
        void onLikeChange(int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener)
    {
        mListener=listener;
    }

    public BlogAdapter(Context mContext, ArrayList<String>like_list, ArrayList<String>like_count, ArrayList<String>Doctor_list, ArrayList<String>Link_list
            , ArrayList<String>body_list,ArrayList<String>title_list,ArrayList<String>time_list,ArrayList<String>date_list,ArrayList<String> Doctor_type) {
        this.mContext = mContext;
        this.like_list=like_list;
        this.like_count=like_count;
        this.Doctor_list=Doctor_list;
        this.body_list=body_list;
        this.title_list=title_list;
        this.date_list=date_list;
        this.time_list=time_list;
        this.Link_list=Link_list;
        this.Doctor_type=Doctor_type;
    }

    @NonNull
    @Override
    public BlogAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.blog_lists, parent, false);
        return new BlogAdapter.ViewHolder(view,mListener);
    }


    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull BlogAdapter.ViewHolder holder, int position) {
        Glide.with(mContext).load(Link_list.get(position)).into(holder.doctor_image);
        holder.d_name.setText(Doctor_list.get(position));
        holder.d_type.setText(Doctor_type.get(position));
        holder.BlogTitle.setText(title_list.get(position));
        holder.body.setText(body_list.get(position));
        holder.Like.setText(like_count.get(position));
        holder.Date.setText(date_list.get(position));
        holder.Time.setText(time_list.get(position));
        if(like_list.get(position).equals("1"))
        {
            holder.like_image.setImageResource(R.drawable.green_like);
            holder.Like.setTextColor(Color.parseColor("#339966"));
        }
    }

    @Override
    public int getItemCount() {
        return body_list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView BlogTitle,Like,Date,Time,body,d_name,d_type,ReadMore;
        public ImageView doctor_image,like_image;
        public ViewHolder(@NonNull View itemView,OnItemClickListener listener) {
            super(itemView);
            doctor_image=itemView.findViewById(R.id.doctor_image);
            d_name=itemView.findViewById(R.id.doctor_name);
            d_type=itemView.findViewById(R.id.doctor_type);
            BlogTitle=itemView.findViewById(R.id.blog_title);
            body=itemView.findViewById(R.id.blog_body);
            like_image=itemView.findViewById(R.id.likes);
            Like=itemView.findViewById(R.id.likes_count);
            Date=itemView.findViewById(R.id.date);
            Time=itemView.findViewById(R.id.time);
            ReadMore=itemView.findViewById(R.id.ReadMore);
            ReadMore.setOnClickListener(new View.OnClickListener() {
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
            like_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener!=null)
                    {
                        int position=getAdapterPosition();
                        if(position!=RecyclerView.NO_POSITION)
                        {
                            listener.onLikeChange(position);
                        }
                    }
                }
            });
        }
    }

    @Override
    public long getItemId(int position) {
        return  position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}
