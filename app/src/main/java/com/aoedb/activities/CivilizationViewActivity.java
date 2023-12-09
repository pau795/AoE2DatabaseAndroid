package com.aoedb.activities;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.aoedb.R;
import com.aoedb.data.Civilization;
import com.aoedb.database.Database;
import com.aoedb.database.Utils;
import com.aoedb.widgets.CivilizationEntityButton;


public class CivilizationViewActivity extends DrawerActivity {

    MediaPlayer mp;
    ImageView play;

    int civID;
    Civilization civ;

    Context c;

    OnItemClicked listener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_civilization_view, null, false);
        setActivityView(contentView);
        setAppBarColor(getColor(R.color.red_background));
        c = this;
        civID = getIntent().getIntExtra(Database.CIV,1);
        civ = Database.getCivilization(civID);
        setTitle(civ.getName());
        int soundID = civ.getCivThemeID();
        mp = MediaPlayer.create(c, soundID);

        play = findViewById(R.id.civ_theme_icon);
        play.setOnClickListener(v -> {
            ImageView i = (ImageView) v;
            if (!mp.isPlaying())  {
                mp.start();
                i.setImageResource(R.drawable.stopicon_red);
            }
            else resetPlayer();
        });

        mp.setOnCompletionListener(mp -> play.setImageResource(R.drawable.playicon_red));

        mp.start();
        resetPlayer();
        listener = this::resetPlayer;

        TextView name = findViewById(R.id.civ_name);
        name.setText(civ.getName());
        ImageView icon = findViewById(R.id.civ_icon);
        icon.setImageResource(civ.getNameElement().getImage());
        icon.setOnClickListener(v -> Utils.showPopupIcon(v, c, civ.getNameElement().getName(), civ.getNameElement().getImage(), true, "red"));
        TextView style = findViewById(R.id.civ_style);
        style.setText(civ.getCivStyle());
        TextView bonuses = findViewById(R.id.civ_bonuses);
        bonuses.setText(Html.fromHtml(civ.writeCivBonuses(this), Html.FROM_HTML_MODE_LEGACY));

        CivilizationEntityButton uniqueUnit1 = findViewById(R.id.unique_unit1);
        uniqueUnit1.setupButton(civ, listener, true);


        if (civ.getUniqueUnitList().size() > 1) {
            CivilizationEntityButton uniqueUnit2 = findViewById(R.id.unique_unit2);
            uniqueUnit2.setVisibility(View.VISIBLE);
            uniqueUnit2.setupButton(civ, listener, true);
            if (civ.getUniqueUnitList().size() > 2) {
                CivilizationEntityButton uniqueUnit3 = findViewById(R.id.unique_unit3);
                uniqueUnit3.setVisibility(View.VISIBLE);
                uniqueUnit3.setupButton(civ, listener, true);

            }
        }

        CivilizationEntityButton castleUniqueTech = findViewById(R.id.castle_unique_tech);
        castleUniqueTech.setupButton(civ, listener, true);

        CivilizationEntityButton imperialUniqueTech = findViewById(R.id.imperial_unique_tech);
        imperialUniqueTech.setupButton(civ, listener, true);

        if (civ.getUniqueBuildingList().size() > 0) {
            TextView ubt = findViewById(R.id.unique_building_title);
            View ubdiv = findViewById(R.id.unique_building_divider);
            View ubs1 = findViewById(R.id.unique_building_space1);
            View ubs2 = findViewById(R.id.unique_building_space2);
            CivilizationEntityButton uniqueBuilding = findViewById(R.id.unique_building);
            uniqueBuilding.setupButton(civ, listener, true);
            uniqueBuilding.setVisibility(View.VISIBLE);
            ubt.setVisibility(View.VISIBLE);
            ubdiv.setVisibility(View.VISIBLE);
            ubs1.setVisibility(View.VISIBLE);
            ubs2.setVisibility(View.VISIBLE);
            if (civ.getUniqueBuildingList().size() > 1) {
                CivilizationEntityButton uniqueBuilding2 = findViewById(R.id.unique_building2);
                uniqueBuilding2.setupButton(civ, listener, true);
                uniqueBuilding2.setVisibility(View.VISIBLE);
            }
        }

        ImageView techTree = findViewById(R.id.civ_tech_tree_icon);
        techTree.setOnClickListener(v -> {
            resetPlayer();
            Intent i =  new Intent(c, LoadingTechTreeActivity.class);
            i.putExtra(Database.CIV, civID);
            startActivity(i);
        });

    }


    public interface OnItemClicked {
        void onItemClicked();
    }

    public void resetPlayer(){
        mp.pause();
        mp.seekTo(0);
        play.setImageResource(R.drawable.playicon_red);
    }

    @Override
    public void onBackPressed(){
        resetPlayer();
        super.onBackPressed();
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        resetPlayer();
        return super.onNavigationItemSelected(item);
    }

}
