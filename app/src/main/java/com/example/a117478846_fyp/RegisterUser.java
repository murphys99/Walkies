package com.example.a117478846_fyp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;


//sourced code for registering from https://www.youtube.com/watch?v=Z-RE1QuUWPg&t=342s&ab_channel=CodeWithMazn&fbclid=IwAR1xxY3MO0NM4bjpQt1q9XrO3FZHv9A53QZse8vUEMzPKFFeEF_arCI6Lu8
//
public class RegisterUser extends AppCompatActivity implements View.OnClickListener {
    EditText editFname, editLname, editEmail, editPassword, editPhone;
    Button btnRegister;
    RadioGroup mRadioGroup;


    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_user);

        mAuth = FirebaseAuth.getInstance();



        btnRegister = (Button) findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(this);

        editFname = (EditText) findViewById(R.id.etFname);
        editLname = (EditText) findViewById(R.id.etLname);
        editEmail = (EditText) findViewById(R.id.etEmail);
        editPassword = (EditText) findViewById(R.id.etPassword);
        editPhone = (EditText) findViewById(R.id.etPhone);

        mRadioGroup = (RadioGroup) findViewById(R.id.radioGroup);


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnRegister:
                registerUser();
                break;
        }
    }

    private void registerUser() {
        String fname = editFname.getText().toString().trim();
        String lname = editLname.getText().toString().trim();
        String email = editEmail.getText().toString().trim();
        String password = editPassword.getText().toString().trim();
        String phone = editPhone.getText().toString().trim();

        int selectId = mRadioGroup.getCheckedRadioButtonId();
        final RadioButton radioButton = (RadioButton) findViewById(selectId);

        if (fname.isEmpty()) {
            editFname.setError("First name is required!");
            editFname.requestFocus();
            return;
        }
        if (lname.isEmpty()) {
            editLname.setError("Last name is required!");
            editLname.requestFocus();
            return;

        }
        if (email.isEmpty()) {
            editEmail.setError("Email is required!");
            editEmail.requestFocus();
            return;

        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editEmail.setError("Please provide a valid email!");
            editEmail.requestFocus();
            return;
        }
        if (password.isEmpty()) {
            editPassword.setError("Password is required!");
            editPassword.requestFocus();
            return;
        }

        if (password.length() < 6) {
            editPassword.setError("Minimum password length should be 6 characters!");
            editPassword.requestFocus();
            return;
        }


        if (phone.isEmpty()) {
            editPhone.setError("Phone number is required!");
            editPhone.requestFocus();
            return;
        }

        if (radioButton.getText() == null) {
            return;
        }


        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (!task.isSuccessful()) {
                            Toast.makeText(RegisterUser.this, "Sign Up Error", Toast.LENGTH_LONG).show();
                        }else{
                           // User user = new User(fname, lname, email, password, phone);
                            String userId = mAuth.getCurrentUser().getUid();

                            DatabaseReference currentUserDb = FirebaseDatabase.getInstance().getReference().child("Users").child(radioButton.getText().toString())
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                            Map userInfo = new HashMap<>();
                            userInfo.put("fname",fname);
                            userInfo.put("lname",lname);
                            userInfo.put("email",email);
                            userInfo.put("password",password);
                            userInfo.put("phone",phone);
                            userInfo.put("profileImageUrl", "default");
                            currentUserDb .updateChildren(userInfo);

                            Toast.makeText(RegisterUser.this, "User has been successfully registered!", Toast.LENGTH_LONG).show();
                            Intent menuIntent = new Intent(getApplicationContext(),MainActivity.class);
                            startActivity(menuIntent);



                        }
                    }
                });
    }

}