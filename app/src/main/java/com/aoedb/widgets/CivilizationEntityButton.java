package com.aoedb.widgets;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.aoedb.R;
import com.aoedb.activities.BuildingTabbedActivity;
import com.aoedb.activities.CivilizationViewActivity;
import com.aoedb.activities.TechnologyTabbedActivity;
import com.aoedb.activities.UnitTabbedActivity;
import com.aoedb.data.Civilization;
import com.aoedb.data.EntityElement;
import com.aoedb.database.Database;
import com.aoedb.database.Utils;

public class CivilizationEntityButton extends LinearLayout {

    final static int UNIQUE_UNIT1 = 0, UNIQUE_UNIT2 = 1, UNIQUE_UNIT3 = 2, CASTLE_UNIQUE_TECH = 3, IMPERIAL_UNIQUE_TECH = 4, UNIQUE_BUILDING1 = 5, UNIQUE_BUILDING2 = 6;

    int buttonType;
    AttributeSet attrs;

    public CivilizationEntityButton(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.attrs = attrs;
        initView();
    }

    protected void initView() {
        inflate(getContext(), R.layout.civilization_entity_button, this);

        TypedArray array = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.CivilizationEntityButton, 0,0);
        buttonType = array.getInt(R.styleable.CivilizationEntityButton_button_type, 0);
    }

    public void setupButton(final Civilization civ, final CivilizationViewActivity.OnItemClicked listener, final boolean click){
        final TextView nameText = findViewById(R.id.button_name);
        TextView descriptionText = findViewById(R.id.button_description);
        ImageView icon = findViewById(R.id.button_icon);


        final int entityID;
        final String type;
        switch (buttonType){
            case UNIQUE_UNIT1:
                nameText.setText(Database.getUnit(civ.getUniqueUnitList().get(0)).getName());
                descriptionText.setText(Database.getUnit(civ.getUniqueUnitList().get(0)).getDescriptor().getQuickDescription());
                icon.setImageResource(Database.getUnit(civ.getUniqueUnitList().get(0)).getNameElement().getImage());
                entityID = civ.getUniqueUnitList().get(0);
                type = Database.UNIT;
                break;
            case UNIQUE_UNIT2:
                nameText.setText(Database.getUnit(civ.getUniqueUnitList().get(1)).getName());
                descriptionText.setText(Database.getUnit(civ.getUniqueUnitList().get(1)).getDescriptor().getQuickDescription());
                icon.setImageResource(Database.getUnit(civ.getUniqueUnitList().get(1)).getNameElement().getImage());
                entityID = civ.getUniqueUnitList().get(1);
                type = Database.UNIT;
                break;
            case UNIQUE_UNIT3:
                nameText.setText(Database.getUnit(civ.getUniqueUnitList().get(2)).getName());
                descriptionText.setText(Database.getUnit(civ.getUniqueUnitList().get(2)).getDescriptor().getQuickDescription());
                icon.setImageResource(Database.getUnit(civ.getUniqueUnitList().get(2)).getNameElement().getImage());
                entityID = civ.getUniqueUnitList().get(2);
                type = Database.UNIT;
                break;
            case CASTLE_UNIQUE_TECH:
                nameText.setText(Database.getTechnology(civ.getUniqueTechList().get(0)).getName());
                descriptionText.setText(Database.getTechnology(civ.getUniqueTechList().get(0)).getDescriptor().getQuickDescription());
                icon.setImageResource(Database.getTechnology(civ.getUniqueTechList().get(0)).getNameElement().getImage());
                entityID = civ.getUniqueTechList().get(0);
                type = Database.TECH;
                break;
            case IMPERIAL_UNIQUE_TECH:
                nameText.setText(Database.getTechnology(civ.getUniqueTechList().get(1)).getName());
                descriptionText.setText(Database.getTechnology(civ.getUniqueTechList().get(1)).getDescriptor().getQuickDescription());
                icon.setImageResource(Database.getTechnology(civ.getUniqueTechList().get(1)).getNameElement().getImage());
                entityID = civ.getUniqueTechList().get(1);
                type = Database.TECH;
                break;
            case UNIQUE_BUILDING1:
                nameText.setText(Database.getBuilding(civ.getUniqueBuildingList().get(0)).getName());
                descriptionText.setText(Database.getBuilding(civ.getUniqueBuildingList().get(0)).getDescriptor().getQuickDescription());
                icon.setImageResource(Database.getBuilding(civ.getUniqueBuildingList().get(0)).getNameElement().getImage());
                entityID = civ.getUniqueBuildingList().get(0);
                type = Database.BUILDING;
                break;
            case UNIQUE_BUILDING2:
                nameText.setText(Database.getBuilding(civ.getUniqueBuildingList().get(1)).getName());
                descriptionText.setText(Database.getBuilding(civ.getUniqueBuildingList().get(1)).getDescriptor().getQuickDescription());
                icon.setImageResource(Database.getBuilding(civ.getUniqueBuildingList().get(1)).getNameElement().getImage());
                entityID = civ.getUniqueBuildingList().get(1);
                type = Database.BUILDING;
                break;
            default:
                entityID = 0;
                type = "";
                break;
        }

        icon.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                EntityElement e = Database.getElement(type +"_list", entityID);
                Utils.showPopupIcon(v, getContext(), e.getName(), e.getImage(), false, "red");
            }
        });
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (entityID != 0 && click) {
                    Intent i;
                    switch (type) {
                        case Database.UNIT:
                            i = new Intent(getContext(), UnitTabbedActivity.class);
                            break;
                        case Database.BUILDING:
                            i = new Intent(getContext(), BuildingTabbedActivity.class);
                            break;
                        case Database.TECH:
                            i = new Intent(getContext(), TechnologyTabbedActivity.class);
                            break;
                        default:
                            i = new Intent();
                            break;
                    }
                    i.putExtra(Database.ENTITY, entityID);
                    int civID = civ.getEntityID();
                    Database.setSelectedCiv(type, entityID, civID); //set clicked element civID
                    listener.onItemClicked();
                    getContext().startActivity(i);
                }
            }
        });
    }
}
