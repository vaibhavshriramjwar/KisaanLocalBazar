package xyz.vpscorelim.kisaan.customer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import xyz.vpscorelim.kisaan.KisanLocalBazar;
import xyz.vpscorelim.kisaan.Log_In;
import xyz.vpscorelim.kisaan.R;
import xyz.vpscorelim.kisaan.Verify_OTP;
import xyz.vpscorelim.kisaan.adapter.HomePageFarmerList;
import xyz.vpscorelim.kisaan.adapter.ModelFarmerData;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class Near_by_farmers extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener
{

    private GoogleMap mMap;
    Marker marker;


    public static double latitude;
    public static double longitude;


    boolean isFarmerFound = false;
    String farmerId="";
    int radius = 100;


    final FusedLocationProviderClient[] fusedLocationProviderClient = new FusedLocationProviderClient[1];

    RecyclerView farmer_list,farmer_list_ar;
    RecyclerView.LayoutManager layoutManager,layoutManager1;
    FirebaseDatabase database;
    DatabaseReference reference;
    HomePageFarmerList homePageFarmerList;
    List<ModelFarmerData> modelFarmerData;
    FirebaseAuth mAuth;
    FirebaseUser currentUser;


    ImageView back;

    int radius_inc = 15;


    MaterialCardView area2,area1,area3,area4,area5;
    TextView city,cut,teh;
    KisanLocalBazar kisanLocalBazar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.near_by_farmers);
        kisanLocalBazar = KisanLocalBazar.getzInstance();
        loadLocale();
        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_ALWAYS, WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_ALWAYS);
        //getSupportActionBar().hide();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.

        farmer_list = findViewById(R.id.farmer_list);
        farmer_list.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        farmer_list.setLayoutManager(layoutManager);
        farmer_list.setHasFixedSize(true);

        back= findViewById(R.id.close);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        farmer_list_ar = findViewById(R.id.farmer_list_ar);
        farmer_list_ar.setHasFixedSize(true);
        layoutManager1 = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        farmer_list_ar.setLayoutManager(layoutManager1);
        farmer_list_ar.setHasFixedSize(true);


        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        cut = findViewById(R.id.cut);


        area3 = findViewById(R.id.area3);
        area3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radius = 100;
                radius_inc =14;
                displayMyLocation();
            }
        });
        area4 = findViewById(R.id.area4);
        area4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radius = 200;
                radius_inc =12;
                displayMyLocation();
            }
        });
        area5 = findViewById(R.id.area5);
        area5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radius = 300;
                radius_inc =10;
                displayMyLocation();

            }
        });




        area1 = findViewById(R.id.area1);
        teh = findViewById(R.id.teh);


        area2 = findViewById(R.id.area2);
        city = findViewById(R.id.city);

        database  = FirebaseDatabase.getInstance();
        reference = database.getReference("Customer_Data");
        Query query = reference.orderByChild("phoneNumber").equalTo(currentUser.getPhoneNumber());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds :snapshot.getChildren()){
                    String customer_City_name =""+ds.child("District_Name").getValue();
                    String subArea            =""+ds.child("Tehsil_Name").getValue();

                    city.setText(customer_City_name);
                    teh.setText(subArea);

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        area2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadAreaFarmer();
            }
        });


        area1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadSubAreaFarmer();
            }
        });







        BottomNavigationView bottomNavigationView = findViewById(R.id.customer_bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.customer_search);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.customer_dashboard:
                        startActivity(new Intent(getApplicationContext(), Customer_Home.class));
                        Animatoo.animateSlideRight(Near_by_farmers.this);
                        return true;
                    case R.id.customer_profile:
                        startActivity(new Intent(getApplicationContext(), Customer_Profile.class));
                        Animatoo.animateSlideLeft(Near_by_farmers.this);
                        return true;
                    case R.id.customer_search:
                        return true;
                    case R.id.customer_order:
                        startActivity(new Intent(getApplicationContext(), CustomerMyOrder.class));
                        Animatoo.animateSlideLeft(Near_by_farmers.this);
                        return true;
                    case R.id.customer_cart:
                        startActivity(new Intent(getApplicationContext(), ViewMyCart.class));
                        Animatoo.animateSlideLeft(Near_by_farmers.this);
                        return true;
                }
                return false;
            }
        });



        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);



    }

    private void loadSubAreaFarmer() {

        farmer_list.setVisibility(View.GONE);
        farmer_list_ar.setVisibility(View.VISIBLE);
        cut.setVisibility(View.VISIBLE);
        cut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                farmer_list_ar.setVisibility(View.GONE);
                cut.setVisibility(View.GONE);
            }
        });
        database  = FirebaseDatabase.getInstance();
        reference = database.getReference("Customer_Data");
        Query query = reference.orderByChild("phoneNumber").equalTo(currentUser.getPhoneNumber());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds :snapshot.getChildren()){
                    String customer_City_name =""+ds.child("District_Name").getValue();
                    String subArea            =""+ds.child("Tehsil_Name").getValue();

                    //loadAllFarmerArea(customer_City_name);
                    loadNearByTahsil(subArea);



                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
    private void loadNearByTahsil(String subArea) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Farmer_Data");
        ref.orderByChild("Tehsil_Name").equalTo(subArea)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        modelFarmerData = new ArrayList<>();
                        for (DataSnapshot pvostsnap : snapshot.getChildren()) {
                            ModelFarmerData post = pvostsnap.getValue(ModelFarmerData.class);
                            //farmerProductData.clear();
                            modelFarmerData.add(post);
                        }
                        homePageFarmerList = new HomePageFarmerList(Near_by_farmers.this, modelFarmerData);
                        farmer_list_ar.setAdapter(homePageFarmerList);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
    private void loadAreaFarmer() {
        farmer_list_ar.setVisibility(View.GONE);
        farmer_list.setVisibility(View.VISIBLE);
        cut.setVisibility(View.VISIBLE);
        cut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                farmer_list.setVisibility(View.GONE);
                cut.setVisibility(View.GONE);
            }
        });
        database  = FirebaseDatabase.getInstance();
        reference = database.getReference("Customer_Data");
        Query query = reference.orderByChild("phoneNumber").equalTo(currentUser.getPhoneNumber());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds :snapshot.getChildren()){
                    String customer_City_name =""+ds.child("District_Name").getValue();
                    String subArea            =""+ds.child("Tehsil_Name").getValue();

                    loadAllFarmerArea(customer_City_name);
                    //loadNearByTahsil(subArea);



                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    private void loadAllFarmerArea(String customer_city_name) {

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Farmer_Data");
        ref.orderByChild("District_Name").equalTo(customer_city_name)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        modelFarmerData = new ArrayList<>();
                        for (DataSnapshot pvostsnap : snapshot.getChildren()) {
                            ModelFarmerData post = pvostsnap.getValue(ModelFarmerData.class);
                            //farmerProductData.clear();
                            modelFarmerData.add(post);
                        }
                        homePageFarmerList = new HomePageFarmerList(Near_by_farmers.this, modelFarmerData);
                        farmer_list.setAdapter(homePageFarmerList);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


        try{
            boolean isSuccess = mMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(this,R.raw.uber_style_map)
            );
            if(!isSuccess){
                //Log.e("ERROR","Map style load failed!!");
            }
        }catch (Resources.NotFoundException ex){
            ex.printStackTrace();
        }




        displayMyLocation();



    }

    private void displayMyLocation() {


        Timer timer1 =  new Timer();
        timer1.schedule(new TimerTask() {
            @Override
            public void run() {

                fusedLocationProviderClient[0] = LocationServices.getFusedLocationProviderClient(Near_by_farmers.this);
                if(ActivityCompat.checkSelfPermission(Near_by_farmers.this
                        , Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED){



                    fusedLocationProviderClient[0].getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                        @Override
                        public void onComplete(@NonNull Task<Location> task) {

                            Location location = task.getResult();
                            if(location !=null){


                                try {
                                    Geocoder geocoder = new Geocoder(Near_by_farmers.this,
                                            Locale.getDefault());
                                    List<Address> addresses = geocoder.getFromLocation(
                                            location.getLatitude(),location.getLongitude(),1
                                    );

                                    latitude = addresses.get(0).getLatitude();
                                    longitude = addresses.get(0).getLongitude();
                                    FirebaseAuth mAuth;
                                    FirebaseUser currentUser;
                                    mAuth = FirebaseAuth.getInstance();
                                    currentUser = mAuth.getCurrentUser();
                                    String phone = currentUser.getPhoneNumber();

                                    mMap.addMarker(new MarkerOptions().position(
                                            new LatLng(latitude,longitude)
                                    ).title("You")
                                            .snippet(phone));
                                    //Toast.makeText(Near_by_farmers.this, ""+radius_inc, Toast.LENGTH_SHORT).show();
                                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude,longitude),radius_inc));

                                    FindFarmer(latitude,longitude);


                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }

                        }
                    });
                }else{
                    ActivityCompat.requestPermissions(Near_by_farmers.this
                            ,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},44);
                }

            }


        },0,500000);
  }

    private void FindFarmer(double latitude, double longitude) {


        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Farmers_Location");
        GeoFire geoFire = new GeoFire(databaseReference);
        final GeoQuery geoQuery = geoFire.queryAtLocation(new GeoLocation(latitude,longitude),
                radius);
        geoQuery.removeAllListeners();
        final FirebaseUser FireUser = FirebaseAuth.getInstance().getCurrentUser();
        geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
            @Override
            public void onKeyEntered(final String key, final GeoLocation location)
            {


                FirebaseDatabase.getInstance().getReference().child("Farmer_Data").orderByChild("uid").equalTo(key).
                        addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                                for (final DataSnapshot pvostsnap : dataSnapshot.getChildren()) {
                                    final GetFarmer getFarmer =pvostsnap.getValue(GetFarmer.class);

                                    int height = 150;
                                    int width = 150;
                                    Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.icon23);
                                    Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);
                                    BitmapDescriptor smallMarkerIcon = BitmapDescriptorFactory.fromBitmap(smallMarker);

                                    assert getFarmer != null;
                                    mMap.addMarker(new MarkerOptions().position(new LatLng(location.latitude,location.longitude))
                                            .flat(true)
                                            .title(getFarmer.getFarmer_Name())
                                            .snippet(getFarmer.getPhoneNumber())
                                            .icon(smallMarkerIcon)



                                    );


                                    mMap.setInfoWindowAdapter(new custome_info_win(Near_by_farmers.this));


                                    mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                                        @Override
                                        public void onInfoWindowClick(Marker marker) {
                                            FirebaseAuth mAuth;
                                            FirebaseUser currentUser;
                                            mAuth = FirebaseAuth.getInstance();
                                            currentUser = mAuth.getCurrentUser();
                                            String phone = currentUser.getPhoneNumber();

                                                String farmer_name = marker.getTitle();
                                                String phone_number = marker.getSnippet();
                                            if(!phone_number.equals(phone)){
                                                //Toast.makeText(Near_by_farmers.this, ""+phone_number, Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(Near_by_farmers.this, His_Farmer_Profile.class);
                                                intent.putExtra("phoneNumber",phone_number);
                                                startActivity(intent);

                                            }else {
                                                Toast.makeText(Near_by_farmers.this, "Its You ", Toast.LENGTH_SHORT).show();

                                            }

                                        }
                                    });
//
//                                    mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
//                                        @Override
//                                        public boolean onMarkerClick(Marker marker) {
//                                            String number = marker.getSnippet();
//                                            Toast.makeText(Near_by_farmers.this, ""+marker.getSnippet(), Toast.LENGTH_SHORT).show();
//                                            Intent intent = new Intent(Near_by_farmers.this, Customer_Profile.class);
//                                            intent.putExtra("phoneNumber",number);
//                                            startActivity(intent);
//
//                                            return false;
//                                        }
//                                    });
                                }



                            }


                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                Toast.makeText(Near_by_farmers.this, ""+databaseError.getMessage(), Toast.LENGTH_SHORT).show();

                            }
                        });

            }


            @Override
            public void onKeyExited(String key) {

            }

            @Override
            public void onKeyMoved(String key, GeoLocation location) {

            }

            @Override
            public void onGeoQueryReady() {

            }

            @Override
            public void onGeoQueryError(DatabaseError error) {

            }
        });


    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {

    }



    public  void loadLocale(){
        SharedPreferences preferences = getSharedPreferences("Setting", Activity.MODE_PRIVATE);
        String lamguage = preferences.getString("My_Language","");
        Locale locale =new Locale(lamguage);
        Locale.setDefault(locale);

    }
}




