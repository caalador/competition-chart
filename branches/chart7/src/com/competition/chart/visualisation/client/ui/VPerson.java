package com.competition.chart.visualisation.client.ui;

public class VPerson {
    long id;
    String name;
    int advancedToTier;

    public VPerson(long id, String name, int advancedToTier) {
        this.id = id;
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

    @Override
    public boolean equals(Object o) {
        if (o instanceof VPerson) {
            return ((VPerson) o).getId() == id;
        }
        return false;
    }
}
