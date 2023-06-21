package xyz.vpscorelim.kisaan.dealer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.URLUtil;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Locale;

import xyz.vpscorelim.kisaan.R;
import xyz.vpscorelim.kisaan.adapter.ViewPagerAdapter;
import xyz.vpscorelim.kisaan.customer.CustomerOrderDetails;
import xyz.vpscorelim.kisaan.farmer.FarmerArriveOrders;
import xyz.vpscorelim.kisaan.farmer.FarmerMyOrders;

public class DealerOrderList extends AppCompatActivity {

     TabLayout tabLayout;
     ViewPager viewPager;

     ImageView back;

    //Card One Identity
    TextView card1TextTitle;
    MaterialButton card1Btn;
    ImageView card1Img;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dealer_order_list);
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


        tabLayout        =findViewById(R.id.tablayout_id);
        viewPager        =findViewById(R.id.viewpager2);


        //Initialize Card1
        card1TextTitle = findViewById(R.id.textTitle);
        card1Btn = findViewById(R.id.make_order);
        card1Img = findViewById(R.id.ic_img);
        loadCard1Ads();

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.AddFragment(new Dealer_My_Orders(),"Farmer Orders");
        viewPagerAdapter.AddFragment(new Dealer_Order_To_Farmer(),"Your Orders");
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);




        BottomNavigationView bottomNavigationView = findViewById(R.id.dealer_bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.dealer_order);
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
                        return true;
                    case R.id.dealer_search:
                        startActivity(new Intent(getApplicationContext(), SearchTwo.class));
                        overridePendingTransition(0,0);
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

    private void loadCard1Ads() {
        FirebaseDatabase card1;
        DatabaseReference card1ref;
        card1  = FirebaseDatabase.getInstance();
        card1ref = card1.getReference("Dealer_Ads");
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
                                Toast.makeText(DealerOrderList.this, "Url is not specified", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    Glide.with(DealerOrderList.this).load(img).into(card1Img);
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