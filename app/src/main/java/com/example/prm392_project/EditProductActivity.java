package com.example.prm392_project;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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
            editTextPrice.setText(productModel.getPrice());
            editTextDescription.setText(productModel.getDescription());
            editTextImageURL.setText(productModel.getImageURL());
            productID = productModel.getId();
        }

        //get database reference to the chosen product
        databaseReference = firebaseDatabase.getReference("Products").child(productID);
    }

    //Event click [Update] button
}