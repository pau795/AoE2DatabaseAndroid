package com.aoedb.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.aoedb.R;
import com.aoedb.activities.CivilizationViewActivity;
import com.aoedb.data.EntityElement;
import com.aoedb.database.Database;
import com.aoedb.database.Utils;

import java.util.List;

public class TwoColumnAdapter extends ArrayAdapter<EntityElement> {

    private final List<EntityElement> itemList;
    Context ctx;

    public TwoColumnAdapter(Context context, List<EntityElement> list) {
        super(context,0, list);
        itemList = list;
        ctx = context;
    }

    public EntityElement getItem(int position){
            return itemList.get(position);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        final EntityElement elem = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.two_column_row, parent, false);
        }
        ImageView im = convertView.findViewById(R.id.list_icon1);
        im.setImageResource(elem.getImage());
        im.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.showPopupIcon(v, ctx, elem.getName(), elem.getImage(), elem.getType().equals(Database.CIV), "blue");
            }
        });
        if (elem.getType().equals(Database.CIV)) im.setBackground(null);
        else im.setBackgroundColor(ctx.getColor(R.color.blue_border));
        TextView t = convertView.findViewById(R.id.list_name1);
        t.setText(elem.getName());
        LinearLayout layout = convertView.findViewById(R.id.list_layout);
        if (elem.getType().equals(Database.CIV)) {
            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(ctx, CivilizationViewActivity.class);
                    i.putExtra(Database.CIV, elem.getId());
                    ctx.startActivity(i);
                }
            });
        }
        return convertView;

    }
}