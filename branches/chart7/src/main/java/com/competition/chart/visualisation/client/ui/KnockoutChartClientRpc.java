package com.competition.chart.visualisation.client.ui;

import com.vaadin.shared.communication.ClientRpc;

public interface KnockoutChartClientRpc extends ClientRpc {

	public void resetPositions();

	// public void setLeftOnly(boolean leftOnly);
	//
	// public void setPanEnabled(boolean enabled);

	public void setNameBoxWidth(int boxWidth);
}
