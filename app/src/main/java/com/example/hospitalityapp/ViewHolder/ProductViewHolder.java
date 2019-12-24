package com.example.hospitalityapp.ViewHolder;


import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.hospitalityapp.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ProductViewHolder extends  RecyclerView.ViewHolder implements View.OnClickListener{


    public TextView Productname,Productprice,ProductDescription,Sellername;
    public EditText productname,productprice,productDescription;

    public ImageView ProductImage,AddToCart,EditproductImage;
    public ElegantNumberButton elegantNumberButton;
    public Button SaveChangesbtn;
    public AdapterView.OnItemClickListener listener;



    public ProductViewHolder(@NonNull View itemView) {
        super(itemView);

        //For viewing Menu by Users

        ProductImage = (ImageView)itemView.findViewById(R.id.Sproduct_image);
        Productprice = (TextView) itemView.findViewById(R.id.Sproduct_price);
        Productname = (TextView) itemView.findViewById(R.id.Sproduct_name);
        ProductDescription = (TextView) itemView.findViewById(R.id.Sproduct_description);
        elegantNumberButton = (ElegantNumberButton)itemView.findViewById(R.id.elegantnumberbutton);
        AddToCart = (ImageView) itemView.findViewById(R.id.add_to_cart);
        Sellername = (TextView)itemView.findViewById(R.id.SHopnameselling);


        //For Editing product details by Vendor

        productname = (EditText)itemView.findViewById(R.id.product_name);
        productprice = (EditText)itemView.findViewById(R.id.product_price);
        productDescription = (EditText)itemView.findViewById(R.id.product_description);
        SaveChangesbtn = (Button) itemView.findViewById(R.id.saveChanges);


        EditproductImage= (ImageView)itemView.findViewById(R.id.product_image);






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


