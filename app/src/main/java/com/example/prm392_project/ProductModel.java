package com.example.prm392_project;

import android.os.Parcel;
import android.os.Parcelable;

//Store a Product
public class ProductModel implements Parcelable {
    //fields
    private String name;
    private int price;
    private String description;
    private String imageURL;

    protected ProductModel(Parcel in) {
        name = in.readString();
        price = in.readInt();
        description = in.readString();
        imageURL = in.readString();
    }

    public static final Creator<ProductModel> CREATOR = new Creator<ProductModel>() {
        @Override
        public ProductModel createFromParcel(Parcel in) {
            return new ProductModel(in);
        }

        @Override
        public ProductModel[] newArray(int size) {
            return new ProductModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeInt(price);
        dest.writeString(description);
        dest.writeString(imageURL);
    }

    //getter, setter, constructor
    public ProductModel() {
    }

    public ProductModel(String name, int price, String description, String imageURL) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.imageURL = imageURL;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
}
