package xyz.vpscorelim.kisaan.customer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.URLUtil;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
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

import xyz.vpscorelim.kisaan.KisanLocalBazar;
import xyz.vpscorelim.kisaan.R;
import xyz.vpscorelim.kisaan.farmer.FarmerOrderDetails;
import xyz.vpscorelim.kisaan.farmer.farmer_adapter.sampleAdapetr;

public class CustomerOrderDetails extends AppCompatActivity {


    String order_id_value="";
    RecyclerView orderLists;
    RecyclerView.LayoutManager layoutManager;
    FirebaseDatabase database,firebaseDatabase,far_database;
    DatabaseReference reference,databaseReference,far_reference;
    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    sampleAdapetr orderViewHolder;
    List<CustomerOrderModel> customerRequest;

    TextView orderId,order_price,user_name,shipping_address,user_call;
    TextView farmer_name,farmer_address,farmer_call;


    //Card One Identity
    TextView card1TextTitle;
    MaterialButton card1Btn;
    ImageView card1Img;
    ImageView back;

    KisanLocalBazar kisanLocalBazar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customer_order_details);
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

        //Initialize Card1
        card1TextTitle = findViewById(R.id.textTitle);
        card1Btn = findViewById(R.id.make_order);
        card1Img = findViewById(R.id.ic_img);
        loadCard1Ads();


        orderLists = findViewById(R.id.order_items);
        layoutManager = new LinearLayoutManager(this);
        orderLists.setLayoutManager(layoutManager);
        orderLists.setHasFixedSize(true);


        orderId          = findViewById(R.id.order_id);
        order_price      = findViewById(R.id.order_price);
        user_name        = findViewById(R.id.user_name);
        shipping_address = findViewById(R.id.shipping_address);
        user_call        = findViewById(R.id.user_call);


        farmer_name          = findViewById(R.id.farmer_name);
        farmer_address       = findViewById(R.id.farmer_address);
        farmer_call          = findViewById(R.id.farmer_call);






        database = FirebaseDatabase.getInstance();
        reference = database.getReference("RequestOrder");


        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();


        String userPhone = currentUser.getPhoneNumber();


            order_id_value = getIntent().getStringExtra("OrderId");
            String userPho = getIntent().getStringExtra("phone");
            String farmerId = getIntent().getStringExtra("farmer_id");

            if(userPhone.equals(userPho)){
                Query query = reference.child(order_id_value).child("orderModels");
                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        customerRequest = new ArrayList<>();
                        for (DataSnapshot pvostsnap : dataSnapshot.getChildren()) {
                            CustomerOrderModel post = pvostsnap.getValue(CustomerOrderModel.class);
                            //customerRequest.clear();
                            customerRequest.add(post);
                        }
                        orderViewHolder = new sampleAdapetr(CustomerOrderDetails.this, customerRequest);
                        orderLists.setAdapter(orderViewHolder);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                        Toast.makeText(CustomerOrderDetails.this, ""+databaseError.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });
            }




            //Get Ordered User data
            mAuth             = FirebaseAuth.getInstance();
            currentUser       = mAuth.getCurrentUser();
            firebaseDatabase  = FirebaseDatabase.getInstance();
            databaseReference = firebaseDatabase.getReference("Customer_Data");
            Query query = databaseReference.orderByChild("phoneNumber").equalTo(userPho);
            query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds :snapshot.getChildren()){
                    String current_name =""+ds.child("Customer_name").getValue();
                    String current_phone =""+ds.child("phoneNumber").getValue();
                    user_name.setText(current_name);
                    user_call.setText(current_phone);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });


        //Get Ordered User data
        far_database  = FirebaseDatabase.getInstance();
        far_reference = far_database.getReference("Farmer_Data");
        Query query_far = far_reference.orderByChild("uid").equalTo(farmerId);
        query_far.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds :snapshot.getChildren()){
                    String farmer_name1              =""+ds.child("Farmer_Name").getValue();
                    String farmer_phone1             =""+ds.child("phoneNumber").getValue();
                    String farmer_selling_location1  =""+ds.child("Selling_Location").getValue();

                    farmer_name.setText(farmer_name1);
                    farmer_call.setText(farmer_phone1);
                    farmer_address.setText(farmer_selling_location1);



                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        //Get Ordered User data


        Query query_ord = reference.orderByChild("order_id").equalTo(order_id_value);
        query_ord.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds :snapshot.getChildren()){
                    String order__ID              =""+ds.child("order_id").getValue();
                    String order__Pri              =""+ds.child("total").getValue();
                    String order__add              =""+ds.child("address").getValue();

                    orderId.setText(order__ID);
                    order_price.setText(order__Pri);
                    shipping_address.setText(order__add);



                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });






    }



    private void loadCard1Ads() {
        FirebaseDatabase card1;
        DatabaseReference card1ref;
        card1  = FirebaseDatabase.getInstance();
        card1ref = card1.getReference("Customer_Ads");
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
                                Toast.makeText(CustomerOrderDetails.this, "Url is not specified", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    Glide.with(CustomerOrderDetails.this).load(img).into(card1Img);
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
}