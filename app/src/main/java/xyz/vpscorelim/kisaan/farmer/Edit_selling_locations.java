package xyz.vpscorelim.kisaan.farmer;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.material.button.MaterialButton;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import xyz.vpscorelim.kisaan.R;

import static android.app.Activity.RESULT_OK;

public class Edit_selling_locations extends Fragment {


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private String mParam1;
    private String mParam2;


    EditText new_loc;
    Dialog myDialog;
    int PLACE_PICKER_REQUEST = 1;



    public Edit_selling_locations() {

    }


    public static Edit_selling_locations newInstance(String param1, String param2) {
        Edit_selling_locations fragment = new Edit_selling_locations();
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
        View selling = inflater.inflate(R.layout.fragment_edit_selling_locations, container, false);
        myDialog = new Dialog(getActivity());
        new_loc = selling.findViewById(R.id.new_loc);
        new_loc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogBox();
                Toast.makeText(getActivity(), "Clicked", Toast.LENGTH_SHORT).show();
            }
        });


        return selling;
    }


    private void dialogBox() {

        MaterialButton getLocation,fromMap;
        myDialog.setContentView(R.layout.chooselocation);
        myDialog.setCanceledOnTouchOutside(false);
        getLocation=(MaterialButton)myDialog.findViewById(R.id.use_geo_loc);
        fromMap=(MaterialButton)myDialog.findViewById(R.id.use_geo_map);
        getLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new_loc.setText(FarmerCommon.selling_location);
                myDialog.dismiss();
            }
        });

        fromMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                try {

                    assert getParentFragment() != null;
                    getActivity().startActivityForResult(builder.build(getActivity()),PLACE_PICKER_REQUEST);
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });


        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();


    }



//    @Override
//    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == PLACE_PICKER_REQUEST) {
//            if (resultCode == RESULT_OK) {
//                Place place = PlacePicker.getPlace(data,getActivity());
//                StringBuilder stringBuilder = new StringBuilder();
//                double latitude = place.getLatLng().latitude;
//                double longitude = place.getLatLng().longitude;
//                try {
//                    Geocoder geocoder;
//                    List<Address> addresses;
//                    geocoder = new Geocoder(getActivity(), Locale.getDefault());
//                    addresses = geocoder.getFromLocation(latitude,longitude,1);
//                    String address = addresses.get(0).getAddressLine(0);
//                    new_loc.setText(address);
//                    myDialog.dismiss();
//                    Toast.makeText(getActivity(), address, Toast.LENGTH_SHORT).show();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
//                }
//            }
//        }
//    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        for (Fragment fragment : getChildFragmentManager().getFragments()) {
            fragment.onActivityResult(requestCode, resultCode, data);

            Toast.makeText(getActivity(), "jdhd", Toast.LENGTH_SHORT).show();
        }
    }
}