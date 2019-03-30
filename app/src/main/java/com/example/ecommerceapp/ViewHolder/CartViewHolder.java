package com.example.ecommerceapp.ViewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.ecommerceapp.R;
import com.rey.material.widget.CheckBox;
import com.squareup.picasso.Picasso;

public class CartViewHolder extends RecyclerView.ViewHolder {
    private View mView;
    public CheckBox chkBuy;

    public interface OnItemClickListener {
        public void onItemClicked(int position);
    }

    public interface OnItemLongClickListener {
        public boolean onItemLongClicked(int position);
    }
    public CartViewHolder(@NonNull View itemView) {
        super(itemView);
        mView=itemView;
        chkBuy=mView.findViewById(R.id.chkBuy);

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
    public void setQuantity(String quantity){
        TextView txtDate=mView.findViewById(R.id.txtQuantity);
        txtDate.setText(quantity);
    }

}
