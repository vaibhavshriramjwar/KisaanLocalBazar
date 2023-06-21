package xyz.vpscorelim.kisaan.farmer;

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

import xyz.vpscorelim.kisaan.R;
import xyz.vpscorelim.kisaan.customer.CustomerOrderDetails;
import xyz.vpscorelim.kisaan.customer.CustomerOrderModel;
import xyz.vpscorelim.kisaan.dealer.DealerOrderToFarmerDeatails;
import xyz.vpscorelim.kisaan.farmer.farmer_adapter.SampleAdapter;
import xyz.vpscorelim.kisaan.farmer.farmer_adapter.sampleAdapetr;

public class FarmerOwnOrderDeatils extends AppCompatActivity {

    String order_id_value="";
    RecyclerView orderLists;
    RecyclerView.LayoutManager layoutManager;
    FirebaseDatabase database,firebaseDatabase,far_database;
    DatabaseReference reference,databaseReference,far_reference;
    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    SampleAdapter orderViewHolder;
    List<FarmerOrderModel> customerRequest;

    TextView orderId,order_price,user_name,shipping_address,user_call;
    TextView dealer_name,dealer_address,dealer_call;

    MaterialButton call_now;

    String call;


    ImageView back;

    //Card One Identity
    TextView card1TextTitle;
    MaterialButton card1Btn;
    ImageView card1Img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.farmer_own_order_deatils);
        loadLocale();
        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_ALWAYS, WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_ALWAYS);
        getSupportActionBar().hide();


        //Initialize Card1
        card1TextTitle = findViewById(R.id.textTitle);
        card1Btn = findViewById(R.id.make_order);
        card1Img = findViewById(R.id.ic_img);
        loadCard1Ads();

        back= findViewById(R.id.close);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        orderLists = findViewById(R.id.order_items);
        layoutManager = new LinearLayoutManager(this);
        orderLists.setLayoutManager(layoutManager);
        orderLists.setHasFixedSize(true);


        orderId          = findViewById(R.id.order_id);
        order_price      = findViewById(R.id.order_price);
        user_name        = findViewById(R.id.user_name);
        shipping_address = findViewById(R.id.shipping_address);
        user_call        = findViewById(R.id.user_call);


        dealer_name          = findViewById(R.id.farmer_name);
        dealer_address       = findViewById(R.id.farmer_address);
        dealer_call          = findViewById(R.id.farmer_call);

        database = FirebaseDatabase.getInstance();
        reference = database.getReference("DealerRequestOrder");

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();


        String userPhone = currentUser.getPhoneNumber();

        order_id_value = getIntent().getStringExtra("OrderId");
        String userPho = getIntent().getStringExtra("phone");
        String farmerId = getIntent().getStringExtra("dealer_id");




        far_database  = FirebaseDatabase.getInstance();
        far_reference = far_database.getReference("Dealer_Data");
        Query query_far = far_reference.orderByChild("uid").equalTo(farmerId);
        query_far.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds :snapshot.getChildren()){
                    String dealer_name1             =""+ds.child("dealerName").getValue();
                    call                            =""+ds.child("phoneNumber").getValue();
                    String dealer_selling_location1  =""+ds.child("shopLocation").getValue();


                    dealer_name.setText(dealer_name1);
                    dealer_call.setText(call);
                    dealer_address.setText(dealer_selling_location1);



                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        call_now = findViewById(R.id.call_now);
        call_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_DIAL);
                String p = "tel:"+call;
                i.setData(Uri.parse(p));
                startActivity(i);
            }
        });




        assert userPhone != null;
        if(userPhone.equals(userPho)){
            Query query = reference.child(order_id_value).child("orderModels");
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    customerRequest = new ArrayList<>();
                    for (DataSnapshot pvostsnap : dataSnapshot.getChildren()) {
                        FarmerOrderModel post = pvostsnap.getValue(FarmerOrderModel.class);
                        //customerRequest.clear();
                        customerRequest.add(post);
                    }
                    orderViewHolder = new SampleAdapter(FarmerOwnOrderDeatils.this, customerRequest);
                    orderLists.setAdapter(orderViewHolder);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                    Toast.makeText(FarmerOwnOrderDeatils.this, ""+databaseError.getMessage(), Toast.LENGTH_SHORT).show();

                }
            });
        }



        //Get Ordered User data
        mAuth             = FirebaseAuth.getInstance();
        currentUser       = mAuth.getCurrentUser();
        firebaseDatabase  = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Farmer_Data");
        Query query = databaseReference.orderByChild("phoneNumber").equalTo(userPho);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds :snapshot.getChildren()){
                    String farmer_name =""+ds.child("Farmer_Name").getValue();
                    String farmer_phone =""+ds.child("phoneNumber").getValue();
                    user_name.setText(farmer_name);
                    user_call.setText(farmer_phone);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });









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
        card1ref = card1.getReference("Farmer_Ads");
        Query query = card1ref.orderByChild("id").equalTo("c3");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds :snapshot.getChildren()){
                    String title          =""+ds.child("Title").getValue();
                    String img            =""+ds.child("image").getValue();
                    Glide.with(FarmerOwnOrderDeatils.this).load(img).into(card1Img);
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

    private void getDealerId(String farmerId) {

        far_database  = FirebaseDatabase.getInstance();
        far_reference = far_database.getReference("Dealer_Data");
        Query query_far = far_reference.orderByChild("uid").equalTo(farmerId);
        query_far.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds :snapshot.getChildren()){
                    String dealer_name1             =""+ds.child("dealerName").getValue();
                    String dealer_phone1            =""+ds.child("phoneNumber").getValue();
                    String dealer_selling_location1  =""+ds.child("shopLocation").getValue();


                    dealer_name.setText(dealer_name1);
                    dealer_call.setText(dealer_phone1);
                    dealer_address.setText(dealer_selling_location1);



                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}