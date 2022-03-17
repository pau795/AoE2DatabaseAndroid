package com.aoedb.activities;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;

import com.aoedb.R;
import com.aoedb.adapters.EcoListAdapter;
import com.aoedb.data.Civilization;
import com.aoedb.data.EcoElement;
import com.aoedb.database.Database;
import com.aoedb.widgets.AgeCivSelector;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class    GatheringRatesActivity extends DrawerActivity {

    Context ctx;

    int ageID;
    int civID;
    Civilization c;
    ListView ecoListView;
    EcoListAdapter adapter;
    List<EcoElement> ecoList;
    HashMap<Integer, String> civRelation;
    HashMap<String, Integer> civRelationR;
    HashMap<Integer, String> statNames;

    List<Integer> upgradeList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.title_gathering_rates);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ctx = this;
        View contentView = inflater.inflate(R.layout.activity_gathering_rates, null, false);
        setActivityView(contentView);
        setAppBarColor(getColor(R.color.blue_background));
        ecoListView = findViewById(R.id.eco_listview);
        civRelation = Database.getCivNameMap();
        civRelationR = Database.getReversedCivNameMap();
        ctx = this;
        ageID = 0;
        civID = 1;
        c = Database.getCivilization(civID);
        upgradeList = c.getUpgradesIds();
        statNames = Database.getEcoList();
        ecoList = Database.getGatheringRates();
        setupAgeCivLayout();

    }

    private void setupAgeCivLayout(){
        final List <String> civNames = new ArrayList<>();
        for(int i: civRelation.keySet()) civNames.add(civRelation.get(i));
        Collections.sort(civNames);
        AgeCivSelector selector = findViewById(R.id.selector);
        selector.setupCivSelector(civID, civNames);
        selector.setupUpgradeSelector(c.getUpgradesIds());
        selector.setOnChangeListener(new AgeCivSelector.OnChangeListener() {
            @Override
            public void onAgeChanged(int age) {
                ageID = age;
                calculateEco();
            }

            @Override
            public void onCivChanged(int civ) {
                civID = civ;
                c = Database.getCivilization(civ);
                calculateEco();
            }

            @Override
            public void onUpgradesChanged(List<Integer> list) {
                upgradeList =  list;
                calculateEco();
            }
        });
        selector.selectInitialAge(0);

    }


    private void calculateEco(){
        c.calculateStats(ageID, civID, upgradeList);
        List<EcoElement> finalEcoList = new ArrayList<>();
        for (int i = 0; i < ecoList.size(); ++i){
            EcoElement element = ecoList.get(i);
            double stat = c.getCalculatedStat(statNames.get(element.getStat()));
            if (i == 13) stat *= 1.75; //deep fish
            else if ( i == 14) stat *= 1.25; // fish trap
            element.setGatheringRate(stat);
            if (!Double.isNaN(element.getGatheringRate())) finalEcoList.add(element);
        }

        adapter = new EcoListAdapter(this, finalEcoList);
        ecoListView.setAdapter(adapter);
    }
}