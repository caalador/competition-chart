package com.competition.chart.visualisation.client.ui;

public class VPerson {
    String name;
    int advancedToTier;

    public VPerson(String name, int advancedToTier) {
        this.name = name;
        this.advancedToTier = advancedToTier;
    }

    public String getName() {
        return name;
    }

    public int advancedTo() {
        return advancedToTier;
    }

}
