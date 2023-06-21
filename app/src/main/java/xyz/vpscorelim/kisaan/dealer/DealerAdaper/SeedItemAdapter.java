package xyz.vpscorelim.kisaan.dealer.DealerAdaper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

import xyz.vpscorelim.kisaan.R;
import xyz.vpscorelim.kisaan.dealer.DealerModel.SeedModel;

public class SeedItemAdapter  extends ArrayAdapter<SeedModel> {


    public SeedItemAdapter(Context context, ArrayList<SeedModel> fruitsItemsArrayList) {
        super(context, 0, fruitsItemsArrayList);
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    private View initView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.seed_spinner_row, parent, false
            );
        }
        ImageView imageViewFlag = convertView.findViewById(R.id.image_view_flag);
        TextView textViewName = convertView.findViewById(R.id.text_view_name);
        SeedModel currentItem = getItem(position);
        if (currentItem != null) {
            imageViewFlag.setImageResource(currentItem.getSeedImage());
            textViewName.setText(currentItem.getSeedName());
        }
        return convertView;
    }
}




