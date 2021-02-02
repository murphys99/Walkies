package com.example.a117478846_fyp;
//potential matches to user video https://www.youtube.com/watch?v=xTGyUUsOTAQ&t=92s&ab_channel=SimCoder
// adapted this video for matching users https://www.youtube.com/watch?v=Xp61U9sCiTw&list=PLxabZQCAe5fio9dm1Vd0peIY6HLfo5MCf&index=9&pbjreload=101&ab_channel=SimCoder
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
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
    private arrayAdapter arrayAdapter;
    private int i;

    private FirebaseAuth mAuth;

    ListView listView;
    List<cards> rowItems;

    private DatabaseReference usersDb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe);

        usersDb = FirebaseDatabase.getInstance().getReference().child("Users");

        mAuth = FirebaseAuth.getInstance();
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

                cards obj = (cards)  dataObject;
                String userId = obj.getUserId();
                usersDb.child(oppositeUserType).child(userId).child("connections").child("no").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(true);

                Toast.makeText(SwipeActivity.this, "left", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRightCardExit(Object dataObject) {
                cards obj = (cards)  dataObject;
                String userId = obj.getUserId();
                usersDb.child(oppositeUserType).child(userId).child("connections").child("yes").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(true);
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
        DatabaseReference currentUserConnectionDb =  usersDb.child(userType).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("connections").child("yes").child(userId);
        currentUserConnectionDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               if (dataSnapshot.exists()){
                   Toast.makeText(SwipeActivity.this,"New connection", Toast.LENGTH_LONG).show();
                   usersDb.child(oppositeUserType).child(dataSnapshot.getKey()).child("connections").child("matches").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(true);
                   usersDb.child(userType).child("connections").child("matches").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(dataSnapshot.getKey()).setValue(true);
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

        DatabaseReference walkerDb = FirebaseDatabase.getInstance().getReference().child("Users").child("Dog Walker");
        walkerDb.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.getKey().equals(user.getUid())) {
                    userType = "Dog Walker";
                    oppositeUserType = "Dog Owner";
                    getOppositeUserType();

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


        DatabaseReference ownerDb = FirebaseDatabase.getInstance().getReference().child("Users").child("Dog Owner");
        ownerDb.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.getKey().equals(user.getUid())) {
                    userType = "Dog Owner";
                    oppositeUserType = "Dog Walker";
                    getOppositeUserType();

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

    public void getOppositeUserType() {
        DatabaseReference oppositeUserTypeDb = FirebaseDatabase.getInstance().getReference().child("Users").child(oppositeUserType);
        oppositeUserTypeDb.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.exists() && !dataSnapshot.child("connections").child("no").hasChild(FirebaseAuth.getInstance().getCurrentUser().getUid()) && !dataSnapshot.child("connections").child("yes").hasChild(FirebaseAuth.getInstance().getCurrentUser().getUid())){


                    cards item = new cards(dataSnapshot.getKey(),dataSnapshot.child("fname").getValue().toString());
                    rowItems.add(item);
                    arrayAdapter.notifyDataSetChanged();

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


    }
