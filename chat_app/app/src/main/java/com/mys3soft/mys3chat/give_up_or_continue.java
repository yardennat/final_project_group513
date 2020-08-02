package com.mys3soft.mys3chat;

import android.content.Intent;
import android.os.Bundle;
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
 * in this activity the users choose if the continue the game or to quit
 */
public class give_up_or_continue extends AppCompatActivity {
    private FirebaseDatabase db;
    private DatabaseReference reference;
    private Bundle extras;
    private String player;
    private int player1;
    private String friendEmail;
    private String userEmail;
    private String category;
    private int id;
    private int score;
    public int WAIT_ANS = 0;
    public int WANT_CONT =1;
    public int WANT_GIVE_UP=2;
    private TextView textView1;
    private TextView textView2;
    private Button end;
    private Button cont;
    private String message;
    private String cont1;
    private String sc;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.give_up_or_continue_activity);
        db= FirebaseDatabase.getInstance();
        textView1 = findViewById(R.id.round_over_view_text);
        textView2 = findViewById(R.id.textView3);
        end= findViewById(R.id.end_game_button);
        cont= findViewById(R.id.continue_buttton);
        message = "you quit";
        end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                end_game_button();
            }
        });
        cont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                keep_playing_button();
            }
        });

        String screen;
        extras = getIntent().getExtras();
        friendEmail = extras.getString("FriendEmail");
        userEmail = extras.getString("userMail");
        score = extras.getInt("score");
        category= extras.getString("category");
        id = extras.getInt("id");
        player1 = extras.getInt("player");
        reference= db.getReference("Game").child(String.valueOf(id));
        if (player1 == 0){
            screen = "screen_player_0";
            cont1 = "wantContP0";
            sc = "score_player0";
        }else{
            screen = "screen_player_1";
            cont1 = "wantContP1";
            sc = "score_player1";
        }
        reference.child(screen).setValue(9);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Game game = dataSnapshot.getValue(Game.class);
                if(game!= null){
                    if(game.getPlayer0().equals(userEmail)){
                        player = "wantContP0";

                    }else{
                        player = "wantContP1";
                    }
                    textView1.setVisibility(TextView.VISIBLE);
                    textView2.setVisibility(TextView.VISIBLE);
                    end.setVisibility(Button.VISIBLE);
                    cont.setVisibility(Button.VISIBLE);
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
    private void keep_playing_button(){
        reference.child(player).setValue(WANT_CONT);

        Intent intent = new Intent(give_up_or_continue.this, process_continue_game.class);
        intent.putExtra("FriendEmail",friendEmail);
        intent.putExtra("userMail", userEmail);
        intent.putExtra("score", score);
        intent.putExtra("category",category);
        intent.putExtra("id",id );
        intent.putExtra("player",player1);

        startActivity(intent);
    }
    private void end_game_button(){
        reference.child(player).setValue(WANT_GIVE_UP);

        Intent intent = new Intent(give_up_or_continue.this, give_up_screen.class);
        intent.putExtra("FriendEmail",friendEmail);
        intent.putExtra("userMail", userEmail);
        intent.putExtra("score", score);
        intent.putExtra("category", category);
        intent.putExtra("id",id );
        intent.putExtra("message",message);
        intent.putExtra("player",player1);
        startActivity(intent);
    }
}
