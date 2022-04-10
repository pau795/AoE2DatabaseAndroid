package com.aoedb.activities;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatAutoCompleteTextView;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.SearchView;

import com.aoedb.R;
import com.aoedb.adapters.EntityAdapter;
import com.aoedb.adapters.TextFilterAdapter;
import com.aoedb.data.EntityElement;
import com.aoedb.database.Database;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class AdvancedSearchActivity extends DrawerActivity {

    Menu menu;
    ExpandableListView elv;
    ScrollView scrollLayout;
    EntityAdapter adapter;
    List<EntityElement> resultsList;
    LinkedHashMap<String, List<EntityElement>> dataModels;
    HashMap<Integer, TextView> buttonMap;
    int selectedButton;
    LinearLayout conditionLayout, addLayout;
    TextView clearButton, searchButton, addButton;
    SearchFilter searchFilter;
    Context c;

    private static final int NONE = 0;
    private static final int UNIT = 1;
    private static final int BUILDING = 2;
    private static final int TECH = 3;
    private static final int CIV = 4;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.title_advanced_search_activity);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_advanced_search, null, false);
        setActivityView(contentView);
        setAppBarColor(getColor(R.color.blue_background));
        c = this;
        scrollLayout = findViewById(R.id.main_layout);
        elv = findViewById(R.id.advanced_search_list);
        initButtons();
    }

    private void initButtons(){
        clearButton = findViewById(R.id.as_clear_button);
        searchButton = findViewById(R.id.as_search_button);
        addButton = findViewById(R.id.as_add_condition);
        conditionLayout = findViewById(R.id.condition_layout);
        addLayout = findViewById(R.id.as_add_layout);
        searchFilter = new SearchFilter(this, conditionLayout);
        TextView unitButton = findViewById(R.id.as_units_button);
        TextView buildingButton = findViewById(R.id.as_buildings_button);
        TextView techsButton = findViewById(R.id.as_techs_button);
        TextView civsButton = findViewById(R.id.as_civs_button);
        unitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleButton(1);
            }
        });
        buildingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleButton(2);
            }
        });
        techsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleButton(3);
            }
        });
        civsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleButton(4);
            }
        });
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearSearch();
            }
        });
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSearch();
            }
        });
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCondition();
            }
        });
        buttonMap = new HashMap<>();
        buttonMap.put(1, unitButton);
        buttonMap.put(2, buildingButton);
        buttonMap.put(3, techsButton);
        buttonMap.put(4, civsButton);
        selectedButton = 0;
    }

    private void addCondition() {
        searchFilter.addCondition();
    }

    private void clearConditions(){
        searchFilter.clearConditions();
    }

    private void handleButton(int button){
        if (selectedButton == button){
            buttonMap.get(button).setSelected(false);
            selectedButton = NONE;
            clearConditions();
            searchFilter.setSelection(selectedButton);
            addLayout.setVisibility(View.GONE);
        }
        else {
            if (selectedButton != NONE) buttonMap.get(selectedButton).setSelected(false);
            buttonMap.get(button).setSelected(true);
            selectedButton = button;
            clearConditions();
            addLayout.setVisibility(View.VISIBLE);
            searchFilter.setSelection(selectedButton);
            searchFilter.addCondition();
        }

    }

    private List<EntityElement> performFilter(List<EntityElement> list){
        List<EntityElement> filteredList = new ArrayList<>();
        for (EntityElement e:list) if (searchFilter.processElement(e.getId())) filteredList.add(e);
        return filteredList;
    }

    private List<EntityElement> setupList(){
        switch (selectedButton){
            case UNIT: return Database.getList(Database.UNIT_LIST);
            case BUILDING: return Database.getList(Database.BUILDING_LIST);
            case TECH: {
                List<EntityElement> l = Database.getList(Database.TECH_LIST);
                l.remove(8);
                l.remove(96);
                return l;
            }
            case CIV: return Database.getList(Database.CIVILIZATION_LIST);
            default: return new ArrayList<>();
        }
    }

    private void sortResults(){
        String file;
        switch (selectedButton){
            case UNIT:
                file = Database.UNIT_GROUPS;
                break;
            case BUILDING:
                file = Database.BUILDING_GROUPS;
                break;
            case TECH:
                file = Database.TECH_GROUPS;
                break;
            case CIV:
                file = Database.CIVILIZATION_GROUPS;
                break;
            default:
                file = "";
                break;
        }
        if (!file.equals("")) {
            Comparator<EntityElement> comp = EntityElement.getListElementComparator(Database.getOrderMap(file, 0));
            Collections.sort(resultsList, comp);
        }
    }

    private void performSearch(){
        List<EntityElement> baseList = setupList();
        resultsList = performFilter(baseList);
        sortResults();
        dataModels = new LinkedHashMap<>();
        dataModels.put(String.format(getString(R.string.as_results), resultsList.size()), resultsList);
        adapter = new EntityAdapter(this, dataModels);
        elv.setAdapter(adapter);
        elv.expandGroup(0);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        this.menu = menu;
        return true;
    }

    private void clearSearch(){
        if (selectedButton != NONE) buttonMap.get(selectedButton).setSelected(false);
        clearConditions();
        addLayout.setVisibility(View.GONE);
        selectedButton = NONE;
    }

    private void startSearch(){
        performSearch();
        menu.findItem(R.id.action_search).setVisible(true);
        scrollLayout.setVisibility(View.GONE);
        elv.setVisibility(View.VISIBLE);
        menu.performIdentifierAction(R.id.action_search, 0);
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            manager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_search) {
            final SearchView searchView = (SearchView) item.getActionView();
            searchView.setQueryHint(getString(R.string.search_hint));
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String s) {
                    scrollLayout.setVisibility(View.GONE);
                    adapter = new EntityAdapter(c, dataModels, s);
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
                    scrollLayout.setVisibility(View.GONE);
                    elv.setVisibility(View.VISIBLE);
                    return true;
                }

                @Override
                public boolean onMenuItemActionCollapse(MenuItem item) {
                    searchView.setQuery("", false);
                    elv.setVisibility(View.GONE);
                    scrollLayout.setVisibility(View.VISIBLE);
                    invalidateOptionsMenu();
                    return true;
                }
            });
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.action_search).setVisible(false);
        return true;
    }

    private class SearchFilter{

        int selection;
        List<GeneralCondition> conditions;
        LinearLayout mainLayout;
        int numConditions;
        Context c;
        public SearchFilter(Context c, LinearLayout layout){
            conditions = new ArrayList<>();
            this.mainLayout = layout;
            this.c = c;
            numConditions = 0;
        }

        public void addCondition(){
            GeneralCondition cond;
            ++numConditions;
            switch (selection){
                case UNIT:
                    cond = new UnitCondition(c, numConditions);
                    break;
                case BUILDING:
                    cond = new BuildingCondition(c, numConditions);
                    break;
                case TECH:
                    cond = new TechnologyCondition(c,numConditions);
                    break;
                case CIV:
                    cond = new CivilizationCondition(c, numConditions);
                    break;
                default:
                    cond = null;
                    break;
            }
            conditions.add(cond);
            mainLayout.addView(cond.getLayout());

        }

        public void setSelection(int n){
            selection = n;
        }

        public void deleteCondition(int position){
            conditions.remove(position);
            mainLayout.removeViewAt(position);
            --numConditions;
        }

        public void clearConditions(){
            mainLayout.removeAllViews();
            conditions.clear();
            selection = NONE;
            numConditions = 0;
        }

        public boolean processElement(int id){
            int position = 0;
            return recursiveProcessing(position, id);
        }

        private boolean recursiveProcessing(int position, int id){
            if (position < conditions.size()){
                GeneralCondition cond = conditions.get(position);
                boolean b = cond.processCondition(id);
                if (cond.isNegated()) b = !b;
                if (position + 1 < conditions.size()){
                    if (conditions.get(position+1).getConnector()) return (b && recursiveProcessing(position+1, id)) ; //AND connector
                    else return (b || recursiveProcessing(position+1, id)) ; // OR connector
                }
                else return b;  //base recursive case, end of the list
            }
            return false; //unreachable
        }

        private abstract class GeneralCondition {
            protected boolean negate;
            protected boolean hasConnector;
            protected boolean orAnd;  //false is or, true is and;
            protected int conditionPosition;
            protected int spinnerPosition;
            protected LinearLayout conditionLayout;
            protected List<String> spinnerList;
            protected Context c;

            ConditionType conditionType;

            public GeneralCondition(Context c, int pos) {
                this.c = c;
                conditionPosition = pos;
                setupLayout();
                customConditionList();
                setupButtons();


            }

            private void setupButtons() {
                AppCompatCheckBox negateBox = conditionLayout.findViewById(R.id.as_negation_checkbox);
                negateBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        negate = isChecked;
                    }
                });

                AppCompatSpinner spinner = conditionLayout.findViewById(R.id.condition_spinner);
                ArrayAdapter<String> adapter = new ArrayAdapter<>(c, android.R.layout.simple_spinner_dropdown_item, spinnerList);
                spinner.setAdapter(adapter);
                spinnerPosition = 0;
                spinner.setSelection(spinnerPosition);
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        spinnerPosition = position;
                        conditionType = setConditionType(spinnerPosition);
                        conditionType.setupLayout();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
                if (hasConnector) {
                    final TextView connector = conditionLayout.findViewById(R.id.as_connector);
                    LinearLayout deleteButton = conditionLayout.findViewById(R.id.as_delete_button);
                    connector.setText(R.string.as_row_and);
                    orAnd = true;
                    connector.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (orAnd) {
                                connector.setText(R.string.as_row_or);
                                orAnd = false;
                            } else {
                                connector.setText(R.string.as_row_and);
                                orAnd = true;
                            }
                        }
                    });

                    deleteButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            int position = ((ViewGroup) conditionLayout.getParent()).indexOfChild(conditionLayout);
                            deleteCondition(position);
                        }
                    });
                }
            }


            public boolean processCondition(int id) {
                return conditionType.processCondition(id);
            }

            public boolean isNegated() {
                return negate;
            }

            public boolean getConnector() {
                return orAnd;
            }

            protected void setupLayout() {
                LayoutInflater inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View mainLayout = inflater.inflate(R.layout.advanced_search_condition, null);
                View connectorLayout = inflater.inflate(R.layout.advanced_search_connector, null);

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                conditionLayout = new LinearLayout(c);
                conditionLayout.setOrientation(LinearLayout.VERTICAL);
                conditionLayout.setLayoutParams(params);
                if (conditionPosition != 1) {
                    conditionLayout.addView(connectorLayout);
                    hasConnector = true;
                } else hasConnector = false;
                conditionLayout.addView(mainLayout);
            }

            protected abstract void customConditionList();

            protected abstract ConditionType setConditionType(int position);

            public View getLayout() {
                return conditionLayout;
            }
        }

        private class UnitCondition extends GeneralCondition{

            public UnitCondition(Context c, int pos) {
                super(c, pos);
            }

            protected void customConditionList(){
                spinnerList = Collections.singletonList(getString(R.string.none));
                /*
                spinnerList = Arrays.asList(getString(R.string.none), getString(R.string.as_class), getString(R.string.as_creation_building),
                getString(R.string.as_affected_upgrades), getString(R.string.as_available_civilization),
                getString(R.string.as_stats), getString(R.string.as_attack_value), getString(R.string.as_armor_value));

                 */
            }

            @Override
            protected ConditionType setConditionType(int position){
                return new NullCondition(getLayout());
            }

            /*
            @Override
            protected void setCustomButtons() {
                switch(spinnerPosition){
                    case 1:
                        LinearLayout spinnerLayout = conditionLayout.findViewById(R.id.spinner_layout);
                        spinnerLayout.setVisibility(View.VISIBLE);
                        TextView spinnerName = conditionLayout.findViewById(R.id.spinner_name_text);
                        spinnerName.setText(R.string.as_class_name);
                        List<ListElement> classes = Utils.readList(Database.CLASS_LIST, c);
                        final List<String> classList =  new ArrayList<>();
                        for(ListElement l : classes) classList.add(l.getName());
                        AppCompatSpinner classSpinner = findViewById(R.id.name_spinner);
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(c, android.R.layout.simple_spinner_dropdown_item, classList);
                        classSpinner.setAdapter(adapter);

                        classSpinner.setSelection(spinnerPosition);
                        classSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                className = classList.get(position);
                            }
                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {
                            }
                        });
                        break;
                    case 2:
                        break;
                    case 4:
                        break;
                    case 5:
                        break;
                    case 6:
                        break;
                    case 7:
                        break;
                    default:
                        LinearLayout selectorLayout = conditionLayout.findViewById(R.id.selector_layout);
                        selectorLayout.setVisibility(View.INVISIBLE);
                        break;
                }
            }
            */

        }
        private class BuildingCondition extends GeneralCondition{

            public BuildingCondition(Context c, int pos) {
                super(c, pos);
            }

            protected void customConditionList(){
                spinnerList = Collections.singletonList(getString(R.string.none));
            }

            @Override
            protected ConditionType setConditionType(int position){
                return new NullCondition(getLayout());
            }


        }
        private class TechnologyCondition extends GeneralCondition{

            public TechnologyCondition(Context c, int pos) {
                super(c, pos);
            }

            protected void customConditionList(){
                spinnerList = Collections.singletonList(getString(R.string.none));
            }

            @Override
            protected ConditionType setConditionType(int position){
                return new NullCondition(getLayout());
            }

        }
        private class CivilizationCondition extends GeneralCondition{

            public CivilizationCondition(Context c, int pos) {
                super(c, pos);
            }

            protected void customConditionList(){

                spinnerList = Arrays.asList(getString(R.string.none),getString(R.string.as_access_unit),
                        getString(R.string.as_access_building), getString(R.string.as_access_tech));
            }

            @Override
            protected ConditionType setConditionType(int position){
                switch (position){
                    case 1:
                    case 2:
                    case 3:
                        return new CivilizationAvailabilityCondition(getLayout(), position);
                    default: return new NullCondition(getLayout());
                }
            }
        }

        private abstract class ConditionType{

            View conditionLayout;
            public ConditionType(View view){
                this.conditionLayout = view;
            }

            protected abstract void setupLayout();

            protected abstract boolean processCondition(int id);

        }
        private class NullCondition extends ConditionType{


            public NullCondition(View view) {
                super(view);
            }

            @Override
            protected void setupLayout() {
                LinearLayout nameLayout = conditionLayout.findViewById(R.id.selector_layout);
                LinearLayout spinnerLayout = conditionLayout.findViewById(R.id.spinner_layout);
                LinearLayout secondRow = conditionLayout.findViewById(R.id.stat_layout);
                nameLayout.setVisibility(View.INVISIBLE);
                spinnerLayout.setVisibility(View.GONE);
                secondRow.setVisibility(View.GONE);
            }

            @Override
            protected boolean processCondition(int id) {
                return true;
            }
        }
        private class CivilizationAvailabilityCondition extends ConditionType{
            int entityID;
            int spinnerPosition;

            public CivilizationAvailabilityCondition(View view, int spinnerPosition) {
                super(view);
                this.spinnerPosition = spinnerPosition;
            }

            @Override
            protected void setupLayout() {
                LinearLayout selectorLayout = conditionLayout.findViewById(R.id.selector_layout);
                TextView selectorName =  conditionLayout.findViewById(R.id.as_row_name_text);
                LinearLayout spinnerLayout = conditionLayout.findViewById(R.id.spinner_layout);
                LinearLayout secondRow = conditionLayout.findViewById(R.id.stat_layout);
                selectorLayout.setVisibility(View.VISIBLE);
                spinnerLayout.setVisibility(View.GONE);
                secondRow.setVisibility(View.GONE);
                final AppCompatAutoCompleteTextView unitSelector = conditionLayout.findViewById(R.id.unit_selector);
                List<String> entityNames =  new ArrayList<>();
                final HashMap<String, Integer> entityRelation =  new HashMap<>();
                List<EntityElement> entityList;
                switch (spinnerPosition){
                    case 1:
                        selectorName.setText(R.string.as_unit);
                        entityList = Database.getList(Database.UNIT_LIST);
                        break;
                    case 2:
                        selectorName.setText(R.string.as_building);
                        entityList = Database.getList(Database.BUILDING_LIST);
                        break;
                    case 3:
                        selectorName.setText(R.string.as_tech);
                        entityList = Database.getList(Database.TECH_LIST);
                        break;
                    default:
                        entityList = new ArrayList<>();
                        break;

                }
                for (EntityElement e : entityList){
                    entityNames.add(e.getName());
                    entityRelation.put(e.getName(), e.getId());
                }
                TextFilterAdapter adapter = new TextFilterAdapter(c, R.layout.autocomplete_text_layout, new ArrayList<>(entityNames));
                unitSelector.setThreshold(0);
                unitSelector.setAdapter(adapter);
                unitSelector.setText("");
                unitSelector.setHint("");
                entityID = 0;
                unitSelector.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                        InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        in.hideSoftInputFromWindow(arg1.getApplicationWindowToken(), 0);
                        String name = (String) arg0.getItemAtPosition(arg2);
                        unitSelector.setText("");
                        unitSelector.setHint(name);
                        entityID = entityRelation.get(name);
                    }
                });
            }

            @Override
            protected boolean processCondition(int id) {
                switch (spinnerPosition){
                    case 1:
                        return Database.getUnit(entityID).isAvailableTo(id);
                    case 2:
                        return Database.getBuilding(entityID).isAvailableTo(id);
                    case 3:
                        return Database.getTechnology(entityID).isAvailableTo(id);
                    default:
                        return true;
                }
            }
        }

    }
}