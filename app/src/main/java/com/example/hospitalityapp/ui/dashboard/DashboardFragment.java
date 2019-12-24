package com.example.hospitalityapp.ui.dashboard;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.hospitalityapp.AddProductActivity;
import com.example.hospitalityapp.AmountFinderActivity;
import com.example.hospitalityapp.BookingPaymentActivity;
import com.example.hospitalityapp.BusinessProfileSettingActivity;
import com.example.hospitalityapp.MessageChatlistActivity;
import com.example.hospitalityapp.R;
import com.example.hospitalityapp.VendorManageProductsActivity;
import com.example.hospitalityapp.ViewHolder.ProductViewHolder;
import com.example.hospitalityapp.model.Products;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class DashboardFragment extends Fragment {

    private DashboardViewModel dashboardViewModel;
    private String CurrentUserID;
    private String messageSenderID;
    private String VendorLocation,Category;
    private ProgressDialog progressDialog;



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {



        dashboardViewModel =
                ViewModelProviders.of(this).get(DashboardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        final TextView textView = root.findViewById(R.id.text_dashboard);
        final RelativeLayout relativeLayout = root.findViewById(R.id.settings_profile);
        final RelativeLayout AddproductLayout = root.findViewById(R.id.add_product_layout);
        final RelativeLayout ManagageProducts = root.findViewById(R.id.manageproducts);
        final RelativeLayout FavproductLayout = root.findViewById(R.id.relative_favorites);
        final RelativeLayout Messageslayout = root.findViewById(R.id.rellay_chat);
        final RelativeLayout BussinessLocationMap = root.findViewById(R.id.businesLocationsetup);
        final RelativeLayout Ordersmanagement = root.findViewById(R.id.manageorders);

        CurrentUserID = FirebaseAuth.getInstance().getUid();








        relativeLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view)
                    {
                        Intent intent = new Intent(getActivity(), BusinessProfileSettingActivity.class);
                        startActivity(intent);
                    }
                });

        AddproductLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(getActivity(), AddProductActivity.class);
                startActivity(intent);
            }
        });




        Ordersmanagement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(getActivity(), BookingPaymentActivity.class);
                startActivity(intent);
            }
        });


        ManagageProducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(getActivity(), VendorManageProductsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("Location",VendorLocation);
                bundle.putString("Category",Category);
                intent.putExtras(bundle);

                startActivity(intent);
            }
        });




        Messageslayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(getActivity(), MessageChatlistActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("receiverID",CurrentUserID);
                bundle.putString("senderID",messageSenderID);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });








        dashboardViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }


    @Override
    public void onStart() {
        super.onStart();





        //Get Vendors Location

      DatabaseReference  productsRef = FirebaseDatabase.getInstance().getReference().child("All Shops").child("ShopDetails").child(CurrentUserID).child("Details");

        productsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists())
                {
                    String VendorLocn = dataSnapshot.child("Location").getValue().toString();
                    String Vendorcate = dataSnapshot.child("Category").getValue().toString();


                    Category=Vendorcate;
                    VendorLocation= VendorLocn;


                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });















}


}