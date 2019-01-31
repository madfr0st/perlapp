package com.example.fr0st.perl;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;


public class AllusersActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private RecyclerView mUserlist;
    private DatabaseReference mDatabase;
    public FirebaseRecyclerAdapter firebaseRecyclerAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_allusers);

        mToolbar = (Toolbar) findViewById(R.id.alluser);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("All users");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("users");

        mUserlist = (RecyclerView) findViewById(R.id.userlist);
        mUserlist.setHasFixedSize(true);
        mUserlist.setLayoutManager(new LinearLayoutManager(this));




    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<users> options =
                new FirebaseRecyclerOptions.Builder<users>()
                        .setQuery(mDatabase, users.class)
                        .build();

        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<users, usersViewHolder>(options) {


            @NonNull
            @Override
            public usersViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {


                    // Create a new instance of the ViewHolder, in this case we are using a custom
                    // layout called R.layout.message for each item
                    View view = LayoutInflater.from(viewGroup.getContext())
                            .inflate(R.layout.user_single_layout, viewGroup, false);

                    return new usersViewHolder(view);

            }
            @Override
            protected void onBindViewHolder(@NonNull usersViewHolder holder, int position, @NonNull users model) {

                holder.setName(model.getName(),model.getStatus(),model.getImage());
                final String user_id = getRef(position).getKey();

                holder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent userProfile = new Intent(AllusersActivity.this,ProfileActivity.class);
                        userProfile.putExtra("user_id",user_id);
                        startActivity(userProfile);

                    }
                });

            }
        };

        firebaseRecyclerAdapter.startListening();

        mUserlist.setAdapter(firebaseRecyclerAdapter);


    }

    @Override
    protected void onStop() {
        super.onStop();

        firebaseRecyclerAdapter.startListening();

    }

    public static class usersViewHolder extends RecyclerView.ViewHolder{

        View mView;

        public usersViewHolder(@NonNull View itemView) {
            super(itemView);

            mView = itemView;

        }

        public void setName(String name,String status,String image){

            TextView userName = (TextView) mView.findViewById(R.id.username);
            userName.setText(name);
            TextView userSame = (TextView) mView.findViewById(R.id.userstatus);
            userSame.setText(status);
            CircleImageView mCircleImage = (CircleImageView) mView.findViewById(R.id.DP);
            Picasso.get()
                    .load(image)
                    .resize(60, 60)
                    .centerCrop()
                    .into(mCircleImage);

        }

    }
}
