package com.aoedb.data;

import com.aoedb.R;
import com.aoedb.database.Database;

import java.util.LinkedHashMap;
import java.util.List;



public class Technology extends Entity {

    protected LinkedHashMap<String, List<EntityElement>> applications;

    public Technology() {
        super();
    }

    public Technology(Technology t){
        super(t);
        this.applications = getApplications();
        resetStats();
    }


    public EntityElement getCreatorElement(){
        return getEntityElement(Database.getAppContext().getString(R.string.research_building));
    }
    

    public void setApplications(LinkedHashMap<String, List<EntityElement>> map){
        applications = map;
    }

    public LinkedHashMap<String, List<EntityElement>> getApplications(){
        return applications;
    }

    }
