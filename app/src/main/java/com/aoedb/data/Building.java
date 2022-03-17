package com.aoedb.data;

import com.aoedb.R;
import com.aoedb.database.Database;

import java.util.LinkedHashMap;
import java.util.List;

public class Building extends Item {

    protected LinkedHashMap<String, List<EntityElement>> trainable;

    public Building() {
        super();
    }

    public Building(Building b){
        super(b);
        this.trainable = b.getTrainable();
        resetStats();
    }

    public EntityElement getRequiredBuildingElement(){
        return getEntityElement(Database.getAppContext().getString(R.string.required_building));
    }

    public EntityElement getCreatorElement(){
        return getEntityElement(Database.getAppContext().getString(R.string.builder_unit));
    }

    public LinkedHashMap<String, List<EntityElement>> getTrainable(){
        return trainable;
    }

    public void setTrainable(LinkedHashMap<String, List<EntityElement>> map) {
        trainable = map;
    }

}
