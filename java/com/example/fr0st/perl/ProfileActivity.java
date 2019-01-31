package com.example.fr0st.perl;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

public class ProfileActivity extends AppCompatActivity {

    private ImageView mImageView;
    private TextView userName;
    private TextView userStatus;
    private Button sendRequest;
    //private Button unfriend;
    private DatabaseReference mDatabaseThird,mDatabase;
    private FirebaseAuth mAuth;
    private String btnkey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        final String user_id = getIntent().getStringExtra("user_id");

        mImageView = (ImageView) findViewById(R.id.profilepic);
        userName = (TextView) findViewById(R.id.username);
        userStatus = (TextView) findViewById(R.id.userstatus);
        sendRequest = (Button) findViewById(R.id.sendfrndrequest);
        //unfriend = (Button) findViewById(R.id.unfriend);


        mAuth = FirebaseAuth.getInstance();
        FirebaseUser mUser = mAuth.getCurrentUser();
        final String Uid = mUser.getUid();

        mDatabaseThird = FirebaseDatabase.getInstance().getReference().child("users").child(user_id);
        mDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(Uid);

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {



                if(dataSnapshot.child("friend_req_send").child(user_id).exists()){

                    sendRequest.setText("DECLINE REQ");

                }
                else if(dataSnapshot.child("friend_req_recieved").child(user_id).exists()){

                    sendRequest.setText("ACCEPT REQ");

                }
                else if(dataSnapshot.child("friends").child(user_id).exists()){

                    sendRequest.setText("UNFRIEND");

                }
                else {

                    sendRequest.setText("SEND REQ");

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        System.out.println(user_id);

        mDatabaseThird.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String Databasename = dataSnapshot.child("name").getValue().toString();
                String Da = dataSnapshot.child("status").getValue().toString();
                String Image = dataSnapshot.child("image").getValue().toString();

                userName.setText(Databasename);
                userStatus.setText(Da);
                Picasso.get().load(Image).into(mImageView);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });





            sendRequest.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(sendRequest.getText().equals("SEND REQ")){

                        mDatabase.child("friend_req_send").child(user_id).child("status").setValue("send").addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                if (task.isSuccessful()) {

                                    mDatabaseThird.child("friend_req_recieved").child(Uid).child("status").setValue("recieved").addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {

                                            Toast.makeText(ProfileActivity.this, "req sent", Toast.LENGTH_LONG).show();
                                            sendRequest.setText("DECLINE REQ");

                                        }
                                    });

                                } else {

                                    Toast.makeText(ProfileActivity.this, "unable to send request", Toast.LENGTH_LONG).show();

                                }

                            }
                        });

                    }

                    if(sendRequest.getText().equals("DECLINE REQ")){

                        mDatabase.child("friend_req_send").child(user_id).child("status").removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                if (task.isSuccessful()) {

                                    mDatabaseThird.child("friend_req_recieved").child(Uid).child("status").removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {

                                            Toast.makeText(ProfileActivity.this, "req removed", Toast.LENGTH_LONG).show();
                                            sendRequest.setText("SEND REQ");

                                        }
                                    });

                                } else {

                                    Toast.makeText(ProfileActivity.this, "unable to remove request", Toast.LENGTH_LONG).show();

                                }

                            }
                        });

                    }

                    if(sendRequest.getText().equals("UNFRIEND")){

                        mDatabase.child("friends").child(user_id).child("status").removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                if (task.isSuccessful()) {

                                    mDatabaseThird.child("friends").child(Uid).child("status").removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {


                                            if (task.isSuccessful()) {

                                                mDatabase.child("friend_req_recieved").child(user_id).child("status").removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {

                                                        if (task.isSuccessful())

                                                            mDatabaseThird.child("friend_req_send").child(Uid).child("status").removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {

                                                                    Toast.makeText(ProfileActivity.this, "ENEMY", Toast.LENGTH_LONG).show();
                                                                    sendRequest.setText("SEND REQ");

                                                                }
                                                            });

                                                    }


                                                });

                                            }
                                        }

                                    });

                                }

                            }
                        });

                    }

                    if(sendRequest.getText().equals("ACCEPT REQ")){

                        mDatabase.child("friends").child(user_id).child("status").setValue("friend").addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                if (task.isSuccessful()) {

                                    mDatabaseThird.child("friends").child(Uid).child("status").setValue("friends").addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                            if (task.isSuccessful()){

                                                mDatabase.child("friend_req_recieved").child(user_id).child("status").removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {

                                                        if (task.isSuccessful()) {

                                                            mDatabaseThird.child("friend_req_send").child(Uid).child("status").removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {

                                                                    Toast.makeText(ProfileActivity.this, "FRIENDS", Toast.LENGTH_LONG).show();
                                                                    sendRequest.setText("UNFRIEND");

                                                                }
                                                            });

                                                        } else {

                                                            Toast.makeText(ProfileActivity.this, "unable to ACCEPT", Toast.LENGTH_LONG).show();

                                                        }

                                                    }
                                                });

                                        }
                                        }
                                    });

                                } else {

                                    Toast.makeText(ProfileActivity.this, "unable to send request", Toast.LENGTH_LONG).show();

                                }

                            }
                        });

                    }

                }
            });

        }

}
