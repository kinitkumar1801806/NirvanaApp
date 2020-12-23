package com.example.nirvana.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.nirvana.Model.Chat;
import com.example.nirvana.R;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {
    private final Context mContext;
    public static final int MSG_TYPE_LEFT=0;
    public static final int MSG_TYPE_RIGHT=1;
    public static final int MSG_TYPE_IMAGE_LEFT=2;
    public static final int MSG_TYPE_IMAGE_RIGHT=3;
    private final List<Chat> mChat;
    private final String imageurl;
    private final String sender;
    private final String reciever;
    public MessageAdapter(Context mContext, List<Chat> mChat, String imageurl,String sender,String reciever) {
        this.mContext = mContext;
        this.mChat = mChat;
        this.imageurl = imageurl;
        this.sender=sender;
        this.reciever=reciever;
    }

    @NonNull
    @Override
    public MessageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType== MSG_TYPE_RIGHT) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_right, parent, false);
            return new MessageAdapter.ViewHolder(view);
        }
        else if(viewType==MSG_TYPE_LEFT)
        {
            View view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_left, parent, false);
            return new MessageAdapter.ViewHolder(view);
        }
        else if(viewType==MSG_TYPE_IMAGE_RIGHT)
        {
            View view = LayoutInflater.from(mContext).inflate(R.layout.chat_image_right, parent, false);
            return new MessageAdapter.ViewHolder(view);
        }
        else
        {
            View view = LayoutInflater.from(mContext).inflate(R.layout.chat_image_left, parent, false);
            return new MessageAdapter.ViewHolder(view);
        }
    }


    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.ViewHolder holder, int position) {
     Chat chat=mChat.get(position);
     if(chat.getType().equals("message"))
         holder.show_message.setText(chat.getMessage());
     else
     {
         Glide.with(mContext).load(chat.getMessage()).into(holder.image);
     }
     if(imageurl.equals("None"))
     {
         holder.profile_image.setImageResource(R.drawable.person_green);
     }
     else
     {
         Glide.with(mContext).load(imageurl).into(holder.profile_image);
     }
    }

    @Override
    public int getItemCount() {
        return mChat.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView show_message;
        public ImageView profile_image,image;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            show_message=itemView.findViewById(R.id.show_message);
            profile_image=itemView.findViewById(R.id.profile_image);
            image=itemView.findViewById(R.id.image);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(mChat.get(position).getSender().equals(sender))
        {
            if(mChat.get(position).getType().equals("message"))
            return MSG_TYPE_RIGHT;
            else
                return MSG_TYPE_IMAGE_RIGHT;
        }else
        {
            if(mChat.get(position).getType().equals("message"))
                return MSG_TYPE_LEFT;
            else
                return MSG_TYPE_IMAGE_LEFT;
        }
    }
}
