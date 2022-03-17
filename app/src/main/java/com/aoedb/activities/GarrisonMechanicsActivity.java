package com.aoedb.activities;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;

import com.aoedb.R;

public class GarrisonMechanicsActivity extends DrawerActivity {

    boolean state1, state2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.title_garrison_mechanics);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_garrison_mechanics, null, false);
        setActivityView(contentView);
        state1 = state2 = false;
        initialize_buttons();
    }

    private void initialize_buttons(){
        Button b1 = findViewById(R.id.garrison_button1);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button b = (Button) v;
                TableLayout t = findViewById(R.id.garrison_table);
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

        Button b2 = findViewById(R.id.garrison_button2);
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button b = (Button) v;
                TableLayout t = findViewById(R.id.projectile_table);
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
