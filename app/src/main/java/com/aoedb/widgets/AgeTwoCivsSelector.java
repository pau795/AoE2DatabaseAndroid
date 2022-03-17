package com.aoedb.widgets;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatSpinner;

import com.aoedb.R;
import com.aoedb.adapters.UpgradeSelectorAdapter;
import com.aoedb.database.Database;
import com.aoedb.database.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AgeTwoCivsSelector extends LinearLayout {

    int ageID, civ1ID, civ2ID;
    AppCompatSpinner spinner1, spinner2;
    HashMap<Integer, String> civRelation;
    HashMap<String, Integer> civRelationR;
    LinearLayout ageButton1;
    LinearLayout ageButton2;
    LinearLayout ageButton3;
    LinearLayout ageButton4;
    LinearLayout ageDivider1;
    LinearLayout ageDivider2;
    LinearLayout ageDivider3;

    List<String> civ1Names, civ2Names;
    UpgradeSelectorAdapter upgrades1Adapter, upgrades2Adapter;

    Context ctx;

    OnAgeChangeListener ageListener;
    OnEntityChangeListener entity1Listener, entity2Listener;




    public AgeTwoCivsSelector(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
        civRelation = Database.getCivNameMap();
        civRelationR = Database.getReversedCivNameMap();
        spinner1 = findViewById(R.id.spinner1);
        spinner2 = findViewById(R.id.spinner2);
        ageButton1 = findViewById(R.id.button_age1);
        ageButton2 = findViewById(R.id.button_age2);
        ageButton3 = findViewById(R.id.button_age3);
        ageButton4 = findViewById(R.id.button_age4);
        ageDivider1 = findViewById(R.id.button_divider1);
        ageDivider2 = findViewById(R.id.button_divider2);
        ageDivider3 = findViewById(R.id.button_divider3);
        civ1Names = new ArrayList<>();
        civ2Names = new ArrayList<>();
        ctx = context;
        setupAgeButtons();

    }

    private void setupAgeButtons() {
        ageButton1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ageButton1.setBackgroundColor(ctx.getColor(R.color.grey_background));
                ageButton2.setBackgroundColor(ctx.getColor(R.color.white));
                ageButton3.setBackgroundColor(ctx.getColor(R.color.white));
                ageButton4.setBackgroundColor(ctx.getColor(R.color.white));
                ageID = 0;
                if (ageListener != null) ageListener.onAgeChanged(ageID);
                if (upgrades1Adapter != null && upgrades1Adapter.isEnabled()) upgrades1Adapter.filterList(ageID, civ1ID);
                if (upgrades2Adapter != null && upgrades2Adapter.isEnabled()) upgrades2Adapter.filterList(ageID, civ2ID);
            }
        });
        ageButton2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ageButton1.setBackgroundColor(ctx.getColor(R.color.white));
                ageButton2.setBackgroundColor(ctx.getColor(R.color.grey_background));
                ageButton3.setBackgroundColor(ctx.getColor(R.color.white));
                ageButton4.setBackgroundColor(ctx.getColor(R.color.white));
                ageID = 1;
                if (ageListener != null) ageListener.onAgeChanged(ageID);
                if (upgrades1Adapter != null && upgrades1Adapter.isEnabled()) upgrades1Adapter.filterList(ageID, civ1ID);
                if (upgrades2Adapter != null && upgrades2Adapter.isEnabled()) upgrades2Adapter.filterList(ageID, civ2ID);
            }
        });
        ageButton3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ageButton1.setBackgroundColor(ctx.getColor(R.color.white));
                ageButton2.setBackgroundColor(ctx.getColor(R.color.white));
                ageButton3.setBackgroundColor(ctx.getColor(R.color.grey_background));
                ageButton4.setBackgroundColor(ctx.getColor(R.color.white));
                ageID = 2;
                if (ageListener != null) ageListener.onAgeChanged(ageID);
                if (upgrades1Adapter != null && upgrades1Adapter.isEnabled()) upgrades1Adapter.filterList(ageID, civ1ID);
                if (upgrades2Adapter != null && upgrades2Adapter.isEnabled()) upgrades2Adapter.filterList(ageID, civ2ID);
            }
        });
        ageButton4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ageButton1.setBackgroundColor(ctx.getColor(R.color.white));
                ageButton2.setBackgroundColor(ctx.getColor(R.color.white));
                ageButton3.setBackgroundColor(ctx.getColor(R.color.white));
                ageButton4.setBackgroundColor(ctx.getColor(R.color.grey_background));
                ageID = 3;
                if (ageListener != null) ageListener.onAgeChanged(ageID);
                if (upgrades1Adapter != null && upgrades1Adapter.isEnabled()) upgrades1Adapter.filterList(ageID, civ1ID);
                if (upgrades2Adapter != null && upgrades2Adapter.isEnabled()) upgrades2Adapter.filterList(ageID, civ2ID);
            }
        });

    }

    protected void initView(){
        inflate(getContext(), R.layout.age_two_civ_selector, this);
    }

    public void setOnAgeChangeListener(OnAgeChangeListener l){
        ageListener = l;
    }

    public void setOnEntity1ChangeListener(OnEntityChangeListener l){
        entity1Listener = l;
    }

    public void setOnEntity2ChangeListener(OnEntityChangeListener l){
        entity2Listener = l;
    }


    public void setupCiv1Selector(int civ, List<String> civs){
        setupCivSelector(civ, civs, spinner1);
    }

    public void setupCiv2Selector(int civ, List<String> civs){
        setupCivSelector(civ, civs, spinner2);
    }



    public void setupUpgrades1Selector(List<Integer> list){
        upgrades1Adapter = new UpgradeSelectorAdapter(ctx, 0, Database.getUpgradeElementList(list));
        upgrades1Adapter.filterList(ageID, civ1ID);
        upgrades1Adapter.setOnItemChangedListener(new UpgradeSelectorAdapter.OnItemChangedListener() {
            @Override
            public void onItemChanged(List<Integer> list) {
                if (entity1Listener != null) entity1Listener.onUpgradesChanged(list);
            }
        });
        TextView upgrade1Button = findViewById(R.id.upgrade_selector1);
        setupUpgradePopup(upgrade1Button, upgrades1Adapter);
    }

    public void setupUpgrades2Selector(List<Integer> list){
        upgrades2Adapter = new UpgradeSelectorAdapter(ctx, 0, Database.getUpgradeElementList(list));
        upgrades2Adapter.filterList(ageID, civ1ID);
        upgrades2Adapter.setOnItemChangedListener(new UpgradeSelectorAdapter.OnItemChangedListener() {
            @Override
            public void onItemChanged(List<Integer> list) {
                if (entity2Listener != null) entity2Listener.onUpgradesChanged(list);
            }
        });
        TextView upgrade2Button = findViewById(R.id.upgrade_selector2);
        setupUpgradePopup(upgrade2Button, upgrades2Adapter);
    }

    private void setupUpgradePopup(TextView button, final UpgradeSelectorAdapter adapter){
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            if (adapter != null && adapter.isEnabled()) {
                LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View view = inflater.inflate(R.layout.upgrades_popup, null);
                ListView list = view.findViewById(R.id.upgrade_list);
                list.setAdapter(adapter);
                int w = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 300, getResources().getDisplayMetrics());
                int h = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 500, getResources().getDisplayMetrics());
                final PopupWindow pw = new PopupWindow(view, w, h, true);
                pw.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                pw.setOutsideTouchable(true);
                pw.setTouchInterceptor(new View.OnTouchListener() {
                    public boolean onTouch(View v, MotionEvent event) {
                        if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                            pw.dismiss();
                            return true;
                        }
                        return false;
                    }
                });
                pw.showAsDropDown(v);
            }
            }
        });
    }

    private void setupCivSelector(int civ, List<String> civs, AppCompatSpinner spinner){
        if (spinner == spinner1) {
            civ1ID = civ;
            civ1Names = civs;
            setupSpinner(spinner, civ1ID, civ1Names);
        }
        else {
            civ2ID = civ;
            civ2Names = civs;
            setupSpinner(spinner, civ2ID, civ2Names);
        }

    }

    private void setupSpinner(final AppCompatSpinner spinner, final int civID, final List<String> civNames){
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, civNames);
        spinner.setAdapter(adapter);
        int pos = Utils.findSpinnerPosition(civID, civRelation, civNames);
        spinner.setSelection(pos);  
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String civName = civNames.get(position);
                if (spinner == spinner1) {
                    civ1ID = civRelationR.get(civName);
                    if (entity1Listener != null) entity1Listener.onCivChanged(civ1ID);
                }
                else{
                    civ2ID = civRelationR.get(civName);
                    if (entity2Listener != null) entity2Listener.onCivChanged(civ2ID);
                }
                if (upgrades1Adapter != null && upgrades1Adapter.isEnabled()) upgrades1Adapter.filterList(ageID, civ1ID);
                if (upgrades2Adapter != null && upgrades2Adapter.isEnabled()) upgrades2Adapter.filterList(ageID, civ2ID);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    public interface OnAgeChangeListener {
        void onAgeChanged(int age);
    }

    public interface OnEntityChangeListener{
        void onCivChanged(int civ);

        void onUpgradesChanged(List<Integer> list);
    }



    public void showDarkAge(){
        ageButton1.setVisibility(LinearLayout.VISIBLE);
        ageDivider1.setVisibility(LinearLayout.VISIBLE);
        ageButton2.setVisibility(LinearLayout.VISIBLE);
        ageDivider2.setVisibility(LinearLayout.VISIBLE);
        ageButton3.setVisibility(LinearLayout.VISIBLE);
        ageDivider3.setVisibility(LinearLayout.VISIBLE);
    }

    public void showFeudalAge(){
        ageButton1.setVisibility(LinearLayout.GONE);
        ageDivider1.setVisibility(LinearLayout.GONE);
        ageButton2.setVisibility(LinearLayout.VISIBLE);
        ageDivider2.setVisibility(LinearLayout.VISIBLE);
        ageButton3.setVisibility(LinearLayout.VISIBLE);
        ageDivider3.setVisibility(LinearLayout.VISIBLE);
    }

    public void showCastleAge(){
        ageButton1.setVisibility(LinearLayout.GONE);
        ageDivider1.setVisibility(LinearLayout.GONE);
        ageButton2.setVisibility(LinearLayout.GONE);
        ageDivider2.setVisibility(LinearLayout.GONE);
        ageButton3.setVisibility(LinearLayout.VISIBLE);
        ageDivider3.setVisibility(LinearLayout.VISIBLE);
    }

    public void showImperialAge(){
        ageButton1.setVisibility(LinearLayout.GONE);
        ageDivider1.setVisibility(LinearLayout.GONE);
        ageButton2.setVisibility(LinearLayout.GONE);
        ageDivider2.setVisibility(LinearLayout.GONE);
        ageButton3.setVisibility(LinearLayout.GONE);
        ageDivider3.setVisibility(LinearLayout.GONE);
    }

    public void selectInitialAge(int age){
        switch (age){
            case 0:
                ageButton1.callOnClick();
                break;
            case 1:
                ageButton2.callOnClick();
                break;
            case 2:
                ageButton3.callOnClick();
                break;
            case 3:
                ageButton4.callOnClick();
                break;
        }
    }
}
