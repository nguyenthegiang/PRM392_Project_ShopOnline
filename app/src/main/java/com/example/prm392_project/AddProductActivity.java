package com.example.prm392_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AddProductActivity extends AppCompatActivity {
    //Views
    EditText editTextName, editTextPrice, editTextDescription, editTextImageURL;
    ProgressBar progressBar;
    Button btnSave;

    //Firebase
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    String productId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        //Get Views
        editTextName = findViewById(R.id.editTextName);
        editTextPrice = findViewById(R.id.editTextPrice);
        editTextDescription = findViewById(R.id.editTextDescription);
        editTextImageURL = findViewById(R.id.editTextImageURL);
        progressBar = findViewById(R.id.progressBarAddProduct);
        btnSave = findViewById(R.id.btnSave);

        //Firebase
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Products");
    }

    public void onCreateClick(View view) {
        //show loading
        progressBar.setVisibility(View.VISIBLE);

        //get data from input
        String name = editTextName.getText().toString().trim();
        int price = Integer.parseInt(editTextPrice.getText().toString().trim());
        String description = editTextDescription.getText().toString().trim();
        String imageURL = editTextImageURL.getText().toString().trim();
        //id generated from name
        productId = name;
        //create a model
        ProductModel product = new ProductModel(productId, name, price, description, imageURL);
        //call add value event -> pass data to firebase database
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //set data to firebase
                databaseReference.child(productId).setValue(product);
                //notify
                Toast.makeText(AddProductActivity.this, "Product added successfully", Toast.LENGTH_SHORT).show();
                //back to main activity
                Intent intent = new Intent(AddProductActivity.this, MainActivity.class);
                startActivity(intent);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //fail: notify
                Toast.makeText(AddProductActivity.this, "Fail to add Product: " + error, Toast.LENGTH_SHORT).show();
            }
        });
        //hide loading
        progressBar.setVisibility(View.INVISIBLE);
    }
}