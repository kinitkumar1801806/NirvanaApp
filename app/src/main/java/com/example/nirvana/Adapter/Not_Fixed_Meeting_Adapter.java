package com.example.nirvana.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.nirvana.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Not_Fixed_Meeting_Adapter extends BaseAdapter {
    private Context mContext;
    ArrayList<String> NameList,Username_List,Date_List,Time_List,Image_Array;
    public Not_Fixed_Meeting_Adapter (Context context,ArrayList<String>NameList,ArrayList<String>Username_List,ArrayList<String>Date_List,ArrayList<String>Time_list,ArrayList<String>Image_Array) {
        mContext = context;
        this.NameList=NameList;
        this.Username_List=Username_List;
        this.Time_List=Time_list;
        this.Date_List= Date_List;
        this.Image_Array= Image_Array;
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
            SimpleDateFormat currentDate = new SimpleDateFormat("dd/MM/yyyy");
            Date todayDate = new Date();
            String thisDate = currentDate.format(todayDate);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/M/yyyy");
            String date=Date_List.get(i);
            gridViewAndroid = new View(mContext);
            gridViewAndroid = inflater.inflate(R.layout.not_fixedd_meeting_list_view, null);
            TextView nameView = (TextView) gridViewAndroid.findViewById(R.id.patient_name);
            TextView dateView=(TextView)gridViewAndroid.findViewById(R.id.submission_date);
            TextView timeview=(TextView)gridViewAndroid.findViewById(R.id.submission_time);
            ImageView imageView=(ImageView)gridViewAndroid.findViewById(R.id.patient_image);
            TextView usernameView=(TextView)gridViewAndroid.findViewById(R.id.patient_username);
            nameView.setText(NameList.get(i));
            timeview.setText(Time_List.get(i));
            dateView.setText(Date_List.get(i));
            usernameView.setText(Username_List.get(i));
            if(!Image_Array.get(i).equals("none"))
            {
                Glide.with(mContext).load(Image_Array.get(i)).into(imageView);
            }
        } else {
            gridViewAndroid = (View) convertView;
        }
        return gridViewAndroid;
    }

}
