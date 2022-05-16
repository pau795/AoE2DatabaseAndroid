package com.aoedb.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.aoedb.R;
import com.aoedb.adapters.EntityAdapter;
import com.aoedb.data.EntityElement;
import com.aoedb.database.Database;
import com.aoedb.database.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;


public class MainActivity extends DrawerActivity{

    ScrollView layout;

    Context ctx;
    LinkedHashMap<String, List<EntityElement>> dataModels;
    ExpandableListView elv;
    View contentView;
    boolean initLang;
    EntityAdapter adapter;
    boolean initList;
    Menu menu;

    String lang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.app_name);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        contentView = inflater.inflate(R.layout.activity_main, null, false);
        setActivityView(contentView);
        ctx=this;
        initLang = false;
        layout = findViewById(R.id.main_layout);
        initList=false;
        initializeLayouts();
        SharedPreferences prefs = getSharedPreferences("init", MODE_PRIVATE);
        if (prefs.getInt("support", -1) == -1) {
            supportPopup();
            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt("support", 1);
            editor.apply();
        }

    }

    private void initializeLayouts(){
        LinearLayout units = findViewById(R.id.layout_units);
        LinearLayout buildings = findViewById(R.id.layout_buildings);
        LinearLayout techs = findViewById(R.id.layout_techs);
        LinearLayout civs = findViewById(R.id.layout_civs);
        LinearLayout techTree = findViewById(R.id.layout_tech_tree);
        LinearLayout gameMechanics = findViewById(R.id.layout_game_mechanics);
        LinearLayout tools = findViewById(R.id.layout_tools);
        LinearLayout misc = findViewById(R.id.layout_misc);
        LinearLayout newQuiz = findViewById(R.id.layout_new_quiz);
        LinearLayout scores = findViewById(R.id.layout_scores);
        LinearLayout web = findViewById(R.id.layout_web);

        setupLangLayout();

        units.setOnClickListener(v -> {
            Intent i = new Intent(ctx, UnitListActivity.class);
            startActivity(i);
        });
        buildings.setOnClickListener(v -> {
            Intent i = new Intent(ctx, BuildingListActivity.class);
            startActivity(i);
        });
        techs.setOnClickListener(v -> {
            Intent i = new Intent(ctx, TechnologyListActivity.class);
            startActivity(i);
        });
        civs.setOnClickListener(v -> {
            Intent i = new Intent(ctx, CivilizationListActivity.class);
            startActivity(i);
        });
        techTree.setOnClickListener(v -> {
            Intent i = new Intent(ctx, LoadingTechTreeActivity.class);
            i.putExtra(Database.CIV, 1);
            startActivity(i);
        });
        gameMechanics.setOnClickListener(v -> {
            Intent i = new Intent(ctx, GameMechanicsActivity.class);
            startActivity(i);
        });
        tools.setOnClickListener(v -> {
            Intent i = new Intent(ctx, ToolsActivity.class);
            startActivity(i);
        });
        misc.setOnClickListener(v -> {
            Intent i = new Intent(ctx, MiscActivity.class);
            startActivity(i);
        });
        newQuiz.setOnClickListener(v -> {
            Intent i = new Intent(ctx, NewQuizActivity.class);
            startActivity(i);
        });
        scores.setOnClickListener(v -> {
            Intent i = new Intent(ctx, ScoreActivity.class);
            startActivity(i);
        });
        web.setOnClickListener(v -> {
            Intent i = new Intent(Intent.ACTION_VIEW , Uri.parse(getString(R.string.web_app_url)));
            startActivity(i);
        });

    }

    private void setupLangLayout(){
        final AppCompatSpinner languageSpinner = findViewById(R.id.language_spinner);
        List<String> languages = new ArrayList<>(Arrays.asList(Database.ENGLISH_FLAG, Database.SPANISH_FLAG));
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, languages);
        languageSpinner.setAdapter(adapter);
        languageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String previousLang = lang;
                lang = Utils.getLanguageFromFlag(adapter.getItem(position));
                Database.writeLanguage(lang, ctx);
                setLocale(lang);
                if (initLang && !previousLang.equals(lang)){
                    finish();
                    Intent intent = new Intent(ctx, LoadingAppActivity.class);
                    startActivity(intent);
                }
                initLang = true;
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        lang = Database.readLanguage(this);
        languageSpinner.setSelection(Utils.getLanguagePosition(lang));
    }

    private void initializeList(){
        initList=true;
        elv = findViewById(R.id.element_list);
        dataModels = Database.readAllLists();
        adapter = new EntityAdapter(this, dataModels);
        elv.setAdapter(adapter);
        for(int i=0; i < adapter.getGroupCount(); i++) elv.expandGroup(i);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        this.menu = menu;

        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_search) {
            if (!initList) initializeList();
            final SearchView searchView = (SearchView) item.getActionView();
            searchView.setQueryHint(ctx.getString(R.string.search_hint));
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String s) {
                    layout.setVisibility(View.GONE);
                    adapter = new EntityAdapter(ctx, dataModels, s);
                    elv.setAdapter(adapter);
                    for (int i = 0; i < adapter.getGroupCount(); i++) elv.expandGroup(i);
                    elv.setVisibility(View.VISIBLE);
                    return true;
                }

            });

            item.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {

                @Override
                public boolean onMenuItemActionExpand(MenuItem item) {
                    menu.findItem(R.id.action_search).setVisible(false);
                    layout.setVisibility(View.GONE);
                    elv.setVisibility(View.VISIBLE);
                    return true;
                }

                @Override
                public boolean onMenuItemActionCollapse(MenuItem item) {
                    searchView.setQuery("", false);
                    elv.setVisibility(View.GONE);
                    layout.setVisibility(View.VISIBLE);
                    invalidateOptionsMenu();
                    return true;
                }
            });
        }

        return super.onOptionsItemSelected(item);
    }


    private void supportPopup(){
        new AlertDialog.Builder(this, R.style.Theme_AppCompat_Light_Dialog)
                .setTitle(getString(R.string.title_support))
                .setMessage(getString(R.string.support_text))
                .setNeutralButton(getString(R.string.buy_me_a_coffee), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent browse = new Intent(Intent.ACTION_VIEW , Uri.parse(getString(R.string.buy_me_a_coffee_url)));
                        startActivity(browse);
                    }
                })
                .setCancelable(true)
                .setPositiveButton(this.getString(R.string.ok), null)
                .show();
    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.action_search).setVisible(true);
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();


        if (id == R.id.nav_home) {
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
            this.finish();
        }
        if (id == R.id.nav_units) {
            Intent i =  new Intent(this, UnitListActivity.class);
            startActivity(i);
        } else if (id == R.id.nav_buildings) {
            Intent i =  new Intent(this, BuildingListActivity.class);
            startActivity(i);
        } else if (id == R.id.nav_techs) {
            Intent i =  new Intent(this, TechnologyListActivity.class);
            startActivity(i);
        } else if (id == R.id.nav_civs) {
            Intent i =  new Intent(this, CivilizationListActivity.class);
            startActivity(i);
        } else if (id == R.id.nav_techtree) {
            Intent i = new Intent(this, LoadingTechTreeActivity.class);
            i.putExtra(Database.CIV, 1);
            startActivity(i);
        } else if (id == R.id.nav_game_mechanics) {
            Intent i =  new Intent(this, GameMechanicsActivity.class);
            startActivity(i);
        } else if (id == R.id.nav_tools) {
            Intent i =  new Intent(this, ToolsActivity.class);
            startActivity(i);
        } else if (id == R.id.nav_misc) {
            Intent i =  new Intent(this, MiscActivity.class);
            startActivity(i);
        } else if (id == R.id.nav_quiz) {
            Intent i =  new Intent(this, NewQuizActivity.class);
            startActivity(i);
        } else if (id == R.id.nav_scores) {
            Intent i =  new Intent(this, ScoreActivity.class);
            startActivity(i);
        } else if (id == R.id.nav_web) {
            Intent i = new Intent(Intent.ACTION_VIEW , Uri.parse(getString(R.string.web_app_url)));
            startActivity(i);
            this.finish();
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}