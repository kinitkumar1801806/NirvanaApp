package com.example.nirvana;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.jar.Attributes;

public class BlogsAdapter extends BaseAdapter {
    private Context mContext;
    ArrayList<String> TitleList;
    ArrayList<String> Like_List,Comment_List,head_List,Date_List,Time_List;

    public BlogsAdapter (Context context, ArrayList<String> Title_List,ArrayList<String> Head_List,ArrayList<String>Like_List
    ,ArrayList<String>Comment_List,ArrayList<String>Date_List,ArrayList<String>Time_List) {
        mContext = context;
        this.TitleList=Title_List;
        this.head_List=Head_List;
        this.Like_List=Like_List;
        this.Comment_List=Comment_List;
        this.Date_List=Date_List;
        this.Time_List=Time_List;
    }

    @Override
    public int getCount() {
        return TitleList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        View gridViewAndroid;
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {

            gridViewAndroid = new View(mContext);
            gridViewAndroid = inflater.inflate(R.layout.blog_lists, null);
            TextView titleView = (TextView) gridViewAndroid.findViewById(R.id.blog_title);
            TextView likeView=(TextView)gridViewAndroid.findViewById(R.id.like_no);
            TextView CommentView = (TextView) gridViewAndroid.findViewById(R.id.comment_no);
            TextView DateView=(TextView)gridViewAndroid.findViewById(R.id.submission_date);
            TextView TimeView=(TextView)gridViewAndroid.findViewById(R.id.submission_time);
            titleView.setText(TitleList.get(i));
            likeView.setText(Like_List.get(i));
            CommentView.setText(Comment_List.get(i));
            DateView.setText(Date_List.get(i));
            TimeView.setText(Time_List.get(i));
        } else {
            gridViewAndroid = (View) convertView;
        }

        return gridViewAndroid;
    }

}
