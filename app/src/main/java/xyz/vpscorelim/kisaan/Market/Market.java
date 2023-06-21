package xyz.vpscorelim.kisaan.Market;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.vikktorn.picker.City;
import com.vikktorn.picker.CityPicker;
import com.vikktorn.picker.Country;
import com.vikktorn.picker.CountryPicker;
import com.vikktorn.picker.OnCityPickerListener;
import com.vikktorn.picker.OnCountryPickerListener;
import com.vikktorn.picker.OnStatePickerListener;
import com.vikktorn.picker.State;
import com.vikktorn.picker.StatePicker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import xyz.vpscorelim.kisaan.KisanLocalBazar;
import xyz.vpscorelim.kisaan.R;
import xyz.vpscorelim.kisaan.adapter.Adapter_Farmer_His_products;
import xyz.vpscorelim.kisaan.adapter.OrderViewHolder;
import xyz.vpscorelim.kisaan.customer.CustomerMyOrder;
import xyz.vpscorelim.kisaan.customer.His_Farmer_Profile;
import xyz.vpscorelim.kisaan.farmer.Farmer_Product_Data;

public class Market extends AppCompatActivity {


    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;



    String url = "https://api.data.gov.in/resource/9ef84268-d588-465a-a308-a864a43d0070?api-key=579b464db66ec23bdd000001cdd3946e44ce4aad7209ff7b23ac571b&format=json&offset=0&limit=50&filters[district]=";

    //MarketAdapter adapter;
    List<Item> items;
    private static String JSON_URL = "https://script.google.com/macros/s/AKfycbw7wzIUxB54GJwGonhVZ41Qr9OGWbzb3oY9qVtwku3DC2NQBhw/exec";
    ListView listView;
    ListAdapter adapter;
    ProgressDialog loading;


    EditText  cityName;
    ImageButton search;
    ImageView back;
    TextView copy;
    KisanLocalBazar kisanLocalBazar;


    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.market);
        kisanLocalBazar = KisanLocalBazar.getzInstance();
        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_ALWAYS, WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_ALWAYS);
        getSupportActionBar().hide();
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_LAYOUT_FLAGS);



        copy = findViewById(R.id.copy);
        back= findViewById(R.id.close);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        items = new ArrayList<>();
        listView = (ListView) findViewById(R.id.lv_items);
        cityName = findViewById(R.id.search_field);
        search =  findViewById(R.id.search_btn);


        //Firebase Init
        mAuth             = FirebaseAuth.getInstance();
        currentUser       = mAuth.getCurrentUser();
        firebaseDatabase  = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Market-API");

        DatabaseReference datref1 = firebaseDatabase.getReference("Market-API");
        datref1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot ds :snapshot.getChildren()){
                    final String core_url           =""+ds.child("core_url").getValue();
                   final String filter_url         =""+ds.child("filter_url").getValue();

                    getItems(core_url);




//                    Toast.makeText(kisanLocalBazar, ""+core_url, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });



        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String city =  cityName.getText().toString().toLowerCase();
                if(city.isEmpty()){
                    Toast.makeText(kisanLocalBazar, "Enter The City Name", Toast.LENGTH_SHORT).show();
                }else{
                    getNewItems(city);
                }

            }
        });


