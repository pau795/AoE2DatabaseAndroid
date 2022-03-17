package com.aoedb.activities;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatAutoCompleteTextView;

import com.aoedb.R;
import com.aoedb.adapters.TextFilterAdapter;
import com.aoedb.data.EntityElement;
import com.aoedb.data.Unit;
import com.aoedb.database.Database;
import com.aoedb.database.Utils;
import com.aoedb.widgets.AgeTwoCivsSelector;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import pl.droidsonroids.gif.GifImageView;

public class UnitComparatorActivity extends DrawerActivity {

    List<String> unitNames;
    List<Integer> unitList;
    HashMap<String, Integer> unitRelation;
    HashMap<Integer,String> unitRelationR;

    HashMap<Integer, String> civRelation;
    HashMap<String, Integer> civRelationR;

    AgeTwoCivsSelector selector;

    Unit unit1, unit2;
    int civ1ID, civ2ID;
    List<Integer> unit1Upgrades, unit2Upgrades;
    int ageID;
    UnitTextView utv1, utv2;

    AppCompatAutoCompleteTextView selector1, selector2;

    static class UnitTextView{
        TextView hp;
        TextView attack;
        TextView mArmor;
        TextView pArmor;
        TextView range;
        TextView minRange;
        TextView los;
        TextView reloadTime;
        TextView speed;
        TextView blastRadius;
        TextView attackDelay;
        TextView accuracy;
        TextView numProjectiles;
        TextView projectileSpeed;
        ImageView res1Type;
        TextView res1Value;
        ImageView res2Type;
        TextView res2Value;
        TextView popTaken;
        TextView garrisonCapacity;
        TextView workRate;
        TextView healingRate;
        TextView trainingTime;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.title_unit_comparator);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_unit_comparator, null, false);
        setActivityView(contentView);
        setAppBarColor(getColor(R.color.blue_background));
        civRelation = Database.getCivNameMap();
        civRelationR = Database.getReversedCivNameMap();
        getUnitsInfo();
        setupSelector();
        int unitID = getIntent().getIntExtra(Database.UNIT, 1);
        civ1ID = getIntent().getIntExtra(Database.CIV, -1);
        civ2ID = civ1ID;
        unit1 = new Unit(Database.getUnit(unitID));
        unit2 = new Unit(Database.getUnit(unitID));
        unit1Upgrades = unit1.getUpgradesIds();
        unit2Upgrades = unit2.getUpgradesIds();
        setupTextViews();
        selector.setOnAgeChangeListener(new AgeTwoCivsSelector.OnAgeChangeListener() {
            @Override
            public void onAgeChanged(int age) {
                ageID = age;
                setStats(unit1, utv1, ageID, civ1ID, unit1Upgrades);
                setStats(unit2, utv2, ageID, civ2ID, unit2Upgrades);
            }
        });
        setUnit1(unitID);
        setUnit2(unitID);
        selector1.setHint(unitRelationR.get(unitID));
        selector2.setHint(unitRelationR.get(unitID));


    }

    private void setupTextViews(){
        utv1 = new UnitTextView();
        utv1.minRange = findViewById(R.id.unit1_minimum_range);
        utv1.blastRadius = findViewById(R.id.unit1_blast_radius);
        utv1.attackDelay = findViewById(R.id.unit1_attack_delay);
        utv1.projectileSpeed = findViewById(R.id.unit1_projectile_speed);
        utv1.numProjectiles = findViewById(R.id.unit1_num_projectiles);
        utv1.popTaken = findViewById(R.id.unit1_pop_taken);
        utv1.res1Type = findViewById(R.id.unit1_res1_type);
        utv1.res1Value = findViewById(R.id.unit1_res1_value);
        utv1.res2Type = findViewById(R.id.unit1_res2_type);
        utv1.res2Value = findViewById(R.id.unit1_res2_value);
        utv1.hp = findViewById(R.id.unit1_hp);
        utv1.attack = findViewById(R.id.unit1_attack);
        utv1.mArmor = findViewById(R.id.unit1_melee_armor);
        utv1.pArmor = findViewById(R.id.unit1_pierce_armor);
        utv1.range = findViewById(R.id.unit1_range);
        utv1.los = findViewById(R.id.unit1_los);
        utv1.reloadTime = findViewById(R.id.unit1_reload_time);
        utv1.speed = findViewById(R.id.unit1_speed);
        utv1.accuracy = findViewById(R.id.unit1_accuracy);
        utv1.garrisonCapacity = findViewById(R.id.unit1_garrison_capacity);
        utv1.workRate = findViewById(R.id.unit1_work_rate);
        utv1.healingRate = findViewById(R.id.unit1_healing_rate);
        utv1.trainingTime  = findViewById(R.id.unit1_training_time);

        utv2 = new UnitTextView();
        utv2.minRange = findViewById(R.id.unit2_minimum_range);
        utv2.blastRadius = findViewById(R.id.unit2_blast_radius);
        utv2.attackDelay = findViewById(R.id.unit2_attack_delay);
        utv2.projectileSpeed = findViewById(R.id.unit2_projectile_speed);
        utv2.numProjectiles = findViewById(R.id.unit2_num_projectiles);
        utv2.popTaken = findViewById(R.id.unit2_pop_taken);
        utv2.res1Type = findViewById(R.id.unit2_res1_type);
        utv2.res1Value = findViewById(R.id.unit2_res1_value);
        utv2.res2Type = findViewById(R.id.unit2_res2_type);
        utv2.res2Value = findViewById(R.id.unit2_res2_value);
        utv2.hp = findViewById(R.id.unit2_hp);
        utv2.attack = findViewById(R.id.unit2_attack);
        utv2.mArmor = findViewById(R.id.unit2_melee_armor);
        utv2.pArmor = findViewById(R.id.unit2_pierce_armor);
        utv2.range = findViewById(R.id.unit2_range);
        utv2.los = findViewById(R.id.unit2_los);
        utv2.reloadTime = findViewById(R.id.unit2_reload_time);
        utv2.speed = findViewById(R.id.unit2_speed);
        utv2.accuracy = findViewById(R.id.unit2_accuracy);
        utv2.garrisonCapacity = findViewById(R.id.unit2_garrison_capacity);
        utv2.workRate = findViewById(R.id.unit2_work_rate);
        utv2.healingRate = findViewById(R.id.unit2_healing_rate);
        utv2.trainingTime  = findViewById(R.id.unit2_training_time);

    }

    private void setUnit1(int id){
        unit1 = new Unit(Database.getUnit(id));
        GifImageView img1 = findViewById(R.id.unit1_gif);
        img1.setImageResource(unit1.getNameElement().getMedia());
        GifImageView img2 = findViewById(R.id.unit2_gif);
        img2.setImageResource(unit2.getNameElement().getMedia());
        List <Integer> availableCivs = unit1.getAvailableCivIds();
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
                setStats(unit1, utv1, ageID, civ1ID, unit1Upgrades);
            }

            @Override
            public void onUpgradesChanged(List<Integer> list) {
                unit1Upgrades = list;
                setStats(unit1, utv1, ageID, civ1ID, unit1Upgrades);
            }
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
        unit2Upgrades = unit2.getUpgradesIds();
        final List <String> civNames = new ArrayList<>();
        for(int i: availableCivs) civNames.add(civRelation.get(i));
        Collections.sort(civNames);
        ageID = Utils.convertAge(Utils.getMaxAge(unit1, unit2));
        if (civ2ID == -1) civ2ID = availableCivs.get(0);
        selector.setupCiv2Selector(civ2ID, civNames);
        selector.setupUpgrades2Selector(unit2.getUpgradesIds());
        selector.setOnEntity2ChangeListener(new AgeTwoCivsSelector.OnEntityChangeListener() {
            @Override
            public void onCivChanged(int civ) {
                civ2ID = civ;
                setStats(unit2, utv2, ageID, civ2ID, unit2Upgrades);
            }

            @Override
            public void onUpgradesChanged(List<Integer> list) {
                unit2Upgrades = list;
                setStats(unit2, utv2, ageID, civ2ID, unit2Upgrades);
            }
        });
        setupAgeLayout();
    }

    private void setupAgeLayout(){
        String ageString = Utils.getMaxAge(unit1, unit2);
        if (ageString.equals(getString(R.string.dark_age))) selector.showDarkAge();
        else if (ageString.equals(getString(R.string.feudal_age))) selector.showFeudalAge();
        else if (ageString.equals(getString(R.string.castle_age))) selector.showCastleAge();
        else if (ageString.equals(getString(R.string.imperial_age))) selector.showImperialAge();
        selector.selectInitialAge(Utils.convertAge(ageString));
    }



    private void setupSelector(){
        selector = findViewById(R.id.selector);
        selector1 = findViewById(R.id.unit1_selector);
        selector2 = findViewById(R.id.unit2_selector);
        TextFilterAdapter adapter = new TextFilterAdapter(this, R.layout.autocomplete_text_layout, new ArrayList<>(unitNames));
        selector1.setThreshold(0);
        selector1.setAdapter(adapter);
        selector1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                in.hideSoftInputFromWindow(arg1.getApplicationWindowToken(), 0);
                String unitName = (String)arg0.getItemAtPosition(arg2);
                int unitId = unitRelation.get(unitName);
                setUnit1(unitId);
                selector1.setHint(unitName);
                selector1.setText("");
            }

        });
        selector2.setThreshold(0);
        selector2.setAdapter(adapter);
        selector2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                in.hideSoftInputFromWindow(arg1.getApplicationWindowToken(), 0);
                String unitName = (String)arg0.getItemAtPosition(arg2);
                int unitId = unitRelation.get(unitName);
                setUnit2(unitId);
                selector2.setHint(unitName);
                selector2.setText("");
            }

        });

    }

    private void setStats(Unit u, UnitTextView utv, int ageID, int civID, List<Integer> upgradeList){
        u.calculateStats(ageID, civID, upgradeList);

        utv.hp.setText(u.getStatString(Database.HP));
        utv.attack.setText(u.getStatString(Database.ATTACK));
        utv.mArmor.setText(u.getStatString(Database.MELEE_ARMOR));
        utv.pArmor.setText(u.getStatString(Database.PIERCE_ARMOR));
        utv.range.setText(u.getStatString(Database.RANGE));
        utv.minRange.setText(u.getStatString(Database.MINIMUM_RANGE));
        utv.speed.setText(u.getStatString(Database.SPEED));
        utv.blastRadius.setText(u.getStatString(Database.BLAST_RADIUS));
        utv.los.setText(u.getStatString(Database.LOS));
        utv.reloadTime.setText(u.getStatString(Database.RELOAD_TIME));
        utv.attackDelay.setText(u.getStatString(Database.ATTACK_DELAY));
        utv.accuracy.setText(u.getStatString(Database.ACCURACY));
        utv.numProjectiles.setText(u.getStatString(Database.NUMBER_PROJECTILES));
        utv.projectileSpeed.setText(u.getStatString(Database.PROJECTILE_SPEED));
        utv.garrisonCapacity.setText(u.getStatString(Database.GARRISON_CAPACITY));
        utv.popTaken.setText(u.getStatString(Database.POPULATION_TAKEN));
        utv.workRate.setText(u.getStatString(Database.POPULATION_TAKEN));
        utv.healingRate.setText(u.getStatString(Database.HEAL_RATE));
        utv.trainingTime.setText(u.getStatString(Database.TRAINING_TIME));

        HashMap<String, Integer> cost = u.getCalculatedCost();
        Iterator<Map.Entry<String, Integer>> it = cost.entrySet().iterator();
        if (it.hasNext()) {
            String res = it.next().getKey();
            utv.res1Type.setImageResource(Utils.getResourceIcon(res));
            utv.res1Value.setText(u.getCostString().get(res));
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
            utv.res2Value.setText(u.getCostString().get(res));
        }
        else{
            utv.res2Type.setImageDrawable(null);
            utv.res2Value.setText("");
        }

    }

    private void getUnitsInfo() {
        unitList  = new ArrayList<>();
        unitNames = new ArrayList<>();
        unitRelation = new HashMap<>();
        unitRelationR = new HashMap<>();

        List<EntityElement> u = Database.getList(Database.UNIT_LIST);
        for (EntityElement e : u){
                unitList.add(e.getId());
                unitNames.add(e.getName());
                unitRelation.put(e.getName(), e.getId());
                unitRelationR.put(e.getId(), e.getName());
        }
    }


}
