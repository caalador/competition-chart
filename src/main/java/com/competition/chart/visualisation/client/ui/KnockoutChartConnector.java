package com.competition.chart.visualisation.client.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Widget;
import com.vaadin.client.communication.StateChangeEvent;
import com.vaadin.client.ui.AbstractComponentConnector;
import com.vaadin.shared.ui.Connect;

@Connect(com.competition.chart.visualisation.KnockoutChart.class)
public class KnockoutChartConnector extends AbstractComponentConnector implements SelectionEventHandler {

	private static final long serialVersionUID = -2333387185953851790L;

	private final List<HandlerRegistration> handlerRegistration = new ArrayList<HandlerRegistration>();

	@Override
	protected void init() {
		super.init();

		handlerRegistration.add(getWidget().addSelectionEventHandler(this));

		registerRpc(KnockoutChartClientRpc.class, new KnockoutChartClientRpc() {
			private static final long serialVersionUID = 4530030113549729546L;

			@Override
			public void resetPositions() {
				getWidget().resetPositions();
			}

			@Override
			public void setNameBoxWidth(final int boxWidth) {
				VKnockoutChart.setBoxWidth(boxWidth);
			}
		});
	}

	@Override
	protected Widget createWidget() {
		return GWT.create(VKnockoutChart.class);
	}

	@Override
	public VKnockoutChart getWidget() {
		return (VKnockoutChart) super.getWidget();
	}

	@Override
	public KnockoutChartState getState() {
		return (KnockoutChartState) super.getState();
	}

	@Override
	public void onStateChanged(final StateChangeEvent stateChangeEvent) {
		super.onStateChanged(stateChangeEvent);
		final Set<String> changed = stateChangeEvent.getChangedProperties();

		getWidget().setCanvasWidth(getState().canvasWidth);
		getWidget().setCanvasHeight(getState().canvasHeight);

		if (changed.contains("pan")) {
			getWidget().setEnableDragging(getState().pan);
			if (!changed.contains("competitorsByGroup")) {
				getWidget().checkGroups(getState().competitorsByGroup);
			}
		}
		if (changed.contains("allOnLeft")) {
			getWidget().allOnLeft(getState().allOnLeft);
			if (!changed.contains("competitorsByGroup")) {
				getWidget().checkGroups(getState().competitorsByGroup);
			}
		}

		if (changed.contains("competitorsByGroup")) {
			getWidget().checkGroups(getState().competitorsByGroup);
		}
	}

	@Override
	public void onSelectionEvent(final SelectionEvent event) {
		getRpcProxy(KnockoutChartServerRpc.class).clickedOnCompetitor(event.getValue());
	}
}
