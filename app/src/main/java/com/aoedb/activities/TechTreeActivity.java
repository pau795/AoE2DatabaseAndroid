package com.aoedb.activities;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatSpinner;

import com.aoedb.R;
import com.aoedb.adapters.TechTreeSpinnerAdapter;
import com.aoedb.data.Civilization;
import com.aoedb.data.TechTreeData;
import com.aoedb.database.Database;
import com.aoedb.database.Utils;
import com.aoedb.widgets.TechTreeBox;
import com.aoedb.widgets.TwoDScrollView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class TechTreeActivity extends BaseActivity {

    HashMap<String, Integer> civilizationRelationR;
    HashMap<Integer, String> civilizationRelation;
    ArrayList<String> spinnerItems;
    Civilization civ;
    int civID;
    TwoDScrollView layout;
    AppCompatSpinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TechTreeBox.techTreeData = new TechTreeData(this);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_tech_tree, null, false);
        setContentView(contentView);
        setTitle(R.string.title_activity_tech_tree);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        civID = getIntent().getIntExtra(Database.CIV,1);
        spinner=findViewById(R.id.spinner);
        civilizationRelation = Database.getCivNameMap();
        civilizationRelationR = Database.getReversedCivNameMap();
        layout = findViewById(R.id.scroll_layout);
        initializeSpinner();
    }

    public void legendIconClick(View v){
        View focusView;
        int id = v.getId();
        if (id == R.id.tt_left_shortcut) focusView = findViewById(R.id.tt_civ_name);
        else if (id == R.id.tt_archery_range_shortcut) focusView = findViewById(R.id.tt_archery_range);
        else if (id == R.id.tt_barracks_shortcut) focusView = findViewById(R.id.tt_barracks);
        else if (id == R.id.tt_stable_shortcut) focusView = findViewById(R.id.tt_stable);
        else if (id == R.id.tt_siege_workshop_shortcut) focusView = findViewById(R.id.tt_siege_workshop);
        else if (id == R.id.tt_blacksmith_shortcut) focusView = findViewById(R.id.tt_blacksmith);
        else if (id == R.id.tt_dock_shortcut) focusView = findViewById(R.id.tt_dock);
        else if (id == R.id.tt_university_shortcut) focusView = findViewById(R.id.tt_university);
        else if (id == R.id.tt_towers_shortcut) focusView = findViewById(R.id.tt_towers);
        else if (id == R.id.tt_castle_shortcut) focusView = findViewById(R.id.tt_castle);
        else if (id == R.id.tt_monastery_shortcut) focusView = findViewById(R.id.tt_monastery);
        else if (id == R.id.tt_town_center_shortcut) focusView = findViewById(R.id.tt_town_center);
        else if (id == R.id.tt_eco_shortcut) focusView = findViewById(R.id.tt_lumber_camp);
        else if (id == R.id.tt_right_shortcut) focusView = findViewById(R.id.tt_right_dark_age_shield);
        else focusView = findViewById(R.id.tt_civ_name);
        scrollToChild(focusView);
    }

    private void scrollToChild(View child){
        Rect rect = new Rect();
        child.getDrawingRect(rect);
        layout.offsetDescendantRectToMyCoords(child, rect);
        int relativeTop = rect.top;
        int relativeLeft = rect.left + child.getWidth()/2 - getResources().getDisplayMetrics().widthPixels / 2;
        layout.smoothScrollTo(relativeLeft, relativeTop);
    }


    void initializeSpinner(){
        spinnerItems= new ArrayList<>();
        for (int i:civilizationRelation.keySet()){
            spinnerItems.add(civilizationRelation.get(i));
        }
        Collections.sort(spinnerItems);
        final TechTreeSpinnerAdapter adapter = new TechTreeSpinnerAdapter(this, android.R.layout.simple_spinner_dropdown_item, spinnerItems);
        spinner.setAdapter(adapter);
        int posCiv = Utils.findSpinnerPosition(civID, civilizationRelation, spinnerItems);
        spinner.setSelection(posCiv);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String civName = spinnerItems.get(position);
                adapter.setSelected(position);
                civID = civilizationRelationR.get(civName);
                loadCiv(civID);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }


    private void loadCiv(int position) {
        civ = Database.getCivilization(position);
        String civ_info = civ.writeTechTreeInfo(this);
        TextView textName = findViewById(R.id.tt_civ_name);
        textName.setText(civ.getName());
        TextView t = findViewById(R.id.tt_civ_info);
        t.setText(Html.fromHtml(civ_info, Html.FROM_HTML_MODE_COMPACT));
        LinearLayout dl1 = findViewById(R.id.tt_donjon_layout1);
        LinearLayout dl2 = findViewById(R.id.tt_donjon_layout2);
        LinearLayout dl3 = findViewById(R.id.tt_donjon_layout3);
        LinearLayout dl4 = findViewById(R.id.tt_donjon_layout4);
        LinearLayout kl1 = findViewById(R.id.tt_krepost_layout1);
        LinearLayout kl2 = findViewById(R.id.tt_krepost_layout2);
        LinearLayout kl3 = findViewById(R.id.tt_krepost_layout3);
        LinearLayout kl4 = findViewById(R.id.tt_krepost_layout4);

        if (civID == 32){
            kl1.setVisibility(View.VISIBLE);
            kl2.setVisibility(View.VISIBLE);
            kl3.setVisibility(View.VISIBLE);
            kl4.setVisibility(View.VISIBLE);
        }
        else{
            kl1.setVisibility(View.GONE);
            kl2.setVisibility(View.GONE);
            kl3.setVisibility(View.GONE);
            kl4.setVisibility(View.GONE);
        }

        if (civID == 37){
            dl1.setVisibility(View.VISIBLE);
            dl2.setVisibility(View.VISIBLE);
            dl3.setVisibility(View.VISIBLE);
            dl4.setVisibility(View.VISIBLE);
        }
        else {
            dl1.setVisibility(View.GONE);
            dl2.setVisibility(View.GONE);
            dl3.setVisibility(View.GONE);
            dl4.setVisibility(View.GONE);
        }

        TechTreeBox.civID = civID;
        for (int i = 0; i < TechTreeBox.techTreeData.getUniqueBoxes().size(); ++i){
            TechTreeBox.techTreeData.getUniqueBoxes().get(i).displayBox();
        }
        for (int i = 0; i < TechTreeBox.techTreeData.getInstanceList().size(); ++i){
            TechTreeBox.techTreeData.getInstanceList().get(i).setAvailability();
        }

    }

}
