package xyz.vpscorelim.kisaan.farmer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
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
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import xyz.vpscorelim.kisaan.R;

public class change_Location extends AppCompatActivity {

    Dialog myDialog;
    EditText new_loc;
    int PLACE_PICKER_REQUEST = 1;
    MaterialButton update;


    public FirebaseAuth firebaseAuth;
    public FirebaseUser currentUser;
    public FirebaseDatabase firebaseDatabase;
    public DatabaseReference reference,geoData;
    String farmer_uid;
    GeoFire geoFire;

    ImageView back;


    final FusedLocationProviderClient[] fusedLocationProviderClient = new FusedLocationProviderClient[1];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change__location);
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

        myDialog = new Dialog(this);
        //Firebase
        firebaseAuth= FirebaseAuth.getInstance();
        currentUser =firebaseAuth.getCurrentUser();
        firebaseDatabase =FirebaseDatabase.getInstance();
        reference =firebaseDatabase.getReference("Farmer_Data");
        farmer_uid =  currentUser.getUid();
        geoData = FirebaseDatabase.getInstance().getReference("Farmers_Location");
        geoFire = new GeoFire(geoData);

        getLocd();

        new_loc    =findViewById(R.id.new_loc);
        new_loc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialogBox();
            }
        });
        update = findViewById(R.id.update_loc);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update_data();
            }
        });





                fusedLocationProviderClient[0] = LocationServices.getFusedLocationProviderClient(change_Location.this);
                if(ActivityCompat.checkSelfPermission(change_Location.this
                        , Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED){
                    fusedLocationProviderClient[0].getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                        @Override
                        public void onComplete(@NonNull Task<Location> task) {
                            Location location = task.getResult();
                            if(location !=null){
                                try {
                                    Geocoder geocoder = new Geocoder(change_Location.this,
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
                    ActivityCompat.requestPermissions(change_Location.this
                            ,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},44);
                }





            }

    private void getLocd() {
        fusedLocationProviderClient[0] = LocationServices.getFusedLocationProviderClient(change_Location.this);
        if(ActivityCompat.checkSelfPermission(change_Location.this
                , Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED){
            fusedLocationProviderClient[0].getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    Location location = task.getResult();
                    if(location !=null){
                        try {
                            Geocoder geocoder = new Geocoder(change_Location.this,
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
            ActivityCompat.requestPermissions(change_Location.this
                    ,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},44);
        }
    }


    public  void loadLocale(){
        SharedPreferences preferences = getSharedPreferences("Setting", Activity.MODE_PRIVATE);
        String lamguage = preferences.getString("My_Language","");
        Locale locale =new Locale(lamguage);
        Locale.setDefault(locale);

    }


    private void update_data() {
        String new_loc1 = new_loc.getText().toString();
        if(new_loc1.isEmpty()){
        }else{
            HashMap<String,Object> result =new HashMap<>();
            result.put("Selling_Location",FarmerCommon.new_selling_location);
            result.put("latitude",FarmerCommon.new_latitude);
            result.put("longitude",FarmerCommon.new_longitude);
            reference.child(currentUser.getUid()).updateChildren(result)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            addDriverLocation();
                            Toast.makeText(change_Location.this, "Your Information is Updated", Toast.LENGTH_SHORT).show();
                            //pd.dismiss();
                            startActivity(new Intent(change_Location.this,Farmer_Profile.class));
                            overridePendingTransition(0,0);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(change_Location.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });



        }



    }

    private void addDriverLocation() {
        geoFire.setLocation(FirebaseAuth.getInstance().getCurrentUser().getUid(), new GeoLocation(FarmerCommon.new_latitude, FarmerCommon.new_longitude), new GeoFire.CompletionListener() {
            @Override
            public void onComplete(String key, DatabaseError error) {
                Toast.makeText(change_Location.this, "Your Location Added", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void dialogBox() {
        MaterialButton getLocation,fromMap;
        myDialog.setContentView(R.layout.chooselocation);
        myDialog.setCanceledOnTouchOutside(false);
        getLocation=(MaterialButton)myDialog.findViewById(R.id.use_geo_loc);
        fromMap=(MaterialButton)myDialog.findViewById(R.id.use_geo_map);
        getLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLocd();
                new_loc.setText(FarmerCommon.new_selling_location);
                myDialog.dismiss();
            }
        });

        fromMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                try {
                    startActivityForResult(builder.build(change_Location.this),PLACE_PICKER_REQUEST);
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });


        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data,this);
                StringBuilder stringBuilder = new StringBuilder();
                double latitude = place.getLatLng().latitude;
                double longitude = place.getLatLng().longitude;
                try {
                    Geocoder geocoder;
                    List<Address> addresses;
                    geocoder = new Geocoder(this, Locale.getDefault());
                    addresses = geocoder.getFromLocation(latitude,longitude,1);
                    String address = addresses.get(0).getAddressLine(0);
                    FarmerCommon.new_latitude = latitude;
                    FarmerCommon.new_longitude = longitude;
                    FarmerCommon.new_selling_location = addresses.get(0).getAddressLine(0);
                    new_loc.setText(address);
                    myDialog.dismiss();
                    Toast.makeText(this, FarmerCommon.new_selling_location, Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }
    }


}