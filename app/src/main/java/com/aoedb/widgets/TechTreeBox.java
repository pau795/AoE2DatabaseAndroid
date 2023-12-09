package com.aoedb.widgets;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;

import com.aoedb.R;
import com.aoedb.activities.BuildingTabbedActivity;
import com.aoedb.activities.TechnologyTabbedActivity;
import com.aoedb.activities.UnitTabbedActivity;
import com.aoedb.data.Building;
import com.aoedb.data.Civilization;
import com.aoedb.data.Entity;
import com.aoedb.data.EntityElement;
import com.aoedb.data.Item;
import com.aoedb.data.TechTreeData;
import com.aoedb.data.Technology;
import com.aoedb.data.Unit;
import com.aoedb.database.Database;
import com.aoedb.database.Utils;

import java.io.ByteArrayOutputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;


public class TechTreeBox extends TechTreeItem {

    final static int UNIT = 0, BUILDING = 1, TECH = 2, UNIQUE_UNIT = 3;
    final static int ELITE_UNIQUE_UNIT = 4, CASTLE_UNIQUE_TECH = 5, IMP_UNIQUE_TECH = 6;
    final static int SPECIAL_UNIQUE_UNIT = 7;
    int entityType;
    int entityID;
    Context c;
    public static Integer civID = 1;
    Civilization civ;
    boolean enabled;

    public static TechTreeData techTreeData;



    public TechTreeBox(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.attrs = attrs;
        this.c = context;
        initView();
        setupWidget();
        techTreeData.addInstance(this);
    }

    protected void initView() {
        inflate(getContext(), R.layout.tech_tree_box, this);
        TypedArray array = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.TechTreeBox, 0,0);
        entityType = array.getInt(R.styleable.TechTreeBox_entity_type, UNIT);
        entityID = array.getInt(R.styleable.TechTreeBox_entity_id, 1);
        enabled = array.getBoolean(R.styleable.TechTreeBox_enabled, true);
        Set<Integer> specialBuildings = new HashSet<>();
        Collections.addAll(specialBuildings, 4, 20, 36, 37);
        Set<Integer> specialUnits = new HashSet<>();
        Collections.addAll(specialUnits, 80, 81, 90, 153, 92, 178, 183);

