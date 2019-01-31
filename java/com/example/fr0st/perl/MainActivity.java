package com.example.fr0st.perl;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity  {

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private Toolbar mToolbar;
    private String uid;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private ViewPagerAdapter mViewPagerAdapter;
    private DrawerLayout drawerLayout;
    private FirebaseUser currentUser;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Firebase---------------------------------------------------------------------------------

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("users");

        //Toolbar-----------------------------------------------------------------------------------

        mToolbar = (Toolbar) findViewById(R.id.mainPageToolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Perl");


        //Tabpager----------------------------------------------------------------------------------

        mViewPager = (ViewPager) findViewById(R.id.tabpager);
        mTabLayout = (TabLayout) findViewById(R.id.tab);
        mViewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mViewPagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);

        //Side menu layout--------------------------------------------------------------------------

        drawerLayout = (DrawerLayout) findViewById(R.id.maintabs);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,mToolbar,
                R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                if(menuItem.getItemId()==R.id.logout){

                    FirebaseAuth.getInstance().signOut();
                    sendToStart();

                }

                if(menuItem.getItemId()==R.id.accountsetting){

                    Intent accsetting = new Intent(MainActivity.this,AccountSettingActivity.class);
                    startActivity(accsetting);


                }
                if(menuItem.getItemId()==R.id.allusermenu){

                    Intent alluseractivity = new Intent(MainActivity.this,AllusersActivity.class);
                    startActivity(alluseractivity);

                }
                if(menuItem.getItemId()==R.id.all_friends){

                    Intent all_friends_activity = new Intent(MainActivity.this,FriendsActivity.class);
                    startActivity(all_friends_activity);

                }

                return true;

            }
        });


    }

    @Override
    public void onBackPressed() {
       if(drawerLayout.isDrawerOpen(GravityCompat.START)){

           drawerLayout.closeDrawer(GravityCompat.START);

       }
           else{
           super.onBackPressed();
       }
    }

    @Override
    public void onStart() {
        super.onStart();




        // Check if user is signed in (non-null) and update UI accordingly.-------------------------

        currentUser = mAuth.getCurrentUser();

        if(currentUser != null){
            uid = currentUser.getUid();
            mDatabase.child(uid).child("lastOnline").setValue(ServerValue.TIMESTAMP);
        }
        if(currentUser==null){
            sendToStart();
        }



    }

    private void sendToStart() {

        Intent startIntent = new Intent(MainActivity.this,StartActivity.class);
        startActivity(startIntent);
        finish();

    }

    @Override
    protected void onStop() {
        super.onStop();
        if(currentUser != null) {
            mDatabase.child(uid).child("lastOnline").onDisconnect().setValue(ServerValue.TIMESTAMP);
        }
    }
}
