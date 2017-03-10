package com.competition.chart.visualisation.client.ui;

import java.util.List;
import java.util.Map;

import com.competition.chart.visualisation.client.Competitor;
import com.vaadin.shared.AbstractComponentState;

public class KnockoutChartState extends AbstractComponentState {

	private static final long serialVersionUID = -8793185930172301203L;

	public Map<String, List<Competitor>> competitorsByGroup;

	public boolean allOnLeft = false;
	public boolean pan = true;
	public int canvasWidth;
	public int canvasHeight;
}
