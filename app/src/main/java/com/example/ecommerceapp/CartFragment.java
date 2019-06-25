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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
    private DatabaseReference mDBProductsProcess;
    private FirebaseRecyclerAdapter adapterCart;
    private List<Cart> cartList;
    private FirebaseAuth mAuth;
    private String CurrentUserId = "";

    private Button btnShip;

    ArrayList<Cart> mArrayList = new ArrayList<>();
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

        btnShip = view.findViewById(R.id.btnShip);

        mAuth = FirebaseAuth.getInstance();
        CurrentUserId = mAuth.getCurrentUser().getUid();
        mDBListCart = FirebaseDatabase.getInstance().getReference().child("Cart").child(CurrentUserId);
        mDBProductsProcess = FirebaseDatabase.getInstance().getReference().child("ProductsProcess").child(CurrentUserId);
        rvListCart.setHasFixedSize(true);
        rvListCart.setLayoutManager(new LinearLayoutManager(getContext()));
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        rvListCart.setLayoutManager(mLayoutManager);


        DisplayListCart();
        btnShip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.app.AlertDialog.Builder mAlert = new android.app.AlertDialog.Builder(getContext());
//                final EditText input = new EditText(getContext());
//
//                input.setInputType(InputType.TYPE_CLASS_TEXT);
//                mAlert.setView(input);

                mAlert.setTitle("Announement")
                        .setMessage("Are you sure you want to book these?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
//                                final HashMap<String, Object> cartMap = new HashMap<>();
//                                cartMap.put("address", input.getText().toString());
//                                mDBProductsProcess.push().updateChildren(cartMap);
                                for(int i =0;i<mArrayList.size();i++){
                                    //cartMap.put(i + "",mArrayList.get(i));
                                    // Cart cart =  new Cart(" "," "," ","Quan "+i,"15000"," "," ");
                                    mDBProductsProcess.push().setValue(mArrayList.get(i));
                                }
                                mArrayList.clear();
                                mDBListCart.removeValue();
                                // mDBProductsProcess.setValue(cartMap);
                                Log.d("ArrayList: ",String.valueOf(mArrayList.size()));
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                mAlert.show();
            }
        });


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


                            //Log.d("Money", totalMoney+1 + "");
                            //TODO BUG Quantity here
                            final int price = Integer.parseInt(cart.getQuantity()) * Integer.parseInt(cart.getPrice()) + Integer.valueOf( txtTotalMoney.getText().toString().replace("$",""));

                            holder.setNameCart(cart.getName());
                            holder.setImageCart(cart.getImage());
                            holder.setPriceCart(price + "$");
                            holder.setDate(cart.getDate());
                            holder.setQuantity(cart.getQuantity());
                            holder.chkBuy.setTag(holder.getAdapterPosition());
                            final int x=0;
                            holder.chkBuy.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (holder.chkBuy.isChecked()) {

                                        txtTotalMoney.setText(price + "$");
                                    } else {
                                        if(!holder.chkBuy.isChecked())
                                        {
                                            int priceReduce =price -  Integer.parseInt(cart.getPrice())*Integer.parseInt(cart.getQuantity());
                                            txtTotalMoney.setText(priceReduce+"$");
                                        }



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
                                    final int deletePosition = holder.getAdapterPosition();
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

                            final  String cartId = dataSnapshot.child("id").getValue().toString();
                            final String cartQuantity = dataSnapshot.child("quantity").getValue().toString();
                            final String cartDescription = dataSnapshot.child("description").getValue().toString();
                            final String cartName = dataSnapshot.child("name").getValue().toString();
                            final String cartImage = dataSnapshot.child("image").getValue().toString();
                            final String cartPrice = dataSnapshot.child("price").getValue().toString();
                            final String cartTime = dataSnapshot.child("date").getValue().toString();

                            holder.setNameCart(cartName);
                            holder.setImageCart(cartImage);
                            holder.setPriceCart(cartPrice);
                            holder.setDate(cartTime);
                            mArrayList.add(new Cart(" ",cartDescription,cartId,cartName,cartPrice,cartQuantity,cartTime,cartImage));

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