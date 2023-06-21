package xyz.vpscorelim.kisaan.farmer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import xyz.vpscorelim.kisaan.CheckUserProfile;
import xyz.vpscorelim.kisaan.GpsUtils;
import xyz.vpscorelim.kisaan.R;
import xyz.vpscorelim.kisaan.Verify_OTP;
import xyz.vpscorelim.kisaan.customer.Customer_Home;

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
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
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

public class Farmer_popup extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    TextView txtclose,t1,t2,t3,t4;
    EditText state,district,tehsil,selling_location,full_name,upi_id;
    MaterialButton get_exact_location,update_info;
    int PLACE_PICKER_REQUEST = 1;
    public RadioGroup radioGroup;
    String txt;
    final FusedLocationProviderClient[] fusedLocationProviderClient = new FusedLocationProviderClient[1];
    ProgressDialog pd;


    private GoogleApiClient mGoogleApiClient;
    //Firebase ID
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
        setContentView(R.layout.farmer_popup);
        loadLocale();
        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_ALWAYS, WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_ALWAYS);
        getSupportActionBar().hide();
        pd = new ProgressDialog(this);
        myDialog = new Dialog(this);


        new CountDownTimer(10000, 1000) {

            public void onTick(long millisUntilFinished) {
                pd.setMessage("Please Wait");
                pd.show();
                //here you can have your logic to set text to edittext
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



        fusedLocationProviderClient[0] = LocationServices.getFusedLocationProviderClient(Farmer_popup.this);
        if(ActivityCompat.checkSelfPermission(Farmer_popup.this
                , Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED){
            fusedLocationProviderClient[0].getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    Location location = task.getResult();
                    if(location !=null){
                        try {
                            Geocoder geocoder = new Geocoder(Farmer_popup.this,
                                    Locale.getDefault());
                            List<Address> addresses = geocoder.getFromLocation(
                                    location.getLatitude(),location.getLongitude(),1
                            );
                            state.setText(addresses.get(0).getAdminArea());
                            district.setText(addresses.get(0).getLocality());
                            tehsil.setText(addresses.get(0).getSubLocality());
                            FarmerCommon.farmerCity = addresses.get(0).getLocality();
                            FarmerCommon.farmerState = addresses.get(0).getAdminArea();
                            FarmerCommon.farmerSub = addresses.get(0).getSubLocality();
                            FarmerCommon.latitude = addresses.get(0).getLatitude();
                            FarmerCommon.longitude = addresses.get(0).getLongitude();
                            FarmerCommon.selling_location = addresses.get(0).getAddressLine(0);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }else{
            ActivityCompat.requestPermissions(Farmer_popup.this
                    ,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},44);
        }


        //Edit Text
        full_name =findViewById(R.id.full_name);
        state               =findViewById(R.id.state);
        state.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLocd();
            }
        });
        district            =findViewById(R.id.district);
        district.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLocd();
            }
        });




        tehsil              =findViewById(R.id.tehsil);
        upi_id              =findViewById(R.id.upiId);
        tehsil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLocd();
            }
        });
        selling_location    =findViewById(R.id.selling_location);
        selling_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialogBox();
            }
        });

        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this, this)
                .build();


        geoData = FirebaseDatabase.getInstance().getReference("Farmers_Location");
        geoFire = new GeoFire(geoData);

        //Firebase Initialize
        mAuth       = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        firebaseDatabase  = FirebaseDatabase.getInstance();
        databaseReference =firebaseDatabase.getReference("Farmer_Data");


        //Button
        radioGroup          = (RadioGroup) findViewById(R.id.radio);
        get_exact_location  =findViewById(R.id.get_exact_location);
        update_info         =findViewById(R.id.update_info);








        update_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                updateUserInfo();

            }
        });




        getLocd();









        get_exact_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                try {
                    startActivityForResult(builder.build(Farmer_popup.this),PLACE_PICKER_REQUEST);
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });


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
        fusedLocationProviderClient[0] = LocationServices.getFusedLocationProviderClient(Farmer_popup.this);
        if(ActivityCompat.checkSelfPermission(Farmer_popup.this
                , Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED){
            fusedLocationProviderClient[0].getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    Location location = task.getResult();
                    if(location !=null){
                        try {
                            Geocoder geocoder = new Geocoder(Farmer_popup.this,
                                    Locale.getDefault());
                            List<Address> addresses = geocoder.getFromLocation(
                                    location.getLatitude(),location.getLongitude(),1
                            );
                            state.setText(addresses.get(0).getAdminArea());
                            district.setText(addresses.get(0).getLocality());
                            tehsil.setText(addresses.get(0).getSubLocality());
                            FarmerCommon.farmerCity = addresses.get(0).getLocality();
                            FarmerCommon.farmerState = addresses.get(0).getAdminArea();
                            FarmerCommon.farmerSub = addresses.get(0).getSubLocality();
                            FarmerCommon.latitude = addresses.get(0).getLatitude();
                            FarmerCommon.longitude = addresses.get(0).getLongitude();
                            FarmerCommon.selling_location = addresses.get(0).getAddressLine(0);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }else{
            ActivityCompat.requestPermissions(Farmer_popup.this
                    ,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},44);
        }
    }

    public  void loadLocale(){
        SharedPreferences preferences = getSharedPreferences("Setting", Activity.MODE_PRIVATE);
        String lamguage = preferences.getString("My_Language","");
        Locale locale =new Locale(lamguage);
        Locale.setDefault(locale);

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
                state.setText(FarmerCommon.farmerState);
                district.setText(FarmerCommon.farmerCity);
                tehsil.setText(FarmerCommon.farmerSub);
                selling_location.setText(FarmerCommon.selling_location);
                myDialog.dismiss();
            }
        });

        fromMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                try {
                    startActivityForResult(builder.build(Farmer_popup.this),PLACE_PICKER_REQUEST);
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
    public void onBackPressed() {
        // do nothing.
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


    private void updateUserInfo() {
        pd.show();
        pd.setMessage("Please wait...");
        final String farmerName =full_name.getText().toString().trim();
        final String stateName =state.getText().toString().trim();
        final String districtName = district.getText().toString().trim();
        final String tehsilName = tehsil.getText().toString().trim();
        final String sellingLocation=selling_location.getText().toString().trim();
        final String UPI_ID = upi_id.getText().toString().trim();

        if(farmerName.isEmpty())
        {
            pd.dismiss();
            full_name.setError(getString(R.string.name_req));
            full_name.requestFocus();
            return;

        }
        if(stateName.isEmpty())
        {
            pd.dismiss();
            state.setError(getString(R.string.state_req));
            state.requestFocus();
            return;
        }
        if(districtName.isEmpty())
        {
            pd.dismiss();
            district.setError(getString(R.string.dist_req));
            district.requestFocus();
            return;
        }
        if(tehsilName.isEmpty())
        {
            pd.dismiss();
            tehsil.setError(getString(R.string.teh_req));
            tehsil.requestFocus();
            return;
        }
        if(sellingLocation.isEmpty())
        {
            pd.dismiss();
            selling_location.setError(getString(R.string.selling_location_req));
            selling_location.requestFocus();
            return;
        }
        if(txt==null)
        {
            pd.dismiss();
            Toast.makeText(this, R.string.home_del_avail_or_not, Toast.LENGTH_SHORT).show();
        }
        else
        {
            String latitude_one = String.valueOf(latitude);
            String longitude_one = String.valueOf(longitude);

            HashMap<String,Object> result =new HashMap<>();
            result.put("Farmer_Name" ,farmerName);
            result.put("State_Name",stateName);
            result.put("District_Name",districtName);
            result.put("Tehsil_Name",tehsilName);
            result.put("Selling_Location",sellingLocation);
            result.put("latitude",FarmerCommon.latitude);
            result.put("longitude",FarmerCommon.longitude);
            result.put("Bulk_avail",txt);
            result.put("movable","null");
            result.put("Store","open");
            result.put("Home_Delivery","Yes");
            result.put("UPI_ID",UPI_ID);


            databaseReference.child(currentUser.getUid()).updateChildren(result)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            addDriverLocation();
                            Toast.makeText(Farmer_popup.this, "Your Information is Updated", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(Farmer_popup.this, Farmer_Home.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            pd.dismiss();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Farmer_popup.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });


        }


    }

    private void addDriverLocation() {
        pd.dismiss();
        geoFire.setLocation(FirebaseAuth.getInstance().getCurrentUser().getUid(), new GeoLocation(FarmerCommon.latitude, FarmerCommon.longitude), new GeoFire.CompletionListener() {
            @Override
            public void onComplete(String key, DatabaseError error) {
                Toast.makeText(Farmer_popup.this, "Your Location Added", Toast.LENGTH_SHORT).show();
            }
        });


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

                FarmerCommon.latitude = place.getLatLng().latitude;
                FarmerCommon.longitude=place.getLatLng().longitude;


                try {
                    Geocoder geocoder;
                    List<Address> addresses;
                    geocoder = new Geocoder(this,Locale.getDefault());
                    addresses = geocoder.getFromLocation(latitude,longitude,1);
                    String address = addresses.get(0).getAddressLine(0);
                    String city = addresses.get(0).getLocality();
                    String state1 = addresses.get(0).getAdminArea();
                    String sub = addresses.get(0).getSubLocality();
                    state.setText(state1);
                    district.setText(city);
                    tehsil.setText(sub);
                    selling_location.setText(address);
                    myDialog.dismiss();
                    Toast.makeText(this, address, Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}