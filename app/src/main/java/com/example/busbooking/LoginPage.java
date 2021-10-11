package com.example.busbooking;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.busbooking.Model.UserInfo;
import com.example.busbooking.Prevalent.Prevalent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginPage extends AppCompatActivity {
    EditText edphone, edpwd;
    Button btnlogin, btnregister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportActionBar().setTitle("Login Here");

        edphone = findViewById(R.id.phone);
        edpwd = findViewById(R.id.pass1);

        btnlogin = findViewById(R.id.button);
        btnregister = findViewById(R.id.button2);

        btnregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(LoginPage.this, RegisterPage.class);
                startActivity(in);
            }
        });

        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginUser();
            }
        });
    }

    private void LoginUser()
    {
        String phone = edphone.getText().toString();
        String password = edpwd.getText().toString();
        if (phone.length()<10 || phone.substring(0,3)=="075" || phone.substring(0,3)=="077" || phone.substring(0,3)=="076" || phone.substring(0,3)=="072" || phone.substring(0,3)=="071") {
            Toast.makeText(LoginPage.this, "Please enter valid mobile phone no", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(phone))
        {
            Toast.makeText(this, "Please enter your phone number...", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(password))
        {
            Toast.makeText(this, "Please enter your password...", Toast.LENGTH_SHORT).show();
        }

        else
        {
            AllowAccessToAccount(phone, password);
        }
    }



    private void AllowAccessToAccount(final String phone, final String password)
    {
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if (dataSnapshot.child("Users").child(phone).exists())
                {
                    UserInfo usersData = dataSnapshot.child("Users").child(phone).getValue(UserInfo.class);

                    if (usersData.getPhone().equals(phone))
                    {
                        if (usersData.getPassword().equals(password))
                        {
                            Toast.makeText(LoginPage.this, "logged in Successfully...", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(LoginPage.this, Rating.class);
                            Prevalent.currentOnlineUser = usersData;
                            startActivity(intent);
                        }
                        else
                        {
                            Toast.makeText(LoginPage.this, "Password is incorrect.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                else
                {
                    Toast.makeText(LoginPage.this, "Account with this " + phone + " number do not exists.", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}