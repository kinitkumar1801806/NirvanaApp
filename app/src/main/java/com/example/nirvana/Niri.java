package com.example.nirvana;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import io.kommunicate.KmConversationBuilder;
import io.kommunicate.Kommunicate;
import io.kommunicate.callbacks.KmCallback;

public class Niri extends AppCompatActivity  {
    ProgressDialog progressDialog;
    TextView niri;
    Button refresh;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_niri);
        getSupportActionBar().hide();
        niri=findViewById(R.id.niri);
        refresh=findViewById(R.id.refresh);
        progressDialog =new ProgressDialog(this);
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        Kommunicate.init(this, "2ad9b6ab1af0b883d6c74e141c7044e77");
        new KmConversationBuilder(this)
                .launchConversation(new KmCallback() {
                    @Override
                    public void onSuccess(Object message) {
                        Log.d("Conversation", "Success : " + message);
                        progressDialog.dismiss();
                        finish();
                    }

                    @Override
                    public void onFailure(Object error) {
                        progressDialog.dismiss();
                        Log.d("Conversation", "Failure : " + error);
                        niri.setText("Please check your internet connection");
                        niri.setVisibility(View.VISIBLE);
                        refresh.setVisibility(View.VISIBLE);
                    }
                });

    }


    public void Refresh(View view) {
        Intent intent=new Intent(this,Niri.class);
        startActivity(intent);
    }

}