        if (entityType == UNIQUE_UNIT || entityType == ELITE_UNIQUE_UNIT || entityType == CASTLE_UNIQUE_TECH || entityType == IMP_UNIQUE_TECH || entityType == SPECIAL_UNIQUE_UNIT)
            techTreeData.addUniqueBox(this);
        if (entityType == BUILDING && specialBuildings.contains(entityID)) techTreeData.addUniqueBox(this);
        if (entityType == UNIT && specialUnits.contains(entityID)) techTreeData.addUniqueBox(this);
        FrameLayout f = findViewById(R.id.tt_box);
        displayBox();
        f.setOnClickListener(v -> createPopup());
    }

    public void displayBox(){

        ImageView im = findViewById(R.id.tt_box_image);
        TextView text = findViewById(R.id.tt_box_text);
        LinearLayout layout = findViewById(R.id.tt_box_layout);
        EntityElement l;
        switch (entityType){
            case UNIT:
                int unitBoxColor;
                //Remember to add the entityIDs on the specialUnit/Building Hashset to mark them as unique entities to reload
                if (entityID == 90 || entityID == 154 || entityID == 80 || entityID == 178 || entityID == 92 || entityID == 183 )  {
                    if (entityID == 90 || entityID == 154) {
                        if (civID == 34 || civID == 39) {
                            entityID = 154;
                            unitBoxColor = R.color.purple;
                        } else {
                            entityID = 90;
                            unitBoxColor = R.color.teal;
                        }
                    }
                    else if (entityID == 80 || entityID == 178) {
                        if (civID == 43){
                            entityID = 178;
                            this.setBottomConnector(false);
                            unitBoxColor = R.color.purple;
                        }
                        else {
                            entityID = 80;
                            this.setBottomConnector(true);
                            unitBoxColor = R.color.teal;

                        }
                    }
                    else {
                        if (civID == 23) {
                            entityID = 183;
                            unitBoxColor = R.color.purple;
                        }
                        else{
                            entityID = 92;
                            unitBoxColor = R.color.teal;
                        }

                    }
                }
                else unitBoxColor = R.color.teal;
                if (entityID == 81){
                    if (civID == 43) this.setVisibility(INVISIBLE);
                    else this.setVisibility(VISIBLE);
                }

                l = Database.getElement(Database.UNIT_LIST, entityID);
                layout.setBackgroundColor(c.getColor(unitBoxColor));
                break;
            case BUILDING:
                if (entityID == 4 || entityID == 34){
                    if (civID == 39) entityID = 34;
                    else entityID = 4;
                }
                if (entityID == 20 || entityID == 37){
                    if (civID == 44 || civID == 45) entityID = 37;
                    else entityID = 20;
                }
                l = Database.getElement(Database.BUILDING_LIST, entityID);
                layout.setBackgroundColor(c.getColor(R.color.red));
                break;
            case TECH:
                l = Database.getElement(Database.TECH_LIST, entityID);
                layout.setBackgroundColor(c.getColor(R.color.green));
                break;
            case UNIQUE_UNIT:
                civ = Database.getCivilization(civID);
                entityID = civ.getUniqueUnit();
                l = Database.getElement(Database.UNIT_LIST, entityID);
                layout.setBackgroundColor(c.getColor(R.color.purple));
                break;
            case ELITE_UNIQUE_UNIT:
                civ = Database.getCivilization(civID);
                entityID = civ.getEliteUniqueUnit(civ.getUniqueUnit());
                l = Database.getElement(Database.UNIT_LIST, entityID);
                layout.setBackgroundColor(c.getColor(R.color.purple));
                break;
            case CASTLE_UNIQUE_TECH:
                civ = Database.getCivilization(civID);
                entityID = civ.getUniqueTechList().get(0);
                l = Database.getElement(Database.TECH_LIST, entityID);
                layout.setBackgroundColor(c.getColor(R.color.green));
                break;
            case IMP_UNIQUE_TECH:
                civ = Database.getCivilization(civID);
                entityID = civ.getUniqueTechList().get(1);
                l = Database.getElement(Database.TECH_LIST, entityID);
                layout.setBackgroundColor(c.getColor(R.color.green));
                break;
            default:
                if (entityID == 23 || entityID == 180){
                    if (civID == 44) entityID = 180;
                    else entityID = 23;
                }
                l = Database.getElement(Database.UNIT_LIST, entityID);
                layout.setBackgroundColor(c.getColor(R.color.purple));
                break;
        }

        text.setText(l.getName());

        //COMPRESS TECH TREE IMAGES TO LOWER THE AMOUNT OF RAM USED

        Bitmap bitmap = ((BitmapDrawable) ResourcesCompat.getDrawable(getResources(), l.getImage(), null)).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 4;
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] data = stream.toByteArray();
        Drawable d = new BitmapDrawable(getResources(), BitmapFactory.decodeByteArray(data, 0, data.length, options));
        im.setBackground(d);
    }

    public void setEnabled(boolean enabled){
        this.enabled = enabled;
        ImageView im = findViewById(R.id.tt_box_image);
        if (enabled) im.setImageDrawable(null);
        else im.setImageResource(R.drawable.t_cancel);
    }

    public void setAvailability(){
        boolean b;
        if (entityType == UNIT || entityType == SPECIAL_UNIQUE_UNIT) b = Database.getUnit(entityID).isAvailableTo(civID);
        else if (entityType == TECH) b = Database.getTechnology(entityID).isAvailableTo(civID);
        else if (entityType == BUILDING) b = Database.getBuilding(entityID).isAvailableTo(civID);
        else b = entityType == UNIQUE_UNIT || entityType == ELITE_UNIQUE_UNIT || entityType == CASTLE_UNIQUE_TECH || entityType == IMP_UNIQUE_TECH;
        setEnabled(b);
    }


    private void fillPopup(View layout, Unit u){
        fillPopupItem(layout, u);

        TextView mArmorValue = layout.findViewById(R.id.popup_melee_armor);
        double mArmor = u.getBaseStat(Database.MELEE_ARMOR);
        if (!Double.isNaN(mArmor)) {
            mArmorValue.setText(Utils.getDecimalString(mArmor, 0));
            ImageView mArmorIcon = layout.findViewById(R.id.popup_melee_armor_icon);
            mArmorValue.setVisibility(View.VISIBLE);
            mArmorIcon.setVisibility(View.VISIBLE);
        }
        TextView pArmorValue = layout.findViewById(R.id.popup_pierce_armor);
        double pArmor = u.getBaseStat(Database.PIERCE_ARMOR);
        if (!Double.isNaN(pArmor)) {
            pArmorValue.setText(Utils.getDecimalString(pArmor, 0));
            ImageView pArmorIcon = layout.findViewById(R.id.popup_pierce_armor_icon);
            pArmorValue.setVisibility(View.VISIBLE);
            pArmorIcon.setVisibility(View.VISIBLE);
        }

        int techID = u.getRequiredTechElement().getId();
        if (techID != 0){
            LinearLayout l = layout.findViewById(R.id.popup_upgrade_layout);
            l.setVisibility(View.VISIBLE);
            Technology t = Database.getTechnology(techID);
            HashMap<String, Integer> techCost = t.getBaseCost();
            int j = 1;
            for (String r: techCost.keySet()) {
                switch (j){
                    case 1:
                        ImageView res1Icon = layout.findViewById(R.id.popup_upgrade_res1_icon);
                        TextView res1Value = layout.findViewById(R.id.popup_upgrade_res1_value);
                        res1Icon.setImageResource(Utils.getResourceIcon(r));
                        res1Value.setText(Utils.getDecimalString(techCost.get(r), 0));
                        break;
                    case 2:
                        ImageView res2Icon = layout.findViewById(R.id.popup_upgrade_res2_icon);
                        TextView res2Value = layout.findViewById(R.id.popup_upgrade_res2_value);
                        res2Icon.setImageResource(Utils.getResourceIcon(r));
                        res2Value.setText(Utils.getDecimalString(techCost.get(r), 0));
                        res2Icon.setVisibility(View.VISIBLE);
                        res2Value.setVisibility(View.VISIBLE);
                        break;
                }
                ++j;
            }
        }

        Button b = layout.findViewById(R.id.popup_more_info_button);
        b.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(c, UnitTabbedActivity.class);
                i.putExtra(Database.ENTITY, entityID);
                Database.setSelectedCiv(Database.UNIT, entityID, civID);
                c.startActivity(i);
            }
        });

    }

    private void fillPopupEntity(View layout, Entity e){
        TextView nameText = layout.findViewById(R.id.popup_name);
        nameText.setText(e.getName());
        ImageView iconName = layout.findViewById(R.id.icon_name);
        iconName.setImageResource(e.getNameElement().getImage());
        HashMap<String, Integer> cost = e.getBaseCost();
        int i = 1;
        for (String r: cost.keySet()) {
            switch (i){
                case 1:
                    ImageView res1Icon = layout.findViewById(R.id.popup_res1_icon);
                    TextView res1Value = layout.findViewById(R.id.popup_res1_value);
                    res1Icon.setImageResource(Utils.getResourceIcon(r));
                    res1Value.setText(Utils.getDecimalString(cost.get(r), 0));
                    break;
                case 2:
                    ImageView res2Icon = layout.findViewById(R.id.popup_res2_icon);
                    TextView res2Value = layout.findViewById(R.id.popup_res2_value);
                    res2Icon.setImageResource(Utils.getResourceIcon(r));
                    res2Value.setText(Utils.getDecimalString(cost.get(r), 0));
                    res2Icon.setVisibility(View.VISIBLE);
                    res2Value.setVisibility(View.VISIBLE);
                    break;
                case 3:
                    ImageView res3Icon = layout.findViewById(R.id.popup_res3_icon);
                    TextView res3Value = layout.findViewById(R.id.popup_res3_value);
                    res3Icon.setImageResource(Utils.getResourceIcon(r));
                    res3Value.setText(Utils.getDecimalString(cost.get(r), 0));
                    res3Icon.setVisibility(View.VISIBLE);
                    res3Value.setVisibility(View.VISIBLE);
                    break;
            }
            ++i;
        }
        TextView descriptionText = layout.findViewById(R.id.popup_description);
        descriptionText.setText(e.getDescriptor().getBriefDescription());


        TextView timeValue = layout.findViewById(R.id.popup_time);
        double time = e.getBaseStat(Database.TRAINING_TIME);
        timeValue.setText(Utils.getDecimalString(time, 0));

    }

    private void fillPopupItem(View layout, Item it){
        fillPopupEntity(layout, it);

        TextView hpValue = layout.findViewById(R.id.popup_hp);
        double hp = it.getBaseStat(Database.HP);
        if (!Double.isNaN(hp)) {
            hpValue.setText(Utils.getDecimalString(hp, 0));
            ImageView hpIcon = layout.findViewById(R.id.popup_hp_icon);
            View v = layout.findViewById(R.id.popup_time_space);
            hpValue.setVisibility(View.VISIBLE);
            hpIcon.setVisibility(View.VISIBLE);
            v.setVisibility(VISIBLE);
        }

        TextView attackValue = layout.findViewById(R.id.popup_attack);
        double attack = it.getBaseStat(Database.ATTACK);
        if (!Double.isNaN(attack)) {
            attackValue.setText(Utils.getDecimalString(attack, 0));
            ImageView attackIcon = layout.findViewById(R.id.popup_attack_icon);
            attackValue.setVisibility(View.VISIBLE);
            attackIcon.setVisibility(View.VISIBLE);
        }

        TextView rangeValue = layout.findViewById(R.id.popup_range);
        double range = it.getBaseStat(Database.RANGE);
        if (!Double.isNaN(range)) {
            rangeValue.setText(Utils.getDecimalString(range, 0));
            ImageView rangeIcon = layout.findViewById(R.id.popup_range_icon);
            rangeValue.setVisibility(View.VISIBLE);
            rangeIcon.setVisibility(View.VISIBLE);
        }
    }

    private void fillPopup(View layout, Technology t){
        fillPopupEntity(layout, t);
        Button b = layout.findViewById(R.id.popup_more_info_button);
        b.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(c, TechnologyTabbedActivity.class);
                i.putExtra(Database.ENTITY, entityID);
                Database.setSelectedCiv(Database.TECH, entityID, civID);
                c.startActivity(i);
            }
        });
    }

    private void fillPopup(View layout, Building b){
        fillPopupItem(layout, b);
        Button bt = layout.findViewById(R.id.popup_more_info_button);
        bt.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(c, BuildingTabbedActivity.class);
                i.putExtra(Database.ENTITY, entityID);
                Database.setSelectedCiv(Database.BUILDING, entityID, civID);
                c.startActivity(i);
            }
        });
    }

    public void createPopup(){
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.tech_tree_popup,null);
        int w =(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 350, getResources().getDisplayMetrics());
        int h = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 375, getResources().getDisplayMetrics());
        final PopupWindow pw = new PopupWindow(layout, w, h, true);
        if (entityType == UNIT || entityType == UNIQUE_UNIT || entityType == ELITE_UNIQUE_UNIT || entityType == SPECIAL_UNIQUE_UNIT)
            fillPopup(layout, Database.getUnit(entityID));
        else if (entityType == TECH || entityType == CASTLE_UNIQUE_TECH || entityType == IMP_UNIQUE_TECH)
            fillPopup(layout, Database.getTechnology(entityID));
        else fillPopup(layout, Database.getBuilding(entityID));
        pw.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pw.setOutsideTouchable(true);
        pw.setTouchInterceptor(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                    pw.dismiss();
                    return true;
                }
                return false;
            }
        });
        pw.showAsDropDown(this);
    }



}
