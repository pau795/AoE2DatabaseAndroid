package com.aoedb.activities;

import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.widget.Button;
import android.view.View;
import android.widget.LinearLayout;

import android.widget.TableLayout;
import android.widget.TextView;

import com.aoedb.R;

public class BuildingMechanicsActivity extends DrawerActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.title_building_mechanics);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_building_mechanics, null, false);
        setActivityView(contentView);
        Button b = findViewById(R.id.building_button);

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button b = (Button) v;
                TableLayout t = findViewById(R.id.building_table);
                if (b.getText().equals(getString(R.string.gm_hide_table))){
                    b.setText(getString(R.string.gm_show_table));
                    t.setVisibility(LinearLayout.GONE);
                }
                else {
                    b.setText(getString(R.string.gm_hide_table));
                    t.setVisibility(LinearLayout.VISIBLE);
                }
            }
        } );

        TextView t = findViewById(R.id.building_formula);
        t.setText(Html.fromHtml(getFormulaString(), Html.FROM_HTML_MODE_COMPACT));


    }

    private String getFormulaString(){
        String text = getString(R.string.building_mechanics_formula_text);
        text += "<br><br>";
        text += "• "+ getString(R.string.building_mechanics_formula_text1) + "<br>";
        text += "• "+ getString(R.string.building_mechanics_formula_text2) + "<br>";
        text += "• "+ getString(R.string.building_mechanics_formula_text3) + "<br>";
        return text;
    }

}
