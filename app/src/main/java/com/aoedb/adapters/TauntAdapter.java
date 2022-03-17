package com.aoedb.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.aoedb.R;
import com.aoedb.data.TauntElement;

import java.util.List;

public class TauntAdapter extends ArrayAdapter<TauntElement> {

    private final List<TauntElement> itemList;

    public TauntAdapter(Context context, List<TauntElement> list) {
        super(context,0, list);
        itemList = list;
    }

    public TauntElement getItem(int position){
            return itemList.get(position);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        TauntElement l3 = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.taunt_row, parent, false);
        }
        TextView t = convertView.findViewById(R.id.element_name);
        t.setText(l3.getName());
        return convertView;
    }
}