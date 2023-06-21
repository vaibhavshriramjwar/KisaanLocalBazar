package xyz.vpscorelim.kisaan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import xyz.vpscorelim.kisaan.adapter.ViewAdapter;
import xyz.vpscorelim.kisaan.adapter.ViewModel;
import xyz.vpscorelim.kisaan.customer.Customer_Home;
import xyz.vpscorelim.kisaan.dealer.Dealer_Home;
import xyz.vpscorelim.kisaan.farmer.Farmer_Home;

import android.Manifest;
import android.animation.ArgbEvaluator;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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
import com.pnikosis.materialishprogress.ProgressWheel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CheckUserProfile extends AppCompatActivity {
    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference ;
    ProgressDialog pd;
    MaterialButton continue_forward;


    ViewPager viewPager;
    ViewAdapter adapter;
    List<ViewModel> models;
    ProgressWheel progressWheel;
    Integer[] colors = null;
    ArgbEvaluator argbEvaluator = new ArgbEvaluator();

    KisanLocalBazar kisanLocalBazar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.check_user_profile);
        kisanLocalBazar = KisanLocalBazar.getzInstance();
        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_ALWAYS, WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_ALWAYS);
        getSupportActionBar().hide();
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        //permission();



        progressWheel   = findViewById(R.id.loading);
        progressWheel.setVisibility(View.VISIBLE);
        getUserRole();

