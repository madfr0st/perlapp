package com.example.fr0st.perl;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;


public class RegisterActivity extends AppCompatActivity {

    private ProgressDialog mProgressDialog;

    private EditText mName;
    private EditText mEmail;
    private EditText mPass;
    private EditText mConfPass;
    private Button mSignup;
    private FirebaseAuth mAuth;

    private FirebaseUser currentUser;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mName = (EditText) findViewById(R.id.displayname);
        mEmail = (EditText) findViewById(R.id.email);
        mPass = (EditText) findViewById(R.id.password1);
        mConfPass = (EditText) findViewById(R.id.password2);
        mSignup = (Button) findViewById(R.id.signup);

        mProgressDialog = new ProgressDialog(this);

        mAuth = FirebaseAuth.getInstance();

        mSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = mName.getText().toString();
                String email = mEmail.getText().toString();
                String pass = mPass.getText().toString();
                String confPass = mConfPass.getText().toString();

                if(!TextUtils.isEmpty(name) && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(pass) && !TextUtils.isEmpty(confPass)){

                    if(pass.equals(confPass)){

                        mProgressDialog.setTitle("Registering user");
                        mProgressDialog.setMessage("wait while we create");
                        mProgressDialog.setCanceledOnTouchOutside(false);
                        mProgressDialog.show();
                        register_user(name,email,pass);

                    }

                     else{
                        mProgressDialog.hide();
                        Toast.makeText(RegisterActivity.this,"pass not same",Toast.LENGTH_LONG).show();
                    }

                }


            }

            private void register_user(final String name, String email, final String pass) {

                mAuth.createUserWithEmailAndPassword(email, pass)
                        .addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {

                                        currentUser = FirebaseAuth.getInstance().getCurrentUser();
                                        String uid = currentUser.getUid();


                                        mDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(uid);

                                        HashMap<String ,String> userMap = new HashMap<>();

                                        userMap.put("name",name);
                                        userMap.put("pass",pass);
                                        userMap.put("status","hey i m back");
                                        userMap.put("image","default image");
                                        userMap.put("thumbimage","default thumb image");

                                        mDatabase.setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {

                                                if(task.isSuccessful()){

                                                    mProgressDialog.dismiss();
                                                    Intent mainIntent = new Intent(RegisterActivity.this,MainActivity.class);
                                                    mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                    startActivity(mainIntent);
                                                    finish();

                                                }

                                                else{

                                                    Toast.makeText(RegisterActivity.this,"error while creating database",Toast.LENGTH_LONG).show();

                                                }

                                            }
                                        });




                                    } else {

                                    mProgressDialog.hide();
                                    Toast.makeText(RegisterActivity.this,"you got some error",Toast.LENGTH_LONG).show();

                                }


                            }
                        });

            }
        });

    }
}
