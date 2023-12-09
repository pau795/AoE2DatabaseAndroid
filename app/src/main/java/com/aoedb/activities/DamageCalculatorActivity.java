package com.aoedb.activities;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatAutoCompleteTextView;
import androidx.appcompat.widget.AppCompatCheckBox;

import com.aoedb.R;
import com.aoedb.adapters.TextFilterAdapter;
import com.aoedb.adapters.TypeAdapter;
import com.aoedb.data.DamageCalculator;
import com.aoedb.data.DamageCalculator.UnitStats;
import com.aoedb.data.DamageCalculator.UnitStats.AttackValues;
import com.aoedb.data.EntityElement;
import com.aoedb.data.TypeElement;
import com.aoedb.data.Unit;
import com.aoedb.database.Database;
import com.aoedb.database.Utils;
import com.aoedb.widgets.AgeTwoCivsSelector;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import pl.droidsonroids.gif.GifImageView;

public class DamageCalculatorActivity extends DrawerActivity {

    List<String> unitNames;
    List<Integer> unitList;
    HashMap<String, Integer> unitRelation;
    HashMap<Integer, String> civRelation;


    Unit unit1, unit2;
    int civ1ID, civ2ID;
    List<Integer> unit1Upgrades, unit2Upgrades;
    int ageID;

    TextView unit1ResultText;
    TextView unit2ResultText;
    TextView unit1DamageText;
    TextView unit2DamageText;
    TextView unit1ChargeDamageText;
    TextView unit2ChargeDamageText;
    TextView unit1HitsText;
    TextView unit2HitsText;
    TextView unit1TimeText;
    TextView unit1HitsPerformedText;
    TextView unit2HitsPerformedText;
    TextView unit2TimeText;
    TextView unit1HPLeftText;
    TextView unit2HPLeftText;
    TextView unit1DPSText;
    TextView unit2DPSText;
    TextView unit1CostEfText;
    TextView unit2CostEfText;
    TextView unit1PopEfText;
    TextView unit2PopEfText;
    Context c;

    AgeTwoCivsSelector selector;

    AppCompatCheckBox hill1, hill2;

    DamageCalculator calculator;

    int unit1Relics, unit2Relics;

