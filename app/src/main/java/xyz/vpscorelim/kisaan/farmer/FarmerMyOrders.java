package xyz.vpscorelim.kisaan.farmer;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

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
import xyz.vpscorelim.kisaan.adapter.OrderViewHolder;
import xyz.vpscorelim.kisaan.customer.CustomerMyOrder;
import xyz.vpscorelim.kisaan.customer.CustomerRequest;
import xyz.vpscorelim.kisaan.farmer.farmer_Model.FarmerRequest;
import xyz.vpscorelim.kisaan.farmer.farmer_adapter.FarmerOwnOrderViewHolder;


public class FarmerMyOrders extends Fragment {


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private String mParam1;
    private String mParam2;



    RecyclerView order_list;
    RecyclerView.LayoutManager layoutManager;
    FirebaseDatabase database;
    DatabaseReference reference;

    FarmerOwnOrderViewHolder orderViewHolder;
    List<FarmerRequest> customerRequests;

    FirebaseAuth mAuth;
    FirebaseUser currentUser;

    String txtPhoneNo;
    ImageView back;



    public FarmerMyOrders() {
        // Required empty public constructor
    }



    public static FarmerMyOrders newInstance(String param1, String param2) {
        FarmerMyOrders fragment = new FarmerMyOrders();
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
        View farmerMyOrder = inflater.inflate(R.layout.fragment_farmer_my_orders, container, false);

        order_list = farmerMyOrder.findViewById(R.id.order_list);
        order_list.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        order_list.setLayoutManager(layoutManager);
        order_list.setHasFixedSize(true);

        database = FirebaseDatabase.getInstance();
        reference = database.getReference("DealerRequestOrder");

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        assert currentUser != null;
        txtPhoneNo = currentUser.getPhoneNumber();

        getOrderList(txtPhoneNo);


        return farmerMyOrder;
    }

    private void getOrderList(String txtPhoneNo) {
        Query query = reference.orderByChild("phone").equalTo(txtPhoneNo);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                customerRequests = new ArrayList<>();
                for (DataSnapshot pvostsnap : dataSnapshot.getChildren()) {
                    FarmerRequest post = pvostsnap.getValue(FarmerRequest.class);
                    //farmerProductData.clear();
                    customerRequests.add(post);
                    Collections.reverse(customerRequests);
                }
                orderViewHolder = new FarmerOwnOrderViewHolder(getActivity(), customerRequests);
                order_list.setAdapter(orderViewHolder);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(getActivity(), ""+databaseError.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }
}