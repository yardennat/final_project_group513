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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;


// this class are deciding by the Game Object where the player supposed to be in the Game
public class wait_before_start extends AppCompatActivity {
    Game game1;
    long gamesNum =0;
    Bundle extras;
    String user1;
    String friend1;
    boolean stop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wait_before_start);
        stop= false;

        extras =  getIntent().getExtras();
        if(extras!= null){
            user1 = extras.getString("userEmail");
            friend1= extras.getString("friendEmail");
            if(user1!= null && friend1!= null){
                if(!friend1.equals(user1)){
                    find_game(user1, friend1);

                }
                else {
                    Intent intent = new Intent(wait_before_start.this, ActivityMain.class);
                    startActivity(intent);
                }

            }else{
                Intent intent = new Intent(wait_before_start.this, ActivityMain.class);
                startActivity(intent);
            }

            }else{
            Intent intent = new Intent(wait_before_start.this, ActivityMain.class);
            startActivity(intent);
        }

    }

    void find_game(final String user, final String friend){
        game1 = null;
        gamesNum=0;
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference gameref = database.getReference("Game");

        gameref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                gamesNum = dataSnapshot.getChildrenCount();
                boolean find = false;
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Game g = postSnapshot.getValue(Game.class);

                    if(g!= null) {
                        if(g.isActive()){
                            if ((g.getPlayer0().equals(friend) && g.getPlayer1().equals(user)) || (g.getPlayer0().equals(user) && g.getPlayer1().equals(friend))) {
                                game1 = g;
                                find = true;
                                Log.d("main", String.valueOf(game1.getId()));
                                break;
                            }
                        }
                    }

                }
                // if not found active game we create
                if(!find){
                    DatabaseReference gameref1 =database.getReference("Game");
                    ArrayList<String> categories = new ArrayList<>();
                    categories.addAll(Arrays.asList("on the roads", "family", "trips", "roommates", "food", "friends", "going out", "childhood", "dreams", "dating", "neighbors"));
                    int ran_i = new Random().nextInt((int) categories.size());
                    game1 = new Game(user, friend, 0, 0, categories.get(ran_i), "hebrew", true,(int)(gamesNum + 1));
                    gameref1.child(String.valueOf(gamesNum + 1)).child("player0").setValue(game1.getPlayer0());
                    gameref1.child(String.valueOf(gamesNum + 1)).child("player1").setValue(game1.getPlayer1());
                    gameref1.child(String.valueOf(gamesNum + 1)).child("numOfRounds").setValue(game1.getNumOfRounds());
                    gameref1.child(String.valueOf(gamesNum + 1)).child("numOfSuccessfulFooling").setValue(game1.getNumOfSuccessfulFooling());
                    gameref1.child(String.valueOf(gamesNum + 1)).child("category").setValue(game1.getCategory());
                    gameref1.child(String.valueOf(gamesNum + 1)).child("lang").setValue(game1.getLang());
                    gameref1.child(String.valueOf(gamesNum + 1)).child("active").setValue(game1.isActive());
                    gameref1.child(String.valueOf(gamesNum + 1)).child("id").setValue(gamesNum+1);
                    gameref1.child(String.valueOf(gamesNum + 1)).child("wantContP0").setValue(0);
                    gameref1.child(String.valueOf(gamesNum + 1)).child("wantContP1").setValue(0);
                    gameref1.child(String.valueOf(gamesNum + 1)).child("screen_player_0").setValue(0);
                    gameref1.child(String.valueOf(gamesNum + 1)).child("screen_player_1").setValue(0);
                    gameref1.child(String.valueOf(gamesNum + 1)).child("score_player0").setValue(0);
                    gameref1.child(String.valueOf(gamesNum + 1)).child("score_player1").setValue(0);
                    gameref1.child(String.valueOf(gamesNum + 1)).child("m0").setValue("");
                    gameref1.child(String.valueOf(gamesNum + 1)).child("m1").setValue("");}
                Intent intend;

                // if its new game send the players to the right screen
                if (game1.getPlayer0().equals(user) &&( game1.getScreen_player_0() ==0|| game1.getScreen_player_0() ==1)&& !stop){
                        stop = true;
                        intend = new Intent(wait_before_start.this, show_category_player_filming.class);
                        intend.putExtra("FriendEmail", friend);
                        intend.putExtra("userMail", user);
                        intend.putExtra("category", game1.getCategory());
                        intend.putExtra("score",0);
                        intend.putExtra("id", game1.getId());
                        intend.putExtra("player",0);
                        startActivity(intend);
                }
                else if (game1.getPlayer1().equals(user) && (game1.getScreen_player_1() ==0|| game1.getScreen_player_1()== 12)&& !stop) {
                        stop = true;
                        intend = new Intent(wait_before_start.this, show_category_for_player_guess.class);//screen show category
                        intend.putExtra("FriendEmail", friend);
                        intend.putExtra("userMail", user);
                        intend.putExtra("category", game1.getCategory());
                        intend.putExtra("score",0);
                        intend.putExtra("id", game1.getId());
                        intend.putExtra("player",1);
                        startActivity(intend);
                }

                // if we are in the middle of the game send the players to the right place
                else if(game1.getPlayer1().equals(user) && !stop){
                    stop = true;
                    Intent intent;
                    switch (game1.getScreen_player_1()){
                        case 2: //film ans send video
                            intent = new Intent(wait_before_start.this,film_and_send_video.class);
                            intent.putExtra("FriendEmail", friend);
                            intent.putExtra("userMail", user);
                            intent.putExtra("category", game1.getCategory());
                            intent.putExtra("score",game1.getScore_player1());
                            intent.putExtra("id", game1.getId());
//                            intent.putExtra("numOfRounds", game1.getNumOfRounds());
                            intent.putExtra("message ", game1.getM1());
                            intent.putExtra("player",1);
                            startActivity(intent);
                            break;
                        case 3: //wait guess
                            intent = new Intent(wait_before_start.this,waiting_guess.class);
                            intent.putExtra("FriendEmail", friend);
                            intent.putExtra("userMail", user);
                            intent.putExtra("category", game1.getCategory());
                            intent.putExtra("score",game1.getScore_player1());
                            intent.putExtra("id", game1.getId());
                            intent.putExtra("numRounds", game1.getNumOfRounds());
                            intent.putExtra("player",1);
                            startActivity(intent);
                            break;
                        case 4: //result player filming
                            intent = new Intent(wait_before_start.this,result_player_filming.class);
                            intent.putExtra("FriendEmail", friend);
                            intent.putExtra("userMail", user);
                            intent.putExtra("category", game1.getCategory());
                            intent.putExtra("score",game1.getScore_player1());
                            intent.putExtra("succeed",0);
                            intent.putExtra("numRounds", game1.getNumOfRounds());
                            intent.putExtra("txt", game1.getM1());
                            intent.putExtra("id", game1.getId());
                            intent.putExtra("player",1);
                            startActivity(intent);
                            break;
                        case 5: //wait response video
                            intent = new Intent(wait_before_start.this,waiting_response_video.class);
                            intent.putExtra("FriendEmail", friend);
                            intent.putExtra("userMail", user);
                            intent.putExtra("category", game1.getCategory());
                            intent.putExtra("score",game1.getScore_player1());
                            intent.putExtra("id", game1.getId());
                            intent.putExtra("player",1);
                            startActivity(intent);
                            break;
                        case 6: //guessing screen
                            intent = new Intent(wait_before_start.this,gusseing_screen.class);
                            intent.putExtra("FriendEmail", friend);
                            intent.putExtra("userMail", user);
                            intent.putExtra("category", game1.getCategory());
                            intent.putExtra("score",game1.getScore_player1());
                            intent.putExtra("id", game1.getId());
                            intent.putExtra("player",1);
                            startActivity(intent);
                            break;

                        case 8: //result player guessing
                            intent = new Intent(wait_before_start.this,result_player_guess.class);
                            intent.putExtra("FriendEmail", friend);
                            intent.putExtra("userMail", user);
                            intent.putExtra("category", game1.getCategory());
                            intent.putExtra("score",game1.getScore_player1());
                            intent.putExtra("message", game1.getM1());
                            intent.putExtra("id", game1.getId());
                            intent.putExtra("numOfRounds", game1.getNumOfRounds());
                            intent.putExtra("player",1);
                            startActivity(intent);
                            break;
                        case 9: //give up or continue
                            intent = new Intent(wait_before_start.this,give_up_or_continue.class);
                            intent.putExtra("FriendEmail", friend);
                            intent.putExtra("userMail", user);
                            intent.putExtra("category", game1.getCategory());
                            intent.putExtra("score",game1.getScore_player1());
                            intent.putExtra("id", game1.getId());
                            intent.putExtra("player",1);
                            startActivity(intent);
                            break;
                        case 10: //wait for answer continue
                            intent = new Intent(wait_before_start.this,process_continue_game.class);
                            intent.putExtra("FriendEmail", friend);
                            intent.putExtra("userMail", user);
                            intent.putExtra("category", game1.getCategory());
                            intent.putExtra("score",game1.getScore_player1());
                            intent.putExtra("id", game1.getId());
                            intent.putExtra("player",1);
                            startActivity(intent);
                            break;
                    }
                }
                else{
                    if(game1.getPlayer0().equals(user)&& !stop){
                        Intent intent;
                        stop = true;
                        switch (game1.getScreen_player_0()){
                            case 2: //film ans send video
                                intent = new Intent(wait_before_start.this,film_and_send_video.class);
                                intent.putExtra("FriendEmail", friend);
                                intent.putExtra("userMail", user);
                                intent.putExtra("category", game1.getCategory());
                                intent.putExtra("score",game1.getScore_player0());
                                intent.putExtra("id", game1.getId());
//                                intent.putExtra("numOfRounds", game1.getNumOfRounds());
                                intent.putExtra("message ", game1.getM0());
                                intent.putExtra("player",0);
                                startActivity(intent);
                                break;
                            case 3: //wait guess
                                intent = new Intent(wait_before_start.this,waiting_guess.class);
                                intent.putExtra("FriendEmail", friend);
                                intent.putExtra("userMail", user);
                                intent.putExtra("category", game1.getCategory());
                                intent.putExtra("score",game1.getScore_player0());
                                intent.putExtra("id", game1.getId());
                                intent.putExtra("numRounds", game1.getNumOfRounds());
                                intent.putExtra("player",0);
                                startActivity(intent);
                                break;
                            case 4: //result player filming
                                intent = new Intent(wait_before_start.this,result_player_filming.class);
                                intent.putExtra("FriendEmail", friend);
                                intent.putExtra("userMail", user);
                                intent.putExtra("category", game1.getCategory());
                                intent.putExtra("score",game1.getScore_player0());
                                intent.putExtra("succeed",0);
                                intent.putExtra("numRounds", game1.getNumOfRounds());
                                intent.putExtra("txt", game1.getM0());
                                intent.putExtra("id", game1.getId());
                                intent.putExtra("player",0);
                                startActivity(intent);
                                break;
                            case 5: //wait response video
                                intent = new Intent(wait_before_start.this,waiting_response_video.class);
                                intent.putExtra("FriendEmail", friend);
                                intent.putExtra("userMail", user);
                                intent.putExtra("category", game1.getCategory());
                                intent.putExtra("score",game1.getScore_player0());
                                intent.putExtra("id", game1.getId());
                                intent.putExtra("player",0);
                                startActivity(intent);
                                break;
                            case 6: //guessing screen
                                intent = new Intent(wait_before_start.this,gusseing_screen.class);
                                intent.putExtra("FriendEmail", friend);
                                intent.putExtra("userMail", user);
                                intent.putExtra("category", game1.getCategory());
                                intent.putExtra("score",game1.getScore_player0());
                                intent.putExtra("id", game1.getId());
                                intent.putExtra("player",0);
                                startActivity(intent);
                                break;
                            case 8: //result player guessing
                                intent = new Intent(wait_before_start.this,result_player_guess.class);
                                intent.putExtra("FriendEmail", friend);
                                intent.putExtra("userMail", user);
                                intent.putExtra("category", game1.getCategory());
                                intent.putExtra("score",game1.getScore_player0());
                                intent.putExtra("message", game1.getM0());
                                intent.putExtra("id", game1.getId());
                                intent.putExtra("numOfRounds", game1.getNumOfRounds());
                                intent.putExtra("player",0);
                                startActivity(intent);
                                break;
                            case 9: //give up or continue
                                intent = new Intent(wait_before_start.this,give_up_or_continue.class);
                                intent.putExtra("FriendEmail", friend);
                                intent.putExtra("userMail", user);
                                intent.putExtra("category", game1.getCategory());
                                intent.putExtra("score",game1.getScore_player0());
                                intent.putExtra("id", game1.getId());
                                intent.putExtra("player",0);
                                startActivity(intent);
                                break;
                            case 10: //wait for answer continue
                                intent = new Intent(wait_before_start.this,process_continue_game.class);
                                intent.putExtra("FriendEmail", friend);
                                intent.putExtra("userMail", user);
                                intent.putExtra("category", game1.getCategory());
                                intent.putExtra("score",game1.getScore_player0());
                                intent.putExtra("id", game1.getId());
                                intent.putExtra("player",0);
                                startActivity(intent);
                                break;
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
