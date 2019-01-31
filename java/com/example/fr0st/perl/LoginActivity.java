package com.example.fr0st.perl;


import android.app.Activity;
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

public class LoginActivity extends AppCompatActivity {

    private Button mButton;
    private EditText mEmail;
    private EditText mPass;
    private ProgressDialog mProgressDialog;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mButton = (Button) findViewById(R.id.login);
        mEmail = (EditText) findViewById(R.id.email);
        mPass = (EditText) findViewById(R.id.pass);
        mProgressDialog = new ProgressDialog(this);

        mAuth = FirebaseAuth.getInstance();

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = mEmail.getEditableText().toString();
                String pass = mPass.getEditableText().toString();

                if(!TextUtils.isEmpty(email) && !TextUtils.isEmpty(pass)){

                    mProgressDialog.setTitle("loging");
                    mProgressDialog.setMessage("wait while we check");
                    mProgressDialog.show();

                    signIn(email,pass);

                }

            }

            private void signIn(String email, String pass) {

                mAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()){

                            mProgressDialog.dismiss();
                            Intent main = new Intent(LoginActivity.this,MainActivity.class);
                            main.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(main);
                            finish();
                        }
                        else{
                            mProgressDialog.hide();
                            Toast.makeText(LoginActivity.this,"check your crediantials",Toast.LENGTH_LONG).show();


                        }

                    }
                });

            }
        });

    }
}
