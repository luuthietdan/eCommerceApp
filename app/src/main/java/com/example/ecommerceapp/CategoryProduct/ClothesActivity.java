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

import com.example.ecommerceapp.DetailClothesActivity;
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

public class ClothesActivity extends AppCompatActivity {

    private TextView txtPriceClothes, txtProductNameClothes;
    private ImageView imgProductClothes;
    private RecyclerView rvListClothes;
    private DatabaseReference mDBListClothes;
    private FirebaseRecyclerAdapter adapterClothes;
    private Toolbar toolbarClothes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clothes);

        Init();
        setSupportActionBar(toolbarClothes);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Clothes");
        mDBListClothes = FirebaseDatabase.getInstance().getReference().child("Clothes");
        rvListClothes.setHasFixedSize(true);
        rvListClothes.setLayoutManager(new LinearLayoutManager(this));
        DisplayListFood();
    }

    private void Init() {
        txtPriceClothes=findViewById(R.id.txtPriceFood);
        txtProductNameClothes=findViewById(R.id.txtProductNameFood);
        imgProductClothes=findViewById(R.id.imgProductFood);
        rvListClothes=findViewById(R.id.rvListClothes);
        toolbarClothes=findViewById(R.id.toolbar_clothes);
    }

    private void DisplayListFood() {
        FirebaseRecyclerOptions<Food> firebaseRecyclerOptions=new FirebaseRecyclerOptions.Builder<Food>()
                .setQuery(mDBListClothes,Food.class)
                .build();
        adapterClothes=new FirebaseRecyclerAdapter<Food, ProductFoodViewHolder>(firebaseRecyclerOptions) {

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
                mDBListClothes.child(cartId).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            final String clothesName = dataSnapshot.child("name").getValue().toString();
                            final String clothesImage = dataSnapshot.child("image").getValue().toString();
                            final String clothesPrice = dataSnapshot.child("price").getValue().toString();

                            holder.setFoodName(clothesName);
                            holder.setFoodImage(clothesImage);
                            holder.setFoodPrice(clothesPrice);


                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intentDetail = new Intent(ClothesActivity.this, DetailClothesActivity.class);
                        intentDetail.putExtra("id", model.getId());
                        startActivity(intentDetail);
                    }
                });
            }


        };

        rvListClothes.setAdapter(adapterClothes);
    }

    @Override
    public void onStart() {
        super.onStart();
        adapterClothes.startListening();



    }

    @Override
    public void onStop() {
        super.onStop();
        adapterClothes.stopListening();

    }
}
