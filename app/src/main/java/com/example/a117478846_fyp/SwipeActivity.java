package com.example.a117478846_fyp;
//potential matches to user video https://www.youtube.com/watch?v=xTGyUUsOTAQ&t=92s&ab_channel=SimCoder
// adapted this video for matching users https://www.youtube.com/watch?v=Xp61U9sCiTw&list=PLxabZQCAe5fio9dm1Vd0peIY6HLfo5MCf&index=9&pbjreload=101&ab_channel=SimCoder
//setting an image to a card using this tutorial https://www.youtube.com/watch?v=h2yHW5wuxoE&list=PLxabZQCAe5fio9dm1Vd0peIY6HLfo5MCf&index=12&ab_channel=SimCoder
//setting up chat functionality with video https://www.youtube.com/watch?v=9dC4w04AuOs&list=PLxabZQCAe5fio9dm1Vd0peIY6HLfo5MCf&index=16&ab_channel=SimCoder
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;

import java.util.ArrayList;
import java.util.List;


public class SwipeActivity extends Activity {

    private cards cards_data[];
    private com.example.a117478846_fyp.arrayAdapter arrayAdapter;
    private int i;

    private FirebaseAuth mAuth;
    private String currentUId;

    ListView listView;
    List<cards> rowItems;

    private DatabaseReference usersDb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe);


        usersDb = FirebaseDatabase.getInstance().getReference().child("Users");

        mAuth = FirebaseAuth.getInstance();
        currentUId = mAuth.getCurrentUser().getUid();
        checkUserType();


        rowItems = new ArrayList<cards>();


        arrayAdapter = new arrayAdapter(this, R.layout.item, rowItems);

        SwipeFlingAdapterView flingContainer = (SwipeFlingAdapterView) findViewById(R.id.frame);


        flingContainer.setAdapter(arrayAdapter);
        flingContainer.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {
                // this is the simplest way to delete an object from the Adapter (/AdapterView)
                Log.d("LIST", "removed object!");
                rowItems.remove(0);
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onLeftCardExit(Object dataObject) {

                cards obj = (cards) dataObject;
                String userId = obj.getUserId();
                usersDb.child(userId).child("connections").child("no").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(true);

                Toast.makeText(SwipeActivity.this, "left", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRightCardExit(Object dataObject) {
                cards obj = (cards) dataObject;
                String userId = obj.getUserId();
                usersDb.child(userId).child("connections").child("yes").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(true);
                isConnectionMatch(userId);


                Toast.makeText(SwipeActivity.this, "right", Toast.LENGTH_SHORT).show();
            }


            @Override
            public void onAdapterAboutToEmpty(int itemsInAdapter) {
            }

            @Override
            public void onScroll(float scrollProgressPercent) {
            }
        });


        // Optionally add an OnItemClickListener
        flingContainer.setOnItemClickListener(new SwipeFlingAdapterView.OnItemClickListener() {
            @Override
            public void onItemClicked(int itemPosition, Object dataObject) {
                Toast.makeText(SwipeActivity.this, "clicked", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void isConnectionMatch(String userId) {
        DatabaseReference currentUserConnectionDb = usersDb.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("connections").child("yes").child(userId);
        currentUserConnectionDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Toast.makeText(SwipeActivity.this, "New connection", Toast.LENGTH_LONG).show();

                    String key = FirebaseDatabase.getInstance().getReference().child("Chat").push().getKey();

                    usersDb.child(dataSnapshot.getKey()).child("connections").child("matches").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("ChatId").setValue(key);
                    usersDb.child(currentUId).child("connections").child("matches").child(dataSnapshot.getKey()).child("ChatId").setValue(key);


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    private String userType;
    private String oppositeUserType;
    public void checkUserType() {
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference userDb = usersDb.child(user.getUid());
        userDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    if(dataSnapshot.child("userType").getValue() !=null){
                        userType = dataSnapshot.child("userType").getValue().toString();
                        switch (userType) {
                            case "Dog Owner":
                                oppositeUserType = "Dog Walker";
                                break;
                            case "Dog Walker":
                                oppositeUserType = "Dog Owner";
                                break;
                        }
                        getOppositeUserType();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        }






    public void getOppositeUserType() {
        usersDb.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.child("userType").getValue() != null) {
                    if (dataSnapshot.exists() && !dataSnapshot.child("connections").child("no").hasChild(currentUId) && !dataSnapshot.child("connections").child("yes").hasChild(currentUId) && dataSnapshot.child("userType").getValue().toString().equals(oppositeUserType)) {
                        String profileImageUrl = "default";
                        //   if(dataSnapshot.child("profileImageUrl").getValue() != null){
                        if (!dataSnapshot.child("profileImageUrl").getValue().equals("default")) {
                            profileImageUrl = dataSnapshot.child("profileImageUrl").getValue().toString();

                        }


                        cards item = new cards(dataSnapshot.getKey(), dataSnapshot.child("fname").getValue().toString(), profileImageUrl);
                        rowItems.add(item);
                        arrayAdapter.notifyDataSetChanged();

                    }
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {


            }

        });


    }

        static void makeToast (Context ctx, String s){
            Toast.makeText(ctx, s, Toast.LENGTH_SHORT).show();
        }

//Creating the edit profile page adapting code from: https://www.youtube.com/watch?v=MUiZhCUHXhk&ab_channel=SimCoder
    public void goToSettings(View view) {
        Intent intent = new Intent(SwipeActivity.this,SettingsActivity.class );
        startActivity(intent);
        return;
    }

    public void goToMatches(View view) {
        Intent intent = new Intent(SwipeActivity.this,MatchesActivity.class );
        startActivity(intent);
        return;
    }

    // public void goToMatches(View view) {
    //   Intent intent = new Intent(SwipeActivity.this, MatchesActivity.class );
      // startActivity(intent);
    //   return;
    //}
}

