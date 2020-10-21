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

public class BlogAdapter extends RecyclerView.Adapter<BlogAdapter.ViewHolder> {
    Context mContext;
    private OnItemClickListener mListener;
    private ArrayList<String>like_list,comment_list,body_list,title_list,head_list,time_list,date_list,Expand_List;
    public interface OnItemClickListener
    {
        void onItemClick(int position);
        void onExpand(int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener)
    {
        mListener=listener;
    }

    public BlogAdapter(Context mContext, ArrayList<String>like_list, ArrayList<String>comment_list, ArrayList<String>body_list,
                       ArrayList<String>title_list,ArrayList<String>head_list,ArrayList<String>time_list,ArrayList<String>date_list,
                       ArrayList<String>Expand_List) {
        this.mContext = mContext;
        this.like_list=like_list;
        this.comment_list=comment_list;
        this.body_list=body_list;
        this.title_list=title_list;
        this.head_list=head_list;
        this.date_list=date_list;
        this.time_list=time_list;
        this.Expand_List=Expand_List;
    }

    @NonNull
    @Override
    public BlogAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.blog_lists, parent, false);
        return new BlogAdapter.ViewHolder(view,mListener);
    }


    @Override
    public void onBindViewHolder(@NonNull BlogAdapter.ViewHolder holder, int position) {
        holder.BlogTitle.setText(title_list.get(position));
        holder.Like.setText(like_list.get(position));
        holder.Comment.setText(comment_list.get(position));
        holder.Submission_Time.setText(time_list.get(position));
        holder.Submission_Date.setText(date_list.get(position));
        holder.Head.setText(head_list.get(position));
        if(Expand_List.get(position).equals("1"))
        {
           LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,525);
           holder.linearLayout1.setLayoutParams(layoutParams);
           holder.Expand_btn.setImageResource(R.drawable.ic_baseline_keyboard_arrow_up_24);

        }
        else
        {
            LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,250);
            holder.linearLayout1.setLayoutParams(layoutParams);
            holder.Expand_btn.setImageResource(R.drawable.ic_baseline_expand_more_24);

        }
    }

    @Override
    public int getItemCount() {
        return body_list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView BlogTitle,Like,Comment,Submission_Date,Submission_Time,Head;
        public ImageView Like_btn,Expand_btn;
        public LinearLayout linearLayout,linearLayout1;
        public ViewHolder(@NonNull View itemView,OnItemClickListener listener) {
            super(itemView);
            BlogTitle=itemView.findViewById(R.id.blog_title);
            Like=itemView.findViewById(R.id.like_no);
            Comment=itemView.findViewById(R.id.comment_no);
            Submission_Date=itemView.findViewById(R.id.submission_date);
            Submission_Time=itemView.findViewById(R.id.submission_time);
            Head=itemView.findViewById(R.id.show_head);
            Like_btn=itemView.findViewById(R.id.like_btn);
            Expand_btn=itemView.findViewById(R.id.show_more);
            linearLayout=itemView.findViewById(R.id.show_head_layout);
            linearLayout1=itemView.findViewById(R.id.linearLayout1);
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
