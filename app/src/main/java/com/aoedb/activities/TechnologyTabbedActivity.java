package com.aoedb.activities;

import com.aoedb.R;
import com.aoedb.data.Technology;
import com.aoedb.database.Database;

public class TechnologyTabbedActivity extends TabbedActivity {

    @Override
    protected void initView() {
        Technology t = Database.getTechnology(entityID);
        entityType = t.getType();
        setTitle(t.getName());
    }

    @Override
    protected String[] getTabTitles() {
        return new String[]{
            getString(R.string.basic_info),
            getString(R.string.applications),
            getString(R.string.availability),
            getString(R.string.bonuses)};
    }
}
