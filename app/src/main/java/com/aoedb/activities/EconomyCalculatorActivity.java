package com.aoedb.activities;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aoedb.R;
import com.aoedb.adapters.TextFilterAdapter;
import com.aoedb.data.EntityElement;
import com.aoedb.data.Civilization;
import com.aoedb.data.Unit;
import com.aoedb.database.Database;
import com.aoedb.database.Utils;
import com.aoedb.widgets.AgeCivSelector;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class EconomyCalculatorActivity extends DrawerActivity {

    List<String> unitNames;
    List<Integer> unitList;
    HashMap<String, Integer> unitRelation;
    HashMap<Integer,String> unitRelationR;

    HashMap<Integer, String> civRelation;
    HashMap<String, Integer> civRelationR;
    LinearLayout selectorLayout, ecoLayout;
    Context ctx;
    View contentView;
    EconomyCalculator eco;

    Civilization civ;

    List<Integer> upgradeList;

    int civID;
    int ageID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.title_economy_calculator);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        contentView = inflater.inflate(R.layout.activity_economy_calculator, null, false);
        setActivityView(contentView);
        setAppBarColor(getColor(R.color.blue_background));
        civRelation = Database.getCivNameMap();
        civRelationR = Database.getReversedCivNameMap();
        ctx = this;
        civID = 1;
        civ = Database.getCivilization(civID);
        upgradeList = civ.getUpgradesIds();
        ageID = 0;
        getUnitsInfo();
        initLayouts();
        initEco();
        setupAgeCivSelector();
    }

    void initEco(){
        eco = new EconomyCalculator();
        eco.addItem();
    }

    private void setupAgeCivSelector(){
        final List <String> civNames = new ArrayList<>();
        for(int i: civRelation.keySet()) civNames.add(civRelation.get(i));
        Collections.sort(civNames);
        AgeCivSelector selector = findViewById(R.id.selector);
        selector.setupCivSelector(civID, civNames);
        selector.setupUpgradeSelector(upgradeList);
        selector.setOnChangeListener(new AgeCivSelector.OnChangeListener() {
            @Override
            public void onAgeChanged(int age) {
                ageID = age;
                eco.calculateTotalEco();
            }

            @Override
            public void onCivChanged(int civId) {
                civID = civId;
                civ = Database.getCivilization(civID);
                eco.calculateTotalEco();
            }

            @Override
            public void onUpgradesChanged(List<Integer> list) {
                upgradeList = list;
                eco.calculateTotalEco();
            }
        });
        selector.selectInitialAge(0);
    }

    private void initLayouts(){
        selectorLayout = findViewById(R.id.unit_adder_layout);
        ecoLayout = findViewById(R.id.unit_display_layout);
        LinearLayout addButton = findViewById(R.id.add_unit_button);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eco.addItem();
            }
        });
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

    private class EconomyCalculator {

        private class UnitItem{
            Unit u;
            Integer numBuildings;
            View unitSelectionLayout, unitEcoLayout;
            double unitWoodRate, unitFoodRate, unitGoldRate;
            int woodVillagers, foodVillagers, goldVillagers;
            AutoCompleteTextView selector;

            public UnitItem(){
                LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                unitSelectionLayout = inflater.inflate(R.layout.eco_calculator_unit_add,null);
                unitEcoLayout = inflater.inflate(R.layout.eco_calculator_unit_res,null);
                selector = unitSelectionLayout.findViewById(R.id.unit_selector);
                TextFilterAdapter adapter = new TextFilterAdapter(ctx, R.layout.autocomplete_text_layout, new ArrayList<>(unitNames));
                selector.setThreshold(0);
                selector.setAdapter(adapter);
                selector.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                        InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        in.hideSoftInputFromWindow(arg1.getApplicationWindowToken(), 0);
                        String unitName = (String)arg0.getItemAtPosition(arg2);
                        int unitId = unitRelation.get(unitName);
                        loadUnit(unitId);
                        calculateTotalEco();
                    }
                });
                ImageView up = unitSelectionLayout.findViewById(R.id.num_buildings_up);
                ImageView down = unitSelectionLayout.findViewById(R.id.num_buildings_down);
                up.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (numBuildings < 10){
                            ++numBuildings;
                            setNumBuildings();
                            calculateTotalEco();
                        }
                    }
                });
                down.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (numBuildings > 1){
                            --numBuildings;
                            setNumBuildings();
                            calculateTotalEco();
                        }
                    }
                });
                final LinearLayout deleteItem = unitSelectionLayout.findViewById(R.id.delete_button);
                deleteItem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int position = ((ViewGroup) unitSelectionLayout.getParent()).indexOfChild(unitSelectionLayout);
                        removeItem(position);
                    }
                });
                loadUnit(1);
            }

            public void loadUnit(int i){
                u = Database.getUnit(i);
                selector.setHint(u.getName());
                selector.setText("");
                numBuildings = 1;
                ImageView buildingIcon = unitSelectionLayout.findViewById(R.id.training_building_icon);
                TextView unitIcon = unitEcoLayout.findViewById(R.id.unit_icon);
                buildingIcon.setImageResource(u.getCreatorElement().getImage());
                unitIcon.setBackgroundResource(u.getNameElement().getImage());
                setNumBuildings();
            }

            private void setNumBuildings(){
                TextView numBuildingsText1 = unitSelectionLayout.findViewById(R.id.num_buildings1);
                numBuildingsText1.setText(numBuildings.toString());
                TextView unitIcon = unitEcoLayout.findViewById(R.id.unit_icon);
                unitIcon.setText("╳ " + numBuildings);
            }

            private void setUnitCost(){
                HashMap<String, Integer> cost = u.getCalculatedCost();
                TextView res1 = unitEcoLayout.findViewById(R.id.res1_value);
                ImageView res1icon = unitEcoLayout.findViewById(R.id.res1_icon);
                TextView res2 = unitEcoLayout.findViewById(R.id.res2_value);
                ImageView res2icon = unitEcoLayout.findViewById(R.id.res2_icon);
                LinearLayout l = unitEcoLayout.findViewById(R.id.res2_layout);

                int i = 1;
                for(String s: cost.keySet()){
                    switch (i){
                        case 1:
                            res1.setText(Utils.getDecimalString(cost.get(s), 0));
                            res1icon.setImageResource(Utils.getResourceIcon(s));
                            break;
                        case 2:
                            res2.setText(Utils.getDecimalString(cost.get(s), 0));
                            res2icon.setImageResource(Utils.getResourceIcon(s));
                            break;
                    }
                    ++i;
                }
                if (cost.size() == 1) l.setVisibility(View.GONE);
                else l.setVisibility(View.VISIBLE);

                TextView unitTrainingTime = unitEcoLayout.findViewById(R.id.unit_training_time);
                unitTrainingTime.setText(Utils.getDecimalString(u.getCalculatedStat(Database.TRAINING_TIME), 0));
            }

            public void calculateUnitEco(){
                woodVillagers = foodVillagers = goldVillagers = 0;
                unitWoodRate = unitFoodRate = unitGoldRate = 0;
                u.calculateStats(ageID, civID, u.getUpgradesIds());
                setUnitCost();
                double trainingRate = 60.0 / u.getCalculatedStat(Database.TRAINING_TIME) * numBuildings;
                HashMap<String, Integer> cost = u.getCalculatedCost();
                for(String s: cost.keySet()){
                    switch (s){
                        case Database.WOOD:
                            unitWoodRate = trainingRate * cost.get(s);
                            woodVillagers = (int) Math.ceil(unitWoodRate / ecoWoodRate);
                            break;
                        case Database.FOOD:
                            unitFoodRate = trainingRate * cost.get(s);
                            foodVillagers = (int) Math.ceil(unitFoodRate / ecoFoodRate);
                            break;
                        case Database.GOLD:
                            unitGoldRate = trainingRate * cost.get(s);
                            goldVillagers = (int) Math.ceil(unitGoldRate / ecoGoldRate);
                            break;
                        default:
                            break;

                    }
                }
                TextView woodVillagersText = unitEcoLayout.findViewById(R.id.required_lumberjacks);
                TextView foodVillagersText = unitEcoLayout.findViewById(R.id.required_farmers);
                TextView goldVillagersText = unitEcoLayout.findViewById(R.id.required_miners);
                TextView woodRateText = unitEcoLayout.findViewById(R.id.required_wood);
                TextView foodRateText = unitEcoLayout.findViewById(R.id.required_food);
                TextView goldRateText = unitEcoLayout.findViewById(R.id.required_gold);
                woodRateText.setText(Utils.getDecimalString(unitWoodRate, 1));
                foodRateText.setText(Utils.getDecimalString(unitFoodRate, 1));
                goldRateText.setText(Utils.getDecimalString(unitGoldRate, 1));
                woodVillagersText.setText(Utils.getDecimalString(woodVillagers, 1));
                foodVillagersText.setText(Utils.getDecimalString(foodVillagers, 1));
                goldVillagersText.setText(Utils.getDecimalString(goldVillagers, 1));

            }
        }

        List<UnitItem> items;
        double ecoWoodRate = 1 , ecoFoodRate = 1, ecoGoldRate = 1;
        double totalWoodRate, totalFoodRate, totalGoldRate;
        int lumberjacks, farmers, miners;
        int numItems = 0;

        public EconomyCalculator(){
            items = new ArrayList<>();
        }

        public void addItem(){
            if(numItems < 5) {
                UnitItem i = new UnitItem();
                selectorLayout.addView(i.unitSelectionLayout);
                ecoLayout.addView(i.unitEcoLayout);
                items.add(i);
                ++numItems;
                calculateTotalEco();

            }
        }

        public void removeItem(int i){
            if (numItems > 1) {
                items.remove(i);
                selectorLayout.removeViewAt(i);
                ecoLayout.removeViewAt(i);
                --numItems;
                calculateTotalEco();
            }
        }

        private void calculateGatherRates(){
            civ.calculateStats(ageID, civID, upgradeList);
            ecoWoodRate = civ.getCalculatedStat(Database.LUMBERJACK);
            ecoFoodRate = civ.getCalculatedStat(Database.FARMER);
            ecoGoldRate = civ.getCalculatedStat(Database.GOLD_MINER);
        }

        public void calculateTotalEco(){
            totalWoodRate = totalFoodRate = totalGoldRate = 0;
            calculateGatherRates();
            for(UnitItem u: items){
                u.calculateUnitEco();
                totalWoodRate += u.unitWoodRate;
                totalFoodRate += u.unitFoodRate;
                totalGoldRate += u.unitGoldRate;
            }
            lumberjacks = (int) Math.ceil(totalWoodRate / ecoWoodRate);
            farmers = (int) Math.ceil(totalFoodRate / ecoFoodRate);
            miners = (int) Math.ceil(totalGoldRate / ecoGoldRate);
            TextView woodVillagersText = contentView.findViewById(R.id.total_lumberjacks);
            TextView foodVillagersText = contentView.findViewById(R.id.total_farmers);
            TextView goldVillagersText = contentView.findViewById(R.id.total_miners);
            TextView woodRateText = contentView.findViewById(R.id.total_wood_rate);
            TextView foodRateText = contentView.findViewById(R.id.total_food_rate);
            TextView goldRateText = contentView.findViewById(R.id.total_gold_rate);
            TextView ecoWoodRateText = contentView.findViewById(R.id.wood_gather_rate);
            TextView ecoFoodRateText = contentView.findViewById(R.id.food_gather_rate);
            TextView ecoGoldRateText = contentView.findViewById(R.id.gold_gather_rate);
            woodRateText.setText(Utils.getDecimalString(totalWoodRate, 1));
            foodRateText.setText(Utils.getDecimalString(totalFoodRate, 1));
            goldRateText.setText(Utils.getDecimalString(totalGoldRate, 1));
            woodVillagersText.setText(Utils.getDecimalString(lumberjacks, 1));
            foodVillagersText.setText(Utils.getDecimalString(farmers, 1));
            goldVillagersText.setText(Utils.getDecimalString(miners, 1));
            ecoWoodRateText.setText(Utils.getDecimalString(ecoWoodRate, 1));
            ecoFoodRateText.setText(Utils.getDecimalString(ecoFoodRate, 1));
            ecoGoldRateText.setText(Utils.getDecimalString(ecoGoldRate, 1));

        }
    }

}
