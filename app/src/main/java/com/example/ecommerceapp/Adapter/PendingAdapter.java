package com.example.ecommerceapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ecommerceapp.Model.Cart;
import com.example.ecommerceapp.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PendingAdapter extends ArrayAdapter<Cart> {
    private Context mContext;
    private int mLayoutID;
    private List<Cart> mList;

    public static class ViewHolder{
        TextView txtName;
        TextView txtPrice;
        TextView txtDate;
        TextView txtTime;
        ImageView imgView;
        TextView txtQuantity;
    }

    public PendingAdapter(Context context, int resource, List<Cart> objects) {
        super(context, resource, objects);
        mContext = context;
        mLayoutID = resource;
        mList = objects;
    }


    @Override
    public View getView(int position, View convertView,  ViewGroup parent) {
        Cart cart = getItem(position);
        //final View result;
        ViewHolder viewHolder;
        if(convertView == null){
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.display_cart,parent,false);
            viewHolder.txtName = convertView.findViewById(R.id.txtProductNameCart);
            viewHolder.txtPrice = convertView.findViewById(R.id.txtPriceCart);
            viewHolder.imgView = convertView.findViewById(R.id.imgProductCart);
            viewHolder.txtQuantity = convertView.findViewById(R.id.txtQuantity);
         //   viewHolder.txtDate = convertView.findViewById(R.id.txtMark);
            viewHolder.txtTime = convertView.findViewById(R.id.txtDate);
            //result = convertView;
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
       //     result = convertView;
        }
        viewHolder.txtName.setText(cart.getName());
        viewHolder.txtPrice.setText(cart.getPrice());
        viewHolder.txtTime.setText(cart.getTime());
        viewHolder.txtQuantity.setText(cart.getQuantity());
        Picasso.get().load(cart.getImage()).into(viewHolder.imgView);

        return convertView;
    }
}
