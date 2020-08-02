package com.mys3soft.mys3chat;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mys3soft.mys3chat.Models.Video;


/**
 *  this activity ia waiting until the other player finish to film an upload
 */
public class waiting_response_video extends AppCompatActivity {

    Bundle extras;
    FirebaseDatabase db;
    DatabaseReference ref;
    String friendEmail;
    String userEmail;
    private int score;
    private  String sc;
    private String category;
    private int id;
    private int player;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting_response_video);
        extras = getIntent().getExtras();
        friendEmail = extras.getString("FriendEmail");
        userEmail = extras.getString("userMail");
        score = extras.getInt("score");
        category= extras.getString("category");
        id = extras.getInt("id");
        player =extras.getInt("player");

        db= FirebaseDatabase.getInstance();
        ref = db.getReference("messages");

        String screen;
        if (player == 0){
            screen = "screen_player_0";
            sc="score_player0";

        }else{
            screen = "screen_player_1";
            sc="score_player1";
        }
        db.getReference("Game").child(String.valueOf(id)).child(screen).setValue(5);
        move();


    }


    @Override
    protected void onStart() {
        super.onStart();
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

    public void move(){
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(friendEmail+"-"+userEmail).exists()){
                    Video v = dataSnapshot.getValue(Video.class);
                    if(v!= null) {
                        if(v.succed_to_guess==0){
                            Intent intent = new Intent(waiting_response_video.this, gusseing_screen.class);
                            intent.putExtra("FriendEmail",friendEmail);
                            intent.putExtra("userMail", userEmail);
                            intent.putExtra("category",category);
                            intent.putExtra("score", score);
                            intent.putExtra("id",id );
                            intent.putExtra("player",player);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            finish();

                            startActivity(intent);
                        }

                    }


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
