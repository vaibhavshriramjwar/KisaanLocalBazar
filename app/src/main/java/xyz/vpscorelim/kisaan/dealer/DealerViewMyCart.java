package xyz.vpscorelim.kisaan.dealer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONObject;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import xyz.vpscorelim.kisaan.Database.Database;
import xyz.vpscorelim.kisaan.Database.DealerDatabase;
import xyz.vpscorelim.kisaan.Database.FarmerDatabase;
import xyz.vpscorelim.kisaan.Notification.Constant;
import xyz.vpscorelim.kisaan.R;
import xyz.vpscorelim.kisaan.adapter.CartItemLoad;
import xyz.vpscorelim.kisaan.customer.CustomerOrderDetails;
import xyz.vpscorelim.kisaan.customer.CustomerOrderModel;
import xyz.vpscorelim.kisaan.customer.CustomerRequest;
import xyz.vpscorelim.kisaan.customer.SearchOne;
import xyz.vpscorelim.kisaan.customer.ViewMyCart;
import xyz.vpscorelim.kisaan.dealer.DealerModel.DealerOrderListModel;
import xyz.vpscorelim.kisaan.dealer.DealerModel.DealerOrderRequest;

public class DealerViewMyCart extends AppCompatActivity {


    Dialog myDialog;

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RelativeLayout lop;

    FirebaseDatabase database;
    DatabaseReference requests;

    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    String uid;


    String address;
    EditText address_loc;
    public RadioGroup radioGroup,radioGroupPayment;
    TextView customer_name, shipAddressT;


    ImageView back;


    LottieAnimationView animation_view;

    List<DealerOrderListModel> cart = new ArrayList<>();
    DealerCartItemLoad cartItemLoad;

    public TextView txt_total,txt_title_u,log,upiIDText;
    MaterialButton make_order, check,start_sho;


    EditText date_time_in;


    String paymentFormat="Cash On Delivery";
    String UPI_ID_Farmer;

