package xyz.vpscorelim.kisaan.dealer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

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
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;
import java.util.List;

import xyz.vpscorelim.kisaan.R;
import xyz.vpscorelim.kisaan.customer.AdapterCardProductView;
import xyz.vpscorelim.kisaan.customer.CustomerCommon;
import xyz.vpscorelim.kisaan.customer.SearchOne;
import xyz.vpscorelim.kisaan.dealer.DealerAdaper.AdapterCardProductViewDealer;
import xyz.vpscorelim.kisaan.farmer.Farmer_Product_Data;

public class SearchTwo extends AppCompatActivity {


    MaterialSearchView searchView;
    public RadioGroup radioGroup;

    RadioButton showAll;

    //Firebase Variable
    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference ;


    TextView city_label,state_label;


    View myView;

    AdapterCardProductViewDealer adapterCardProductView;
    List<Farmer_Product_Data> farmerProductData;


    //RecyclerView
    RecyclerView product_list;
    LinearLayoutManager mLayoutManager;


    Dialog myDialog;
    ImageView filter_icon,back;



    String filter="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_two);
        radioGroup = findViewById(R.id.radio);
        myDialog = new Dialog(this);

        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_ALWAYS, WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_ALWAYS);
        getSupportActionBar().hide();



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




        filter_icon = findViewById(R.id.filter);
        filter_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterPop();
            }
        });


        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Search Product");
        getSupportActionBar().setIcon(R.drawable.icon1);
        searchView = findViewById(R.id.search_view);
        searchView.setHint("Search By Product");

        city_label = findViewById(R.id.city_name);
        state_label= findViewById(R.id.state_name2);

        mAuth= FirebaseAuth.getInstance();
        currentUser =mAuth.getCurrentUser();
        String currentUid = currentUser.getUid();
        firebaseDatabase =FirebaseDatabase.getInstance();
        databaseReference =firebaseDatabase.getReference("Dealer_Data");

        //onRadioButtonClicked(myView);
        showAll = findViewById(R.id.show_all);
        showAll.setChecked(true);

        //Get Current User data
        Query query = databaseReference.orderByChild("uid").equalTo(currentUid);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds :snapshot.getChildren()){
                    String customer_name_f      =""+ds.child("dealerName").getValue();
                    String customer_sub_area    =""+ds.child("subArea").getValue();
                    String customer_phone_f     =""+ds.child("phoneNumber").getValue();
                    String customer_city        =""+ds.child("districtName").getValue();
                    String customer_state       =""+ds.child("stateName").getValue();

                    DealerCommon.dealerCity  = customer_city;
                    DealerCommon.dealerState = customer_state;
                    DealerCommon.dealerTehsil   = customer_sub_area;

                    city_label.setText(DealerCommon.dealerCity);
                    state_label.setText(DealerCommon.dealerState);

                    getAllProducts(filter);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });



        //RecyclerView
        product_list = findViewById(R.id.product_list);
        mLayoutManager     = new LinearLayoutManager(this);
        product_list.setLayoutManager(
                new LinearLayoutManager(this));
        product_list.setHasFixedSize(true);
        product_list.setLayoutManager(mLayoutManager);




    }



    private void filterPop() {
        MaterialButton update;
        EditText state1,city_name,sub_area_name;

        myDialog.setContentView(R.layout.serch_filter);
        myDialog.setCanceledOnTouchOutside(true);


        state1        =(EditText)myDialog.findViewById(R.id.state);
        city_name    =(EditText)myDialog.findViewById(R.id.city);
        sub_area_name=(EditText)myDialog.findViewById(R.id.sub);

        state1.setText(DealerCommon.dealerState);
        city_name.setText(DealerCommon.dealerCity);
        sub_area_name.setText(DealerCommon.dealerTehsil);


        update  =(MaterialButton)myDialog.findViewById(R.id.update_order);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String state = state1.getText().toString();
                DealerCommon.dealerCity = city_name.getText().toString();
                String sub = sub_area_name.getText().toString();

                getAllProducts(filter);


                myDialog.dismiss();

                city_label.setText(DealerCommon.dealerCity);
                state_label.setText(DealerCommon.dealerState);
            }
        });


        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
    }

    private void getAllProducts(String filter) {
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference("Farmer_Products");
        Query query = ref.orderByChild("FarmerCity").equalTo(DealerCommon.dealerCity);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                farmerProductData = new ArrayList<>();
                for (DataSnapshot pvostsnap : dataSnapshot.getChildren()) {
                    Farmer_Product_Data post = pvostsnap.getValue(Farmer_Product_Data.class);

                    if (filter.equals("")){
                        farmerProductData.add(post);
                    }if (post.getProduct_Category().equals(filter)){
                        farmerProductData.add(post);
                    }
                }
                adapterCardProductView = new AdapterCardProductViewDealer(SearchTwo.this, farmerProductData);
                product_list.setAdapter(adapterCardProductView);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(SearchTwo.this, ""+databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_view,menu);
        MenuItem item = menu.findItem(R.id.searchView);
        searchView.setMenuItem(item);

        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                if(!TextUtils.isEmpty(query.trim())){
                    searchUser(query,filter);
                }else {
                    getAllProducts(filter);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                if(!TextUtils.isEmpty(query.trim())){

                    searchUser(query,filter);


                }else {
                    getAllProducts(filter);

                }

                return false;
            }
        });



        return true;
    }

    private void searchUser(String query,String filter) {
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference("Farmer_Products");
        Query query1 = ref.orderByChild("FarmerCity").equalTo(DealerCommon.dealerCity);
        query1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                farmerProductData = new ArrayList<>();
                for (DataSnapshot pvostsnap : dataSnapshot.getChildren()) {
                    Farmer_Product_Data post = pvostsnap.getValue(Farmer_Product_Data.class);


                    if(post.getProduct_Name().toLowerCase().contains(query.toLowerCase()) || post.getProduct_Name().toLowerCase().contains(query.toLowerCase())){
                        if (filter.equals("")){
                            farmerProductData.add(post);
                        }if (post.getProduct_Category().equals(filter)){
                            farmerProductData.add(post);
                        }
                    }


                }
                adapterCardProductView = new AdapterCardProductViewDealer(SearchTwo.this, farmerProductData);
                product_list.setAdapter(adapterCardProductView);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(SearchTwo.this, ""+databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void onRadioButtonClicked(View myView) {

        boolean checked = ((RadioButton) myView).isChecked();

        // Check which radio button was clicked
        switch (myView.getId()) {
            case R.id.show_all:
                if (checked)
                    filter="";
                getAllProducts(filter);
                break;
            case R.id.vegetables:
                if (checked)
                    filter = "Vegetable";
                getAllProducts(filter);
                break;
            case R.id.fruits:
                if (checked)
                    filter ="Fruits";
                getAllProducts(filter);
                break;
            case R.id.grains:
                if (checked)
                    filter ="Grains";
                getAllProducts(filter);
                break;


        }

    }


}