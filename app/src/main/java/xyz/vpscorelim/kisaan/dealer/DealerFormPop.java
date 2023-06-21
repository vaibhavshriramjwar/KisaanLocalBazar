package xyz.vpscorelim.kisaan.dealer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.google.android.gms.common.ConnectionResult;
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
import xyz.vpscorelim.kisaan.farmer.FarmerCommon;
import xyz.vpscorelim.kisaan.farmer.Farmer_Home;
import xyz.vpscorelim.kisaan.farmer.Farmer_popup;

public class DealerFormPop extends AppCompatActivity {

    EditText store_name,own_name,state_name,city_name,sub_area,complete_ad,upi_id;
    RadioGroup radio;
    MaterialButton update_info;
    String txt;
    int PLACE_PICKER_REQUEST = 1;
    final FusedLocationProviderClient[] fusedLocationProviderClient = new FusedLocationProviderClient[1];

    ProgressDialog pd;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference,geoData ;
    FirebaseAuth mAuth;
    FirebaseUser currentUser;

    double latitude;
    double longitude;


    GeoFire geoFire;

    Dialog myDialog;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dealer_form_pop);
        pd = new ProgressDialog(this);
        loadLocale();
        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_ALWAYS, WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_ALWAYS);
        getSupportActionBar().hide();
        myDialog = new Dialog(this);



        new CountDownTimer(10000, 1000) {

            public void onTick(long millisUntilFinished) {
                pd.setMessage("Please Wait");
                pd.show();
            }

            public void onFinish() {

                pd.dismiss();

            }

        }.start();

        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            Toast.makeText(this, "GPS is Enabled in your devide", Toast.LENGTH_SHORT).show();
        }else{

            showGPSDisabledAlertToUser();

        }


        geoData = FirebaseDatabase.getInstance().getReference("Dealer_Location");
        geoFire = new GeoFire(geoData);

        //Firebase Initialize
        mAuth       = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        firebaseDatabase  = FirebaseDatabase.getInstance();
        databaseReference =firebaseDatabase.getReference("Dealer_Data");

        //Initialize Variable
        upi_id              =findViewById(R.id.upiIDText);
        store_name = findViewById(R.id.store_name);
        own_name   = findViewById(R.id.own_name);
        state_name = findViewById(R.id.state_name);
        state_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLocd();
            }
        });
        city_name  = findViewById(R.id.city_name);
        city_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLocd();
            }
        });
        sub_area   = findViewById(R.id.sub_area);
        sub_area.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLocd();
            }
        });
        complete_ad= findViewById(R.id.complete_ad);
        complete_ad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogBox();
            }
        });




        radio      = findViewById(R.id.radio);
        update_info= findViewById(R.id.update_info);
        update_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateDealerInfo();
            }
        });





                fusedLocationProviderClient[0] = LocationServices.getFusedLocationProviderClient(DealerFormPop.this);
                if(ActivityCompat.checkSelfPermission(DealerFormPop.this
                        , Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED){
                    fusedLocationProviderClient[0].getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                        @Override
                        public void onComplete(@NonNull Task<Location> task) {
                            Location location = task.getResult();
                            if(location !=null){
                                try {
                                    Geocoder geocoder = new Geocoder(DealerFormPop.this,
                                            Locale.getDefault());
                                    List<Address> addresses = geocoder.getFromLocation(
                                            location.getLatitude(),location.getLongitude(),1
                                    );

                                    state_name.setText(addresses.get(0).getAdminArea());
                                    city_name.setText(addresses.get(0).getSubAdminArea());
                                    sub_area.setText(addresses.get(0).getSubLocality());

                                    DealerCommon.loc_state=addresses.get(0).getAdminArea();
                                    DealerCommon.loc_city=addresses.get(0).getSubAdminArea();
                                    DealerCommon.loc_tehsil=addresses.get(0).getSubLocality();
                                    DealerCommon.latitude = addresses.get(0).getLatitude();
                                    DealerCommon.longitude = addresses.get(0).getLongitude();
                                    DealerCommon.store_location = addresses.get(0).getAddressLine(0);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    });
                }else{
                    ActivityCompat.requestPermissions(DealerFormPop.this
                            ,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},44);
                }



        getLocd();

            }

    private void showGPSDisabledAlertToUser() {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("GPS is disabled in your device. Would you like to enable it?")
                .setCancelable(false)
                .setPositiveButton("Goto Settings Page To Enable GPS",
                        new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dialog, int id){
                                Intent callGPSSettingIntent = new Intent(
                                        android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                startActivity(callGPSSettingIntent);
                            }
                        });
        alertDialogBuilder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int id){
                        dialog.cancel();
                    }
                });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();


    }

    private void getLocd() {
        fusedLocationProviderClient[0] = LocationServices.getFusedLocationProviderClient(DealerFormPop.this);
        if(ActivityCompat.checkSelfPermission(DealerFormPop.this
                , Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED){
            fusedLocationProviderClient[0].getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    Location location = task.getResult();
                    if(location !=null){
                        try {
                            Geocoder geocoder = new Geocoder(DealerFormPop.this,
                                    Locale.getDefault());
                            List<Address> addresses = geocoder.getFromLocation(
                                    location.getLatitude(),location.getLongitude(),1
                            );

                            state_name.setText(addresses.get(0).getAdminArea());
                            city_name.setText(addresses.get(0).getSubAdminArea());
                            sub_area.setText(addresses.get(0).getSubLocality());

                            DealerCommon.loc_state=addresses.get(0).getAdminArea();
                            DealerCommon.loc_city=addresses.get(0).getSubAdminArea();
                            DealerCommon.loc_tehsil=addresses.get(0).getSubLocality();
                            DealerCommon.latitude = addresses.get(0).getLatitude();
                            DealerCommon.longitude = addresses.get(0).getLongitude();
                            DealerCommon.store_location = addresses.get(0).getAddressLine(0);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }else{
            ActivityCompat.requestPermissions(DealerFormPop.this
                    ,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},44);
        }
    }


    public  void loadLocale(){
        SharedPreferences preferences = getSharedPreferences("Setting", Activity.MODE_PRIVATE);
        String lamguage = preferences.getString("My_Language","");
        Locale locale =new Locale(lamguage);
        Locale.setDefault(locale);

    }



    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radioYes:
                if (checked)
                    txt ="Yes";
                break;
            case R.id.radioNo:
                if (checked)
                    txt ="No";
                break;
        }
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
                state_name.setText(DealerCommon.loc_state);
                city_name.setText(DealerCommon.loc_city);
                sub_area.setText(DealerCommon.loc_tehsil);
                complete_ad.setText(DealerCommon.store_location);
                myDialog.dismiss();
            }
        });

        fromMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                try {
                    startActivityForResult(builder.build(DealerFormPop.this),PLACE_PICKER_REQUEST);
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
                latitude = place.getLatLng().latitude;
                longitude = place.getLatLng().longitude;


                DealerCommon.latitude = place.getLatLng().latitude;
                DealerCommon.longitude = place.getLatLng().longitude;
                try {
                    Geocoder geocoder;
                    List<Address> addresses;
                    geocoder = new Geocoder(this, Locale.getDefault());
                    addresses = geocoder.getFromLocation(latitude,longitude,1);
                    String address = addresses.get(0).getAddressLine(0);
                    String city = addresses.get(0).getSubAdminArea();
                    String state = addresses.get(0).getAdminArea();
                    String sub = addresses.get(0).getLocality();
                    state_name.setText(state);
                    city_name.setText(city);
                    sub_area.setText(sub);
                    complete_ad.setText(address);
                    myDialog.dismiss();
                    Toast.makeText(this, address, Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }
    }


    private void updateDealerInfo() {

        final String dealerName =own_name.getText().toString().trim();
        final String stateName =state_name.getText().toString().trim();
        final String districtName = city_name.getText().toString().trim();
        final String subArea = sub_area.getText().toString().trim();
        final String shopLocation=complete_ad.getText().toString().trim();
        final String storeName =  store_name.getText().toString().trim();
        final String UPI_ID = upi_id.getText().toString().trim();

        if(dealerName.isEmpty())
        {
            //pd.dismiss();
            own_name.setError(getString(R.string.name_req));
            own_name.requestFocus();
            return;

        }
        if(stateName.isEmpty())
        {
            //pd.dismiss();
            state_name.setError(getString(R.string.state_req));
            state_name.requestFocus();
            return;
        }
        if(districtName.isEmpty())
        {
            //pd.dismiss();
            city_name.setError(getString(R.string.dist_req));
            city_name.requestFocus();
            return;
        }
        if(subArea.isEmpty())
        {
            //pd.dismiss();
            sub_area.setError(getString(R.string.teh_req));
            sub_area.requestFocus();
            return;
        }
        if(shopLocation.isEmpty())
        {
            //pd.dismiss();
            complete_ad.setError(getString(R.string.add_shop_location));
            complete_ad.requestFocus();
            return;
        }
        if(storeName.isEmpty())
        {
            //pd.dismiss();
            store_name.setError(getString(R.string.add_your_store_name));
            store_name.requestFocus();
            return;
        }
        if(txt==null)
        {
            //pd.dismiss();
            Toast.makeText(this, R.string.home_lp, Toast.LENGTH_SHORT).show();
        }
        else
        {
            String latitude_one = String.valueOf(DealerCommon.latitude);
            String longitude_one = String.valueOf(DealerCommon.longitude);

            HashMap<String,Object> result =new HashMap<>();
            result.put("dealerName",dealerName);
            result.put("storeName",storeName);
            result.put("stateName",stateName);
            result.put("districtName",districtName);
            result.put("subArea",subArea);
            result.put("shopLocation",shopLocation);
            result.put("latitude",latitude_one);
            result.put("longitude",longitude_one);
            result.put("Home_Delivery",txt);
            result.put("Store","open");
            result.put("UPI_ID",UPI_ID);


            databaseReference.child(currentUser.getUid()).updateChildren(result)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            addDealerLocation();
                            Toast.makeText(DealerFormPop.this, "Your Information is Updated", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(DealerFormPop.this, Dealer_Home.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            //pd.dismiss();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(DealerFormPop.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });


        }





    }

    @Override
    public void onBackPressed() {
        // do nothing.
    }

    private void addDealerLocation() {
        //pd.dismiss();
        geoFire.setLocation(FirebaseAuth.getInstance().getCurrentUser().getUid(), new GeoLocation(DealerCommon.latitude, DealerCommon.longitude), new GeoFire.CompletionListener() {
            @Override
            public void onComplete(String key, DatabaseError error) {
                Toast.makeText(DealerFormPop.this, "Your Location Added", Toast.LENGTH_SHORT).show();
            }
        });
    }

}