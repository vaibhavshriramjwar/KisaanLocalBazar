package xyz.vpscorelim.kisaan.customer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import cn.pedant.SweetAlert.SweetAlertDialog;
import xyz.vpscorelim.kisaan.Log_In;
import xyz.vpscorelim.kisaan.R;
import xyz.vpscorelim.kisaan.dealer.DealerFormPop;
import xyz.vpscorelim.kisaan.dealer.Dealer_Home;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
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
import android.widget.Toast;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class Customer_Info_Form extends AppCompatActivity {

    EditText full_name,state,district,tehsil,complete_address;
    MaterialButton update_info;
    ProgressDialog pd;

    double latitude;
    double longitude;


    final FusedLocationProviderClient[] fusedLocationProviderClient = new FusedLocationProviderClient[1];


    //Firebase ID
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference ;
    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customer_info_form);
        loadLocale();
        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_ALWAYS, WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_ALWAYS);
        getSupportActionBar().hide();


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

        loc();



        full_name        = findViewById(R.id.full_name);
        state            = findViewById(R.id.state);
        state.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                locd();
            }
        });
        district         = findViewById(R.id.district);
        district.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                locd();
            }
        });
        tehsil           = findViewById(R.id.tehsil);
        tehsil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                locd();
            }
        });
        complete_address = findViewById(R.id.complete_address);
        complete_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                locd();
            }
        });
        update_info      = findViewById(R.id.update_info);
        pd = new ProgressDialog(this);


        //Firebase Initialize
        mAuth       = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        firebaseDatabase  = FirebaseDatabase.getInstance();
        databaseReference =firebaseDatabase.getReference("Customer_Data");


        update_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUserInfo();

            }
        });




        locd();

        Timer timer1 =  new Timer();
        timer1.schedule(new TimerTask() {
            @Override
            public void run() {
                fusedLocationProviderClient[0] = LocationServices.getFusedLocationProviderClient(Customer_Info_Form.this);
                if(ActivityCompat.checkSelfPermission(Customer_Info_Form.this
                        , Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED){
                    fusedLocationProviderClient[0].getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                        @Override
                        public void onComplete(@NonNull Task<Location> task) {

                            Location location = task.getResult();
                            if(location !=null){


                                try {
                                    Geocoder geocoder = new Geocoder(Customer_Info_Form.this,
                                            Locale.getDefault());
                                    List<Address> addresses = geocoder.getFromLocation(
                                            location.getLatitude(),location.getLongitude(),1
                                    );

                                    latitude = addresses.get(0).getLatitude();
                                    longitude = addresses.get(0).getLongitude();
                                    state.setText(addresses.get(0).getAdminArea());
                                    district.setText(addresses.get(0).getLocality());
                                    tehsil.setText(addresses.get(0).getSubLocality());
                                    complete_address.setText(addresses.get(0).getAddressLine(0));

                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }

                        }
                    });
                }else{
                    ActivityCompat.requestPermissions(Customer_Info_Form.this
                            ,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},44);
                }
            }


        },0,2000);
    }

    private void locd() {


        fusedLocationProviderClient[0] = LocationServices.getFusedLocationProviderClient(Customer_Info_Form.this);
        if(ActivityCompat.checkSelfPermission(Customer_Info_Form.this
                , Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED){
            fusedLocationProviderClient[0].getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {

                    Location location = task.getResult();
                    if(location !=null){


                        try {
                            Geocoder geocoder = new Geocoder(Customer_Info_Form.this,
                                    Locale.getDefault());
                            List<Address> addresses = geocoder.getFromLocation(
                                    location.getLatitude(),location.getLongitude(),1
                            );

                            latitude = addresses.get(0).getLatitude();
                            longitude = addresses.get(0).getLongitude();
                            state.setText(addresses.get(0).getAdminArea());
                            district.setText(addresses.get(0).getLocality());
                            tehsil.setText(addresses.get(0).getSubLocality());
                            complete_address.setText(addresses.get(0).getAddressLine(0));

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                }
            });
        }else{
            ActivityCompat.requestPermissions(Customer_Info_Form.this
                    ,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},44);
        }


    }

    private void loc() {


        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            Toast.makeText(this, "GPS is Enabled in your devide", Toast.LENGTH_SHORT).show();
        }else{
            showGPSDisabledAlertToUser();
        }

    }

    private void showGPSDisabledAlertToUser() {
        new SweetAlertDialog(Customer_Info_Form.this, SweetAlertDialog.CUSTOM_IMAGE_TYPE)
                .setTitleText("GPS is disabled!")
                .setContentText("Please enable GPS ")
                .setCustomImage(R.drawable.loc)
                .setConfirmText("Enable Now")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        Intent callGPSSettingIntent = new Intent(
                                android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(callGPSSettingIntent);
                    }
                })
                .show();
    }


    @Override
    public void onBackPressed() {
        // do nothing.
    }

    private void updateUserInfo() {
        pd.show();
        final String customerName       =full_name.getText().toString().trim();
        final String stateName          =state.getText().toString().trim();
        final String districtName       = district.getText().toString().trim();
        final String tehsilName         = tehsil.getText().toString().trim();
        final String current_address    =complete_address.getText().toString().trim();

        if(customerName.isEmpty())
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
        if(current_address.isEmpty())
        {
            pd.dismiss();
            complete_address.setError(getString(R.string.add_req));
            complete_address.requestFocus();
            return;
        }
        else
        {

            HashMap<String,Object> result =new HashMap<>();
            result.put("Customer_name" ,customerName);
            result.put("State_Name",stateName);
            result.put("District_Name",districtName);
            result.put("Tehsil_Name",tehsilName);
            result.put("Current_Location",current_address);
            result.put("latitude",latitude);
            result.put("longitude",longitude);

            databaseReference.child(currentUser.getUid()).updateChildren(result)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(Customer_Info_Form.this, R.string.info_uodates, Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(Customer_Info_Form.this, Customer_Home.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            pd.show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Customer_Info_Form.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });


        }

        }

    public  void loadLocale(){
        SharedPreferences preferences = getSharedPreferences("Setting", Activity.MODE_PRIVATE);
        String lamguage = preferences.getString("My_Language","");
        Locale locale =new Locale(lamguage);
        Locale.setDefault(locale);

    }


    }
