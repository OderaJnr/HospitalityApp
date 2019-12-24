package com.example.hospitalityapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import de.hdodenhof.circleimageview.CircleImageView;

public class BusinessProfileSettingActivity extends AppCompatActivity {

    private EditText Username,Status,Phone,email,Timeline,Rates;
    private CircleImageView Profileimage;
    private Button SaveButton;
    private ProgressDialog loadingBar;

    private  static  final int Gallery_Pick=1;


    private Uri imageUrl;
    private  String myUrl= "";
    private StorageReference storageProfilePicturesRef;
    private  String checker= "";
    private  String Location,Category,SubCategory;

    private StorageTask uploadtask;
    private ProgressDialog progressDialog;

    private String currentUser,downloadImageUrl;
    private FirebaseAuth mAuth;

    private Uri ImageUri;
    private Spinner Spinnerlocation,Spinnercategory, Spinnersubcate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_profile_setting);



        mAuth= FirebaseAuth.getInstance();
        currentUser=mAuth.getCurrentUser().getUid();



        progressDialog=new ProgressDialog(this);
        storageProfilePicturesRef = FirebaseStorage.getInstance().getReference().child("profile pictures");


        Username = (EditText)findViewById(R.id.username);
        Status = (EditText)findViewById(R.id.status);
        Phone = (EditText)findViewById(R.id.phonenumber);
        email = (EditText)findViewById(R.id.email);
        Timeline = (EditText)findViewById(R.id.hours);
        Rates = findViewById(R.id.rates);


         Spinnerlocation = findViewById(R.id.location);
         Spinnercategory = findViewById(R.id.category);
         Spinnersubcate = findViewById(R.id.sub_category);




        loadingBar= new ProgressDialog(this);





        Profileimage = (CircleImageView) findViewById(R.id.profileImage);
        SaveButton = (Button) findViewById(R.id.savebutton);




        Profileimage.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                OpenGallery();
            }
        });





        SaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                SaveprofileImagetoFirebase();


            }
        });



