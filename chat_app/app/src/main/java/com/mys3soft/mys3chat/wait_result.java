package com.mys3soft.mys3chat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.mys3soft.mys3chat.Models.Video;

/**
 * this activity get the answer from the player who guess and check if he guess right
 * or wrong and move to the next screen and update the score of the Game
 */
public class wait_result extends AppCompatActivity {
    private DatabaseReference reference1;
    private FirebaseDatabase database;
    private String TAG;
    private boolean ans;
    private boolean succed_to_guess;
    private boolean answer;
    private int GUESS_RIGHT = 1;
    private int GUESS_WRONG= 2;
    private int numOfRounds;
    private String friendEmail;
    private String userEmail;
    private String category;
    private int player;
    private int id;
    private Bundle extras;
    private int score;
    private String message;
    private DatabaseReference myRef;


    private DatabaseReference refGame;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wait_result);

        database = FirebaseDatabase.getInstance();
        String screen;

        extras = getIntent().getExtras();
        friendEmail = extras.getString("FriendEmail");
        userEmail = extras.getString("userMail");
        score = extras.getInt("score");
        category = extras.getString("category");
        id = extras.getInt("id");
        player =extras.getInt("player");
        answer = extras.getBoolean("answer");
        numOfRounds =extras.getInt("numOfRounds");


        if (player == 0){
            screen = "screen_player_0";
        }else{
            screen = "screen_player_1";
        }
        database.getReference("Game").child(String.valueOf(id)).child(screen).setValue(7);

        reference1 = database.getReference("messages");
        myRef = database.getReference("video");

        reference1.child(friendEmail + "-" + userEmail).runTransaction(new Transaction.Handler() {
            @NonNull
            @Override
            public Transaction.Result doTransaction(@NonNull MutableData mutableData) {

                Video video = mutableData.getValue(Video.class);
                if(video!= null){
                    if (video.type == answer) {
//                        succed_to_guess = true;
                        score+=1;
                        message = "you won!";
                        reference1.child(friendEmail + "-" + userEmail).child("succed_to_guess").setValue(GUESS_RIGHT);
                        video.succed_to_guess= GUESS_RIGHT;

                    } else {
//                        succed_to_guess = false;
                        video.succed_to_guess = GUESS_WRONG;
                        message = "you lose!";
                        reference1.child(friendEmail + "-" + userEmail).child("succed_to_guess").setValue(GUESS_WRONG);
                    }
                    myRef.push().setValue(video);
                }

                // Set value and report transaction success
                mutableData.setValue(video);
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b,
                                   DataSnapshot dataSnapshot) {
                // Transaction completed
                Log.d(TAG, "postTransaction:onComplete:" + databaseError);
                Intent intent = new Intent(wait_result.this, result_player_guess.class);
                intent.putExtra("FriendEmail", friendEmail);
                intent.putExtra("userMail", userEmail);
                intent.putExtra("message", message);
                intent.putExtra("category",category);
                intent.putExtra("score", score);
                intent.putExtra("id", id);

                intent.putExtra("numOfRounds", numOfRounds);
                intent.putExtra("player",player);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                finish();

                startActivity(intent);

            }

        });

    }

}