//        if(ActivityCompat.checkSelfPermission(CheckUserProfile.this
//                , Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED)
//        {
//            permission();
//            getUserRole();
//
//        }
//        else
//        {
//            ActivityCompat.requestPermissions(CheckUserProfile.this
//                    ,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},44);
//        }

        continue_forward = findViewById(R.id.continue_forward);

        continue_forward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ActivityCompat.checkSelfPermission(CheckUserProfile.this
                , Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED)
                {

                    getUserRole();

                }
                else
                    {
                    ActivityCompat.requestPermissions(CheckUserProfile.this
                            ,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},44);
                }

            }
        });



        loadSlide();










                }

    private void loadSlide() {

        models = new ArrayList<>();
        models.add(new ViewModel(R.drawable.banner_2, "Brochure", "Brochure is an informative paper document (often also used for advertising) that can be folded into a template"));
        models.add(new ViewModel(R.drawable.banner_2, "Sticker", "Sticker is a type of label: a piece of printed paper, plastic, vinyl, or other material with pressure sensitive adhesive on one side"));
        models.add(new ViewModel(R.drawable.banner_2, "Poster", "Poster is any piece of printed paper designed to be attached to a wall or vertical surface."));
        models.add(new ViewModel(R.drawable.banner_2, "Namecard", "Business cards are cards bearing business information about a company or individual."));
        models.add(new ViewModel(R.drawable.banner_2, "Namecard", "Business cards are cards bearing business information about a company or individual."));


        adapter = new ViewAdapter(models, this);

        viewPager = findViewById(R.id.viewPager);
        viewPager.setAdapter(adapter);
        viewPager.setPadding(120, 0, 120, 80);

        Integer[] colors_temp = {
                getResources().getColor(R.color.my_color1),
                getResources().getColor(R.color.my_color1),
                getResources().getColor(R.color.my_color1),
                getResources().getColor(R.color.my_color1)
        };

        colors = colors_temp;

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                if (position < (adapter.getCount() +2) && position < (colors.length - 1)) {
                    viewPager.setBackgroundColor(

                            (Integer) argbEvaluator.evaluate(
                                    positionOffset,
                                    colors[position],
                                    colors[position + 1]
                            )
                    );
                }

                else {
                    viewPager.setBackgroundColor(colors[colors.length - 1]);
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


    }

    private void permission() {
        if(ActivityCompat.checkSelfPermission(CheckUserProfile.this
                , Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED)
        {

            Toast.makeText(this, "Thank You for giving permission", Toast.LENGTH_SHORT).show();

        }
        else
        {
            ActivityCompat.requestPermissions(CheckUserProfile.this
                    ,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},44);


        }

    }

    private void getUserRole() {
        //pd.show();

        //pd.setMessage("Please wait");
        mAuth             = FirebaseAuth.getInstance();
        currentUser       = mAuth.getCurrentUser();
        firebaseDatabase  = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("User_Data");

        Query query = databaseReference.orderByChild("phoneNumber").equalTo(currentUser.getPhoneNumber());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds :snapshot.getChildren()){
                    final String userRole    =""+ds.child("User_Role").getValue();
                    final String phoneNumber =""+ds.child("phoneNumber").getValue();
                    final String uid         = currentUser.getUid();


                    Toast.makeText(CheckUserProfile.this, "Welcome To Kisaan Local Bazar!", Toast.LENGTH_SHORT).show();

                    switch (userRole){
                        case "farmer":
                            //pd.dismiss();
                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Farmer_Data");
                            reference.orderByChild("phoneNumber").equalTo(phoneNumber).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if(snapshot.getValue()!=null){
                                        progressWheel.setVisibility(View.GONE);
                                        Intent intent = new Intent(CheckUserProfile.this, Farmer_Home.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                        //Toast.makeText(CheckUserProfile.this, "Farmer", Toast.LENGTH_SHORT).show();

                                    }else{

                                        HashMap<Object ,String> hashMap =new HashMap<>();
                                        //put hashmap in info
                                        hashMap.put("User_Role",userRole);
                                        hashMap.put("uid",uid);
                                        hashMap.put("phoneNumber",phoneNumber);
                                        hashMap.put("Farmer_Name","null");
                                        hashMap.put("State_Name","null");
                                        hashMap.put("District_Name","null");
                                        hashMap.put("Tehsil_Name","null");
                                        hashMap.put("Selling_Location","null");
                                        hashMap.put("latitude","null");
                                        hashMap.put("longitude","null");
                                        hashMap.put("Bulk_avail","null");
                                        hashMap.put("movable","null");
                                        hashMap.put("UPI_ID","null");
                                        //Firebase Database  instance
                                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                                        DatabaseReference reference = database.getReference("Farmer_Data");
                                        reference.child(uid).setValue(hashMap);
                                        //Toast.makeText(CheckUserProfile.this, "Data Added", Toast.LENGTH_SHORT).show();
                                        progressWheel.setVisibility(View.GONE);
                                        Intent intent = new Intent(CheckUserProfile.this,Farmer_Home.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                    }

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                    Toast.makeText(CheckUserProfile.this, "Something Goes Wrong", Toast.LENGTH_SHORT).show();
                                }
                            });
                            break;


                        case "customer":
                            //pd.dismiss();
                            DatabaseReference sec_reference = FirebaseDatabase.getInstance().getReference("Customer_Data");
                            sec_reference.orderByChild("phoneNumber").equalTo(phoneNumber).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if(snapshot.getValue()!=null){
                                        progressWheel.setVisibility(View.GONE);
                                        Intent intent = new Intent(CheckUserProfile.this, Customer_Home.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                        //Toast.makeText(CheckUserProfile.this, "customer", Toast.LENGTH_SHORT).show();

                                    }else{

                                        HashMap<Object ,String> hashMap =new HashMap<>();
                                        //put hashmap in info
                                        hashMap.put("User_Role",userRole);
                                        hashMap.put("uid",uid);
                                        hashMap.put("phoneNumber",phoneNumber);
                                        hashMap.put("Customer_name","null");
                                        hashMap.put("State_Name","null");
                                        hashMap.put("District_Name","null");
                                        hashMap.put("Tehsil_Name","null");
                                        hashMap.put("Current_Location","null");
                                        hashMap.put("latitude","null");
                                        hashMap.put("longitude","null");
                                        //Firebase Database  instance
                                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                                        DatabaseReference reference = database.getReference("Customer_Data");
                                        reference.child(uid).setValue(hashMap);
                                        //Toast.makeText(CheckUserProfile.this, "Data Added", Toast.LENGTH_SHORT).show();
                                        progressWheel.setVisibility(View.GONE);
                                        Intent intent = new Intent(CheckUserProfile.this,Customer_Home.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                    }

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    Toast.makeText(CheckUserProfile.this, "Something Goes Wrong", Toast.LENGTH_SHORT).show();
                                }
                            });
                            break;


                        case "dealer":
                            //pd.dismiss();
                            DatabaseReference third_reference = FirebaseDatabase.getInstance().getReference("Dealer_Data");
                            third_reference.orderByChild("phoneNumber").equalTo(phoneNumber).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if(snapshot.getValue()!=null){
                                        progressWheel.setVisibility(View.GONE);
                                        Intent intent = new Intent(CheckUserProfile.this, Dealer_Home.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                        //Toast.makeText(CheckUserProfile.this, "dealer", Toast.LENGTH_SHORT).show();

                                    }else{

                                        HashMap<Object ,String> hashMap =new HashMap<>();
                                        //put hashMap in info
                                        hashMap.put("User_Role",userRole);
                                        hashMap.put("uid",uid);
                                        hashMap.put("phoneNumber",phoneNumber);
                                        hashMap.put("storeName","null");
                                        hashMap.put("dealerName","null");
                                        hashMap.put("stateName","null");
                                        hashMap.put("districtName","null");
                                        hashMap.put("subArea","null");
                                        hashMap.put("shopLocation","null");
                                        hashMap.put("Home_Delivery","null");
                                        hashMap.put("latitude","null");
                                        hashMap.put("longitude","null");
                                        hashMap.put("UPI_ID","null");
                                        //Firebase Database  instance
                                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                                        DatabaseReference reference = database.getReference("Dealer_Data");
                                        reference.child(uid).setValue(hashMap);
                                        //Toast.makeText(CheckUserProfile.this, "Data Added", Toast.LENGTH_SHORT).show();
                                        progressWheel.setVisibility(View.GONE);
                                        Intent intent = new Intent(CheckUserProfile.this,Dealer_Home.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                    }

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    progressWheel.setVisibility(View.GONE);
                                    Toast.makeText(CheckUserProfile.this, "Something Goes Wrong", Toast.LENGTH_SHORT).show();
                                }
                            });
                            break;
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });





    }


}