    AppCompatAutoCompleteTextView selector1, selector2;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.title_damage_calculator_activity);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_damage_calculator, null, false);
        setActivityView(contentView);
        setAppBarColor(getColor(R.color.blue_background));

        civRelation = Database.getCivNameMap();

        unit1ResultText = findViewById(R.id.dc_unit1_results);
        unit2ResultText = findViewById(R.id.dc_unit2_results);
        unit1DamageText = findViewById(R.id.dc_unit1_damage);
        unit2DamageText = findViewById(R.id.dc_unit2_damage);
        unit1ChargeDamageText = findViewById(R.id.dc_unit1_damage_charge);
        unit2ChargeDamageText = findViewById(R.id.dc_unit2_damage_charge);;
        unit1HitsText = findViewById(R.id.dc_unit1_hits);
        unit2HitsText = findViewById(R.id.dc_unit2_hits);
        unit1TimeText = findViewById(R.id.dc_unit1_time);
        unit2TimeText = findViewById(R.id.dc_unit2_time);
        unit1HitsPerformedText = findViewById(R.id.dc_unit1_hits_performed);
        unit2HitsPerformedText = findViewById(R.id.dc_unit2_hits_performed);
        unit1HPLeftText = findViewById(R.id.dc_unit1_hp_left);
        unit2HPLeftText = findViewById(R.id.dc_unit2_hp_left);
        unit1DPSText = findViewById(R.id.dc_unit1_dps);
        unit2DPSText = findViewById(R.id.dc_unit2_dps);
        unit1CostEfText = findViewById(R.id.dc_unit1_cost_efficiency);
        unit2CostEfText = findViewById(R.id.dc_unit2_cost_efficiency);
        unit1PopEfText = findViewById(R.id.dc_unit1_pop_efficiency);
        unit2PopEfText = findViewById(R.id.dc_unit2_pop_efficiency);

        hill1 = findViewById(R.id.unit1_hill_checkbox);
        hill2 = findViewById(R.id.unit2_hill_checkbox);

        c = this;
        getUnitsInfo();
        setupUnitSelectors();
        int unit1ID = getIntent().getIntExtra("unit1", 1);
        int unit2ID = getIntent().getIntExtra("unit2", 1);
        civ1ID = getIntent().getIntExtra("civ1", -1);
        civ2ID = getIntent().getIntExtra("civ2", -1);
        unit1 = new Unit(Database.getUnit(unit1ID));
        unit2 = new Unit(Database.getUnit(unit2ID));
        unit1Upgrades = unit1.getUpgradesIds();
        unit2Upgrades = unit2.getUpgradesIds();
        selector.setOnAgeChangeListener(new AgeTwoCivsSelector.OnAgeChangeListener() {
            @Override
            public void onAgeChanged(int age) {
                ageID = age;
                calculateStats();
            }
        });

        setUnit1(unit1ID);
        setUnit2(unit2ID);
        calculateStats();
        selector1.setHint(Database.getUnit(unit1ID).getName());
        selector2.setHint(Database.getUnit(unit2ID).getName());
        setupPopUps();
        setupRelicsLayout();
    }

    private void setupRelicsLayout(){
        ImageView unit1Ru = findViewById(R.id.dc_unit1_relic_up);
        ImageView unit1Rd = findViewById(R.id.dc_unit1_relic_down);
        final TextView unit1Rn = findViewById(R.id.dc_unit1_relic_number);
        ImageView unit2Ru = findViewById(R.id.dc_unit2_relic_up);
        ImageView unit2Rd = findViewById(R.id.dc_unit2_relic_down);
        final TextView unit2Rn = findViewById(R.id.dc_unit2_relic_number);


        unit1Relics = 0;
        unit2Relics = 0;
        unit1Rn.setText(String.valueOf(unit1Relics));
        unit2Rn.setText(String.valueOf(unit2Relics));
        calculateStats();
        unit1Ru.setOnClickListener(v -> {
            if (unit1Relics < 4) ++unit1Relics;
            unit1Rn.setText(String.valueOf(unit1Relics));
            calculateStats();
        });
        unit1Rd.setOnClickListener(v -> {
            if (unit1Relics > 0) --unit1Relics;
            unit1Rn.setText(String.valueOf(unit1Relics));
            calculateStats();
        });
        unit2Ru.setOnClickListener(v -> {
            if (unit2Relics < 4) ++unit2Relics;
            unit2Rn.setText(String.valueOf(unit2Relics));
            calculateStats();
        });
        unit2Rd.setOnClickListener(v -> {
            if (unit2Relics > 0) --unit2Relics;
            unit2Rn.setText(String.valueOf(unit2Relics));
            calculateStats();
        });

    }


    private void setupPopUps(){
        TextView unit1Stats = findViewById(R.id.dc_unit1_stats);
        unit1Stats.setOnClickListener(v -> {
            int w =(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 325, getResources().getDisplayMetrics());
            int h = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 600, getResources().getDisplayMetrics());
            View popupView = setUnitStats(unit1);
            final PopupWindow pw = new PopupWindow(popupView, w, h, true);
            pw.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            pw.setOutsideTouchable(true);
            pw.setTouchInterceptor((v1, event) -> {
                if(event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                    pw.dismiss();
                    return true;
                }
                return false;
            });
            pw.showAsDropDown(v);
        });
        TextView unit2Stats = findViewById(R.id.dc_unit2_stats);
        unit2Stats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int w =(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 325, getResources().getDisplayMetrics());
                int h = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 600, getResources().getDisplayMetrics());
                View popupView = setUnitStats(unit2);
                final PopupWindow pw = new PopupWindow(popupView, w, h, true);
                pw.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                pw.setOutsideTouchable(true);
                pw.setTouchInterceptor((v12, event) -> {
                    if(event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                        pw.dismiss();
                        return true;
                    }
                    return false;
                });
                pw.showAsDropDown(v);
            }
        });

        TextView unit1Attack = findViewById(R.id.dc_unit1_attack_values);
        unit1Attack.setOnClickListener(v -> {
            LayoutInflater inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.damage_calculator_attack_popup,null);
            ImageView unitIcon = view.findViewById(R.id.dc_popup_unit_icon);
            TextView unitName = view.findViewById(R.id.dc_popup_unit_name);
            unitIcon.setImageResource(unit1.getNameElement().getImage());
            unitName.setText(String.format(getString(R.string.dc_popup_attack_values), unit1.getName()));
            final ExpandableListView elv = view.findViewById(R.id.element_list);
            Utils.setTypeValues(unit1.getAttackValues(), elv, c);
            int w =(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 325, getResources().getDisplayMetrics());
            int h = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 600, getResources().getDisplayMetrics());
            final PopupWindow pw = new PopupWindow(view, w, h, true);
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
            pw.showAsDropDown(v);
        });
        TextView unit2Attack = findViewById(R.id.dc_unit2_attack_values);
        unit2Attack.setOnClickListener(v -> {
            LayoutInflater inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.damage_calculator_attack_popup,null);
            ImageView unitIcon = view.findViewById(R.id.dc_popup_unit_icon);
            TextView unitName = view.findViewById(R.id.dc_popup_unit_name);
            unitIcon.setImageResource(unit2.getNameElement().getImage());
            unitName.setText(String.format(getString(R.string.dc_popup_attack_values), unit2.getName()));
            final ExpandableListView elv = view.findViewById(R.id.element_list);
            Utils.setTypeValues(unit2.getAttackValues(), elv, c);
            int w = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 325, getResources().getDisplayMetrics());
            int h = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 600, getResources().getDisplayMetrics());
            final PopupWindow pw = new PopupWindow(view, w, h, true);
            pw.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            pw.setOutsideTouchable(true);
            pw.setTouchInterceptor((v15, event) -> {
                if(event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                    pw.dismiss();
                    return true;
                }
                return false;
            });
            pw.showAsDropDown(v);
        });
        TextView unit1Armor = findViewById(R.id.dc_unit1_armor_values);
        unit1Armor.setOnClickListener(v -> {
            LayoutInflater inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.damage_calculator_attack_popup,null);
            ImageView unitIcon = view.findViewById(R.id.dc_popup_unit_icon);
            TextView unitName = view.findViewById(R.id.dc_popup_unit_name);
            unitIcon.setImageResource(unit1.getNameElement().getImage());
            unitName.setText(String.format(getString(R.string.dc_popup_armor_values), unit1.getName()));
            final ExpandableListView elv = view.findViewById(R.id.element_list);
            LinkedHashMap<String, List<TypeElement>> attack = unit1.getArmorValues();
            ArrayList<String> groupList = new ArrayList<>(attack.keySet());
            TypeAdapter attackAdapter = new TypeAdapter(c,groupList, attack);
            elv.setAdapter(attackAdapter);
            for(int i=0; i < attackAdapter.getGroupCount(); i++) elv.expandGroup(i);
            int w =(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 325, getResources().getDisplayMetrics());
            int h = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 600, getResources().getDisplayMetrics());
            final PopupWindow pw = new PopupWindow(view, w, h, true);
            pw.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            pw.setOutsideTouchable(true);
            pw.setTouchInterceptor((v16, event) -> {
                if(event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                    pw.dismiss();
                    return true;
                }
                return false;
            });
            pw.showAsDropDown(v);
        });
        TextView unit2Armor = findViewById(R.id.dc_unit2_armor_values);
        unit2Armor.setOnClickListener(v -> {
            LayoutInflater inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.damage_calculator_attack_popup,null);
            ImageView unitIcon = view.findViewById(R.id.dc_popup_unit_icon);
            TextView unitName = view.findViewById(R.id.dc_popup_unit_name);
            unitIcon.setImageResource(unit2.getNameElement().getImage());
            unitName.setText(String.format(getString(R.string.dc_popup_armor_values), unit2.getName()));
            final ExpandableListView elv = view.findViewById(R.id.element_list);
            LinkedHashMap<String, List<TypeElement>> attack = unit2.getArmorValues();
            if (attack.isEmpty()) attack.put(getString(R.string.none), new ArrayList<TypeElement>());
            ArrayList<String> groupList = new ArrayList<>(attack.keySet());
            TypeAdapter attackAdapter = new TypeAdapter(c,groupList, attack);
            elv.setAdapter(attackAdapter);
            for(int i=0; i < attackAdapter.getGroupCount(); i++) elv.expandGroup(i);
            int w =(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 325, getResources().getDisplayMetrics());
            int h = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 600, getResources().getDisplayMetrics());
            final PopupWindow pw = new PopupWindow(view, w, h, true);
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
            pw.showAsDropDown(v);
        });

        TextView unit1Calc = findViewById(R.id.dc_unit1_calculations);
        unit1Calc.setOnClickListener(v -> {
            int w =(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 325, getResources().getDisplayMetrics());
            int h = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 600, getResources().getDisplayMetrics());
            View popupView = getCalculationsPopup(calculator.getUnit1Stats());
            final PopupWindow pw = new PopupWindow(popupView, w, h, true);
            pw.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            pw.setOutsideTouchable(true);
            pw.setTouchInterceptor((v13, event) -> {
                if(event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                    pw.dismiss();
                    return true;
                }
                return false;
            });
            pw.showAsDropDown(v);
        });
        TextView unit2Calc = findViewById(R.id.dc_unit2_calculations);
        unit2Calc.setOnClickListener(v -> {
            int w =(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 325, getResources().getDisplayMetrics());
            int h = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 600, getResources().getDisplayMetrics());
            View popupView = getCalculationsPopup(calculator.getUnit2Stats());
            final PopupWindow pw = new PopupWindow(popupView, w, h, true);
            pw.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            pw.setOutsideTouchable(true);
            pw.setTouchInterceptor((v14, event) -> {
                if(event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                    pw.dismiss();
                    return true;
                }
                return false;
            });
            pw.showAsDropDown(v);
        });
    }

    private void getUnitsInfo() {
        unitList  = new ArrayList<>();
        unitNames = new ArrayList<>();
        unitRelation = new HashMap<>();

        List<EntityElement> u = Database.getList(Database.UNIT_LIST);
        for (EntityElement e : u){
            unitList.add(e.getId());
            unitNames.add(e.getName());
            unitRelation.put(e.getName(), e.getId());
        }
    }


    private void setupUnitSelectors(){
        selector = findViewById(R.id.selector);
        selector1 = findViewById(R.id.unit1_selector);
        selector2 = findViewById(R.id.unit2_selector);
        TextFilterAdapter adapter = new TextFilterAdapter(this, R.layout.autocomplete_text_layout, new ArrayList<>(unitNames));
        selector1.setThreshold(0);
        selector1.setAdapter(adapter);
        selector1.setOnItemClickListener((arg0, arg1, arg2, arg3) -> {
            InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            in.hideSoftInputFromWindow(arg1.getApplicationWindowToken(), 0);
            String unitName = (String)arg0.getItemAtPosition(arg2);
            int unitId = unitRelation.get(unitName);
            setUnit1(unitId);
            calculateStats();
            selector1.setHint(unitName);
            selector1.setText("");
        });
        selector2.setThreshold(0);
        selector2.setAdapter(adapter);
        selector2.setOnItemClickListener((arg0, arg1, arg2, arg3) -> {
            InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            in.hideSoftInputFromWindow(arg1.getApplicationWindowToken(), 0);
            String unitName = (String)arg0.getItemAtPosition(arg2);
            int unitId = unitRelation.get(unitName);
            setUnit2(unitId);
            calculateStats();
            selector2.setHint(unitName);
            selector2.setText("");
        });
    }

    private void setUnit1(int id){
        unit1 = new Unit(Database.getUnit(id));
        GifImageView img1 = findViewById(R.id.unit1_gif);
        img1.setImageResource(unit1.getNameElement().getMedia());
        GifImageView img2 = findViewById(R.id.unit2_gif);
        img2.setImageResource(unit2.getNameElement().getMedia());
        List<Integer> availableCivs = unit1.getAvailableCivIds();
        final List <String> civNames = new ArrayList<>();
        for(int i: availableCivs) civNames.add(civRelation.get(i));
        Collections.sort(civNames);
        unit1Upgrades = unit1.getUpgradesIds();
        ageID = Utils.convertAge(Utils.getMaxAge(unit1, unit2));
        if (civ1ID == -1) civ1ID = availableCivs.get(0);
        selector.setupCiv1Selector(civ1ID, civNames);
        selector.setupUpgrades1Selector(unit1.getUpgradesIds());
        selector.setOnEntity1ChangeListener(new AgeTwoCivsSelector.OnEntityChangeListener() {
            @Override
            public void onCivChanged(int civ) {
                civ1ID = civ;
                calculateStats();
            }

            @Override
            public void onUpgradesChanged(List<Integer> list) {
                unit1Upgrades = list;
                calculateStats();
            }
        });

        hill1.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked && hill2.isChecked()) hill2.setChecked(false);
            calculateStats();
        });
        setupAgeLayout();
    }

    private void setUnit2(int id){
        unit2 = new Unit(Database.getUnit(id));
        GifImageView img2 = findViewById(R.id.unit2_gif);
        img2.setImageResource(unit2.getNameElement().getMedia());
        GifImageView img1 = findViewById(R.id.unit1_gif);
        img1.setImageResource(unit1.getNameElement().getMedia());
        List <Integer> availableCivs = unit2.getAvailableCivIds();

        final List <String> civNames = new ArrayList<>();
        for(int i: availableCivs) civNames.add(civRelation.get(i));
        Collections.sort(civNames);
        unit2Upgrades = unit2.getUpgradesIds();
        ageID = Utils.convertAge(Utils.getMaxAge(unit1, unit2));
        if (civ2ID == -1) civ2ID = availableCivs.get(0);
        selector.setupCiv2Selector(civ2ID, civNames);
        selector.setupUpgrades2Selector(unit2.getUpgradesIds());
        selector.setOnEntity2ChangeListener(new AgeTwoCivsSelector.OnEntityChangeListener() {
            @Override
            public void onCivChanged(int civ) {
                civ2ID = civ;
                calculateStats();
            }

            @Override
            public void onUpgradesChanged(List<Integer> list) {
                unit2Upgrades = list;
                calculateStats();
            }
        });
        hill2.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked && hill1.isChecked()) hill1.setChecked(false);
            calculateStats();
        });
        setupAgeLayout();
    }

    private View setUnitStats(Unit unit){
        UnitComparatorActivity.UnitTextView utv = new UnitComparatorActivity.UnitTextView();
        LayoutInflater inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.damage_calculator_stats_popup,null);
        ImageView unitIcon = view.findViewById(R.id.dc_popup_unit_icon);
        TextView unitTitle = view.findViewById(R.id.dc_popup_unit_name);
        unitIcon.setImageResource(unit.getNameElement().getImage());
        unitTitle.setText(String.format(getString(R.string.dc_popup_stats), unit.getName()));

        utv.minRange = view.findViewById(R.id.unit_minimum_range);
        utv.blastRadius = view.findViewById(R.id.unit_blast_radius);
        utv.attackDelay = view.findViewById(R.id.unit_attack_delay);
        utv.projectileSpeed = view.findViewById(R.id.unit_projectile_speed);
        utv.numProjectiles = view.findViewById(R.id.unit_num_projectiles);
        utv.popTaken = view.findViewById(R.id.unit_pop_taken);
        utv.res1Type = view.findViewById(R.id.unit_res1_type);
        utv.res1Value = view.findViewById(R.id.unit_res1_value);
        utv.res2Type = view.findViewById(R.id.unit_res2_type);
        utv.res2Value = view.findViewById(R.id.unit_res2_value);
        utv.hp = view.findViewById(R.id.unit_hp);
        utv.attack = view.findViewById(R.id.unit_attack);
        utv.mArmor = view.findViewById(R.id.unit_melee_armor);
        utv.pArmor = view.findViewById(R.id.unit_pierce_armor);
        utv.range = view.findViewById(R.id.unit_range);
        utv.los = view.findViewById(R.id.unit_los);
        utv.reloadTime = view.findViewById(R.id.unit_reload_time);
        utv.speed = view.findViewById(R.id.unit_speed);
        utv.accuracy = view.findViewById(R.id.unit_accuracy);
        utv.garrisonCapacity = view.findViewById(R.id.unit_garrison_capacity);
        utv.workRate = view.findViewById(R.id.unit_work_rate);
        utv.healingRate = view.findViewById(R.id.unit_healing_rate);
        utv.trainingTime  = view.findViewById(R.id.unit_training_time);

        utv.hp.setText(unit.getStatString(Database.HP));
        utv.attack.setText(unit.getStatString(Database.ATTACK));
        utv.mArmor.setText(unit.getStatString(Database.MELEE_ARMOR));
        utv.pArmor.setText(unit.getStatString(Database.PIERCE_ARMOR));
        utv.range.setText(unit.getStatString(Database.RANGE));
        utv.minRange.setText(unit.getStatString(Database.MINIMUM_RANGE));
        utv.speed.setText(unit.getStatString(Database.SPEED));
        utv.blastRadius.setText(unit.getStatString(Database.BLAST_RADIUS));
        utv.los.setText(unit.getStatString(Database.LOS));
        utv.reloadTime.setText(unit.getStatString(Database.RELOAD_TIME));
        utv.attackDelay.setText(unit.getStatString(Database.ATTACK_DELAY));
        utv.accuracy.setText(unit.getStatString(Database.ACCURACY));
        utv.numProjectiles.setText(unit.getStatString(Database.NUMBER_PROJECTILES));
        utv.projectileSpeed.setText(unit.getStatString(Database.PROJECTILE_SPEED));
        utv.garrisonCapacity.setText(unit.getStatString(Database.GARRISON_CAPACITY));
        utv.popTaken.setText(unit.getStatString(Database.POPULATION_TAKEN));
        utv.workRate.setText(unit.getStatString(Database.POPULATION_TAKEN));
        utv.healingRate.setText(unit.getStatString(Database.HEAL_RATE));
        utv.trainingTime.setText(unit.getStatString(Database.TRAINING_TIME));

        HashMap<String, Integer> cost = unit.getCalculatedCost();
        Iterator<Map.Entry<String, Integer>> it = cost.entrySet().iterator();
        if (it.hasNext()) {
            String res = it.next().getKey();
            utv.res1Type.setImageResource(Utils.getResourceIcon(res));
            utv.res1Value.setText(unit.getCostString().get(res));
        }
        else{
            utv.res1Type.setImageDrawable(null);
            utv.res1Value.setText("");
            utv.res2Type.setImageDrawable(null);
            utv.res2Value.setText("");
        }
        if (it.hasNext()) {
            String res = it.next().getKey();
            utv.res2Type.setImageResource(Utils.getResourceIcon(res));
            utv.res2Value.setText(unit.getCostString().get(res));
        }
        else{
            utv.res2Type.setImageDrawable(null);
            utv.res2Value.setText("");
        }
        return view;

    }


    private void calculateStats() {

        calculator = new DamageCalculator(unit1, unit2);

        calculator.setUnit1Hill(hill1.isChecked());
        calculator.setUnit2Hill(hill2.isChecked());
        calculator.setUnit1Relics(unit1Relics);
        calculator.setUnit2Relics(unit2Relics);

        calculator.calculateStats(ageID, civ1ID, civ2ID, unit1Upgrades, unit2Upgrades);

        UnitStats u1 = calculator.getUnit1Stats();
        UnitStats u2 = calculator.getUnit2Stats();

        if (u1.time == u2.time) {
            unit1ResultText.setTextColor(getColor(R.color.yellow));
            unit1ResultText.setText(getString(R.string.dc_tie));
            unit2ResultText.setTextColor(getColor(R.color.yellow));
            unit2ResultText.setText(getString(R.string.dc_tie));
        }
        else if ((u1.time < u2.time && u1.totalDamage != 0 || u2.totalDamage == 0)) {
            unit1ResultText.setTextColor(getColor(R.color.green));
            unit1ResultText.setText(getString(R.string.dc_win));
            unit2ResultText.setTextColor(getColor(R.color.red));
            unit2ResultText.setText(getString(R.string.dc_lose));
        }
        else{
            unit1ResultText.setTextColor(getColor(R.color.red));
            unit1ResultText.setText(getString(R.string.dc_lose));
            unit2ResultText.setTextColor(getColor(R.color.green));
            unit2ResultText.setText(getString(R.string.dc_win));
        }
        if (u1.hasCharge){
            unit1ChargeDamageText.setText(String.format(getString(R.string.dc_damage_dealt_charge), Utils.getDecimalString(u1.totalChargeDamage, 2)));
            unit1ChargeDamageText.setVisibility(View.VISIBLE);
        }
        else unit1ChargeDamageText.setVisibility(View.GONE);
        if (u2.hasCharge){
            unit2ChargeDamageText.setText(String.format(getString(R.string.dc_damage_dealt_charge), Utils.getDecimalString(u2.totalChargeDamage, 2)));
            unit2ChargeDamageText.setVisibility(View.VISIBLE);
        }
        else unit2ChargeDamageText.setVisibility(View.GONE);
        unit1DamageText.setText(String.format(getString(R.string.dc_damage_dealt), Utils.getDecimalString(u1.totalDamage, 2)));
        unit2DamageText.setText(String.format(getString(R.string.dc_damage_dealt), Utils.getDecimalString(u2.totalDamage, 2)));
        unit1HitsText.setText(String.format(getString(R.string.dc_hits_dealt), u1.hits));
        unit2HitsText.setText(String.format(getString(R.string.dc_hits_dealt), u2.hits));
        unit1HitsPerformedText.setText(String.format(getString(R.string.dc_hits_dealt), u1.hitsPerformed));
        unit2HitsPerformedText.setText(String.format(getString(R.string.dc_hits_dealt), u2.hitsPerformed));
        unit1TimeText.setText(String.format(getString(R.string.dc_time_needed), Utils.getDecimalString(u1.time, 2)));
        unit2TimeText.setText(String.format(getString(R.string.dc_time_needed), Utils.getDecimalString(u2.time, 2)));
        unit1HPLeftText.setText(String.format(getString(R.string.dc_hp_left), Utils.getDecimalString(u1.hpLeft, 1), Utils.getDecimalString(u1.hp, 1)));
        unit2HPLeftText.setText(String.format(getString(R.string.dc_hp_left), Utils.getDecimalString(u2.hpLeft, 1), Utils.getDecimalString(u2.hp, 1)));
        unit1DPSText.setText(String.format(getString(R.string.dc_dps_dealt), Utils.getDecimalString(u1.dps, 2)));
        unit2DPSText.setText(String.format(getString(R.string.dc_dps_dealt), Utils.getDecimalString(u2.dps, 2)));
        unit1CostEfText.setText(Utils.getDecimalString(u1.costEfficiency, 2));
        unit2CostEfText.setText(Utils.getDecimalString(u2.costEfficiency, 2));
        unit1PopEfText.setText(Utils.getDecimalString(u1.popEfficiency, 2));
        unit2PopEfText.setText(Utils.getDecimalString(u2.popEfficiency, 2));
    }


    private View getCalculationsPopup(UnitStats u){
        LayoutInflater inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.damage_calculator_calculator_popup,null);
        LinearLayout unitLayout = view.findViewById(R.id.dc_main_unit_layout);
        LinearLayout projectileLayout = view.findViewById(R.id.dc_projectile_layout);
        LinearLayout chargeLayout = view.findViewById(R.id.dc_charge_layout);
        LinearLayout resultsLayout = view.findViewById(R.id.dc_results_layout);

        ImageView unit1Icon1 = view.findViewById(R.id.dc_calc_unit_icon);
        ImageView unit1Icon2 = view.findViewById(R.id.dc_calc_unit_icon2);
        TextView unit1Title = view.findViewById(R.id.dc_calc_unit_name);

        unit1Icon1.setImageResource(u.uIcon);
        unit1Icon2.setImageResource(u.rIcon);
        unit1Title.setText(String.format(getString(R.string.dc_calc_unit_name), u.uName, u.rName));

        View sectionDivider = new View(c);
        sectionDivider.setBackgroundColor(getColor(R.color.grey_divider));
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 2);
        params.setMargins(50,0,50,0);
        sectionDivider.setLayoutParams(params);

        //UNIT ATTACK
        if (u.attackValuesContent.containsKey("attack")) {
            for (AttackValues at : u.attackValuesContent.get("attack")) {
                EntityElement e = Database.getElement(Database.TYPE_LIST, at.type);
                View damageView = setDamageLayout(e.getName(), e.getImage(), at.attackValue, at.armorValue, at.multiplier, at.result, 0);
                unitLayout.addView(damageView);
            }
            unitLayout.addView(sectionDivider);
            View sumView = setOneRowLayout(getString(R.string.dc_calc_damage_sum), u.damageSum, false);
            unitLayout.addView(sumView);
            View hillView = setTwoRowsLayout(getString(R.string.dc_calc_hill_bonus), u.damageSum,
                    u.hillMultiplier, getString(R.string.dc_calc_damage_sum), getString(R.string.dc_calc_multiplier),
                    getColor(R.color.red), getColor(R.color.purple), "╳", u.hillDamage, false);
            unitLayout.addView(hillView);
            if (u.finalDamage != u.hillDamage) {
                View minView = setOneRowLayout(getString(R.string.dc_calc_min_damage), u.finalDamage, false);
                unitLayout.addView(minView);
            }
            View totalDamageView = setOneRowLayout(getString(R.string.dc_calc_unit_damage), u.finalDamage, true);
            unitLayout.addView(totalDamageView);

            //UNIT PROJECTILES
            if (u.hasExtraProjectiles) {
                for (AttackValues at : u.attackValuesContent.get("projectile")) {
                    EntityElement e = Database.getElement(Database.TYPE_LIST, at.type);
                    View damageView = setDamageLayout(e.getName(), e.getImage(), at.attackValue, at.armorValue, at.multiplier, at.result, 0);
                    projectileLayout.addView(damageView);
                }
                View sectionDivider1 = new View(c);
                sectionDivider1.setBackgroundColor(getColor(R.color.grey_divider));
                sectionDivider1.setLayoutParams(params);
                projectileLayout.addView(sectionDivider1);
                View projectileSumView = setOneRowLayout(getString(R.string.dc_calc_damage_sum), u.projectileSum, false);
                projectileLayout.addView(projectileSumView);
                if (u.projectileSum != u.projectileDamage) {
                    View minView = setOneRowLayout(getString(R.string.dc_calc_min_damage), u.projectileDamage, false);
                    projectileLayout.addView(minView);
                }
                View projectileView = setTwoRowsLayout(getString(R.string.dc_calc_projectiles_damage), u.projectileDamage,
                        u.numProjectiles, getString(R.string.dc_calc_damage_sum), getString(R.string.dc_calc_projectile),
                        getColor(R.color.red), getColor(R.color.purple), "╳", u.finalProjectileDamage, true);
                projectileLayout.addView(projectileView);
                View projectileSection = view.findViewById(R.id.dc_projectile_section);
                projectileSection.setVisibility(View.VISIBLE);
            }
        }

        //UNIT CHARGE
        if (u.hasCharge){
            for (AttackValues at : u.attackValuesContent.get("charge")){
                EntityElement e = Database.getElement(Database.TYPE_LIST, at.type);
                View damageView;
                if (at.type == 1) damageView = setDamageLayout(e.getName(), e.getImage(), at.attackValue, at.armorValue, at.multiplier, at.result, u.chargeAttack);
                else damageView = setDamageLayout(e.getName(), e.getImage(), at.attackValue, at.armorValue, at.multiplier, at.result, 0);
                chargeLayout.addView(damageView);
            }
            View sectionDivider1 = new View(c);
            sectionDivider1.setBackgroundColor(getColor(R.color.grey_divider));
            sectionDivider1.setLayoutParams(params);
            chargeLayout.addView(sectionDivider1);
            View chargeSumView = setOneRowLayout(getString(R.string.dc_calc_damage_sum), u.chargeSum, false);
            projectileLayout.addView(chargeSumView);
            View chargeHillView = setTwoRowsLayout(getString(R.string.dc_calc_hill_bonus), u.chargeSum,
                    u.hillMultiplier, getString(R.string.dc_calc_damage_sum), getString(R.string.dc_calc_multiplier),
                    getColor(R.color.red), getColor(R.color.purple),"╳", u.chargeHillDamage, false);
            chargeLayout.addView(chargeHillView);
            if (u.finalChargeDamage != u.chargeHillDamage) {
                View minView = setOneRowLayout(getString(R.string.dc_calc_min_damage), u.finalChargeDamage, false);
                chargeLayout.addView(minView);
            }
            View totalChargeDamageView = setOneRowLayout(getString(R.string.dc_calc_charge_attack), u.finalChargeDamage, true);
            chargeLayout.addView(totalChargeDamageView);
            View chargeSection = view.findViewById(R.id.dc_charge_section);
            chargeSection.setVisibility(View.VISIBLE);
        }

        //RESULTS
        View damageOneHit = setOneRowLayout(getString(R.string.dc_calc_damage_one_hit), u.totalDamage, true);
        View timeToKill = setTimeLayout(getString(R.string.dc_calc_time_kill), u.hits - 1, u.reload,
                    u.delay, u.time);
        View hpLeft = setOneRowLayout(getString(R.string.dc_calc_remaining_hp), u.hpLeft, true);
        View hitsPerformed = setOneRowLayout(getString(R.string.dc_calc_hits_performed), u.hitsPerformed, true);
        View costEf = setEfficiencyLayout(getString(R.string.dc_calc_cost_ef), u.rTime, u.rCost, u.time, u.cost, getString(R.string.dc_calc_time),
                getString(R.string.dc_calc_cost), u.costEfficiency);
        View popEf = setEfficiencyLayout(getString(R.string.dc_calc_pop_ef), u.rTime, u.rPopulation, u.time, u.population, getString(R.string.dc_calc_time),
                getString(R.string.dc_calc_pop), u.popEfficiency);
        View hitsToKill, dps;

        resultsLayout.addView(damageOneHit);
        if (u.hasCharge){
            hitsToKill = setTwoRowsLayout(getString(R.string.dc_calc_hits_kill), u.chargeHits, u.normalHits,
                    getString(R.string.dc_calc_charge_hits), getString(R.string.dc_calc_normal_hits), getColor(R.color.red), getColor(R.color.blue), "+", u.hits, true);
            dps = setOneRowLayout(getString(R.string.dc_calc_dps), u.dps, true);

            View chargeDamageOneHit = setOneRowLayout(getString(R.string.dc_calc_charge_damage_one_hit), u.totalChargeDamage, true);
            resultsLayout.addView(chargeDamageOneHit);

        }
        else {
            hitsToKill = setTwoRowsLayout(getString(R.string.dc_calc_hits_kill), u.rHp, u.totalDamage,
                    getString(R.string.dc_calc_hp), getString(R.string.dc_calc_damage), getColor(R.color.green), getColor(R.color.red), "/", u.hits, true);
            dps = setTwoRowsLayout(getString(R.string.dc_calc_dps), u.totalDamage, u.reload,
                    getString(R.string.dc_calc_damage), getString(R.string.dc_calc_rof), getColor(R.color.red), getColor(R.color.green), "/", u.dps, true);
        }
        resultsLayout.addView(hitsToKill);
        resultsLayout.addView(timeToKill);
        resultsLayout.addView(hitsPerformed);
        resultsLayout.addView(hpLeft);
        resultsLayout.addView(dps);
        resultsLayout.addView(costEf);
        resultsLayout.addView(popEf);
        return view;
    }

    private View setDamageLayout(String title, int icon, double value1, double value2, double multiplier, double result, double chargeValue){
        LayoutInflater inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.damage_calculator_damage_row,null);
        TextView titleText = view.findViewById(R.id.dc_calc_row_title);
        titleText.setText(title);
        ImageView iconImage = view.findViewById(R.id.dc_calc_row_icon);
        iconImage.setImageResource(icon);
        if(icon == R.drawable.t_white) iconImage.setBackground(null);
        TextView value1Text = view.findViewById(R.id.dc_calc_row_value1);
        value1Text.setText(Utils.getDecimalString(value1, 2));
        TextView value2Text = view.findViewById(R.id.dc_calc_row_value2);
        value2Text.setText(Utils.getDecimalString(value2, 2));
        TextView resultText = view.findViewById(R.id.dc_calc_row_result);
        resultText.setText(Utils.getDecimalString(result, 2));
        if(chargeValue != 0){
            TextView chargeSymbol = view.findViewById(R.id.dc_calc_charge_symbol);
            TextView chargeValueText = view.findViewById(R.id.dc_calc_row_charge_value);
            LinearLayout chargeLayout = view.findViewById(R.id.dc_calc_charge_row_layout);
            chargeValueText.setText(Utils.getDecimalString(chargeValue, 2));
            chargeSymbol.setVisibility(View.VISIBLE);
            chargeLayout.setVisibility(View.VISIBLE);
        }
        if (multiplier != 1.0){
            TextView multiplierSymbol = view.findViewById(R.id.dc_calc_multiplier_symbol);
            TextView multiplierValueText = view.findViewById(R.id.dc_calc_row_multiplier);
            LinearLayout multiplierLayout  = view.findViewById(R.id.dc_calc_multiplier_layout);
            TextView lp = view.findViewById(R.id.dc_calc_row_left_parenthesis);
            TextView rp = view.findViewById(R.id.dc_calc_row_right_parenthesis);
            multiplierValueText.setText(Utils.getDecimalString(multiplier, 2));
            multiplierSymbol.setVisibility(View.VISIBLE);
            multiplierLayout.setVisibility(View.VISIBLE);
            lp.setVisibility(View.VISIBLE);
            rp.setVisibility(View.VISIBLE);
        }
        return view;
    }

    private View setOneRowLayout(String title, double result, boolean boldResult){
        LayoutInflater inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.damage_calculator_one_row_layout,null);
        TextView minTitle = view.findViewById(R.id.dc_calc_row_title);
        minTitle.setText(title);
        TextView minResult = view.findViewById(R.id.dc_calc_row_result);
        if (boldResult) minResult.setTypeface(Typeface.DEFAULT_BOLD);
        minResult.setText(Utils.getDecimalString(result, 2));
        return view;
    }

    private View setTwoRowsLayout(String title, double value1, double value2, String annotation1, String annotation2, int color1, int color2, String operator, double result, boolean boldResult){
        LayoutInflater inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.damage_calculator_two_rows_layout,null);
        TextView titleText = view.findViewById(R.id.dc_calc_row_title);
        titleText.setText(title);
        TextView value1Text = view.findViewById(R.id.dc_calc_row_value1);
        value1Text.setText(Utils.getDecimalString(value1, 2));
        TextView value2Text = view.findViewById(R.id.dc_calc_row_value2);
        value2Text.setText(Utils.getDecimalString(value2, 2));
        TextView annotation1Text = view.findViewById(R.id.dc_calc_row_annotation1);
        annotation1Text.setText(annotation1);
        annotation1Text.setTextColor(color1);
        View divider1 = view.findViewById(R.id.dc_calc_row_divider1);
        divider1.setBackgroundColor(color1);
        TextView operatorText = view.findViewById(R.id.dc_calc_row_operator);
        operatorText.setText(operator);
        TextView annotation2Text = view.findViewById(R.id.dc_calc_row_annotation2);
        annotation2Text.setText(annotation2);
        annotation2Text.setTextColor(color2);
        View divider2 = view.findViewById(R.id.dc_calc_row_divider2);
        divider2.setBackgroundColor(color2);
        TextView resultText = view.findViewById(R.id.dc_calc_row_result);
        if (boldResult) resultText.setTypeface(Typeface.DEFAULT_BOLD);
        resultText.setText(Utils.getDecimalString(result, 2));
        return view;
    }

    private View setTimeLayout(String title, double value1, double value2, double multiplier, double result){
        LayoutInflater inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.damage_calculator_time_row,null);
        TextView titleText = view.findViewById(R.id.dc_calc_row_title);
        titleText.setText(title);
        TextView value1Text = view.findViewById(R.id.dc_calc_row_value1);
        value1Text.setText(Utils.getDecimalString(value1, 2));
        TextView value2Text = view.findViewById(R.id.dc_calc_row_value2);
        value2Text.setText(Utils.getDecimalString(value2, 2));
        TextView multiplierText = view.findViewById(R.id.dc_calc_row_multiplier);
        multiplierText.setText(Utils.getDecimalString(multiplier, 2));
        TextView resultText = view.findViewById(R.id.dc_calc_row_result);
        resultText.setTypeface(Typeface.DEFAULT_BOLD);
        resultText.setText(Utils.getDecimalString(result, 2));
        return view;
    }

    private View setEfficiencyLayout(String title, double value1, double value2, double value3, double value4, String annotation1, String annotation2,  double result){
        LayoutInflater inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.damage_calculator_efficiency_layout,null);
        TextView titleText = view.findViewById(R.id.dc_calc_row_title);
        titleText.setText(title);
        TextView value1Text = view.findViewById(R.id.dc_calc_row_value1);
        value1Text.setText(Utils.getDecimalString(value1, 2));
        TextView value2Text = view.findViewById(R.id.dc_calc_row_value2);
        value2Text.setText(Utils.getDecimalString(value2, 2));
        TextView value3Text = view.findViewById(R.id.dc_calc_row_value3);
        value3Text.setText(Utils.getDecimalString(value3, 2));
        TextView value4Text = view.findViewById(R.id.dc_calc_row_value4);
        TextView annotation1Text = view.findViewById(R.id.dc_calc_row_annotation1);
        annotation1Text.setText(annotation1);
        TextView annotation2Text = view.findViewById(R.id.dc_calc_row_annotation2);
        annotation2Text.setText(annotation2);
        TextView annotation3Text = view.findViewById(R.id.dc_calc_row_annotation3);
        annotation3Text.setText(annotation1);
        TextView annotation4Text = view.findViewById(R.id.dc_calc_row_annotation4);
        annotation4Text.setText(annotation2);
        value4Text.setText(Utils.getDecimalString(value4, 2));
        TextView resultText = view.findViewById(R.id.dc_calc_row_result);
        resultText.setTypeface(Typeface.DEFAULT_BOLD);
        resultText.setText(Utils.getDecimalString(result, 2));
        return view;
    }




    private void setupAgeLayout(){
        String ageString = Utils.getMaxAge(unit1, unit2);
        if (ageString.equals(getString(R.string.dark_age))) selector.showDarkAge();
        else if (ageString.equals(getString(R.string.feudal_age))) selector.showFeudalAge();
        else if (ageString.equals(getString(R.string.castle_age))) selector.showCastleAge();
        else if (ageString.equals(getString(R.string.imperial_age))) selector.showImperialAge();
        selector.selectInitialAge(Utils.convertAge(ageString));
    }
}
