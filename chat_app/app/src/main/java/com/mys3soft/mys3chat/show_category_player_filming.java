package com.mys3soft.mys3chat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 *  this class show to the player who first filming his category
 */
public class show_category_player_filming extends AppCompatActivity {
    private TextView subject;
    private FirebaseDatabase db;
    private DatabaseReference ref;
    private String userEmail;
    private String friendEmail;
    private int score;
    private String category;
    private int id;
    private int player;
    private Bundle extras;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_category_player_filming_activity);
        extras = getIntent().getExtras();
        friendEmail = extras.getString("FriendEmail");
        userEmail = extras.getString("userMail");
        score = extras.getInt("score");
        category= extras.getString("category");
        id = extras.getInt("id");
        player =extras.getInt("player");
        db = FirebaseDatabase.getInstance();
        String screen;

        subject= findViewById(R.id.textViewSubject);

        subject.setText(category);
        if (player == 0){
            screen = "screen_player_0";
        }else{
            screen = "screen_player_1";
        }
        db.getReference("Game").child(String.valueOf(id)).child(screen).setValue(1);
        TextView s = findViewById(R.id.score_box);
        String sc = "your score is "+score;

        s.setText(sc);
    }

    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this,ActivityMain.class);
        startActivity(intent);

    }

    // click on this button to start the game
    public void startGame(View view){
        Intent intent = new Intent(show_category_player_filming.this, film_and_send_video.class);
        intent.putExtra("FriendEmail",friendEmail);
        intent.putExtra("userMail", userEmail);
        intent.putExtra("score", score);
        intent.putExtra("category",category);
        intent.putExtra("id",id);
        intent.putExtra("player",player);
        startActivity(intent);
    }

}