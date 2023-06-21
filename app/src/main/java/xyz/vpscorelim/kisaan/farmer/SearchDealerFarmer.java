package xyz.vpscorelim.kisaan.farmer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import xyz.vpscorelim.kisaan.R;
import xyz.vpscorelim.kisaan.adapter.HomePageFarmerList;
import xyz.vpscorelim.kisaan.adapter.ModelFarmerData;
import xyz.vpscorelim.kisaan.customer.Customer_Home;
import xyz.vpscorelim.kisaan.farmer.farmer_Model.GetDealerModel;
import xyz.vpscorelim.kisaan.farmer.farmer_adapter.NearByDealerListAdapter;

public class SearchDealerFarmer extends AppCompatActivity {


    public FirebaseAuth firebaseAuth;
    public FirebaseUser currentUser;
    public FirebaseDatabase firebaseDatabase;
    public DatabaseReference reference;
    String farmer_uid;


    NearByDealerListAdapter nearByDealerListAdapter;
    List<GetDealerModel> getDealerModel;
    RecyclerView dealer_list;
    RecyclerView.LayoutManager layoutManager;


    TextView city_m,state_m;
    Dialog myDialog;


    public static String farmer_city,ew;

    String value = "Amravati";


    ImageView filter_icon,back;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_dealer_farmer);
        loadLocale();
        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_ALWAYS, WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_ALWAYS);
        getSupportActionBar().hide();
        myDialog = new Dialog(this);

        city_m = findViewById(R.id.city_name);
        state_m = findViewById(R.id.state_name2);
        back= findViewById(R.id.close);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        //Firebase
        firebaseAuth= FirebaseAuth.getInstance();
        currentUser =firebaseAuth.getCurrentUser();
        firebaseDatabase =FirebaseDatabase.getInstance();
        reference =firebaseDatabase.getReference("Farmer_Data");
        farmer_uid =  currentUser.getUid();



        dealer_list = findViewById(R.id.dealer_list);
        dealer_list.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        dealer_list.setLayoutManager(layoutManager);
        dealer_list.setHasFixedSize(true);


        filter_icon = findViewById(R.id.filter);
        filter_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterPop();
            }
        });

        //---------------------/Get Current User Data/----------------------------//
        Query query = reference.orderByChild("uid").equalTo(farmer_uid);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds :snapshot.getChildren()){
                    String farmer_name        =""+ds.child("Farmer_Name").getValue();
                    String farmer_sub_area    =""+ds.child("Tehsil_Name").getValue();
                    String farmer_phone       =""+ds.child("phoneNumber").getValue();
                    String farmer_city        =""+ds.child("District_Name").getValue();
                    String farmer_state       =""+ds.child("State_Name").getValue();
                    String bulk_service       =""+ds.child("Bulk_avail").getValue();
                    String store              =""+ds.child("Store").getValue();
                    FarmerCommon.farmerCity = farmer_city;
                    FarmerCommon.farmerState = farmer_state;
                    FarmerCommon.farmerSub = farmer_sub_area;

                    city_m.setText(FarmerCommon.farmerCity);
                    state_m.setText(FarmerCommon.farmerState);




                    getAllDealer(farmer_city,farmer_sub_area,farmer_state,farmer_name,farmer_phone,store);






                    //Toast.makeText(SearchDealerFarmer.this, ""+FarmerCommon.city, Toast.LENGTH_SHORT).show();


                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        //---------------------//----------------------------//



        final BottomNavigationView bottomNavigationView = findViewById(R.id.farmer_bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.farmer_search);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.farmer_dashboard:
                        startActivity(new Intent(getApplicationContext(),Farmer_Home.class));
                        Animatoo.animateSlideRight(SearchDealerFarmer.this);
                        return true;
                    case R.id.farmer_profile:
                        startActivity(new Intent(getApplicationContext(),FarmerViewMyCart.class));
                        Animatoo.animateSlideLeft(SearchDealerFarmer.this);
                        return true;
                    case R.id.farmer_orders:
                        startActivity(new Intent(getApplicationContext(),Farmer_Order_Status.class));
                        Animatoo.animateSlideLeft(SearchDealerFarmer.this);
                        return true;
                    case R.id.farmer_profile_new:
                        startActivity(new Intent(getApplicationContext(),Farmer_Profile.class));
                        Animatoo.animateSlideLeft(SearchDealerFarmer.this);
                        return true;
                    case R.id.farmer_search:
                        return true;
                }
                return false;
            }
        });





    }


    public  void loadLocale(){
        SharedPreferences preferences = getSharedPreferences("Setting", Activity.MODE_PRIVATE);
        String lamguage = preferences.getString("My_Language","");
        Locale locale =new Locale(lamguage);
        Locale.setDefault(locale);

    }

    private void getAllDealer(String farmer_city,String farmer_sub_area,String farmer_state,String farmer_name,String farmer_phone,String store) {


        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Dealer_Data");
        ref.orderByChild("districtName").equalTo(FarmerCommon.farmerCity).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        getDealerModel = new ArrayList<>();
                        for (DataSnapshot pvostsnap : snapshot.getChildren()) {
                            GetDealerModel post = pvostsnap.getValue(GetDealerModel.class);

                                getDealerModel.add(post);


                        }
                        nearByDealerListAdapter = new NearByDealerListAdapter(SearchDealerFarmer.this, getDealerModel);
                        dealer_list.setAdapter(nearByDealerListAdapter);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }


    private void filterPop() {


        MaterialButton update;
        EditText state1,city_name,sub_area_name;

        myDialog.setContentView(R.layout.serch_filter);
        myDialog.setCanceledOnTouchOutside(true);


        state1        =(EditText)myDialog.findViewById(R.id.state);
        city_name    =(EditText)myDialog.findViewById(R.id.city);
        sub_area_name=(EditText)myDialog.findViewById(R.id.sub);


        state1.setText(FarmerCommon.farmerState);
        city_name.setText(FarmerCommon.farmerCity);
        sub_area_name.setText(FarmerCommon.farmerSub);

        update  =(MaterialButton)myDialog.findViewById(R.id.update_order);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String state = state1.getText().toString();
                String city = city_name.getText().toString();
                String sub = sub_area_name.getText().toString();

                FarmerCommon.farmerCity = city;
                FarmerCommon.farmerState = state;
                FarmerCommon.farmerSub = sub;

               update(state,city,sub);

            }
        });


        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();

    }


    private void update(String state, String city, String sub) {
        myDialog.dismiss();
        //Toast.makeText(this, ""+state_name, Toast.LENGTH_SHORT).show();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Dealer_Data");
        ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        getDealerModel = new ArrayList<>();
                        for (DataSnapshot pvostsnap : snapshot.getChildren()) {
                            GetDealerModel post = pvostsnap.getValue(GetDealerModel.class);
                            if(post.getStateName().toLowerCase().contains(state.toLowerCase()) && post.getDistrictName().toLowerCase().contains(city.toLowerCase()) &&
                                    post.getSubArea().toLowerCase().contains(sub.toLowerCase())){
                                getDealerModel.add(post);
                            }

                        }
                        nearByDealerListAdapter = new NearByDealerListAdapter(SearchDealerFarmer.this, getDealerModel);
                        dealer_list.setAdapter(nearByDealerListAdapter);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });


    }
}