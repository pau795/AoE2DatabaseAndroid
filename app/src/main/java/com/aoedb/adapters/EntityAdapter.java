package com.aoedb.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aoedb.R;
import com.aoedb.activities.BuildingTabbedActivity;
import com.aoedb.activities.CivilizationViewActivity;
import com.aoedb.activities.HistoryViewActivity;
import com.aoedb.activities.TechnologyTabbedActivity;
import com.aoedb.activities.UnitTabbedActivity;
import com.aoedb.data.EntityElement;
import com.aoedb.database.Database;
import com.aoedb.database.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class EntityAdapter extends BaseExpandableListAdapter {

    private final Context context;
    private final List<String> expandableListTitle;
    private final HashMap<String, List<EntityElement>> expandableListDetail;
    private final int entityID;
    private final String entityType;

    public EntityAdapter(Context context, HashMap<String, List<EntityElement>> expandableListDetail) {
        this.context = context;
        this.expandableListTitle = new ArrayList<>(expandableListDetail.keySet());
        this.expandableListDetail = expandableListDetail;
        this.entityID = -1;
        this.entityType = Database.NONE;
    }

    public EntityAdapter(Context context, HashMap<String, List<EntityElement>> expandableListDetail, int entityID, String entityType) {
        this.context = context;
        this.expandableListTitle = new ArrayList<>(expandableListDetail.keySet());
        this.expandableListDetail = expandableListDetail;
        this.entityID = entityID;
        this.entityType = entityType;
    }

    public EntityAdapter(Context context, HashMap<String, List<EntityElement>> expandableListDetail, String filter) {
        this.context = context;
        HashMap<String,List<EntityElement>> filteredMap = new LinkedHashMap<>();
        for (String h: expandableListDetail.keySet()){
            List<EntityElement> l = expandableListDetail.get(h);
            List<EntityElement> filteredList = new ArrayList<>();
            for (EntityElement e : l){
                if (e.getName().toLowerCase().startsWith(filter.toLowerCase()) || e.getName().toLowerCase().contains(filter.toLowerCase()))
                    filteredList.add(e);

            }
            if (!filteredList.isEmpty()) filteredMap.put(h, filteredList);
        }
        this.expandableListTitle = new ArrayList<>(filteredMap.keySet());
        this.expandableListDetail = filteredMap;
        this.entityID = -1;
        this.entityType = Database.NONE;
    }

    @Override
    public EntityElement getChild(int listPosition, int expandedListPosition) {
        return this.expandableListDetail.get(this.expandableListTitle.get(listPosition))
                .get(expandedListPosition);
    }

    @Override
    public long getChildId(int listPosition, int expandedListPosition) {
        return expandedListPosition;
    }


    @Override
    public View getChildView(final int listPosition, final int expandedListPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        final EntityElement elem = getChild(listPosition, expandedListPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.entity_row, null);
        }
        TextView text = convertView.findViewById(R.id.list_name1);
        text.setText(elem.getName());
        ImageView img = convertView.findViewById(R.id.list_icon1);
        img.setImageResource(elem.getImage());
        setIconBorder(img, getPopupColor(elem), listPosition, expandedListPosition);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.showPopupIcon(v, context, elem.getName(), elem.getImage(), popupIsTransparent(listPosition, expandedListPosition), getPopupColor(elem));
            }
        });
        LinearLayout layout = convertView.findViewById(R.id.list_layout);
        if (isClickableEntity(elem)) {
            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i;
                    int civ = -1;
                    switch (elem.getType()) {
                        case Database.UNIT:
                            i = new Intent(context, UnitTabbedActivity.class);
                            if (!entityType.equals(Database.NONE)) civ = Database.getSelectedCiv(entityType, entityID);
                            Database.setSelectedCiv(Database.UNIT, elem.getId(), civ);
                            i.putExtra(Database.ENTITY, elem.getId());
                            break;
                        case Database.BUILDING:
                            i = new Intent(context, BuildingTabbedActivity.class);
                            if (!entityType.equals(Database.NONE)) civ = Database.getSelectedCiv(entityType, entityID);
                            Database.setSelectedCiv(Database.BUILDING, elem.getId(), civ);
                            i.putExtra(Database.ENTITY, elem.getId());
                            break;
                        case Database.TECH:
                            i = new Intent(context, TechnologyTabbedActivity.class);
                            if (!entityType.equals(Database.NONE)) civ = Database.getSelectedCiv(entityType, entityID);
                            Database.setSelectedCiv(Database.TECH, elem.getId(), civ);
                            i.putExtra(Database.ENTITY, elem.getId());
                            break;
                        case Database.CIV:
                            i = new Intent(context, CivilizationViewActivity.class);
                            i.putExtra(Database.CIV, elem.getId());
                            break;
                        case "history":
                            i = new Intent(context, HistoryViewActivity.class);
                            i.putExtra("id", elem.getId());
                            i.putExtra("name", elem.getName());
                            break;
                        default:
                            i = new Intent();
                            break;
                    }
                    context.startActivity(i);
                }
            });
        }

        return convertView;
    }

    private void setIconBorder(ImageView icon, String color, int listPosition, int expandedListPosition){
        EntityElement elem = getChild(listPosition, expandedListPosition);
        switch (color){
            case Database.BLUE:
                icon.setBackgroundColor(context.getColor(R.color.blue_border));
                break;
            case Database.RED:
                icon.setBackground(null);
                break;
            case Database.GREEN:
                if(expandableListTitle.get(listPosition).equals("Civilizations") || elem.getName().equals("Middle Ages")) icon.setBackground(null);
                else icon.setBackgroundColor(context.getColor(R.color.green_border));
                break;
        }
    }

    private boolean popupIsTransparent(int listPosition, int expandedListPosition){
        EntityElement elem = getChild(listPosition, expandedListPosition);
        return elem.getType().equals(Database.CIV) || expandableListTitle.get(listPosition).equals("Civilizations") || elem.getName().equals("Middle Ages")
            || expandableListTitle.get(listPosition).equals("Civilizaciones") || elem.getName().equals("La Edad Media");
    }


    private boolean isClickableEntity(EntityElement e){
        switch (e.getType()){
            case Database.UNIT:
            case Database.BUILDING:
            case Database.TECH:
            case Database.CIV:
            case Database.HISTORY:
                return true;
            default: return false;
        }
    }

    private String getPopupColor(EntityElement e){
        switch (e.getType()){
            case Database.UNIT:
            case Database.BUILDING:
            case Database.TECH:
            case Database.CLASS:
            case Database.TYPE:
            case Database.PERFORMANCE: return Database.BLUE;
            case Database.CIV: return Database.RED;
            case Database.HISTORY: return Database.GREEN;
            default: return Database.EMPTY;
        }
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
            LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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