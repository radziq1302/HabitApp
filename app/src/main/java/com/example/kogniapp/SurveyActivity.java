package com.example.kogniapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

public class SurveyActivity extends AppCompatActivity {
    private Button sendDb;
    private String newString;
    private DatabaseReference dtbase;
    private String TAG = "Survey69";
    private String id = "1";
    private long numberId;
    private boolean doubleClick = false;
    private static final int TIME_INTERVAL = 2000;
    private long startTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey);
        sendDb = findViewById(R.id.test_firebase);

        // przeslanie danych z MainActivity
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                newString= null;
            } else {
                newString= extras.getString("email");
            }
        } else {
            newString= (String) savedInstanceState.getSerializable("email");
        }

        // Sprawdzenie numeru z bazy, jeśli pierwszy raz, to nadanie id
        FirebaseDatabase database;
        database = FirebaseDatabase.getInstance();
        dtbase=database.getReference();
        DatabaseReference userCounter = dtbase.child("users");
        userCounter.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    numberId = (dataSnapshot.getChildrenCount());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
/*        DatabaseReference uzytkownik = dtbase.child("users").child(String.valueOf(numberId));
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(!dataSnapshot.exists()) {
                    //create new user
                    User user = new User(numberId+1,newString);
                    dtbase.child("users").child(String.valueOf(numberId+1)).setValue(user);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, databaseError.getMessage()); //Don't ignore errors!
            }
        };
        uzytkownik.addListenerForSingleValueEvent(eventListener);*/

        //druga opcja
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("users");
        ref.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        //Get map of users in datasnapshot
                        Log.v(TAG, dataSnapshot.getValue()+"");
                        checkEmail((Map<String,Object>) dataSnapshot.getValue(),newString);//,newString);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        //handle databaseError
                    }
                });

        sendDb.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                //DatabaseReference myRef = database.getReference("message1");

                //myRef.setValue("świnka test");

                Survey survey = new Survey("1","tak","mało");
                dtbase.child("testś").child("id").setValue(survey);


                //Intent i = new Intent(getApplicationContext(), SurveyActivity.class);
                //startActivity(i);

            }
        });

    }
    @Override
    public void onBackPressed()
    {
        if (startTime + TIME_INTERVAL > System.currentTimeMillis())
        {
            super.onBackPressed();
            android.os.Process.killProcess(android.os.Process.myPid());
            super.finish();
            //return;
        }
        else { Toast.makeText(getBaseContext(), "Naciśnij ponownie, by wyjść", Toast.LENGTH_SHORT).show(); }

        startTime = System.currentTimeMillis();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.wyloguj:
                Intent intent = new Intent(this, MainActivity.class);
                this.startActivity(intent);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }

        return true;
    }
    private void checkEmail(Map<String,Object> users, String currentEmail) {
        boolean flag = false;
        ArrayList<String> ids = new ArrayList<>();

        //iterate through each user, ignoring their UID
        for (Map.Entry<String, Object> entry : users.entrySet()){

            //Get user map
            Map singleUser = (Map) entry.getValue();
            //Get phone field and append to list
            String cur = (String) singleUser.get("email");
            Log.v(TAG, cur);
            if (cur.equals(newString)) {
                Log.v(TAG, "wejszło");
                flag = true;

                //Todo 69 wez stringa z danymi: id i czasem też?
                
            }
            ids.add((String) singleUser.get("email"));
            Log.v(TAG, flag+" flaga");
        }
        if (!flag){
            Log.v(TAG, "wejszło we flage");
            User user = new User(numberId+1,newString);
            dtbase.child("users").child(String.valueOf(numberId+1)).setValue(user);
        }

        System.out.println(ids.toString());
    }
}
