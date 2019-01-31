package com.example.fr0st.perl;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class ChatFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private DatabaseReference chatdata;
    private FirebaseRecyclerAdapter firebaseRecyclerAdapter;
    private View view;
    private FirebaseAuth mAuth;



    public ChatFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_chat, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.chatList);

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));

        mAuth = FirebaseAuth.getInstance();



        // Inflate the layout for this fragment-----------------------------------------------------
        return view;


    }
    @Override
    public void onStart() {
        super.onStart();

        String uid = mAuth.getCurrentUser().getUid().toString();

        //Database reference------------------------------------------------------------------------

        chatdata = FirebaseDatabase.getInstance().getReference().child("users").child(uid).
                child("chatlist");

        //Recycler view-----------------------------------------------------------------------------

        FirebaseRecyclerOptions<chatlist> options = new FirebaseRecyclerOptions.
                Builder<chatlist>().
                setQuery(chatdata,chatlist.class).build();

        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<chatlist,chatlistholder>(options){

            @NonNull
            @Override
            public chatlistholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

                // Create a new instance of the ViewHolder, in this case we are using a custom
                // layout called R.layout.message for each item
                View v = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.user_single_layout, viewGroup, false);

                return new chatlistholder(v);
            }

            @Override
            protected void onBindViewHolder(@NonNull chatlistholder holder, int position, @NonNull chatlist model) {

                final String user_id = getRef(position).getKey();
                holder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent chatpage = new Intent(getActivity(), ChatActivity.class);
                        chatpage.putExtra("user_id", user_id);
                        startActivity(chatpage);

                    }
                });
            }



        };

        firebaseRecyclerAdapter.startListening();
        mRecyclerView.setAdapter(firebaseRecyclerAdapter);


    }


    public static class chatlistholder extends RecyclerView.ViewHolder {

        View mView;

        public chatlistholder(@NonNull View itemView) {
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
