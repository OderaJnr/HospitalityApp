package com.example.hospitalityapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import de.hdodenhof.circleimageview.CircleImageView;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class ProfileSettingsActivity extends AppCompatActivity {

    private  static  final int Gallery_Pick=1;
    private EditText mNamefield,mPhonefield,mEmailfield,mLocationfield;
    private Button mSave;
    private CircleImageView mProfileimage;

    private ProgressDialog progressDialog;



    private Uri imageUrl;
    private  String myUrl= "";
    private String currentUser;
    private FirebaseAuth mAuth;
    private StorageReference storageProfilePicturesRef;
    private  String checker= "";
    private StorageTask uploadtask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_settings);



        progressDialog=new ProgressDialog(this);
        mAuth= FirebaseAuth.getInstance();
        currentUser=mAuth.getCurrentUser().getUid();

        storageProfilePicturesRef = FirebaseStorage.getInstance().getReference().child("profile pictures");

        mNamefield=findViewById(R.id.username);
        mPhonefield=findViewById(R.id.phonenumber);
        mEmailfield=findViewById(R.id.email);
        mLocationfield=findViewById(R.id.location);
        mSave= (Button)findViewById(R.id.savebutton);




        mProfileimage= findViewById(R.id.profileImage);

        mProfileimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent,Gallery_Pick);

            }
        });





        userInfoDisplay(mProfileimage, mNamefield,mPhonefield,mEmailfield,mLocationfield);

        mSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                saveUserInfo();


            }
        });






    }

    private void saveUserInfo()
    {

        if (TextUtils.isEmpty(mNamefield.getText()))
        {
            Toast.makeText(this, "Please enter your name", Toast.LENGTH_SHORT).show();
        }

        else if (TextUtils.isEmpty(mPhonefield.getText()))
        {
            Toast.makeText(this, "Please enter your phone number", Toast.LENGTH_SHORT).show();
        }

        else if (TextUtils.isEmpty(mLocationfield.getText()))
        {
            Toast.makeText(this, "Please enter your location", Toast.LENGTH_SHORT).show();
        }



        else
        {
            uploadImage();
        }


    }



    private void uploadImage()
    {

        progressDialog.setTitle("Updating Profile");
        progressDialog.setMessage("please wait...");
        progressDialog.setCanceledOnTouchOutside(false);

        if (imageUrl!= null)
        {

            final StorageReference fileRef = storageProfilePicturesRef
                    .child(currentUser+ ".jpg");
            uploadtask = fileRef.putFile(imageUrl);

            uploadtask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception
                {
                    if (!task.isSuccessful())
                    {
                        throw  task.getException();
                    }
                    return fileRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>()
            {
                @Override
                public void onComplete(@NonNull Task<Uri>task)
                {
                    if (task.isSuccessful())
                    {
                        Uri downloadUrl  = task.getResult();
                        myUrl = downloadUrl.toString();

                        DatabaseReference ref =  FirebaseDatabase.getInstance().getReference().child("Users");

                        HashMap<String,Object> userMap = new HashMap<>();
                        userMap.put("name",mNamefield.getText().toString());
                        userMap.put("phone",mPhonefield.getText().toString());
                        userMap.put("email",mEmailfield.getText().toString());
                        userMap.put("location",mLocationfield.getText().toString());

                        userMap.put("image",myUrl);

                        ref.child(currentUser).child("Details").updateChildren(userMap);

                        progressDialog.dismiss();

                        startActivity(new Intent(ProfileSettingsActivity.this,MainActivity.class));
                        Toast.makeText(ProfileSettingsActivity.this, "Profile Information  Updated Successfully", Toast.LENGTH_SHORT).show();
                        finish();
                    } else
                    {
                        progressDialog.dismiss();
                        Toast.makeText(ProfileSettingsActivity.this, "Error Occurred ", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else
        {
            updateonlyuserInfo();
        }



    }

    private void updateonlyuserInfo()
    {

        progressDialog.setTitle("Updating Profile");
        progressDialog.setMessage("please wait...");
        progressDialog.setCanceledOnTouchOutside(false);

        DatabaseReference ref =  FirebaseDatabase.getInstance().getReference().child("Users");

        HashMap<String,Object> userMap = new HashMap<>();
        userMap.put("name",mNamefield.getText().toString());
        userMap.put("phone",mPhonefield.getText().toString());
        userMap.put("email",mEmailfield.getText().toString());
        userMap.put("location",mLocationfield.getText().toString());


        ref.child(currentUser).child("Details").updateChildren(userMap);



        startActivity(new Intent(ProfileSettingsActivity.this,MainActivity.class));

        progressDialog.dismiss();
        Toast.makeText(ProfileSettingsActivity.this, "Profile Information  Updated Successfully", Toast.LENGTH_SHORT).show();
        finish();
    }


    private void userInfoDisplay(final CircleImageView mProfileimage, final EditText mNamefield, final EditText mPhonefield, final EditText mEmailfield, final EditText mLocationfield)
    {
        DatabaseReference UserRef = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUser).child("Details");
        UserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                if (dataSnapshot.exists())
                {
                    if (dataSnapshot.child("image").exists())
                    {
                        String image = dataSnapshot.child("image").getValue().toString();
                        String name = dataSnapshot.child("name").getValue().toString();
                        String phone = dataSnapshot.child("phone").getValue().toString();
                        String email = dataSnapshot.child("email").getValue().toString();
                        String location = dataSnapshot.child("location").getValue().toString();


                        Picasso.get().load(image).into(mProfileimage);

                        mNamefield.setText(name);
                        mPhonefield.setText(phone);
                        mEmailfield.setText(email);
                        mLocationfield.setText(location);






                    }
                }
            }




            @Override
            public void onCancelled(DatabaseError databaseError)
            {

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==1 && resultCode == Activity.RESULT_OK)
        {
            final Uri imageUri= data.getData();
            imageUrl=imageUri;
            mProfileimage.setImageURI(imageUrl);

        } else
        {
            Toast.makeText(this, "Error Occurred", Toast.LENGTH_SHORT).show();
        }
    }

}
