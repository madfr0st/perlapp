package com.example.fr0st.perl;


import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class StatusActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private Button mUpdate;
    private EditText mEdittext;
    private ProgressDialog mProgressDialog;

    private DatabaseReference mDatabase;
    private FirebaseUser mCurrentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setTitle("Updating");
        mProgressDialog.setMessage("wait a while");
        mProgressDialog.setCanceledOnTouchOutside(false);


        mToolbar = (Toolbar) findViewById(R.id.statusLayout);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Change Status");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mUpdate = (Button) findViewById(R.id.updatestatus);
        mEdittext = (EditText) findViewById(R.id.editstatus);

        String newStatus = getIntent().getStringExtra("newStatus");


        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        String userid = mCurrentUser.getUid();

        mDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(userid).child("status");
        mEdittext.setText(newStatus);

        mUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mProgressDialog.show();

                String updatedStatus = mEdittext.getText().toString();

                mDatabase.setValue(updatedStatus).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                            if(task.isSuccessful()){

                                mProgressDialog.hide();
                                Toast.makeText(StatusActivity.this,"Done!",Toast.LENGTH_LONG).show();
                            }
                            else{
                                mProgressDialog.hide();
                                Toast.makeText(StatusActivity.this,"error",Toast.LENGTH_LONG).show();
                            }

                    }
                });

            }
        });
    }
}
