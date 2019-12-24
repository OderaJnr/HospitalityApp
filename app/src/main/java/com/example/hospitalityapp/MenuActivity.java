package com.example.hospitalityapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hospitalityapp.ViewHolder.ProductViewHolder;
import com.example.hospitalityapp.model.Products;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class MenuActivity extends AppCompatActivity {


    private DatabaseReference productsRef;

    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    private Button orderbtn;
    private TextView TotalpricetextView;
    private  String  Quantity,productid,productname,productprice,vendorname,vendorID;
    private int TotalPriceofanItem;

    private String currentUser;
    private String Username,UserPhonenumber;
    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;
    private LinearLayout LinearLayout;
    private  int overTotalPrice = 0;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        mAuth= FirebaseAuth.getInstance();
        currentUser=mAuth.getCurrentUser().getUid();

        progressDialog =new ProgressDialog(this);


        productsRef = FirebaseDatabase.getInstance().getReference().child("All Products");


        recyclerView=  findViewById(R.id.recycler6_menu);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this );
        recyclerView.setLayoutManager(layoutManager);

        LinearLayout= (LinearLayout)findViewById(R.id.linearlayoutt);
        TotalpricetextView = findViewById(R.id.Totalpricetxview);
        orderbtn = (Button)findViewById(R.id.orderbtn);
        orderbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Makeorder();
            }
        });

    }

    private void Makeorder()
    {
        progressDialog.setTitle("Processing your order");
        progressDialog.setMessage("please wait..");
        progressDialog.show();



        String saveCurrentDate,saveCurrentTime,OrderID;


        Calendar callForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd,yyyy");
        saveCurrentDate = currentDate.format(callForDate.getTime());


        Calendar callForTime = Calendar.getInstance();
        SimpleDateFormat currentTime = new SimpleDateFormat("hh:mm a");
        saveCurrentTime = currentTime.format(callForTime.getTime());

        OrderID= saveCurrentDate+saveCurrentTime;



        final DatabaseReference OrderRef = FirebaseDatabase.getInstance().getReference().child("All Shops").child("Bookings");

        final HashMap<String, Object> ordermap = new HashMap<>();
        ordermap.put("bookingID",OrderID);
        ordermap.put("date",saveCurrentDate);
        ordermap.put("time",saveCurrentTime);
        ordermap.put("vendorName",vendorname);
        ordermap.put("userName",Username);
        ordermap.put("vendorID",vendorID);
        ordermap.put("Userphone",UserPhonenumber);
        ordermap.put("TotalAmount",String.valueOf(overTotalPrice));



        OrderRef.child("User view").child(currentUser).child("Bookings").child(OrderID).updateChildren(ordermap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        if (task.isSuccessful())
                        {
                            OrderRef.child("Vendor view").child(vendorID).child("Bookings").child(productid).updateChildren(ordermap);
                            Toast.makeText(MenuActivity.this, "Product ordered  successfully please pay to complete the order ", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(MenuActivity.this,BookingPaymentActivity.class);
                            startActivity(intent);

                            progressDialog.dismiss();

                        }
                        else
                        {
                            Toast.makeText(MenuActivity.this, "Error Occurred Please Try Again", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();

                        }

                    }
                });

    }


    @Override
    protected void onStart() {
        super.onStart();

        Bundle bundle = getIntent().getExtras();
        final String VendorId = bundle.getString("VendorShopID");
        String vendorLocation =  bundle.getString("VendorLocation");
        String VendorCategory = bundle.getString("VendorCategory");

        vendorID = VendorId;

        GetUsername();


        progressDialog.setTitle("Loading Menu");
        progressDialog.setMessage("please wait..");
        progressDialog.show();



        FirebaseRecyclerOptions<Products> options =
                new FirebaseRecyclerOptions.Builder<Products>()
                        .setQuery(productsRef.child("LocationSort").child(vendorLocation).child(VendorCategory).child(VendorId)
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
                        Picasso.get().load(model.getImage()).into(holder.ProductImage);



                        progressDialog.dismiss();



                        holder.AddToCart.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                progressDialog.setTitle("Adding this product");
                                progressDialog.setMessage("please wait..");
                                progressDialog.show();


                                String quantityfromnumber = holder.elegantNumberButton.getNumber();
                                Quantity = quantityfromnumber;

                                int convertedquantity = Integer.parseInt(quantityfromnumber);


                                if ( convertedquantity>=1)
                                {

                                    //Calculating Total Price of an item

                                    int OneitemPrice = Integer.parseInt(model.getPrice());
                                    int quantity = Integer.parseInt(Quantity);

                                    int Totalprice = OneitemPrice *quantity;
                                    TotalPriceofanItem = Totalprice;

                                    overTotalPrice = overTotalPrice + Totalprice;


                                    final DatabaseReference cartlist = FirebaseDatabase.getInstance().getReference().child("All Shops").child("CartList");



                                    final HashMap<String, Object> cartmap = new HashMap<>();
                                    cartmap.put("pid",model.getPid());
                                    cartmap.put("pname",model.getPname());
                                    cartmap.put("price",model.getPrice());
                                    cartmap.put("quantity",Quantity);
                                    cartmap.put("VendorName",model.getVendorName());





                                    cartlist.child("User view").child(currentUser).child(model.getPid()).child("Products").updateChildren(cartmap)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task)
                                                {
                                                    if (task.isSuccessful())
                                                    {
                                                        cartlist.child("Vendor view").child(VendorId).child(model.getPid()).child("Products").updateChildren(cartmap);
                                                        Toast.makeText(MenuActivity.this, "Product selected successfully ", Toast.LENGTH_SHORT).show();

                                                        holder.AddToCart.setImageResource(R.drawable.checkedtick);



                                                        String ProductID    =  model.getPid();
                                                        String ProductName  =  model.getPname();
                                                        String ProductPrice =  model.getPrice();
                                                        String Vendorname   =  model.getVendorName();

                                                        productid=ProductID;
                                                        productname= ProductName;
                                                        productprice=ProductPrice;
                                                        vendorname= Vendorname;


                                                        LinearLayout.setVisibility(View.VISIBLE);
                                                        TotalpricetextView.setText("Total Amount = Ksh " + String.valueOf(overTotalPrice)+".00");



                                                        progressDialog.dismiss();






                                                    }
                                                    else
                                                    {
                                                        progressDialog.dismiss();

                                                        Toast.makeText(MenuActivity.this, "Error Occurred Please Try Again", Toast.LENGTH_SHORT).show();
                                                    }

                                                }
                                            });


                                } else{
                                    Toast.makeText(MenuActivity.this, "Select quantity", Toast.LENGTH_SHORT).show();
                                    progressDialog.dismiss();

                                }
                            }
                        });






                    }

                    @NonNull
                    @Override
                    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
                    {
                        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.shop_menu_products_layout,viewGroup, false);
                        ProductViewHolder holder = new ProductViewHolder(view);
                        return holder;
                    }
                };

        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }




    private void GetUsername()
    {

        DatabaseReference UserRef = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUser).child("Details");


        UserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                if (dataSnapshot.exists())
                {

                    String sName = dataSnapshot.child("name").getValue().toString();
                    String Sphone = dataSnapshot.child("phone").getValue().toString();


                    Username = sName;
                    UserPhonenumber = Sphone;




                }else
                {
                    Toast.makeText(MenuActivity.this, "No info saved in your profile", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {

            }
        });
    }


}
