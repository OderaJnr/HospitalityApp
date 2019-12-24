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

public class LoginActivity extends AppCompatActivity {

    private ImageButton Customerloginbtn;
    private Button CustomerRegisterbtn;
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
        setContentView(R.layout.activity_login);




        CustomerRegisterbtn=findViewById(R.id.customer_register_link);
        Emailcustomer=findViewById(R.id.email_customer);
        Passwordcustomer=findViewById(R.id.password_customer);
        Customerloginbtn = findViewById(R.id.login_customer);
        loadingbar=new ProgressDialog(this);

        mAuth=FirebaseAuth.getInstance();


        CustomerRegisterbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);

            }
        });




        Customerloginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                String email=Emailcustomer.getText().toString();
                String password=Passwordcustomer.getText().toString();

                SignInCustomer(email,password);

            }
        });



    }

    private void SignInCustomer(String email, String password)
    { if (TextUtils.isEmpty(email))
    {
        Toast.makeText(LoginActivity.this,"Please enter your email",Toast.LENGTH_SHORT).show();
    }

        if (TextUtils.isEmpty(password))
        {
            Toast.makeText(LoginActivity.this,"Please enter your password",Toast.LENGTH_SHORT).show();
        }else
        {
            loadingbar.setTitle("SIGNING IN USER");
            loadingbar.setMessage("please wait..");
            loadingbar.show();


            mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task)
                {
                    if (task.isSuccessful())
                    {

                        Intent customerIntent=new Intent(LoginActivity.this,MainActivity.class);
                        startActivity(customerIntent);

                        Toast.makeText(LoginActivity.this,"signed in successfully ",Toast.LENGTH_SHORT).show();
                        loadingbar.dismiss();



                    }

                    else
                    {
                        Toast.makeText(LoginActivity.this,"Sign in Unsuccessful..please try again",Toast.LENGTH_SHORT).show();
                        loadingbar.dismiss();

                    }

                }
            });
        }

    }
}

