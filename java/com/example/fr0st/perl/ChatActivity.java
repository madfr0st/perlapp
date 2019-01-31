package com.example.fr0st.perl;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;


public class ChatActivity extends AppCompatActivity {

    private TextView mTextView;
    private Button mButton;
    private RecyclerView messageView;
    private Toolbar mToolbar;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private TextView time;
    private DatabaseReference messagedata;
    private FirebaseRecyclerAdapter firebaseRecyclerAdapter;
    private String uid;
    private DatabaseReference chatUserData;
    private DatabaseReference UserData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);



        //getting data from previous activity-------------------------------------------------------

        final String chatUser = getIntent().getStringExtra("user_id");

        //Items-------------------------------------------------------------------------------------

        time = (TextView) findViewById(R.id.last_online_time);
        mButton = (Button) findViewById(R.id.send_message);
        mTextView =(TextView) findViewById(R.id.input_message);

        //Database----------------------------------------------------------------------------------

        mDatabase = FirebaseDatabase.getInstance().getReference().child("users");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();
        chatUserData = mDatabase.child(chatUser);
        UserData = mDatabase.child(uid);

        //access message database-------------------------------------------------------------------

        messagedata = mDatabase.child(uid).child("chatlist").child(chatUser).child("message");

        //Recycler view-----------------------------------------------------------------------------

        messageView = (RecyclerView) findViewById(R.id.messageList);
        messageView.setHasFixedSize(true);
        messageView.setLayoutManager(new LinearLayoutManager(this));

        //Titlebar----------------------------------------------------------------------------------

        mToolbar = (Toolbar) findViewById(R.id.chat_page_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        chatUserData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String chatUserName = dataSnapshot.child("name").getValue().toString();
                getSupportActionBar().setTitle(chatUserName);
                String lastOnlineTime = dataSnapshot.child("lastOnline").getValue().toString();
                time.setText(lastOnlineTime);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //send message btn--------------------------------------------------------------------------

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(mTextView.getText()!=null){

                    String textMessage = mTextView.getText().toString();

                    //Self chat data----------------------------------------------------------------

                    Map usermessage = new HashMap<>();
                    usermessage.put("textmessage",textMessage);
                    usermessage.put("time",ServerValue.TIMESTAMP);
                    usermessage.put("from","send");

                    UserData.child("chatlist").child(chatUser).child("message").push().
                            setValue(usermessage);
                    UserData.child("chatlist").child(chatUser).child("chat").setValue("started");

                    //Chatuser chat data------------------------------------------------------------

                    Map chatusermessage = new HashMap<>();
                    chatusermessage.put("textmessage",textMessage);
                    chatusermessage.put("time",ServerValue.TIMESTAMP);
                    chatusermessage.put("from","recieved");

                    chatUserData.child("chatlist").child(uid).child("message").push().
                            setValue(chatusermessage);
                    chatUserData.child("chatlist").child(uid).child("chat").setValue("started");

                    mTextView.setText(null);


                }
                else{
                    Toast.makeText(ChatActivity.this,"Text field is empty",
                            Toast.LENGTH_LONG).show();
                }

            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<message> options = new FirebaseRecyclerOptions.Builder<message>().
                setQuery(messagedata,message.class).build();

        firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<message, messageholder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull messageholder holder, int position,
                                                    @NonNull message model) {

                        holder.setmessage(model.getTextmessage());

                    }

                    @NonNull
                    @Override

                    public messageholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

                        View view = LayoutInflater.from(viewGroup.getContext())
                                .inflate(R.layout.single_message, viewGroup, false);

                        return new messageholder(view);
                    }
                };

        firebaseRecyclerAdapter.startListening();

        messageView.setAdapter(firebaseRecyclerAdapter);

    }

    public static class messageholder extends RecyclerView.ViewHolder{

        View mview;

        public messageholder(@NonNull View itemView) {
            super(itemView);

            mview = itemView;

        }
        public void setmessage(String text){
            TextView textView = (TextView) mview.findViewById(R.id.msg);
            textView.setText(text);
        }
    }



}
