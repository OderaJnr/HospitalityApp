package com.example.hospitalityapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.hospitalityapp.ViewHolder.BookingsViewHolder;
import com.example.hospitalityapp.model.Bookings;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class BookingPaymentActivity extends AppCompatActivity {

    private DatabaseReference BookingRef;

    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    private  String  Quantity;

    private String currentUser;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_payment);



        mAuth= FirebaseAuth.getInstance();
        currentUser=mAuth.getCurrentUser().getUid();


        BookingRef = FirebaseDatabase.getInstance().getReference().child("All Shops").child("Bookings");


        recyclerView=  findViewById(R.id.checkout_menu);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this );
        recyclerView.setLayoutManager(layoutManager);

    }


    @Override
    protected void onStart() {
        super.onStart();



        FirebaseRecyclerOptions<Bookings> options =
                new FirebaseRecyclerOptions.Builder<Bookings>()
                        .setQuery(BookingRef.child("User view").child(currentUser).child("Bookings")
                                ,Bookings.class)
                        .build();

        FirebaseRecyclerAdapter<Bookings, BookingsViewHolder> adapter =
                new FirebaseRecyclerAdapter<Bookings, BookingsViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull final BookingsViewHolder holder, int position, @NonNull final Bookings model)
                    {

                        holder.Username.setText(model.getVendorName());
                        holder.Date.setText(model.getDate());
                        holder.OrderNumber.setText("#0123456");
                        holder.PaymentStatus.setText("PAID");
                        holder.CheckIn.setText("Check-In: "+ model.getCheckIn());
                        holder.CheckOut.setText("Check-Out: "+ model.getCheckOut());





                    }

                    @NonNull
                    @Override
                    public BookingsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
                    {
                        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.my_bookings_layout,viewGroup, false);
                        BookingsViewHolder holder = new BookingsViewHolder(view);
                        return holder;
                    }
                };

        recyclerView.setAdapter(adapter);
        adapter.startListening();



    }





}
