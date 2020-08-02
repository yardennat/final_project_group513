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
import com.google.firebase.database.ValueEventListener;
import com.mys3soft.mys3chat.Models.Game;

/**
 * this class are supposed to wait for the answer of the other player if to continue the game or not
 */
public class process_continue_game extends AppCompatActivity {

    private Bundle extras;
    private String friendEmail;
    private String userEmail;
    private int score;
    private String category;
    private int id;
    private int player;
    private String message;
    private int choise;
    private boolean flag;
    private FirebaseDatabase database;
    private DatabaseReference gameRef;
    private String sc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_process_continue_game);
//        if(getIntent().getExtras()!= null) {
        extras = getIntent().getExtras();
        friendEmail = extras.getString("FriendEmail");
        userEmail = extras.getString("userMail");
        score = extras.getInt("score");
        category = extras.getString("category");
        id = extras.getInt("id");
        player = extras.getInt("player");
        message="";
        choise = 0;
        flag = true;

        String screen;
        if (player == 0){
            screen = "screen_player_0";
            sc= "score_player0";
        }else{
            screen = "screen_player_1";
            sc= "score_player1";
        }
        database = FirebaseDatabase.getInstance();
        gameRef =  database.getReference("Game").child(String.valueOf(id));
        gameRef.child(screen).setValue(10);
        gameRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Game game = dataSnapshot.getValue(Game.class);
                if(game!= null){
                    if(player == 0 && game.getWantContP1() == 1){
                        cont_p0(flag);
                        flag= false;

                        Log.d("processContinue","choice =1");


                    }
                    else if(player ==1 && game.getWantContP0() ==1){
                        cont_p1(flag);
                        flag= false;
                        Log.d("processContinue","choice =2");


                    }
                    else if( (player == 0&& game.getWantContP1() ==2)|| (player ==1 && game.getWantContP0() ==2)){
                        endGame(flag);
                        flag = false;
                        Log.d("processContinue","choice = 3");
                    }else{
                        Log.d("processContinue","wait for the other playing");
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this,ActivityMain.class);
        FirebaseDatabase.getInstance().getReference("Game").child(String.valueOf(id)).child(sc).setValue(score);
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

    // for case you player 0 an player 1 want to continue
    private  void cont_p0(boolean toDo){
        if(toDo){
            Intent intent = new Intent(process_continue_game.this, film_and_send_video.class);
            intent.putExtra("FriendEmail",friendEmail);
            intent.putExtra("userMail", userEmail);
            intent.putExtra("score", score);
            intent.putExtra("category",category);
            intent.putExtra("id",id );
            intent.putExtra("messages",message);
            intent.putExtra("player",player);

            startActivity(intent);
        }

    }
    // for case you player 1 an player 0 want to continue
    private  void cont_p1(boolean toDo){
        if(toDo){
            Intent intent = new Intent(process_continue_game.this, waiting_response_video.class);
            intent.putExtra("FriendEmail",friendEmail);
            intent.putExtra("userMail", userEmail);
            intent.putExtra("score", score);
            intent.putExtra("category",category);
            intent.putExtra("id",id );
            intent.putExtra("messages",message);
            intent.putExtra("player",player);

            startActivity(intent);

        }

    }
    // for case that the other player quit
    private  void endGame(boolean toDo){
        if(toDo){
            gameRef.child("active").setValue(false);
            message= "Your opponent has left the match so you have earned another point";
            Intent intent = new Intent(process_continue_game.this, give_up_screen.class);
            intent.putExtra("FriendEmail",friendEmail);
            intent.putExtra("userMail", userEmail);
            intent.putExtra("score", score);
            intent.putExtra("category",category);
            intent.putExtra("id",id );
            intent.putExtra("messages",message);
            intent.putExtra("player",player);

            startActivity(intent);


        }

    }

}
