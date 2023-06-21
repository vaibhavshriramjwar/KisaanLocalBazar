package xyz.vpscorelim.kisaan.dealer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import xyz.vpscorelim.kisaan.Notification.Constant;
import xyz.vpscorelim.kisaan.R;
import xyz.vpscorelim.kisaan.customer.CustomerOrderModel;
import xyz.vpscorelim.kisaan.farmer.FarmerOrderDetails;
import xyz.vpscorelim.kisaan.farmer.FarmerOrderModel;
import xyz.vpscorelim.kisaan.farmer.VegetableItems;
import xyz.vpscorelim.kisaan.farmer.farmer_adapter.SampleAdapter;
import xyz.vpscorelim.kisaan.farmer.farmer_adapter.sampleAdapetr;

public class DealerOrderDetails extends AppCompatActivity {



    TextView order_id,order_name,order_phone,order_address,order_total,expected_date,farmer_date,payment_options,orderStatus;
    String order_id_value="";
    RecyclerView orderLists;
    RecyclerView.LayoutManager layoutManager;
    FirebaseDatabase database;
    DatabaseReference reference;
    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    String productUnit;
    SampleAdapter orderViewHolder;
    private final static int SEND_SMS_PERMISSION_REQ=1;
    Dialog myDialog;
    MaterialButton update,call_btn;

    List<FarmerOrderModel> customerRequest;

    String customer_uid;
    String farmer_uid;
    String phone;

    TextView order_deli;

