package com.mys3soft.mys3chat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mys3soft.mys3chat.Models.Game;

/**
 * this activity sho the result for the round to the player that film
 */
public class result_player_filming extends AppCompatActivity {

    private FirebaseDatabase database;
    private String category;
    private int id;
    private int player;
    private int GUESS_TRUE = 1;
    private int GUESS_FALSE= 2;
    private int score;
    private String friendEmail;
    private String userEmail;
    private Bundle extras;
    private int numOfRounds;
    private String mes;
    private String cont;
    private String sc;
    private String txt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result_player_filming_activity);
        database = FirebaseDatabase.getInstance();
        TextView res = findViewById(R.id.result_guess);
        final Button b = (Button)findViewById(R.id.cont_film);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wait_for_video();
            }
        });

        extras = getIntent().getExtras();
        friendEmail = extras.getString("FriendEmail");
        userEmail = extras.getString("userMail");
        score = extras.getInt("score");
        category = extras.getString("category");
        id = extras.getInt("id");
        numOfRounds = extras.getInt("numRounds");
        player =extras.getInt("player");
        if(extras.getInt("succeed") != 0){// check if this player win or not
            DatabaseReference reference = database.getReference("messages").child(userEmail+"-"+friendEmail);
            reference.removeValue();// delete the object from the message database
            if(extras.getInt("succeed") ==1) {
                mes = "you lose!";
                res.setText(mes);

            }else if(extras.getInt("succeed") == 2){
                mes= "you won!";
                res.setText(mes);
                score+=1;
            }

        }else{
            mes = extras.getString("txt");
            res.setText(mes);
        }

        TextView s = findViewById(R.id.score_box);
        String screen;
        if (player == 0){
            screen = "screen_player_0";
            cont = "wantContP0";
            sc = "score_player0";
            txt = "m0";
        }else{
            screen = "screen_player_1";
            cont = "wantContP1";
            sc = "score_player1";
            txt = "m1";
        }
        database.getReference("Game").child(String.valueOf(id)).child(screen).setValue(4);

        database.getReference("Game").child(String.valueOf(id)).child(cont).setValue(0);
        String sc = "your score is "+ score;
        s.setText(sc);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        FirebaseDatabase.getInstance().getReference("Game").child(String.valueOf(id)).child(sc).setValue(score);
        FirebaseDatabase.getInstance().getReference("Game").child(String.valueOf(id)).child(txt).setValue(mes);
        Intent intent = new Intent(this,ActivityMain.class);
        startActivity(intent);

    }

    @Override
    protected  void onStop() {
        super.onStop();
        FirebaseDatabase.getInstance().getReference("Game").child(String.valueOf(id)).child(sc).setValue(score);
        FirebaseDatabase.getInstance().getReference("Game").child(String.valueOf(id)).child(txt).setValue(mes);


    }
    @Override
    protected  void onDestroy(){
        super.onDestroy();
        FirebaseDatabase.getInstance().getReference("Game").child(String.valueOf(id)).child(sc).setValue(score);
        FirebaseDatabase.getInstance().getReference("Game").child(String.valueOf(id)).child(txt).setValue(mes);


    }

    // continue in the Game - this function choose where to move,
    // if it even round for giveup or continue screen else to waiting response video
    private void wait_for_video(){

        DatabaseReference ref = database.getReference("Game").child(String.valueOf(id));

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Game g = dataSnapshot.getValue(Game.class);
                if(g!= null){
                    int screen_comp;
                    if(player ==0){
                        screen_comp = g.getScreen_player_1();
                    }
                    else{
                        screen_comp= g.getScreen_player_0();
                    }
                    Log.d("result_player_filming", "screen comp = "+ screen_comp);
                    if(numOfRounds % 2 == 1 ||(screen_comp==3  && numOfRounds %2 ==0) ){
                        Intent intent = new Intent(result_player_filming.this, waiting_response_video.class);
                        intent.putExtra("FriendEmail",friendEmail);
                        intent.putExtra("userMail", userEmail);
                        intent.putExtra("score",score);
                        intent.putExtra("category",category);
                        intent.putExtra("id",id );
                        intent.putExtra("player",player);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        finish();
                        startActivity(intent);

                    }else{
                        Intent intent = new Intent(result_player_filming.this, give_up_or_continue.class);
                        intent.putExtra("FriendEmail",friendEmail);
                        intent.putExtra("userMail", userEmail);
                        intent.putExtra("score",score);
                        intent.putExtra("category",category);
                        intent.putExtra("id",id );
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
