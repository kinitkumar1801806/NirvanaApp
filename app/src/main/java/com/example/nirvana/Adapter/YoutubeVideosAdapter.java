package com.example.nirvana.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.nirvana.R;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class YoutubeVideosAdapter extends RecyclerView.Adapter<YoutubeVideosAdapter.ViewHolder> {
    Context mContext;
    String videoId;
    private  OnItemClickListener mListener;
    private final ArrayList<String> VideosList;
    private final ArrayList<String> TitleList;
    private final ArrayList<String> ThumbnailList;
    public interface OnItemClickListener
    {
        void onItemClick(int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener)
    {
        mListener=listener;
    }

    public YoutubeVideosAdapter(Context mContext,ArrayList<String> VideosList,ArrayList<String> TitleList,ArrayList<String> ThumbnailList) {
        this.mContext = mContext;
        this.VideosList=VideosList;
        this.TitleList=TitleList;
        this.ThumbnailList=ThumbnailList;
    }

    @NonNull
    @Override
    public YoutubeVideosAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.youtube_video_list, parent, false);
        return new YoutubeVideosAdapter.ViewHolder(view,mListener);
    }


    @Override
    public void onBindViewHolder(@NonNull YoutubeVideosAdapter.ViewHolder holder, int position) {
     holder.video_title.setText(TitleList.get(TitleList.size()-position-1));
     String img_url=ThumbnailList.get(ThumbnailList.size()-position-1);
     Glide.with(mContext).load(img_url)
            .placeholder(R.drawable.signup_bg)
            .into(holder.video_image);
     System.out.println(img_url);
    }

    @Override
    public int getItemCount() {
        return VideosList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView video_title;
        private final ImageView video_image;
        public ViewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);
            video_image = itemView.findViewById(R.id.video_image);
            video_title=itemView.findViewById(R.id.video_title);
            video_image.setOnClickListener(new View.OnClickListener() {
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
