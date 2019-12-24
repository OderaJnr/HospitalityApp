package com.example.hospitalityapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.hospitalityapp.ViewHolder.ShopViewHolder;
import com.example.hospitalityapp.model.Shops;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class ShopsProfileActivity extends AppCompatActivity {


    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    private DatabaseReference ShopsRef;
    private String type = "";
    private String CurrentUserID, MainCategory;
    String Category = "Hotel And Accommodation";
    String Location = "Kisumu";
    String VENDORSHOPID = "";





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shops_profile);

        VENDORSHOPID = getIntent().getStringExtra("VendorShopID");

        ShopsRef = FirebaseDatabase.getInstance().getReference().child("All Shops");




        CurrentUserID = FirebaseAuth.getInstance().getUid();


        recyclerView = findViewById(R.id.recycler_menu3);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
    }


    @Override
    protected void onStart() {
        super.onStart();



        FirebaseRecyclerOptions<Shops> options = new FirebaseRecyclerOptions.Builder<Shops>()
                .setQuery(ShopsRef.child("ShopDetails").child(VENDORSHOPID)
                        ,Shops.class).build();

        FirebaseRecyclerAdapter<Shops, ShopViewHolder> adapter =
                new FirebaseRecyclerAdapter<Shops, ShopViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull final ShopViewHolder shopViewHolder, int i, @NonNull final Shops shops)
                    {

                        shopViewHolder.ShopPhone.setText(shops.getPhone());
                        shopViewHolder.Shopname.setText(shops.getName());
                        Picasso.get().load(shops.getImage()).into(shopViewHolder.profileimage);



                        shopViewHolder.ShopOverview.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view)
                            {
                                shopViewHolder.ViewDetails.setText(shops.getStatus());
                            }
                        });

                        shopViewHolder.ShopAmenities.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view)
                            {
                                shopViewHolder.ViewDetails.setText("AMENITIES COMING SOON ");
                            }
                        });



                        shopViewHolder.BookButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view)
                            {
                                Intent intent = new Intent(ShopsProfileActivity.this,BookingCheckoutActivity.class);

                                Bundle bundle = new Bundle();
                                bundle.putString("VendorShopID",VENDORSHOPID);
                                bundle.putString("VendorName",shops.getName());
                                intent.putExtras(bundle);

                                startActivity(intent);
                            }
                        });




                        shopViewHolder.ShopPriceList.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view)
                            {

                                Intent intent = new Intent(ShopsProfileActivity.this,MenuActivity.class);

                                Bundle bundle = new Bundle();
                                bundle.putString("VendorShopID",VENDORSHOPID);
                                bundle.putString("VendorLocation",shops.getLocation());
                                bundle.putString("VendorCategory",shops.getCategory());
                                intent.putExtras(bundle);


                                startActivity(intent);

                            }
                        });



                        shopViewHolder.sendmesaagelayout.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view)
                            {
                                Intent intent = new Intent(ShopsProfileActivity.this,MesageAdminActivity.class);

                                Bundle bundle = new Bundle();
                                bundle.putString("receiverID",VENDORSHOPID);
                                bundle.putString("senderID",CurrentUserID);

                                intent.putExtras(bundle);


                                startActivity(intent);


                            }
                        });



                    }

                    @NonNull
                    @Override
                    public ShopViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.shops_profile_layout,parent,false);
                        ShopViewHolder holder = new ShopViewHolder(view);

                        return holder;

                    }
                };


        recyclerView.setAdapter(adapter);
        adapter.startListening();




    }


}