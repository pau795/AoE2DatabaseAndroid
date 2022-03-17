package com.aoedb.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.aoedb.R;
import com.aoedb.database.Database;

import java.util.List;

public class TechTreeSpinnerAdapter extends ArrayAdapter<String> {

    Context context;
    int selected;

    public TechTreeSpinnerAdapter(@NonNull Context context, int resource, @NonNull List<String> objects) {
        super(context, resource, objects);
        this.context = context;
    }

    public void setSelected(int position){
        selected = position;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.spinner_item_image, parent, false);
        }
        ImageView icon = view.findViewById(R.id.spinner_icon);
        String civName = getItem(position);
        int civID = Database.getReversedCivNameMap().get(civName);
        int iconID = Database.getCivilization(civID).getNameElement().getImage();
        icon.setImageResource(iconID);

        return view;

    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(android.R.layout.simple_spinner_dropdown_item, parent, false);
        }
        TextView t = (TextView) view;
        t.setText(getItem(position));
        if (position ==  selected) t.setTypeface(Typeface.DEFAULT_BOLD);
        else t.setTypeface(Typeface.DEFAULT);
        return view;
    }
}
