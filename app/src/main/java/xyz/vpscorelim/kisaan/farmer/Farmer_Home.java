package xyz.vpscorelim.kisaan.farmer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import cn.pedant.SweetAlert.SweetAlertDialog;
import xyz.vpscorelim.kisaan.CheckUserProfile;
import xyz.vpscorelim.kisaan.Log_In;
import xyz.vpscorelim.kisaan.Market.Market;
import xyz.vpscorelim.kisaan.R;
import xyz.vpscorelim.kisaan.SliderContent.SliderAdapterExample;
import xyz.vpscorelim.kisaan.SliderContent.SliderAdapterExampleFarmer;
import xyz.vpscorelim.kisaan.SliderContent.SliderItem;
import xyz.vpscorelim.kisaan.Weather.Model.Weather;
import xyz.vpscorelim.kisaan.Weather.WeatherActivity;
import xyz.vpscorelim.kisaan.adapter.HomePageFarmerList;
import xyz.vpscorelim.kisaan.adapter.ModelFarmerData;
import xyz.vpscorelim.kisaan.customer.Customer_Home;
import xyz.vpscorelim.kisaan.customer.Customer_Info_Form;
import xyz.vpscorelim.kisaan.customer.Customer_Profile;
import xyz.vpscorelim.kisaan.customer.Near_by_farmers;
import xyz.vpscorelim.kisaan.customer.SearchOne;
import xyz.vpscorelim.kisaan.dealer.Dealer_Home;
import xyz.vpscorelim.kisaan.farmer.farmer_Model.GetDealerModel;
import xyz.vpscorelim.kisaan.farmer.farmer_adapter.FarmerHomePageDealerProfileAdapter;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.URLUtil;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.bumptech.glide.Glide;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.FusedLocationProviderClient;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.snackbar.Snackbar;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import static android.net.ConnectivityManager.TYPE_WIFI;
import static android.provider.ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE;

public class Farmer_Home extends AppCompatActivity {

    Dialog myDialog,change_language;
    MaterialCardView card1,add_product,my_products,my_orders;
    SliderView sliderView,sliderView1;
    private SliderAdapterExampleFarmer adapter,adapter1;
    private View view,view2;
    private Snackbar snackbar;
    private RelativeLayout coordinatorLayout;
    private boolean internetConnected=true;

    public static int TYPE_WIFI = 1;
    public static int TYPE_MOBILE = 2;
    public static int TYPE_NOT_CONNECTED = 0;

    LinearLayout linear3,myProduct,nearByDealer,weather;
    ImageView profileClick;

    FarmerHomePageDealerProfileAdapter homePageFarmerList;
    List<GetDealerModel> modelFarmerData;
    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    RecyclerView farmer_list,farmer_list_ar;
    RecyclerView.LayoutManager layoutManager,layoutManager1;
    FirebaseDatabase database;
    DatabaseReference reference;

    //Card One Identity
    TextView card1TextTitle,showMore1,showMore2;
    MaterialButton card1Btn;
    ImageView card1Img;


    //Card two Identity
    TextView card2TextTitle;
    MaterialButton card2Btn;
    ImageView card2Img;





    MaterialCardView home_del,store_open;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.farmer_home);
        loadLocale();
        myDialog =new Dialog(this);
//        Window w = getWindow();
//        w.setFlags(WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_ALWAYS, WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_ALWAYS);
//        getSupportActionBar().hide();
        registerInternetCheckReceiver();


        showMore1 = findViewById(R.id.txt4);
        showMore1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(Farmer_Home.this, SearchDealerFarmer.class);
                startActivity(intent1);
                Animatoo.animateZoom(Farmer_Home.this);
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


        home_del = findViewById(R.id.home_del);
        store_open = findViewById(R.id.store_open);

        loadCard1Ads();
        loadCard2Ads();

        farmer_list = findViewById(R.id.farmer_list);
        farmer_list.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        farmer_list.setLayoutManager(layoutManager);
        farmer_list.setHasFixedSize(true);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        loadAreaFarmer();

        profileClick = findViewById(R.id.cart);
        profileClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent1 = new Intent(Farmer_Home.this, Farmer_Profile.class);
                startActivity(intent1);
                Animatoo.animateZoom(Farmer_Home.this);


            }
        });


        coordinatorLayout = findViewById(R.id.coordinate);


        linear3 = findViewById(R.id.linear3);
        linear3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(Farmer_Home.this, Market.class);
                startActivity(intent1);
                Animatoo.animateZoom(Farmer_Home.this);
            }
        });

        myProduct = findViewById(R.id.linear1);
        myProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(Farmer_Home.this,Farmer_Add_product.class);
                startActivity(intent1);
                Animatoo.animateZoom(Farmer_Home.this);
