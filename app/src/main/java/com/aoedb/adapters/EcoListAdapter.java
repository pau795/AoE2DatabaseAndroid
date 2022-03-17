package com.aoedb.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.aoedb.R;
import com.aoedb.data.EcoElement;
import com.aoedb.database.Utils;

import java.util.List;

public class EcoListAdapter extends ArrayAdapter<EcoElement> {
    private final List<EcoElement> itemList;
    Context ctx;



    public EcoListAdapter(Context context, List<EcoElement> list) {
        super(context,0, list);
        itemList = list;
        ctx = context;
    }

    public EcoElement getItem(int position){
        return itemList.get(position);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        final EcoElement l = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.eco_row, parent, false);
        }
        ImageView statIcon = convertView.findViewById(R.id.eco_stat_icon);

        ImageView resIcon = convertView.findViewById(R.id.eco_res_icon);
        TextView gatheringRate = convertView.findViewById(R.id.eco_gathering_rate);
        TextView statName = convertView.findViewById(R.id.eco_stat_name);
        statIcon.setImageResource(l.getStatIcon());
        statIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.showPopupIcon(v, ctx, l.getStatName(), l.getStatIcon(), false, "blue");
            }
        });
        statName.setText(l.getStatName());
        resIcon.setImageResource(l.getResourceIcon());
        gatheringRate.setText(Utils.getDecimalString(l.getGatheringRate(),2));
        return convertView;

    }
}
