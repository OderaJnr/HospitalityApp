package com.example.hospitalityapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hospitalityapp.ViewHolder.MessagesAdapter;
import com.example.hospitalityapp.model.Messages;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MessageUserviewActivity extends AppCompatActivity {

    private ImageButton SendmessageButton, SendImagefileButton;
    private EditText userMessageInput;
    private RecyclerView userMessageList;
    private  final List<Messages> messagesList =new ArrayList<>();
    private LinearLayoutManager linearLayoutManager;

    private String messaReceiverID, messageReceiverName, messageSenderID,messageSenderName,saveCurrentDate,saveCurrentTime;

    private TextView userLastSeen,receiverName;
    private MessagesAdapter messagesAdapter;


    private DatabaseReference RootRef,UserRef;


    private String currentUser;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mesage_admin);



        mAuth= FirebaseAuth.getInstance();
        currentUser=mAuth.getCurrentUser().getUid();



        RootRef = FirebaseDatabase.getInstance().getReference();


        messageSenderID = getIntent().getExtras().get("senderID").toString();
        messaReceiverID = getIntent().getExtras().get("receiverID").toString();





        SendmessageButton =(ImageButton)findViewById(R.id.sendMessage);
        userMessageInput =(EditText)findViewById(R.id.message);



        messagesAdapter = new MessagesAdapter(messagesList);
        userMessageList =  (RecyclerView)findViewById(R.id.messages_view);

        linearLayoutManager = new LinearLayoutManager(this);
        userMessageList.setHasFixedSize(true);
        userMessageList.setLayoutManager(linearLayoutManager);
        userMessageList.setAdapter(messagesAdapter);







        SendmessageButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
//                SendMessage();

                getUserName();


            }
        });









    }

    private void SendMessage()
    {

        String  messageText = userMessageInput.getText().toString();

        if (TextUtils.isEmpty(messageText))

        {
            Toast.makeText(this, "Type message first...", Toast.LENGTH_SHORT).show();

        } else


        {

            String  message_sender_ref = "Messages/" + messageSenderID + "/" + messaReceiverID;
            String  message_receiver_ref = "Messages/" + messaReceiverID + "/" + messageSenderID;

            DatabaseReference user_nessage_key = RootRef.child("messages").child(messageSenderID).child(messaReceiverID).push();

            final String  message_push_id =  user_nessage_key.getKey();


            Calendar callForDate = Calendar.getInstance();
            SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMMM-yyyy");
            saveCurrentDate = currentDate.format(callForDate.getTime());

            Calendar callforTime = Calendar.getInstance();
            SimpleDateFormat CurrentTime = new SimpleDateFormat("HH:mm");
            saveCurrentTime = CurrentTime.format(callforTime.getTime());

            Map messageTextBody = new HashMap();
            messageTextBody.put("message", messageText);
            messageTextBody.put("time", saveCurrentTime);
            messageTextBody.put("date", saveCurrentDate);
            messageTextBody.put("type", "text");
            messageTextBody.put("from", messageSenderID);
            messageTextBody.put("sendername", messageSenderName);


            Map messageBodyDetails = new HashMap();
            messageBodyDetails.put(message_sender_ref + "/"  + message_push_id , messageTextBody);
            messageBodyDetails.put(message_receiver_ref + "/"  + message_push_id , messageTextBody);

            RootRef.updateChildren(messageBodyDetails).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task)
                {

                    if (task.isSuccessful())
                    {
                        Toast.makeText(MessageUserviewActivity.this, "Message Sent Successfully ", Toast.LENGTH_SHORT).show();
                        userMessageInput.setText("");



                        if (task.isSuccessful())
                        {

                            DatabaseReference datetimeandusername = FirebaseDatabase.getInstance().getReference().child("Messages").child("Senders").child(messaReceiverID).child(currentUser);

                            Map messageList = new HashMap();
                            messageList.put("date", saveCurrentDate);
                            messageList.put("time", saveCurrentTime);
                            messageList.put("sendername", messageSenderName);
                            messageList.put("from", messageSenderID);




                            datetimeandusername.updateChildren(messageList);



                        }



                    }
                    else
                    {
                        String messaege = task.getException().getMessage();
                        Toast.makeText(MessageUserviewActivity.this, "Error Occurred:" + messaege, Toast.LENGTH_SHORT).show();
                        userMessageInput.setText("");

                    }






                }
            });




        }

    }


    @Override
    protected void onStart() {
        super.onStart();

        FetchMessages();




    }




    private void FetchMessages()
    {

        RootRef.child("Messages").child(messaReceiverID).child(messageSenderID)

                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s)
                    {
                        Messages messages = dataSnapshot.getValue(Messages.class);
                        messagesList.add(messages);
                        messagesAdapter.notifyDataSetChanged();


                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

    }






    private void getUserName()
    {
        UserRef = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUser).child("Details");

        UserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists())
                {
                    String sendername = dataSnapshot.child("name").getValue().toString();
                    messageSenderName = sendername;

                    SendMessage();



                } else
                {
                    Toast.makeText(MessageUserviewActivity.this, "Please fill your Profile data before sending a message", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }















}
