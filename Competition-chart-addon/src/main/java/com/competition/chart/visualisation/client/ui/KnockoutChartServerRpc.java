package com.competition.chart.visualisation.client.ui;

import com.vaadin.shared.communication.ServerRpc;

public interface KnockoutChartServerRpc extends ServerRpc {

	public void clickedOnCompetitor(long id);
}
