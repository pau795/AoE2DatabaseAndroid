package com.aoedb.activities;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;

import com.aoedb.R;

public class FarmMechanicsActivity extends DrawerActivity {
    Boolean state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.title_farm_mechanics);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_farm_mechanics, null, false);
        setActivityView(contentView);
        state = false;
        Button b = findViewById(R.id.farm_button);

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button b = (Button) v;
                TableLayout t = findViewById(R.id.farm_table);
                if (state){
                    b.setText(getString(R.string.gm_show_table));
                    t.setVisibility(LinearLayout.GONE);
                    state=false;
                }
                else {
                    b.setText(getString(R.string.gm_hide_table));
                    t.setVisibility(LinearLayout.VISIBLE);
                    state=true;
                }
            }
        });
    }
}