//        getItems();





    }



    private void getNewItems(String city) {

        DatabaseReference datref1 = firebaseDatabase.getReference("Market-API");
        datref1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot ds :snapshot.getChildren()){
                    final String filter_url         =""+ds.child("filter_url").getValue();


                    loading =  ProgressDialog.show(Market.this,"Loading","please wait",false,true);
                    Log.d("Json", filter_url+city);
                    String upperString = city.substring(0, 1).toUpperCase() + city.substring(1).toLowerCase();
                    String josnUrl = String.format("%s%s", filter_url, upperString);
                    StringRequest stringRequest = new StringRequest(Request.Method.GET, josnUrl,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    parseNewItems(response,city);
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                }
                            }
                    );
                    int socketTimeOut = 50000;
                    RetryPolicy policy = new DefaultRetryPolicy(socketTimeOut, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
                    stringRequest.setRetryPolicy(policy);
                    RequestQueue queue = Volley.newRequestQueue(Market.this);
                    queue.add(stringRequest);




                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });




    }

    private void parseNewItems(String response,String city) {
        ArrayList<HashMap<String, String>> list = new ArrayList<>();

        try {
            JSONObject jobj = new JSONObject(response);
            JSONArray jarray = jobj.getJSONArray("records");


            for (int i = 0; i < jarray.length(); i++) {

                JSONObject jo = jarray.getJSONObject(i);



                String state = jo.getString("state");
                String district = jo.getString("district");
                String market = jo.getString("market");
                String commodity = jo.getString("commodity");
                String variety = jo.getString("variety");
                String min_price = jo.getString("min_price");
                String max_price = jo.getString("max_price");
                String modal_price = jo.getString("modal_price");


                HashMap<String, String> item = new HashMap<>();
                item.put("state", state);
                item.put("district", district);
                item.put("market",market);
                item.put("commodity", commodity);
                item.put("variety", variety);
                item.put("min_price",min_price);
                item.put("max_price", max_price);
                item.put("modal_price", modal_price);



                if(district.toLowerCase().contains(city.toLowerCase())){
                    list.add(item);
                }





            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        adapter = new SimpleAdapter(this,list,R.layout.market_list_layout,
                new String[]{"state",
                        "district",
                        "market",
                        "commodity",
                        "variety",
                        "min_price",
                        "max_price",
                        "modal_price"},

                new int[]{R.id.txt_state,
                        R.id.txt_district,
                        R.id.txt_market,
                        R.id.txt_commodity,
                        R.id.txt_variety,
                        R.id.txt_min_price,
                        R.id.txt_max_price,
                        R.id.txt_modal_price});


        listView.setAdapter(adapter);
        loading.dismiss();
    }


    private void getItems(String url) {


        loading =  ProgressDialog.show(this,"Loading","please wait",false,true);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        parseItems(response);
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        );

        int socketTimeOut = 50000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeOut, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

        stringRequest.setRetryPolicy(policy);

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(stringRequest);
    }

    private void parseItems(String jsonResposnce) {


//        Toast.makeText(kisanLocalBazar, jsonResposnce, Toast.LENGTH_SHORT).show();
//        Log.d("Json", jsonResposnce);


        ArrayList<HashMap<String, String>> list = new ArrayList<>();

        try {
            JSONObject jobj = new JSONObject(jsonResposnce);
            JSONArray jarray = jobj.getJSONArray("records");

            Toast.makeText(kisanLocalBazar, jarray.toString(), Toast.LENGTH_SHORT).show();

//          copy.setText(jarray.toString());
            Log.d("Json", jarray.toString());
//            JSONObject resultObject = (JSONObject) jarray.get(0);
//            JSONObject location = (JSONObject) resultObject.get("location");



            for (int i = 0; i < jarray.length(); i++) {

                JSONObject jo = jarray.getJSONObject(i);


                String state = jo.getString("state");
                String district = jo.getString("district");
                String market = jo.getString("market");
                String commodity = jo.getString("commodity");
                String variety = jo.getString("variety");
                String min_price = jo.getString("min_price");
                String max_price = jo.getString("max_price");
                String modal_price = jo.getString("modal_price");


                HashMap<String, String> item = new HashMap<>();
                item.put("state", state);
                item.put("district", district);
                item.put("market",market);
                item.put("commodity", commodity);
                item.put("variety", variety);
                item.put("min_price",min_price);
                item.put("max_price", max_price);
                item.put("modal_price", modal_price);
                list.add(item);
                String district_txt ="Chittor";

                if(district.equals(district_txt)){

                }





            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        adapter = new SimpleAdapter(this,list,R.layout.market_list_layout,
                new String[]{"state",
                        "district",
                        "market",
                        "commodity",
                        "variety",
                        "min_price",
                        "max_price",
                        "modal_price"},

                new int[]{R.id.txt_state,
                        R.id.txt_district,
                        R.id.txt_market,
                        R.id.txt_commodity,
                        R.id.txt_variety,
                        R.id.txt_min_price,
                        R.id.txt_max_price,
                        R.id.txt_modal_price});


        listView.setAdapter(adapter);
        loading.dismiss();






    }








//    private void getData() {
//
//        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://script.google.com/macros/s/AKfycbw7wzIUxB54GJwGonhVZ41Qr9OGWbzb3oY9qVtwku3DC2NQBhw/exec",
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        parseItems(response);
//                    }
//                },
//
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//
//                    }
//                }
//        );
//
//        int socketTimeOut = 50000;
//        RetryPolicy policy = new DefaultRetryPolicy(socketTimeOut, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
//
//        stringRequest.setRetryPolicy(policy);
//
//        RequestQueue queue = Volley.newRequestQueue(this);
//        queue.add(stringRequest);
//
//    }

//    private void parseItems() {
//
//
//        RequestQueue queue = Volley.newRequestQueue(this);
//        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, JSON_URL, null, new Response.Listener<JSONArray>() {
//            @Override
//            public void onResponse(JSONArray response) {
//                for (int i = 0; i < response.length(); i++) {
//                    try {
//                        JSONObject songObject = response.getJSONObject(i);
//
//                        Item song = new Item();
//                        song.setDistrict(songObject.getString("district").toString());
//                        song.setState(songObject.getString("state").toString());
//
//                        items.add(song);
//
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//
//                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
//                adapter = new MarketAdapter(getApplicationContext(),items);
//                recyclerView.setAdapter(adapter);
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Log.d("tag", "onErrorResponse: " + error.getMessage());
//            }
//        });
//
//        queue.add(jsonArrayRequest);
//
//
//    }


}
