package com.aoedb.activities;

import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.aoedb.R;
import com.aoedb.adapters.EntityAdapter;
import com.aoedb.adapters.TwoColumnAdapter;
import com.aoedb.data.Building;
import com.aoedb.data.Entity;
import com.aoedb.data.EntityElement;
import com.aoedb.data.Item;
import com.aoedb.data.Technology;
import com.aoedb.data.Unit;
import com.aoedb.database.Database;
import com.aoedb.database.Utils;
import com.aoedb.widgets.AgeCivSelector;
import com.aoedb.widgets.EntityButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import pl.droidsonroids.gif.GifImageView;

public abstract class TabbedActivity extends DrawerActivity {

    int entityID;
    String entityType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_tabbed, null, false);
        setActivityView(contentView);
        setAppBarColor(getColor(R.color.blue_background));
        entityID = getIntent().getIntExtra(Database.ENTITY, 1);
        initView();
        Database.setSelectedAge(entityType, entityID, -1);
        final SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(this, entityID, entityType);
        ViewPager2 mViewPager = findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        TabLayout tabLayout = findViewById(R.id.tablayout);
        tabLayout.setSelectedTabIndicatorColor(getColor(R.color.blue_background));
        new TabLayoutMediator(tabLayout, mViewPager,
                new TabLayoutMediator.TabConfigurationStrategy() {
                    @Override
                    public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                        tab.setText(mSectionsPagerAdapter.getPageTitle(position));
                    }
                }
        ).attach();
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            LinearLayout layout = (LinearLayout)
                    LayoutInflater.from(this).inflate(R.layout.tab_layout, tabLayout, false);
            TextView tabTextView = layout.findViewById(R.id.tab_title);
            tabTextView.setText(tab.getText().toString().toUpperCase());
            tab.setCustomView(layout);
        }
    }

    protected abstract void initView();

    protected abstract String[] getTabTitles();

    public static class EntityFragment extends Fragment {

        private static final String ARG_SECTION_NUMBER = "section_number";
        private static final String ARG_ENTITY_ID = "entity_id";
        private static final String ARG_ENTITY_TYPE = "entity_type";

        String entityType;
        int entityID, ageID, civID, sectionNumber;
        List<Integer> upgradeList;

        LayoutInflater inflater;
        ViewGroup container;

        public EntityFragment() {
        }

        public static EntityFragment newInstance(int sectionNumber, int entityID, String entityType) {
            EntityFragment fragment = new EntityFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            args.putInt(ARG_ENTITY_ID, entityID);
            args.putString(ARG_ENTITY_TYPE, entityType);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            entityType = getArguments().getString(ARG_ENTITY_TYPE);
            entityID = getArguments().getInt(ARG_ENTITY_ID);
            sectionNumber = getArguments().getInt(ARG_SECTION_NUMBER);
            this.inflater = inflater;
            this.container = container;
            return getEntityView(entityID, entityType);
        }

        private View getEntityView(int id, String type){
            switch (type){
                case Database.UNIT: return getFragmentView(Database.getUnit(id));
                case Database.BUILDING: return getFragmentView(Database.getBuilding(id));
                default: return getFragmentView(Database.getTechnology(id));
            }
        }

        private View getFragmentView(Unit u){
            switch (sectionNumber){
                case 1: return basicInfoView(u);
                case 2: return attackView(u);
                case 3: return armorView(u);
                case 4: return upgradesView(u);
                case 5: return performanceView(u);
                case 6: return availabilityView(u);
                case 7: return bonusesView(u);
                default: return new View(getContext());
            }
        }

        private View getFragmentView(Building b){
            switch (sectionNumber){
                case 1: return basicInfoView(b);
                case 2: return attackView(b);
                case 3: return armorView(b);
                case 4: return upgradesView(b);
                case 5: return trainableView(b);
                case 6: return availabilityView(b);
                case 7: return bonusesView(b);
                default: return new View(getContext());
            }
        }

        private View getFragmentView(Technology t){
            switch (sectionNumber){
                case 1: return basicInfoView(t);
                case 2: return applicationsView(t);
                case 3: return availabilityView(t);
                case 4: return bonusesView(t);
                default: return new View(getContext());
            }
        }

        private View getFragmentLayout(){
            switch (entityType){
                case Database.UNIT: return inflater.inflate(R.layout.fragment_unit_info, container, false);
                case Database.BUILDING: return inflater.inflate(R.layout.fragment_building_info, container, false);
                default: return inflater.inflate(R.layout.fragment_tech_info, container, false);
            }
        }

        private boolean specialDarkAgeEntities(int id){
            if (Database.UNIT.equals(entityType)) {
                return id != 12 && id != 15;
            }
            return true;
        }

        private void setPanels(final View rootView, Entity entity){
            final EntityElement nameElement = entity.getNameElement();

            TextView name = rootView.findViewById(R.id.name);
            name.setText(nameElement.getName());

            ImageView icon = rootView.findViewById(R.id.icon);
            icon.setImageResource(nameElement.getImage());

            icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Utils.showPopupIcon(v, rootView.getContext(), nameElement.getName(), nameElement.getImage(), false, "blue");
                }
            });

            TextView t = rootView.findViewById(R.id.description);
            t.setText(entity.getDescriptor().getLongDescription());

            EntityButton creatorButton = rootView.findViewById(R.id.creator_button);
            creatorButton.setupButton(entity, true);

            EntityButton ageButton = rootView.findViewById(R.id.age_button);
            ageButton.setupButton(entity, true);

            EntityButton requiredTechButton = rootView.findViewById(R.id.required_tech);
            requiredTechButton.setupButton(entity, true);

            EntityButton nextUpgradeButton = rootView.findViewById(R.id.next_upgrade);
            nextUpgradeButton.setupButton(entity, true);

            if (entityType.equals(Database.UNIT) || entityType.equals(Database.BUILDING)) {
                GifImageView gif = rootView.findViewById(R.id.gif);
                gif.setImageResource(entity.getNameElement().getMedia());

                EntityButton classButton = rootView.findViewById(R.id.class_button);
                classButton.setupButton(entity, false);

                EntityButton previousUpgradeButton = rootView.findViewById(R.id.previous_upgrade);
                previousUpgradeButton.setupButton(entity, true);

                if (entityType.equals(Database.BUILDING)){
                    EntityButton requiredBuilding = rootView.findViewById(R.id.required_building);
                    requiredBuilding.setupButton(entity, true);
                }
            }

        }

        private void fixCivIDbyEntity(Entity entity){
            switch (entityType){
                case Database.UNIT:
                    if (civID == -1) {
                        if (entityID == 144) civID = 33; // elite kipchak -> cumans
                        else if (entityID == 84) civID = 14; // condottiero -> italians
                        else if (entityID == 30 || entityID == 89) civID = 2; //genitour -> berbers
                        else if (entityID == 86) civID = 30; // imp skirmisher -> vietnamese
                        else civID = entity.getAvailableCivIds().get(0); //get the first available civ
                    }
                    break;
                case Database.BUILDING:
                    if (civID == -1) civID = entity.getAvailableCivIds().get(0);
                    break;
                case Database.TECH:
                    if (civID == -1) {
                        if(entityID == 124) civID = 30; //imp skirmisher -> vietnamese
                        else if(entityID == 126) civID = 2; // elite genitour -> berbers
                        else civID = entity.getAvailableCivIds().get(0);
                    }
                    break;
            }
        }

        private View basicInfoView(final Entity entity){
            List <Integer> availableCivs = entity.getAvailableCivIds();
            final View rootView = getFragmentLayout();
            final HashMap<Integer, String> civRelation = Database.getCivNameMap();
            final List <String> civNames = new ArrayList<>();
            for(int i: availableCivs) civNames.add(civRelation.get(i));
            Collections.sort(civNames);
            ageID = Database.getSelectedAge(entityType, entityID);
            if (ageID == -1) ageID = Utils.convertAge(entity.getAgeElement().getName());
            Database.setSelectedAge(entityType, entityID, ageID);
            civID = Database.getSelectedCiv(entityType, entityID);
            fixCivIDbyEntity(entity);
            Database.setSelectedCiv(entityType, entityID, civID);
            upgradeList = entity.getUpgradesIds();
            AgeCivSelector selector = rootView.findViewById(R.id.selector);
            selector.setupCivSelector(civID, civNames);
            selector.setupUpgradeSelector(entity.getUpgradesIds());
            selector.setOnChangeListener(new AgeCivSelector.OnChangeListener() {
                @Override
                public void onAgeChanged(int age) {
                    ageID = age;
                    Database.setSelectedAge(entityType, entityID, ageID);
                    setStats(rootView, entity);
                }

                @Override
                public void onCivChanged(int civ) {
                    civID = civ;
                    Database.setSelectedCiv(entityType, entityID, civID);
                    setStats(rootView, entity);
                }

                @Override
                public void onUpgradesChanged(List<Integer> list) {
                    upgradeList = list;
                    setStats(rootView, entity);
                }
            });
            if (!entity.getAgeElement().getName().equals(getString(R.string.dark_age)) && specialDarkAgeEntities(entityID)) {
                if (entity.getAgeElement().getName().equals(getString(R.string.feudal_age))) selector.hideDarkAge();
                else if (entity.getAgeElement().getName().equals(getString(R.string.castle_age))) selector.hideFeudalAge();
                else if (entity.getAgeElement().getName().equals(getString(R.string.imperial_age))) selector.hideCastleAge();
            }
            selector.selectInitialAge(ageID);
            setPanels(rootView, entity);
            return rootView;
        }

        private View upgradesView(Entity entity){
            View rootView = inflater.inflate(R.layout.fragment_list, container, false);
            ExpandableListView elv = rootView.findViewById(R.id.element_list);
            LinkedHashMap<String, List<EntityElement>> list = entity.getUpgrades();
            final EntityAdapter adapter = new EntityAdapter(getContext(), list, entityID, entityType);
            elv.setAdapter(adapter);
            for (int i = 0; i < adapter.getGroupCount(); i++) elv.expandGroup(i);
            return rootView;
        }

        private View availabilityView(Entity entity){
            View rootView = inflater.inflate(R.layout.fragment_two_column, container, false);
            LinkedHashMap<String, List<EntityElement>> civList = entity.getCivAvailability();
            ListView list1 = rootView.findViewById(R.id.column_list1), list2 = rootView.findViewById(R.id.column_list2);
            TextView text1 = rootView.findViewById(R.id.column_header1), text2 = rootView.findViewById(R.id.column_header2);
            Iterator<Map.Entry<String, List<EntityElement>>> it = civList.entrySet().iterator();
            if (it.hasNext()){
                String title = it.next().getKey();
                List<EntityElement> list = civList.get(title);
                final TwoColumnAdapter adapter = new TwoColumnAdapter(rootView.getContext(), list);
                text1.setText(title);
                list1.setAdapter(adapter);
            }
            if (it.hasNext()){
                String title = it.next().getKey();
                List<EntityElement> list = civList.get(title);
                final TwoColumnAdapter adapter = new TwoColumnAdapter(rootView.getContext(), list);
                text2.setText(title);
                list2.setAdapter(adapter);
            }
            return rootView;
        }

        private View bonusesView(Entity entity){
            View rootView = inflater.inflate(R.layout.fragment_bonuses, container, false);
            List<String> bonuses = entity.writeBonuses();
            TextView cBonus = rootView.findViewById(R.id.civ_bonuses);
            TextView tBonus = rootView.findViewById(R.id.team_bonuses);
            TextView uTechs = rootView.findViewById(R.id.unique_techs);
            if (bonuses.size() > 0 && !bonuses.get(0).isEmpty()) {
                cBonus.setText(Html.fromHtml(bonuses.get(0), Html.FROM_HTML_MODE_LEGACY));
                LinearLayout l = rootView.findViewById(R.id.civ_bonuses_layout);
                l.setVisibility(View.VISIBLE);
            }
            if (bonuses.size() > 1 && !bonuses.get(1).isEmpty()) {
                tBonus.setText(Html.fromHtml(bonuses.get(1), Html.FROM_HTML_MODE_LEGACY));
                LinearLayout l = rootView.findViewById(R.id.team_bonuses_layout);
                l.setVisibility(View.VISIBLE);
            }
            if (bonuses.size() > 2 && !bonuses.get(2).isEmpty()){
                uTechs.setText(Html.fromHtml(bonuses.get(2), Html.FROM_HTML_MODE_LEGACY));
                LinearLayout l = rootView.findViewById(R.id.unique_techs_layout);
                l.setVisibility(View.VISIBLE);
            }
            return rootView;
        }

        private View attackView(final Item item){
            final View rootView = inflater.inflate(R.layout.fragment_list, container, false);
            final ExpandableListView elv = rootView.findViewById(R.id.element_list);
            Utils.setTypeValues(item.getAttackValues(), elv, rootView.getContext());
            item.setOnAttackChangedListener(new Item.OnTypeValueChanged() {
                @Override
                public void onChange() {
                    Utils.setTypeValues(item.getAttackValues(), elv, rootView.getContext());
                }
            });
            return rootView;
        }

        private View armorView(final Item item){
            final View rootView = inflater.inflate(R.layout.fragment_list, container, false);
            final ExpandableListView elv = rootView.findViewById(R.id.element_list);
            Utils.setTypeValues(item.getArmorValues(), elv, rootView.getContext());
            item.setOnAttackChangedListener(new Item.OnTypeValueChanged() {
                @Override
                public void onChange() {
                    Utils.setTypeValues(item.getArmorValues(), elv, rootView.getContext());
                }
            });
            return rootView;
        }

        private View performanceView(Unit unit){
            View rootView = inflater.inflate(R.layout.fragment_two_column, container, false);
            LinkedHashMap<String, List<EntityElement>> performance = unit.getPerformance();
            ListView list1 = rootView.findViewById(R.id.column_list1), list2 = rootView.findViewById(R.id.column_list2);
            TextView text1 = rootView.findViewById(R.id.column_header1), text2 = rootView.findViewById(R.id.column_header2);
            Iterator<Map.Entry<String, List<EntityElement>>> it = performance.entrySet().iterator();
            if (it.hasNext()) {
                String title = it.next().getKey();
                List<EntityElement> list = performance.get(title);
                final TwoColumnAdapter adapter = new TwoColumnAdapter(rootView.getContext(), list);
                text1.setText(title);
                list1.setAdapter(adapter);
            }
            if (it.hasNext()){
                String title = it.next().getKey();
                List<EntityElement> list = performance.get(title);
                final TwoColumnAdapter adapter = new TwoColumnAdapter(rootView.getContext(), list);
                text2.setText(title);
                list2.setAdapter(adapter);
            }
            return rootView;
        }

        private View trainableView(Building b){
            View rootView = inflater.inflate(R.layout.fragment_list, container, false);
            ExpandableListView elv = rootView.findViewById(R.id.element_list);
            LinkedHashMap<String, List<EntityElement>> list = b.getTrainable();
            final EntityAdapter adapter = new EntityAdapter(getContext(), list, entityID, entityType);
            elv.setAdapter(adapter);
            for(int i = 0; i < adapter.getGroupCount(); i++) elv.expandGroup(i);
            return rootView;
        }

        private View applicationsView(Technology tech){
            View rootView = inflater.inflate(R.layout.fragment_list, container, false);
            ExpandableListView elv = rootView.findViewById(R.id.element_list);
            LinkedHashMap<String, List<EntityElement>> list = tech.getApplications();
            final EntityAdapter adapter = new EntityAdapter(getContext(), list, entityID, entityType);
            elv.setAdapter(adapter);
            for(int i=0; i < adapter.getGroupCount(); i++) elv.expandGroup(i);
            return rootView;
        }

        private void setStats(View rootView, Entity entity){
            entity.calculateStats(ageID, civID, upgradeList);
            setCustomStats(rootView, entity);
            setCost(rootView, entity);
        }

        private void setCustomStats(View rootView, Entity entity){
            switch (entityType) {
                case Database.UNIT: {
                    TextView hp = rootView.findViewById(R.id.unit_hp);
                    TextView attack = rootView.findViewById(R.id.unit_attack);
                    TextView ma = rootView.findViewById(R.id.unit_melee_armor);
                    TextView minRange = rootView.findViewById(R.id.unit_min_range);
                    TextView pa = rootView.findViewById(R.id.unit_pierce_armor);
                    TextView range = rootView.findViewById(R.id.unit_range);
                    TextView los = rootView.findViewById(R.id.unit_los);
                    TextView rt = rootView.findViewById(R.id.unit_reload_time);
                    TextView speed = rootView.findViewById(R.id.unit_speed);
                    TextView acc = rootView.findViewById(R.id.unit_accuracy);
                    TextView gc = rootView.findViewById(R.id.unit_garrison_capacity);
                    TextView br = rootView.findViewById(R.id.unit_blast_radius);
                    TextView ad = rootView.findViewById(R.id.unit_attack_delay);
                    TextView np = rootView.findViewById(R.id.unit_projectiles);
                    TextView ps = rootView.findViewById(R.id.unit_projectile_speed);
                    TextView ph = rootView.findViewById(R.id.unit_population_headroom);
                    TextView wr = rootView.findViewById(R.id.unit_work_rate);
                    TextView hr = rootView.findViewById(R.id.unit_healing_rate);
                    TextView tt = rootView.findViewById(R.id.unit_training_time);


                    hp.setText(entity.getStatString(Database.HP));
                    attack.setText(entity.getStatString(Database.ATTACK));
                    ma.setText(entity.getStatString(Database.MELEE_ARMOR));
                    pa.setText(entity.getStatString(Database.PIERCE_ARMOR));
                    range.setText(entity.getStatString(Database.RANGE));
                    minRange.setText(entity.getStatString(Database.MINIMUM_RANGE));
                    speed.setText(entity.getStatString(Database.SPEED));
                    br.setText(entity.getStatString(Database.BLAST_RADIUS));
                    los.setText(entity.getStatString(Database.LOS));
                    rt.setText(entity.getStatString(Database.RELOAD_TIME));
                    ad.setText(entity.getStatString(Database.ATTACK_DELAY));
                    acc.setText(entity.getStatString(Database.ACCURACY));
                    np.setText(entity.getStatString(Database.NUMBER_PROJECTILES));
                    ps.setText(entity.getStatString(Database.PROJECTILE_SPEED));
                    gc.setText(entity.getStatString(Database.GARRISON_CAPACITY));
                    ph.setText(entity.getStatString(Database.POPULATION_TAKEN));
                    wr.setText(entity.getStatString(Database.WORK_RATE));
                    hr.setText(entity.getStatString(Database.HEAL_RATE));
                    tt.setText(entity.getStatString(Database.TRAINING_TIME));
                    break;
                }
                case Database.BUILDING: {
                    TextView hp = rootView.findViewById(R.id.building_hp);
                    TextView attack = rootView.findViewById(R.id.building_attack);
                    TextView ma = rootView.findViewById(R.id.building_melee_armor);
                    TextView pa = rootView.findViewById(R.id.building_pierce_armor);
                    TextView range = rootView.findViewById(R.id.building_range);
                    TextView minRange = rootView.findViewById(R.id.building_min_range);
                    TextView los = rootView.findViewById(R.id.building_los);
                    TextView rt = rootView.findViewById(R.id.building_reload_time);
                    TextView ad = rootView.findViewById(R.id.building_attack_delay);
                    TextView acc = rootView.findViewById(R.id.building_accuracy);
                    TextView np = rootView.findViewById(R.id.building_projectiles);
                    TextView ps = rootView.findViewById(R.id.building_projectile_speed);
                    TextView gc = rootView.findViewById(R.id.building_garrison_capacity);
                    TextView ph = rootView.findViewById(R.id.building_population_headroom);
                    TextView wr = rootView.findViewById(R.id.building_working_rate);
                    TextView hr = rootView.findViewById(R.id.building_healing_rate);
                    TextView tt = rootView.findViewById(R.id.building_building_time);

                    hp.setText(entity.getStatString(Database.HP));
                    attack.setText(entity.getStatString(Database.ATTACK));
                    ma.setText(entity.getStatString(Database.MELEE_ARMOR));
                    pa.setText(entity.getStatString(Database.PIERCE_ARMOR));
                    range.setText(entity.getStatString(Database.RANGE));
                    minRange.setText(entity.getStatString(Database.MINIMUM_RANGE));
                    los.setText(entity.getStatString(Database.LOS));
                    rt.setText(entity.getStatString(Database.RELOAD_TIME));
                    ad.setText(entity.getStatString(Database.ATTACK_DELAY));
                    acc.setText(entity.getStatString(Database.ACCURACY));
                    np.setText(entity.getStatString(Database.NUMBER_PROJECTILES));
                    ps.setText(entity.getStatString(Database.PROJECTILE_SPEED));
                    gc.setText(entity.getStatString(Database.GARRISON_CAPACITY));
                    ph.setText(entity.getStatString(Database.POPULATION_TAKEN));
                    wr.setText(entity.getStatString(Database.WORK_RATE));
                    hr.setText(entity.getStatString(Database.HEAL_RATE));
                    tt.setText(entity.getStatString(Database.TRAINING_TIME));
                    break;
                }
                case Database.TECH: {
                    TextView tt = rootView.findViewById(R.id.tech_training_time);
                    tt.setText(entity.getStatString(Database.TRAINING_TIME));
                    break;
                }
            }
        }

        private void setCost(View rootView, Entity entity){
            HashMap<String, Integer> cost = entity.getCalculatedCost();
            ImageView res1 = rootView.findViewById(R.id.res1);
            TextView res1v = rootView.findViewById(R.id.res1_amount);
            ImageView res2 = rootView.findViewById(R.id.res2);
            TextView res2v = rootView.findViewById(R.id.res2_amount);
            Iterator<Map.Entry<String, Integer>> it = cost.entrySet().iterator();
            if (it.hasNext()) {
                String res = it.next().getKey();
                res1.setImageResource(Utils.getResourceIcon(res));
                res1v.setText(entity.getCostString().get(res));
            }
            else{
                res1.setImageDrawable(null);
                res1v.setText("");
                res2.setImageDrawable(null);
                res2v.setText("");
            }
            if (it.hasNext()) {
                String res = it.next().getKey();
                res2.setImageResource(Utils.getResourceIcon(res));
                res2v.setText(entity.getCostString().get(res));
            }
            else {
                res2.setImageDrawable(null);
                res2v.setText("");
            }
            if (it.hasNext()){
                ImageView res3 = rootView.findViewById(R.id.res3);
                TextView res3v = rootView.findViewById(R.id.res3_amount);
                String res = it.next().getKey();
                res3.setImageResource(Utils.getResourceIcon(res));
                res3v.setText(entity.getCostString().get(res));
                res3.setVisibility(View.VISIBLE);
                res3v.setVisibility(View.VISIBLE);
            }
        }



    }

    public class SectionsPagerAdapter extends FragmentStateAdapter {
        private final String[] tabTitles;
        private final HashMap<Integer, Fragment> viewMap;

        int entityID;
        String entityType;

        public SectionsPagerAdapter(FragmentActivity fa, int entityID, String entityType){
            super(fa);
            viewMap = new HashMap<>();
            this.entityID = entityID;
            this.entityType = entityType;
            tabTitles = getTabTitles();
        }

        public CharSequence getPageTitle(int position) {
            return tabTitles[position];
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            if (viewMap.containsKey(position)) return viewMap.get(position);
            else {
                Fragment f = EntityFragment.newInstance(position + 1, entityID, entityType);
                viewMap.put(position, f);
                return f;
            }
        }

        @Override
        public int getItemCount() {
            return tabTitles.length;
        }
    }

}
