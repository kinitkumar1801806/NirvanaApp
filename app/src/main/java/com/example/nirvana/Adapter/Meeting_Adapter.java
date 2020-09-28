package com.example.nirvana.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.nirvana.R;

import org.w3c.dom.Text;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Meeting_Adapter extends BaseAdapter {
    private Context mContext;
    ArrayList<String> NameList,Username_List;
    ArrayList<String> ImageArray;
    ArrayList<String> Date_List,Time_List;
    public Meeting_Adapter (Context context, ArrayList<String> ImageArray,ArrayList<String>NameList,ArrayList<String>Username_List,ArrayList<String> Date_List,ArrayList<String>Time_List) {
        mContext = context;
        this.ImageArray=ImageArray;
        this.NameList=NameList;
        this.Username_List=Username_List;
        this.Time_List= Time_List;
        this.Date_List= Date_List;
    }

    @Override
    public int getCount() {
        return NameList.size();
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
            gridViewAndroid = inflater.inflate(R.layout.meeting_already_fixed_listview, null);
            TextView nameView = (TextView) gridViewAndroid.findViewById(R.id.doctor_name);
            TextView UsernameView=(TextView)gridViewAndroid.findViewById(R.id.doctor_username);
            ImageView imageView = (ImageView) gridViewAndroid.findViewById(R.id.doctor_image);
            TextView dateView=(TextView)gridViewAndroid.findViewById(R.id.submission_date);
            TextView timeView=(TextView)gridViewAndroid.findViewById(R.id.submission_time);
            nameView.setText(NameList.get(i));
            dateView.setText(Date_List.get(i));
            timeView.setText(Time_List.get(i));
            UsernameView.setText(Username_List.get(i));
            Glide.with(mContext).load(ImageArray.get(i)).into(imageView);
        } else {
            gridViewAndroid = (View) convertView;
        }

        return gridViewAndroid;
    }


}
