package com.aoedb.data;


import android.content.Context;

import com.aoedb.database.Database;
import com.aoedb.database.Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class BonusContainer {
    private List<Integer> bonusList;
    private List<Integer> teamBonusList;
    private List<Integer> uniqueTechList;
    private List<Integer> hiddenBonusList;

    private final HashMap<Integer, String> civMap;

    Context c;

    private String type;

    public BonusContainer(Context c) {
        this.c = c;
        bonusList = new ArrayList<>();
        teamBonusList = new ArrayList<>();
        uniqueTechList = new ArrayList<>();
        hiddenBonusList = new ArrayList<>();
        civMap = Database.getCivNameMap();
    }

    public List<Integer> getBonusList() {
        return bonusList;
    }

    public List<Integer> getTeamBonusList() {
        return teamBonusList;
    }

    public List<Integer> getUniqueTechList() {
        return uniqueTechList;
    }

    public List<Integer> getHiddenBonusList(){
        return hiddenBonusList;
    }

    public void setType(String type){
        this.type =  type;
    }

    Comparator<Integer> bonusComp = new Comparator<Integer>() {
        @Override
        public int compare(Integer o1, Integer o2) {
            Bonus b1= Database.getBonus(o1), b2 = Database.getBonus(o2);
            return civMap.get(b1.getCivilization()).compareTo(civMap.get(b2.getCivilization()));
        }
    };

    Comparator<Integer> teamBonusComp = new Comparator<Integer>() {
        @Override
        public int compare(Integer o1, Integer o2) {
            Bonus b1= Database.getBonus(o1), b2 = Database.getBonus(o2);
            return civMap.get(b1.getCivilization()).compareTo(civMap.get(b2.getCivilization()));
        }
    };

    Comparator<Integer> uniqueTechComp = new Comparator<Integer>() {
        @Override
        public int compare(Integer o1, Integer o2) {
            String civName1 = Utils.getUTCiv(o1), civName2 = Utils.getUTCiv(o2);
            return civName1.compareTo(civName2);
        }
    };

    public void setList(List<Integer> list, int i){
        switch (i){
            case 0:
                bonusList = list;
                break;
            case 1:
                teamBonusList = list;
                break;
            case 2:
                uniqueTechList = list;
                break;
            case 3:
                hiddenBonusList = list;
                break;
        }
    }

    public void sortBonuses(){
        Collections.sort(bonusList, bonusComp);
        Collections.sort(teamBonusList, teamBonusComp);
        Collections.sort(uniqueTechList, uniqueTechComp);
    }


    public List<String> writeBonuses(String name){
        List<String> bonusTextList = new ArrayList<>();

        StringBuilder bonusText = new StringBuilder();
        for (int i = 0; i< bonusList.size(); ++i){
            int id = bonusList.get(i);
            Bonus bonus = Database.getBonus(id);
            String desc = Utils.getProperDescription(bonus.getItemDescription(), type);
            desc = desc.replace("@", name);
            if (i != bonusList.size()-1) bonusText.append("<p><b>-").append(civMap.get(bonus.getCivilization())).append(":</b> ").append(desc).append("</p>");
            else bonusText.append("<b>-").append(civMap.get(bonus.getCivilization())).append(":</b> ").append(desc);
        }
        bonusTextList.add(bonusText.toString());
        StringBuilder teamBonusText = new StringBuilder();
        for (int i = 0; i< teamBonusList.size(); ++i){
            int id = teamBonusList.get(i);
            Bonus bonus = Database.getBonus(id);
            String desc = Utils.getProperDescription(bonus.getItemDescription(), type);
            desc = desc.replace("@", name);
            if (i!=teamBonusList.size()-1) teamBonusText.append("<p><b>-").append(civMap.get(bonus.getCivilization())).append(":</b> ").append(desc).append("</p>");
            else teamBonusText.append("<b>-").append(civMap.get(bonus.getCivilization())).append(":</b> ").append(desc);
        }
        bonusTextList.add(teamBonusText.toString());
        StringBuilder uniqueTechText = new StringBuilder();
        for(int i = 0; i< uniqueTechList.size(); ++i){
            int id = uniqueTechList.get(i);
            Descriptor d = Database.getTechnology(id).getDescriptor();
            String civName = Utils.getUTCiv(id);
            if (i!=uniqueTechList.size()-1) uniqueTechText.append("<p><b>-").append(civName).append(", ").append(d.getNominative()).append(":</b> ").append(d.getExtraDescription()).append("</p>");
            else uniqueTechText.append("<b>-").append(civName).append(", ").append(d.getNominative()).append(":</b> ").append(d.getExtraDescription());
        }
        bonusTextList.add(uniqueTechText.toString());
        return bonusTextList;

    }
}
