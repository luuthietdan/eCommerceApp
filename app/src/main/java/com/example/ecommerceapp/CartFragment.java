package com.example.ecommerceapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ecommerceapp.Model.Cart;
import com.example.ecommerceapp.Model.Food;
import com.example.ecommerceapp.ViewHolder.CartViewHolder;
import com.example.ecommerceapp.ViewHolder.FoodViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class CartFragment extends Fragment {
    private TextView txtPriceCart, txtNameCart, txtDate;
    private ImageView imgProductCart;
    private RecyclerView rvListCart;
    private DatabaseReference mDBListCart;
    private FirebaseRecyclerAdapter adapterCart;
    private FirebaseAuth mAuth;
    private String CurrentUserId="";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cart, container, false);
        txtDate = view.findViewById(R.id.txtDate);
        txtNameCart = view.findViewById(R.id.txtProductNameCart);
        txtPriceCart = view.findViewById(R.id.txtPriceCart);
        rvListCart = view.findViewById(R.id.rvCart);
        imgProductCart = view.findViewById(R.id.imgProductCart);
        mAuth=FirebaseAuth.getInstance();
        CurrentUserId=mAuth.getCurrentUser().getUid();
        mDBListCart = FirebaseDatabase.getInstance().getReference().child("Cart").child(CurrentUserId);
        rvListCart.setHasFixedSize(true);
        rvListCart.setLayoutManager(new LinearLayoutManager(getContext()));
        DisplayListCart();
        return view;
    }

    private void DisplayListCart() {
        FirebaseRecyclerOptions<Cart> firebaseRecyclerOptions = new FirebaseRecyclerOptions.Builder<Cart>()
                .setQuery(mDBListCart, Cart.class)
                .build();
        adapterCart = new FirebaseRecyclerAdapter<Cart, CartViewHolder>(firebaseRecyclerOptions) {
            @NonNull
            @Override
            public CartViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.display_cart, viewGroup, false);

                return new CartViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull final CartViewHolder holder, int position, @NonNull Cart model) {
                final String cartId = getRef(position).getKey();
                mDBListCart.child(cartId).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            final String cartName = dataSnapshot.child("name").getValue().toString();
                            final String cartImage = dataSnapshot.child("image").getValue().toString();
                            final String cartPrice = dataSnapshot.child("price").getValue().toString();
                            final String cartTime = dataSnapshot.child("date").getValue().toString();
                            holder.setNameCart(cartName);
                            holder.setImageCart(cartImage);
                            holder.setPriceCart(cartPrice);
                            holder.setDate(cartTime);

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        };
        rvListCart.setAdapter(adapterCart);
    }
    @Override
    public void onStart() {
        super.onStart();
        adapterCart.startListening();



    }

    @Override
    public void onStop() {
        super.onStop();
        adapterCart.stopListening();

    }

}