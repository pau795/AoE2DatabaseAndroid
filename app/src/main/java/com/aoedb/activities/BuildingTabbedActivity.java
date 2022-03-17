package com.aoedb.activities;

import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;

import com.aoedb.R;
import com.aoedb.data.Building;
import com.aoedb.database.Database;

public class BuildingTabbedActivity extends TabbedActivity {

    @Override
    protected void initView() {
        Building b = Database.getBuilding(entityID);
        entityType = b.getType();
        setTitle(b.getName());
    }

    @Override
    protected String[] getTabTitles() {
        return new String[]{
                getString(R.string.basic_info),
                getString(R.string.attack_values),
                getString(R.string.armor_values),
                getString(R.string.upgrades),
                getString(R.string.units_and_research),
                getString(R.string.availability),
                getString(R.string.bonuses)};
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_stats, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_stats) {
            Intent i = new Intent(this, StatsDefinitionActivity.class);
            startActivity(i);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
