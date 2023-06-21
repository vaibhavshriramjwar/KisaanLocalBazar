package xyz.vpscorelim.kisaan.dealer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import xyz.vpscorelim.kisaan.Market.Market;
import xyz.vpscorelim.kisaan.R;
import xyz.vpscorelim.kisaan.SliderContent.SliderAdapterExample;
import xyz.vpscorelim.kisaan.SliderContent.SliderAdapterExampleDealer;
import xyz.vpscorelim.kisaan.SliderContent.SliderItem;
import xyz.vpscorelim.kisaan.Weather.Model.Weather;
import xyz.vpscorelim.kisaan.Weather.WeatherActivity;
import xyz.vpscorelim.kisaan.adapter.HomePageFarmerList;
import xyz.vpscorelim.kisaan.adapter.ModelFarmerData;
import xyz.vpscorelim.kisaan.customer.Customer_Home;
import xyz.vpscorelim.kisaan.customer.Near_by_farmers;
import xyz.vpscorelim.kisaan.customer.SearchOne;
import xyz.vpscorelim.kisaan.dealer.DealerAdaper.DealerHomePageFarmerList;
import xyz.vpscorelim.kisaan.farmer.Farmer_Home;
import xyz.vpscorelim.kisaan.farmer.Farmer_Profile;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.URLUtil;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.IndicatorView.draw.controller.DrawController;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Dealer_Home extends AppCompatActivity {


    ImageView profile;
    SliderView sliderView,sliderView1;
    private SliderAdapterExampleDealer adapter,adapter2;
    private View view,view2;

    //Card One Identity
    TextView card1TextTitle;
    MaterialButton card1Btn;
    ImageView card1Img;


    //Card two Identity
    TextView card2TextTitle,showMore1,showMore2;
    MaterialButton card2Btn;
    ImageView card2Img;

    LinearLayout weather,market,near_farmer,add_product;

    RecyclerView farmer_list,farmer_list_ar;
    RecyclerView.LayoutManager layoutManager,layoutManager1;
    FirebaseDatabase database;
    DatabaseReference reference;
    DealerHomePageFarmerList homePageFarmerList;
    List<ModelFarmerData> modelFarmerData;
    FirebaseAuth mAuth;
    FirebaseUser currentUser;

    MaterialCardView home_del,store_open;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dealer_home);
        loadLocale();
