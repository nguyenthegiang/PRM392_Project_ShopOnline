package com.example.prm392_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.prm392_project.Model.CartModel;
import com.example.prm392_project.Model.ProductModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class ProductDetailActivity extends AppCompatActivity {
    //Views
    private TextView textViewPriceDetail, textViewDescriptionDetail;
    private ImageView imageViewImageDetail;
    private EditText editTextQuantityCart;
    //Model
    ProductModel productModel;
    //Firebase
    FirebaseDatabase firebaseDatabase;
    DatabaseReference cartReference;
    private FirebaseAuth mAuth;

    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        //Get Views
        textViewPriceDetail = findViewById(R.id.textViewPriceDetail);
        textViewDescriptionDetail = findViewById(R.id.textViewDescriptionDetail);
        imageViewImageDetail = findViewById(R.id.imageViewImageDetail);
        editTextQuantityCart = findViewById(R.id.editTextQuantityCart);

        //get the model passed from MainActivity
        productModel = getIntent().getParcelableExtra("product");

        //set data of model to Views
        if (productModel != null) {
            //Product Name to Action Bar
            setTitle(productModel.getName()); //Change Text in ActionBar

            //Product Price
            textViewPriceDetail.setText("$ " + productModel.getPrice());

            //Product Description
            textViewDescriptionDetail.setText(productModel.getDescription());

            //Product Image: Display with Picasso
            Picasso.get().load(productModel.getImageURL()).into(imageViewImageDetail);
        }

        //firebase
        firebaseDatabase = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        //get reference to DB
        cartReference = firebaseDatabase.getReference("Carts");
    }

    @Override
    protected void onStart() {
        //get id of current user, for add to cart
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            userId = user.getUid();
        }
        super.onStart();
    }

    //Event click Add to Cart button: add to cart with an Amount
    public void onAddToCartClick(View view) {
        //Get amount from editText
        int amount = 0;
        try {
            amount = Integer.parseInt(editTextQuantityCart.getText().toString());
        } catch (Exception ex) {
            //default: 0
            amount = 0;
        }

        //if amount = 0 => not allow -> show alert & return
        if (amount == 0) {
            showAlertDialog();
            return;
        }

        //Create Cart model
        CartModel cart = new CartModel();
        cart.setUserId(userId);
        cart.setProductId(productModel.getId());
        cart.setAmount(amount); //amount = input amount
        //unit price is current price of product
        cart.setUnitPrice(productModel.getPrice());
        //generate cartId: = userId + productId
        cart.setCartId(userId + productModel.getId());

        //add to database
        cartReference.child(cart.getCartId()).setValue(cart).addOnSuccessListener(new OnSuccessListener<Void>() {
            //Success
            @Override
            public void onSuccess(Void unused) {
                //Success: notify
                Toast.makeText(ProductDetailActivity.this, "Add to Cart successfully", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //Fail: notify
                Toast.makeText(ProductDetailActivity.this, "Add to Cart failed" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    //Show Alert Dialog for to screen
    private void showAlertDialog() {
        //create dialog = builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Input not allowed"); //title
        builder.setMessage("Amount must be a number greater than zero!"); //message
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