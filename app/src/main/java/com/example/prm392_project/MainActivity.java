package com.example.prm392_project;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/*Product List*/
public class MainActivity extends AppCompatActivity implements ProductRVAdapter.ProductClickInterface{
    //Firebase
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    private FirebaseAuth mAuth;
    //Views
    private RecyclerView RVProduct;
    private ProgressBar loadingPB;
    private RelativeLayout homeRL;
    //for list product
    private ArrayList<ProductModel> productModelArrayList;
    private ProductRVAdapter productRVAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //get Views
        RVProduct = findViewById(R.id.RVProductList);
        homeRL = findViewById(R.id.RLBottomSheet);
        loadingPB = findViewById(R.id.progressBarMain);
        //firebase
        firebaseDatabase = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        //get reference to DB
        databaseReference = firebaseDatabase.getReference("Products");
        //for product list
        productModelArrayList = new ArrayList<>();
        //for recycler view
        productRVAdapter = new ProductRVAdapter(productModelArrayList, this, this::onProductClick); //init adapter
        RVProduct.setLayoutManager(new LinearLayoutManager(this));  //set layout manager
        RVProduct.setAdapter(productRVAdapter);   //set adapter to recycler view

        //fetch products data from DB
        getProducts();
    }

    //fetch products data from DB
    private void getProducts() {
        //clear list
        productModelArrayList.clear();
        //read data from DB
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                //hide progress bar
                loadingPB.setVisibility(View.GONE);
                //read data to list
                productModelArrayList.add(snapshot.getValue(ProductModel.class));
                //notify adapter
                productRVAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                //this method is called when new child is added
                //notify adapter & hide progress bar
                loadingPB.setVisibility(View.GONE);
                productRVAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                //this method is called when child is removed
                productRVAdapter.notifyDataSetChanged();
                loadingPB.setVisibility(View.GONE);
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                //method called when  child is moved
                productRVAdapter.notifyDataSetChanged();
                loadingPB.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    //method from Interface
    @Override
    public void onProductClick(int position) {
        //Click on 1 Course -> Display a bottom Sheet for this course
        displayBottomSheet(productModelArrayList.get(position));
    }

    //click listener for Appbar (Action Bar)
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            //Log out
            case R.id.logOut: {
                //notify
                Toast.makeText(getApplicationContext(), "Logged out", Toast.LENGTH_SHORT).show();
                //log out in server
                mAuth.signOut();
                //move to Login Activity
                Intent i = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(i);
                this.finish();
                return true;
            }
            default: {
                return super.onOptionsItemSelected(item);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Create menu (appbar) from layout
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    //Display the Bottom Sheet that shows Product Detail when select 1 item in recycler view
    private void displayBottomSheet(ProductModel productModel) {
        //create bottom sheet dialog from themes.xml
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this, R.style.BottomSheetDialogTheme);
        //set layout for bottom sheet
        View layout = LayoutInflater.from(this).inflate(R.layout.bottom_sheet_layout, homeRL);
        bottomSheetDialog.setContentView(layout);
        //set cancelable: when touch outside the sheet
        bottomSheetDialog.setCancelable(false);
        bottomSheetDialog.setCanceledOnTouchOutside(true);
        //display sheet
        bottomSheetDialog.show();

        //set data to Views in sheet
        TextView productNameTV = layout.findViewById(R.id.TVProductName);
        TextView productDescriptionTV = layout.findViewById(R.id.TVProductDescription);
        TextView productPriceTV = layout.findViewById(R.id.TVProductPrice);
        ImageView productImageIV = layout.findViewById(R.id.IVProductImage);
        productNameTV.setText(productModel.getName());
        productDescriptionTV.setText(productModel.getDescription());
        productPriceTV.setText("$ " + productModel.getPrice());
        Picasso.get().load(productModel.getImageURL()).into(productImageIV);

        //set event for buttons
        Button viewDetailBtn = layout.findViewById(R.id.btnViewDetail);
        Button editBtn = layout.findViewById(R.id.btnEditProduct);
    }

    //--------------------------Delete this Later--------------------------
    public void moveToAddProduct(View view) {
        //Move to AddProductActivity
        Intent intent = new Intent(this, AddProductActivity.class);
        startActivity(intent);
    }
}