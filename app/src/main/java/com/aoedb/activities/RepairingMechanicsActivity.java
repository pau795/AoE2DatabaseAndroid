package com.aoedb.activities;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.aoedb.R;

public class RepairingMechanicsActivity extends DrawerActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.title_repairing_mechanics);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_repairing_mechanics, null, false);
        setActivityView(contentView);
        Button b = findViewById(R.id.repair_button);

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button b = (Button) v;
                LinearLayout t = findViewById(R.id.repair_table);
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
    }
}
