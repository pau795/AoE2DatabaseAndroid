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

public class AgeCivSelector extends LinearLayout {

    int ageID, civID;
    AppCompatSpinner spinner;
    HashMap<Integer, String> civRelation;
    HashMap<String, Integer> civRelationR;
    LinearLayout ageButton1;
    LinearLayout ageButton2;
    LinearLayout ageButton3;
    LinearLayout ageButton4;
    LinearLayout ageDivider1;
    LinearLayout ageDivider2;
    LinearLayout ageDivider3;
    List<String> civNames;

    Context ctx;

    UpgradeSelectorAdapter upgradeAdapter;

    OnChangeListener listener;


    public AgeCivSelector(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
        ctx = context;
        civRelation = Database.getCivNameMap();
        civRelationR = Database.getReversedCivNameMap();
        spinner = findViewById(R.id.spinner1);
        ageButton1 = findViewById(R.id.button_age1);
        ageButton2 = findViewById(R.id.button_age2);
        ageButton3 = findViewById(R.id.button_age3);
        ageButton4 = findViewById(R.id.button_age4);
        ageDivider1 = findViewById(R.id.button_divider1);
        ageDivider2 = findViewById(R.id.button_divider2);
        ageDivider3 = findViewById(R.id.button_divider3);
        civNames = new ArrayList<>();
        setupAgeButtons();
        setupUpgradePopup();


    }

    private void setupAgeButtons() {
        ageButton1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ageButton1.setBackgroundColor(ctx.getColor(R.color.grey_background));
                ageButton2.setBackgroundColor(ctx.getColor(R.color.white));
                ageButton3.setBackgroundColor(ctx.getColor(R.color.white));
                ageButton4.setBackgroundColor(ctx.getColor(R.color.white));
                ageID = 0;
                if (listener != null) listener.onAgeChanged(ageID);
                if (upgradeAdapter.isEnabled()) upgradeAdapter.filterList(ageID, civID);
            }
        });
        ageButton2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ageButton1.setBackgroundColor(ctx.getColor(R.color.white));
                ageButton2.setBackgroundColor(ctx.getColor(R.color.grey_background));
                ageButton3.setBackgroundColor(ctx.getColor(R.color.white));
                ageButton4.setBackgroundColor(ctx.getColor(R.color.white));
                ageID = 1;
                if (listener != null) listener.onAgeChanged(ageID);
                if (upgradeAdapter.isEnabled()) upgradeAdapter.filterList(ageID, civID);
            }
        });
        ageButton3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ageButton1.setBackgroundColor(ctx.getColor(R.color.white));
                ageButton2.setBackgroundColor(ctx.getColor(R.color.white));
                ageButton3.setBackgroundColor(ctx.getColor(R.color.grey_background));
                ageButton4.setBackgroundColor(ctx.getColor(R.color.white));
                ageID = 2;
                if (listener != null) listener.onAgeChanged(ageID);
                if (upgradeAdapter.isEnabled()) upgradeAdapter.filterList(ageID, civID);
            }
        });
        ageButton4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ageButton1.setBackgroundColor(ctx.getColor(R.color.white));
                ageButton2.setBackgroundColor(ctx.getColor(R.color.white));
                ageButton3.setBackgroundColor(ctx.getColor(R.color.white));
                ageButton4.setBackgroundColor(ctx.getColor(R.color.grey_background));
                ageID = 3;
                if (listener != null) listener.onAgeChanged(ageID);
                if (upgradeAdapter.isEnabled()) upgradeAdapter.filterList(ageID, civID);
            }
        });

    }

    protected void initView(){
        inflate(getContext(), R.layout.age_civ_selector, this);
    }

    public void setupCivSelector(int civ, List<String> civs){
        civID = civ;
        civNames = civs;
        setupSpinner();
    }

    private void setupUpgradePopup(){

        TextView b = findViewById(R.id.upgrade_selector);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (upgradeAdapter.isEnabled()) {
                    LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View view = inflater.inflate(R.layout.upgrades_popup, null);
                    ListView list = view.findViewById(R.id.upgrade_list);
                    list.setAdapter(upgradeAdapter);
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

    public void setupUpgradeSelector(List<Integer> list){
        upgradeAdapter = new UpgradeSelectorAdapter(ctx, 0, Database.getUpgradeElementList(list));
        upgradeAdapter.filterList(ageID, civID);
        upgradeAdapter.setOnItemChangedListener(new UpgradeSelectorAdapter.OnItemChangedListener() {
            @Override
            public void onItemChanged(List<Integer> list) {
            if (listener != null) listener.onUpgradesChanged(list);
            }
        });
    }

    private void setupSpinner(){
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, civNames);
        spinner.setAdapter(adapter);
        int pos = Utils.findSpinnerPosition(civID, civRelation, civNames);
        spinner.setSelection(pos);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String civName = civNames.get(position);
                civID = civRelationR.get(civName);
                if (listener != null) listener.onCivChanged(civID);
                if (upgradeAdapter.isEnabled()) upgradeAdapter.filterList(ageID, civID);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    public void setOnChangeListener(OnChangeListener l){
        listener = l;
    }

    public interface OnChangeListener {
        void onAgeChanged(int age);

        void onCivChanged(int civ);

        void onUpgradesChanged(List<Integer> list);

    }


    public void hideDarkAge(){
        ageButton1.setVisibility(LinearLayout.GONE);
        ageDivider1.setVisibility(LinearLayout.GONE);
    }

    public void hideFeudalAge(){
        hideDarkAge();
        ageButton2.setVisibility(LinearLayout.GONE);
        ageDivider2.setVisibility(LinearLayout.GONE);
    }

    public void hideCastleAge(){
        hideFeudalAge();
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