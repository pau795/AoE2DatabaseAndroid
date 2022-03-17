package com.aoedb.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.aoedb.R;
import com.aoedb.database.Database;
import com.google.android.material.navigation.NavigationView;

public abstract class DrawerActivity extends BaseActivity
    implements NavigationView.OnNavigationItemSelectedListener {

    protected DrawerLayout mDrawer;
    protected FrameLayout mainLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);
        Toolbar toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mDrawer = findViewById(R.id.drawer_layout);
        mainLayout = findViewById(R.id.main_content_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    protected void setActivityView(View v){
        mainLayout.addView(v);
    }

    protected void setAppBarColor(int color){
        Toolbar toolbar =  findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(color);
        NavigationView navigationView = findViewById(R.id.nav_view);
        View v1 = navigationView.getHeaderView(0);
        LinearLayout l1 = v1.findViewById(R.id.nav_header_layout);
        l1.setBackgroundColor(color);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();


        if (id == R.id.action_about) {
            Database.action_about(this);
            return true;
        }

        if (id == R.id.action_support) {
            Intent browse = new Intent(Intent.ACTION_VIEW , Uri.parse(getString(R.string.buy_me_a_coffee_url)));
            startActivity(browse);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            Intent i = new Intent(this, MainActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            this.finish();
        }
        else if (id == R.id.nav_units) {
            Intent i =  new Intent(this, MainActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            i =  new Intent(this, UnitListActivity.class);
            startActivity(i);
            this.finish();
        } else if (id == R.id.nav_buildings) {
            Intent i =  new Intent(this, MainActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            i =  new Intent(this, BuildingListActivity.class);
            startActivity(i);
            this.finish();
        } else if (id == R.id.nav_techs) {
            Intent i =  new Intent(this, MainActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            i =  new Intent(this, TechnologyListActivity.class);
            startActivity(i);
            this.finish();
        } else if (id == R.id.nav_civs) {
            Intent i =  new Intent(this, MainActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            i =  new Intent(this, CivilizationListActivity.class);
            startActivity(i);
            this.finish();
        } else if (id == R.id.nav_techtree) {
            Intent i =  new Intent(this, MainActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            i =  new Intent(this, LoadingTechTreeActivity.class);
            i.putExtra(Database.CIV, 1);
            startActivity(i);
            this.finish();
        } else if (id == R.id.nav_game_mechanics) {
            Intent i =  new Intent(this, MainActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            i =  new Intent(this, GameMechanicsActivity.class);
            startActivity(i);
            this.finish();
        } else if (id == R.id.nav_tools) {
            Intent i =  new Intent(this, ToolsActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            i =  new Intent(this, ToolsActivity.class);
            startActivity(i);
            this.finish();
        } else if (id == R.id.nav_misc) {
            Intent i =  new Intent(this, MainActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            i =  new Intent(this, MiscActivity.class);
            startActivity(i);
            this.finish();
        } else if (id == R.id.nav_quiz) {
            Intent i =  new Intent(this, MainActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            i =  new Intent(this, NewQuizActivity.class);
            startActivity(i);
            this.finish();
        } else if (id == R.id.nav_scores) {
            Intent i =  new Intent(this, MainActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            i =  new Intent(this, ScoreActivity.class);
            startActivity(i);
            this.finish();
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
