package com.mys3soft.mys3chat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mys3soft.mys3chat.Models.Game;
import com.mys3soft.mys3chat.Models.User;
import com.mys3soft.mys3chat.Services.Tools;

/**
 * this is game over activity. the score of th user updates
 */
public class give_up_screen extends AppCompatActivity {
    private Bundle extras;
    private FirebaseDatabase db;
    private DatabaseReference ref;
    private String TAG;
    private String friendEmail;
    private String userEmail;
    private String category;
    private int player;
    private int id;
    private int score;
    private String message;
    private String mes;
    private int sc;
    private int scf;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.give_up_screen_activity);
        db = FirebaseDatabase.getInstance();
        String screen;

        message ="game over";

        extras = getIntent().getExtras();
        friendEmail = extras.getString("FriendEmail");
        userEmail = extras.getString("userMail");
        score = extras.getInt("score");
        category= extras.getString("category");
        id = extras.getInt("id");
        message =extras.getString("message");
        player = extras.getInt("player");

        final DatabaseReference reference= db.getReference("users").child(Tools.encodeString(userEmail));
        final DatabaseReference gameref = db.getReference("Game").child(String.valueOf(id));
        gameref.child("active").setValue(false);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User  u = dataSnapshot.getValue(User.class);
                if(u!= null){
                  int s = u.getScore();
                  reference.child("score").setValue(s+score);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        gameref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Game game = dataSnapshot.getValue(Game.class);
                if(game!= null){
                    if(game.getScore_player0()> game.getScore_player1()){
                        if(player==0){
                            mes = "You won";
                            sc = game.getScore_player0();
                            scf= game.getScore_player1();
                        }
                        else{
                            mes = "You lose";
                            sc=game.getScore_player1();
                            scf = game.getScore_player0();
                        }
                    }else if(game.getScore_player0()== game.getScore_player1()){
                        mes = "There's a tie in the game";
                        if(player==0){
                            sc = game.getScore_player0();
                            scf= game.getScore_player1();
                        }
                        else{
                            sc=game.getScore_player1();
                            scf = game.getScore_player0();
                        }
                    }else{
                        if(player==1){
                            mes = "You won";
                            sc = game.getScore_player1();
                            scf= game.getScore_player0();
                        }
                        else{
                            mes = "You lose";
                            sc=game.getScore_player0();
                            scf = game.getScore_player1();
                        }
                    }
                    TextView t1 = (TextView)findViewById(R.id.won_lose);
                    t1.setText(mes);
                    TextView t2 =  (TextView)findViewById(R.id.score_view1);
                    String sc1= "Your score is "+String.valueOf(sc);
                    t2.setText(sc1);
                    TextView t3 =  (TextView)findViewById(R.id.score_view2);
                    String sc2 ="Your friend score is "+String.valueOf(scf);
                    t3.setText(sc2);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        if (player == 0){
            screen = "screen_player_0";
        }else{
            screen = "screen_player_1";
        }
        db.getReference("Game").child(String.valueOf(id)).child(screen).setValue(0);
        TextView m = findViewById(R.id.textView2);
        m.setText(message);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this,ActivityMain.class);
        startActivity(intent);

    }

    public void exit_button(View view){
        Intent intent = new Intent(give_up_screen.this, ActivityMain.class);
        intent.putExtra("FriendEmail",friendEmail);
        intent.putExtra("userMail", userEmail);
        startActivity(intent);
    }
}
