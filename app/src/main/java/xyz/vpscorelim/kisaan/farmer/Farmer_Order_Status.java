package xyz.vpscorelim.kisaan.farmer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.URLUtil;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import xyz.vpscorelim.kisaan.R;
import xyz.vpscorelim.kisaan.adapter.OrderViewHolder;
import xyz.vpscorelim.kisaan.adapter.ViewPagerAdapter;
import xyz.vpscorelim.kisaan.customer.CustomerMyOrder;
import xyz.vpscorelim.kisaan.customer.CustomerOrderDetails;
import xyz.vpscorelim.kisaan.customer.CustomerRequest;
import xyz.vpscorelim.kisaan.farmer.farmer_adapter.FarmerOrderViewHolder;

public class Farmer_Order_Status extends AppCompatActivity {

    RecyclerView order_list;
    RecyclerView.LayoutManager layoutManager;
    FirebaseDatabase database;
    DatabaseReference reference;

    FirebaseAuth mAuth;
    FirebaseUser currentUser;


    FarmerOrderViewHolder orderViewHolder;
    List<CustomerRequest> customerRequests;


     TabLayout tabLayout;
     ViewPager viewPager;


     ImageView back;

    //Card One Identity
    TextView card1TextTitle;
    MaterialButton card1Btn;
    ImageView card1Img;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.farmer_order_status);
        loadLocale();
        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_ALWAYS, WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_ALWAYS);
        getSupportActionBar().hide();


        tabLayout        =findViewById(R.id.tablayout_id);
        viewPager        =findViewById(R.id.viewpager2);


        //Initialize Card1
        card1TextTitle = findViewById(R.id.textTitle);
        card1Btn = findViewById(R.id.make_order);
        card1Img = findViewById(R.id.ic_img);
        loadCard1Ads();


        back= findViewById(R.id.close);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });



        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());


        //Adding Fragments
        viewPagerAdapter.AddFragment(new FarmerArriveOrders(),"My Orders");
        viewPagerAdapter.AddFragment(new FarmerMyOrders(),"Orders");



        //Adapter Setup
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);







        order_list = findViewById(R.id.order_list);
        order_list.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        order_list.setLayoutManager(layoutManager);
        order_list.setHasFixedSize(true);

        database = FirebaseDatabase.getInstance();
        reference = database.getReference("RequestOrder");

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        String farmer_uid = currentUser.getUid();


        loadAllOrder(farmer_uid);




        final BottomNavigationView bottomNavigationView = findViewById(R.id.farmer_bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.farmer_orders);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.farmer_dashboard:
                        startActivity(new Intent(getApplicationContext(),Farmer_Home.class));
                        Animatoo.animateSlideRight(Farmer_Order_Status.this);
                        return true;
                    case R.id.farmer_profile:
                        startActivity(new Intent(getApplicationContext(),FarmerViewMyCart.class));
                        Animatoo.animateSlideLeft(Farmer_Order_Status.this);
                        return true;
                    case R.id.farmer_orders:
                        return true;
                    case R.id.farmer_search:
                        startActivity(new Intent(getApplicationContext(),SearchDealerFarmer.class));
                        Animatoo.animateSlideRight(Farmer_Order_Status.this);
                        return true;
                    case R.id.farmer_profile_new:
                        startActivity(new Intent(getApplicationContext(),Farmer_Profile.class));
                        Animatoo.animateSlideLeft(Farmer_Order_Status.this);
                        return true;
                }
                return false;
            }
        });





    }

    private void loadCard1Ads() {
        FirebaseDatabase card1;
        DatabaseReference card1ref;
        card1  = FirebaseDatabase.getInstance();
        card1ref = card1.getReference("Farmer_ads");
        Query query = card1ref.orderByChild("id").equalTo("c3");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds :snapshot.getChildren()){
                    String title          =""+ds.child("Title").getValue();
                    String img            =""+ds.child("image").getValue();
                    String url            =""+ds.child("url").getValue();
                    card1Btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent myWebLink = new Intent(android.content.Intent.ACTION_VIEW);
                            boolean urlcheck = URLUtil.isValidUrl(url);
                            if(urlcheck){
                                myWebLink.setData(Uri.parse(url));
                                startActivity(myWebLink);
                            }else{
                                Toast.makeText(Farmer_Order_Status.this, "Url is not specified", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    Glide.with(Farmer_Order_Status.this).load(img).into(card1Img);
                    card1TextTitle.setText(title);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    public  void loadLocale(){
        SharedPreferences preferences = getSharedPreferences("Setting", Activity.MODE_PRIVATE);
        String lamguage = preferences.getString("My_Language","");
        Locale locale =new Locale(lamguage);
        Locale.setDefault(locale);

    }

    private void loadAllOrder(String farmer_uid) {

        Query query = reference.orderByChild("farmer_id").equalTo(farmer_uid);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                customerRequests = new ArrayList<>();
                for (DataSnapshot pvostsnap : dataSnapshot.getChildren()) {
                    CustomerRequest post = pvostsnap.getValue(CustomerRequest.class);
                    //farmerProductData.clear();
                    customerRequests.add(post);
                    Collections.reverse(customerRequests);
                }
                orderViewHolder = new FarmerOrderViewHolder(Farmer_Order_Status.this, customerRequests);
                order_list.setAdapter(orderViewHolder);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(Farmer_Order_Status.this, ""+databaseError.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });



    }
}