//                FragmentManager fm = getSupportFragmentManager();
//                Farmer_My_Product fragment = new Farmer_My_Product();
//                fm.beginTransaction()
//                        .add(R.id.container,fragment).addToBackStack(null)
//                        .commit();
            }
        });


        nearByDealer = findViewById(R.id.linear2);
        nearByDealer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(Farmer_Home.this, SearchDealerFarmer.class);
                startActivity(intent1);
                Animatoo.animateZoom(Farmer_Home.this);
            }
        });

        weather =  findViewById(R.id.linear5);
        weather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(Farmer_Home.this, WeatherActivity.class);
                startActivity(intent1);
                Animatoo.animateZoom(Farmer_Home.this);
            }
        });







        sliderView = findViewById(R.id.imageSlider);
        adapter = new SliderAdapterExampleFarmer(this);
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




        my_products = findViewById(R.id.my_products);
        my_orders = findViewById(R.id.my_order);
        card1       = findViewById(R.id.card_pop);
        add_product = findViewById(R.id.add_product);
        card1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(Farmer_Home.this, Farmer_popup.class);
                startActivity(intent1);
            }
        });
        my_orders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(Farmer_Home.this, Farmer_Order_Status.class);
                startActivity(intent1);
                //ShowPopUpForAddProduct();
            }
        });
        add_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(Farmer_Home.this, Farmer_Add_product.class);
                startActivity(intent1);
                //ShowPopUpForAddProduct();
            }
        });



        final BottomNavigationView bottomNavigationView = findViewById(R.id.farmer_bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.farmer_dashboard);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.farmer_dashboard:
                        return true;
                    case R.id.farmer_profile:
                        startActivity(new Intent(getApplicationContext(),FarmerViewMyCart.class));
                        Animatoo.animateSlideLeft(Farmer_Home.this);
                        return true;
                    case R.id.farmer_orders:
                        startActivity(new Intent(getApplicationContext(),Farmer_Order_Status.class));
                        Animatoo.animateSlideLeft(Farmer_Home.this);
                        return true;
                    case R.id.farmer_search:
                        startActivity(new Intent(getApplicationContext(),SearchDealerFarmer.class));
                        Animatoo.animateSlideLeft(Farmer_Home.this);
                        return true;
                    case R.id.farmer_profile_new:
                        startActivity(new Intent(getApplicationContext(),Farmer_Profile.class));
                        Animatoo.animateSlideLeft(Farmer_Home.this);
                        return true;

                }
                return false;
            }
        });



        my_products.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getSupportFragmentManager();
                Farmer_My_Product fragment = new Farmer_My_Product();
                fm.beginTransaction()
                        .add(R.id.container,fragment).addToBackStack(null)
                        .commit();

            }
        });





    }


    private void loadCard1Ads() {
        FirebaseDatabase card1;
        DatabaseReference card1ref;
        card1  = FirebaseDatabase.getInstance();
        card1ref = card1.getReference("Farmer_Ads");
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
                                Toast.makeText(Farmer_Home.this, "Url is not specified", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    Glide.with(Farmer_Home.this).load(img).into(card1Img);
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
        card1ref = card1.getReference("Farmer_Ads");
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
                                Toast.makeText(Farmer_Home.this, "Url is not specified", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    Glide.with(Farmer_Home.this).load(img).into(card2Img);
                    card2TextTitle.setText(title);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

    }

    private void loadAreaFarmer() {

        database  = FirebaseDatabase.getInstance();
        reference = database.getReference("Farmer_Data");
        Query query = reference.orderByChild("phoneNumber").equalTo(currentUser.getPhoneNumber());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds :snapshot.getChildren()){
                    String customer_City_name =""+ds.child("District_Name").getValue();
                    String subArea            =""+ds.child("Tehsil_Name").getValue();
                    String name            =""+ds.child("Farmer_Name").getValue();
                    String store_open_not            =""+ds.child("Store").getValue();
                    String home_delivery            =""+ds.child("Home_Delivery").getValue();

                    if(store_open_not.equals("close")){
                        store_open.setVisibility(View.VISIBLE);

                    }else {
                        store_open.setVisibility(View.GONE);

                    }

                    if(home_delivery.equals("No")){
                        home_del.setVisibility(View.VISIBLE);

                    }else {
                        home_del.setVisibility(View.GONE);

                    }




                    if(name.equals("null")){
                        startActivity(new Intent(getApplicationContext(), Farmer_popup.class));
                        overridePendingTransition(0,0);
                    }

                    loadAllFarmerArea(customer_City_name);
                    //loadNearByTahsil(subArea);



                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void loadNearByTahsil(String subArea) {


        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Dealer_Data");
        ref.orderByChild("districtName").equalTo(subArea)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        modelFarmerData = new ArrayList<>();
                        for (DataSnapshot pvostsnap : snapshot.getChildren()) {
                            ModelFarmerData post = pvostsnap.getValue(ModelFarmerData.class);
                            //farmerProductData.clear();
                            //modelFarmerData.add(post);
                        }
                       // homePageFarmerList = new HomePageFarmerList(Farmer_Home.this, modelFarmerData);
                        farmer_list_ar.setAdapter(homePageFarmerList);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });



    }

    private void loadAllFarmerArea(String customer_city_name) {

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Dealer_Data");
        ref.orderByChild("districtName").equalTo(customer_city_name)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        modelFarmerData = new ArrayList<>();
                        for (DataSnapshot pvostsnap : snapshot.getChildren()) {
                            GetDealerModel post = pvostsnap.getValue(GetDealerModel.class);
                            //farmerProductData.clear();
                            modelFarmerData.add(post);
                        }
                        homePageFarmerList = new FarmerHomePageDealerProfileAdapter(Farmer_Home.this, modelFarmerData);
                        farmer_list.setAdapter(homePageFarmerList);

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

    private void renewItems(View view) {
        this.view = view;
        FirebaseDatabase slide;
        DatabaseReference slideRef;
        slide  = FirebaseDatabase.getInstance();
        slideRef = slide.getReference("Slide_Images");
        Query query = slideRef.orderByChild("id").equalTo("farSlide");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds :snapshot.getChildren()){
                    String img1          =""+ds.child("Image1").getValue();
                    String img2          =""+ds.child("Image2").getValue();
                    String img3          =""+ds.child("Image3").getValue();
                    String img4          =""+ds.child("Image4").getValue();
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

    private void ShowPopUpForAddProduct() {
        ImageView img_view;
         final Spinner spinner,addPro,unit;
        myDialog.setContentView(R.layout.farmer_add_products);
        myDialog.setCanceledOnTouchOutside(false);
        img_view =(ImageView) myDialog.findViewById(R.id.close);
        img_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });

        spinner =(Spinner)myDialog.findViewById(R.id.spin1);
        addPro  =(Spinner)myDialog.findViewById(R.id.spin2);
        unit    =(Spinner)myDialog.findViewById(R.id.spin3);

        //Spinner Add Product Options
        final List<String> subOptions = new ArrayList<>();
        List<String> options = new ArrayList<>();
        options.add(0,"Select Product Type");
        options.add("Vegetable");
        options.add("Fruits");
        options.add("Grain");
        ArrayAdapter<String> dataAdapter;
        dataAdapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,options);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(parent.getItemAtPosition(position).equals("Choose Options"))
                {
                    Toast.makeText(Farmer_Home.this, "Please Choose Something", Toast.LENGTH_SHORT).show();
                }
                else if(parent.getItemAtPosition(position).equals("Vegetable"))
                {
                    subOptions.clear();
                    subOptions.add("Lady Finger");
                    subOptions.add("Potato");
                    subOptions.add("Tomato");
                    fillSpinner();
                }
                else if(parent.getItemAtPosition(position).equals("Fruits"))
                {
                    subOptions.clear();
                    subOptions.add("Apple");
                    subOptions.add("Banana");
                    subOptions.add("Orange");
                    fillSpinner();

                }
                else if(parent.getItemAtPosition(position).equals("Grain"))
                {
                    subOptions.clear();
                    subOptions.add("Wheat");
                    subOptions.add("Rice");
                    subOptions.add("Dal");
                    fillSpinner();
                }

                else{
                    String item = parent.getItemAtPosition(position).toString();

                    Toast.makeText(Farmer_Home.this, "You Select "+item, Toast.LENGTH_SHORT).show();
                }
            }

            public void fillSpinner() {
                ArrayAdapter<String> dataAdapter1;
                dataAdapter1 = new ArrayAdapter<String> (getApplicationContext(),android.R.layout.simple_spinner_item,subOptions);
                dataAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                addPro.setAdapter(dataAdapter1);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //End Options









        //End Options


        //Spinner Units
        List<String> units = new ArrayList<>();
        units.add(0,"Select Unit");
        units.add("Gm");
        units.add("Kg");
        units.add("Ton");
        units.add("Doz");
        ArrayAdapter<String> dataAdapter2;
        dataAdapter2 = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,units);
        dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        unit.setAdapter(dataAdapter2);
        unit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(parent.getItemAtPosition(position).equals("Select Unit"))
                {

                }

                else{
                    String item = parent.getItemAtPosition(position).toString();

                    Toast.makeText(Farmer_Home.this, "You Select "+item, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //End Options



        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }


    private void registerInternetCheckReceiver() {
        IntentFilter internetFilter = new IntentFilter();
        internetFilter.addAction("android.net.wifi.STATE_CHANGE");
        internetFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(broadcastReceiver, internetFilter);
    }



    public BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String status = getConnectivityStatusString(context);
            setSnackbarMessage(status,false);
        }
    };


    private void setSnackbarMessage(String status, boolean b) {
        String internetStatus = "";
        if (status.equalsIgnoreCase("Wifi enabled") || status.equalsIgnoreCase("Mobile data enabled")) {
            internetStatus = "Internet Connected";
        } else {
            internetStatus = "Lost Internet Connection";

            new SweetAlertDialog(Farmer_Home.this, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("Are you Sure!")
                    .setContentText("Want to logout")
                    .setCustomImage(R.drawable.icon1)
                    .setConfirmText("Yes! Logout")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {

                        }
                    })
                    .show();
        }
        snackbar = Snackbar
                .make(coordinatorLayout, internetStatus, Snackbar.LENGTH_LONG)
                .setAction("X", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        snackbar.dismiss();
                    }
                });
        // Changing message text color
        snackbar.setActionTextColor(Color.WHITE);
        // Changing action button text color
        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(com.google.android.material.R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);
        if (internetStatus.equalsIgnoreCase("Lost Internet Connection")) {
            if (internetConnected) {
                snackbar.show();
                internetConnected = false;
            }
        } else {
            if (!internetConnected) {
                internetConnected = true;
                snackbar.show();
            }
        }

    }



    public static int getConnectivityStatus(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (null != activeNetwork) {
            if(activeNetwork.getType() == TYPE_WIFI)
                return TYPE_WIFI;

            if(activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE)
                return TYPE_MOBILE;
        }
        return TYPE_NOT_CONNECTED;
    }


    private String getConnectivityStatusString(Context context) {
        int conn = getConnectivityStatus(context);
        String status = null;
        if (conn == TYPE_WIFI) {
            status = "Wifi enabled";
        } else if (conn == TYPE_MOBILE) {
            status = "Mobile data enabled";
        } else if (conn == TYPE_NOT_CONNECTED) {
            status = "Not connected to Internet";
        }
        return status;

    }





}