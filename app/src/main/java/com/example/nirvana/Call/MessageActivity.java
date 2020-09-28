package com.example.nirvana.Call;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.nirvana.Adapter.MessageAdapter;
import com.example.nirvana.Model.Chat;
import com.example.nirvana.Model.Status_details;
import com.example.nirvana.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageActivity extends AppCompatActivity {
    CircleImageView circleImageView;
    TextView username,Status;
    String link,name,d_phone,p_phone,who;
    ImageButton btn_send,btn_voice,btn_camera,btn_attach;
    EditText text_send;
    String sender,reciever;
    MessageAdapter messageAdapter;
    List<Chat> mChat;
    RecyclerView recyclerView;
    SpeechRecognizer speechRecognizer;
    private static final int pic_id = 123,pic_file=213;
    private StorageReference mstorageRef;
    private Uri Imagepath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        Toolbar toolbar=findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        speechRecognizer= SpeechRecognizer.createSpeechRecognizer(this);
        recyclerView=findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        circleImageView=findViewById(R.id.profile_image);
        username=findViewById(R.id.username);
        Status=findViewById(R.id.status);
        Intent intent=getIntent();
        link=intent.getStringExtra("link");
        name=intent.getStringExtra("name");
        d_phone=intent.getStringExtra("d_phone");
        p_phone=intent.getStringExtra("p_phone");
        who=intent.getStringExtra("who");
        btn_camera=findViewById(R.id.camera);
        mstorageRef= FirebaseStorage.getInstance().getReference();
        if(link.equals("None"))
        {
          circleImageView.setImageResource(R.drawable.green_person_logo);
        }
        else
        {
            Glide.with(this).load(link).into(circleImageView);
        }
        username.setText(name);
        btn_send=findViewById(R.id.btn_send);
        btn_voice=findViewById(R.id.send_voice);
        text_send=findViewById(R.id.text_send);
        text_send.addTextChangedListener(filterTextWatcher);
        btn_attach=findViewById(R.id.attach);
        if(who.equals("doctor"))
        {
            sender=d_phone;
            reciever=p_phone;
        }
        else
        {
            sender=p_phone;
            reciever=d_phone;
        }
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message=text_send.getText().toString();
                if(!message.equals(""))
                {
                  sendMessage(sender,reciever,message,"message");
                  btn_send.setVisibility(View.GONE);
                  btn_voice.setVisibility(View.VISIBLE);
                }
                else
                {
                    Toast.makeText(MessageActivity.this,"Please type a message before sending",Toast.LENGTH_SHORT).show();
                }
            }
        });
        final Intent speechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

        speechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle bundle) {

            }

            @Override
            public void onBeginningOfSpeech() {

            }

            @Override
            public void onRmsChanged(float v) {

            }

            @Override
            public void onBufferReceived(byte[] bytes) {

            }

            @Override
            public void onEndOfSpeech() {

            }

            @Override
            public void onError(int i) {

            }

            @Override
            public void onResults(Bundle bundle) {
                ArrayList<String> data = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                speechRecognizer.stopListening();
                sendMessage(sender,reciever,data.get(0),"message");
            }

            @Override
            public void onPartialResults(Bundle bundle) {

            }

            @Override
            public void onEvent(int i, Bundle bundle) {

            }
        });

        btn_voice.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_UP){
                    speechRecognizer.stopListening();
                }
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                    speechRecognizer.startListening(speechRecognizerIntent);
                }
                return false;
            }
        });

        readMessages(sender,reciever,link);
        setStatus();
        checkStatus();
        btn_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent camera_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                startActivityForResult(camera_intent, pic_id);
            }
        });
        btn_attach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select an Image"),pic_file);
            }
        });
    }
    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }
    protected void onActivityResult(int requestCode, int resultCode,@Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == pic_id && resultCode == RESULT_OK ) {
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                Imagepath = getImageUri(this, bitmap);
            }
            else if(requestCode==pic_file&&resultCode==RESULT_OK)
            {
                Imagepath=data.getData();
            }
                StorageReference reference = mstorageRef.child("Messages").child(sender).child(reciever).child(Imagepath.toString());
                reference.putFile(Imagepath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                String link = uri.toString();
                                sendMessage(sender, reciever, link, "image");
                            }
                        });
                    }
                });
    }
   private void sendMessage(String sender,String reciever,String message,String type)
   {
       DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("Chat_Messages").child(d_phone).child(p_phone);
       HashMap<String,Object> hashMap=new HashMap<>();
       hashMap.put("sender",sender);
       hashMap.put("reciever",reciever);
       hashMap.put("message",message);
       hashMap.put("type",type);
       databaseReference.push().setValue(hashMap);
       readMessages(sender,reciever,link);
       text_send.setText("");
   }
   private void readMessages(String myid,String userid,String imageurl)
   {
       mChat=new ArrayList<>();
       DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("Chat_Messages").child(d_phone).child(p_phone);
       databaseReference.addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot snapshot) {
               mChat.clear();
               for(DataSnapshot snapshot1:snapshot.getChildren())
               {
                   Chat chat=snapshot1.getValue(Chat.class);
                   if(chat.getReciever().equals(myid)&&chat.getSender().equals(userid)||
                           chat.getReciever().equals(userid)&&chat.getSender().equals(myid))
                   {
                       mChat.add(chat);
                   }
               }
               messageAdapter =new MessageAdapter(MessageActivity.this,mChat,imageurl,myid,userid);
               recyclerView.setAdapter(messageAdapter);
           }

           @Override
           public void onCancelled(@NonNull DatabaseError error) {

           }
       });

   }
   private void setStatus()
   {
       String info="Online";
       DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("Status").child(sender);
       Status_details status=new Status_details(
               info
       );
       databaseReference.setValue(status).addOnCompleteListener(new OnCompleteListener<Void>() {
           @Override
           public void onComplete(@NonNull Task<Void> task) {

           }
       });
   }
    private void checkStatus()
    {
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("Status").child(reciever);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    HashMap<String, Object> hashMap = (HashMap<String, Object>) snapshot.getValue();
                    String status = (String) hashMap.get("status");
                    Status.setText(status);
                }
                else
                {
                    Status.setText("Offline");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Task<Void> databaseReference= FirebaseDatabase.getInstance().getReference("Status").child(sender).child("status")
                .setValue("Offline");
    }
    private TextWatcher filterTextWatcher = new TextWatcher() {
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if(text_send.getText().toString().equals(""))
            {
                btn_voice.setVisibility(View.VISIBLE);
                btn_send.setVisibility(View.GONE);
            }
            else
            {
                btn_voice.setVisibility(View.GONE);
                btn_send.setVisibility(View.VISIBLE);
            }

        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            if(text_send.getText().toString().equals(""))
            {
                btn_voice.setVisibility(View.VISIBLE);
                btn_send.setVisibility(View.GONE);
            }
            else
            {
                btn_voice.setVisibility(View.GONE);
                btn_send.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
            if(text_send.getText().toString().equals(""))
            {
                btn_voice.setVisibility(View.VISIBLE);
                btn_send.setVisibility(View.GONE);
            }
            else
            {
                btn_voice.setVisibility(View.GONE);
                btn_send.setVisibility(View.VISIBLE);
            }
        }

    };

}