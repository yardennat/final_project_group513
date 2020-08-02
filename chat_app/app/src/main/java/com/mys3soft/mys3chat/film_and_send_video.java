package com.mys3soft.mys3chat;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mys3soft.mys3chat.Models.Game;
import com.mys3soft.mys3chat.Models.StaticInfo;
import com.mys3soft.mys3chat.Models.Video;

import java.util.Random;

public class film_and_send_video extends AppCompatActivity {
    private Uri videoUri;
    String friendEmail,userEmail;
    Firebase reference1;
    private static final int REQUEST_CODE = 101;
    private StorageReference videoRef;
    FirebaseDatabase database;
    TextView TrueOrFalse;
    DatabaseReference myRefTrue, myRefFalse;
    private static final String TagTrue = "MainActivity";
    private static final String TagFalse = "MainActivity";
    boolean typeVideo;
    long t;
    long f;
    String category;
    int score;
    int player;
    Bundle extras;
    String TAG;
    int id;
    int numOfRounds;
    String message;
    private String sc;
    private String mes;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.film_and_send_activity);
        database = FirebaseDatabase.getInstance();
        myRefTrue = database.getReference("TrueVideo");
        myRefFalse = database.getReference("FalseVideo");
        TrueOrFalse= findViewById(R.id.textView_truFalse);
        TextView s = findViewById(R.id.score_box);
        String screen;

        extras = getIntent().getExtras();
        friendEmail = extras.getString("FriendEmail");
        userEmail = extras.getString("userMail");
        score = extras.getInt("score");
        category= extras.getString("category");
        id = extras.getInt("id");
        player =extras.getInt("player");
        if(extras.getString("message")== null){
            message = chooseTrueOrFalse();
        }else{
            message = extras.getString("message");
        }

        if (player == 0){
            screen = "screen_player_0";
            sc = "score_player0";
            mes = "m0";

        }else{
            screen = "screen_player_1";
            sc = "score_player1";
            mes = "m1";
        }
        String sc = "your score is "+ score;
        s.setText(sc);
        database.getReference("Game").child(String.valueOf(id)).child(screen).setValue(2);
        reference1 = new Firebase(StaticInfo.MessagesEndPoint);
        TextView category1 = findViewById(R.id.put_category);
        category1.setText(category);
        videoRef = FirebaseStorage.getInstance().getReference();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        FirebaseDatabase.getInstance().getReference("Game").child(String.valueOf(id)).child(sc).setValue(score);
        FirebaseDatabase.getInstance().getReference("Game").child(String.valueOf(id)).child(mes).setValue(message);
        Intent intent = new Intent(this,ActivityMain.class);
        startActivity(intent);

    }
    @Override
    protected  void onStop() {
        super.onStop();
        FirebaseDatabase.getInstance().getReference("Game").child(String.valueOf(id)).child(sc).setValue(score);
        FirebaseDatabase.getInstance().getReference("Game").child(String.valueOf(id)).child(mes).setValue(message);


    }
    @Override
    protected  void onDestroy(){
        super.onDestroy();
        FirebaseDatabase.getInstance().getReference("Game").child(String.valueOf(id)).child(sc).setValue(score);
        FirebaseDatabase.getInstance().getReference("Game").child(String.valueOf(id)).child(mes).setValue(message);


    }

    // we choose if to ask for true or lie video
    public String chooseTrueOrFalse(){
        final String tellTrue = "true story";
        final String tellFalse = "lie";
        myRefTrue.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                long value = dataSnapshot.getValue(long.class);
                t = value;
                Log.d(TagFalse,"the value is "+value);
                myRefFalse.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        long value = dataSnapshot.getValue(long.class);
                        f = value;

                        Log.d(TagFalse, "the value is "+ value);
                        if ((float)t/(t+f)>=0.52){
                                typeVideo= false;
                                TrueOrFalse.setText(tellFalse);
                                message = tellFalse;
                            }
                        else if((float)f/(t+f)>=0.52){
                                typeVideo= true;
                                TrueOrFalse.setText(tellTrue);
                                message = tellTrue;

                        }else{
                                int i= new Random().nextInt(100);
                                if((i %  2) ==0){
                                    typeVideo= false;
                                    TrueOrFalse.setText(tellFalse);
                                    message = tellFalse;
                                }else{
                                    typeVideo= true;
                                    TrueOrFalse.setText(tellTrue);
                                    message = tellTrue;
                                }
                            }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.w(TagTrue, "Failed to read value.", error.toException());
                    }
                });
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Failed to read value
                Log.w(TagTrue, "Failed to read value.", error.toException());
            }
        });
        return message;

    }

    // record a video
    public void record(View view) {
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        startActivityForResult(intent, REQUEST_CODE);
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        videoUri = data.getData();
        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(this, "Video saved to:\n" +
                        videoUri, Toast.LENGTH_LONG).show();
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Video recording cancelled.",
                        Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Failed to record video",
                        Toast.LENGTH_LONG).show();
            }
        }
    }

    // upload
    public void upload(View view) {
        if (videoUri != null) {
            findViewById(R.id.button_upload).setEnabled(false);
            findViewById(R.id.button_rec).setEnabled(false);
            myRefTrue.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    long value = dataSnapshot.getValue(long.class);
                    t = value;
                    Log.d(TagFalse,"the value is "+value);
                    myRefFalse.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            long value = dataSnapshot.getValue(long.class);
                            f = value;
                            if(typeVideo){
                                myRefTrue.setValue((t+1));
                            }else{
                                myRefFalse.setValue((f+1));
                            }
                            Log.d(TagFalse, "the value is "+ value);
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Log.w(TagTrue, "Failed to read value.", error.toException());
                        }
                    });
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Failed to read value
                    Log.w(TagTrue, "Failed to read value.", error.toException());
                }
            });
            DatabaseReference postRef = database.getReference("Game").child(String.valueOf(id));
            postRef.runTransaction(new Transaction.Handler() {
                @NonNull
                @Override
                public Transaction.Result doTransaction(@NonNull MutableData mutableData) {

                    Game p= mutableData.getValue(Game.class);
                    if(p!= null){
                        p.setNumOfRounds(p.getNumOfRounds()+1);
                        numOfRounds = p.getNumOfRounds();
                        StorageReference riversRef = videoRef.child("video/ver"+id+"_"+(numOfRounds-1)+".mp4");

                        riversRef.putFile(videoUri)
                                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        // Get a URL to the uploaded content
                                        Task<Uri> downloadUrl = taskSnapshot.getStorage().getDownloadUrl();
                                        Toast.makeText(film_and_send_video.this, "Upload complete",
                                                Toast.LENGTH_LONG).show();
                                    }
                                }).addOnProgressListener(
                                new OnProgressListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                        updateProgress(taskSnapshot);
                                    }})
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(film_and_send_video.this,
                                                "Upload failed: " + e.getLocalizedMessage(),
                                                Toast.LENGTH_LONG).show();

                                    }
                                });

                    }


                    // Set value and report transaction success
                    mutableData.setValue(p);
                    return Transaction.success(mutableData);
                }

                @Override
                public void onComplete(DatabaseError databaseError, boolean b,
                                       DataSnapshot dataSnapshot) {
                    // Transaction completed
                    Log.d(TAG, "postTransaction:onComplete:" + databaseError);
                }
            });
        } else {
            Toast.makeText(film_and_send_video.this, "Nothing to upload",
                    Toast.LENGTH_LONG).show();
        }

    }
    public void updateProgress(UploadTask.TaskSnapshot taskSnapshot) {

        @SuppressWarnings("VisibleForTests") long fileSize = taskSnapshot.getTotalByteCount();

        @SuppressWarnings("VisibleForTests")
        long uploadBytes = taskSnapshot.getBytesTransferred();

        long progress = (100 * uploadBytes) / fileSize;

        ProgressBar progressBar = (ProgressBar) findViewById(R.id.pbar);
        progressBar.setProgress((int) progress);
        if(progress == 100){

            Video v = new Video("video/ver"+id+"_"+(numOfRounds-1)+".mp4",typeVideo,userEmail,friendEmail);

            reference1.child(userEmail+"-"+friendEmail).setValue(v);

            Intent intent = new Intent(film_and_send_video.this, waiting_guess.class);
            intent.putExtra("FriendEmail",friendEmail);
            intent.putExtra("userMail", userEmail);
            intent.putExtra("category",category);
            intent.putExtra("score", score);
            intent.putExtra("id",id );
            intent.putExtra("numRounds", numOfRounds);
            intent.putExtra("player",player);
            startActivity(intent);

        }
    }



}
