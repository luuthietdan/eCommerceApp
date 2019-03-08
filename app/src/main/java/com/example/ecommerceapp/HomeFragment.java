package com.example.ecommerceapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ecommerceapp.Adapter.MySliderAdapter;
import com.example.ecommerceapp.CategoryProduct.FoodActivity;
import com.example.ecommerceapp.CategoryProduct.LaptopActivity;
import com.example.ecommerceapp.Interface.IBannerLoadingDone;
import com.example.ecommerceapp.Model.Category;

import com.example.ecommerceapp.Model.Food;
import com.example.ecommerceapp.Service.PicassoLoadingService;
import com.example.ecommerceapp.ViewHolder.FoodViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import ss.com.bannerslider.Slider;


public class HomeFragment extends Fragment implements IBannerLoadingDone {

    private RecyclerView rvCategory,rvFood;
    private DatabaseReference mDBCategory,mDBFood;
    private FirebaseRecyclerAdapter adapter,adapterFood;
    private Slider slider;
    private DatabaseReference mDBBanner;
    private IBannerLoadingDone bannerLoadingDone;
    public HomeFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView=inflater.inflate(R.layout.fragment_home, container, false);
        mDBCategory= FirebaseDatabase.getInstance().getReference().child("Category");
        mDBFood=FirebaseDatabase.getInstance().getReference().child("Food");
        rvCategory=rootView.findViewById(R.id.rvCategory);
        rvFood=rootView.findViewById(R.id.rvListHotFood);
        slider=rootView.findViewById(R.id.slider);
        mDBBanner=FirebaseDatabase.getInstance().getReference().child("Banner");
        bannerLoadingDone=this;
        Slider.init(new PicassoLoadingService());
        loadBanner();
        rvFood.setHasFixedSize(true);
        rvCategory.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        rvCategory.setLayoutManager(linearLayoutManager);
//        int numberOfColumns = 4;
        int numberOfColumsForFood=2;
//        rvCategory.setLayoutManager(new GridLayoutManager(getActivity(), numberOfColumns));
        rvFood.setLayoutManager(new GridLayoutManager(getActivity(), numberOfColumsForFood));
        DisplayCategory();
        DisplayFood();
        return rootView;
    }
    private void loadBanner() {
        mDBBanner.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<String> bannerList=new ArrayList<>();
                for (DataSnapshot bannerSnapshot:dataSnapshot.getChildren()){
                    String image=bannerSnapshot.child("image").getValue(String.class);
                    bannerList.add(image);
                }
                bannerLoadingDone.onBannerLoadingDoneListener(bannerList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    @Override
    public void onBannerLoadingDoneListener(List<String> banners) {
        slider.setAdapter(new MySliderAdapter(banners));

    }

    private void DisplayFood() {
        FirebaseRecyclerOptions<Food> firebaseRecyclerOptions=new FirebaseRecyclerOptions.Builder<Food>()
                .setQuery(mDBFood,Food.class)
                .build();
         adapterFood=new FirebaseRecyclerAdapter<Food,FoodViewHolder>(firebaseRecyclerOptions) {
           @Override
           protected void onBindViewHolder(@NonNull final FoodViewHolder holder, int position, @NonNull final Food model) {
               final String foodId=getRef(position).getKey();
               mDBFood.child(foodId).addValueEventListener(new ValueEventListener() {
                   @Override
                   public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                       if (dataSnapshot.exists()){
                           final String foodName=dataSnapshot.child("name").getValue().toString();
                           final String foodImage=dataSnapshot.child("image").getValue().toString();
                           final String foodPrice=dataSnapshot.child("price").getValue().toString();
                           holder.setFoodName(foodName);
                           holder.setFoodImage(foodImage);
                           holder.setFoodPrice(foodPrice);
                           holder.itemView.setOnClickListener(new View.OnClickListener() {
                               @Override
                               public void onClick(View v) {
                                   Intent intentDetail=new Intent(getActivity(),DetailProductActivity.class);
                                   intentDetail.putExtra("id",model.getId());
                                   startActivity(intentDetail);
                               }
                           });
                       }

                   }

                   @Override
                   public void onCancelled(@NonNull DatabaseError databaseError) {

                   }
               });
           }


           @NonNull
           @Override
           public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
               View view = LayoutInflater.from(viewGroup.getContext())
                       .inflate(R.layout.display_suggestion, viewGroup, false);

               return new FoodViewHolder(view);
           }
       };
       rvFood.setAdapter(adapterFood);
    }

    private void DisplayCategory() {
        FirebaseRecyclerOptions<Category> firebaseRecyclerOptions=new FirebaseRecyclerOptions.Builder<Category>()
                .setQuery(mDBCategory,Category.class)
                .build();
          adapter=new FirebaseRecyclerAdapter<Category,CategoryViewHolder>(
              firebaseRecyclerOptions
        ){

            @NonNull
            @Override
            public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.dislay_category, viewGroup, false);

                return new CategoryViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull final CategoryViewHolder holder, final int position, @NonNull Category model) {
                final String categoryId=getRef(position).getKey();
                mDBCategory.child(categoryId).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()){
                            final String categoryName=dataSnapshot.child("name").getValue().toString();
                            final String categoryImage=dataSnapshot.child("image").getValue().toString();

                            holder.setCategoryName(categoryName);
                            holder.setCategoryImage(categoryImage);
                            holder.itemView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    switch (position){
                                        case 0:
                                            Intent intentProduct=new Intent(getActivity(), FoodActivity.class);
                                            startActivity(intentProduct);
                                            break;
                                        case 1:
                                            Intent intentClothes=new Intent(getActivity(), LaptopActivity.class);
                                            startActivity(intentClothes);
                                            break;
                                        case 5:
                                            Intent intentLaptop=new Intent(getActivity(), LaptopActivity.class);
                                            startActivity(intentLaptop);
                                            break;
                                            default: break;
                                    }

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
        rvCategory.setAdapter(adapter);

    }


    public static class CategoryViewHolder extends RecyclerView.ViewHolder {
        private View mView;
        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            mView=itemView;

        }
        public void setCategoryImage(String categoryImage){
            ImageView imgCategory=mView.findViewById(R.id.imgCategory);
            Picasso.get().load(categoryImage).into(imgCategory);
        }
        public void setCategoryName(String categoryName){
            TextView txtCategory=mView.findViewById(R.id.txtCategory);
            txtCategory.setText(categoryName);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
        adapterFood.startListening();


    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
        adapterFood.stopListening();
    }
}
