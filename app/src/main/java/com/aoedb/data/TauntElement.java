package com.aoedb.data;

public class TauntElement {

    private final String name;
    private final int id;
    private final int fileID;

    public TauntElement(int id, String name, int fileID) {
        this.id = id;
        this.name = name;
        this.fileID = fileID;
    }

    public int getID() {
        return id;
    }

    public String getName() {
        return name;
    }


    public int getFileID() {
        return fileID;
    }
}
