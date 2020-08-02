package com.mys3soft.mys3chat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.FirebaseDatabase;

/**
 * this activity is result for player who guess
 */
public class result_player_guess extends AppCompatActivity {


    private FirebaseDatabase database;
    private String category;
    private int id;
    private int numOfRounds;
    private int player;
    private String friendEmail;
    private String userEmail;
    private Bundle extras;
    private String message;
    private int score;
    private String cont;
    private String sc;
    private String mes;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.result_player_guess_activity);
        database = FirebaseDatabase.getInstance();
        String screen;
        extras = getIntent().getExtras();
        friendEmail = extras.getString("FriendEmail");
        userEmail = extras.getString("userMail");
        score = extras.getInt("score");

        category= extras.getString("category");
        id = extras.getInt("id");
        player =extras.getInt("player");
        message = extras.getString("message");
        numOfRounds = extras.getInt("numOfRounds");

        if (player == 0){
            screen = "screen_player_0";
            cont = "wantContP0";
            sc="score_player0";
            mes = "m0";
        }else{
            screen = "screen_player_1";
            cont = "wantContP1";
            sc="score_player1";
            mes = "m1";
        }
        final Button b = (Button)findViewById(R.id.cont_guess);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start_film_button();
            }
        });
        database.getReference("Game").child(String.valueOf(id)).child(screen).setValue(8);
        database.getReference("Game").child(String.valueOf(id)).child(cont).setValue(0);

        TextView s = findViewById(R.id.score_box);

        TextView res = findViewById(R.id.result_guess);
        res.setText(message);
        String sc = "your score is "+ score;
        s.setText(sc);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this,ActivityMain.class);
        FirebaseDatabase.getInstance().getReference("Game").child(String.valueOf(id)).child(sc).setValue(score);
        FirebaseDatabase.getInstance().getReference("Game").child(String.valueOf(id)).child(mes).setValue(message);
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
    // continue in the Game - this function choose where to move,
    // if it even round for giveup or continue screen else to film and send video

    private void start_film_button(){

        Log.d("messege", "number of rounds"+numOfRounds);
        if(numOfRounds % 2 == 1 ){
            Intent intent = new Intent(result_player_guess.this, film_and_send_video.class);
            intent.putExtra("FriendEmail",friendEmail);
            intent.putExtra("userMail", userEmail);
            intent.putExtra("score",score);
            intent.putExtra("category",category);
            intent.putExtra("id",id );
            intent.putExtra("player",player);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            finish();

            startActivity(intent);
        }else {
            Intent intent = new Intent(result_player_guess.this, give_up_or_continue.class);
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
