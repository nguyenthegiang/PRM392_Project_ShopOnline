package com.example.prm392_project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.prm392_project.Model.CartModel;
import com.example.prm392_project.Utils.CartRVAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ViewCartActivity extends AppCompatActivity implements CartRVAdapter.DeleteCartClickInterface {
    //Firebase
    FirebaseDatabase firebaseDatabase;
    DatabaseReference cartReference;
    FirebaseAuth mAuth;
    //Views
    TextView TVcartTotalAmount;
    RecyclerView RVCart;
    ProgressBar loadingPB;
    //for list Cart
    private ArrayList<CartModel> cartModelArrayList;
    private CartRVAdapter cartRVAdapter;

    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_cart);

        //get Views
        TVcartTotalAmount = findViewById(R.id.TVcartTotalAmount);
        RVCart = findViewById(R.id.RVCart);
        loadingPB = findViewById(R.id.progressBarViewCart);
        //firebase
        firebaseDatabase = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        cartReference = firebaseDatabase.getReference("Carts");
        //for recyclerView
        cartModelArrayList = new ArrayList<>();
        cartRVAdapter = new CartRVAdapter(cartModelArrayList, this, this::onDeleteCartClick);
        RVCart.setLayoutManager(new LinearLayoutManager(this));
        RVCart.setAdapter(cartRVAdapter);

        //fetch carts data from DB
        getCarts();
    }

    @Override
    protected void onStart() {
        //get ID of current user, for other operations
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            userId = user.getUid();
        }
        super.onStart();
    }

    private void getCarts() {
    }

    @Override
    public void onDeleteCartClick(int position) {

    }
}