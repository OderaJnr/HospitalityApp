package com.example.hospitalityapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class BookingCheckoutActivity extends AppCompatActivity {

    private TextView CheckinTxtview,Checkout,GuestsTextview,Username;
    private LinearLayout DetailsLayout;
    private EditText PhoneNumber;
    private Button BookButton;
    private ElegantNumberButton Adultsbtn,Childrenbtn,Infantsbtn;
    private ProgressDialog progressDialog;




    private String currentUser,VendorName,VendorID,UserPhonenumber,AdultsQuantity,ChildrenQuantity,InfantsQuantity,CheckinDate,CheckoutDate,username;


    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_checkout);

        mAuth= FirebaseAuth.getInstance();
        currentUser=mAuth.getCurrentUser().getUid();
        progressDialog =new ProgressDialog(this);

        GuestsTextview = (TextView)findViewById(R.id.guests) ;
        DetailsLayout = (LinearLayout) findViewById(R.id.checkoutlayout) ;
        Username= (TextView)findViewById(R.id.order_user_name);
        PhoneNumber = (EditText)findViewById(R.id.phone_number);
        BookButton = (Button)findViewById(R.id.book_button);
        Adultsbtn = (ElegantNumberButton)findViewById(R.id.adultselegantbtn) ;
        Childrenbtn = (ElegantNumberButton)findViewById(R.id.childrenButton) ;
        Infantsbtn = (ElegantNumberButton)findViewById(R.id.infantsbtn) ;


        GuestsTextview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                DetailsLayout.setVisibility(View.VISIBLE);
                Toast.makeText(BookingCheckoutActivity.this, "Number of Guests", Toast.LENGTH_SHORT).show();

            }
        });




        CheckinTxtview= (TextView)findViewById(R.id.checkin);
        CheckinTxtview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(BookingCheckoutActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {

                    }
                },year,month,dayOfMonth);

                datePickerDialog.show();

                CheckinTxtview.setText("CHECK-IN "+dayOfMonth + "/" + (month+ 1) + "/" + year);

                CheckinDate = dayOfMonth+ "/"+(month+1)+"/"+year;

            }
        });


        Checkout = (TextView)findViewById(R.id.checkout);

        Checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);



                DatePickerDialog datePickerDialog = new DatePickerDialog(BookingCheckoutActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {

                    }
                },year,month,dayOfMonth);

                datePickerDialog.show();

                Checkout.setText("CHECK-OUT "+dayOfMonth + "/" + (month+ 1) + "/" + year);
                CheckoutDate = dayOfMonth+ "/"+(month+1)+"/"+year;

            }
        });


        BookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {

                GetnatureandnumberofGuests();


            }
        });


    }

    private void GetnatureandnumberofGuests()
    {

        progressDialog.setTitle("Processing Your Booking");
        progressDialog.setMessage("please wait..");
        progressDialog.show();

        //Getting Values from elegant number button i.e number of guests booking


        String quantityadults = Adultsbtn.getNumber();
        AdultsQuantity = quantityadults;

        String quantityChildren = Childrenbtn.getNumber();
        ChildrenQuantity = quantityChildren;

        String quantityInfants = Infantsbtn.getNumber();
        InfantsQuantity = quantityInfants;


                        FetchUserBookingData();

    }

    private void FetchUserBookingData()
    {




        final String saveCurrentDate,saveCurrentTime,BookID;


        Calendar callForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd,yyyy");
        saveCurrentDate = currentDate.format(callForDate.getTime());


        Calendar callForTime = Calendar.getInstance();
        SimpleDateFormat currentTime = new SimpleDateFormat("hh:mm a");
        saveCurrentTime = currentTime.format(callForTime.getTime());

        BookID= saveCurrentDate+saveCurrentTime;



        final DatabaseReference Bookingref = FirebaseDatabase.getInstance().getReference().child("All Shops").child("Bookings");

        final HashMap<String, Object> bookingMap = new HashMap<>();
        bookingMap.put("bookingID",BookID);
        bookingMap.put("date",saveCurrentDate);
        bookingMap.put("time",saveCurrentTime);
        bookingMap.put("vendorName",VendorName);
        bookingMap.put("userName",username);
        bookingMap.put("vendorID",VendorID);
        bookingMap.put("Userphone",UserPhonenumber);
        bookingMap.put("adults",AdultsQuantity);
        bookingMap.put("children",ChildrenQuantity);
        bookingMap.put("infants",InfantsQuantity);
        bookingMap.put("checkIn",CheckinDate);
        bookingMap.put("checkOut",CheckoutDate);



        Bookingref.child("User view").child(currentUser).child("Bookings").child(BookID).updateChildren(bookingMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task)
            {

                if (task.isSuccessful())
                {
                    Bookingref.child("Vendor view").child(VendorID).child("Bookings").child(BookID).updateChildren(bookingMap);
                    Toast.makeText(BookingCheckoutActivity.this, "You have successfully placed your booking, please pay to complete the order ", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(BookingCheckoutActivity.this,BookingPaymentActivity.class);

                    startActivity(intent);


                    progressDialog.dismiss();

                }
                else
                {
                    Toast.makeText(BookingCheckoutActivity.this, "Error Occurred Please Try Again", Toast.LENGTH_SHORT).show();
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
        String vendorName =  bundle.getString("VendorName");

        VendorName = vendorName;
        VendorID   = VendorId;







        DatabaseReference userRef  = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUser).child("Details");

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                if (dataSnapshot.exists())
                {
                    String sName = dataSnapshot.child("name").getValue().toString();
                    String sPhone = dataSnapshot.child("phone").getValue().toString();
                    String sEmail = dataSnapshot.child("email").getValue().toString();

                    UserPhonenumber = sPhone;
                    username = sName;


                    Username.setText(sName);
                    PhoneNumber.setText(sPhone);



                } else
                      {
                          Toast.makeText(BookingCheckoutActivity.this, "Please complete your profile before making an order", Toast.LENGTH_SHORT).show();
                          Intent intent = new Intent(BookingCheckoutActivity.this,ProfileSettingsActivity.class);
                          startActivity(intent);
                      }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }
}
