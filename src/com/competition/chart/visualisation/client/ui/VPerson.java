package com.competition.chart.visualisation.client.ui;

public class VPerson {
    long id;
    String name;
    int advancedToTier;

    public VPerson(long id, String name, int advancedToTier) {
        this.name = name;
        this.advancedToTier = advancedToTier;
    }

    public String getName() {
        return name;
    }

    public int advancedTo() {
        return advancedToTier;
    }

    public long getId() {
        return id;
    }
}
