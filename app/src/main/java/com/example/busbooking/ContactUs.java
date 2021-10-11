package com.example.busbooking;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.busbooking.Prevalent.Prevalent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class ContactUs extends AppCompatActivity {

    EditText title, subject;
    RadioButton radioButton;
    RadioGroup radioGroup;
    Button btnupload;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contactus);

        getSupportActionBar().setTitle("Contact Us");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        title = findViewById(R.id.editTextTextPersonName);
        subject = findViewById(R.id.editTextTextPersonName2);
        radioGroup =(RadioGroup)findViewById(R.id.radioGroup);

        btnupload=findViewById(R.id.button5);
        btnupload.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NewApi")
            @Override
            public void onClick(View view) {

                CreateAccount();

            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void CreateAccount() {
        int selectedRadioButtonId = radioGroup.getCheckedRadioButtonId();
        radioButton = findViewById(selectedRadioButtonId);
        String contt1 = radioButton.getText().toString().trim();

        String phone = Prevalent.currentOnlineUser.getPhone();
        String title1 = title.getText().toString().trim();
        String subject1 = subject.getText().toString().trim();

        if (title1.equals("")) {
            Toast.makeText(ContactUs.this, "Please enter username", Toast.LENGTH_SHORT).show();
        } else if (subject1.equals("")) {
            Toast.makeText(ContactUs.this, "Please enter email", Toast.LENGTH_SHORT).show();
        }
//        else if (contt1.equals("")) {
//            Toast.makeText(ContactUs.this, "Please Select reason", Toast.LENGTH_SHORT).show();
//        }
        else {
            final DatabaseReference RootRef;
            RootRef = FirebaseDatabase.getInstance().getReference();

            RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    HashMap<String, Object> userdataMap = new HashMap<>();
                    userdataMap.put("title", title1);
                    userdataMap.put("subject", subject1);
                    userdataMap.put("cont", contt1);
                    RootRef.child("Contact").child(phone).child(title1).updateChildren(userdataMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(ContactUs.this, "your message has been sented.", Toast.LENGTH_SHORT).show();

                                        Intent intent = new Intent(ContactUs.this, ContactUs.class);
                                        startActivity(intent);
                                    } else {
                                        Toast.makeText(ContactUs.this, "Network Error: Please try again after some time...", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.homemenu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item2id:
                startActivity(new Intent(ContactUs.this, Rating.class));
                break;

            case R.id.item3id:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(ContactUs.this, LoginPage.class));
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}