    private ClipboardManager myClipboard;
    private ClipData myClip;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dealer_view_my_cart);
        loadLocale();
        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_ALWAYS, WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_ALWAYS);
        getSupportActionBar().hide();
        radioGroupPayment = findViewById(R.id.radio_pay);


        myDialog = new Dialog(this);


        shipAddressT = findViewById(R.id.shipAddressT);
        address_loc = findViewById(R.id.address_location);
        customer_name = findViewById(R.id.customer_name);
        radioGroup = findViewById(R.id.radio);
        myClipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);

        upiIDText = findViewById(R.id.upiIDText);


        back= findViewById(R.id.close);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        date_time_in =findViewById(R.id.expect_date);
        date_time_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Calendar calendar=Calendar.getInstance();
                DatePickerDialog.OnDateSetListener dateSetListener=new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        calendar.set(Calendar.YEAR,year);
                        calendar.set(Calendar.MONTH,month);
                        calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);

                        TimePickerDialog.OnTimeSetListener timeSetListener=new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                calendar.set(Calendar.HOUR_OF_DAY,hourOfDay);
                                calendar.set(Calendar.MINUTE,minute);

                                SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd-MM-yy hh:mm a");

                                date_time_in.setText(simpleDateFormat.format(calendar.getTime()));
                            }
                        };

                        new TimePickerDialog(DealerViewMyCart.this,timeSetListener,calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE),false).show();
                    }
                };

                new DatePickerDialog(DealerViewMyCart.this,dateSetListener,calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show();




            }
        });

        lop= findViewById(R.id.rel3);



        database = FirebaseDatabase.getInstance();
        requests = database.getReference("DealerToFarmerRequest");


        BottomNavigationView bottomNavigationView = findViewById(R.id.dealer_bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.dealer_profile);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.dealer_dashboard:
                        startActivity(new Intent(getApplicationContext(), Dealer_Home.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.dealer_profile:
                        return true;
                    case R.id.dealer_order:
                        startActivity(new Intent(getApplicationContext(), DealerOrderList.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.dealer_search:
                        startActivity(new Intent(getApplicationContext(), SearchTwo.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.dealer_profile2:
                        startActivity(new Intent(getApplicationContext(), Dealer_Profile.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });








        //Firebase Init
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        uid = currentUser.getUid();
        String phone = currentUser.getPhoneNumber();


        recyclerView = findViewById(R.id.cart_list);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        final String phone1 = currentUser.getPhoneNumber();
        cart = new DealerDatabase(this).getCarts(phone1);
        cartItemLoad = new DealerCartItemLoad(cart, this);


        if(cartItemLoad.getItemCount()==0){
            checkIsCart();
        }

        txt_total = findViewById(R.id.total);
        make_order = findViewById(R.id.make_order);
        make_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                order_now();
            }
        });


        // Is the button now checked?
        RadioButton radioYes;
        radioYes=findViewById(R.id.radioYes);
        radioYes.isChecked();


        DatabaseReference datref1 = database.getReference("Dealer_Data");
        Query query = datref1.orderByChild("uid").equalTo(uid);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot ds : snapshot.getChildren()) {

                    //get Value
                    final String customer_name_t = "" + ds.child("dealerName").getValue();
                    final String customer_phone = "" + ds.child("phoneNumber").getValue();
                    final String customer_address = "" + ds.child("shopLocation").getValue();

                    customer_name.setText(customer_name_t);
                    shipAddressT.setText(customer_address);


                    address =customer_address;
                    //popup();
                    //requestOrder(customer_name, customer_phone, customer_address);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(DealerViewMyCart.this, "" + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


        String dealerId = new FarmerDatabase(getBaseContext()).checkFoodExists();

        DatabaseReference farmerDataForUPI = database.getReference("Farmer_Data");
        Query farmerQueryForUPI = farmerDataForUPI.orderByChild("uid").equalTo(dealerId);
        farmerQueryForUPI.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot ds : snapshot.getChildren()) {

                    //get Value
                    final String farmerUPI_ID = "" + ds.child("UPI_ID").getValue();
                    UPI_ID_Farmer = "" + ds.child("UPI_ID").getValue();

                    if(farmerUPI_ID.equals("")){

                        radioGroupPayment.setVisibility(View.GONE);

                    }else{

                        radioGroupPayment.setVisibility(View.VISIBLE);

                    }

                    //popup();
                    //requestOrder(customer_name, customer_phone, customer_address);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(DealerViewMyCart.this, "" + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                loadListofproduct();
            }
        }, 0, 1000);



        upiIDText.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String text;
                text = upiIDText.getText().toString();

                myClip = ClipData.newPlainText("text", text);
                myClipboard.setPrimaryClip(myClip);
                upiIDText.setTextColor(getResources().getColor(R.color.colorUPI));

                Toast.makeText(getApplicationContext(), "UPI Copied",
                        Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void onRadioButtonPayment(final View view){



        final boolean checked = ((RadioButton) view).isChecked();
        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radioCash:
                if (checked)
                    paymentFormat="Cash On Delivery";
                upiIDText.setVisibility(View.GONE);
                break;
            case R.id.radioOnline:
                if (checked) paymentFormat="Online Payment";
                upiIDText.setVisibility(View.VISIBLE);
                upiIDText.setText(UPI_ID_Farmer);





                break;


        }


    }


    public  void loadLocale(){
        SharedPreferences preferences = getSharedPreferences("Setting", Activity.MODE_PRIVATE);
        String lamguage = preferences.getString("My_Language","");
        Locale locale =new Locale(lamguage);
        Locale.setDefault(locale);

    }

    private void checkIsCart() {



        txt_title_u = findViewById(R.id.txt_title_u);
        log =findViewById(R.id.log);
        start_sho = findViewById(R.id.start_sho);


        lop.setVisibility(View.GONE);
        animation_view= findViewById(R.id.empty);
        animation_view.setVisibility(View.VISIBLE);
        txt_title_u.setVisibility(View.VISIBLE);
        log.setVisibility(View.VISIBLE);
        start_sho.setVisibility(View.VISIBLE);
        start_sho.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DealerViewMyCart.this, SearchTwo.class));
                Animatoo.animateSlideRight(DealerViewMyCart.this);
            }
        });

    }


    public void onRadioButtonClicked(final View view) {
        DatabaseReference datref1 = database.getReference("Dealer_Data");
        Query query = datref1.orderByChild("uid").equalTo(uid);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    //get Value
                    final String customer_name_t = "" + ds.child("dealerName").getValue();
                    final String customer_phone = "" + ds.child("phoneNumber").getValue();
                    final String customer_address = "" + ds.child("shopLocation").getValue();
                    final boolean checked = ((RadioButton) view).isChecked();
                    // Check which radio button was clicked
                    switch(view.getId()) {
                        case R.id.radioYes:
                            if (checked)

                                shipAddressT.setVisibility(View.VISIBLE);
                            address_loc.setVisibility(View.GONE);

                            Timer timer = new Timer();
                            timer.schedule(new TimerTask() {
                                @Override
                                public void run() {
                                    address = customer_address;
                                }
                            }, 0, 1000);

                            break;
                        case R.id.radioNo:
                            if (checked)

                                shipAddressT.setVisibility(View.GONE);
                            address_loc.setVisibility(View.VISIBLE);

                            Timer timer1 = new Timer();
                            timer1.schedule(new TimerTask() {
                                @Override
                                public void run() {
                                    address =address_loc.getText().toString();
                                }
                            }, 0, 1000);






                            break;


                    }





                    //popup();
                    //requestOrder(customer_name, customer_phone, customer_address);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(DealerViewMyCart.this, "" + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });



    }



    private void order_now() {

        final String date_time = date_time_in.getText().toString();

        if(address.isEmpty())
        {
            address_loc.setError("Please Provide Address");
            address_loc.requestFocus();
            return;
        }
        if(date_time.isEmpty())
        {
            date_time_in.setError("Please Provide Date");
            date_time_in.requestFocus();
            return;
        }
        else {

            DatabaseReference datref1 = database.getReference("Dealer_Data");
            Query query = datref1.orderByChild("uid").equalTo(uid);
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        final String customer_name_t = "" + ds.child("dealerName").getValue();
                        final String customer_phone = "" + ds.child("phoneNumber").getValue();
                        final String customer_address = "" + ds.child("shopLocation").getValue();
                        new_popup(customer_name_t,customer_phone,customer_address,date_time,address);
                        //requestOrder(customer_name, customer_phone, customer_address);

                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(DealerViewMyCart.this, "" + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });



        }




    }


    private void new_popup(final String customer_name_t, final String customer_phone, final String customer_address, final String date_time, final String address) {

        MaterialButton request;
        myDialog.setContentView(R.layout.customer_request_popup);
        myDialog.setCanceledOnTouchOutside(false);
        request=(MaterialButton)myDialog.findViewById(R.id.request_order);
        request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestOrderNow(customer_name_t,customer_phone,customer_address,date_time,address);
            }
        });
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();

    }

    private void requestOrderNow(String customer_name_t, String customer_phone, String customer_address, String date_time, String address) {


        String isEx = new DealerDatabase(getBaseContext()).checkFoodExists();
        String status = "Wait For Response";
        String paymentMethod="Cash On Delivery";
        String date="Not Mentioned";
        String id = String.valueOf(System.currentTimeMillis());
        DealerOrderRequest customerRequest = new DealerOrderRequest(
                id,
                isEx,
                customer_phone,
                customer_name_t,
                address,
                txt_total.getText().toString(),
                status,
                paymentFormat,
                date_time,
                date,
                cart
        );
        requests.child(id).setValue(customerRequest);

        sendNotification(id,isEx);

        new DealerDatabase(getBaseContext()).cleanCart();
        Toast.makeText(this, "Cart Is Empty Now", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(DealerViewMyCart.this, DealerOrderList.class);
        intent.putExtra("OrderId",id);
        intent.putExtra("phone",customer_phone);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);





        myDialog.dismiss();




    }

    private void sendNotification(String id, String isEx) {

        String NOTIFICATION_TOPIC = "/topics/"+ Constant.FCM_TOPIC;
        String NOTIFICATION_TITLE = "New Order "+id;
        String NOTIFICATION_MESSAGE = "Congratulations..! You Have New Order";
        String NOTIFICATION_TYPE = "NewOrderToFarmer";


        JSONObject notify = new JSONObject();
        JSONObject notifyBody = new JSONObject();
        try
        {
            notifyBody.put("notificationType",NOTIFICATION_TYPE);
            notifyBody.put("customerUid",uid);
            notifyBody.put("farmerUid",isEx);
            notifyBody.put("orderId",id);
            notifyBody.put("customer_phone",currentUser.getPhoneNumber());
            notifyBody.put("notificationTitle",NOTIFICATION_TITLE);
            notifyBody.put("notificationMessage",NOTIFICATION_MESSAGE);

            Toast.makeText(this, ""+uid, Toast.LENGTH_SHORT).show();

            notify.put("to",NOTIFICATION_TOPIC);
            notify.put("data",notifyBody);



        }
        catch (Exception e)
        {
            Toast.makeText(this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        sendNotify(notify,id);









    }

    private void sendNotify(JSONObject notify, String id) {

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest("https://fcm.googleapis.com/fcm/send", notify, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(DealerViewMyCart.this, "Error"+error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {


                Map<String,String> headers = new HashMap<>();
                headers.put("Content-Type","application/json");
                headers.put("Authorization","key="+Constant.FCM_KEY);


                return headers;
            }
        };


        Volley.newRequestQueue(this).add(jsonObjectRequest);



    }

    private void loadListofproduct() {

        FirebaseAuth mAuth;
        FirebaseUser currentUser;
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        final String phone = currentUser.getPhoneNumber();
        cart = new DealerDatabase(this).getCarts(phone);
        cartItemLoad = new DealerCartItemLoad(cart, this);
        runOnUiThread(new Runnable() {

            @Override
            public void run() {

                // Stuff that updates the UI
                recyclerView.setAdapter(cartItemLoad);
            }
        });


        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Do something after 100ms
                int total = 0;
                for (DealerOrderListModel customerOrderModel : cart)
                    total += (Integer.parseInt(customerOrderModel.getPrice())) * (Integer.parseInt(customerOrderModel.getQuantity()));
                NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("en", "IN"));
                txt_total.setText(formatter.format(total));
            }
        }, 1000);


    }



}