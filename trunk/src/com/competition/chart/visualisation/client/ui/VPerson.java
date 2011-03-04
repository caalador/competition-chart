package com.competition.chart.visualisation.client.ui;

public class VPerson {
    String name;
    boolean hasAdvanced;

    public VPerson(String name, boolean hasAdvanced) {
        this.name = name;
        this.hasAdvanced = hasAdvanced;
    }

    public String getName() {
        return name;
    }

    public boolean hasAdvanced() {
        return hasAdvanced;
    }

}
