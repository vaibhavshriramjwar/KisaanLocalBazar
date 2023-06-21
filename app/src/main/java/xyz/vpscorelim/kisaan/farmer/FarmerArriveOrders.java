package xyz.vpscorelim.kisaan.farmer;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import xyz.vpscorelim.kisaan.R;
import xyz.vpscorelim.kisaan.adapter.ViewPagerAdapter;
import xyz.vpscorelim.kisaan.customer.CustomerRequest;
import xyz.vpscorelim.kisaan.farmer.farmer_adapter.FarmerOrderViewHolder;


public class FarmerArriveOrders extends Fragment {


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private String mParam1;
    private String mParam2;







    TabLayout tabLayout;
    ViewPager viewPager;

    public FarmerArriveOrders() {
        // Required empty public constructor
    }


    public static FarmerArriveOrders newInstance(String param1, String param2) {
        FarmerArriveOrders fragment = new FarmerArriveOrders();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View farmer_arrives_order = inflater.inflate(R.layout.fragment_farmer_arrive_orders, container, false);





        tabLayout        =farmer_arrives_order.findViewById(R.id.tablayout_id);
        viewPager        =farmer_arrives_order.findViewById(R.id.viewpager2);
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getChildFragmentManager());
        //Adding Fragments
        viewPagerAdapter.AddFragment(new FarmerMyOrderFromCustomer(),"Customer Order");
        viewPagerAdapter.AddFragment(new FarmerMyOrderFromDealer(),"Dealer Order");



        //Adapter Setup
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);


        return farmer_arrives_order;
    }


}