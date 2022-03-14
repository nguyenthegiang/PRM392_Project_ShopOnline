package com.example.prm392_project.Utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm392_project.Model.CartModel;
import com.example.prm392_project.R;

import java.util.ArrayList;

/*Adapter for Recycler View of Carts*/
public class CartRVAdapter extends RecyclerView.Adapter<CartRVAdapter.ViewHolder> {
    private ArrayList<CartModel> cartModelArrayList;    //list carts
    private Context context;
    private DeleteCartClickInterface deleteCartClickInterface;
    int lastPos = -1;  //position

    //constructor
    public CartRVAdapter(ArrayList<CartModel> cartModelArrayList, Context context, DeleteCartClickInterface deleteCartClickInterface) {
        this.cartModelArrayList = cartModelArrayList;
        this.context = context;
        this.deleteCartClickInterface = deleteCartClickInterface;
    }

    //read layout file and bind each item in file to each item in ViewHolder
    @NonNull
    @Override
    public CartRVAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //using inflater to read layout file
        View view = LayoutInflater.from(context).inflate(R.layout.cart_item, parent, false);
        return new ViewHolder(view);
    }

    //Bind data from ArrayList to RecyclerView
    //There's a red line here but it's NOT a fault, so don't change it
    @Override
    public void onBindViewHolder(@NonNull CartRVAdapter.ViewHolder holder, int position) {
        //set data to recycler view item
        CartModel cartModel = cartModelArrayList.get(position);    //get data by position
        holder.TVcartAmount.setText(cartModel.getAmount());
        holder.TVcartUnitPrice.setText("$ "+ cartModel.getUnitPrice());
        holder.TVcartProductName.setText(cartModel.getProductId()); //productId is also ProductName
        //set animation
        setAnimation(holder.itemView, position);
        //when user click Delete button -> call event
        holder.BtnCartDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteCartClickInterface.onDeleteCartClick(position);
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
        return cartModelArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        //data of a Cart
        private TextView TVcartAmount, TVcartUnitPrice, TVcartProductName;
        private Button BtnCartDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            //init all Views
            TVcartAmount = itemView.findViewById(R.id.cartAmount);
            TVcartUnitPrice = itemView.findViewById(R.id.cartUnitPrice);
            TVcartProductName = itemView.findViewById(R.id.cartProductName);
            BtnCartDelete = itemView.findViewById(R.id.cartBtnDelete);
        }
    }

    //creating an interface for on click
    public interface DeleteCartClickInterface {
        void onDeleteCartClick(int position);
    }
}
