package com.mys3soft.mys3chat;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.firebase.client.Firebase;
import com.mys3soft.mys3chat.Models.StaticInfo;
import com.mys3soft.mys3chat.Models.User;
import com.mys3soft.mys3chat.Services.DataContext;
import com.mys3soft.mys3chat.Services.IFireBaseAPI;
import com.mys3soft.mys3chat.Services.LocalUserService;
import com.mys3soft.mys3chat.Services.Tools;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;

/**
 * this class showing a friend profile and enable to add this friend to user friend lists
 */

public class ActivityFriendProfile extends AppCompatActivity {

    String friendEmail;
    TextView tv_FriendFullName;
    User user, f;
    ProgressDialog pd;
    Button btn_AddFriend;
    DataContext db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_profile);
        Firebase.setAndroidContext(this);
        btn_AddFriend = (Button) findViewById(R.id.btn_AddFriend);
        pd = new ProgressDialog(this);

        pd.setMessage("Loading...");
        f = new User();
        Bundle extras = getIntent().getExtras();
        friendEmail = extras.getString("Email");
        tv_FriendFullName = (TextView) findViewById(R.id.tv_FriendFullName_L_FriendProfile);

        user = LocalUserService.getLocalUserFromPreferences(this);

        db = new DataContext(this, null, null, 1);

        // check if already friends otherwise get info from server
        User friend = db.getFriendByEmailFromLocalDB(friendEmail);
        if (friend.getEmail() == null) {
            ImageButton btn_EditName = (ImageButton) findViewById(R.id.btn_EditName);
            btn_EditName.setVisibility(View.INVISIBLE);
            FindFriendsTask t = new FindFriendsTask();
            t.execute();
        } else {
            tv_FriendFullName.setText(Tools.toProperName(friend.getFirstName()) + " " + Tools.toProperName(friend.getLastName()));
            btn_AddFriend.setEnabled(false);
            btn_AddFriend.setText("Connected");


        }


    }


    public class FindFriendsTask extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            pd.show();
        }

        @Override
        protected String doInBackground(Void... params) {

            IFireBaseAPI api = Tools.makeRetroFitApi();
            Call<String> call = api.getAllUsersAsJsonString();

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
                JSONObject jsonObjectList = new JSONObject(jsonListString);
                JSONObject item = jsonObjectList.getJSONObject(friendEmail);
                f.setEmail(item.getString("Email"));
                f.setFirstName(item.getString("FirstName"));
                f.setLastName(item.getString("LastName"));
                tv_FriendFullName.setText(Tools.toProperName(f.getFirstName()) + " " + Tools.toProperName(f.getLastName()));
                pd.hide();
            } catch (JSONException e1) {
                e1.printStackTrace();
                pd.hide();
            }
        }
    }

    public void btn_SendFriendRequestClick(View view) {
        Firebase firebase = new Firebase("https://chat-example-f10a5.firebaseio.com/friendrequests");
        Firebase notifRef = new Firebase(StaticInfo.NotificationEndPoint + "/" + friendEmail);
        Map<String, String> map = new HashMap<>();
        map.put("FirstName", user.getFirstName());
        map.put("LastName", user.getLastName());
        firebase.child(Tools.encodeString(f.getEmail())).child(Tools.encodeString(user.getEmail())).setValue(map);
        btn_AddFriend.setEnabled(false);
        btn_AddFriend.setText("Request Sent");

        map.put("SenderEmail", user.getEmail());
        map.put("Message", "Pending contact request");
        map.put("NotificationType", "2");

        notifRef.push().setValue(map);

    }

    public void btn_EditNameClick(View view) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Set display name");
        final EditText et = new EditText(this);
        et.setText(tv_FriendFullName.getText());
        et.setInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        builder.setView(et);
        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String newName = et.getText().toString();
                db.setPreferedDisplayName(friendEmail, newName);
                tv_FriendFullName.setText(newName);
                setResult(Activity.RESULT_OK);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();

    }

}