    ImageView back;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dealer_order_details);
        loadLocale();
        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_ALWAYS, WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_ALWAYS);
        getSupportActionBar().hide();
        myDialog = new Dialog(this);


        back= findViewById(R.id.close);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        //Init
        order_id        = findViewById(R.id.order_id);
        order_name      = findViewById(R.id.customer_name);
        order_phone     = findViewById(R.id.customer_phone);
        order_address   = findViewById(R.id.customer_address);
        order_total     = findViewById(R.id.order_total_p);
        expected_date   = findViewById(R.id.order_expect_date);
        farmer_date     = findViewById(R.id.your_expect_date);
        payment_options = findViewById(R.id.payment_option);
        call_btn        = findViewById(R.id.call_btn);
        orderStatus    = findViewById(R.id.order_name);
        order_deli     = findViewById(R.id.order_delivered_not);



        if (ContextCompat.checkSelfPermission(DealerOrderDetails.this, Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            // Ask for permision
            ActivityCompat.requestPermissions(this,new String[] { Manifest.permission.SEND_SMS}, 1);
        }
        else {
            Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
        }

        update          = findViewById(R.id.update);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogPopup();
            }
        });

        orderLists = findViewById(R.id.orders);
        layoutManager = new LinearLayoutManager(this);
        orderLists.setLayoutManager(layoutManager);
        orderLists.setHasFixedSize(true);

        database = FirebaseDatabase.getInstance();
        reference = database.getReference("DealerRequestOrder");


        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        farmer_uid  = currentUser.getUid();



        order_id_value = getIntent().getStringExtra("OrderId");
        phone = getIntent().getStringExtra("phone");


        call_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callToCustomer(phone);
            }
        });

        Toast.makeText(this, ""+phone, Toast.LENGTH_SHORT).show();




        getRequestOrder(order_id_value);


        Query query = reference.child(order_id_value).child("orderModels");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                customerRequest = new ArrayList<>();
                for (DataSnapshot pvostsnap : dataSnapshot.getChildren()) {
                    FarmerOrderModel post = pvostsnap.getValue(FarmerOrderModel.class);
                    //customerRequest.clear();
                    customerRequest.add(post);
                }
                orderViewHolder = new SampleAdapter(DealerOrderDetails.this, customerRequest);
                orderLists.setAdapter(orderViewHolder);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(DealerOrderDetails.this, ""+databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });



    }

    public  void loadLocale(){
        SharedPreferences preferences = getSharedPreferences("Setting", Activity.MODE_PRIVATE);
        String lamguage = preferences.getString("My_Language","");
        Locale locale =new Locale(lamguage);
        Locale.setDefault(locale);

    }

    private void getRequestOrder(final String order_id_value) {


        DatabaseReference ref = database.getReference("DealerRequestOrder");
        Query query = ref.orderByChild("order_id").equalTo(order_id_value);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot ds :snapshot.getChildren()){

                    //get Value
                    final String farmer_name                =""+ds.child("farmer_name").getValue();
                    final String farmer_phone               =""+ds.child("phone").getValue();
                    final String farmer_address             =""+ds.child("address").getValue();
                    final String farmer_total               =""+ds.child("total").getValue();
                    final String farmer_expected_date       =""+ds.child("customer_needed_date").getValue();
                    final String dealer_expected_delivery     =""+ds.child("dealer_delivery_date").getValue();
                    final String payment_option               =""+ds.child("payment_method").getValue();
                    final String order_status                 =""+ds.child("status").getValue();

                    order_id.setText(order_id_value);
                    order_name.setText(farmer_name);
                    order_address.setText(farmer_address);
                    order_phone.setText(farmer_phone);
                    order_total.setText(farmer_total);
                    expected_date.setText(farmer_expected_date);
                    farmer_date.setText(dealer_expected_delivery);
                    payment_options.setText(payment_option);
                    orderStatus.setText(order_status);



                    if(order_status.equals("Delivered")){

                        update.setVisibility(View.GONE);
                        order_deli.setVisibility(View.VISIBLE);
                        call_btn.setVisibility(View.GONE);

                    }



                    //Toast.makeText(His_Farmer_Profile.this, ""+farmer_uid, Toast.LENGTH_SHORT).show();

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(DealerOrderDetails.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void showDialogPopup() {


        Spinner spinner;
        TextView tet_label_op;
        final EditText order_date;
        MaterialButton update;
        ArrayList<VegetableItems> vegetableItems;
        myDialog.setContentView(R.layout.update_order_status);
        myDialog.setCanceledOnTouchOutside(false);
        spinner =(Spinner)myDialog.findViewById(R.id.spin1);
        update  =(MaterialButton)myDialog.findViewById(R.id.update_order);
        tet_label_op =(TextView) myDialog.findViewById(R.id.tet_label_op) ;
        order_date =(EditText)myDialog.findViewById(R.id.order_date);


        DatabaseReference ref = database.getReference("DealerRequestOrder");
        Query query = ref.orderByChild("order_id").equalTo(order_id_value);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot ds :snapshot.getChildren()){
                    final String order_status                 =""+ds.child("status").getValue();
                    final String order_da                     =""+ds.child("dealer_delivery_date").getValue();
                    tet_label_op.setText(order_status);
                    order_date.setText(order_da);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(DealerOrderDetails.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        order_date.setOnClickListener(new View.OnClickListener() {
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

                                order_date.setText(simpleDateFormat.format(calendar.getTime()));
                            }
                        };

                        new TimePickerDialog(DealerOrderDetails.this,timeSetListener,calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE),false).show();
                    }
                };

                new DatePickerDialog(DealerOrderDetails.this,dateSetListener,calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show();

            }
        });



        //Spinner Units
        List<String> units = new ArrayList<>();
        units.add(0,"Update Order Status");
        units.add("Accept");
        units.add("Packed");
        units.add("Shipped");
        units.add("Delivered");
        ArrayAdapter<String> dataAdapter2;
        dataAdapter2 = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,units);
        dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter2);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(parent.getItemAtPosition(position).equals("Update Order Status"))
                {
                    productUnit=null;
                }
                else{
                    productUnit = parent.getItemAtPosition(position).toString();
                    Toast.makeText(DealerOrderDetails.this, "You Select "+productUnit, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String order_date_far =  order_date.getText().toString().trim();
                if(order_date_far.equals("Not Mentioned")){
                    order_date.setError("Please Provide Delivery Date");
                    order_date.requestFocus();
                    return;
                }else {
                    updateOrderStatus(order_id_value,productUnit,order_date_far);


                }

            }
        });


        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();



    }










    private void updateOrderStatus(String order_id_value, String productUnit,String order_date_far) {
        DatabaseReference ref = database.getReference("Farmer_Data");
        Query query1 = ref.orderByChild("phoneNumber").equalTo(phone);
        query1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds :snapshot.getChildren()){
                    String customer_uid1               =""+ds.child("uid").getValue();
                    updateStatusOrder(order_id_value,productUnit,customer_uid1,order_date_far);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(DealerOrderDetails.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });




    }

    private void updateStatusOrder(String order_id_value, String productUnit, String customer_uid1,String order_date_far) {
        if(productUnit==null){
            Toast.makeText(this, "Please Change Status", Toast.LENGTH_SHORT).show();
        }

        else {



            HashMap<String,Object> result =new HashMap<>();
            result.put("status" ,productUnit);
            result.put("dealer_delivery_date",order_date_far);
            reference.child(order_id_value).updateChildren(result)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            sendNotification(order_id_value,productUnit,customer_uid1);
                            Toast.makeText(DealerOrderDetails.this, "order Status Change", Toast.LENGTH_SHORT).show();

                            sendSmsToCustomer(phone,productUnit);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(DealerOrderDetails.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

        }
    }


    private void callToCustomer(String phone)
    {
        Intent i = new Intent(Intent.ACTION_DIAL);
        String p = "tel:"+phone;
        i.setData(Uri.parse(p));
        startActivity(i);
    }

    private void sendSmsToCustomer(String phone, String productUnit) {
        if(checkPermission(Manifest.permission.SEND_SMS))
        {
            String msg="Hii "+productUnit;
            //Get the SmsManager instance and call the sendTextMessage method to send message
            SmsManager sms=SmsManager.getDefault();
            sms.sendTextMessage(phone, null, msg, null,null);
        }
        else {
            Toast.makeText(DealerOrderDetails.this, "Permission Denied", Toast.LENGTH_SHORT).show();
        }
        Toast.makeText(getApplicationContext(), "Message Sent successfully!",
                Toast.LENGTH_LONG).show();
    }

    private boolean checkPermission(String sendSms) {

        int checkpermission= ContextCompat.checkSelfPermission(this,sendSms);
        return checkpermission== PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode)
        {
            case SEND_SMS_PERMISSION_REQ:
                if(grantResults.length>0 &&(grantResults[0]==PackageManager.PERMISSION_GRANTED))
                {
                }
                break;
        }
    }

    private void sendNotification(String order_id_value, String productUnit, String customer_uid1) {
        String NOTIFICATION_TOPIC = "/topics/"+ Constant.FCM_TOPIC;
        String NOTIFICATION_TITLE = "Your Order "+order_id_value;
        String NOTIFICATION_MESSAGE = "Your Order Status Change "+productUnit;
        String NOTIFICATION_TYPE = "OrderStatusChangeByDealer";
        JSONObject notify = new JSONObject();
        JSONObject notifyBody = new JSONObject();
        try
        {
            notifyBody.put("notificationType",NOTIFICATION_TYPE);
            notifyBody.put("customerUid",customer_uid1);
            notifyBody.put("farmerUid",farmer_uid);
            notifyBody.put("orderId",order_id_value);
            notifyBody.put("customer_phone",phone);
            notifyBody.put("notificationTitle",NOTIFICATION_TITLE);
            notifyBody.put("notificationMessage",NOTIFICATION_MESSAGE);
            notify.put("to",NOTIFICATION_TOPIC);
            notify.put("data",notifyBody);
        }
        catch (Exception e)
        {
            Toast.makeText(this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        sendNotify(notify,order_id_value);


    }

    private void sendNotify(JSONObject notify, String order_id_value) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest("https://fcm.googleapis.com/fcm/send", notify, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(DealerOrderDetails.this, "Error"+error.getMessage(), Toast.LENGTH_SHORT).show();
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





}