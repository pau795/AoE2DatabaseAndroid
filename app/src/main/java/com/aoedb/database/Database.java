package com.aoedb.database;

import android.app.AlertDialog;
import android.content.Context;

import com.aoedb.R;
import com.aoedb.data.Bonus;
import com.aoedb.data.Building;
import com.aoedb.data.Civilization;
import com.aoedb.data.EcoElement;
import com.aoedb.data.EntityElement;
import com.aoedb.data.ScoreList;
import com.aoedb.data.TauntElement;
import com.aoedb.data.TechBonus;
import com.aoedb.data.Technology;
import com.aoedb.data.Unit;
import com.aoedb.data.UpgradeElement;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class Database {

    //DATABASE CONSTANTS
    public final static String APP_VERSION = "v2.5";
    public final static int PATCH_VERSION = 93001;

    //LANGUAGES
    public final static String SPANISH = "es";
    public final static String ENGLISH = "en";
    public final static String DEUTSCH = "de";
    public final static String DEFAULT_LANGUAGE = ENGLISH;

    public final static String ENGLISH_FLAG = "\uD83C\uDDEC\uD83C\uDDE7 English";
    public final static String SPANISH_FLAG = "\uD83C\uDDEA\uD83C\uDDF8 Español";
    public final static String DEUTSCH_FLAG = "\uD83C\uDDE9\uD83C\uDDEA Deutsch";
    public final static String DEFAULT_FLAG = ENGLISH_FLAG;

    //TYPES
    public final static String ENTITY = "entity";
    public final static String UNIT = "unit";
    public final static String BUILDING = "building";
    public final static String TECH = "tech";
    public final static String CIV = "civilization";
    public final static String CLASS = "class";
    public final static String TYPE = "type";
    public final static String PERFORMANCE = "performance";
    public final static String HISTORY = "history";
    public final static String EMPTY = "";
    public final static String NONE = "none";

    //RESOURCES
    public final static String WOOD = "Wood";
    public final static String FOOD = "Food";
    public final static String GOLD = "Gold";
    public final static String STONE = "Stone";

    //FILES
    public final static String BONUS_EFFECT = "bonus_effect";
    public final static String BONUS_LIST = "bonus_list";
    public final static String BUILDING_ARMOR = "building_armor";
    public final static String BUILDING_ATTACK = "building_attack";
    public final static String BUILDING_AVAILABILITY = "building_availability";
    public final static String BUILDING_BONUS = "building_bonus";
    public final static String BUILDING_GROUPS = "building_groups";
    public final static String BUILDING_LIST = "building_list";
    public final static String BUILDING_STATS = "building_stats";
    public final static String BUILDING_TRAINABLE = "building_trainable";
    public final static String BUILDING_UPGRADES = "building_upgrades";
    public final static String CIVILIZATION_GROUPS = "civilization_groups";
    public final static String CIVILIZATION_INFO = "civilization_info";
    public final static String CIVILIZATION_LIST = "civilization_list";
    public final static String CLASS_LIST = "class_list";
    public final static String ECO_LIST = "eco_list";
    public final static String ECO_UPGRADES = "eco_upgrades";
    public final static String GATHERING_RATES = "gathering_rates";
    public final static String HIDDEN_BONUS = "hidden_bonus";
    public final static String HIDDEN_BONUS_EFFECT = "hidden_bonus_effect";
    public final static String HISTORY_GROUPS = "history_groups";
    public final static String HISTORY_LIST = "history_list";
    public final static String HISTORY_TEXT = "history_text";
    public final static String PERFORMANCE_GROUPS = "performance_groups";
    public final static String PERFORMANCE_LIST = "performance_list";
    public final static String STAT_LIST = "stat_list";
    public final static String TAUNT_LIST = "taunt_list";
    public final static String TECH_AVAILABILITY = "tech_availability";
    public final static String TECH_BONUS = "tech_bonus";
    public final static String TECH_EFFECT = "tech_effect";
    public final static String TECH_GROUPS = "tech_groups";
    public final static String TECH_LIST = "tech_list";
    public final static String TECH_STATS = "tech_stats";
    public final static String TECH_TREE_QUIZ = "tech_tree_quiz";
    public final static String TECH_UPGRADES = "tech_upgrades";
    public final static String TYPE_LIST = "type_list";
    public final static String UNIT_ARMOR = "unit_armor";
    public final static String UNIT_ATTACK = "unit_attack";
    public final static String UNIT_AVAILABILITY = "unit_availability";
    public final static String UNIT_BONUS = "unit_bonus";
    public final static String UNIT_GROUPS = "unit_groups";
    public final static String UNIT_LIST = "unit_list";
    public final static String UNIT_PERFORMANCE = "unit_performance";
    public final static String UNIT_STATS = "unit_stats";
    public final static String UNIT_UPGRADES = "unit_upgrades";


    //STATS
    public final static String HP = "Hit Points";
    public final static String ATTACK = "Attack";
    public final static String MELEE_ARMOR = "Melee Armor";
    public final static String PIERCE_ARMOR = "Pierce Armor";
    public final static String RANGE = "Range";
    public final static String MINIMUM_RANGE = "Minimum Range";
    public final static String LOS = "Line Of Sight";
    public final static String RELOAD_TIME = "Reload Time";
    public final static String SPEED = "Speed";
    public final static String BLAST_RADIUS = "Blast Radius";
    public final static String ACCURACY = "Accuracy";
    public final static String ATTACK_DELAY = "Attack Delay";
    public final static String NUMBER_PROJECTILES = "Number Projectiles";
    public final static String PROJECTILE_SPEED = "Projectile Speed";
    public final static String GARRISON_CAPACITY = "Garrison Capacity";
    public final static String POPULATION_TAKEN = "Population Taken";
    public final static String TRAINING_TIME = "Training Time";
    public final static String WORK_RATE = "Work Rate";
    public final static String HEAL_RATE = "Heal Rate";
    public final static String HILL_BONUS = "Hill Bonus";
    public final static String HILL_REDUCTION = "Hill Reduction";
    public final static String BONUS_REDUCTION = "Bonus Reduction";
    public final static String CHARGE_ATTACK = "Charge Attack";
    public final static String CHARGE_RELOAD = "Charge Reload";
    public final static String RELICS = "Relics";
    public final static String IGNORE_ARMOR = "Ignore Armor";
    public final static String RESIST_ARMOR_IGNORE = "Resist Armor Ignore";

    //ECO STATS
    public final static String LUMBERJACK = "Lumberjack";
    public final static String SHEPHERD = "Shepherd";
    public final static String FORAGER = "Forager";
    public final static String HUNTER = "Hunter";
    public final static String FISHERMAN = "Fisherman";
    public final static String FARMER = "Farmer";
    public final static String BUILDER = "Builder";
    public final static String REPAIRER = "Repairer";
    public final static String GOLD_MINER = "Gold Miner";
    public final static String STONE_MINER = "Stone Miner";
    public final static String FISHING_SHIP = "Fishing Ship";
    public final static String RELIC_GOLD = "Relic Gold";
    public final static String TRADE_CART = "Trade Cart";
    public final static String TRADE_COG = "Trade Cog";
    public final static String FEITORIA_WOOD = "Feitoria FWood";
    public final static String FEITORIA_FOOD = "Feitoria Food";
    public final static String FEITORIA_GOLD = "Feitoria Gold";
    public final static String FEITORIA_STONE = "Feitoria Stone";
    public final static String KESHIK = "Keshik";
    public final static String FARMING_GOLD = "Farming Gold";
    public final static String RELIC_FOOD = "Relic Food";
    public final static String GOLD_STONE_MINERS = "Gold Stone Miners";
    public final static String GOLD_LUMBERJACKS = "Gold Lumberjacks";


    //COLORS
    public final static String RED = "red";
    public final static String BLUE = "blue";
    public final static String GREEN = "green";


    //LISTS AND MAPS
    private static List<EntityElement> unitList;
    private static List<EntityElement> buildingList;
    private static List<EntityElement> techList;
    private static List<EntityElement> civilizationList;
    private static List<EntityElement> classList;
    private static List<EntityElement> performanceList;
    private static List<EntityElement> typeList;
    private static List<EntityElement> historyList;

    private static List<TauntElement> tauntList;
    private static HashMap<Integer, String> statList;
    private static HashMap<String, Boolean> statAddition;
    private static HashMap<Integer, String> ecoList;
    private static HashMap<String, Double> ecoValues;
    private static List<EcoElement> gatheringRates;
    private static List<Integer> ecoUpgrades;
    private static LinkedHashMap<String, List<Integer>> techTreeQuizQuestions;

    private static List<HashMap<Integer, Integer>> unitOrder;
    private static List<HashMap<Integer, Integer>> buildingOrder;
    private static List<HashMap<Integer, Integer>> techOrder;
    private static List<HashMap<Integer, Integer>> civilizationOrder;
    private static List<HashMap<Integer, Integer>> performanceOrder;

    private static List<LinkedHashMap<String, List<EntityElement>>> unitGroups;
    private static List<LinkedHashMap<String, List<EntityElement>>> buildingGroups;
    private static List<LinkedHashMap<String, List<EntityElement>>> techGroups;
    private static List<LinkedHashMap<String, List<EntityElement>>> civilizationGroups;
    private static List<LinkedHashMap<String, List<EntityElement>>> performanceGroups;
    private static List<LinkedHashMap<String, List<EntityElement>>> historyGroups;

    private static HashMap<Integer, String> civNameMap;
    private static HashMap<String, Integer> reversedCivNameMap;

    private static List<String> historyText;

    private static List<Unit> unitMap;
    private static List<Building> buildingMap;
    private static List<Technology> techMap;
    private static List<Civilization> civilizationMap;

    private static List<Bonus> bonusMap;
    private static List<Bonus> hiddenBonusMap;
    private static List<TechBonus> techBonusMap;

    private static List<Integer> unitSelectedCiv;
    private static List<Integer> buildingSelectedCiv;
    private static List<Integer> techSelectedCiv;

    private static List<Integer> unitSelectedAge;
    private static List<Integer> buildingSelectedAge;
    private static List<Integer> techSelectedAge;



    private static Context c;


    public static void loadDatabase(Context ctx){

        c = ctx;
        Reader r = new Reader(c);

        unitList = r.readList(UNIT_LIST);
        buildingList = r.readList(Database.BUILDING_LIST);
        techList = r.readList(Database.TECH_LIST);
        civilizationList = r.readList(Database.CIVILIZATION_LIST);
        classList = r.readList(Database.CLASS_LIST);
        performanceList = r.readList(Database.PERFORMANCE_LIST);
        typeList = r.readList(Database.TYPE_LIST);
        historyList = r.readList(Database.HISTORY_LIST);

        tauntList = r.readTaunts();
        statList = r.readStatsNames(Database.STAT_LIST);
        statAddition = r.readStatsAddition();
        ecoList = r.readStatsNames(Database.ECO_LIST);
        ecoValues = r.readEcoValues();
        gatheringRates = r.readGatheringRates();
        ecoUpgrades = r.readEcoUpgrades();
        techTreeQuizQuestions = r.readTechTreeQuestions();
        civNameMap = r.makeCivMap();
        reversedCivNameMap = r.makeReversedCivMap();

        unitOrder = r.sortIndexMap(Database.UNIT_GROUPS);
        buildingOrder = r.sortIndexMap(Database.BUILDING_GROUPS);
        techOrder = r.sortIndexMap(Database.TECH_GROUPS);
        civilizationOrder = r.sortIndexMap(Database.CIVILIZATION_GROUPS);
        performanceOrder = r.sortIndexMap(Database.PERFORMANCE_GROUPS);

        unitGroups = r.readGroupLists(Database.UNIT_GROUPS, unitList);
        buildingGroups = r.readGroupLists(Database.BUILDING_GROUPS, buildingList);
        techGroups = r.readGroupLists(Database.TECH_GROUPS, techList);
        civilizationGroups = r.readGroupLists(Database.CIVILIZATION_GROUPS, civilizationList);
        performanceGroups = r.readGroupLists(Database.PERFORMANCE_GROUPS, performanceList);
        historyGroups = r.readGroupLists(Database.HISTORY_GROUPS, historyList);

        historyText = r.readHistoryText();

        bonusMap = r.readBonuses(Database.BONUS_LIST);
        hiddenBonusMap = r.readBonuses(Database.HIDDEN_BONUS);

        unitMap = r.readUnits();
        buildingMap = r.readBuildings();
        techMap = r.readTechnologies();
        r.readTechApplications();
        civilizationMap = r.readCivilizations();
        techBonusMap = r.readTechEffects();


        unitSelectedCiv = new ArrayList<>(unitList.size());
        buildingSelectedCiv = new ArrayList<>(buildingList.size());
        techSelectedCiv = new ArrayList<>(techList.size());

        unitSelectedAge = new ArrayList<>(unitList.size());
        buildingSelectedAge = new ArrayList<>(buildingList.size());
        techSelectedAge  = new ArrayList<>(techList.size());

        for (int i = 0; i < unitList.size(); ++i){
            getUnit(i + 1).getBonusContainer().sortBonuses();
            unitSelectedCiv.add(-1);
            unitSelectedAge.add(-1);
        }

        for (int i = 0; i < buildingList.size(); ++i) {
            getBuilding(i + 1).getBonusContainer().sortBonuses();
            buildingSelectedCiv.add(-1);
            buildingSelectedAge.add(-1);
        }

        for (int i = 0; i < techList.size(); ++i) {
            getTechnology(i + 1).getBonusContainer().sortBonuses();
            techSelectedCiv.add(-1);
            techSelectedAge.add(-1);
        }

    }

    public static void setContext(Context ctx){
        c = ctx;
    }

    public static EntityElement getElement(String file, int row) {
        if (row == 0) return new EntityElement(0, Database.getAppContext().getString(R.string.none), R.drawable.t_white, 0, "");
        else if (row == -1 && file.equals(Database.TECH_LIST)) return new EntityElement(0, Database.getAppContext().getString(R.string.dark_age), R.drawable.t_dark_age, 0, Database.TECH);
        else switch (file){
            case Database.UNIT_LIST: return unitList.get(row - 1);
            case Database.BUILDING_LIST: return buildingList.get(row - 1);
            case Database.TECH_LIST: return techList.get(row - 1);
            case Database.CIVILIZATION_LIST: return civilizationList.get(row - 1);
            case Database.CLASS_LIST: return classList.get(row - 1);
            case Database.TYPE_LIST: return typeList.get(row - 1);
            case Database.PERFORMANCE_LIST: return performanceList.get(row - 1);
            case Database.HISTORY_LIST: return historyList.get(row - 1);
            default: return new EntityElement(0, Database.getAppContext().getString(R.string.none), R.drawable.t_white, 0, "");
        }
    }

    public static List<EntityElement> getList(String file) {
        switch (file){
            case Database.UNIT_LIST: return unitList;
            case Database.BUILDING_LIST: return buildingList;
            case Database.TECH_LIST: return techList;
            case Database.CIVILIZATION_LIST: return civilizationList;
            case Database.CLASS_LIST: return classList;
            case Database.TYPE_LIST: return typeList;
            case Database.PERFORMANCE_LIST: return performanceList;
            case Database.HISTORY_LIST: return historyList;
            default: return new ArrayList<>();
        }
    }

    public static LinkedHashMap<String, List<EntityElement>> readAllLists() {
        LinkedHashMap<String, List<EntityElement>> b = new LinkedHashMap<>();
        List<EntityElement> units = getList(Database.UNIT_LIST);
        List<EntityElement> buildings = getList(Database.BUILDING_LIST);
        List<EntityElement> techs = getList(Database.TECH_LIST);
        List<EntityElement> civs = getList(Database.CIVILIZATION_LIST);
        b.put(Database.getAppContext().getString(R.string.tt_units), units);
        b.put(Database.getAppContext().getString(R.string.tt_buildings), buildings);
        b.put(Database.getAppContext().getString(R.string.tt_techs), techs);
        b.put(Database.getAppContext().getString(R.string.tt_civilizations), civs);
        return b;
    }

    public static HashMap<Integer, Integer> getOrderMap(String file, int index){
        switch (file){
            case Database.UNIT_GROUPS: return unitOrder.get(index);
            case Database.BUILDING_GROUPS: return buildingOrder.get(index);
            case Database.TECH_GROUPS: return techOrder.get(index);
            case Database.CIVILIZATION_GROUPS: return civilizationOrder.get(index);
            case Database.PERFORMANCE_GROUPS: return performanceOrder.get(index);
            default: return new HashMap<>();
        }
    }

    public static LinkedHashMap<String, List<EntityElement>> getGroupList(String file, int sort){
        switch (file){
            case Database.UNIT_GROUPS: return unitGroups.get(sort);
            case Database.BUILDING_GROUPS: return buildingGroups.get(sort);
            case Database.TECH_GROUPS: return techGroups.get(sort);
            case Database.CIVILIZATION_GROUPS: return civilizationGroups.get(sort);
            case Database.PERFORMANCE_GROUPS: return performanceGroups.get(sort);
            case Database.HISTORY_GROUPS: return historyGroups.get(sort);
            default: return new LinkedHashMap<>();
        }
    }

    public static Context getAppContext(){
        return c ;
    }

    public static List<TauntElement> getTauntList(){
        return tauntList;
    }

    public static HashMap<Integer, String> getStatList(){
        return statList;
    }

    public static HashMap<String, Boolean> getStatAddition(){
        return statAddition;
    }

    public static HashMap<Integer, String> getEcoList(){
        return ecoList;
    }

    public static HashMap<String, Double> getEcoValues(){
        return ecoValues;
    }

    public static List<EcoElement> getGatheringRates(){
        return gatheringRates;
    }

    public static List<Integer> getEcoUpgrades(){
        return ecoUpgrades;
    }

    public static LinkedHashMap<String, List<Integer>> getTechTreeQuizQuestions(){
        return techTreeQuizQuestions;
    }

    public static HashMap<Integer, String> getCivNameMap(){
        return civNameMap;
    }

    public static HashMap<String, Integer> getReversedCivNameMap(){
        return reversedCivNameMap;
    }

    public static String getHistoryText(int id){
        return historyText.get(id - 1);
    }

    public static Unit getUnit(int id){
        return unitMap.get(id - 1);
    }

    public static Building getBuilding(int id){
        return buildingMap.get(id - 1);
    }

    public static Technology getTechnology(int id){
        return techMap.get(id - 1);
    }

    public static Civilization getCivilization(int id){
        return civilizationMap.get(id - 1);
    }

    public static Bonus getBonus(int id) {
        return bonusMap.get(id - 1);
    }

    public static Bonus getHiddenBonus(int id){
        return hiddenBonusMap.get(id - 1);
    }

    public static TechBonus getTechEffect(int id){
        return techBonusMap.get(id - 1);
    }

    public static int getSelectedCiv(String type, int id){
        switch (type){
            case Database.UNIT: return unitSelectedCiv.get(id - 1);
            case Database.BUILDING: return buildingSelectedCiv.get(id - 1);
            case Database.TECH: return techSelectedCiv.get(id - 1);
            default: return -1;
        }
    }

    public static void setSelectedCiv(String type, int entityID, int civID){
        switch (type){
            case Database.UNIT:
                unitSelectedCiv.set(entityID - 1 , civID);
                break;
            case Database.BUILDING:
                buildingSelectedCiv.set(entityID - 1, civID);
                break;
            case Database.TECH:
                techSelectedCiv.set(entityID - 1, civID);
                break;
        }
    }

    public static int getSelectedAge(String type, int id){
        switch (type){
            case Database.UNIT: return unitSelectedAge.get(id - 1);
            case Database.BUILDING: return buildingSelectedAge.get(id - 1);
            case Database.TECH: return techSelectedAge.get(id - 1);
            default: return -1;
        }
    }

    public static void setSelectedAge(String type, int entityID, int ageID){
        switch (type){
            case Database.UNIT:
                unitSelectedAge.set(entityID - 1 , ageID);
                break;
            case Database.BUILDING:
                buildingSelectedAge.set(entityID - 1, ageID);
                break;
            case Database.TECH:
                techSelectedAge.set(entityID - 1, ageID);
                break;
        }
    }

    public static List<UpgradeElement> getUpgradeElementList(List<Integer> list){
        List<UpgradeElement> upgradeList = new ArrayList<>(list.size());
        for(int i: list) upgradeList.add(new UpgradeElement(Database.getElement(Database.TECH_LIST, i)));
        upgradeList.sort(UpgradeElement.getListElementComparator(Database.TECH_GROUPS, 1));
        return upgradeList;
    }

    public static void writeScore(ScoreList s, Context c) {
        String fileName = "score.dat";
        try {
            FileOutputStream fileOut = c.openFileOutput(fileName, Context.MODE_PRIVATE);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(s);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ScoreList readScore(Context c){
        String filename = "score.dat";
        try{
            FileInputStream fileIn = c.openFileInput(filename);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            Object o = in.readObject();
            ScoreList s = (ScoreList) o;
            in.close();
            return s;
        }catch (Exception e){
            e.printStackTrace();
        }
        ScoreList s =  new ScoreList();
        writeScore(s, c);
        return s;
    }

    public static String readLanguage(Context c){
        String fileName = "lang.dat";
        try{
            FileInputStream fileIn = c.openFileInput(fileName);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            Object o = in.readObject();
            String s = (String) o;
            in.close();
            return s;
        }catch (Exception e){
            e.printStackTrace();
        }
        writeLanguage(Database.ENGLISH, c);
        return Database.ENGLISH;
    }

    public static void writeLanguage(String language, Context c){
        String fileName = "lang.dat";
        try {
            FileOutputStream fileOut = c.openFileOutput(fileName, Context.MODE_PRIVATE);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(language);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void action_about(Context c) {
        new AlertDialog.Builder(c, R.style.Theme_AppCompat_Light_Dialog)
            .setTitle(c.getResources().getText(R.string.title_about))
            .setMessage((String.format(c.getString(R.string.about_text), Database.APP_VERSION, Database.PATCH_VERSION)))
            .setCancelable(true)
            .setPositiveButton(c.getString(R.string.ok), null)
            .show();
    }



}
