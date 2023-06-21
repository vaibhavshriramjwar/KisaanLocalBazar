package xyz.vpscorelim.kisaan.customer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import cn.pedant.SweetAlert.SweetAlertDialog;
import xyz.vpscorelim.kisaan.AboutUs;
import xyz.vpscorelim.kisaan.BuildConfig;
import xyz.vpscorelim.kisaan.Developer;
import xyz.vpscorelim.kisaan.KisanLocalBazar;
import xyz.vpscorelim.kisaan.Log_In;
import xyz.vpscorelim.kisaan.Notification.Constant;
import xyz.vpscorelim.kisaan.PrivacyPolicy;
import xyz.vpscorelim.kisaan.R;
import xyz.vpscorelim.kisaan.dealer.Dealer_Home;
import xyz.vpscorelim.kisaan.dealer.Dealer_Profile;
import xyz.vpscorelim.kisaan.farmer.Farmer_Home;
import xyz.vpscorelim.kisaan.farmer.Farmer_Profile;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
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
import com.google.android.gms.appinvite.AppInviteInvitation;
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
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.ShortDynamicLink;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.HashMap;
import java.util.Locale;

public class Customer_Profile extends AppCompatActivity {

    MaterialButton logout;


    private FirebaseAuth firebaseAuth;
    private FirebaseUser currentUser;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference reference;
    ProgressDialog pd;

    private boolean isChecked =false;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor shEditor;

    ImageView back;
    String lang;


    //TextView
    TextView cus_name,cus_subArea,cus_phone,cus_city,cus_state;


    Dialog myDialog,popAddPost,myDialogStore,change_language;


    //RelativeLayout
    RelativeLayout my_order,notification,changeLanguage,my_delivery_address,edit_profile,privacy_policy,app_version,updates,developer,logout_user,share_app,about_us;

