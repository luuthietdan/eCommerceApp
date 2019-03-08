package com.example.ecommerceapp;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.example.ecommerceapp.Adapter.MySliderAdapter;
import com.example.ecommerceapp.Interface.IBannerLoadingDone;
import com.example.ecommerceapp.Model.Banner;
import com.example.ecommerceapp.Service.PicassoLoadingService;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ss.com.bannerslider.Slider;

public class HomeActivity extends AppCompatActivity  {


    private RecyclerView rvPostList;

    private FrameLayout mContainer;
    private BottomNavigationView mNavView;
    private  HomeFragment homeFragment;
    private CartFragment cartFragment;
    private NotificationFragment notificationFragment;

    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Init();

        homeFragment=new HomeFragment();
        cartFragment=new CartFragment();
        notificationFragment=new NotificationFragment();

//        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,R.color.colorPrimaryDark);
//        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                loadBanner();
//            }
//        });
//        swipeRefreshLayout.post(new Runnable() {
//            @Override
//            public void run() {
//
//            }
//        });
     //   setupSlider();
        mNavView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.nav_home:
                        setFragment(homeFragment);
                        return true;
                    case R.id.nav_cart:
                        setFragment(cartFragment);
                        return true;
                    case R.id.nav_notification:
                        setFragment(notificationFragment);
                        return  true;
                        default: return  false;

                }
            }
        });
    }



    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_container,fragment);
        fragmentTransaction.commit();
    }

//    private void setupSlider() {
//        hashMap=new HashMap<>();
//        final DatabaseReference mDBRef= FirebaseDatabase.getInstance().getReference().child("Banner");
//        mDBRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                for (DataSnapshot postData:dataSnapshot.getChildren()){
//                    Banner banner=postData.getValue(Banner.class);
//                    hashMap.put(banner.getName()+"_"+banner.getId(),banner.getImage());
//
//                }
//                for (String key: hashMap.keySet()){
//                    String[] keySplit=key.split("_");
//                    String name=keySplit[0];
//                    String id=keySplit[1];
//
//                    TextSliderView textSliderView=new TextSliderView(getBaseContext());
//                    textSliderView.description(name)
//                            .image(hashMap.get(key))
//                            .setScaleType(BaseSliderView.ScaleType.Fit)
//                            .setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
//                                @Override
//                                public void onSliderClick(BaseSliderView slider) {
//
//                                }
//                            });
//                    mSlider.addSlider(textSliderView);
//                    mDBRef.removeEventListener(this);
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//        mSlider.setPresetTransformer(SliderLayout.Transformer.Background2Foreground);
//        mSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
//        mSlider.setCustomAnimation(new DescriptionAnimation());
//    }

    private void Init() {

     //   mSlider=findViewById(R.id.sliderLayout);
        mContainer=findViewById(R.id.main_container);
        mNavView=findViewById(R.id.navigationView);



    }


}
