package xyz.vpscorelim.kisaan.farmer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import xyz.vpscorelim.kisaan.AboutUs;
import xyz.vpscorelim.kisaan.BuildConfig;
import xyz.vpscorelim.kisaan.CheckUserProfile;
import xyz.vpscorelim.kisaan.Developer;
import xyz.vpscorelim.kisaan.Log_In;
import xyz.vpscorelim.kisaan.Notification.Constant;
import xyz.vpscorelim.kisaan.PrivacyPolicy;
import xyz.vpscorelim.kisaan.R;
import xyz.vpscorelim.kisaan.customer.Customer_Home;
import xyz.vpscorelim.kisaan.customer.Customer_Profile;
import xyz.vpscorelim.kisaan.dealer.DealerCommon;
import xyz.vpscorelim.kisaan.dealer.Dealer_Profile;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class Farmer_Profile extends AppCompatActivity {


    ProgressDialog pd;
    Dialog myDialog,popAddPost,notification_popup,change_language;
    String lang;
    MaterialButton logout;
    SwitchMaterial fcmSwitch,store_close1;
    TextView store12;

    public FirebaseAuth firebaseAuth;
    public FirebaseUser currentUser;
    public FirebaseDatabase firebaseDatabase;
    public DatabaseReference reference;
    String farmer_uid;

    private boolean isChecked =false;
    private boolean isChecked1 =false;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor shEditor;

    TextView farmerName,farmerSubArea,farmerPhoneNumber,farmerCity,farmerState,upiIdText;


    RelativeLayout check_orders,my_product,my_order,edit_profile,edit_selling_location,store_setting;
    RelativeLayout notifications,privacy_policy,developer,about_us,share_app,log_out,changeLanguage;


    final FusedLocationProviderClient[] fusedLocationProviderClient = new FusedLocationProviderClient[1];
    int PLACE_PICKER_REQUEST = 1;


    ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.farmer_profile);
        loadLocale();
        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_ALWAYS, WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_ALWAYS);
        getSupportActionBar().hide();
        change_language =new Dialog(this);
        myDialog = new Dialog(this);
        popAddPost = new Dialog(this);
        notification_popup = new Dialog(this);
        pd = new ProgressDialog(this);


        upiIdText = findViewById(R.id.upiIdText);


        changeLanguage = findViewById(R.id.r13);
        changeLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseNewLanguage();
            }
        });




        back= findViewById(R.id.close);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });



        //Firebase
        firebaseAuth= FirebaseAuth.getInstance();
        currentUser =firebaseAuth.getCurrentUser();
        firebaseDatabase =FirebaseDatabase.getInstance();
        reference =firebaseDatabase.getReference("Farmer_Data");
        farmer_uid =  currentUser.getUid();




        store_close1 = findViewById(R.id.store_open1);
        store12 = findViewById(R.id.store11);




        //Init Variable
        check_orders          = findViewById(R.id.r1);
        my_product            = findViewById(R.id.r2);
        my_order              = findViewById(R.id.r3);
        edit_profile          = findViewById(R.id.r4);
        edit_selling_location = findViewById(R.id.r56);
        store_setting         = findViewById(R.id.r5);




        notifications         = findViewById(R.id.r6);
        privacy_policy        = findViewById(R.id.r7);
        developer             = findViewById(R.id.r8);
        about_us              = findViewById(R.id.r9);
        share_app             = findViewById(R.id.r10);
        log_out               = findViewById(R.id.r11);
        log_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(Farmer_Profile.this, Log_In.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                Toast.makeText(Farmer_Profile.this, "Farmer", Toast.LENGTH_SHORT).show();

            }
        });




        //TextView
        farmerName          = findViewById(R.id.farmer_name);
        farmerSubArea       = findViewById(R.id.farmer_tahsil);
        farmerPhoneNumber   = findViewById(R.id.customer_phone);
        farmerCity          = findViewById(R.id.district_name);
        farmerState         = findViewById(R.id.state_name);




        final BottomNavigationView bottomNavigationView = findViewById(R.id.farmer_bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.farmer_profile_new);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.farmer_dashboard:
                        startActivity(new Intent(getApplicationContext(),Farmer_Home.class));
                        Animatoo.animateSlideRight(Farmer_Profile.this);
                        return true;
                    case R.id.farmer_profile:
                        startActivity(new Intent(getApplicationContext(),FarmerViewMyCart.class));
                        Animatoo.animateSlideRight(Farmer_Profile.this);
                        return true;
                    case R.id.farmer_orders:
                        startActivity(new Intent(getApplicationContext(),Farmer_Order_Status.class));
                        Animatoo.animateSlideRight(Farmer_Profile.this);
                        return true;
                    case R.id.farmer_profile_new:
                        return true;
                    case R.id.farmer_search:
                        startActivity(new Intent(getApplicationContext(),SearchDealerFarmer.class));
                        Animatoo.animateSlideRight(Farmer_Profile.this);
                        return true;
                }
                return false;
            }
        });




        //---------------------/Get Current User Data/----------------------------//
        Query query = reference.orderByChild("uid").equalTo(farmer_uid);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds :snapshot.getChildren()){
                    String farmer_name        =""+ds.child("Farmer_Name").getValue();
                    String farmer_sub_area    =""+ds.child("Tehsil_Name").getValue();
                    String farmer_phone       =""+ds.child("phoneNumber").getValue();
                    String farmer_city        =""+ds.child("District_Name").getValue();
                    String farmer_state       =""+ds.child("State_Name").getValue();
                    String bulk_service       =""+ds.child("Bulk_avail").getValue();
                    String store              =""+ds.child("Store").getValue();
                    String UPI_ID             =""+ds.child("UPI_ID").getValue();
                    FarmerCommon.BULK_SERVICE_AVAILABLE = bulk_service;
                    farmerName.setText(farmer_name);
                    farmerSubArea.setText(farmer_sub_area);
                    farmerPhoneNumber.setText(farmer_phone);
                    farmerCity.setText(farmer_city);
                    farmerState.setText(farmer_state);
                    upiIdText.setText(UPI_ID);

                    if(store.equals("open")){

                        store_close1.setChecked(true);
                        store12.setText("Open");

                    }else {

                        store_close1.setChecked(false);
                        store12.setText("Close");
                    }


                    store_close1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            if(isChecked){

                                Toast.makeText(Farmer_Profile.this, "Checked", Toast.LENGTH_SHORT).show();
                                HashMap<String,Object> result =new HashMap<>();
                                result.put("Store","open");
                                reference.child(currentUser.getUid()).updateChildren(result)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(Farmer_Profile.this, "Your Information is Updated", Toast.LENGTH_SHORT).show();
                                                //pd.dismiss();


                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(Farmer_Profile.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });


                            }else {

                                Toast.makeText(Farmer_Profile.this, "UnChecked", Toast.LENGTH_SHORT).show();
                                HashMap<String,Object> result =new HashMap<>();
                                result.put("Store","close");
                                reference.child(currentUser.getUid()).updateChildren(result)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(Farmer_Profile.this, "Your Information is Updated", Toast.LENGTH_SHORT).show();
                                                //pd.dismiss();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(Farmer_Profile.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });

                            }
                        }
                    });


                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        //---------------------//----------------------------//










        fcmSwitch=findViewById(R.id.fcmSwitch);
        firebaseAuth= FirebaseAuth.getInstance();
        sharedPreferences =getSharedPreferences("NOTIFICATION",MODE_PRIVATE);
        isChecked = sharedPreferences.getBoolean("FCM_ENABLED",false);
        fcmSwitch.setChecked(isChecked);
        fcmSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){

                    subscribedToTopic();

                }else {

                    unsubscribedToTopic();

                }
            }
        });

        logout = findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(Farmer_Profile.this, Log_In.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                Toast.makeText(Farmer_Profile.this, "Farmer", Toast.LENGTH_SHORT).show();
            }
        });







        //On Click Listener
        check_orders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Farmer_Profile.this,Farmer_Order_Status.class));
                Animatoo.animateZoom(Farmer_Profile.this);
            }
        });



        my_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getSupportFragmentManager();
                Farmer_My_Product fragment = new Farmer_My_Product();
                fm.beginTransaction()
                        .add(R.id.container,fragment).addToBackStack(null)
                        .commit();
            }
        });



        my_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Farmer_Profile.this, "My Order Click", Toast.LENGTH_SHORT).show();
            }
        });



        edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEditProfileDialog();
                Toast.makeText(Farmer_Profile.this, "Edit Profile Click", Toast.LENGTH_SHORT).show();
            }
        });



        edit_selling_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                fusedLocationProviderClient[0] = LocationServices.getFusedLocationProviderClient(Farmer_Profile.this);
                if(ActivityCompat.checkSelfPermission(Farmer_Profile.this
                        , Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED){
                    fusedLocationProviderClient[0].getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                        @Override
                        public void onComplete(@NonNull Task<Location> task) {
                            Location location = task.getResult();
                            if(location !=null){
                                try {
                                    Geocoder geocoder = new Geocoder(Farmer_Profile.this,
                                            Locale.getDefault());
                                    List<Address> addresses = geocoder.getFromLocation(
                                            location.getLatitude(),location.getLongitude(),1
                                    );
                                    FarmerCommon.new_latitude = addresses.get(0).getLatitude();
                                    FarmerCommon.new_longitude = addresses.get(0).getLongitude();
                                    FarmerCommon.new_selling_location = addresses.get(0).getAddressLine(0);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    });
                }else{
                    ActivityCompat.requestPermissions(Farmer_Profile.this
                            ,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},44);
                }



                startActivity(new Intent(Farmer_Profile.this,change_Location.class));
                Animatoo.animateZoom(Farmer_Profile.this);
                Toast.makeText(Farmer_Profile.this, "Edit Selling Location", Toast.LENGTH_SHORT).show();

            }
        });



        store_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeLocationPopup();
                Toast.makeText(Farmer_Profile.this, "Store Setting", Toast.LENGTH_SHORT).show();
            }
        });



        notifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSettingPop();
            }
        });



        privacy_policy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(Farmer_Profile.this, PrivacyPolicy.class));
                Animatoo.animateZoom(Farmer_Profile.this);

            }
        });



        developer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                startActivity(new Intent(Farmer_Profile.this, Developer.class));
                Animatoo.animateZoom(Farmer_Profile.this);

            }
        });



        about_us.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                startActivity(new Intent(Farmer_Profile.this, AboutUs.class));
                Animatoo.animateZoom(Farmer_Profile.this);

            }
        });



        share_app.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                shareApp();

            }
        });




    }


    private void shareApp() {


        try {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Kisaan Local Bazar");
            String shareMessage= "\nLet me recommend you this application\n\n";
            shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID +"\n\n";
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
            startActivity(Intent.createChooser(shareIntent, "choose one"));
        } catch(Exception e) {
            //e.toString();
        }


//        try {
//
//            String link ="https://kisaanlocalbazar.page.link/?"+
//                    "link=https://vpscorelim.in"+
//                    "&apn="+getPackageName()+
//                    "&st=My refer link"+
//                    "&sd=Reward Coins"+
//                    "&si="+"https://www.vpscorelim.in/wp-content/uploads/2020/10/cropped-logo-otoes2xz3opsxwwjf10857rl6m4vkt2q0huua98kru-300x100.png";
//
//            Task<ShortDynamicLink> shortLinkTask = FirebaseDynamicLinks.getInstance().createDynamicLink()
//                    .setLink(Uri.parse("https://www.example.com/"))
//                    .setDomainUriPrefix("https://kisaanlocalbazar.page.link")
//                    .setSocialMetaTagParameters(
//                            new DynamicLink.SocialMetaTagParameters.Builder()
//                                    .setTitle("Example of a Dynamic Link")
//                                    .setDescription("This link works whether the app is installed or not!")
//                                    .build())
//                    .buildShortDynamicLink()
//                    .addOnCompleteListener(this, new OnCompleteListener<ShortDynamicLink>() {
//                        @Override
//                        public void onComplete(@NonNull Task<ShortDynamicLink> task) {
//                            if (task.isSuccessful()) {
//                                // Short link created
//                                Uri shortLink = task.getResult().getShortLink();
//                                Uri flowchartLink = task.getResult().getPreviewLink();
//
//                                Intent intent = new Intent(Intent.ACTION_SEND);
//                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                                String sharemessge = getString(R.string.app_name);
//                                sharemessge = sharemessge +"https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID +"\n\n";
//                                intent.putExtra(Intent.EXTRA_TEXT,link);
//                                intent.setType("text/plain");
//                                startActivity(Intent.createChooser(intent, "Share app via"));
//
//                            } else {
//                                // Error
//                                // ...
//                            }
//                        }
//                    });
//
//
//
//        }
//        catch (Exception e){
//            e.printStackTrace();
//        }
    }

    private void openSettingPop() {

        TextView cut;
        SwitchMaterial fcmSwitch;
        myDialog.setContentView(R.layout.customer_popup_form);
        myDialog.setCanceledOnTouchOutside(false);
        cut = (TextView)myDialog.findViewById(R.id.cut);
        fcmSwitch=(SwitchMaterial)myDialog.findViewById(R.id.fcmSwitch);
        firebaseAuth= FirebaseAuth.getInstance();
        sharedPreferences =getSharedPreferences("NOTIFICATION",MODE_PRIVATE);
        isChecked = sharedPreferences.getBoolean("FCM_ENABLED",false);
        fcmSwitch.setChecked(isChecked);
        fcmSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){

                    subscribedToTopic();

                }else {

                    unsubscribedToTopic();

                }
            }
        });



        cut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });


        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();


    }

    private void changeLocationPopup() {
        SwitchMaterial store_open_close,home_delivery,bulk_service;
        TextView bulk1,home1,store1;
        myDialog.setContentView(R.layout.store_settings);
        myDialog.setCanceledOnTouchOutside(false);
        store_open_close = (SwitchMaterial)myDialog.findViewById(R.id.store_open);
        home_delivery = (SwitchMaterial)myDialog.findViewById(R.id.home_delivery);
        bulk_service = (SwitchMaterial)myDialog.findViewById(R.id.bulk_service);
        bulk1 = (TextView)myDialog.findViewById(R.id.bulk1);
        home1 = (TextView)myDialog.findViewById(R.id.home1);
        store1 = (TextView)myDialog.findViewById(R.id.store1);


        Query query = reference.orderByChild("uid").equalTo(farmer_uid);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds :snapshot.getChildren()){
                    String bulk_service1         =""+ds.child("Bulk_avail").getValue();
                    String home_deliver          =""+ds.child("Home_Delivery").getValue();
                    String store_open_clo        =""+ds.child("Store").getValue();
                    FarmerCommon.BULK_SERVICE_AVAILABLE = bulk_service1;
                    if(bulk_service1.equals("Yes")){

                        bulk_service.setChecked(true);
                        bulk1.setText("Available");

                    }else {

                        bulk_service.setChecked(false);
                        bulk1.setText("Not Available");
                    }
                    if(home_deliver.equals("Yes")){

                        home_delivery.setChecked(true);
                        home1.setText("Available");

                    }else {
                        home_delivery.setChecked(false);
                        home1.setText("Not Available");
                    }
                    if(store_open_clo.equals("open")){
                        store_open_close.setChecked(true);
                        store1.setText("Open");
                    }else {
                        store_open_close.setChecked(false);
                        store1.setText("Close");
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });


        bulk_service.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){

                    Toast.makeText(Farmer_Profile.this, "Checked", Toast.LENGTH_SHORT).show();
                    HashMap<String,Object> result =new HashMap<>();
                    result.put("Bulk_avail","Yes");
                    reference.child(currentUser.getUid()).updateChildren(result)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(Farmer_Profile.this, "Your Information is Updated", Toast.LENGTH_SHORT).show();
                                    //pd.dismiss();


                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(Farmer_Profile.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });


                }else {

                    Toast.makeText(Farmer_Profile.this, "UnChecked", Toast.LENGTH_SHORT).show();
                    HashMap<String,Object> result =new HashMap<>();
                    result.put("Bulk_avail","No");
                    reference.child(currentUser.getUid()).updateChildren(result)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(Farmer_Profile.this, "Your Information is Updated", Toast.LENGTH_SHORT).show();
                                    //pd.dismiss();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(Farmer_Profile.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });

                }
            }
        });

        home_delivery.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){

                    Toast.makeText(Farmer_Profile.this, "Checked", Toast.LENGTH_SHORT).show();
                    HashMap<String,Object> result =new HashMap<>();
                    result.put("Home_Delivery","Yes");
                    reference.child(currentUser.getUid()).updateChildren(result)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(Farmer_Profile.this, "Your Information is Updated", Toast.LENGTH_SHORT).show();
                                    //pd.dismiss();


                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(Farmer_Profile.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });


                }else {

                    Toast.makeText(Farmer_Profile.this, "UnChecked", Toast.LENGTH_SHORT).show();
                    HashMap<String,Object> result =new HashMap<>();
                    result.put("Home_Delivery","No");
                    reference.child(currentUser.getUid()).updateChildren(result)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(Farmer_Profile.this, "Your Information is Updated", Toast.LENGTH_SHORT).show();
                                    //pd.dismiss();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(Farmer_Profile.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });

                }
            }
        });

        store_open_close.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){

                    Toast.makeText(Farmer_Profile.this, "Checked", Toast.LENGTH_SHORT).show();
                    HashMap<String,Object> result =new HashMap<>();
                    result.put("Store","open");
                    reference.child(currentUser.getUid()).updateChildren(result)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(Farmer_Profile.this, "Your Information is Updated", Toast.LENGTH_SHORT).show();
                                    //pd.dismiss();


                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(Farmer_Profile.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });


                }else {

                    Toast.makeText(Farmer_Profile.this, "UnChecked", Toast.LENGTH_SHORT).show();
                    HashMap<String,Object> result =new HashMap<>();
                    result.put("Store","close");
                    reference.child(currentUser.getUid()).updateChildren(result)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(Farmer_Profile.this, "Your Information is Updated", Toast.LENGTH_SHORT).show();
                                    //pd.dismiss();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(Farmer_Profile.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });

                }
            }
        });

        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data,this);
                StringBuilder stringBuilder = new StringBuilder();
                double latitude = place.getLatLng().latitude;
                double longitude = place.getLatLng().longitude;
                try {
                    Geocoder geocoder;
                    List<Address> addresses;
                    geocoder = new Geocoder(this,Locale.getDefault());
                    addresses = geocoder.getFromLocation(latitude,longitude,1);
                    DealerCommon.new_selling_location = addresses.get(0).getAddressLine(0);
                    myDialog.dismiss();
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void showEditProfileDialog() {
        String option [] ={"Edit Name","Edit City","Edit SubArea","Edit State","Edit UPI ID"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose Action");
        builder.setItems(option, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(which==0)
                {
                    pd.setMessage("Update Name");
                    showNamePhoneAddressUpdateDialog("Farmer_Name");
                }
                else if(which==1)
                {
                    //Edit Name Clicked
                    pd.setMessage("Update City");
                    showNamePhoneAddressUpdateDialog("District_Name");
                }
                else if(which==2)
                {
                    //Edit Phone No Clicked
                    pd.setMessage("Update Sub Area");
                    showNamePhoneAddressUpdateDialog("Tehsil_Name");
                }
                else if(which==3){
                    //Edit Address Clicked
                    pd.setMessage("Edit State");
                    showNamePhoneAddressUpdateDialog("State_Name");
                }
                else if(which==4){
                    //Edit Address Clicked
                    pd.setMessage("Edit UPI ID");
                    showNamePhoneAddressUpdateDialog("UPI_ID");
                }
            }
        });
        builder.create().show();
    }

    private void showNamePhoneAddressUpdateDialog(final String key) {
        AlertDialog.Builder builder =new AlertDialog.Builder(this);
        builder.setTitle("Update "+key);
        LinearLayout linearLayout =new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setPadding(10,10,10,10);
        final EditText editText = new EditText(this);
        editText.setHint("Enter "+key);
        if(key.equals("Farmer_Name")){
            editText.setSingleLine(true);
            editText.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);

        }
        if(key.equals("District_Name")){

            editText.setSingleLine(true);
            editText.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);

        }
        if(key.equals("Tehsil_Name")){
            editText.setSingleLine(true);
            editText.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);

        }
        if(key.equals("State_Name")){
            editText.setSingleLine(true);
            editText.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
        }
        if(key.equals("UPI_ID")){
            editText.setSingleLine(true);
            editText.setInputType(InputType.TYPE_CLASS_TEXT);
        }

        linearLayout.addView(editText);
        builder.setView(linearLayout);
        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String value =editText.getText().toString().trim();
                FirebaseDatabase Fade = FirebaseDatabase.getInstance();
                DatabaseReference FadRefe =Fade.getReference("Farmer_Data");
                if(!TextUtils.isEmpty(value)){

                    pd.show();
                    HashMap<String,Object> result =new HashMap<>();
                    result.put(key ,value);


                    FadRefe.child(currentUser.getUid()).updateChildren(result)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    pd.dismiss();
                                    Toast.makeText(Farmer_Profile.this, "Updated...", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    pd.dismiss();
                                    Toast.makeText(Farmer_Profile.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();

                                }
                            });


                }
                else{
                    Toast.makeText(Farmer_Profile.this, "Please Enter "+key, Toast.LENGTH_SHORT).show();

                }



            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

            }
        });
        builder.create().show();
    }

    private void subscribedToTopic(){
        FirebaseMessaging.getInstance().subscribeToTopic(Constant.FCM_TOPIC)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        //Subscribed Successfully
                        shEditor=sharedPreferences.edit();
                        shEditor.putBoolean("FCM_ENABLED",true);
                        shEditor.apply();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //Error Occurred

                    }
                });
    }

    private void unsubscribedToTopic(){
        FirebaseMessaging.getInstance().unsubscribeFromTopic(Constant.FCM_TOPIC)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        shEditor=sharedPreferences.edit();
                        shEditor.putBoolean("FCM_ENABLED",false);
                        shEditor.apply();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }


    private void chooseNewLanguage() {


        MaterialButton changeLng;
        MaterialCardView marathi,hindi,english;
        RadioGroup radioGroupLanguage;
        String txt;

        change_language.setContentView(R.layout.change_language);
        change_language.setCanceledOnTouchOutside(false);
        radioGroupLanguage = (RadioGroup)change_language.findViewById(R.id.radio);
        marathi = (MaterialCardView)change_language.findViewById(R.id.marathi);
        hindi = (MaterialCardView)change_language.findViewById(R.id.hindi);
        english = (MaterialCardView)change_language.findViewById(R.id.english);

        marathi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLocals("mr");
                change_language.dismiss();
                Intent i2 = new Intent(Farmer_Profile.this, Farmer_Home.class);
                i2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i2);
            }
        });
        hindi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLocals("hi");
                change_language.dismiss();
                Intent i2 = new Intent(Farmer_Profile.this, Farmer_Home.class);
                i2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i2);
            }
        });
        english.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLocals("en");
                change_language.dismiss();
                Intent i2 = new Intent(Farmer_Profile.this, Farmer_Home.class);
                i2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i2);
            }
        });









        change_language.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        change_language.show();



    }

    private void setLocals(String lang) {
        Locale locale =new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale =locale;
        getBaseContext().getResources().updateConfiguration(config,getBaseContext().getResources().getDisplayMetrics());
        SharedPreferences.Editor editor = getSharedPreferences("Setting",MODE_PRIVATE).edit();
        editor.putString("My_Language",lang);
        editor.apply();


    }

    public void onRadioButtonClickedPop(View view) {

        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radioEnglish:
                if (checked)
                    lang ="en";
                break;
            case R.id.radioMarathi:
                if (checked)
                    lang ="mr";
                break;
            case R.id.radioHindi:
                if (checked)
                    lang ="hi";
                break;
        }

    }

    public  void loadLocale(){
        SharedPreferences preferences = getSharedPreferences("Setting", Activity.MODE_PRIVATE);
        String lamguage = preferences.getString("My_Language","");
        setLocals(lamguage);

    }
}