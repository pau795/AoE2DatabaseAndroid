package com.aoedb.data;


import com.aoedb.database.Database;

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


    public static Comparator<EntityElement> getListElementComparator(final String file, final int index){

        return new Comparator<EntityElement>() {
            final HashMap<Integer, Integer> order =  Database.getOrderMap(file, index);
            @Override
            public int compare(EntityElement o1, EntityElement o2) {
                return order.get(o1.getId()).compareTo(order.get(o2.getId()));
            }
        };
    }
}
