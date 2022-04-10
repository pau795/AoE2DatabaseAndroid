package com.aoedb.data;


import java.io.Serializable;
import java.util.Comparator;
import java.util.HashMap;

public class EntityElement implements Serializable{
    private final int id;
    private final String name;
    private final int image;
    private final int media;
    private final String type;

    public EntityElement(int id, String name, int image, int gif, String type) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.media = gif;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getImage() {
        return image;
    }

    public int getMedia() {
        return media;
    }

    public String getType() {
        return type;
    }


    public static Comparator<EntityElement> getListElementComparator(HashMap<Integer, Integer> orderMap){
        return Comparator.comparing(o -> orderMap.get(o.getId()));
    }

    public static Comparator<EntityElement> getAlphabeticalComparator(){
        return Comparator.comparing(EntityElement::getName);
    }
}
