package xyz.vpscorelim.kisaan.customer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import xyz.vpscorelim.kisaan.KisanLocalBazar;
import xyz.vpscorelim.kisaan.R;
import xyz.vpscorelim.kisaan.adapter.Adapter_Farmer_His_products;
import xyz.vpscorelim.kisaan.farmer.Adapter_Farmer_product_list;
import xyz.vpscorelim.kisaan.farmer.Farmer_Product_Data;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class His_Farmer_Profile extends AppCompatActivity {

    //Firebase Variable
    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference ;
    ProgressDialog pd;
    public RadioGroup radioGroup;

    String filter="show_all";


    Adapter_Farmer_His_products adapterFarmerHisProducts;
    List<Farmer_Product_Data> farmerProductData;


    //Ui Component
    TextView name_farmer,bulk_service,selling_location,shop_close,shop_open,home_del;



    //Ads Component
    String mob_no;
    TextView ad_title1,ad_description;


    //RecyclerView
    RecyclerView product_list;
    LinearLayoutManager mLayoutManager;

    String farmer_uid;
    String txt;

    EditText search_field;
    ImageButton search_btn;

    MaterialButton call_now;

    KisanLocalBazar kisanLocalBazar;


    ImageView back;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.his_farmer_profile);
        kisanLocalBazar = KisanLocalBazar.getzInstance();
        loadLocale();
        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_ALWAYS, WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_ALWAYS);
        getSupportActionBar().hide();
        radioGroup = findViewById(R.id.radio);


        back= findViewById(R.id.close);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        RadioButton show_all;
        show_all = findViewById(R.id.show_all);
        show_all.isChecked();
        //Ui Init
        name_farmer         = findViewById(R.id.farmer_name);
        bulk_service        = findViewById(R.id.bulk);
        selling_location    = findViewById(R.id.login_desc);
        call_now = findViewById(R.id.call_now);
        home_del = findViewById(R.id.home_del);


        shop_close =  findViewById(R.id.close_shop);
        shop_open  = findViewById(R.id.open);

        search_field =findViewById(R.id.search_field);
        search_btn = findViewById(R.id.search_btn);

        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search_item();
            }
        });


        //Firebase Init
        mAuth             = FirebaseAuth.getInstance();
        currentUser       = mAuth.getCurrentUser();
        firebaseDatabase  = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Customer_Data");



        //Get Phone Number
        mob_no = getIntent().getStringExtra("phoneNumber");
        call_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_DIAL);
                String p = "tel:"+mob_no;
                i.setData(Uri.parse(p));
                startActivity(i);
            }
        });




        //RecyclerView
        product_list = findViewById(R.id.product_list);
        mLayoutManager     = new LinearLayoutManager(this);
        product_list.setLayoutManager(
                new LinearLayoutManager(this));
        product_list.setHasFixedSize(true);
        product_list.setLayoutManager(mLayoutManager);



        DatabaseReference datref1 = firebaseDatabase.getReference("Farmer_Data");
        Query query = datref1.orderByChild("phoneNumber").equalTo(mob_no);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot ds :snapshot.getChildren()){

                    //get Value
                    final String farmer_name                =""+ds.child("Farmer_Name").getValue();
                    final String farmer_phoneNumber         =""+ds.child("phoneNumber").getValue();
                    farmer_uid                 =""+ds.child("uid").getValue();
                    final String farmer_selling_location    =""+ds.child("Selling_Location").getValue();
                    final String farmer_Bulk_avail          =""+ds.child("Bulk_avail").getValue();
                    final String farmer_home_delivery       =""+ds.child("Home_Delivery").getValue();
                    final String farmer_shop                =""+ds.child("Store").getValue();




                    DatabaseReference ref= FirebaseDatabase.getInstance().getReference("Farmer_Products");
                    Query query = ref.orderByChild("uid").equalTo(farmer_uid);
                    query.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            farmerProductData = new ArrayList<>();
                            for (DataSnapshot pvostsnap : dataSnapshot.getChildren()) {
                                Farmer_Product_Data post = pvostsnap.getValue(Farmer_Product_Data.class);
                                //farmerProductData.clear();
                                farmerProductData.add(post);


                            }
                            adapterFarmerHisProducts = new Adapter_Farmer_His_products(His_Farmer_Profile.this, farmerProductData);
                            product_list.setAdapter(adapterFarmerHisProducts);


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                            Toast.makeText(His_Farmer_Profile.this, ""+databaseError.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    });


                    txt =farmer_uid;

                    name_farmer.setText(farmer_name);
                    selling_location.setText(farmer_selling_location);


                    if(farmer_home_delivery.equals("Yes")){

                        home_del.setText(R.string.home_del_ser_avail);
                        home_del.setTextColor(Color.parseColor("#45CE30"));

                    }else {

                        home_del.setText(R.string.home_del_ser_not_avail);
                        home_del.setTextColor(Color.RED);

                    }


                    if(farmer_shop.equals("open")){

                        shop_open.setVisibility(View.VISIBLE);
                        shop_close.setVisibility(View.GONE);

                    }else {

                        shop_close.setVisibility(View.VISIBLE);
                        shop_open.setVisibility(View.GONE);

                    }



                    switch (farmer_Bulk_avail) {
                        case "Yes":
                            bulk_service.setText(R.string.bulk_avail);
                            break;
                        case "No":
                            bulk_service.setText(R.string.bulk_not_avail);
                            bulk_service.setTextColor(Color.RED);
                            break;
                        case "null":
                            bulk_service.setText(R.string.bulk_availNot);
                            break;
                    }







                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(His_Farmer_Profile.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });





        //loadFarmerProduct();
    }

    private void search_item() {
        String txt_item = search_field.getText().toString().trim();
        DatabaseReference datref1 = firebaseDatabase.getReference("Farmer_Data");
        Query query = datref1.orderByChild("phoneNumber").equalTo(mob_no);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot ds :snapshot.getChildren()){
                    farmer_uid                 =""+ds.child("uid").getValue();
                    DatabaseReference ref= FirebaseDatabase.getInstance().getReference("Farmer_Products");
                    Query query = ref.orderByChild("uid").equalTo(farmer_uid);
                    query.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            farmerProductData = new ArrayList<>();
                            for (DataSnapshot pvostsnap : dataSnapshot.getChildren()) {
                                Farmer_Product_Data post = pvostsnap.getValue(Farmer_Product_Data.class);
                                //farmerProductData.clear();
                                if(post.getProduct_Name().toLowerCase().contains(txt_item.toLowerCase()) || post.getProduct_Category().toLowerCase().contains(txt_item.toLowerCase())){
                                    farmerProductData.add(post);

                                }



                            }
                            adapterFarmerHisProducts = new Adapter_Farmer_His_products(His_Farmer_Profile.this, farmerProductData);
                            product_list.setAdapter(adapterFarmerHisProducts);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                            Toast.makeText(His_Farmer_Profile.this, ""+databaseError.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(His_Farmer_Profile.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void loadFarmerProduct() {
        //Toast.makeText(this, ""+farmer_uid, Toast.LENGTH_SHORT).show();
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference("Farmer_Products");
        Query query = ref.orderByChild("uid").equalTo(farmer_uid);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                farmerProductData = new ArrayList<>();
                for (DataSnapshot pvostsnap : dataSnapshot.getChildren()) {
                    Farmer_Product_Data post = pvostsnap.getValue(Farmer_Product_Data.class);
                    farmerProductData.add(post);
                }
                adapterFarmerHisProducts = new Adapter_Farmer_His_products(His_Farmer_Profile.this, farmerProductData);
                product_list.setAdapter(adapterFarmerHisProducts);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(His_Farmer_Profile.this, ""+databaseError.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });




    }

    public void onRadioButtonClicked(View view) {


        DatabaseReference datref1 = firebaseDatabase.getReference("Farmer_Data");
        Query query = datref1.orderByChild("phoneNumber").equalTo(mob_no);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds :snapshot.getChildren()){
                    farmer_uid                 =""+ds.child("uid").getValue();
                    boolean checked = ((RadioButton) view).isChecked();
                    // Check which radio button was clicked
                    switch (view.getId()) {
                        case R.id.show_all:
                            if (checked)
                                filter="show_all";
                            click_farmer_data_all(farmer_uid,filter);
                            break;
                        case R.id.vegetables:
                            if (checked)
                                filter = "Vegetable";
                            click_farmer_data(farmer_uid,filter);
                            break;
                        case R.id.fruits:
                            if (checked)
                                filter ="Fruits";
                            click_farmer_data(farmer_uid,filter);
                            break;
                        case R.id.grains:
                            if (checked)
                                filter ="Grains";
                            click_farmer_data(farmer_uid,filter);


                                break;


                    }





                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(His_Farmer_Profile.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });


    }

    private void click_farmer_data_all(String farmer_uid, String filter) {

        Toast.makeText(this, ""+farmer_uid, Toast.LENGTH_SHORT).show();
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference("Farmer_Products");
        Query query = ref.orderByChild("uid").equalTo(farmer_uid);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                farmerProductData = new ArrayList<>();
                for (DataSnapshot pvostsnap : dataSnapshot.getChildren()) {
                    Farmer_Product_Data post = pvostsnap.getValue(Farmer_Product_Data.class);
                        //farmerProductData.clear();
                        farmerProductData.add(post);


                }
                adapterFarmerHisProducts = new Adapter_Farmer_His_products(His_Farmer_Profile.this, farmerProductData);
                product_list.setAdapter(adapterFarmerHisProducts);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(His_Farmer_Profile.this, ""+databaseError.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

    }


    public void click_farmer_data(String farmer_uid,String filter) {


        Toast.makeText(this, ""+farmer_uid, Toast.LENGTH_SHORT).show();
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference("Farmer_Products");
        Query query = ref.orderByChild("uid").equalTo(farmer_uid);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                farmerProductData = new ArrayList<>();
                for (DataSnapshot pvostsnap : dataSnapshot.getChildren()) {
                    Farmer_Product_Data post = pvostsnap.getValue(Farmer_Product_Data.class);
                    assert post != null;
                    if(post.getProduct_Category().equals(filter)){
                        //farmerProductData.clear();
                        farmerProductData.add(post);
                    }

                }
                adapterFarmerHisProducts = new Adapter_Farmer_His_products(His_Farmer_Profile.this, farmerProductData);
                product_list.setAdapter(adapterFarmerHisProducts);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(His_Farmer_Profile.this, ""+databaseError.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });




    }


    public  void loadLocale(){
        SharedPreferences preferences = getSharedPreferences("Setting", Activity.MODE_PRIVATE);
        String lamguage = preferences.getString("My_Language","");
        Locale locale =new Locale(lamguage);
        Locale.setDefault(locale);

    }
}