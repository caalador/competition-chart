package com.competition.chart.visualisation;

public class Competitor {
    private String name;
    private int advancedToTier;

    public Competitor(String name, int advancedToTier) {
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
