package com.example.ecommerceapp.ViewHolder;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ecommerceapp.R;
import com.squareup.picasso.Picasso;

public class ProductFoodViewHolder extends RecyclerView.ViewHolder {
    private View mView;
    public ProductFoodViewHolder(@NonNull View itemView) {
        super(itemView);
        mView=itemView;

    }
    public void setFoodImage(String foodImage){
        ImageView imgFood=mView.findViewById(R.id.imgProductFood);
        Picasso.get().load(foodImage).into(imgFood);
    }
    public void setFoodName(String foodName){
        TextView txtFoodName=mView.findViewById(R.id.txtProductNameFood);
        txtFoodName.setShadowLayer(1.5f,-2,2, Color.GRAY);
        txtFoodName.setText(foodName);
    }
    public void setFoodPrice(String foodPrice){
        TextView txtFoodPrice=mView.findViewById(R.id.txtPriceFood);
        txtFoodPrice.setShadowLayer(1.5f,-2,2, Color.GRAY);
        txtFoodPrice.setText(foodPrice);
    }
}
