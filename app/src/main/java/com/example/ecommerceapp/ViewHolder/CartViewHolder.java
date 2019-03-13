package com.example.ecommerceapp.ViewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ecommerceapp.R;
import com.squareup.picasso.Picasso;

public class CartViewHolder extends RecyclerView.ViewHolder {
    private View mView;
    public CartViewHolder(@NonNull View itemView) {
        super(itemView);
        mView=itemView;
    }
    public void setImageCart(String imageCart){
        ImageView imgCart=mView.findViewById(R.id.imgProductCart);
        Picasso.get().load(imageCart).into(imgCart);
    }
    public void setNameCart(String nameCart){
        TextView txtNameCart=mView.findViewById(R.id.txtProductNameCart);
        txtNameCart.setText(nameCart);
    }
    public void setPriceCart(String priceCart){
        TextView txtPriceCart=mView.findViewById(R.id.txtPriceCart);
        txtPriceCart.setText(priceCart);
    }
    public void setDate(String date){
        TextView txtDate=mView.findViewById(R.id.txtDate);
        txtDate.setText(date);
    }
}
