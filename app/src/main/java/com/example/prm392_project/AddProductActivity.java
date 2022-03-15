package com.example.prm392_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.prm392_project.Model.ProductModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddProductActivity extends AppCompatActivity {
    //Views
    EditText editTextName, editTextPrice, editTextDescription, editTextImageURL;
    ProgressBar progressBar;
    Button btnSave;

    //Firebase
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseAuth mAuth;

    String productId;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        setTitle("Add Product");

        //Get Views
        editTextName = findViewById(R.id.editTextName);
        editTextPrice = findViewById(R.id.editTextPrice);
        editTextDescription = findViewById(R.id.editTextDescription);
        editTextImageURL = findViewById(R.id.editTextImageURL);
        progressBar = findViewById(R.id.progressBarAddProduct);
        btnSave = findViewById(R.id.btnSave);

        //Firebase
        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Products");
    }

    public void onCreateClick(View view) {
        //show loading
        progressBar.setVisibility(View.VISIBLE);

        //get data from input
        String name = "";
        int price = 0;
        String description = "";
        String imageURL = "";

        //Validation: no value is empty or wrong format
        try {
            name = editTextName.getText().toString().trim();
            price = Integer.parseInt(editTextPrice.getText().toString().trim());
            description = editTextDescription.getText().toString().trim();
            imageURL = editTextImageURL.getText().toString().trim();

            if (name.isEmpty() || price <= 0 || description.isEmpty() || imageURL.isEmpty()) {
                throw new Exception();
            }
        } catch (Exception ex) {
            //notify
            showAlertDialog();
            progressBar.setVisibility(View.GONE);
            return;
        }

        //id generated from name
        productId = name;
        //SellerID is id of the current user
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            userId = user.getUid();
        }
        //create a model
        ProductModel product = new ProductModel(productId, name, price, description, imageURL, userId);

        //call add value event -> pass data to firebase database
//        databaseReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                //set data to firebase
//                databaseReference.child(productId).setValue(product);
//                //notify
//                Toast.makeText(AddProductActivity.this, "Product added successfully", Toast.LENGTH_SHORT).show();
//                //back to main activity
//                Intent intent = new Intent(AddProductActivity.this, MainActivity.class);
//                startActivity(intent);
//                finish();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                //fail: notify
//                Toast.makeText(AddProductActivity.this, "Fail to add Product: " + error, Toast.LENGTH_SHORT).show();
//            }
//        });

        //Add to Database (no Bug)
        databaseReference.child(productId).setValue(product).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                //notify
                Toast.makeText(AddProductActivity.this, "Product added successfully", Toast.LENGTH_SHORT).show();
                //back to manage product activity
                Intent intent = new Intent(AddProductActivity.this, ManageProductActivity.class);
                startActivity(intent);
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //fail: notify
                Toast.makeText(AddProductActivity.this, "Fail to add Product: " + e.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });

        //hide loading
        progressBar.setVisibility(View.INVISIBLE);
    }

    //Show Alert Dialog for to screen
    private void showAlertDialog() {
        //create dialog = builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Input not allowed"); //title
        builder.setMessage("Please fill in all fields with correct format!"); //message
        builder.setIcon(android.R.drawable.ic_dialog_alert);//icon

        //add button
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //do nothing
            }
        });

        //show dialog
        Dialog dialog = builder.create();

        dialog.show();
    }
}