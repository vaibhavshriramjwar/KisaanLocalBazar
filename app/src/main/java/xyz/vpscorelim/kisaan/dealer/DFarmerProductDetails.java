package xyz.vpscorelim.kisaan.dealer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
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

import xyz.vpscorelim.kisaan.Database.Database;
import xyz.vpscorelim.kisaan.Database.DealerDatabase;
import xyz.vpscorelim.kisaan.R;
import xyz.vpscorelim.kisaan.adapter.AdapterHorizontalRecycler;
import xyz.vpscorelim.kisaan.customer.CustomerOrderModel;
import xyz.vpscorelim.kisaan.customer.Vegetable_Details;
import xyz.vpscorelim.kisaan.customer.ViewMyCart;
import xyz.vpscorelim.kisaan.dealer.DealerModel.DealerOrderListModel;
import xyz.vpscorelim.kisaan.farmer.Farmer_Product_Data;

public class DFarmerProductDetails extends AppCompatActivity {

    //UI Component
    TextView categories,product_name,product_price,product_price_unit,rs,add_price;
    MaterialButton add_to_cart,view_cart;
    RecyclerView product_list;
    ImageView veg_img,sub,add;
    String productName,productImage,productPrice,productUnit,productId,productCategory,far_id;
    String phone;
    ElegantNumberButton numberButton;


    FirebaseAuth mAuth;
    FirebaseUser currentUser;


    //Firebase Variable
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference ;
    ProgressDialog pd;


    AdapterHorizontalRecycler adapterFarmerHisProducts;
    List<Farmer_Product_Data> farmerProductData;

    //Ads Component
    String mob_no;
    TextView ad_title1,ad_description;

    LinearLayoutManager mLayoutManager;

    String farmer_uid;
    String txt;

    ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dealer_farmer_product_details);
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

        //RecyclerView
        product_list = findViewById(R.id.product_list);
        mLayoutManager     = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        product_list.setLayoutManager(
                new LinearLayoutManager(this));
        product_list.setHasFixedSize(true);
        product_list.setLayoutManager(mLayoutManager);

        firebaseDatabase  = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Dealer_Data");


        mAuth             = FirebaseAuth.getInstance();
        currentUser       = mAuth.getCurrentUser();

        final String custUid = currentUser.getUid();
        phone                = currentUser.getPhoneNumber();



        //Init
        categories          = findViewById(R.id.category);
        product_name        = findViewById(R.id.product_name);
        product_price       = findViewById(R.id.price);
        product_price_unit  = findViewById(R.id.product_price_unit);
        veg_img             = findViewById(R.id.veg_img);
        //sub                 = findViewById(R.id.sub);
        add                 = findViewById(R.id.add);
        numberButton        = findViewById(R.id.number);
        add_to_cart         = findViewById(R.id.add_cart);
        view_cart           =findViewById(R.id.view_cart);
        view_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DFarmerProductDetails.this, DealerViewMyCart.class);
                startActivity(intent);
            }
        });




        //get Intent Value
        productName     = getIntent().getStringExtra("vegetable_name");
        productImage    = getIntent().getStringExtra("vegetable_img");
        productPrice    = getIntent().getStringExtra("vegetable_price");
        productUnit     = getIntent().getStringExtra("vegetable_unit");
        productId       = getIntent().getStringExtra("vegetable_id");
        productCategory = getIntent().getStringExtra("vegetable_category");
        far_id          = getIntent().getStringExtra("far_id");




        click_farmer_data(far_id);

        setVegetableDetails(productName,productImage,productPrice,productUnit,productId,productCategory);



        add_to_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkFarmer(product_name,product_price,productUnit,productCategory,far_id,productImage,productId,custUid);
            }
        });




    }

    public  void loadLocale(){
        SharedPreferences preferences = getSharedPreferences("Setting", Activity.MODE_PRIVATE);
        String lamguage = preferences.getString("My_Language","");
        Locale locale =new Locale(lamguage);
        Locale.setDefault(locale);

    }

    private void checkFarmer(TextView product_name, TextView product_price, String productUnit, String productCategory, String far_id, String productImage, String productId, String custUid) {


        String isEx = new DealerDatabase(getBaseContext()).checkFoodExists();


        //Toast.makeText(this, ""+isEx, Toast.LENGTH_SHORT).show();
        if(isEx.equals("null")){

            addToemptyCard(custUid);

        }else if(isEx.equals(far_id))
        {
            addToemptyCard(custUid);
        }
        else {
            Toast.makeText(this, "You are fool", Toast.LENGTH_SHORT).show();
        }




    }



    private void addToemptyCard(String custUid) {
        new DealerDatabase(DFarmerProductDetails.this).addToCart(new DealerOrderListModel(
                phone,
                productId,
                productName,
                numberButton.getNumber(),
                productPrice,
                productUnit,
                far_id,
                custUid,
                productImage


        ));

        Toast.makeText(DFarmerProductDetails.this, "Added to cart", Toast.LENGTH_SHORT).show();

    }



    public void click_farmer_data(String farmer_uid) {
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
                adapterFarmerHisProducts = new AdapterHorizontalRecycler(DFarmerProductDetails.this, farmerProductData);
                product_list.setAdapter(adapterFarmerHisProducts);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(DFarmerProductDetails.this, ""+databaseError.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });




    }



    private void setVegetableDetails(String productName, String productImage, String productPrice, String productUnit, String productId, String productCategory) {

        categories.setText(productCategory);
        product_name.setText(productName);
        product_price.setText(productPrice);
        product_price_unit.setText(productUnit);

        try {

            Glide.with(this).load(productImage).into(veg_img);


        }catch (Exception e){

            Glide.with(this).load(R.drawable.farmer_ic).into(veg_img);


        }



    }



}