package com.example.ecommerceapp.ViewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ecommerceapp.R;
import com.squareup.picasso.Picasso;

public class FoodViewHolder extends RecyclerView.ViewHolder {
    private View mView;
    public FoodViewHolder(@NonNull View itemView) {
        super(itemView);
        mView=itemView;

    }
    public void setFoodImage(String foodImage){
        ImageView imgFood=mView.findViewById(R.id.imgProductSuggestion);
        Picasso.get().load(foodImage).into(imgFood);
    }
    public void setFoodName(String foodName){
        TextView txtFoodName=mView.findViewById(R.id.txtProductName);
        txtFoodName.setText(foodName);
    }
    public void setFoodPrice(String foodPrice){
        TextView txtFoodPrice=mView.findViewById(R.id.txtProductPrice);
        txtFoodPrice.setText(foodPrice);
    }
}