    KisanLocalBazar kisanLocalBazar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customer_profile);
        kisanLocalBazar = KisanLocalBazar.getzInstance();
        Window w = getWindow();
        loadLocale();
        w.setFlags(WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_ALWAYS, WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_ALWAYS);
        getSupportActionBar().hide();
        change_language =new Dialog(this);


        changeLanguage = findViewById(R.id.r5);
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
        myDialog = new Dialog(this);
        popAddPost = new Dialog(this);
        myDialogStore = new Dialog(this);

        pd = new ProgressDialog(this);
        //Firebase
        firebaseAuth= FirebaseAuth.getInstance();
        currentUser =firebaseAuth.getCurrentUser();
        String currentUid = currentUser.getUid();
        firebaseDatabase =FirebaseDatabase.getInstance();
        reference =firebaseDatabase.getReference("Customer_Data");


        //Text view Init
        cus_name    = findViewById(R.id.customer_name);
        cus_subArea = findViewById(R.id.customer_tahsil);
        cus_phone   = findViewById(R.id.customer_phone);
        cus_city    = findViewById(R.id.district_name);
        cus_state   = findViewById(R.id.state_name);


        about_us = findViewById(R.id.r8);
        about_us.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), AboutUs.class));
                overridePendingTransition(0,0);
                Animatoo.animateZoom(Customer_Profile.this);
            }
        });


        //Get Current User data
        Query query = reference.orderByChild("uid").equalTo(currentUid);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds :snapshot.getChildren()){
                    String customer_name_f      =""+ds.child("Customer_name").getValue();
                    String customer_sub_area    =""+ds.child("Tehsil_Name").getValue();
                    String customer_phone_f     =""+ds.child("phoneNumber").getValue();
                    String customer_city        =""+ds.child("District_Name").getValue();
                    String customer_state       =""+ds.child("State_Name").getValue();

                    cus_name.setText(customer_name_f);
                    cus_subArea.setText(customer_sub_area);
                    cus_phone.setText(customer_phone_f);
                    cus_city.setText(customer_city);
                    cus_state.setText(customer_state);

                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });






        //Init variable
        my_order            = findViewById(R.id.r1);
        notification        = findViewById(R.id.r2);
        my_delivery_address = findViewById(R.id.r3);
        edit_profile        = findViewById(R.id.r4);
        privacy_policy      = findViewById(R.id.r56);
        updates             = findViewById(R.id.r6);
        developer           = findViewById(R.id.r7);
        logout_user         = findViewById(R.id.r9);
        share_app           = findViewById(R.id.r10);









        //Click Listener on Relative Layout
        my_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), CustomerMyOrder.class));
                overridePendingTransition(0,0);
                Animatoo.animateZoom(Customer_Profile.this);
                //Toast.makeText(Customer_Profile.this, "My Order CLicked", Toast.LENGTH_SHORT).show();
            }
        });
        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSettingPop();

                //Toast.makeText(Customer_Profile.this, "Notification Clicked", Toast.LENGTH_SHORT).show();

            }
        });
        my_delivery_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(Customer_Profile.this, "My Delivery Address", Toast.LENGTH_SHORT).show();

            }
        });
        edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showEditProfileDialog();

                //Toast.makeText(Customer_Profile.this, "Developer Click", Toast.LENGTH_SHORT).show();

            }
        });
        privacy_policy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Customer_Profile.this, PrivacyPolicy.class));
                overridePendingTransition(0,0);
                Animatoo.animateZoom(Customer_Profile.this);
                //Toast.makeText(Customer_Profile.this, "Developer Click", Toast.LENGTH_SHORT).show();
            }
        });


        developer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Customer_Profile.this, Developer.class));
                overridePendingTransition(0,0);
                Animatoo.animateZoom(Customer_Profile.this);
                //Toast.makeText(Customer_Profile.this, "Developer Click", Toast.LENGTH_SHORT).show();
            }
        });
        logout_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SweetAlertDialog(Customer_Profile.this, SweetAlertDialog.CUSTOM_IMAGE_TYPE)
                        .setTitleText(getString(R.string.are_you_sure))
                        .setContentText("Want to logout")
                        .setCustomImage(R.drawable.icon1)
                        .setConfirmText("Yes! Logout")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                FirebaseAuth.getInstance().signOut();
                                Intent intent = new Intent(Customer_Profile.this, Log_In.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                Animatoo.animateZoom(Customer_Profile.this);
                            }
                        })
                        .show();
            }
        });
        share_app.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


               getlink();


            }
        });


        Task<ShortDynamicLink> shortLinkTask = FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setLink(Uri.parse("https://www.example.com/"))
                .setDomainUriPrefix("https://example.page.link")
                .buildShortDynamicLink()
                .addOnCompleteListener(this, new OnCompleteListener<ShortDynamicLink>() {
                    @Override
                    public void onComplete(@NonNull Task<ShortDynamicLink> task) {
                        if (task.isSuccessful()) {
                            Uri shortLink = task.getResult().getShortLink();
                            Uri flowchartLink = task.getResult().getPreviewLink();
                        } else {

                        }
                    }
                });







        final String phoneNumber = getIntent().getStringExtra("phoneNumber");
        Toast.makeText(this, ""+phoneNumber, Toast.LENGTH_SHORT).show();


        sharedPreferences =getSharedPreferences("NOTIFICATION",MODE_PRIVATE);

        isChecked = sharedPreferences.getBoolean("FCM_ENABLED",false);











        logout = findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(Customer_Profile.this, Log_In.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                Toast.makeText(Customer_Profile.this, "Farmer", Toast.LENGTH_SHORT).show();
            }
        });


        BottomNavigationView bottomNavigationView = findViewById(R.id.customer_bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.customer_profile);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.customer_dashboard:
                        startActivity(new Intent(getApplicationContext(), Customer_Home.class));
                        Animatoo.animateSlideRight(Customer_Profile.this);
                        return true;
                    case R.id.customer_profile:
                        return  true;
                    case R.id.customer_search:
                        startActivity(new Intent(getApplicationContext(), SearchOne.class));
                        Animatoo.animateSlideRight(Customer_Profile.this);
                        return true;
                    case R.id.customer_order:
                        startActivity(new Intent(getApplicationContext(), CustomerMyOrder.class));
                        Animatoo.animateSlideRight(Customer_Profile.this);
                        return true;
                    case R.id.customer_cart:
                        startActivity(new Intent(getApplicationContext(), ViewMyCart.class));
                        Animatoo.animateSlideRight(Customer_Profile.this);
                        return true;
                }
                return false;
            }
        });
    }

    private void getlink() {


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

//        String link ="https://kisaanlocalbazar.page.link/?"+
//                "link=https://vpscorelim.in"+
//                "&apn="+getPackageName()+
//                "&st=My refer link"+
//                "&sd=Reward Coins"+
//                "&si="+"https://www.vpscorelim.in/wp-content/uploads/2020/10/cropped-logo-otoes2xz3opsxwwjf10857rl6m4vkt2q0huua98kru-300x100.png";
//        Task<ShortDynamicLink> shortLinkTask = FirebaseDynamicLinks.getInstance().createDynamicLink()
//                .setLongLink(Uri.parse(link))
//                .buildShortDynamicLink()
//                .addOnCompleteListener(this, new OnCompleteListener<ShortDynamicLink>() {
//                    @Override
//                    public void onComplete(@NonNull Task<ShortDynamicLink> task) {
//                        if (task.isSuccessful()) {
//                            Uri shortLink = task.getResult().getShortLink();
//                            Uri flowchartLink = task.getResult().getPreviewLink();
//
//
//
//                            try {
//
//                                Intent intent = new Intent(Intent.ACTION_SEND);
//                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                                intent.putExtra(Intent.EXTRA_TEXT,link);
//                                intent.setType("text/plain");
//                                startActivity(Intent.createChooser(intent, "Share app via"));
//                            }
//                            catch (Exception e){
//                                e.printStackTrace();
//                            }
//
//                            Log.e("link",shortLink.toString());
//
//                            Toast.makeText(Customer_Profile.this, ""+shortLink, Toast.LENGTH_SHORT).show();
//
//
//                        } else {
//
//                        }
//                    }
//                });


    }

    private void showEditProfileDialog() {
        String option [] ={"Edit Name","Edit Address","Edit City","Edit SubArea","Edit State"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Choose Action");

        builder.setItems(option, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if(which==0)
                {
                    pd.setMessage(getString(R.string.update_name));
                    showNamePhoneAddressUpdateDialog("Customer_name");

                }
                else if(which==1)
                {
                    //Edit Name Clicked
                    pd.setMessage(getString(R.string.update_ad));
                    showNamePhoneAddressUpdateDialog("Current_Location");

                }
                else if(which==2)
                {
                    //Edit Phone No Clicked
                    pd.setMessage(getString(R.string.update_city));
                    showNamePhoneAddressUpdateDialog("District_Name");

                }
                else if(which==3){
                    //Edit Address Clicked
                    pd.setMessage(getString(R.string.edit_sub));
                    showNamePhoneAddressUpdateDialog("Tehsil_Name");
                }
                else if(which==4)
                {
                    //Edit Address Clicked
                    pd.setMessage(getString(R.string.edit_st));
                    showNamePhoneAddressUpdateDialog("State_Name");
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

        if(key=="Customer_name"){
            editText.setSingleLine(true);
            editText.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);

        }
        if(key=="Current_Location"){
            int maxLength=155;
            editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});


        }
        if(key=="District_Name"){

            editText.setSingleLine(true);
            editText.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);

        }
        if(key=="Tehsil_Name"){
            editText.setSingleLine(true);
            editText.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);

        }
        if(key=="State_Name"){
            editText.setSingleLine(true);
            editText.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
        }

        linearLayout.addView(editText);
        builder.setView(linearLayout);

        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String value =editText.getText().toString().trim();
                FirebaseDatabase Fade = FirebaseDatabase.getInstance();
                DatabaseReference FadRefe =Fade.getReference("Customer_Data");
                if(!TextUtils.isEmpty(value)){

                    pd.show();
                    HashMap<String,Object> result =new HashMap<>();
                    result.put(key ,value);


                    FadRefe.child(currentUser.getUid()).updateChildren(result)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    pd.dismiss();
                                    Toast.makeText(Customer_Profile.this, "Updated...", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    pd.dismiss();
                                    Toast.makeText(Customer_Profile.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();

                                }
                            });


                }
                else{
                    Toast.makeText(Customer_Profile.this, "Please Enter "+key, Toast.LENGTH_SHORT).show();

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

    private void openSettingPop() {

        TextView cut;
        SwitchMaterial fcmSwitch;
        myDialog.setContentView(R.layout.customer_popup_form);
        myDialog.setCanceledOnTouchOutside(false);
        cut = (TextView)myDialog.findViewById(R.id.cut);
        fcmSwitch=(SwitchMaterial)myDialog.findViewById(R.id.fcmSwitch);
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
                Intent i2 = new Intent(Customer_Profile.this, Customer_Home.class);
                i2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i2);
            }
        });
        hindi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLocals("hi");
                change_language.dismiss();
                Intent i2 = new Intent(Customer_Profile.this, Customer_Home.class);
                i2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i2);
            }
        });
        english.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLocals("en");
                change_language.dismiss();
                Intent i2 = new Intent(Customer_Profile.this, Customer_Home.class);
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
        //loadLocale();

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