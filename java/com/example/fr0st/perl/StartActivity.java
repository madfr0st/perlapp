package com.example.fr0st.perl;


import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

public class StartActivity extends AppCompatActivity {

    private Button mSignInBtn;
    private Button mSignUpBtn;

    private RelativeLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        layout = (RelativeLayout) findViewById(R.id.start_activity);
        layout.setBackgroundResource(R.drawable.startup);

        mSignInBtn = (Button) findViewById(R.id.signin);

        mSignInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent logIn = new Intent(StartActivity.this,LoginActivity.class);
                startActivity(logIn);

            }
        });

        mSignUpBtn = (Button) findViewById(R.id.signup);

        mSignUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent register = new Intent(StartActivity.this,RegisterActivity.class);
                startActivity(register);

            }
        });

    }


}
