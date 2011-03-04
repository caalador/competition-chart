package com.competition.chart.visualisation.client.ui;

import java.util.LinkedList;
import java.util.List;

public class VGroup {
    private String name;
    private List<VPerson> names = new LinkedList<VPerson>();

    public VGroup(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addName(VPerson name) {
        names.add(name);
    }

    public List<VPerson> getNames() {
        return names;
    }
}
