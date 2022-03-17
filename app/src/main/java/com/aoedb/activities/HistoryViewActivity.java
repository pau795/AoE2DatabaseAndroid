package com.aoedb.activities;

import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.aoedb.R;
import com.aoedb.database.Database;

public class HistoryViewActivity extends DrawerActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_history_view, null, false);
        setActivityView(contentView);
        final int historyID = getIntent().getIntExtra("id",0);
        final String historyName = getIntent().getStringExtra("name");
        setTitle(historyName);
        String text = Database.getHistoryText(historyID);
        TextView t = findViewById(R.id.history_text);
        t.setText(Html.fromHtml(text, Html.FROM_HTML_MODE_LEGACY));
    }
}
