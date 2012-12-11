package com.competition.chart.visualisation.client.ui;

import java.util.LinkedList;
import java.util.List;

import com.competition.chart.visualisation.client.Competitor;

public class VGroup {
	private final String id;
	private String name;
	private int number;
	private int tier = 0;
	private final List<Competitor> names = new LinkedList<Competitor>();
	private VGroup childGroup = null;
	private final List<VGroup> parents = new LinkedList<VGroup>();
	private float middleOfGroup, top, bottom, leftSide;

	private boolean hasPosition = false;

	public VGroup(final String id) {
		this.id = id;
		final String[] idString = id.split("_");
		number = Integer.parseInt(idString[0]);
		name = idString[1];
		if (idString.length == 3) {
			tier = Integer.parseInt(idString[2]);
		}
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(final int number) {
		this.number = number;
	}

	public int getTier() {
		return tier;
	}

	public void setTier(final int tier) {
		this.tier = tier;
	}

	public void addName(final Competitor name) {
		if (!names.contains(name)) {
			names.add(name);
		} else {
			for (final Competitor p : names) {
				if (p.equals(name)) {
					names.remove(p);
					names.add(name);
					break;
				}
			}
		}
	}

	public List<Competitor> getNames() {
		return names;
	}

	public void setChildGroup(final VGroup child) {
		childGroup = child;
	}

	public VGroup getChildGroup() {
		return childGroup;
	}

	public void addParent(final VGroup parent) {
		parents.add(parent);
	}

	public List<VGroup> getParents() {
		return parents;
	}

	public float getMiddleOfGroup() {
		return middleOfGroup;
	}

	public float getBottom() {
		return bottom;
	}

	public float getTop() {
		return top;
	}

	public float getLeftSide() {
		return leftSide;
	}

	public void setLeftSide(final float leftSide) {
		this.leftSide = leftSide;
	}

	public float calculatePosition(final float offsetTop) {
		top = offsetTop;
		bottom = offsetTop + (20 * names.size());
		middleOfGroup = top + (bottom - top) / 2;
		hasPosition = true;
		return bottom + 20;
	}

	public void updatePosition(final float offset) {
		top += offset;
		bottom += offset;
		middleOfGroup += offset;
	}

	public void calculatePositionFromMiddle(final float middleOfGroup) {
		this.middleOfGroup = middleOfGroup;
		final int namesSizeHalved = 20 * names.size() / 2;
		top = middleOfGroup - namesSizeHalved;
		bottom = middleOfGroup + namesSizeHalved;
		hasPosition = true;
	}

	public boolean hasPosition() {
		return hasPosition;
	}

	public boolean hasCompetitors() {
		for (final Competitor p : names) {
			if (p.getName().length() > 0 && p.advancedTo() > 0) {
				return true;
			}
		}
		return false;
	}
}
