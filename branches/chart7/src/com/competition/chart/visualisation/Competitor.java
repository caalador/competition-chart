package com.competition.chart.visualisation;

public class Competitor {

    private long id;
    private String name;
    private int advancedToTier;

    public Competitor(long id, String name, int advancedToTier) {
        this.name = name;
        this.advancedToTier = advancedToTier;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public int advancedTo() {
        return advancedToTier;
    }

    public void addAdvanced() {
        advancedToTier++;
    }

    public long getId() {
        return id;
    }
}
