package com.example.fr0st.perl;


import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;




import de.hdodenhof.circleimageview.CircleImageView;

public class AccountSettingActivity extends AppCompatActivity {

    private CircleImageView mCircleImage;
    private TextView mName;
    private TextView mStatus;
    private Button mChangeDP;
    private Button mChangeStatus;
    private DatabaseReference mDatabase;
    private FirebaseUser mCurrentUser;
    private StorageReference mStorage;
    private String uid;
    private ProgressDialog mProgressDialog;
    private String downloadUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_setting);

        mCircleImage = (CircleImageView) findViewById(R.id.settingdp);
        mName = (TextView) findViewById(R.id.settingname);
        mStatus = (TextView) findViewById(R.id.status);
        mChangeDP = (Button) findViewById(R.id.changedp);
        mChangeStatus = (Button) findViewById(R.id.changestatus);


        mStorage = FirebaseStorage.getInstance().getReference();


        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();

        mDatabase = FirebaseDatabase.getInstance().getReference();
        uid = mCurrentUser.getUid();

        mDatabase = mDatabase.child("users").child(uid);
        mDatabase.keepSynced(true);

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String qname = dataSnapshot.child("name").getValue().toString();
                String qstatus = dataSnapshot.child("status").getValue().toString();
                final String image = dataSnapshot.child("image").getValue().toString();

                mName.setText(qname);
                mStatus.setText(qstatus);

                Picasso.get().load(image).networkPolicy(NetworkPolicy.OFFLINE).into(mCircleImage, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError(Exception e) {

                        Picasso.get().load(image).into(mCircleImage);

                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mChangeStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String newStatus = mStatus.getText().toString();
                Intent status = new Intent(AccountSettingActivity.this, StatusActivity.class);
                status.putExtra("newStatus", newStatus);
                startActivity(status);

            }
        });

        mChangeDP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent gallary = new Intent();
                gallary.setType("image/*");
                gallary.setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(Intent.createChooser(gallary, "SELECT IMAGE"), 1);


            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK) {

            Uri imageUri = data.getData();

            CropImage.activity(imageUri)
                    .setAspectRatio(1,1)
                    .start(this);



        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {

            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK) {

                mProgressDialog = new ProgressDialog(this);
                mProgressDialog.setTitle("Changing Dp");
                mProgressDialog.setMessage("Whait while we create");
                mProgressDialog.show();

                Uri resultUri = result.getUri();

                final StorageReference mProfilePic = mStorage.child("profile_pic").child( uid +".jpg");



                mProfilePic.putFile(resultUri).
                        addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                        if(task.isSuccessful()){

                                        Toast.makeText(AccountSettingActivity.this,"Successfylly Uploaded",Toast.LENGTH_LONG).show();
                                         mProgressDialog.dismiss();

                                        final DatabaseReference mImage = FirebaseDatabase.getInstance().getReference().child("users").child(uid).child("image");

                                        mProfilePic.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri) {
                                                downloadUrl = uri.toString();
                                                mImage.setValue(downloadUrl);

                                                System.out.println(downloadUrl);
                                            }
                                        });

                                }
                                else{

                            Toast.makeText(AccountSettingActivity.this,"Error",Toast.LENGTH_LONG).show();
                                mProgressDialog.dismiss();
                        }

                    }
                });


            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {

                Exception error = result.getError();
            }
        }

    }


}