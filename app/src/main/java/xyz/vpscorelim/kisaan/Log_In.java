package xyz.vpscorelim.kisaan;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;

import java.util.Locale;

public class Log_In extends AppCompatActivity {

    MaterialButton b1;
    public RadioGroup radioGroup;
    String txt;
    EditText editText;
    TextView login_desc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadLocale();
        setContentView(R.layout.log_in);
        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getSupportActionBar().hide();
        login_desc = findViewById(R.id.login_desc);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        radioGroup = (RadioGroup) findViewById(R.id.radio);
        editText = findViewById(R.id.phone_number_text);

        permission();



        b1= findViewById(R.id.generate_btn);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                value();
            }
        });


    }

    private void permission() {
        if(ActivityCompat.checkSelfPermission(Log_In.this
                , Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED)
        {

            Toast.makeText(this, "Log In", Toast.LENGTH_SHORT).show();

        }
        else
        {
            ActivityCompat.requestPermissions(Log_In.this
                    ,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},44);


        }
    }


    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radioFarmer:
                if (checked)
                    txt ="farmer";
                    break;
            case R.id.radioCustomer:
                if (checked)
                    txt ="customer";
                    break;
            case R.id.radioDealer:
                if (checked)
                    txt ="dealer";
                break;
        }
    }

    private void value() {
        String number =editText.getText().toString().trim();
        if(number.isEmpty()|| number.length()<10){
           editText.setError(getString(R.string.phone_number_required));
           editText.requestFocus();
           return;

        } if(txt==null){
            Toast.makeText(this, R.string.please_choose_role, Toast.LENGTH_SHORT).show();
        }
        else{
            String phoneNumber ="+91" + number;
            Intent intent = new Intent(Log_In.this,Verify_OTP.class);
            intent.putExtra("phoneNumber",phoneNumber);
            intent.putExtra("userRole",txt);
            startActivity(intent);
        }
    }

    public  void loadLocale(){
        SharedPreferences preferences = getSharedPreferences("Setting", Activity.MODE_PRIVATE);
        String lamguage = preferences.getString("My_Language","");
        Locale locale =new Locale(lamguage);
        Locale.setDefault(locale);

    }




}