package xyz.vpscorelim.kisaan.customer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.google.android.material.bottomnavigation.BottomNavigationView;
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

import xyz.vpscorelim.kisaan.KisanLocalBazar;
import xyz.vpscorelim.kisaan.R;
import xyz.vpscorelim.kisaan.adapter.Adapter_Farmer_His_products;
import xyz.vpscorelim.kisaan.adapter.OrderViewHolder;
import xyz.vpscorelim.kisaan.farmer.Farmer_Product_Data;

public class CustomerMyOrder extends AppCompatActivity {


    RecyclerView order_list;
    RecyclerView.LayoutManager layoutManager;
    FirebaseDatabase database;
    DatabaseReference reference;

    OrderViewHolder orderViewHolder;
    List<CustomerRequest> customerRequests;

    FirebaseAuth mAuth;
    FirebaseUser currentUser;

    String txtPhoneNo;
    ImageView back;

    KisanLocalBazar kisanLocalBazar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customer_my_order);
        kisanLocalBazar = KisanLocalBazar.getzInstance();
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

        order_list = findViewById(R.id.order_list);
        order_list.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        order_list.setLayoutManager(layoutManager);
        order_list.setHasFixedSize(true);


        database = FirebaseDatabase.getInstance();
        reference = database.getReference("RequestOrder");

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        txtPhoneNo = currentUser.getPhoneNumber();

        getOrderList(txtPhoneNo);

        
        order();





        //Bottom Navigation
        BottomNavigationView bottomNavigationView = findViewById(R.id.customer_bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.customer_order);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.customer_dashboard:
                        startActivity(new Intent(getApplicationContext(), Customer_Home.class));
                        Animatoo.animateSlideRight(CustomerMyOrder.this);
                        return true;
                    case R.id.customer_profile:
                        startActivity(new Intent(getApplicationContext(), Customer_Profile.class));
                        Animatoo.animateSlideLeft(CustomerMyOrder.this);
                        return true;
                    case R.id.customer_search:
                        startActivity(new Intent(getApplicationContext(), SearchOne.class));
                        Animatoo.animateSlideRight(CustomerMyOrder.this);
                        return true;
                    case R.id.customer_order:
                        return true;
                    case R.id.customer_cart:
                        startActivity(new Intent(getApplicationContext(), ViewMyCart.class));
                        Animatoo.animateSlideLeft(CustomerMyOrder.this);
                        return true;
                }
                return false;
            }
        });
        //End of Bottom Navigation




    }

    private void order() {







    }

    private void getOrderList(String txtPhoneNo) {



        Query query = reference.orderByChild("phone").equalTo(txtPhoneNo);
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
                orderViewHolder = new OrderViewHolder(CustomerMyOrder.this, customerRequests);
                order_list.setAdapter(orderViewHolder);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(CustomerMyOrder.this, ""+databaseError.getMessage(), Toast.LENGTH_SHORT).show();

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