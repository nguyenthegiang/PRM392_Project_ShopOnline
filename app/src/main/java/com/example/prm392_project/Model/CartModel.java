package com.example.prm392_project.Model;

import android.os.Parcel;
import android.os.Parcelable;

//store a product in cart
//Implement Parcelable for performing CRUD actions with DB
public class CartModel implements Parcelable {
    //fields
    String userId;
    String productId;
    int amount;

    //getter, setter, constructor

    public CartModel() {
    }

    public CartModel(String userId, String productId, int amount) {
        this.userId = userId;
        this.productId = productId;
        this.amount = amount;
    }

    protected CartModel(Parcel in) {
        userId = in.readString();
        productId = in.readString();
        amount = in.readInt();
    }

    public static final Creator<CartModel> CREATOR = new Creator<CartModel>() {
        @Override
        public CartModel createFromParcel(Parcel in) {
            return new CartModel(in);
        }

        @Override
        public CartModel[] newArray(int size) {
            return new CartModel[size];
        }
    };

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userId);
        dest.writeString(productId);
        dest.writeInt(amount);
    }
}
