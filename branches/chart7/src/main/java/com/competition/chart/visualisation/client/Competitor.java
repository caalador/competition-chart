package com.competition.chart.visualisation.client;


public class Competitor {

	private long id;
	private String name;
	public int advancedToTier;

	public Competitor() {
		id = 1l;
		name = "";
	}

	public Competitor(final long id, final String name, final int advancedToTier) {
		this.name = name;
		this.advancedToTier = advancedToTier;
		this.id = id;
	}

	public void setId(final long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
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

	@Override
	public boolean equals(final Object o) {
		if (o instanceof Competitor) {
			return ((Competitor) o).getId() == id;
		}
		return false;
	}
}
