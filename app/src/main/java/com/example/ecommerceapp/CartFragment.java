package com.example.ecommerceapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ecommerceapp.Model.Cart;
import com.example.ecommerceapp.Model.Food;
import com.example.ecommerceapp.ViewHolder.CartViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rey.material.widget.CheckBox;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class CartFragment extends Fragment {
    private static final String TAG = CartFragment.class.getSimpleName();
    private TextView txtPriceCart, txtNameCart, txtDate, txtQuantity, txtTotalMoney;

    private ImageView imgProductCart;
    private RecyclerView rvListCart;
    private DatabaseReference mDBListCart;
    private FirebaseRecyclerAdapter adapterCart;
    private List<Cart> cartList;
    private FirebaseAuth mAuth;
    private String CurrentUserId = "";

    @Override
    public boolean getAllowReturnTransitionOverlap() {
        return super.getAllowReturnTransitionOverlap();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cart, container, false);

        rvListCart = view.findViewById(R.id.rvCart);
        txtTotalMoney = view.findViewById(R.id.txtTotalMoney);

        mAuth = FirebaseAuth.getInstance();
        CurrentUserId = mAuth.getCurrentUser().getUid();
        mDBListCart = FirebaseDatabase.getInstance().getReference().child("Cart").child(CurrentUserId);
        rvListCart.setHasFixedSize(true);
        rvListCart.setLayoutManager(new LinearLayoutManager(getContext()));
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        rvListCart.setLayoutManager(mLayoutManager);

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
            protected void onBindViewHolder(@NonNull final CartViewHolder holder, final int position, @NonNull Cart model) {
                final String cartId = getRef(position).getKey();
                mDBListCart.child(cartId).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
//                            final String cartName = dataSnapshot.child("name").getValue().toString();
//                            //TODO lấy ảnh, Tính tổng giá tiền, xóa, ghi chú, sửa
//                            final String cartImage = dataSnapshot.child("image").getValue().toString();
//                            final String cartPrice = dataSnapshot.child("price").getValue().toString();
//                            final String cartTime = dataSnapshot.child("date").getValue().toString();
//
//                            final String cartQuantity = dataSnapshot.child("quantity").getValue().toString();
                            cartList = new ArrayList<>();
                            final Cart cart = dataSnapshot.getValue(Cart.class);
                            final int price = Integer.parseInt(cart.getQuantity()) * Integer.parseInt(cart.getPrice());
                            holder.setNameCart(cart.getName());
                            holder.setImageCart(cart.getImage());
                            holder.setPriceCart(price + "$");
                            holder.setDate(cart.getDate());
                            holder.setQuantity(cart.getQuantity());
                            holder.chkBuy.setTag(position);
                            final int x=0;
                            holder.chkBuy.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (holder.chkBuy.isChecked()) {

                                        txtTotalMoney.setText(price + "$");
                                    } else {
                                        txtTotalMoney.setText("0$");
                                    }
                                }
                            });


                            holder.itemView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                    builder.setTitle("Update Product");


                                    final EditText input = new EditText(getContext());

                                    input.setInputType(InputType.TYPE_CLASS_TEXT);
                                    builder.setView(input);

                                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            final HashMap<String, Object> cartMap = new HashMap<>();
                                            cartMap.put("note", input.getText().toString());
                                            mDBListCart.child(cartId).updateChildren(cartMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {

                                                        Toast.makeText(getActivity(), "Note " + cart.getName() + " Successfully", Toast.LENGTH_SHORT).show();


                                                    }
                                                }
                                            });
                                        }
                                    });
                                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.cancel();
                                        }
                                    });

                                    builder.show();
                                }
                            });
                            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                                @Override
                                public boolean onLongClick(View v) {
                                    final int deletePosition = position;
                                    AlertDialog.Builder alert = new AlertDialog.Builder(
                                            getContext());
                                    alert.setTitle("Delete");
                                    alert.setMessage("Do you want to delete : " + cart.getName());
                                    alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            adapterCart.getRef(deletePosition).removeValue();
                                        }
                                    });


                                    alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            // TODO Auto-generated method stub
                                            dialog.dismiss();
                                        }
                                    });
                                    alert.show();
                                    return false;
                                }
                            });

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