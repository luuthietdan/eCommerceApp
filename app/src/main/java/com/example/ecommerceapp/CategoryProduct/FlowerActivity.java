package com.example.ecommerceapp.CategoryProduct;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ecommerceapp.DetailFlowerActivity;
import com.example.ecommerceapp.Model.Food;
import com.example.ecommerceapp.R;
import com.example.ecommerceapp.ViewHolder.ProductFoodViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FlowerActivity extends AppCompatActivity {

    private TextView txtPriceFlower, txtProductNameFlower;
    private ImageView imgProductFlower;
    private RecyclerView rvListFlower;
    private DatabaseReference mDBListFlower;
    private FirebaseRecyclerAdapter adapterFlower;
    private Toolbar toolbarFlower;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flower);

        Init();
        setSupportActionBar(toolbarFlower);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Flower");
        mDBListFlower = FirebaseDatabase.getInstance().getReference().child("Flower");
        rvListFlower.setHasFixedSize(true);
        rvListFlower.setLayoutManager(new LinearLayoutManager(this));
        DisplayListFood();
    }

    private void Init() {
        txtPriceFlower=findViewById(R.id.txtPriceFood);
        txtProductNameFlower=findViewById(R.id.txtProductNameFood);
        imgProductFlower=findViewById(R.id.imgProductFood);
        rvListFlower=findViewById(R.id.rvListFlower);
        toolbarFlower=findViewById(R.id.toolbar_flower);
    }

    private void DisplayListFood() {
        FirebaseRecyclerOptions<Food> firebaseRecyclerOptions=new FirebaseRecyclerOptions.Builder<Food>()
                .setQuery(mDBListFlower,Food.class)
                .build();
        adapterFlower=new FirebaseRecyclerAdapter<Food, ProductFoodViewHolder>(firebaseRecyclerOptions) {

            @NonNull
            @Override
            public ProductFoodViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.display_product, viewGroup, false);

                return new ProductFoodViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull final ProductFoodViewHolder holder, int position, @NonNull final Food model) {
                final String cartId = getRef(holder.getAdapterPosition()).getKey();
                mDBListFlower.child(cartId).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            final String flowerName = dataSnapshot.child("name").getValue().toString();
                            final String flowerImage = dataSnapshot.child("image").getValue().toString();
                            final String flowerPrice = dataSnapshot.child("price").getValue().toString();

                            holder.setFoodName(flowerName);
                            holder.setFoodImage(flowerImage);
                            holder.setFoodPrice(flowerPrice);


                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intentDetail = new Intent(FlowerActivity.this, DetailFlowerActivity.class);
                        intentDetail.putExtra("id", model.getId());
                        startActivity(intentDetail);
                    }
                });
            }


        };

        rvListFlower.setAdapter(adapterFlower);
    }
    @Override
    public void onStart() {
        super.onStart();
        adapterFlower.startListening();



    }

    @Override
    public void onStop() {
        super.onStop();
        adapterFlower.stopListening();

    }
}
