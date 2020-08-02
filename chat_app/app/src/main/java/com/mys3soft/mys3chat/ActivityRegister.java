package com.mys3soft.mys3chat;


import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.firebase.client.Firebase;
import com.mys3soft.mys3chat.Models.StaticInfo;
import com.mys3soft.mys3chat.Services.IFireBaseAPI;
import com.mys3soft.mys3chat.Services.Tools;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import retrofit2.Call;

/**
 * this class is responsible to register a new user to the game
 */
public class ActivityRegister extends AppCompatActivity {


    EditText et_Email, et_Password, et_FirstName, et_LastName;
    Button btn_Register;
    ProgressDialog pd;
    Spinner dynamicSpinner;
    String email;
    Set<String> lang = new HashSet<>();
    public static String ans;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Firebase.setAndroidContext(this);

        pd = new ProgressDialog(this);
        pd.setMessage("Loading...");

        et_Email = (EditText) findViewById(R.id.et_Email_Rigister);
        et_Password = (EditText) findViewById(R.id.et_Password_Rigister);
        et_FirstName = (EditText) findViewById(R.id.et_FirstName_Rigister);
        et_LastName = (EditText) findViewById(R.id.et_LastName_Rigister);
        dynamicSpinner = (Spinner) findViewById(R.id.spinner);
        String[] items = new String[] { "Male", "Female", "None of your business" };
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, items);

        dynamicSpinner.setAdapter(adapter);

        dynamicSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                ans = (String) parent.getItemAtPosition(position);
                Log.v("item", (String) parent.getItemAtPosition(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });

    }
    public void onCheckboxClicked(View view) {
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();

        // Check which checkbox was clicked
        switch(view.getId()) {
            case R.id.checkbox_hebrew:
                if (checked){
                    lang.add("Hebrew");
                }
                break;
            case R.id.checkbox_english:
                if (checked){
                    lang.add("English");

                }
                break;
            case R.id.checkbox_spanish:
                if (checked){
                    lang.add("Spanish");
                }
                break;

        }

    }


    public void btn_RegClick(View view) {
        if (!Tools.isNetworkAvailable(this)) {
            Toast.makeText(this, "Please check your internet connection.", Toast.LENGTH_SHORT).show();
        } else if (et_FirstName.getText().toString().equals("")) {
            et_FirstName.setError("Enter Firstname");
        } else if (et_LastName.getText().toString().equals("")) {
            et_LastName.setError("Enter Lastname");
        } else if (et_Email.getText().toString().equals("") || !Tools.isValidEmail(et_Email.getText().toString().toLowerCase())) {
            et_Email.setError("Enter Valid Email");
        } else if (et_Password.getText().toString().equals("")) {
            et_Password.setError("Enter Password");
        }else if(lang.isEmpty()){
            Toast.makeText(ActivityRegister.this, "Error:You have to choose at least one language",Toast.LENGTH_SHORT).show();
        }else {
            email = Tools.encodeString(et_Email.getText().toString().toLowerCase());
            RegisterUserTask t = new RegisterUserTask();
            t.execute();
        }
    }

    public class RegisterUserTask extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            pd.show();
        }

        @Override
        protected String doInBackground(Void... params) {

            IFireBaseAPI api = Tools.makeRetroFitApi();
            Call<String> call = api.getSingleUserByEmail(StaticInfo.UsersURL + "/" + email + ".json");
            try {
                return call.execute().body();
            } catch (IOException e) {
                e.printStackTrace();
                pd.hide();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String jsonString) {
            try {
                if (jsonString.trim().equals("null")) {
                    Firebase firebase = new Firebase(StaticInfo.UsersURL);
                    firebase.child(email).child("FirstName").setValue(et_FirstName.getText().toString());
                    firebase.child(email).child("LastName").setValue(et_LastName.getText().toString());
                    firebase.child(email).child("Email").setValue(email);
                    firebase.child(email).child("Password").setValue(et_Password.getText().toString());
                    firebase.child(email).child("language").setValue(lang);
                    firebase.child(email).child("score").setValue(0);
                    firebase.child(email).child("gender").setValue(ans);
                    DateFormat dateFormat = new SimpleDateFormat("dd MM yy hh:mm a");
                    Date date = new Date();
                    firebase.child(email).child("Status").setValue(dateFormat.format(date));
                    Toast.makeText(getApplicationContext(), "Signup Success", Toast.LENGTH_SHORT).show();
                    pd.hide();
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "Email already exists", Toast.LENGTH_SHORT).show();
                    pd.hide();
                }
            } catch (Exception e) {
                pd.hide();
                e.printStackTrace();
            }
        }
    }


}

