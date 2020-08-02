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
 * this activity waiting for the other player to guess
 */
public class waiting_guess extends AppCompatActivity {
    private String friendEmail;
    private String userEmail;
    private int score;
    private String category;
    private int id;
    private int numOfRounds;
    private FirebaseDatabase db;
    private DatabaseReference ref;
    private Bundle extras;
    private int player;
    private String sc;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.waiting_guess_activity);

        extras = getIntent().getExtras();
        friendEmail = extras.getString("FriendEmail");
        userEmail = extras.getString("userMail");
        score = extras.getInt("score");
        category= extras.getString("category");
        id = extras.getInt("id");
        numOfRounds = extras.getInt("numRounds");
        player =extras.getInt("player");

        db = FirebaseDatabase.getInstance();
        String screen;
        if (player == 0){
            screen = "screen_player_0";
            sc="score_player0";

        }else{
            screen = "screen_player_1";
            sc="score_player1";
        }
        db.getReference("Game").child(String.valueOf(id)).child(screen).setValue(3);
        moveTo();
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


    public void moveTo(){
        ref= db.getReference("messages").child(userEmail+"-"+friendEmail);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Video v = dataSnapshot.getValue(Video.class);
                if(v!= null){
                    if(v.succed_to_guess != v.WAIT){

                        Intent intent = new Intent(waiting_guess.this, result_player_filming.class);
                        intent.putExtra("FriendEmail",friendEmail);
                        intent.putExtra("userMail", userEmail);
                        intent.putExtra("category",category);
                        intent.putExtra("score",score);
                        intent.putExtra("id",id );
                        intent.putExtra("succeed", v.succed_to_guess);
                        intent.putExtra("numRounds",numOfRounds);
                        intent.putExtra("player",player);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        finish();
                        startActivity(intent);

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
}
