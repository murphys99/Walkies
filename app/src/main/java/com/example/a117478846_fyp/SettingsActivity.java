package com.example.a117478846_fyp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
//Used a youtube tutorial to set up the edit profile functionality
//https://www.youtube.com/watch?v=MUiZhCUHXhk&list=PLxabZQCAe5fio9dm1Vd0peIY6HLfo5MCf&index=10&ab_channel=SimCoder
public class SettingsActivity extends AppCompatActivity {

    private EditText mFnameField, mEmailField, mPhoneField, mBioField;

    private Button mBack, mSave, mAdd;

    private ImageView mProfileImage;

    private FirebaseAuth mAuth;
    private DatabaseReference mUserDatabase;

    private String userId, fname,email, phone, bio, profileImageUrl, userType;

    private Uri resultUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);


        mFnameField = (EditText) findViewById(R.id.firstName);
        mEmailField = (EditText) findViewById(R.id.email);
        mPhoneField = (EditText) findViewById(R.id.phone);
        mBioField = (EditText) findViewById(R.id.bio);


        mProfileImage = (ImageView) findViewById(R.id.profileImage);

        mBack = (Button) findViewById(R.id.back);
        mSave = (Button) findViewById(R.id.save);
        mAdd  =  (Button) findViewById(R.id.add);


        mAuth = FirebaseAuth.getInstance();
        userId = mAuth.getCurrentUser().getUid();
        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);

        getUserInfo();
        mProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //tells the phone that we need to access other features (gallery in this case)
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent,1);

            }
        });

        mAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),RegisterDog.class);
                startActivity(intent);
            }
        });


        mSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveUserInformation();
            }
        });

        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),SwipeActivity.class);
                startActivity(intent);
            }
        });


    }

    private void getUserInfo() {
        mUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {
                    Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                    if (map.get("fname") != null) {
                        fname = map.get("fname").toString();
                        mFnameField.setText(fname);
                    }
                    if(map.get("email") != null){
                        email = map.get("email").toString();
                        mEmailField.setText(email);
                    }

                    if (map.get("phone") != null) {
                        phone = map.get("phone").toString();
                        mPhoneField.setText(phone);

                        if (map.get("bio") != null) {
                            bio = map.get("bio").toString();
                            mBioField.setText(bio);
                        }
                        if (map.get("userType") != null) {
                            userType = map.get("userType").toString();
                        }
                        Glide.clear(mProfileImage);
                        if (map.get("profileImageUrl") != null) {
                            profileImageUrl = map.get("profileImageUrl").toString();
                            switch (profileImageUrl) {
                                case "default":
                                    mProfileImage.setImageResource(R.mipmap.ic_launcher);
                                    break;
                                default:
                                    Glide.with(getApplicationContext()).load(profileImageUrl).into(mProfileImage);
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void saveUserInformation() {
        fname = mFnameField.getText().toString();
        email= mEmailField.getText().toString();
        phone = mPhoneField.getText().toString();
        bio = mBioField.getText().toString();

        Map userInfo = new HashMap<>();
        userInfo.put("fname",fname);
        userInfo.put("email",email);
        userInfo.put("phone",phone);
        userInfo.put("bio",bio);
        mUserDatabase.updateChildren(userInfo);
        if(resultUri != null){
            StorageReference filepath = FirebaseStorage.getInstance().getReference().child("profileImages").child(userId);
            Bitmap bitmap = null;

            try {
                bitmap = MediaStore.Images.Media.getBitmap(getApplication().getContentResolver(),resultUri);
            } catch (IOException e) {
                e.printStackTrace();
            }

            //Compressing photo so it fits nicely
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos);
            byte[] data = baos.toByteArray();
            UploadTask uploadTask = filepath.putBytes(data);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    finish();
                }
            });
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Map newImage = new HashMap();
                            newImage.put("profileImageUrl",uri.toString());
                            mUserDatabase.updateChildren(newImage);

                            finish();
                            return;
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            finish();
                            return;
                        }
                    });
                }
            });


        }else{
            finish();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            final Uri imageUri = data.getData();
            resultUri = imageUri;
            mProfileImage.setImageURI(resultUri);
        }
    }
    public void goToRegisterDog(View view) {

    }
}