package xyz.vpscorelim.kisaan.farmer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import xyz.vpscorelim.kisaan.adapter.Adapter_Farmer_His_products;
import xyz.vpscorelim.kisaan.customer.His_Farmer_Profile;
import xyz.vpscorelim.kisaan.dealer.DealerModel.DealerProductListModel;
import xyz.vpscorelim.kisaan.dealer.DealerMyProduct;
import xyz.vpscorelim.kisaan.farmer.farmer_adapter.FarDealerProductsListAdapter;

public class FarmerDealerProfile extends AppCompatActivity {

    //Ui Component
    TextView dealer_store_name,dealer_name,dealer_store_loc,dealer_home_delivery,shop_close,shop_open;
    EditText search_field;
    ImageButton search_btn;
    MaterialButton call_now;
    RecyclerView product_list;
    LinearLayoutManager mLayoutManager;


    String filter="show_all";
    String mob_no;
    String dealer_uid;
    String txt;


    //Firebase Variable
    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference ;
    ProgressDialog pd;
    public RadioGroup radioGroup;


    FarDealerProductsListAdapter farDealerProductsListAdapter;
    List<DealerProductListModel> productListModels;



    ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.farmer_dealer_profile);
        loadLocale();
        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_ALWAYS, WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_ALWAYS);
        getSupportActionBar().hide();


        back= findViewById(R.id.close);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        dealer_store_name = findViewById(R.id.dealer_name);
        dealer_name = findViewById(R.id.bulk);
        dealer_store_loc = findViewById(R.id.login_desc);
        dealer_home_delivery = findViewById(R.id.home_del);
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
        databaseReference = firebaseDatabase.getReference("Farmer_Data");

        //Get Phone Number
        mob_no = getIntent().getStringExtra("phoneNumber");
