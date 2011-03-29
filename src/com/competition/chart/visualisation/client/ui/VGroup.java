package com.competition.chart.visualisation.client.ui;

import java.util.LinkedList;
import java.util.List;

public class VGroup {
    private String name;
    private int number;
    private int tier = 0;
    private List<VPerson> names = new LinkedList<VPerson>();
    private VGroup childGroup = null;
    private List<VGroup> parents = new LinkedList<VGroup>();
    private int middleOfGroup, top, bottom;

    private boolean hasPosition = false;

    public VGroup(String id) {
        String[] idString = id.split("_");
        number = Integer.parseInt(idString[0]);
        name = idString[1];
        if (idString.length == 3) {
            tier = Integer.parseInt(idString[2]);
        }
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

    public void addParent(VGroup parent) {
        parents.add(parent);
    }

    public List<VGroup> getParents() {
        return parents;
    }

    public int getMiddleOfGroup() {
        return middleOfGroup;
    }

    public int getBottom() {
        return bottom;
    }

    public int getTop() {
        return top;
    }

    public int calculatePosition(int offsetTop) {
        top = offsetTop;
        bottom = offsetTop + (20 * names.size());
        middleOfGroup = top + (bottom - top) / 2;
        hasPosition = true;
        return bottom + 20;
    }

    public void calculatePositionFromMiddle(int middleOfGroup) {
        this.middleOfGroup = middleOfGroup;
        int namesSizeHalved = 20 * names.size() / 2;
        top = middleOfGroup - namesSizeHalved;
        bottom = middleOfGroup + namesSizeHalved;
        hasPosition = true;
    }

    public boolean hasPosition() {
        return hasPosition;
    }

    public boolean hasCompetitors() {
        for (VPerson p : names) {
            if (p.getName().length() > 0 && p.advancedTo() > 0) {
                return true;
            }
        }
        return false;
    }
}
