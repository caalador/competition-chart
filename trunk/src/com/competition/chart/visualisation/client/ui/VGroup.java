package com.competition.chart.visualisation.client.ui;

import java.util.LinkedList;
import java.util.List;

public class VGroup {
    private String name;
    private int number;
    private int tier;
    private List<VPerson> names = new LinkedList<VPerson>();
    private VGroup childGroup = null;

    public VGroup(String id) {
        String[] idString = id.split("_");
        number = Integer.parseInt(idString[0]);
        name = idString[1];
        tier = Integer.parseInt(idString[2]);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getTier() {
        return tier;
    }

    public void setTier(int tier) {
        this.tier = tier;
    }

    public void addName(VPerson name) {
        names.add(name);
    }

    public List<VPerson> getNames() {
        return names;
    }

    public void setChildGroup(VGroup child) {
        childGroup = child;
    }

    public VGroup getChildGroup() {
        return childGroup;
    }
}
