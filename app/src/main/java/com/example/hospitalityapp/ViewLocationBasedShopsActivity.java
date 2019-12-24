package com.example.hospitalityapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hospitalityapp.ViewHolder.ShopViewHolder;
import com.example.hospitalityapp.model.Shops;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ViewLocationBasedShopsActivity extends AppCompatActivity {

    private DatabaseReference ShopsRef;

    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    private  String type = "";
    private String CurrentUserID,Location,MainCategory,SubCategory,VendorShopID;

    private ProgressDialog progressDialog;



    Button positiveDialog,negativeDialog;
    AlertDialog.Builder dialogBuilder;
    AlertDialog alertDialog;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_location_based_shops);

        progressDialog =new ProgressDialog(this);




        Bundle bundle = getIntent().getExtras();
        String Category = bundle.getString("Hotel And Accommodation");
        String Category2 =  bundle.getString("Night Club And Bars");
        String Category3 = bundle.getString("Restaurants and Food Joints");
        String Category4 = bundle.getString("Fun Activities");
        String Category5 = bundle.getString("Add Event Around");
        String SubCategory1 = bundle.getString("SubCategory");


        Toast.makeText(this, SubCategory1, Toast.LENGTH_SHORT).show();
        SubCategory = SubCategory1;





        if (Category != null)
        {

            MainCategory=Category;
            Toast.makeText(this,Category, Toast.LENGTH_SHORT).show();

        }

        else if (Category2 != null)
        {


            MainCategory=Category2;
            Toast.makeText(this,Category2 , Toast.LENGTH_SHORT).show();
        }

        else if (Category3 != null)
        {
            MainCategory=Category3;
            Toast.makeText(this,Category3 , Toast.LENGTH_SHORT).show();
        }

        else if (Category4 != null)
        {
            MainCategory=Category4;
            Toast.makeText(this,Category4 , Toast.LENGTH_SHORT).show();
        }
        else if (Category5 != null)
        {

            MainCategory=Category5;
            Toast.makeText(this,Category5 , Toast.LENGTH_SHORT).show();
        }

        else
        {
            Toast.makeText(this, "No Category Selected", Toast.LENGTH_SHORT).show();
        }






        ShopsRef = FirebaseDatabase.getInstance().getReference().child("All Shops");
        CurrentUserID = FirebaseAuth.getInstance().getUid();




        recyclerView=  findViewById(R.id.recycler_menu);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this );
        recyclerView.setLayoutManager(layoutManager);










        //Spinner

        Spinner spinner = findViewById(R.id.locatiospinner);
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
        spinner.setAdapter(arrayAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                String town = parent.getItemAtPosition(position).toString();
                Location=town;

                if (town == "Select Your City/Town")
                {
                    Snackbar.make(view, "Please Select Your Town", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();

                } else
                {
                    Toast.makeText(ViewLocationBasedShopsActivity.this, Location, Toast.LENGTH_SHORT).show();
                    CheckIfShopsareavailable();


                }



            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });


    }

    private void CheckIfShopsareavailable() {


        progressDialog.setTitle("Searching...");
        progressDialog.setMessage("please wait..");
        progressDialog.show();


        DatabaseReference ShopsReference = FirebaseDatabase.getInstance().getReference().child("All Shops").child(Location).child(MainCategory).child(SubCategory).child("ShopDetails");
        ShopsReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                if (dataSnapshot.exists())
                {
                    SearchShops();


                } else{



                    //before inflating the custom alert dialog layout, we will get the current activity viewgroup
                    ViewGroup viewGroup = findViewById(android.R.id.content);

                    //then we will inflate the custom alert dialog xml that we created
                    View dialogView = LayoutInflater.from(ViewLocationBasedShopsActivity.this).inflate(R.layout.customized_dialogue_box, viewGroup, false);


                    //Now we need an AlertDialog.Builder object
                    AlertDialog.Builder builder = new AlertDialog.Builder(ViewLocationBasedShopsActivity.this);

                    //setting the view of the builder to our custom view that we already inflated
                    builder.setView(dialogView);


                    builder.setPositiveButton(
                            "Yes",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    Intent intent = new Intent(ViewLocationBasedShopsActivity.this,MainActivity.class);
                                    startActivity(intent);

                                }
                            });

                    builder.setNegativeButton(
                            "No",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });



                    //finally creating the alert dialog and displaying it
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();





                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void SearchShops()
    {

        FirebaseRecyclerOptions<Shops> options = new FirebaseRecyclerOptions.Builder<Shops>()
                .setQuery(ShopsRef.child(Location).child(MainCategory).child(SubCategory).child("ShopDetails")
                        ,Shops.class).build();

        FirebaseRecyclerAdapter<Shops, ShopViewHolder> adapter =
                new FirebaseRecyclerAdapter<Shops, ShopViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull final ShopViewHolder shopViewHolder, int i, @NonNull final Shops shops)
                    {

                        shopViewHolder.ShopPhone.setText(shops.getPhone());
                        shopViewHolder.Shopname.setText(shops.getName());
                        shopViewHolder.Shopstatus.setText(shops.getStatus());
                        shopViewHolder.location.setText(shops.getLocation());
                        shopViewHolder.RatesHotel.setText(shops.getGeneralRates());
                        Picasso.get().load(shops.getImage()).into(shopViewHolder.profileimage);

                        progressDialog.dismiss();



                        shopViewHolder.ViewshopButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view)
                            {
                                Intent intent = new Intent(ViewLocationBasedShopsActivity.this,ShopsProfileActivity.class);
                                intent.putExtra("VendorShopID",shops.getShopIdentity());
                                startActivity(intent);

                            }
                        });




                    }

                    @NonNull
                    @Override
                    public ShopViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.shops_list_layout,parent,false);
                        ShopViewHolder holder = new ShopViewHolder(view);

                        return holder;

                    }
                };


        recyclerView.setAdapter(adapter);
        adapter.startListening();

    }


    @Override
    protected void onStart() {
        super.onStart();

        Toast.makeText(this, MainCategory, Toast.LENGTH_SHORT).show();
    }
}


