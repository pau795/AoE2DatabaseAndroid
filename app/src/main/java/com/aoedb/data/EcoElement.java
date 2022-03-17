package com.aoedb.data;

public class EcoElement {
    int stat;
    String statName;
    int statIcon;
    int resourceIcon;
    double gatheringRate;

    public EcoElement(){

    }



    public int getStat() {
        return stat;
    }

    public void setStat(int stat) {
        this.stat = stat;
    }

    public String getStatName() {
        return statName;
    }

    public void setStatName(String statName) {
        this.statName = statName;
    }

    public int getStatIcon() {
        return statIcon;
    }

    public void setStatIcon(int statIcon) {
        this.statIcon = statIcon;
    }

    public int getResourceIcon() {
        return resourceIcon;
    }

    public void setResourceIcon(int resourceIcon) {
        this.resourceIcon = resourceIcon;
    }

    public double getGatheringRate() {
        return gatheringRate;
    }

    public void setGatheringRate(double gatheringRate) {
        this.gatheringRate = gatheringRate;
    }
}
