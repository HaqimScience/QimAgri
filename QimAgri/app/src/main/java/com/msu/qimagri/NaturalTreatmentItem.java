package com.msu.qimagri;

public class NaturalTreatmentItem {
    // Attributes
    private int id;
    private String name;
    private String description;
    private String imageName;
    private int pestAndDiseaseId;
    // Constructor
    public NaturalTreatmentItem(int id, String name, String description, String imageName, int pestAndDiseaseId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.imageName = imageName;
        this.pestAndDiseaseId = pestAndDiseaseId;
    }
    // Getters
    public int getId(){ return this.id; }
    public String getName(){
        return this.name;
    }
    public String getDescription(){
        return this.description;
    }
    public String getImageName(){
        return this.imageName;
    }
    public int getPestAndDiseaseId(){ return this.pestAndDiseaseId; }
}
