package com.example.sellany;

import android.app.ProgressDialog;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.sellany.Model.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class SignUpActivity extends AppCompatActivity {

    private Button btnLogin,btnSignup;
    private Button signUp;
    private EditText Email,Pass,Cpass,Phonen,Name;
    private FirebaseAuth mAuth;
    private Users mUser;

    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        btnLogin=(Button) findViewById(R.id.btnLogin);
        btnSignup=(Button) findViewById(R.id.btnSignup);
        signUp=(Button) findViewById(R.id.button4);
        Email=(EditText) findViewById(R.id.PoneText);
        Pass=(EditText) findViewById(R.id.PassText);
        Cpass=(EditText) findViewById(R.id.ConfirmText);
        Phonen=(EditText) findViewById(R.id.PhoneText);
        Name=(EditText) findViewById(R.id.NameText);
        loadingBar=new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        mUser=new Users();


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUpActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateAccount();
            }
        });


    }
    /*@Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser(); }*/


    private void CreateAccount() {
        String email=Email.getText().toString();
        String pass=Pass.getText().toString();
        String cpass=Cpass.getText().toString();
        String phone=Phonen.getText().toString();
        String name=Name.getText().toString();
        if (TextUtils.isEmpty(email)){
            Toast.makeText(this, "Please enter your Email Address", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(pass)){
            Toast.makeText(this, "Please write your Password", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(name)){
            Toast.makeText(this, "Please write your Name", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(cpass)){
            Toast.makeText(this, "Please Confirm your password", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(phone)){
            Toast.makeText(this, "Please enter your Phone Number", Toast.LENGTH_SHORT).show();
        }
        else{
            loadingBar.setTitle("Create Account");
            loadingBar.setMessage("Creating Account");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();
            ValidatePhoneNumber(email, pass, cpass, phone,name);


        }
    }

    private void ValidatePhoneNumber(final String email, final String pass, final String cpass, final String phone, final String name) {
        if(!pass.equals(cpass)){
            loadingBar.dismiss();
            Toast.makeText(SignUpActivity.this,"Please Confirm Your Password",Toast.LENGTH_SHORT).show();
        }

        else{
            mAuth.createUserWithEmailAndPassword(email,pass)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                sendVerificationEmail();
                        }
                            else {
                                // If sign in fails, display a message to the user.
                                loadingBar.dismiss();
                                Toast.makeText(SignUpActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            }
                    }
                    });
            /*String userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            mUser.setName(name);
            mUser.setUser_id(userid);*/
            final FirebaseDatabase database = FirebaseDatabase.getInstance();
            final DatabaseReference myRef = database.getReference();

            myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(!((dataSnapshot.child("Users").child(phone).exists() )&& (dataSnapshot.child("Users").child(phone).child(email).exists()) )){

                        HashMap<String, Object> userdataMap = new HashMap<>();
                        userdataMap.put("Phone", phone);
                        userdataMap.put("Password", pass);
                        userdataMap.put("Email", email);
                        userdataMap.put("Namre",name);
                        /*userdataMap.put("UserID",mUser);*/
                        myRef.child("Users").child(phone).updateChildren(userdataMap)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                      if(task.isSuccessful()){
                                          Toast.makeText(SignUpActivity.this, "Congratulations! Your account has been created.",Toast.LENGTH_SHORT).show();
                                          loadingBar.dismiss();
                                          Intent intent = new Intent(SignUpActivity.this,MainActivity.class);
                                          startActivity(intent);

                                      }
                                      else {
                                          loadingBar.dismiss();
                                          Toast.makeText(SignUpActivity.this, "NetWork ERROR : Please try again after some time",Toast.LENGTH_SHORT).show();
                                      }
                                    }
                                });

                    }
                    else{
                        Toast.makeText(SignUpActivity.this, "User is already exists", Toast.LENGTH_SHORT).show();
                        loadingBar.dismiss();
                        Intent intent = new Intent(SignUpActivity.this,MainActivity.class);
                        startActivity(intent);

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


        }
    }


    public void sendVerificationEmail() {
        final FirebaseUser user = mAuth.getCurrentUser();
        if(user != null){
            user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(SignUpActivity.this, "Verification mail is sent", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        loadingBar.dismiss();
                        Toast.makeText(SignUpActivity.this, "couldn't send email", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }
    }

}