//        call_now.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i = new Intent(Intent.ACTION_DIAL);
//                String p = "tel:"+mob_no;
//                i.setData(Uri.parse(p));
//                startActivity(i);
//            }
//        });


        //RecyclerView
        product_list = findViewById(R.id.product_list);
        mLayoutManager     = new LinearLayoutManager(this);
        product_list.setLayoutManager(
                new LinearLayoutManager(this));
        product_list.setHasFixedSize(true);
        product_list.setLayoutManager(mLayoutManager);




        DatabaseReference datref1 = firebaseDatabase.getReference("Dealer_Data");
        Query query = datref1.orderByChild("phoneNumber").equalTo(mob_no);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot ds :snapshot.getChildren()){

                    //get Value
                    final String txt_dealer_name             =""+ds.child("dealerName").getValue();
                    final String txt_dealer_shop_name        =""+ds.child("storeName").getValue();
                    final String txt_dealer_shop_add         =""+ds.child("shopLocation").getValue();
                    final String txt_dealer_shop_home        =""+ds.child("Home_Delivery").getValue();
                    final String txt_shop_close              =""+ds.child("Store").getValue();
                    final String dealer_uid                  =""+ds.child("uid").getValue();

                    dealer_store_name.setText(txt_dealer_shop_name);
                    dealer_name.setText(txt_dealer_name);
                    dealer_store_loc.setText(txt_dealer_shop_add);


                    if(txt_dealer_shop_home.equals("Yes")){

                        dealer_home_delivery.setText(R.string.home_del_ser_avail);
                        dealer_home_delivery.setTextColor(Color.parseColor("#45CE30"));

                    }else {

                        dealer_home_delivery.setText(R.string.home_del_ser_not_avail);
                        dealer_home_delivery.setTextColor(Color.RED);

                    }

                    if(txt_shop_close.equals("open")){

                       shop_open.setVisibility(View.VISIBLE);
                        shop_close.setVisibility(View.GONE);

                    }else {

                        shop_close.setVisibility(View.VISIBLE);
                        shop_open.setVisibility(View.GONE);

                    }






                    DatabaseReference ref= FirebaseDatabase.getInstance().getReference("Dealer_Products");
                    Query query = ref.orderByChild("uid").equalTo(dealer_uid);
                    query.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            productListModels = new ArrayList<>();
                            for (DataSnapshot pvostsnap : dataSnapshot.getChildren()) {
                                DealerProductListModel post = pvostsnap.getValue(DealerProductListModel.class);
                                //farmerProductData.clear();
                                productListModels.add(post);


                            }
                            farDealerProductsListAdapter = new FarDealerProductsListAdapter(FarmerDealerProfile.this, productListModels);
                            product_list.setAdapter(farDealerProductsListAdapter);


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                            Toast.makeText(FarmerDealerProfile.this, ""+databaseError.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    });


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(FarmerDealerProfile.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });







    }


    public  void loadLocale(){
        SharedPreferences preferences = getSharedPreferences("Setting", Activity.MODE_PRIVATE);
        String lamguage = preferences.getString("My_Language","");
        Locale locale =new Locale(lamguage);
        Locale.setDefault(locale);

    }

    private void search_item() {
        String txt_item = search_field.getText().toString().trim();
        DatabaseReference datref1 = firebaseDatabase.getReference("Dealer_Data");
        Query query = datref1.orderByChild("phoneNumber").equalTo(mob_no);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot ds :snapshot.getChildren()){
                   String dealer_uid              =""+ds.child("uid").getValue();
                    DatabaseReference ref= FirebaseDatabase.getInstance().getReference("Dealer_Products");
                    Query query = ref.orderByChild("uid").equalTo(dealer_uid);
                    query.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            productListModels = new ArrayList<>();
                            for (DataSnapshot pvostsnap : dataSnapshot.getChildren()) {
                                DealerProductListModel post = pvostsnap.getValue(DealerProductListModel.class);
                                //farmerProductData.clear();
                                if(post.getProduct_Name().toLowerCase().contains(txt_item.toLowerCase()) || post.getProduct_Category().toLowerCase().contains(txt_item.toLowerCase())){
                                    productListModels.add(post);

                                }



                            }
                            farDealerProductsListAdapter = new FarDealerProductsListAdapter(FarmerDealerProfile.this, productListModels);
                            product_list.setAdapter(farDealerProductsListAdapter);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                            Toast.makeText(FarmerDealerProfile.this, ""+databaseError.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(FarmerDealerProfile.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void onRadioButtonClicked(View view) {

        DatabaseReference datref1 = firebaseDatabase.getReference("Dealer_Data");
        Query query = datref1.orderByChild("phoneNumber").equalTo(mob_no);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot ds :snapshot.getChildren()){


                    String dealer_uid              =""+ds.child("uid").getValue();


                    boolean checked = ((RadioButton) view).isChecked();


                    // Check which radio button was clicked

                    switch (view.getId()) {
                        case R.id.show_all:
                            if (checked)
                                filter="show_all";

                            click_delaer_data_all(dealer_uid,filter);

                            break;
                        case R.id.seed:
                            if (checked)

                                filter = "Seed";
                            click_dealer_data(dealer_uid,filter);

                            break;
                        case R.id.pesticide:
                            if (checked)

                                filter ="Pesticide";
                            click_dealer_data(dealer_uid,filter);


                            break;
                        case R.id.fertilizer:
                            if (checked)

                                filter ="Fertilizer";
                            click_dealer_data(dealer_uid,filter);


                            break;
                        case R.id.farm_machinery:
                            if (checked)

                                filter ="Farm machinery";
                            click_dealer_data(dealer_uid,filter);


                            break;


                    }





                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(FarmerDealerProfile.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void click_delaer_data_all(String dealer_uid, String filter) {

        DatabaseReference ref= FirebaseDatabase.getInstance().getReference("Dealer_Products");
        Query query = ref.orderByChild("uid").equalTo(dealer_uid);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                productListModels = new ArrayList<>();
                for (DataSnapshot pvostsnap : dataSnapshot.getChildren()) {
                    DealerProductListModel post = pvostsnap.getValue(DealerProductListModel.class);
                    //farmerProductData.clear();
                    productListModels.add(post);


                }
                farDealerProductsListAdapter = new FarDealerProductsListAdapter(FarmerDealerProfile.this, productListModels);
                product_list.setAdapter(farDealerProductsListAdapter);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(FarmerDealerProfile.this, ""+databaseError.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void click_dealer_data(String dealer_uid, String filter) {


        Toast.makeText(this, ""+dealer_uid, Toast.LENGTH_SHORT).show();
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference("Dealer_Products");
        Query query = ref.orderByChild("uid").equalTo(dealer_uid);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                productListModels = new ArrayList<>();
                for (DataSnapshot pvostsnap : dataSnapshot.getChildren()) {
                    DealerProductListModel post = pvostsnap.getValue(DealerProductListModel.class);
                    assert post != null;
                    if(post.getProduct_Category().equals(filter)){
                        //farmerProductData.clear();
                        productListModels.add(post);
                    }

                }
                farDealerProductsListAdapter = new FarDealerProductsListAdapter(FarmerDealerProfile.this, productListModels);
                product_list.setAdapter(farDealerProductsListAdapter);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(FarmerDealerProfile.this, ""+databaseError.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

    }
}