package com.mys3soft.mys3chat;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.mys3soft.mys3chat.Models.Game;
import com.mys3soft.mys3chat.Models.Video;

public class gusseing_screen extends AppCompatActivity {
    private StorageReference videoRef;
    private DatabaseReference reference1,reference2;
    private FirebaseDatabase database;
    private String TAG;
    private String nameVideo;
    private String friendEmail;
    private String userEmail;
    private Bundle extras;
    private int score;
    private String category;
    private int id;
    private int player;
    private VideoView videoView;
    private int numOfRounds;
    private String sc;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gusseing_screen);
        database = FirebaseDatabase.getInstance();
        String screen;
//        mPreferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);
        videoView = (VideoView) findViewById(R.id.videoView2);

//        if(getIntent().getExtras()!= null){
        extras = getIntent().getExtras();
        friendEmail = extras.getString("FriendEmail");
        userEmail = extras.getString("userMail");
        score = extras.getInt("score");
        category= extras.getString("category");
        id = extras.getInt("id");
        player =extras.getInt("player");


        if (player == 0){
            screen = "screen_player_0";
            sc="score_player0";
        }else{
            screen = "screen_player_1";
            sc="score_player1";
        }
        database.getReference("Game").child(String.valueOf(id)).child(screen).setValue(6);
        TextView s = findViewById(R.id.score_box);
        String sc = "your score is "+ score;
        s.setText(sc);
        reference1 = database.getReference("messages");
        reference1.child(friendEmail+"-"+userEmail).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Video user = dataSnapshot.getValue(Video.class);
                if(user!= null){
                    nameVideo= user.name_video;

                    Log.d(TAG, "video name: " + user.name_video );
                }

            }

            @Override
            public void onCancelled(DatabaseError error) {
// Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
        reference2 = database.getReference("Game").child(String.valueOf(id));
        reference2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Game g = dataSnapshot.getValue(Game.class);
                if(g!= null){
                    numOfRounds = g.getNumOfRounds();

                    Log.d(TAG, "num rounds: " + g.getNumOfRounds());
                }

            }

            @Override
            public void onCancelled(DatabaseError error) {
// Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        FirebaseDatabase.getInstance().getReference("Game").child(String.valueOf(id)).child(sc).setValue(score);
        Intent intent = new Intent(this,ActivityMain.class);
        startActivity(intent);

    }
    @Override
    protected  void onStop() {
        super.onStop();
        FirebaseDatabase.getInstance().getReference("Game").child(String.valueOf(id)).child(sc).setValue(score);

    }
    @Override
    protected  void onDestroy(){
        super.onDestroy();
        FirebaseDatabase.getInstance().getReference("Game").child(String.valueOf(id)).child(sc).setValue(score);

    }


    // download the video from firebase
    public void download(View view) {
        findViewById(R.id.button5).setEnabled(false);
        MediaStore.Video video  =new MediaStore.Video();
        videoRef = FirebaseStorage.getInstance().getReference();
        videoRef.child(nameVideo).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // Got the download URL for 'users/me/profile.png'
                videoView.setVideoURI(uri);
                videoView.start();


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });

    }
    // start again the video hen the video is finish by click on the screen
    public void start_again(View view){
        if(!videoView.isPlaying()){
            videoView.start();
        }
    }

    // the next to buttons is the choice of the user if the video is true or not

    public void true_button(View view){
        findViewById(R.id.true_button).setEnabled(false);

        Intent intent = new Intent(gusseing_screen.this, wait_result.class);
        intent.putExtra("FriendEmail",friendEmail);
        intent.putExtra("userMail", userEmail);
        intent.putExtra("answer",true);
        intent.putExtra("category",category);
        intent.putExtra("score", score);
        intent.putExtra("id",id );
        intent.putExtra("player",player);
        intent.putExtra("numOfRounds", numOfRounds);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        finish();

        startActivity(intent);
    }

    public void false_button(View view){
        findViewById(R.id.false_button).setEnabled(false);
        Intent intent = new Intent(gusseing_screen.this, wait_result.class);
        intent.putExtra("FriendEmail",friendEmail);
        intent.putExtra("userMail", userEmail);
        intent.putExtra("answer",false);
        intent.putExtra("category",category);
        intent.putExtra("score", score);
        intent.putExtra("id",id );
        intent.putExtra("player",player);
        intent.putExtra("numOfRounds", numOfRounds);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        finish();

        startActivity(intent);
    }



}
