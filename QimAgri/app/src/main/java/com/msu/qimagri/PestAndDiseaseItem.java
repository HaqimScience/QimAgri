// Package name
package com.msu.qimagri;

// Class name
public class PestAndDiseaseItem {
    // Attributes
    private int id;
    private String name;
    private String type;
    private String description;
    private String imageName;
    private int naturalTreatmentId;
    // Constructor
    public PestAndDiseaseItem(int id, String name, String type, String description, String imageName, int naturalTreatmentId) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.description = description;
        this.imageName = imageName;
        this.naturalTreatmentId = naturalTreatmentId;
    }
    // Getters
    public int getId(){
        return this.id;
    }
    public String getName(){
        return this.name;
    }
    public String getType(){
        return this.type;
    }
    public String getDescription(){
        return this.description;
    }
    public String getImageName(){
        return this.imageName;
    }
    public int getNaturalTreatmentId(){
        return this.naturalTreatmentId;
    }
}
