package xyz.vpscorelim.kisaan.Weather;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.tabs.TabLayout;
import com.pnikosis.materialishprogress.ProgressWheel;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import xyz.vpscorelim.kisaan.R;
import xyz.vpscorelim.kisaan.Weather.Common.Common;
import xyz.vpscorelim.kisaan.Weather.Model.WeatherResult;
import xyz.vpscorelim.kisaan.Weather.Retrofit.IOpenWeatherMap;
import xyz.vpscorelim.kisaan.Weather.Retrofit.RetrofitClient;

public class WeatherActivity extends AppCompatActivity {


    private MaterialToolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private CoordinatorLayout coordinatorLayout;

    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationCallback locationCallback;
    private LocationRequest locationRequest;

    ImageView img_weather;
    TextView txt_city_name,txt_temperature,txt_description,txt_date_time,txt_wind,txt_pressure,txt_humidity,txt_sunrise,txt_sunset,txt_geo_cords;

    LinearLayout weather_panel;
    ProgressWheel progressWheel;

    CompositeDisposable compositeDisposable;
    IOpenWeatherMap mService;
    double latitude;
    double longitude;


    MaterialButton refresh;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_ALWAYS, WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_ALWAYS);
        getSupportActionBar().hide();
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_LAYOUT_FLAGS);

        compositeDisposable = new CompositeDisposable();
        Retrofit retrofit = RetrofitClient.getInstance();
        mService = retrofit.create(IOpenWeatherMap.class);

        refresh = findViewById(R.id.refresh);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getWeatherInformation();
            }
        });


        //Init
        img_weather     = findViewById(R.id.img_weather);
        txt_city_name   = findViewById(R.id.txt_city_name);
        txt_temperature = findViewById(R.id.txt_temperature);
        txt_description = findViewById(R.id.txt_description);
        txt_date_time   = findViewById(R.id.txt_date_time);
        txt_wind        = findViewById(R.id.txt_wind);
        txt_pressure    = findViewById(R.id.txt_pressure);
        txt_humidity    = findViewById(R.id.txt_humidity);
        txt_sunrise     = findViewById(R.id.txt_sunrise);
        txt_sunset      = findViewById(R.id.txt_sunset);
        txt_geo_cords   = findViewById(R.id.txt_geo_cords);
        progressWheel   = findViewById(R.id.loading);

        weather_panel   = findViewById(R.id.weather_panel);



        Timer timer1 =  new Timer();
        timer1.schedule(new TimerTask() {
            @Override
            public void run() {
                getCurrentLocation();
            }
        },0,2000);


        getWeatherInformation();













//        Dexter.withActivity(this)
//                .withPermissions(Manifest.permission.ACCESS_COARSE_LOCATION,
//                        Manifest.permission.ACCESS_FINE_LOCATION)
//                .withListener(new MultiplePermissionsListener() {
//                    @Override
//                    public void onPermissionsChecked(MultiplePermissionsReport report) {
//                        if (report.areAllPermissionsGranted()) {
//
//                            buildLocationRequest();
//                            buildLocationCallBack();
//
//                            if (ActivityCompat.checkSelfPermission(WeatherActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(WeatherActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//
//                                return;
//                            }
//                            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(WeatherActivity.this);
//                            fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
//
//                        }
//
//                    }
//
//                    @Override
//                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
//
//                        Snackbar.make(coordinatorLayout,"Permission Denied",Snackbar.LENGTH_LONG)
//                                .show();
//                    }
//                }).check();




    }

    private void getCurrentLocation() {

                fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(WeatherActivity.this);
                if(ActivityCompat.checkSelfPermission(WeatherActivity.this
                        , Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED){



                    fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                        @Override
                        public void onComplete(@NonNull Task<Location> task) {

                            Location location = task.getResult();
                            if(location !=null){


                                try {
                                    Geocoder geocoder = new Geocoder(WeatherActivity.this,
                                            Locale.getDefault());
                                    List<Address> addresses = geocoder.getFromLocation(
                                            location.getLatitude(),location.getLongitude(),1
                                    );

                                    latitude = addresses.get(0).getLatitude();
                                    longitude = addresses.get(0).getLongitude();
                                    Common.latitude = latitude;
                                    Common.longitude = longitude;


                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }

                        }
                    });
                }else{
                    ActivityCompat.requestPermissions(WeatherActivity.this
                            ,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},44);
                }

    }


    private void buildLocationCallBack() {
        locationCallback = new LocationCallback(){
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);

                Common.current_location = locationResult.getLastLocation();



                Log.d("Location",locationResult.getLastLocation().getLatitude()+"/"+locationResult.getLastLocation().getLongitude());


            }
        };

    }

    private void buildLocationRequest() {
        locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setSmallestDisplacement(10.0f);
    }


    private void getWeatherInformation() {
                compositeDisposable.add(mService.getWeatherByLatLng(String.valueOf(Common.latitude),
                        String.valueOf(Common.longitude),
                        Common.APP_ID,
                        "metric")
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<WeatherResult>() {
                            @Override
                            public void accept(WeatherResult weatherResult) throws Exception {

                                Glide.with(WeatherActivity.this).load(new StringBuilder("https://openweathermap.org/img/wn/")
                                        .append(weatherResult.getWeather().get(0).getIcon()).append(".png").toString()).into(img_weather);
                                if(weatherResult.getName().equals("Globe"))
                                {
                                    txt_city_name.setText("Click On Refresh");
                                }
                                txt_city_name.setText(weatherResult.getName());

                                txt_description.setText(new StringBuilder("Weather in ").append(weatherResult.getName()).toString());
                                txt_temperature.setText(new StringBuilder(String.valueOf(weatherResult.getMain().getTemp())).append("°C").toString());
                                txt_date_time.setText(Common.convertUnixToDate(weatherResult.getDt()));
                                txt_pressure.setText(new StringBuilder(String.valueOf(weatherResult.getMain().getPressure())).append("hpa").toString());
                                txt_humidity.setText(new StringBuilder(String.valueOf(weatherResult.getMain().getHumidity())).append("%").toString());
                                txt_sunrise.setText(Common.convertUnixToHour(weatherResult.getSys().getSunrise()));
                                txt_sunset.setText(Common.convertUnixToHour(weatherResult.getSys().getSunset()));
                                txt_geo_cords.setText(new StringBuilder(weatherResult.getCoord().toString()).toString());
                                weather_panel.setVisibility(View.VISIBLE);
                                progressWheel.setVisibility(View.GONE);

                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                Toast.makeText(WeatherActivity.this, "vb"+throwable.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        })
                );





    }
}