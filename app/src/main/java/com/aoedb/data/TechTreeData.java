package com.aoedb.data;

import android.content.Context;

import com.aoedb.widgets.TechTreeBox;

import java.util.ArrayList;
import java.util.List;

public class TechTreeData {



    List<TechTreeBox> instanceList;
    List<TechTreeBox> uniqueBoxes;

    public TechTreeData(Context c){
        instanceList = new ArrayList<>();
        uniqueBoxes = new ArrayList<>();
    }

    public void addInstance(TechTreeBox t){
        instanceList.add(t);
    }

    public void addUniqueBox(TechTreeBox t){
        uniqueBoxes.add(t);
    }

    public List<TechTreeBox> getInstanceList(){
        return instanceList;
    }

    public List<TechTreeBox> getUniqueBoxes(){
        return uniqueBoxes;
    }

}
