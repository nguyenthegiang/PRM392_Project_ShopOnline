package com.example.prm392_project;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.prm392_project.Model.CartModel;
import com.example.prm392_project.Utils.CartRVAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
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
    private int totalPrice;

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

        //fetch carts data from DB, display total price
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

    //fetch carts data from DB, display total price
    private void getCarts() {
        //clear list
        cartModelArrayList.clear();
        //read data from DB
        cartReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                //hide progress bar
                loadingPB.setVisibility(View.GONE);

                //read data to list: only get Carts of this User
                CartModel cart = snapshot.getValue(CartModel.class);
                if (cart.getUserId().equals(userId)) {
                    cartModelArrayList.add(cart);
                }

                //Update Display Total Price
                totalPrice += cart.getUnitPrice() * cart.getAmount();
                TVcartTotalAmount.setText("$ " + totalPrice);

                //notify adapter
                cartRVAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                //notify adapter & hide progress bar
                loadingPB.setVisibility(View.GONE);
                cartRVAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                //notify adapter & hide progress bar
                loadingPB.setVisibility(View.GONE);
                cartRVAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                //notify adapter & hide progress bar
                loadingPB.setVisibility(View.GONE);
                cartRVAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    //method from Interface: Handle when Click [Delete] in a Cart
    @Override
    public void onDeleteCartClick(int position) {
        //get cart to delete
        CartModel cart = cartModelArrayList.get(position);
        //delete in DB
        DatabaseReference databaseReference = firebaseDatabase.getReference("Carts").child(cart.getCartId());
        databaseReference.removeValue();
        //notify
        Toast.makeText(this, "Cart deleted", Toast.LENGTH_SHORT).show();

        //reload Activity to reset values in List Cart & Total
        finish();
        startActivity(getIntent());
    }

    //Create the Appbar menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Create menu (appbar) from layout
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    //click listener for Appbar (Action Bar)
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            //ShowCart -> do nothing
            case R.id.menuShowCart: {
                return true;
            }

            //Shop -> do nothing
            case R.id.menuShop: {
                //move to Main Activity
                Intent i = new Intent(ViewCartActivity.this, MainActivity.class);
                startActivity(i);
                this.finish();
                return true;
            }

            //Manage Product -> go to ManageProduct Activity
            case R.id.menuManageProduct: {
                Intent i = new Intent(ViewCartActivity.this, ManageProductActivity.class);
                startActivity(i);
                this.finish();
                return true;
            }

            //Log out
            case R.id.menuLogOut: {
                //notify
                Toast.makeText(getApplicationContext(), "Logged out", Toast.LENGTH_SHORT).show();
                //log out in server
                mAuth.signOut();
                //move to Login Activity
                Intent i = new Intent(ViewCartActivity.this, LoginActivity.class);
                startActivity(i);
                this.finish();
                return true;
            }

            default: {
                return super.onOptionsItemSelected(item);
            }
        }
    }
}