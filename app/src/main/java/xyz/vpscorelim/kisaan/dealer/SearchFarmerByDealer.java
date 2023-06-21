package xyz.vpscorelim.kisaan.dealer;

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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import xyz.vpscorelim.kisaan.R;
import xyz.vpscorelim.kisaan.adapter.ModelFarmerData;
import xyz.vpscorelim.kisaan.customer.GetFarmer;
import xyz.vpscorelim.kisaan.dealer.DealerAdaper.DealerGetNearByFarmerAdapter;
import xyz.vpscorelim.kisaan.farmer.FarmerCommon;
import xyz.vpscorelim.kisaan.farmer.SearchDealerFarmer;
import xyz.vpscorelim.kisaan.farmer.farmer_Model.GetDealerModel;
import xyz.vpscorelim.kisaan.farmer.farmer_adapter.NearByDealerListAdapter;

public class SearchFarmerByDealer extends AppCompatActivity {


    public FirebaseAuth firebaseAuth;
    public FirebaseUser currentUser;
    public FirebaseDatabase firebaseDatabase;
    public DatabaseReference reference;
    String farmer_uid;

    DealerGetNearByFarmerAdapter nearByDealerListAdapter;
    List<ModelFarmerData> modelFarmerData;
    RecyclerView dealer_list;
    RecyclerView.LayoutManager layoutManager;


    Dialog myDialog;
    public static String farmer_city,ew;
    String value = "Amravati";
    ImageView filter_icon,back;

    TextView city_m,state_m;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_farmer_by_dealer);
        loadLocale();
        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_ALWAYS, WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_ALWAYS);
        getSupportActionBar().hide();


        city_m = findViewById(R.id.city_name);
        state_m = findViewById(R.id.state_name2);

        back= findViewById(R.id.close);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        myDialog = new Dialog(this);
        firebaseAuth= FirebaseAuth.getInstance();
        currentUser =firebaseAuth.getCurrentUser();
        firebaseDatabase =FirebaseDatabase.getInstance();
        reference =firebaseDatabase.getReference("Dealer_Data");
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
                    String farmer_name        =""+ds.child("dealerName").getValue();
                    String farmer_sub_area    =""+ds.child("subArea").getValue();
                    String farmer_phone       =""+ds.child("phoneNumber").getValue();
                    String farmer_city        =""+ds.child("districtName").getValue();
                    String farmer_state       =""+ds.child("stateName").getValue();
                    String bulk_service       =""+ds.child("Bulk_avail").getValue();
                    String store              =""+ds.child("Store").getValue();
                    FarmerCommon.city = farmer_city;



                    city_m.setText(farmer_city);
                    state_m.setText(farmer_state);
                    getAllFarmer(farmer_city,farmer_sub_area,farmer_state,farmer_name,farmer_phone,store);






                    //Toast.makeText(SearchDealerFarmer.this, ""+FarmerCommon.city, Toast.LENGTH_SHORT).show();


                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        //---------------------//----------------------------//


        BottomNavigationView bottomNavigationView = findViewById(R.id.dealer_bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.dealer_search);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.dealer_dashboard:
                        startActivity(new Intent(getApplicationContext(), Dealer_Home.class));
                        overridePendingTransition(0,0);
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
                        return true;
                    case R.id.dealer_profile2:
                        startActivity(new Intent(getApplicationContext(), Dealer_Profile.class));
                        overridePendingTransition(0,0);
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

    private void getAllFarmer(String farmer_city, String farmer_sub_area, String farmer_state, String farmer_name, String farmer_phone, String store) {


        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Farmer_Data");
        ref.orderByChild("stateName").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                modelFarmerData = new ArrayList<>();
                for (DataSnapshot pvostsnap : snapshot.getChildren()) {
                    ModelFarmerData post = pvostsnap.getValue(ModelFarmerData.class);

                    modelFarmerData.add(post);


                }
                nearByDealerListAdapter = new DealerGetNearByFarmerAdapter(SearchFarmerByDealer.this, modelFarmerData);
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








        update  =(MaterialButton)myDialog.findViewById(R.id.update_order);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String state = state1.getText().toString();
                String city = city_name.getText().toString();
                String sub = sub_area_name.getText().toString();


                update(state,city,sub);

            }
        });


        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();

    }


    private void update(String state, String city, String sub) {
        myDialog.dismiss();
        //Toast.makeText(this, ""+state_name, Toast.LENGTH_SHORT).show();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Farmer_Data");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                modelFarmerData = new ArrayList<>();
                for (DataSnapshot pvostsnap : snapshot.getChildren()) {
                    ModelFarmerData post = pvostsnap.getValue(ModelFarmerData.class);
                    if(post.getState_Name().toLowerCase().contains(state.toLowerCase()) && post.getDistrict_Name().toLowerCase().contains(city.toLowerCase()) &&
                            post.getTehsil_Name().toLowerCase().contains(sub.toLowerCase())){
                        modelFarmerData.add(post);
                    }

                }
                nearByDealerListAdapter = new DealerGetNearByFarmerAdapter(SearchFarmerByDealer.this, modelFarmerData);
                dealer_list.setAdapter(nearByDealerListAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });


    }

}