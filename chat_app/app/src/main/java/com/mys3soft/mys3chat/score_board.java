package com.mys3soft.mys3chat;

import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mys3soft.mys3chat.Models.User;
import com.mys3soft.mys3chat.Services.DataContext;
import com.mys3soft.mys3chat.Services.LocalUserService;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * this activity is the score board
 */
public class score_board extends AppCompatActivity {

    private DataContext db;
    private TableLayout t;
    private HashMap<String,Integer> scores;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_board);

        db = new DataContext(this, null, null, 1);
        final User user= LocalUserService.getLocalUserFromPreferences(this);
        final List<User> friend = db.getUserFriendList();
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("users");
        t = findViewById(R.id.table_score);
        t.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        scores = new HashMap<String, Integer>();

        TableRow tr_head = new TableRow(this);
        tr_head.setLayoutParams(new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));

        TextView label_name = new TextView(this);
        label_name.setText("NAME");
        label_name.setGravity(Gravity.CENTER);
        label_name.setPadding(5, 5, 5, 5);
        label_name.setTextSize(20);
        tr_head.addView(label_name);// add the column to the table row here

        TextView label_score = new TextView(this);
        label_score.setText("SCORE");// set the text for the header
        label_score.setGravity(Gravity.CENTER);
        label_score.setTextSize(20);
        label_score.setPadding(5, 5, 5, 5); // set the padding (if required)
        tr_head.addView(label_score); // add the column to the table row here

        t.addView(tr_head,new TableLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));


        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (User f : friend) {
                    for (DataSnapshot post : dataSnapshot.getChildren()) {
                        User u = post.getValue(User.class);
                        if (u != null) {
                            if (u.getEmail().equals(f.getEmail())) {
                                scores.put(f.getFirstName() + " " + f.getLastName(),u.getScore()*(-1));
                                break;
                            }
                        }
                    }
                }
                for (DataSnapshot post : dataSnapshot.getChildren()) {
                    User u = post.getValue(User.class);
                    if(u!= null){
                        if(u.getEmail().equals(user.getEmail())){
                            scores.put("you", u.getScore()*(-1));
                            break;
                        }
                    }
                }
                Map<String, Integer> hm1 = sortByValue(scores);
                for(Map.Entry<String, Integer> en : hm1.entrySet()){
                    TableRow row = new TableRow(score_board.this);
                    TextView name = new TextView(score_board.this);
                    String s =en.getKey();
                    name.setText(s);
                    name.setTextSize(18);
                    name.setGravity(Gravity.CENTER);
                    name.setPadding(5, 5, 5,5);
                    TextView score = new TextView(score_board.this);
                    score.setText(String.valueOf(en.getValue()*(-1)));
                    score.setGravity(Gravity.CENTER);
                    score.setTextSize(18);
                    score.setPadding(5, 5, 5,5);
                    row.addView(name);
                    row.addView(score);

                    t.addView(row,new TableLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT));


                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    public static HashMap<String, Integer> sortByValue(HashMap<String, Integer> hm)
    {
        // Create a list from elements of HashMap
        List<Map.Entry<String, Integer> > list = new LinkedList<Map.Entry<String, Integer> >(hm.entrySet());

        // Sort the list
        Collections.sort(list, new Comparator<Map.Entry<String, Integer> >() {
            public int compare(Map.Entry<String, Integer> o1,
                               Map.Entry<String, Integer> o2)
            {
                return (o1.getValue()).compareTo(o2.getValue());
            }
        });

        // put data from sorted list to hashmap
        HashMap<String, Integer> temp = new LinkedHashMap<String, Integer>();
        for (Map.Entry<String, Integer> aa : list) {
            temp.put(aa.getKey(), aa.getValue());
        }
        return temp;
    }

}