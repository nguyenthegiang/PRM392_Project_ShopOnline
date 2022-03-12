package com.example.prm392_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

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

        //get the model passed from MainActivity
        productModel = getIntent().getParcelableExtra("product");

        //set data of chosen product to editText
        if (productModel != null) {
            editTextName.setText(productModel.getName());
            editTextPrice.setText(productModel.getPriceString());
            editTextDescription.setText(productModel.getDescription());
            editTextImageURL.setText(productModel.getImageURL());
            productID = productModel.getId();
        }

        //get database reference to the chosen product
        databaseReference = firebaseDatabase.getReference("Products").child(productID);
    }

    //Event click [Update] button
    public void onUpdateClick(View view) {
        //show loading
        loadingPB.setVisibility(View.VISIBLE);

        //get input data
        String productName = editTextName.getText().toString();
        String productPrice = editTextPrice.getText().toString();
        String productDescription = editTextDescription.getText().toString();
        String productImageURL = editTextImageURL.getText().toString();
        //create a map for passing data to DB using key & value pair
        Map<String, Object> map = new HashMap<>();
        map.put("description", productDescription);
        map.put("imageURL", productImageURL);
        map.put("name", productName);
        map.put("price", productPrice);

        //update value to Database
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //hide loading
                loadingPB.setVisibility(View.INVISIBLE);
                //update
                databaseReference.updateChildren(map);
                //notify
                Toast.makeText(EditProductActivity.this, "Product updated", Toast.LENGTH_SHORT).show();
                //move to mainActivity
                Intent intent = new Intent(EditProductActivity.this, MainActivity.class);
                startActivity(intent);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //notify: fail
                Toast.makeText(EditProductActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                //hide loading
                loadingPB.setVisibility(View.INVISIBLE);
            }
        });

    }

    //Event click [Delete] button
    public void onDeleteClick(View view) {
        //delete in DB
        databaseReference.removeValue();
        //notify
        Toast.makeText(this, "Product deleted", Toast.LENGTH_SHORT).show();
        //move to mainActivity
        Intent intent = new Intent(EditProductActivity.this, MainActivity.class);
        startActivity(intent);
    }
}