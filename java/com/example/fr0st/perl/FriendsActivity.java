package com.example.fr0st.perl;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.load.resource.bitmap.CenterInside;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class FriendsActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private RecyclerView mRecyclerView;
    private DatabaseReference mDatabase,mUserData;
    private FirebaseRecyclerAdapter firebaseRecyclerAdapter;
    private FirebaseAuth mAuth;
    private PopupMenu mPopup;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);

        mToolbar = (Toolbar) findViewById(R.id.friends_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("All Friends");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mRecyclerView = (RecyclerView) findViewById(R.id.allfriends);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = current_user.getUid();

        mDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(uid).child("friends");
        mUserData = FirebaseDatabase.getInstance().getReference().child("users");


    }


    @Override
    protected void onStart() {
        super.onStart();


        FirebaseRecyclerOptions<friends> options = new FirebaseRecyclerOptions.Builder<friends>()
                .setQuery(mDatabase,friends.class).build();

        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter <friends , friendsViewHolder> (options) {
            @Override
            protected void onBindViewHolder(@NonNull final friendsViewHolder holder, int position, @NonNull friends model) {

               // holder.setName(model.getName(),model.getStatus(),model.getImage());
                final String user_id = getRef(position).getKey();
                DatabaseReference singleUserData = mUserData.child(user_id);

                singleUserData.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        String name = dataSnapshot.child("name").getValue().toString();
                        String status = dataSnapshot.child("status").getValue().toString();
                        String image = dataSnapshot.child("image").getValue().toString();

                        holder.setName(name,status,image);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                holder.mmView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        mPopup = new PopupMenu(FriendsActivity.this,holder.mmView,Gravity.CENTER);

                        mPopup.getMenuInflater()
                                .inflate(R.menu.popup_menu,mPopup.getMenu());

                        mPopup.show();

                        mPopup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {

                                if(item.getItemId()==R.id.view_profile){

                                    Intent userProfile = new Intent(FriendsActivity.this,ProfileActivity.class);
                                    userProfile.putExtra("user_id",user_id);
                                    startActivity(userProfile);

                                }

                                if(item.getItemId()==R.id.start_chat){

                                    Intent userProfile = new Intent(FriendsActivity.this,ChatActivity.class);
                                    userProfile.putExtra("user_id",user_id);
                                    startActivity(userProfile);

                                }

                                return true;
                            }
                        });



                    }
                });

            }

            @NonNull
            @Override
            public friendsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.user_single_layout, viewGroup, false);

                return new friendsViewHolder(view);
            }
        };

        firebaseRecyclerAdapter.startListening();

        mRecyclerView.setAdapter(firebaseRecyclerAdapter);

    }
    public static class friendsViewHolder extends RecyclerView.ViewHolder{

        View mmView;

        public friendsViewHolder(@NonNull View itemView) {
            super(itemView);

            mmView = itemView;

        }

       public  void setName(String name,String status,String image){

            TextView userName = (TextView) mmView.findViewById(R.id.username);
            userName.setText(name);
            TextView userSame = (TextView) mmView.findViewById(R.id.userstatus);
            userSame.setText(status);
            CircleImageView mCircleImage = (CircleImageView) mmView.findViewById(R.id.DP);
            Picasso.get()
                    .load(image)
                    .resize(60, 60)
                    .centerCrop()
                    .into(mCircleImage);

        }

    }
}


