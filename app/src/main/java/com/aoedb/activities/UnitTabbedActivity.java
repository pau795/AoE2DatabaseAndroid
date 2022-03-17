package com.aoedb.activities;

import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;

import com.aoedb.R;
import com.aoedb.data.Unit;
import com.aoedb.database.Database;

public class UnitTabbedActivity extends TabbedActivity {


    @Override
    protected void initView() {
        Unit u = Database.getUnit(entityID);
        entityType = u.getType();
        setTitle(u.getName());
    }

    @Override
    protected String[] getTabTitles() {
        return new String[]{
            getString(R.string.basic_info),
            getString(R.string.attack_values),
            getString(R.string.armor_values),
            getString(R.string.upgrades),
            getString(R.string.performance),
            getString(R.string.availability),
            getString(R.string.bonuses)};
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_stats_compare, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_compare) {
            Intent i = new Intent(this, UnitComparatorActivity.class);
            int civID = Database.getSelectedCiv(Database.UNIT, entityID);
            i.putExtra(Database.UNIT, entityID);
            i.putExtra(Database.CIV, civID);
            startActivity(i);
            return true;
        }
        if (id == R.id.action_stats) {
            Intent i = new Intent(this, StatsDefinitionActivity.class);
            startActivity(i);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
