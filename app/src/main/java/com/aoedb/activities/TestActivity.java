package com.aoedb.activities;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.aoedb.R;
import com.aoedb.data.Civilization;
import com.aoedb.database.Database;
import com.aoedb.widgets.AgeCivSelector;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class TestActivity extends DrawerActivity {

    Context c;
    int ageID, civID;
    HashMap<Integer, String> civRelation;
    Civilization civ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.title_test_activity);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_test, null, false);
        setActivityView(contentView);
        c = this;
        civRelation = Database.getCivNameMap();
        ageID = 0;
        civID = 1;
        civ = Database.getCivilization(civID);
        setupAgeCivSelector();


    }

    private void setupAgeCivSelector(){
        final List <String> civNames = new ArrayList<>();
        for(int i: civRelation.keySet()) civNames.add(civRelation.get(i));
        Collections.sort(civNames);
        AgeCivSelector selector = findViewById(R.id.selector);
        selector.setupCivSelector(civID, civNames);
        selector.setupUpgradeSelector(civ.getUpgradesIds());
        selector.setOnChangeListener(new AgeCivSelector.OnChangeListener() {
            @Override
            public void onAgeChanged(int age) {
                ageID = age;
            }

            @Override
            public void onCivChanged(int civId) {
                civID = civId;
                civ = Database.getCivilization(civID);
            }

            @Override
            public void onUpgradesChanged(List<Integer> list) {
                civ.calculateStats(ageID, civID, list);
            }
        });
        selector.selectInitialAge(0);
    }


}
