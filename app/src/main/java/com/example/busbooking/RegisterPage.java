package com.example.busbooking;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class RegisterPage extends AppCompatActivity {

    EditText edname, edemail,edno,edpass,edcpass;
    Button btnlogin, btnregister;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        getSupportActionBar().setTitle("Register Here");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        edname = findViewById(R.id.name);
        edemail = findViewById(R.id.email);
        edno = findViewById(R.id.phone);
        edpass = findViewById(R.id.pass);
        edcpass = findViewById(R.id.cpassword);

        btnlogin = findViewById(R.id.button2);
        btnregister = findViewById(R.id.button);
        mAuth=FirebaseAuth.getInstance();
//        mDbRef = mDatabase.getReference("booking-723b9-default-rtdb/User");
        //------------------------//
//        Navigate to Login Activity

        btnregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                CreateAccount();
            }
        });
        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(RegisterPage.this, LoginPage.class);
                startActivity(in);
            }
        });
    }

    private void CreateAccount() {

        String name = edname.getText().toString().trim();
        String email = edemail.getText().toString().trim();
        String phone = edno.getText().toString().trim();
        String pass = edpass.getText().toString().trim();
        String cpass = edcpass.getText().toString().trim();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        if (email.matches(emailPattern)) {
            if (name.equals("")) {
                Toast.makeText(RegisterPage.this, "Please enter username", Toast.LENGTH_SHORT).show();
            } else if (email.equals("")) {
                Toast.makeText(RegisterPage.this, "Please enter email", Toast.LENGTH_SHORT).show();
            } else if (phone.equals("")) {
                Toast.makeText(RegisterPage.this, "Please enter phone no", Toast.LENGTH_SHORT).show();
            }else if (phone.length()<10 || phone.substring(0,3)=="075" || phone.substring(0,3)=="077" || phone.substring(0,3)=="076" || phone.substring(0,3)=="072" || phone.substring(0,3)=="071") {
                Toast.makeText(RegisterPage.this, "Please enter valid mobile phone no", Toast.LENGTH_SHORT).show();
            } else if (pass.equals("")) {
                Toast.makeText(RegisterPage.this, "Please enter password", Toast.LENGTH_SHORT).show();
            } else if (cpass.equals("")) {
                Toast.makeText(RegisterPage.this, "Please enter confirm password", Toast.LENGTH_SHORT).show();
            }
            else if (cpass.length()<6) {
                Toast.makeText(RegisterPage.this, "Password length must be 6", Toast.LENGTH_SHORT).show();
            }else if (!cpass.equals(pass)) {
                Toast.makeText(RegisterPage.this, "Password not matched", Toast.LENGTH_SHORT).show();
            }else {
                final DatabaseReference RootRef;
                RootRef = FirebaseDatabase.getInstance().getReference();

                RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (!(dataSnapshot.child("Users").child(phone).exists())) {
                            HashMap<String, Object> userdataMap = new HashMap<>();
                            userdataMap.put("phone", phone);
                            userdataMap.put("password", pass);
                            userdataMap.put("Name", name);
                            userdataMap.put("Email", email);
                            RootRef.child("Users").child(phone).updateChildren(userdataMap)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(RegisterPage.this, "Congratulations, your account has been created.", Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(RegisterPage.this, Rating.class);
                                                startActivity(intent);
                                            } else {
                                                Toast.makeText(RegisterPage.this, "Network Error: Please try again after some time...", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                        } else {
                            Toast.makeText(RegisterPage.this, "This " + phone + " already exists.", Toast.LENGTH_SHORT).show();
                            Toast.makeText(RegisterPage.this, "Please try again using another phone number.", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(RegisterPage.this, RegisterPage.class);
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        } else {
            Toast.makeText(RegisterPage.this, "Please enter valid email", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(RegisterPage.this, RegisterPage.class);
        }
    }
}
