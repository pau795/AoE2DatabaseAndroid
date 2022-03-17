package com.aoedb.activities;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;

import androidx.appcompat.widget.SearchView;

import com.aoedb.R;
import com.aoedb.adapters.EntityAdapter;
import com.aoedb.data.EntityElement;

import java.util.LinkedHashMap;
import java.util.List;

public abstract class ListActivity extends DrawerActivity {

    Context ctx;

    LinkedHashMap<String, List<EntityElement>> dataModels;
    ExpandableListView elv;
    EntityAdapter adapter;
    Menu menu;
    int sort;
    int choice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ctx = this;
        View contentView = inflater.inflate(R.layout.activity_element_list, null, false);
        setActivityView(contentView);
        elv = findViewById(R.id.element_list);
        sort = 0;
        choice = 0;
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_sort_search, menu);
        this.menu = menu;
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_search) {
            final SearchView searchView = (SearchView) item.getActionView();
            searchView.setQueryHint(ctx.getString(R.string.search_hint));
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String s) {
                    adapter = new EntityAdapter(ctx, dataModels, s);
                    elv.setAdapter(adapter);
                    for (int i = 0; i < adapter.getGroupCount(); i++) elv.expandGroup(i);
                    return true;
                }
            });

            item.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
                @Override
                public boolean onMenuItemActionExpand(MenuItem item) {
                    menu.findItem(R.id.action_sort).setVisible(false);
                    menu.findItem(R.id.action_search).setVisible(false);
                    return true;
                }

                @Override
                public boolean onMenuItemActionCollapse(MenuItem item) {
                    invalidateOptionsMenu();
                    return true;
                }
            });
        }
        if (id == R.id.action_sort) {
            actionSort();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public abstract void actionSort();

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.action_sort).setVisible(true);
        menu.findItem(R.id.action_search).setVisible(true);
        return true;
    }


}
