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
    int lastPos = -1;  //position

    //constructor
    public ProductRVAdapter(ArrayList<ProductModel> productModelArrayList, Context context, ProductClickInterface productClickInterface) {
        this.productModelArrayList = productModelArrayList;
        this.context = context;
        this.productClickInterface = productClickInterface;
    }

    //read layout file and bind each item in file to each item in ViewHolder
    @NonNull
    @Override
    public ProductRVAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //using inflater to read layout file
        View view = LayoutInflater.from(context).inflate(R.layout.product_item, parent, false);
        return new ViewHolder(view);
    }

    //Bind data from ArrayList to RecyclerView
    @Override
    public void onBindViewHolder(@NonNull ProductRVAdapter.ViewHolder holder, int position) {
        //set data to recycler view item
        ProductModel productModel = productModelArrayList.get(position);    //get data by position
        holder.TVproductName.setText(productModel.getName());
        holder.TVproductPrice.setText("$ " + productModel.getPrice());  //format price
        //use Picasso to display image
        Picasso.get().load(productModel.getImageURL()).into(holder.IVproductImage);
        //add animation to item
        setAnimation(holder.itemView, position);
        //when user click on Image -> call method & pass position
        holder.IVproductImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                productClickInterface.onProductClick(position);
            }
        });
    }

    //animation for item: slide from the left when load
    private void setAnimation(View itemView, int position) {
        if (position > lastPos) {
            Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
            itemView.setAnimation(animation);
            lastPos = position;
        }
    }

    @Override
    public int getItemCount() {
        return productModelArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        //data of a Product
        private ImageView IVproductImage;
        private TextView TVproductName, TVproductPrice;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            //init all Views
            IVproductImage = itemView.findViewById(R.id.productImageView);
            TVproductName = itemView.findViewById(R.id.productName);
            TVproductPrice = itemView.findViewById(R.id.productPrice);
        }
    }

    //creating an interface for on click (image)
    public interface ProductClickInterface {
        void onProductClick(int position);
    }
}
