package com.aoedb.activities;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import com.aoedb.R;

public class TradeMechanicsActivity extends DrawerActivity {
    Boolean state1, state2, state3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.title_trade_mechanics);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_trade_mechanics, null, false);
        setActivityView(contentView);
        state1 = false;
        state2 = false;
        state3 = false;
        initialize_buttons();
        initialize_texts();
    }

    private void initialize_buttons(){
        Button b1 = findViewById(R.id.trade_button1);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button b = (Button) v;
                TableLayout t = findViewById(R.id.trade_table1);
                if (state1){
                    b.setText(getString(R.string.gm_show_table));
                    t.setVisibility(LinearLayout.GONE);
                    state1=false;
                }
                else {
                    b.setText(getString(R.string.gm_hide_table));
                    t.setVisibility(LinearLayout.VISIBLE);
                    state1=true;
                }
            }
        });

        Button b2 = findViewById(R.id.trade_button2);
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button b = (Button) v;
                TableLayout t = findViewById(R.id.trade_table2);
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

        Button b3 = findViewById(R.id.trade_button3);
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button b = (Button) v;
                TableLayout t = findViewById(R.id.trade_table3);
                if (state3){
                    b.setText(getString(R.string.gm_show_table));
                    t.setVisibility(LinearLayout.GONE);
                    state3=false;
                }
                else {
                    b.setText(getString(R.string.gm_hide_table));
                    t.setVisibility(LinearLayout.VISIBLE);
                    state3=true;
                }
            }
        });
    }

    private void initialize_texts(){
        TextView t2 = findViewById(R.id.trade_profit2);
        TextView t3 = findViewById(R.id.trade_profit3);
        TextView t4 = findViewById(R.id.trade_profit4);
        TextView t5 = findViewById(R.id.trade_profit5);
        t2.setText(getText(R.string.trade_mechanics_profit2));
        t3.setText(getText(R.string.trade_mechanics_profit3));
        t4.setText(getText(R.string.trade_mechanics_profit4));
        t5.setText(getText(R.string.trade_mechanics_profit5));
    }
}