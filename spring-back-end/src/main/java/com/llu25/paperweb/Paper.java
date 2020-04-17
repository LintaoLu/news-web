package com.llu25.paperweb;


public class Paper {

    private String paperName;
    private String description;

    public Paper(String paperName, String description) {
        this.paperName = paperName;
        this.description = description;
    }

    public String getPaperName() {
        return paperName;
    }

    public void setPaperName(String paperName) {
        this.paperName = paperName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
