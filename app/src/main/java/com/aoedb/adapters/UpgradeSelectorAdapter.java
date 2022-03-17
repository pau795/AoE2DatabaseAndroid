package com.aoedb.adapters;

import android.content.Context;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatCheckBox;

import com.aoedb.R;
import com.aoedb.data.Technology;
import com.aoedb.data.UpgradeElement;
import com.aoedb.database.Database;
import com.aoedb.database.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class UpgradeSelectorAdapter extends ArrayAdapter<UpgradeElement>{

    private final List<UpgradeElement> upgradeList;
    private final HashMap<Integer, UpgradeElement> indexMap;
    private final HashMap<AppCompatCheckBox, UpgradeElement> checkMap;
    private OnItemChangedListener listener;
    boolean enabled;

    Context ctx;

    public UpgradeSelectorAdapter(@NonNull Context context, int resource, List<UpgradeElement> list) {
        super(context, resource, list);
        upgradeList = list;
        indexMap = new HashMap<>();
        checkMap = new HashMap<>();
        for (UpgradeElement u: list) indexMap.put(u.getId(), u);
        enabled = false;
        ctx = context;
    }


    public boolean isEnabled() {
        return enabled;
    }

    public void filterList(int age, int civID){
        for(UpgradeElement u: upgradeList){
            Technology t = Database.getTechnology(u.getId());
            boolean b = Utils.mapAgeID(t.getAgeElement().getId()) <= age && t.isAvailableTo(civID);
            u.setEnabled(b);
            u.setSelected(b);
        }
        notifyDataSetChanged();
        enabled = true;
        if (listener != null) listener.onItemChanged(getSelectedElements());
    }



    private List<Integer> getSelectedElements(){
        List<Integer> list = new ArrayList<>();
        for(UpgradeElement u: upgradeList) if (u.isEnabled() && u.isSelected()) list.add(u.getId());
        return list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) convertView = LayoutInflater.from(getContext()).inflate(R.layout.upgrade_element, parent, false);
        final UpgradeElement u = upgradeList.get(position);
        TextView name = convertView.findViewById(R.id.list_name);
        name.setText(u.getName());
        ImageView image = convertView.findViewById(R.id.list_icon);
        image.setImageResource(u.getImage());
        final AppCompatCheckBox c = convertView.findViewById(R.id.list_checkbox);
        checkMap.put(c, u);
        c.setChecked(u.isSelected());
        if (!u.isEnabled()) {
            c.setEnabled(false);
            name.setEnabled(false);
            image.setEnabled(false);
            enableImage(image, false);
            convertView.setEnabled(false);
        }
        else{
            c.setEnabled(true);
            name.setEnabled(true);
            enableImage(image, true);
            convertView.setEnabled(true);
        }
        c.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setSelectedItems(checkMap.get(buttonView), isChecked);
                notifyDataSetChanged();
                if (listener != null) listener.onItemChanged(getSelectedElements());
            }
        });
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                c.performClick();
            }
        });
        return convertView;
    }



    private void setSelectedItems(UpgradeElement u, boolean selected){
        Technology t = Database.getTechnology(u.getId());
        u.setSelected(selected);
        int techID;
        if (selected) techID = t.getRequiredTechElement().getId();
        else techID = t.getNextUpgradeElement().getId();
        if (techID != 0 && indexMap.containsKey(techID) && indexMap.get(techID).isEnabled()) setSelectedItems(indexMap.get(techID), selected);
    }

    private void enableImage(ImageView v, boolean enabled)
    {
        if (!enabled) {
            ColorMatrix matrix = new ColorMatrix();
            matrix.setSaturation(0);  //0 means grayscale
            ColorMatrixColorFilter cf = new ColorMatrixColorFilter(matrix);
            v.setColorFilter(cf);
            v.setImageAlpha(128);   // 128 = 0.5
        }
        else{
            v.clearColorFilter();
            v.setImageAlpha(255);
        }
    }


    public void setOnItemChangedListener(OnItemChangedListener l){
        this.listener = l;
    }

    public interface OnItemChangedListener{
        void onItemChanged(List<Integer> list);
    }
}
