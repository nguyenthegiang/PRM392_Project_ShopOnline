package com.example.prm392_project;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.lang.reflect.Array;
import java.util.ArrayList;


public class ProductRVAdapter extends RecyclerView.Adapter<ProductRVAdapter.ViewHolder> {
    private ArrayList<ProductModel> productModelArrayList;    //list products
    private Context context;
    private ProductClickInterface productClickInterface;
    int lastPost = -1;  //position




    @NonNull
    @Override
    public ProductRVAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ProductRVAdapter.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        //data of a Product
        private ImageView IVproductImageURL;
        private TextView TVproductName, TVproductPrice;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            //init all Views
            IVproductImageURL = itemView.findViewById(R.id.productImageView);
            TVproductName = itemView.findViewById(R.id.productName);
            TVproductPrice = itemView.findViewById(R.id.productPrice);
        }
    }

    //creating an interface for on click
    public interface ProductClickInterface {
        void onProductClick(int position);
    }
}
