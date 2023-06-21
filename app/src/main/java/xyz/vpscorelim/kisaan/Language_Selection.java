package xyz.vpscorelim.kisaan;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Locale;

public class Language_Selection extends AppCompatActivity {

    MaterialButton b1;

    MaterialCardView marathi,hindi,english;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadLocale();

        setContentView(R.layout.language__selection);
        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getSupportActionBar().hide();
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);









        marathi =findViewById(R.id.marathi);
        
        hindi   = findViewById(R.id.hindi);
        english = findViewById(R.id.english);


        marathi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setLocals("mr");
                Intent i2 = new Intent(Language_Selection.this, Log_In.class);
                startActivity(i2);


            }
        });

        hindi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLocals("hi");
                Intent i2 = new Intent(Language_Selection.this, Log_In.class);
                startActivity(i2);
            }
        });

        english.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLocals("en");
                Intent i2 = new Intent(Language_Selection.this, Log_In.class);
                startActivity(i2);
            }
        });

        b1= findViewById(R.id.next);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i2 = new Intent(Language_Selection.this, Log_In.class);
                startActivity(i2);
            }
        });
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

    public  void loadLocale(){
        SharedPreferences preferences = getSharedPreferences("Setting", Activity.MODE_PRIVATE);
        String lamguage = preferences.getString("My_Language","");
        setLocals(lamguage);

    }

    @Override
    protected void onStart() {
        super.onStart();
        if(FirebaseAuth.getInstance().getCurrentUser() !=null){
            Intent intent = new Intent(Language_Selection.this,CheckUserProfile.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }

    }
}