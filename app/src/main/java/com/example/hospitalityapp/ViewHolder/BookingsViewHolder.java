package com.example.hospitalityapp.ViewHolder;

import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.example.hospitalityapp.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class BookingsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


    public TextView Username,Totalamount,phonenumber,Date,OrderNumber,PaymentStatus,CheckIn,CheckOut;
    public AdapterView.OnItemClickListener listener;

    public BookingsViewHolder(@NonNull View itemView) {
        super(itemView);

        Username = (TextView) itemView.findViewById(R.id.order_user_name);
        Totalamount = (TextView) itemView.findViewById(R.id.order_total_price);
        Date = (TextView) itemView.findViewById(R.id.order_datetime);
        OrderNumber = (TextView) itemView.findViewById(R.id.order_number);
        PaymentStatus = (TextView) itemView.findViewById(R.id.payment_status);
        CheckIn = (TextView) itemView.findViewById(R.id.check_in);
        CheckOut = (TextView) itemView.findViewById(R.id.check_out);




    }

    @Override
    public void onClick(View view) {

    }
}