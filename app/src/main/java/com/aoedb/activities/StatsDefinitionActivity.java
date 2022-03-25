package com.aoedb.activities;

import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.aoedb.R;

public class StatsDefinitionActivity extends DrawerActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.title_stats_definition);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_stats_definition, null, false);
        setActivityView(contentView);
        TextView t = findViewById(R.id.stats_definition);
        t.setText(Html.fromHtml(getString(R.string.stats_definition1).replace("\n", "<br>"), Html.FROM_HTML_MODE_COMPACT));
    }
}
