package com.example.hospitalityapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    private Button Customerloginbtn;
    private ImageButton RegisterButton;
    private TextView CustomerRegisterlink;
    private TextView Customerstatus;
    private EditText Emailcustomer,Passwordcustomer;
    private ProgressDialog loadingbar;
    private FirebaseAuth mAuth;
    private DatabaseReference CustomerDatabaseRef;
    private String onlineCustomerID;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth=FirebaseAuth.getInstance();



        RegisterButton = findViewById(R.id.login_customer);
        Emailcustomer=findViewById(R.id.email_customer);
        Passwordcustomer=findViewById(R.id.password_customer);
        loadingbar=new ProgressDialog(this);











        RegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                String email=Emailcustomer.getText().toString();
                String password=Passwordcustomer.getText().toString();

                RegisterCustomer(email,password);


            }
        });



    }






    private void RegisterCustomer(String email, String password)
    { if (TextUtils.isEmpty(email))
    {
        Toast.makeText(RegisterActivity.this,"Please enter your email",Toast.LENGTH_SHORT).show();
    }

        if (TextUtils.isEmpty(password))
        {
            Toast.makeText(RegisterActivity.this,"Please enter your password",Toast.LENGTH_SHORT).show();
        }else
        {
            loadingbar.setTitle("REGISTERING NEW USER");
            loadingbar.setMessage("please wait..");
            loadingbar.show();


            mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task)
                {
                    if (task.isSuccessful())

                    {

                        Toast.makeText(RegisterActivity.this,"Registration Successful ",Toast.LENGTH_SHORT).show();
                        loadingbar.dismiss();
                        Intent intent = new Intent(RegisterActivity.this,ProfileSettingsActivity.class);
                        startActivity(intent);


                    }

                    else
                    {

                        Toast.makeText(RegisterActivity.this,"Registration Unsuccessful..please try again",Toast.LENGTH_SHORT).show();
                        loadingbar.dismiss();

                    }

                }
            });
        }



    }
}
