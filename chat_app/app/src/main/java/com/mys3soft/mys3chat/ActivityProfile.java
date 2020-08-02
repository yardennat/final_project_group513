package com.mys3soft.mys3chat;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mys3soft.mys3chat.Models.User;
import com.mys3soft.mys3chat.Services.DataContext;
import com.mys3soft.mys3chat.Services.LocalUserService;
import com.mys3soft.mys3chat.Services.Tools;

/**
 * this class is present the profile user
 */
public class ActivityProfile extends AppCompatActivity {

    DataContext db = new DataContext(this, null, null, 1);
    TextView score;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        final User user = LocalUserService.getLocalUserFromPreferences(this);
        TextView tv_UserFullName = (TextView) findViewById(R.id.tv_UserFullName);
        tv_UserFullName.setText(Tools.toProperName(user.getFirstName()) + " " + Tools.toProperName(user.getLastName()));

        score= (TextView) findViewById(R.id.score_box1);

        FirebaseDatabase.getInstance().getReference("users").child(user.getEmail()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User u = dataSnapshot.getValue(User.class);
                if(u!= null){
                    score.setText(String.valueOf(u.getScore()));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
}
