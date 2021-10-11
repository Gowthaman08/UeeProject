package com.example.busbooking;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
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

public class Rating extends AppCompatActivity{
    Button btnupload1;
    EditText subject;
    RatingBar cont;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);

        getSupportActionBar().setTitle("Trip");
        subject = findViewById(R.id.editTextTextPersonName2);
        cont = findViewById(R.id.ratingBar);
        int[] i = new int[]{ R.id.ratingBar};
        int j=i[0];
        btnupload1=findViewById(R.id.button4);
        btnupload1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                CreateAccount(j);
            }
        });

    }

    private void CreateAccount(int i) {
        String subject1 = subject.getText().toString().trim();
        float contt1 = cont.getRating();
        String phone = Prevalent.currentOnlineUser.getPhone();

        if (contt1==0) {
            Toast.makeText(Rating.this, "Please give rating", Toast.LENGTH_SHORT).show();
        }
        else {
            final DatabaseReference RootRef;
            RootRef = FirebaseDatabase.getInstance().getReference();

            RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    HashMap<String, Object> userdataMap = new HashMap<>();
                    userdataMap.put("subject", subject1);
                    userdataMap.put("stars", contt1);
                    RootRef.child("Rating").child(phone).child("123").updateChildren(userdataMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(Rating.this, "Thank you for your feedback.", Toast.LENGTH_SHORT).show();

                                        Intent intent = new Intent(Rating.this, Rating.class);
                                        startActivity(intent);
                                    } else {
                                        Toast.makeText(Rating.this, "Network Error: Please try again after some time...", Toast.LENGTH_SHORT).show();
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
                startActivity(new Intent(Rating.this, ContactUs.class));
                break;

            case R.id.item3id:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(Rating.this, LoginPage.class));
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}

