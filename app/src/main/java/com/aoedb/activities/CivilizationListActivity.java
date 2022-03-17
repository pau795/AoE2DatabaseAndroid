package com.aoedb.activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import com.aoedb.R;
import com.aoedb.adapters.EntityAdapter;
import com.aoedb.database.Database;


public class CivilizationListActivity extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.title_activity_civilization);
        dataModels = Database.getGroupList(Database.CIVILIZATION_GROUPS, sort);
        adapter = new EntityAdapter(this, dataModels);
        elv.setAdapter(adapter);
        setAppBarColor(getColor(R.color.red_background));
        for(int i=0; i < adapter.getGroupCount(); i++) elv.expandGroup(i);
    }

    public void actionSort(){
        Dialog dialog;
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.Theme_AppCompat_Light_Dialog);
        builder.setTitle(getString(R.string.action_sort));
        String[] items = {getString(R.string.sort_alphabetically), getString(R.string.sort_expansions)};
        builder.setSingleChoiceItems(items, sort, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                choice = i;
            }}).setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int id) {
                sort=choice;
                dataModels = Database.getGroupList(Database.CIVILIZATION_GROUPS, sort);
                adapter = new EntityAdapter(ctx, dataModels);
                elv.setAdapter(adapter);
                for(int i=0; i < adapter.getGroupCount(); i++) elv.expandGroup(i);
            }}).setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int id) {
            }
        });
        dialog = builder.create();
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }

}
