package com.example.hospitalityapp.ui.home;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.hospitalityapp.AmountFinderActivity;
import com.example.hospitalityapp.R;
import com.example.hospitalityapp.ViewLocationBasedShopsActivity;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;

    public String HotelCategory="Hotel And Accommodation",Barandclubs="Night Club And Bars",Restaurants="Restaurants and Food Joints";




    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        final TextView textView = root.findViewById(R.id.text_home);

        final CardView AccomodationCardview = root.findViewById(R.id.accomodation);
        final CardView BarCardview = root.findViewById(R.id.barandclubs);
        final CardView RestaurantsCardview = root.findViewById(R.id.Restaurants);
        final CardView HelpCardview = root.findViewById(R.id.helpcardview);







//        homeViewModel.getText().observe(this, new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });



        AccomodationCardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                final CharSequence[] items = {"Hotels", "Apartments", "Hostels"};

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Preferred Accommodation");
                builder.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        Intent intent = new Intent(getActivity(), ViewLocationBasedShopsActivity.class);
                        intent.putExtra("Night Club And Bars",HotelCategory);
                        intent.putExtra("SubCategory",items[item]);

                        startActivity(intent);

                    }
                });

                builder.setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Toast.makeText(getActivity(), "Cancelled ", Toast.LENGTH_SHORT).show();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();

            }
        });




        BarCardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                final CharSequence[] items = {"Club/Disco", "Lounge", "Bar/Pub"};

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Preferred Drinking Joint");
                builder.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        Intent intent = new Intent(getActivity(), ViewLocationBasedShopsActivity.class);
                        intent.putExtra("Night Club And Bars",Barandclubs);
                        intent.putExtra("SubCategory",items[item]);

                        startActivity(intent);

                    }
                });

                builder.setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Toast.makeText(getActivity(), "Cancelled ", Toast.LENGTH_SHORT).show();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();

            }
        });




        RestaurantsCardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                final CharSequence[] items = {"Fine Dining", "Casual Dining", "Fast Food Joints","Cafeteria","Take-away Counters","Food Deliveries"};

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Preferred Restaurant");
                builder.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        Intent intent = new Intent(getActivity(), ViewLocationBasedShopsActivity.class);
                        intent.putExtra("Restaurants and Food Joints",Restaurants);
                        intent.putExtra("SubCategory",items[item]);

                        startActivity(intent);

                    }
                });

                builder.setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Toast.makeText(getActivity(), "Cancelled ", Toast.LENGTH_SHORT).show();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();

            }
        });




        HelpCardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(getContext(), AmountFinderActivity.class);
                startActivity(intent);

            }
        });






        return root;
    }




}