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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class EditProductActivity extends AppCompatActivity {
    //Views
    private EditText editTextName, editTextPrice, editTextDescription, editTextImageURL;
    private ProgressBar loadingPB;
    //Firebase
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    //Model
    ProductModel productModel;
    private String productID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_product);
        //get Views
        editTextName = findViewById(R.id.editTextNameEdit);
        editTextPrice = findViewById(R.id.editTextPriceEdit);
        editTextDescription = findViewById(R.id.editTextDescriptionEdit);
        editTextImageURL = findViewById(R.id.editTextImageURLEdit);
        loadingPB = findViewById(R.id.progressBarEditProduct);
        Button btnUpdate = findViewById(R.id.btnUpdateProduct);
        Button btnDelete = findViewById(R.id.btnDeleteProduct);
        //firebase
        firebaseDatabase = FirebaseDatabase.getInstance();

        //get the model passed from MainActivity
        productModel = getIntent().getParcelableExtra("product");

        //set data of chosen product to editText
        if (productModel != null) {
            editTextName.setText(productModel.getName());
            editTextPrice.setText(String.valueOf(productModel.getPrice()));
            editTextDescription.setText(productModel.getDescription());
            editTextImageURL.setText(productModel.getImageURL());
            productID = productModel.getId();
        }

        //get database reference to the chosen product
        databaseReference = firebaseDatabase.getReference("Products").child(productID);

        //Event click [Update] button
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //show loading
                loadingPB.setVisibility(View.VISIBLE);

                //get input data
                String productName = editTextName.getText().toString();
                int productPrice = Integer.parseInt(editTextPrice.getText().toString());
                String productDescription = editTextDescription.getText().toString();
                String productImageURL = editTextImageURL.getText().toString();

                //create a map for passing data to DB using key & value pair
                Map<String, Object> map = new HashMap<>();
                map.put("id", productID);
                map.put("name", productName);
                map.put("price", productPrice);
                map.put("description", productDescription);
                map.put("imageURL", productImageURL);

                //update value to Database
//                databaseReference.addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        //hide loading
//                        loadingPB.setVisibility(View.INVISIBLE);
//                        //update
//                        databaseReference.updateChildren(map);
//                        //notify
//                        Toast.makeText(EditProductActivity.this, "Product updated", Toast.LENGTH_SHORT).show();
//                        //move to mainActivity
//                        startActivity(new Intent(EditProductActivity.this, MainActivity.class));
//                        finish();
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//                        //notify: fail
//                        Toast.makeText(EditProductActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
//                        //hide loading
//                        loadingPB.setVisibility(View.INVISIBLE);
//                    }
//                });

                //Update Value to Database (no Bug)
                //hide loading
                loadingPB.setVisibility(View.INVISIBLE);
                databaseReference.updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                    //Success
                    @Override
                    public void onSuccess(Void unused) {
                        //notify
                        Toast.makeText(EditProductActivity.this, "Product updated", Toast.LENGTH_SHORT).show();
                        //move to mainActivity
                        startActivity(new Intent(EditProductActivity.this, MainActivity.class));
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    //Fail
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //notify: fail
                        Toast.makeText(EditProductActivity.this, "Fail to update product: " + e.getMessage(),
                                Toast.LENGTH_SHORT).show();
                        //hide loading
                        loadingPB.setVisibility(View.INVISIBLE);
                    }
                });

            }
        });

        //Event click [Delete] button
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteProduct();
            }
        });
    }

    private void deleteProduct() {
        //delete in DB
        databaseReference.removeValue();
        //notify
        Toast.makeText(this, "Product deleted", Toast.LENGTH_SHORT).show();
        //move to mainActivity
        Intent intent = new Intent(EditProductActivity.this, MainActivity.class);
        startActivity(intent);
    }
}