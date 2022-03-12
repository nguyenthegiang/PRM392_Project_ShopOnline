package com.example.prm392_project;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class ProductDetailActivity extends AppCompatActivity {
    //Views
    private TextView textViewPriceDetail, textViewDescriptionDetail;
    private ImageView imageViewImageDetail;
    //Model
    ProductModel productModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        //Get Views
        textViewPriceDetail = findViewById(R.id.textViewPriceDetail);
        textViewDescriptionDetail = findViewById(R.id.textViewDescriptionDetail);
        imageViewImageDetail = findViewById(R.id.imageViewImageDetail);

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
    }
}