//        Window w = getWindow();
//        w.setFlags(WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_ALWAYS, WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_ALWAYS);
//        getSupportActionBar().hide();



        showMore2 = findViewById(R.id.txt5);
        showMore2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(Dealer_Home.this, SearchTwo.class);
                startActivity(intent1);
                Animatoo.animateZoom(Dealer_Home.this);
            }
        });
        showMore1 = findViewById(R.id.txt4);
        showMore1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(Dealer_Home.this, SearchTwo.class);
                startActivity(intent1);
                Animatoo.animateZoom(Dealer_Home.this);
            }
        });


        BottomNavigationView bottomNavigationView = findViewById(R.id.dealer_bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.dealer_dashboard);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.dealer_dashboard:
                        return true;
                    case R.id.dealer_profile:
                        startActivity(new Intent(getApplicationContext(), DealerViewMyCart.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.dealer_order:
                        startActivity(new Intent(getApplicationContext(), DealerOrderList.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.dealer_search:
                        startActivity(new Intent(getApplicationContext(), SearchTwo.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.dealer_profile2:
                        startActivity(new Intent(getApplicationContext(), Dealer_Profile.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });



        //Initialize Card1
        card1TextTitle = findViewById(R.id.textTitle);
        card1Btn = findViewById(R.id.make_order);
        card1Img = findViewById(R.id.ic_img);

        //Initialize card2
        card2TextTitle = findViewById(R.id.textTitle1);
        card2Btn = findViewById(R.id.make_order2);
        card2Img = findViewById(R.id.ic_img2);


        loadCard1Ads();
        loadCard2Ads();

        //add_info = findViewById(R.id.add_info);
        add_product = findViewById(R.id.linear2);



        near_farmer = findViewById(R.id.linear1);
        near_farmer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), SearchFarmerByDealer.class));
                overridePendingTransition(0,0);
            }
        });

        market = findViewById(R.id.linear3);
        market.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Market.class));
                overridePendingTransition(0,0);
            }
        });

        weather = findViewById(R.id.linear5);
        weather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), WeatherActivity.class));
                overridePendingTransition(0,0);
            }
        });

        add_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), DealerAddProduct.class));
                overridePendingTransition(0,0);
            }
        });

        home_del = findViewById(R.id.home_del);
        store_open = findViewById(R.id.store_open);


        farmer_list = findViewById(R.id.farmer_list);
        farmer_list.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        farmer_list.setLayoutManager(layoutManager);
        farmer_list.setHasFixedSize(true);


        farmer_list_ar = findViewById(R.id.farmer_list_ar);
        farmer_list_ar.setHasFixedSize(true);
        layoutManager1 = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        farmer_list_ar.setLayoutManager(layoutManager1);
        farmer_list_ar.setHasFixedSize(true);


        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        loadAreaFarmer();



        if (ContextCompat.checkSelfPermission(Dealer_Home.this, Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            // Ask for permision
            ActivityCompat.requestPermissions(this,new String[] { Manifest.permission.SEND_SMS}, 1);
        }
        else {
            Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
        }


        profile = findViewById(R.id.cart);
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(Dealer_Home.this, Dealer_Profile.class);
                startActivity(intent1);
            }
        });






        sliderView = findViewById(R.id.imageSlider);
        adapter = new SliderAdapterExampleDealer(this);
        sliderView.setSliderAdapter(adapter);
        sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM); //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
        sliderView.setIndicatorSelectedColor(Color.WHITE);
        sliderView.setIndicatorUnselectedColor(Color.GRAY);
        sliderView.setScrollTimeInSec(3);
        sliderView.setAutoCycle(true);
        sliderView.startAutoCycle();
        renewItems(view);
        sliderView.setOnIndicatorClickListener(new DrawController.ClickListener() {
            @Override
            public void onIndicatorClicked(int position) {
                //Log.i("GGG", "onIndicatorClicked: " + sliderView.getCurrentPagePosition());
            }
        });


        sliderView1 = findViewById(R.id.imageSlider1);
        adapter2 = new SliderAdapterExampleDealer(this);
        sliderView1.setSliderAdapter(adapter2);
        sliderView1.setIndicatorAnimation(IndicatorAnimationType.WORM); //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        sliderView1.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        sliderView1.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
        sliderView1.setIndicatorSelectedColor(Color.WHITE);
        sliderView1.setIndicatorUnselectedColor(Color.GRAY);
        sliderView1.setScrollTimeInSec(3);
        sliderView1.setAutoCycle(true);
        sliderView1.startAutoCycle();
        renewItems2(view2);
        sliderView1.setOnIndicatorClickListener(new DrawController.ClickListener() {
            @Override
            public void onIndicatorClicked(int position) {
                //Log.i("GGG", "onIndicatorClicked: " + sliderView.getCurrentPagePosition());
            }
        });




    }

    private void loadCard1Ads() {
        FirebaseDatabase card1;
        DatabaseReference card1ref;
        card1  = FirebaseDatabase.getInstance();
        card1ref = card1.getReference("Dealer_Ads");
        Query query = card1ref.orderByChild("id").equalTo("c1");
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
                                Toast.makeText(Dealer_Home.this, "Url is not specified", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    Glide.with(Dealer_Home.this).load(img).into(card1Img);
                    card1TextTitle.setText(title);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }


    private void loadCard2Ads(){

        FirebaseDatabase card1;
        DatabaseReference card1ref;
        card1  = FirebaseDatabase.getInstance();
        card1ref = card1.getReference("Dealer_Ads");
        Query query = card1ref.orderByChild("id").equalTo("c2");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds :snapshot.getChildren()){
                    String title          =""+ds.child("Title").getValue();
                    String img            =""+ds.child("image").getValue();
                    String url            =""+ds.child("url").getValue();
                    card2Btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent myWebLink = new Intent(android.content.Intent.ACTION_VIEW);
                            boolean urlcheck = URLUtil.isValidUrl(url);
                            if(urlcheck){
                                myWebLink.setData(Uri.parse(url));
                                startActivity(myWebLink);
                            }else{
                                Toast.makeText(Dealer_Home.this, "Url is not specified", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    Glide.with(Dealer_Home.this).load(img).into(card2Img);
                    card2TextTitle.setText(title);
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

    private void loadAreaFarmer() {

        database  = FirebaseDatabase.getInstance();
        reference = database.getReference("Dealer_Data");
        Query query = reference.orderByChild("phoneNumber").equalTo(currentUser.getPhoneNumber());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds :snapshot.getChildren()){
                    String customer_City_name =""+ds.child("districtName").getValue();
                    String subArea            =""+ds.child("subArea").getValue();
                    String name               =""+ds.child("dealerName").getValue();
                    String store              =""+ds.child("Store").getValue();
                    String home_de            =""+ds.child("Home_Delivery").getValue();

                    if(store.equals("close")){
                        store_open.setVisibility(View.VISIBLE);

                    }else {
                        store_open.setVisibility(View.GONE);

                    }

                    if(home_de.equals("No")){
                        home_del.setVisibility(View.VISIBLE);

                    }else {
                        home_del.setVisibility(View.GONE);

                    }

                    if(name.equals("null")){
                        startActivity(new Intent(getApplicationContext(), DealerFormPop.class));
                        overridePendingTransition(0,0);
                    }

                    loadAllFarmerArea(customer_City_name);
                    loadNearByTahsil(subArea);



                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void loadNearByTahsil(String subArea) {


        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Farmer_Data");
        ref.orderByChild("Tehsil_Name").equalTo(subArea)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        modelFarmerData = new ArrayList<>();
                        for (DataSnapshot pvostsnap : snapshot.getChildren()) {
                            ModelFarmerData post = pvostsnap.getValue(ModelFarmerData.class);
                            //farmerProductData.clear();
                            modelFarmerData.add(post);
                        }
                        homePageFarmerList = new DealerHomePageFarmerList(Dealer_Home.this, modelFarmerData);
                        farmer_list_ar.setAdapter(homePageFarmerList);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });



    }

    private void loadAllFarmerArea(String customer_city_name) {

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Farmer_Data");
        ref.orderByChild("District_Name").equalTo(customer_city_name)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        modelFarmerData = new ArrayList<>();
                        for (DataSnapshot pvostsnap : snapshot.getChildren()) {
                            ModelFarmerData post = pvostsnap.getValue(ModelFarmerData.class);
                            //farmerProductData.clear();
                            modelFarmerData.add(post);
                        }
                        homePageFarmerList = new DealerHomePageFarmerList(Dealer_Home.this, modelFarmerData);
                        farmer_list.setAdapter(homePageFarmerList);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


    }




    private void renewItems(View view) {
        this.view = view;
        FirebaseDatabase card1;
        DatabaseReference card1ref;
        card1  = FirebaseDatabase.getInstance();
        card1ref = card1.getReference("Slide_Images");
        Query query = card1ref.orderByChild("id").equalTo("deaSlide");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds :snapshot.getChildren()){
                    String img1         =""+ds.child("Image1").getValue();
                    String img2         =""+ds.child("Image2").getValue();
                    String img3         =""+ds.child("Image3").getValue();
                    String img4         =""+ds.child("Image4").getValue();
                    List<SliderItem> sliderItemList = new ArrayList<>();
                    //dummy data

                    for (int i = 0; i < 5; i++) {
                        SliderItem sliderItem = new SliderItem();
                        sliderItem.setDescription("Slider Item " + i);
                        if (i % 2 == 1) {
                            sliderItem.setImageUrl(img1);
                        } else if(i%3 ==1){
                            sliderItem.setImageUrl(img2);
                        }else if(i%4 ==0) {
                            sliderItem.setImageUrl(img3);
                        }else {
                            sliderItem.setImageUrl(img4);

                        }

                        sliderItemList.add(sliderItem);
                    }
                    adapter.renewItems(sliderItemList);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });



    }



    private void renewItems2(View view2) {
        this.view2 = view2;
        FirebaseDatabase card1;
        DatabaseReference card1ref;
        card1  = FirebaseDatabase.getInstance();
        card1ref = card1.getReference("Slide_Images");
        Query query = card1ref.orderByChild("id").equalTo("deaSlide");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds :snapshot.getChildren()){
                    String img1         =""+ds.child("Image1").getValue();
                    String img2         =""+ds.child("Image2").getValue();
                    String img3         =""+ds.child("Image3").getValue();
                    String img4         =""+ds.child("Image4").getValue();
                    List<SliderItem> sliderItemList = new ArrayList<>();
        //dummy data

        for (int i = 0; i < 5; i++) {
            SliderItem sliderItem = new SliderItem();
            sliderItem.setDescription("Slider Item " + i);
            if (i % 2 == 1) {
                sliderItem.setImageUrl(img1);
            } else if(i%3 ==1){
                sliderItem.setImageUrl(img2);
            }else if(i%4 ==0) {
                sliderItem.setImageUrl(img3);
            }else {
                sliderItem.setImageUrl(img4);

            }

            sliderItemList.add(sliderItem);
        }
        adapter2.renewItems(sliderItemList);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

    }
}