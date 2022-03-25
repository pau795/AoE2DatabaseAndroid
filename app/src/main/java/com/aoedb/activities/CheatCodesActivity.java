package com.aoedb.activities;

import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.aoedb.R;

public class CheatCodesActivity extends DrawerActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.title_cheat_codes);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_cheat_codes, null, false);
        setActivityView(contentView);

        TextView resources = findViewById(R.id.cheat_codes_resources);
        resources.setText(Html.fromHtml(getString(R.string.cheat_codes_resources).replace("\n", "<br>"), Html.FROM_HTML_MODE_COMPACT));
        TextView units = findViewById(R.id.cheat_codes_units);
        units.setText(Html.fromHtml(getString(R.string.cheat_codes_units).replace("\n", "<br>"), Html.FROM_HTML_MODE_COMPACT));
        TextView various = findViewById(R.id.cheat_codeS_various);
        various.setText(Html.fromHtml(getString(R.string.cheat_codes_various).replace("\n", "<br>"), Html.FROM_HTML_MODE_COMPACT));
        TextView nonCheat = findViewById(R.id.non_cheat_codes);
        nonCheat.setText(Html.fromHtml(getString(R.string.non_cheat_codes).replace("\n", "<br>"), Html.FROM_HTML_MODE_COMPACT));
        TextView events = findViewById(R.id.cheat_codes_events);
        events.setText(Html.fromHtml(getString(R.string.event_cheat_codes).replace("\n", "<br>"), Html.FROM_HTML_MODE_COMPACT));
    }
}
