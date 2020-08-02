package com.mys3soft.mys3chat;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.firebase.client.Firebase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mys3soft.mys3chat.Models.Game;
import com.mys3soft.mys3chat.Models.Message;
import com.mys3soft.mys3chat.Models.StaticInfo;
import com.mys3soft.mys3chat.Models.User;
import com.mys3soft.mys3chat.Services.DataContext;
import com.mys3soft.mys3chat.Services.IFireBaseAPI;
import com.mys3soft.mys3chat.Services.LocalUserService;
import com.mys3soft.mys3chat.Services.Tools;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import retrofit2.Call;

/**
 * this is thr main of the app
 */

public class ActivityMain extends AppCompatActivity {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    private User user;
    Firebase refUser;
    private DataContext db;
    private ProgressDialog pd;
    private List<Message> userLastChatList;
    private List<User> userFriednList;

    public static Game game1;
    public static long gamesNum;
    private TextView not;
    public  static ArrayList<Game> games;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Firebase.setAndroidContext(this);

        db = new DataContext(this, null, null, 1);

        pd = new ProgressDialog(this);
        pd.setMessage("Refreshing...");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        // check if user exists in local db
        user = LocalUserService.getLocalUserFromPreferences(this);
        if (user.getEmail() == null) {
            // send to activitylogin
            Intent intent = new Intent(this, ActivityLogin.class);
            startActivityForResult(intent, 100);
        } else {
            startService(new Intent(this, AppService.class));
            if (refUser == null) {
                refUser = new Firebase(StaticInfo.UsersURL + "/" + user.getEmail());
            }
        }
        if (ContextCompat.checkSelfPermission(ActivityMain.this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(ActivityMain.this,
                    Manifest.permission.CAMERA)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(ActivityMain.this,
                        new String[]{Manifest.permission.CAMERA},
                        1);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            // Permission has already been granted
        }
        if (ContextCompat.checkSelfPermission(ActivityMain.this,
                Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(ActivityMain.this,
                    Manifest.permission.RECORD_AUDIO)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(ActivityMain.this,
                        new String[]{Manifest.permission.RECORD_AUDIO},
                        1);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            // Permission has already been granted
        }
        if (ContextCompat.checkSelfPermission(ActivityMain.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(ActivityMain.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(ActivityMain.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        1);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            // Permission has already been granted
        }
        not = (TextView)findViewById(R.id.textOne);
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference notref = database.getReference("friendrequests");
        notref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(user.getEmail()).exists()){
                    not.setVisibility(View.VISIBLE);
                    not.setText(String.valueOf(dataSnapshot.child(user.getEmail()).getChildrenCount()));
                    Log.d("notificaton",String.valueOf(dataSnapshot.child(user.getEmail()).getChildrenCount()) );

                }
                else{
                    not.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        DatabaseReference gameref= database.getReference("Game");
        gameref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<Game> gamesnow= new ArrayList<>();
                for(DataSnapshot post: dataSnapshot.getChildren()){
                    Game g = post.getValue(Game.class);
                    if(g!= null){
                        if(g.isActive()){
                            if(g.getPlayer0().equals(user.getEmail())|| g.getPlayer1().equals(user.getEmail())){
                                gamesnow.add(g);
                            }
                        }
                    }
                }
                games = gamesnow;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }


    @Override
    protected void onStart() {
        super.onStart();

        // check if user exists in local db
        user = LocalUserService.getLocalUserFromPreferences(this);
        if (user.getEmail() == null) {
            // send to activitylogin
            Intent intent = new Intent(this, ActivityLogin.class);
            startActivityForResult(intent, 100);
            return;
        }
        not = (TextView)findViewById(R.id.textOne);
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference notref = database.getReference("friendrequests");
        notref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(user.getEmail()).exists()){
                    not.setVisibility(View.VISIBLE);
                    not.setText(String.valueOf(dataSnapshot.child(user.getEmail()).getChildrenCount()));
                    Log.d("notificaton",String.valueOf(dataSnapshot.child(user.getEmail()).getChildrenCount()) );

                }
                else{
                    not.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        // refresh last chat
        userLastChatList = db.getUserLastChatList(user.getEmail());
        ListAdapter lastChatAdp = new AdapterLastChat(this, userLastChatList);
        final ListView lv_LastChatList = (ListView) findViewById(R.id.lv_LastChatList);
        if (lv_LastChatList != null) {
            lv_LastChatList.setAdapter(lastChatAdp);
            // reset listener

            lv_LastChatList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    if (userLastChatList.size() <= position) return false;
                    final Message selectedMessageItem = userLastChatList.get(position);
                    final CharSequence options[] = new CharSequence[]{"Delete Chat"};
                    AlertDialog.Builder builder = new AlertDialog.Builder(ActivityMain.this);
                    builder.setTitle(selectedMessageItem.FriendFullName);
                    builder.setItems(options, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int index) {
                            // the user clicked on list[index]
                            if (index == 0) {
                                // Delete Chat
                                new AlertDialog.Builder(ActivityMain.this)
                                        .setTitle(selectedMessageItem.FriendFullName)
                                        .setMessage("Are you sure to delete this chat?")
                                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                db.deleteChat(user.getEmail(), selectedMessageItem.FromMail);
                                                Toast.makeText(getApplicationContext(), "Chat deleted successfully", Toast.LENGTH_SHORT).show();
                                                userLastChatList = db.getUserLastChatList(user.getEmail());
                                                ListAdapter adp = new AdapterLastChat(getApplicationContext(), userLastChatList);
                                                lv_LastChatList.setAdapter(adp);
                                            }
                                        })
                                        .setNegativeButton(android.R.string.no, null)
                                        .show();
                            }
                        }
                    });

                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    builder.show();
                    return true;
                }
            });

        }

        // refresh contacts
        userFriednList = db.getUserFriendList();
        ListAdapter adp = new FriendListAdapter(this, userFriednList);
        final ListView lv_FriendList = (ListView) findViewById(R.id.lv_FriendList);
        if (lv_FriendList != null) {
            lv_FriendList.setAdapter(adp);
            lv_FriendList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    if (userFriednList.size() <= position) return false;
                    final User selectedUser = userFriednList.get(position);
                    final CharSequence options[] = new CharSequence[]{"Profile", "Delete Contact"};
                    AlertDialog.Builder builder = new AlertDialog.Builder(ActivityMain.this);
                    builder.setTitle(selectedUser.getFirstName() + " " + selectedUser.getLastName());
                    builder.setItems(options, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int index) {
                            // the user clicked on list[index]
                            if (index == 0) {
                                // Profile
                                Intent intent = new Intent(ActivityMain.this, ActivityFriendProfile.class);
                                intent.putExtra("Email", selectedUser.getEmail());
                                startActivityForResult(intent, StaticInfo.ChatAciviityRequestCode);
                            } else {
                                // Delete Contact
                                new AlertDialog.Builder(ActivityMain.this)
                                        .setTitle(selectedUser.getFirstName() + " " + selectedUser.getLastName())
                                        .setMessage("Are you sure to delete this contact?")
                                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                Firebase ref = new Firebase(StaticInfo.EndPoint + "/friends/" + user.getEmail() + "/" + selectedUser.getEmail());
                                                ref.removeValue();
                                                // delete from local database
                                                db.deleteFriendByEmailFromLocalDB(selectedUser.getEmail());
                                                Toast.makeText(ActivityMain.this, "Contact deleted successfully", Toast.LENGTH_SHORT).show();
                                                userFriednList = db.getUserFriendList();
                                                ListAdapter adp = new FriendListAdapter(ActivityMain.this, userFriednList);
                                                lv_FriendList.setAdapter(adp);
                                            }
                                        })
                                        .setNegativeButton(android.R.string.no, null)
                                        .show();
                            }
                        }
                    });

                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

                    builder.show();


                    return true;
                }
            });
        }
        // set online status
        user = LocalUserService.getLocalUserFromPreferences(this);
        if (user.getEmail() != null) {
            if (refUser == null) {
                refUser = new Firebase(StaticInfo.UsersURL + "/" + user.getEmail());
            }
        }
        if (refUser != null)
            refUser.child("Status").setValue("Online");

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // set last seen
        DateFormat dateFormat = new SimpleDateFormat("dd MM yy hh:mm a");
        Date date = new Date();
        refUser.child("Status").setValue(dateFormat.format(date));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    // logout button in the main screen
    public void logout_button(View view) {
        new AlertDialog.Builder(this)
                .setTitle("Logout?")
                .setMessage("Are you sure to logout, you will no longer receive notifications.")
                .setPositiveButton("Logout", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // set last seen
                        DateFormat dateFormat = new SimpleDateFormat("dd MM yy hh:mm a");
                        Date date = new Date();
                        refUser.child("Status").setValue(dateFormat.format(date));
                        if (LocalUserService.deleteLocalUserFromPreferences(getApplicationContext())) {
                            db.deleteAllFriendsFromLocalDB();
                            // stopService(new Intent(getApplicationContext(), AppService.class));
                            Toast.makeText(getApplicationContext(), "Logout Success", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), ActivityLogin.class);
                            startActivityForResult(intent, 100);
                        } else {
                            Toast.makeText(getApplicationContext(), "Logout Success", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), ActivityLogin.class);
                            startActivityForResult(intent, 100);
                        }
                    }
                })
                .setNegativeButton(android.R.string.no, null)
                .show();
    }
    // add contact button in the main screen
    public void add_contact_button(View view){
        startActivity(new Intent(this, ActivityAddContact.class));

    }
    // requests friend button in the main screen
    public void request_friend_button(View view){
        startActivity(new Intent(this, ActivityNotifications.class));
    }
    // score board button in the main screen
    public void score_board(View view){
        startActivity(new Intent(this, score_board.class));
    }
    // profile button in the main screen
    public void profile_button(View view){
        Intent intent = new Intent(this, ActivityProfile.class);
        intent.putExtra("email", user.getEmail());
        startActivity(intent);

    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_refresh) {

            if (Tools.isNetworkAvailable(this)) {

                FriendListTask t = new FriendListTask();
                t.execute();
            } else {
                Toast.makeText(this, "Please check your internet connection.", Toast.LENGTH_SHORT).show();
            }

        }

        return super.onOptionsItemSelected(item);
    }

    public static class PlaceholderFragment extends Fragment {

        private static final String ARG_SECTION_NUMBER = "section_number";
        private View rootView;
        private DataContext db;
        User user;
        private List<User> userFriendList;
        Bundle extras;
        ListView lv_FriendList;


        public PlaceholderFragment() {
        }


        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }


    @Override
        public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            if (user == null) {
                user = LocalUserService.getLocalUserFromPreferences(getActivity());
            }
            db = new DataContext(getActivity(), null, null, 1);
            rootView = inflater.inflate(R.layout.fragment_contact, container, false);
            userFriendList = db.getUserFriendList();
            ListAdapter adp = new FriendListAdapter(getActivity(), userFriendList);
            lv_FriendList = (ListView) rootView.findViewById(R.id.lv_FriendList);
            lv_FriendList.setAdapter(adp);
            lv_FriendList.setOnItemClickListener(
                    new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            TextView email = (TextView) view.findViewById(R.id.tv_HiddenEmail);
                            Intent intent= new Intent(getActivity(),wait_before_start.class);
                            intent.putExtra("userEmail", user.getEmail());
                            intent.putExtra("friendEmail", email.getText().toString());
                            startActivity(intent);
                        }
                    }

            );

        lv_FriendList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    if (userFriendList.size() <= position) return false;
                    final User selectedUser = userFriendList.get(position);
                    final CharSequence options[] = new CharSequence[]{"Profile", "Delete Contact"};
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle(selectedUser.getEmail() + " " + selectedUser.getEmail());
                    builder.setItems(options, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int index) {
                            // the user clicked on list[index]
                            if (index == 0) {
                                // Profile
                                Intent intent = new Intent(getActivity(), ActivityFriendProfile.class);
                                intent.putExtra("Email", selectedUser.getEmail());
                                startActivityForResult(intent, StaticInfo.ChatAciviityRequestCode);
                            } else {
                                // Delete Contact
                                new AlertDialog.Builder(getActivity())
                                        .setTitle(selectedUser.getEmail() + " " + selectedUser.getEmail())
                                        .setMessage("Are you sure to delete this contact?")
                                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                Firebase ref = new Firebase(StaticInfo.EndPoint + "/friends/" + user.getEmail() + "/" + selectedUser.getEmail());
                                                ref.removeValue();
                                                // delete from local database
                                                db.deleteFriendByEmailFromLocalDB(selectedUser.getEmail());
                                                Toast.makeText(getActivity(), "Contact deleted successfully", Toast.LENGTH_SHORT).show();
                                                userFriendList = db.getUserFriendList();
                                                ListAdapter adp = new FriendListAdapter(getActivity(), userFriendList);
                                                ListView lv_FriendList = (ListView) rootView.findViewById(R.id.lv_FriendList);
                                                lv_FriendList.setAdapter(adp);
                                            }
                                        })
                                        .setNegativeButton(android.R.string.no, null)
                                        .show();
                            }
                        }
                    });

                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

                    builder.show();


                    return true;
                }
            });
            return rootView;
        }
    }


    public class SectionsPagerAdapter extends FragmentPagerAdapter {


        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "CONTACTS";
        }
    }

    public class FriendListTask extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            pd.show();
        }

        @Override
        protected String doInBackground(Void... params) {
            user = LocalUserService.getLocalUserFromPreferences(getApplicationContext());
            IFireBaseAPI api = Tools.makeRetroFitApi();
            Call<String> call = api.getUserFriendsListAsJsonString(StaticInfo.FriendsURL + "/" + user.getEmail() + ".json");
            try {
                return call.execute().body();
            } catch (IOException e) {
                e.printStackTrace();
                pd.hide();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String jsonListString) {

            try {
                user = LocalUserService.getLocalUserFromPreferences(getApplicationContext());
                List<User> friendList = new ArrayList<>();
                JSONObject userFriendTree = new JSONObject(jsonListString);
                for (Iterator iterator = userFriendTree.keys(); iterator.hasNext(); ) {
                    String key = (String) iterator.next();
                    User friend = new User();
                    JSONObject friendJson = userFriendTree.getJSONObject(key);
                    friend.setEmail( friendJson.getString("Email"));
                    friend.setFirstName(friendJson.getString("FirstName"));
                    friend.setLastName(friendJson.getString("LastName"));
                    friendList.add(friend);
                }

                // refresh local database
                db = new DataContext(getApplicationContext(), null, null, 1);
                db.refreshUserFriendList(friendList);

                // set to adapter
                ListAdapter adp = new FriendListAdapter(getApplicationContext(), db.getUserFriendList());
                ListView lv_FriendList = (ListView) findViewById(R.id.lv_FriendList);
                lv_FriendList.setAdapter(adp);
                pd.hide();
                Toast.makeText(ActivityMain.this, "Contact list is updated", Toast.LENGTH_SHORT).show();
            } catch (JSONException e) {
                pd.hide();
                e.printStackTrace();
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        user = LocalUserService.getLocalUserFromPreferences(this);
        if (requestCode == 100 && resultCode == Activity.RESULT_OK) {
            if (refUser == null) {
                refUser = new Firebase(StaticInfo.UsersURL + "/" + user.getEmail());
            }
            startService(new Intent(getApplicationContext(), AppService.class));
            FriendListTask t = new FriendListTask();
            t.execute();
        }
    }


}
