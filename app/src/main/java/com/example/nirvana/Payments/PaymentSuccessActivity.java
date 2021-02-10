package com.example.nirvana.Payments;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nirvana.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class PaymentSuccessActivity extends AppCompatActivity {
    TextView dateofPayment,paymentAmount,doctorname,uniqueid,bookdate,BookTime;
    Button download;
    private LinearLayout llPdf;
    private Bitmap bitmap;
    ArrayList<String> arr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_success);
        dateofPayment=findViewById(R.id.dateOfPayment);
        paymentAmount=findViewById(R.id.paymentAmount);
        doctorname=findViewById(R.id.doctorname);
        uniqueid=findViewById(R.id.uniqueid);
        bookdate=findViewById(R.id.bookdate);
        BookTime=findViewById(R.id.BookTime);
        Intent intent=getIntent();
        arr=intent.getStringArrayListExtra("arr");
        dateofPayment.setText(arr.get(0));
        bookdate.setText(arr.get(1));
        BookTime.setText(arr.get(2));
        paymentAmount.setText("INR "+arr.get(3));
        doctorname.setText(arr.get(4));
        download=findViewById(R.id.download);
        download.setVisibility(View.VISIBLE);
        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                download.setVisibility(View.GONE);
                llPdf=findViewById(R.id.linearLayout);
                llPdf.setBackgroundColor(Color.parseColor("#FFFFFF"));
                bitmap = loadBitmapFromView(llPdf, llPdf.getWidth(), llPdf.getHeight());
                createPdf();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.no_animation,R.anim.slide_in_bottom);
    }
    public static Bitmap loadBitmapFromView(View v, int width, int height) {
        Bitmap b = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        v.draw(c);

        return b;
    }

    private void createPdf(){
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        //  Display display = wm.getDefaultDisplay();
        DisplayMetrics displaymetrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        float hight = displaymetrics.heightPixels ;
        float width = displaymetrics.widthPixels ;

        int convertHighet = (int) hight, convertWidth = (int) width;

//        Resources mResources = getResources();
//        Bitmap bitmap = BitmapFactory.decodeResource(mResources, R.drawable.screenshot);

        PdfDocument document = new PdfDocument();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(convertWidth, convertHighet, 1).create();
        PdfDocument.Page page = document.startPage(pageInfo);

        Canvas canvas = page.getCanvas();

        Paint paint = new Paint();
        canvas.drawPaint(paint);

        bitmap = Bitmap.createScaledBitmap(bitmap, convertWidth, convertHighet, true);

        paint.setColor(Color.BLUE);
        canvas.drawBitmap(bitmap, 0, 0 , null);
        document.finishPage(page);

        // write the document content
        File direct = new File(Environment.getExternalStorageDirectory() + "/Nirvana/");

        if(!direct.exists()) {
            direct.mkdirs(); //directory is created;
        }
        File filePath;
        String name="/payment.pdf";
        filePath = new File(direct,name);
        try {
            if (!filePath.exists()) {
                if (!filePath.createNewFile()) {
                    Toast.makeText(this, "Can't create file", Toast.LENGTH_LONG).show();
                }
            }
            document.writeTo(new FileOutputStream(filePath));
            document.close();
            Toast.makeText(this, "PDF downloaded to "+filePath.toString(), Toast.LENGTH_SHORT).show();

        } catch (IOException e) {
            Toast.makeText(this, "There is an error while downloading the pdf please take the screenshot of the receipt." , Toast.LENGTH_LONG).show();
        }

        // close the document

    }
}