//        DisplayUserInfo(Profileimage,Username,Status,Phone,email,Timeline);
        DisplayUserInfoinsettings(Profileimage,Username,Status,Phone,email,Timeline,Spinnercategory,Spinnerlocation,Spinnersubcate);



        //Spinner

        ArrayList<String> arrayList = new ArrayList<>();

        arrayList.add("Select Your City/Town");
        arrayList.add("Nairobi");
        arrayList.add("Mombasa");
        arrayList.add("Kisumu");
        arrayList.add("Eldoret");
        arrayList.add("Nakuru");
        arrayList.add("Thika");
        arrayList.add("Malindi");
        arrayList.add("Machakos");
        arrayList.add("Naivasha");


        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arrayList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinnerlocation.setAdapter(arrayAdapter);

        Spinnerlocation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                String town = parent.getItemAtPosition(position).toString();
                Location=town;


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });

        ArrayList<String> arrayList2 = new ArrayList<>();

        arrayList2.add("Select The Category");
        arrayList2.add("Hotel And Accommodation");
        arrayList2.add("Night Club And Bars");
        arrayList2.add("Restaurants and Food Joints");


        ArrayAdapter<String> arrayAdapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arrayList2);
        arrayAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinnercategory.setAdapter(arrayAdapter2);

        Spinnercategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                String category = parent.getItemAtPosition(position).toString();

                Category = category;

                Toast.makeText(BusinessProfileSettingActivity.this, category, Toast.LENGTH_SHORT).show();


                FindSubCategory();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {
            }
        });
















    }

    private void DisplayUserInfoinsettings(final CircleImageView profileimage, final EditText username, final EditText status, final EditText phone, final EditText email, final EditText timeline, final Spinner spinnercategory, final Spinner spinnerlocation, final Spinner spinnersubcate)
    {
        DatabaseReference UserRef = FirebaseDatabase.getInstance().getReference().child("All Shops").child("ShopDetails").child(currentUser).child("Details");

        UserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                if (dataSnapshot.exists())
                {
                    String sName = dataSnapshot.child("Name").getValue().toString();
                    String sStatus = dataSnapshot.child("Status").getValue().toString();
                    String sPhone = dataSnapshot.child("Phone").getValue().toString();
                    String sEmail = dataSnapshot.child("Email").getValue().toString();
                    String sTimeline = dataSnapshot.child("Timeline").getValue().toString();
                    String sRates = dataSnapshot.child("GeneralRates").getValue().toString();
                    String image = dataSnapshot.child("image").getValue().toString();
                    String Category = dataSnapshot.child("Category").getValue().toString();
                    String Subcategory = dataSnapshot.child("Sub-Category").getValue().toString();
                    String Location = dataSnapshot.child("Location").getValue().toString();






                    username.setText(sName);
                    status.setText(sStatus);
                    email.setText(sEmail);
                    phone.setText(sPhone);
                    phone.setText(sRates);
                    timeline.setText(sTimeline);
                    //default messages on spiners not showing
                    spinnerlocation.setPrompt(Location);
                    spinnersubcate.setPrompt(Subcategory);



                    Picasso.get().load(image).into(profileimage);






                }else
                {
                    Toast.makeText(BusinessProfileSettingActivity.this, "No info saved in your profile", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {

            }
        });

    }







    private void FindSubCategory()
    {
        if (Category=="Hotel And Accommodation")

        {
            ArrayList<String> arrayList3 = new ArrayList<>();

            arrayList3.add("Select The Sub-category");
            arrayList3.add("Hotels");
            arrayList3.add("Apartments");
            arrayList3.add("Hostels");


            ArrayAdapter<String> arrayAdapter3 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arrayList3);
            arrayAdapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            Spinnersubcate.setAdapter(arrayAdapter3);

            Spinnersubcate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
                {
                    String Subcategory = parent.getItemAtPosition(position).toString();

                    SubCategory = Subcategory;

                    Toast.makeText(BusinessProfileSettingActivity.this, Subcategory, Toast.LENGTH_SHORT).show();

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent)
                {
                }
            });


        } else if (Category == "Night Club And Bars")
            {

                Spinner spinner3 = findViewById(R.id.sub_category);
                ArrayList<String> arrayList3 = new ArrayList<>();

                arrayList3.add("Select The Sub-category");
                arrayList3.add("Club/Disco");
                arrayList3.add("Lounge");
                arrayList3.add("Bar/Pub");


                ArrayAdapter<String> arrayAdapter3 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arrayList3);
                arrayAdapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner3.setAdapter(arrayAdapter3);

                spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
                    {
                        String Subcategory = parent.getItemAtPosition(position).toString();

                        SubCategory = Subcategory;

                        Toast.makeText(BusinessProfileSettingActivity.this, Subcategory, Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent)
                    {
                    }
                });

            }

        else if (Category == "Restaurants and Food Joints")
        {

            Spinner spinner3 = findViewById(R.id.sub_category);
            ArrayList<String> arrayList3 = new ArrayList<>();

            arrayList3.add("Select The Sub-category");
            arrayList3.add("Fine Dining");
            arrayList3.add("Casual Dining");
            arrayList3.add("Fast Food Joints");
            arrayList3.add("Cafeteria");
            arrayList3.add("Take-away Counters");
            arrayList3.add("Food Deliveries");


            ArrayAdapter<String> arrayAdapter3 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arrayList3);
            arrayAdapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner3.setAdapter(arrayAdapter3);

            spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
                {
                    String Subcategory = parent.getItemAtPosition(position).toString();

                    SubCategory = Subcategory;

                    Toast.makeText(BusinessProfileSettingActivity.this, Subcategory, Toast.LENGTH_SHORT).show();

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent)
                {
                }
            });

        }




    }















    private void SaveprofileImagetoFirebase()
    {

        loadingBar.setTitle("Saving Profile Picture");
        loadingBar.setMessage("Please Wait");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();



        final StorageReference filePath = storageProfilePicturesRef.child("Profile Images").child(ImageUri.getLastPathSegment()  + ".jpg");

        final UploadTask uploadTask = filePath.putFile(ImageUri);


        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e)
            {
                String message = e.toString();

                Toast.makeText(BusinessProfileSettingActivity.this, "Error:" + message, Toast.LENGTH_SHORT).show();
                loadingBar.dismiss();

            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
            {


                Toast.makeText(BusinessProfileSettingActivity.this, "Profile Image Uploaded successfully ", Toast.LENGTH_SHORT).show();

                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception
                    {
                        if (!task.isSuccessful())

                        {
                            throw task.getException();
                        }
                        downloadImageUrl=  filePath.getDownloadUrl().toString();
                        return filePath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task)
                    {
                        if (task.isSuccessful())
                        {

                            downloadImageUrl = task.getResult().toString();

                            saveUserInfo();
                        }

                    }
                });

            }
        });

    }









    private void saveUserInfo()
    {
        progressDialog.setTitle("Saving Profile Details");
        progressDialog.setMessage("please wait...");
        progressDialog.setCanceledOnTouchOutside(false);

        DatabaseReference ref =  FirebaseDatabase.getInstance().getReference().child("All Shops");

        HashMap<String,Object> userMap = new HashMap<>();
        userMap.put("Name",Username.getText().toString());
        userMap.put("Phone",Phone.getText().toString());
        userMap.put("Status",Status.getText().toString());
        userMap.put("Email",email.getText().toString());
        userMap.put("Timeline",Timeline.getText().toString());
        userMap.put("GeneralRates",Rates.getText().toString());
        userMap.put("Category",Category);
        userMap.put("Sub-Category",SubCategory);
        userMap.put("Location",Location);
        userMap.put("ShopIdentity",currentUser);
        userMap.put("image",downloadImageUrl);


        ref.child(Location).child(Category).child(SubCategory).child("ShopDetails").child(currentUser).updateChildren(userMap);
        ref.child("ShopDetails").child(currentUser).child("Details").updateChildren(userMap);

        startActivity(new Intent(BusinessProfileSettingActivity.this,MainActivity.class));

        progressDialog.dismiss();
        Toast.makeText(BusinessProfileSettingActivity.this, "Profile Information  Updated Successfully", Toast.LENGTH_SHORT).show();
        finish();

    }









    private void OpenGallery()
    {
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent,Gallery_Pick);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==Gallery_Pick && resultCode == RESULT_OK && data!= null)
        {
            ImageUri = data.getData();
            Profileimage.setImageURI(ImageUri);


        }

    }




}

