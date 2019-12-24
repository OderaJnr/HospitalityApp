package com.example.hospitalityapp.ViewHolder;

import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.hospitalityapp.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ShopViewHolder extends  RecyclerView.ViewHolder implements View.OnClickListener {


    public TextView Shopname,Shopstatus,location,ShopPhone;
    public TextView ShopOverview,ShopAmenities,ShopPriceList,ViewDetails,RatesHotel;
    public ImageView profileimage;
    public Button ViewshopButton,BookButton;
    public LinearLayout sendmesaagelayout;


    public AdapterView.OnItemClickListener listener;


    public ShopViewHolder(@NonNull View itemView) {
        super(itemView);

        Shopname = (TextView) itemView.findViewById(R.id.shopname);
        Shopstatus = (TextView) itemView.findViewById(R.id.shop_status);
        ShopPhone = (TextView) itemView.findViewById(R.id.shop_phone);
        location = (TextView) itemView.findViewById(R.id.shop_location);
        profileimage = (ImageView) itemView.findViewById(R.id.product_image);
        ViewshopButton = (Button)itemView.findViewById(R.id.view_button);
        BookButton   = (Button)itemView.findViewById(R.id.bookRoom);
        sendmesaagelayout = (LinearLayout)itemView.findViewById(R.id.sendmessagela);


        ShopOverview = (TextView) itemView.findViewById(R.id.textoverview);
        ShopAmenities = (TextView) itemView.findViewById(R.id.textamenities);
        ShopPriceList = (TextView) itemView.findViewById(R.id.textpricelist);
        ViewDetails = (TextView) itemView.findViewById(R.id.DetailstextView);

        RatesHotel = (TextView) itemView.findViewById(R.id.hotel_rates);








    }



    public void  setItemClickListener(AdapterView.OnItemClickListener listener)
    {
        this.listener = listener;
    }


    @Override
    public void onClick(View view) {


//        listener.onItemClick(view,getAdapterPosition(),false);

    }


}

