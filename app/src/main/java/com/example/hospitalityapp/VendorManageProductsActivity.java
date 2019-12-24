package com.example.hospitalityapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hospitalityapp.ViewHolder.ProductViewHolder;
import com.example.hospitalityapp.model.Products;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class VendorManageProductsActivity extends AppCompatActivity {


    private DatabaseReference productsRef;

    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    private String currentUser;
    private FirebaseAuth mAuth;



    private static final int Gallery_Pick = 1;

    private String Price,Name,Description,Offer;
    private Uri ImageUri;
    private String saveCurrentDate, saveCurrentTime, productRandomKey, downloadImageUrl;

    private StorageReference ProductImagesRef;
    private ProgressDialog loadingBar;

    private  String VeeLocation,VeeCategory,ProductKey,TypeCategory,VendorName;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_manage_products);


        mAuth= FirebaseAuth.getInstance();
        currentUser=mAuth.getCurrentUser().getUid();
        loadingBar= new ProgressDialog(this);




        productsRef = FirebaseDatabase.getInstance().getReference().child("All Products");
        ProductImagesRef = FirebaseStorage.getInstance().getReference().child("Product Images");



        recyclerView=  findViewById(R.id.manage_productsview);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this );
        recyclerView.setLayoutManager(layoutManager);





    }


    @Override
    protected void onStart() {
        super.onStart();


        Bundle bundle = getIntent().getExtras();
        String vendorLocation =  bundle.getString("Location");
        String VendorCategory = bundle.getString("Category");



        //making the strings accessible
         VeeLocation = vendorLocation;
         VeeCategory = VendorCategory;






        FirebaseRecyclerOptions<Products> options =
                new FirebaseRecyclerOptions.Builder<Products>()
                        .setQuery(productsRef.child("LocationSort").child(vendorLocation).child(VendorCategory).child(currentUser)
                                ,Products.class)
                        .build();
        FirebaseRecyclerAdapter<Products, ProductViewHolder> adapter =
                new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull final ProductViewHolder holder, int position, @NonNull final Products model)
                    {
                        //Getting product Unique key
                        ProductKey = model.getPid();
                        TypeCategory = model.getCategory();
                        VendorName = model.getVendorName();

                        // Showing Product Details
                        holder.productname.setText(model.getPname());
                        holder.productprice.setText(model.getPrice());
                        holder.productDescription.setText( model.getDescription());
                        Picasso.get().load(model.getImage()).into(holder.EditproductImage);



                        holder.EditproductImage.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view)
                            {
                                OpenGallery();

                            }
                        });

                        holder.SaveChangesbtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view)
                            {
                                //Get updated Values from  the Vendor

                                Name =  holder.productname.getText().toString();
                                Description =  holder.productDescription.getText().toString();
                                Price =  holder.productprice.getText().toString();


                                SaveImagetoFirebase();


                            }
                        });







                    }

                    @NonNull
                    @Override
                    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
                    {
                        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.vendormanageproducts_layout,viewGroup, false);
                        ProductViewHolder holder = new ProductViewHolder(view);
                        return holder;
                    }
                };

        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    private void SaveImagetoFirebase()
    {

        saveProductInfo();

//        loadingBar.setTitle("Saving Profile Picture");
//        loadingBar.setMessage("Please Wait");
//        loadingBar.setCanceledOnTouchOutside(false);
//        loadingBar.show();
//
//
//
//        final StorageReference filePath = ProductImagesRef.child("Post Images").child(ImageUri.getLastPathSegment()  + ".jpg");
//
//        final UploadTask uploadTask = filePath.putFile(ImageUri);
//
//
//        uploadTask.addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e)
//            {
//                String message = e.toString();
//
//                Toast.makeText(VendorManageProductsActivity.this, "Error:" + message, Toast.LENGTH_SHORT).show();
//                loadingBar.dismiss();
//
//            }
//        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
//            {
//
//
//                Toast.makeText(VendorManageProductsActivity.this, "Image Uploaded successfully ", Toast.LENGTH_SHORT).show();
//
//                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
//                    @Override
//                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception
//                    {
//                        if (!task.isSuccessful())
//
//                        {
//                            throw task.getException();
//                        }
//                        downloadImageUrl=  filePath.getDownloadUrl().toString();
//                        return filePath.getDownloadUrl();
//                    }
//                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Uri> task)
//                    {
//                        if (task.isSuccessful())
//                        {
//
//                            downloadImageUrl = task.getResult().toString();
//
//                            saveProductInfo();
//                        }
//
//                    }
//                });
//
//            }
//        });


    }

    private void saveProductInfo()
    {

        final HashMap<String,Object> productMap  = new HashMap<>();
        productMap.put("pid",ProductKey);
        productMap.put("date",saveCurrentDate);
        productMap.put("time",saveCurrentTime);
        productMap.put("description",Description);
        productMap.put("image",downloadImageUrl);
        productMap.put("category",VeeCategory);
        productMap.put("price",Price);
        productMap.put("pname",Name);
        productMap.put("poffer",Offer);
        productMap.put("VendorName",VendorName);
        productMap.put("ShopID",currentUser);
        productMap.put("Location",VeeLocation);






        productsRef.child("LocationSort").child(VeeLocation).child(VeeCategory).child(currentUser).child(ProductKey).updateChildren(productMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        if (task.isSuccessful())
                        {

                            productsRef.child("CategorySort").child(VeeLocation).child(VeeCategory).child(ProductKey).updateChildren(productMap);


                            Toast.makeText(VendorManageProductsActivity.this, "Product added Successfully", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(VendorManageProductsActivity.this,MainActivity.class);
                            startActivity(intent);

                            loadingBar.dismiss();
                            loadingBar.dismiss();

                        }
                        else
                        {
                            String message = task.getException().toString();

                            Toast.makeText(VendorManageProductsActivity.this, "Error:" + message, Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();

                        }

                    }
                });
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
//            EditproductImage.setImageURI(ImageUri);


        }

    }









}
