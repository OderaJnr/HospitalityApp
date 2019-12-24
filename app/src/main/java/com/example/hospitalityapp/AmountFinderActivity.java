package com.example.hospitalityapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.hospitalityapp.ViewHolder.ProductViewHolder;
import com.example.hospitalityapp.model.Products;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

public class AmountFinderActivity extends AppCompatActivity {

    private EditText AmountInput;
    private Button SearchButton;

    private Spinner Locationspinner,Categoryspnner;

    private String CAtegory,Location,UserEnteredAmount;
    private DatabaseReference productsRef;

    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_amount_finder);

        AmountInput= (EditText)findViewById(R.id.inputamount);
        SearchButton=(Button)findViewById(R.id.searchbtn);

        productsRef = FirebaseDatabase.getInstance().getReference().child("All Products");


        recyclerView=  findViewById(R.id.recycler6_menu);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this );
        recyclerView.setLayoutManager(layoutManager);





        Categoryspnner = (Spinner)findViewById(R.id.pricecategoryspinner);
        ArrayList<String> arrayList = new ArrayList<>();

        arrayList.add("Select Category");
        arrayList.add("Breakfast");
        arrayList.add("Main Dishes");
        arrayList.add("Fast Foods");
        arrayList.add("Alcoholic Drinks");
        arrayList.add("Non-Alcoholic Drinks/Beverages");
        arrayList.add("Accommodation");




        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arrayList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Categoryspnner.setAdapter(arrayAdapter);

        Categoryspnner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                String category = parent.getItemAtPosition(position).toString();
                CAtegory=category;


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });



        Locationspinner = (Spinner)findViewById(R.id.pricelocationSpinner);

        ArrayList<String> arrayList2 = new ArrayList<>();

        arrayList2.add("Select Your City/Town");
        arrayList2.add("Nairobi");
        arrayList2.add("Mombasa");
        arrayList2.add("Kisumu");
        arrayList2.add("Eldoret");
        arrayList2.add("Nakuru");
        arrayList2.add("Thika");
        arrayList2.add("Malindi");
        arrayList2.add("Machakos");
        arrayList2.add("Naivasha");


        ArrayAdapter<String> arrayAdapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arrayList2);
        arrayAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Locationspinner.setAdapter(arrayAdapter2);

        Locationspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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










        SearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                String AmounKeyed = AmountInput.getText().toString();
                Toast.makeText(AmountFinderActivity.this, AmounKeyed, Toast.LENGTH_SHORT).show();
                UserEnteredAmount = AmounKeyed;

                if (TextUtils.isEmpty(AmountInput.getText()))
                {
                    Toast.makeText(AmountFinderActivity.this, "Please enter amount", Toast.LENGTH_SHORT).show();
                }

                else if (Location=="Select Your City/Town")
                {
                    Toast.makeText(AmountFinderActivity.this, "Please select Location", Toast.LENGTH_SHORT).show();


                }


                else if (CAtegory=="Select Category")
                {
                    Toast.makeText(AmountFinderActivity.this, "Please select Category", Toast.LENGTH_SHORT).show();

                }


                else
                    {
                        GetitemswithkeyedAmount();

                    }




            }
        });
    }


    private void GetitemswithkeyedAmount()
    {



        final Query query  = productsRef.child("CategorySort").child(Location).child(CAtegory).orderByChild("price").endAt(UserEnteredAmount);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                        // do with your result
                        String Snap = data.getValue().toString();



                        // Show Filtered Products


                        FirebaseRecyclerOptions<Products> options =
                                new FirebaseRecyclerOptions.Builder<Products>()
                                        .setQuery(query
                                                ,Products.class)
                                        .build();
                        FirebaseRecyclerAdapter<Products, ProductViewHolder> adapter =
                                new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
                                    @Override
                                    protected void onBindViewHolder(@NonNull final ProductViewHolder holder, int position, @NonNull final Products model)
                                    {
                                        holder.Productname.setText(model.getPname());
                                        holder.Productprice.setText("Ksh"+ " "+ model.getPrice());
                                        holder.ProductDescription.setText( model.getDescription());
                                        holder.Sellername.setText(model.getVendorName());
                                        Picasso.get().load(model.getImage()).into(holder.ProductImage);




                                    }

                                    @NonNull
                                    @Override
                                    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
                                    {
                                        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.price_finder_menu_layout,viewGroup, false);
                                        ProductViewHolder holder = new ProductViewHolder(view);
                                        return holder;
                                    }
                                };

                        recyclerView.setAdapter(adapter);
                        adapter.startListening();




                    }

                } else {
                    Toast.makeText(AmountFinderActivity.this, "Get Prices method not working", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


    @Override
    protected void onStart() {
        super.onStart();




        //before inflating the custom alert dialog layout, we will get the current activity viewgroup
        ViewGroup viewGroup = findViewById(android.R.id.content);

        //then we will inflate the custom alert dialog xml that we created
        View dialogView = LayoutInflater.from(AmountFinderActivity.this).inflate(R.layout.customized_amountfinderdialogue_box, viewGroup, false);


        //Now we need an AlertDialog.Builder object
        AlertDialog.Builder builder = new AlertDialog.Builder(AmountFinderActivity.this);

        //setting the view of the builder to our custom view that we already inflated
        builder.setView(dialogView);


        builder.setPositiveButton(
                "Proceed",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();


                    }
                });

        builder.setNegativeButton(
                "Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();

                        Intent intent = new Intent(AmountFinderActivity.this,MainActivity .class);
                        startActivity(intent);

                    }
                });



        //finally creating the alert dialog and displaying it
        AlertDialog alertDialog = builder.create();
        alertDialog.show();


    }
}
