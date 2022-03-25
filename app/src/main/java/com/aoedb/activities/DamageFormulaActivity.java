package com.aoedb.activities;

import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import com.aoedb.R;

public class DamageFormulaActivity extends DrawerActivity {

    boolean state1, state2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.title_damage_formula);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_damage_formula, null, false);
        setActivityView(contentView);
        state1 = state2 = false;
        initialize_buttons();

        TextView t1 = findViewById(R.id.damage_formula_1);
        t1.setText(Html.fromHtml(getFormula1String(), Html.FROM_HTML_MODE_COMPACT));

        TextView t2 = findViewById(R.id.damage_formula_2);
        t2.setText(Html.fromHtml(getFormula2String(), Html.FROM_HTML_MODE_COMPACT));

        TextView t3 = findViewById(R.id.damage_formula_disclaimer);
        t3.setText(Html.fromHtml(getString(R.string.damage_formula_formula2).replace("\n", "<br>"), Html.FROM_HTML_MODE_COMPACT));

    }

    private String getFormula1String(){
        String text = getString(R.string.damage_formula_formula1_text);
        text += "<br><br>";
        text += "• "+ getString(R.string.damage_formula_formula1_text1) + "<br>";
        text += "• "+ getString(R.string.damage_formula_formula1_text2) + "<br>";
        text += "• "+ getString(R.string.damage_formula_formula1_text3) + "<br>";
        return text;
    }

    private String getFormula2String(){
        String text = getString(R.string.damage_formula_formula2_text);
        text += "<br><br>";
        text += "• "+ getString(R.string.damage_formula_formula2_text1) + "<br>";
        text += "• "+ getString(R.string.damage_formula_formula2_text2) + "<br>";
        text += "• "+ getString(R.string.damage_formula_formula2_text3) + "<br>";
        text += "• "+ getString(R.string.damage_formula_formula2_text5) + "<br>";
        text += "• "+ getString(R.string.damage_formula_formula2_text6) + "<br>";
        text += "• "+ getString(R.string.damage_formula_formula2_text7) + "<br>";
        text += "• "+ getString(R.string.damage_formula_formula2_text8) + "<br>";
        return text;
    }

    private void initialize_buttons(){
        Button b1 = findViewById(R.id.damage_button1);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button b = (Button) v;
                TableLayout t = findViewById(R.id.example1);
                if (state1){
                    b.setText(getString(R.string.gm_show_table));
                    t.setVisibility(LinearLayout.GONE);
                    state1 = false;
                }
                else {
                    b.setText(getString(R.string.gm_hide_table));
                    t.setVisibility(LinearLayout.VISIBLE);
                    state1 = true;
                }
            }
        });

        Button b2 = findViewById(R.id.damage_button2);
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button b = (Button) v;
                TableLayout t = findViewById(R.id.example2);
                if (state2){
                    b.setText(getString(R.string.gm_show_table));
                    t.setVisibility(LinearLayout.GONE);
                    state2=false;
                }
                else {
                    b.setText(getString(R.string.gm_hide_table));
                    t.setVisibility(LinearLayout.VISIBLE);
                    state2=true;
                }
            }
        });

    }
}
