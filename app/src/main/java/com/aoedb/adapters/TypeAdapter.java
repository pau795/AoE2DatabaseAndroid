package com.aoedb.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.aoedb.R;
import com.aoedb.data.TypeElement;
import com.aoedb.database.Utils;

import java.util.HashMap;
import java.util.List;

public class TypeAdapter extends BaseExpandableListAdapter {

    private final Context context;
    private final List<String> expandableListTitle;
    private final HashMap<String, List<TypeElement>> expandableListDetail;

    public TypeAdapter(Context context, List<String> expandableListTitle,
                       HashMap<String, List<TypeElement>> expandableListDetail) {
        this.context = context;
        this.expandableListTitle = expandableListTitle;
        this.expandableListDetail = expandableListDetail;
    }

    @Override
    public TypeElement getChild(int listPosition, int expandedListPosition) {
        return this.expandableListDetail.get(this.expandableListTitle.get(listPosition))
                .get(expandedListPosition);
    }

    @Override
    public long getChildId(int listPosition, int expandedListPosition) {
        return expandedListPosition;
    }


    @Override
    public View getChildView(int listPosition, final int expandedListPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        final TypeElement elem = getChild(listPosition, expandedListPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.type_row, null);
        }
        TextView text = convertView.findViewById(R.id.list_name1);
        text.setText(elem.getName());
        ImageView img = convertView.findViewById(R.id.list_icon1);
        img.setImageResource(elem.getImage());
        if(elem.getImage() == R.drawable.t_white) img.setBackground(null);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.showPopupIcon(v, context, elem.getName(), elem.getImage(), false, "blue");
            }
        });
        if (!elem.getValue().equals("")) {
            TextView value = convertView.findViewById(R.id.list_value);
            value.setText(elem.getValue());
        }
        return convertView;
    }

    @Override
    public int getChildrenCount(int listPosition) {
        return this.expandableListDetail.get(this.expandableListTitle.get(listPosition))
                .size();
    }

    @Override
    public String getGroup(int listPosition) {
        return this.expandableListTitle.get(listPosition);
    }

    @Override
    public int getGroupCount() {
        return this.expandableListTitle.size();
    }

    @Override
    public long getGroupId(int listPosition) {
        return listPosition;
    }

    @Override
    public View getGroupView(int listPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String elem = getGroup(listPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.entity_group, null);
        }
        TextView text = convertView.findViewById(R.id.list_group_name1);
        text.setText(elem);
        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int listPosition, int expandedListPosition) {
        return true;
    }
}