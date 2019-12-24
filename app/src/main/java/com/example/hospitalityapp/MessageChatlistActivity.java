package com.example.hospitalityapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hospitalityapp.model.Messages;
import com.example.hospitalityapp.model.Users;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class MessageChatlistActivity extends AppCompatActivity {

    private  String  messageSenderID,messageReceiverID;
    private DatabaseReference MessagelistRef;

    private RecyclerView recyclerView;
    private String currentUser;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_chatlist);

        mAuth= FirebaseAuth.getInstance();
        currentUser=mAuth.getCurrentUser().getUid();


//        Bundle bundle = getIntent().getExtras();
//        messageSenderID = bundle.getString("senderID");
//        messageReceiverID =  bundle.getString("receiverID");


//        messageSenderID = getIntent().getExtras().get("senderID").toString();
//        messageReceiverID = getIntent().getExtras().get("receiverID").toString();




        MessagelistRef = FirebaseDatabase.getInstance().getReference().child("Messages").child("Senders").child(currentUser);


        recyclerView=  findViewById(R.id.recycler3_menu);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));






    }


    @Override
    protected void onStart() {
        super.onStart();



        FirebaseRecyclerOptions<Messages> options =
                new FirebaseRecyclerOptions.Builder<Messages>()
                        .setQuery(MessagelistRef,Messages.class)
                        .build();
        FirebaseRecyclerAdapter<Messages,MessageListViewHolder> adapter =
                new FirebaseRecyclerAdapter<Messages, MessageListViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull MessageListViewHolder holder, int position, @NonNull final Messages model)
                    {
                        holder.userName.setText(model.getSendername());
                        holder.date.setText("sent On: "+model.getDate());
                        holder.Time.setText("  at:"+model.getTime());
//                        Picasso.get().load(model.getImage()).into(holder.profileimage);


                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v)
                            {
                                Toast.makeText(MessageChatlistActivity.this, "Messages...", Toast.LENGTH_SHORT).show();
                                Intent messageintent=new Intent(MessageChatlistActivity.this,MessageUserviewActivity.class);
                                messageintent.putExtra("receiverID",currentUser);
                                messageintent.putExtra("senderID", model.from);


                                startActivity(messageintent);
                            }
                        });






                    }

                    @NonNull
                    @Override
                    public MessageListViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
                    {
                        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.message_list_layout,viewGroup, false);
                        MessageListViewHolder holder = new MessageListViewHolder(view);
                        return holder;
                    }
                };

        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }









    public  static  class  MessageListViewHolder extends  RecyclerView.ViewHolder
    {
        public TextView userName,date,Time;
        public CircleImageView profileimage;


        public MessageListViewHolder(@NonNull View itemView)
        {
            super(itemView);

            date = itemView.findViewById(R.id.Senderuser_date);
            Time = itemView.findViewById(R.id.Senderuser_TIME);

            userName = itemView.findViewById(R.id.Senderuser_name);
            profileimage = itemView.findViewById(R.id.senderdp);


        }
    }





}
