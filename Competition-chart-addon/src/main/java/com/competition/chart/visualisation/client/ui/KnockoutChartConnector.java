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
				getWidget().checkGroups(getState().competitorsByGroup);
			}

			@Override
			public void setNameBoxWidth(final int boxWidth) {
				CKnockoutChart.setBoxWidth(boxWidth);
			}

			@Override
			public void setLeftOnly(final boolean leftOnly) {
				getWidget().allOnLeft(leftOnly);
				getWidget().checkGroups(getState().competitorsByGroup);
			}

			@Override
			public void setPanEnabled(final boolean enabled) {
				getWidget().setEnableDragging(enabled);
				getWidget().checkGroups(getState().competitorsByGroup);
			}
		});
	}

	@Override
	protected Widget createWidget() {
		return GWT.create(CKnockoutChart.class);
	}

	@Override
	public CKnockoutChart getWidget() {
		return (CKnockoutChart) super.getWidget();
	}

	@Override
	public KnockoutChartState getState() {
		return (KnockoutChartState) super.getState();
	}

	@Override
	public void onStateChanged(final StateChangeEvent stateChangeEvent) {
		super.onStateChanged(stateChangeEvent);

		getWidget().setCanvasWidth(getState().canvasWidth);
		getWidget().setCanvasHeight(getState().canvasHeight);

		if (stateChangeEvent.hasPropertyChanged("competitorsByGroup")) {
			getWidget().checkGroups(getState().competitorsByGroup);
		}
	}

	@Override
	public void onSelectionEvent(final SelectionEvent event) {
		getRpcProxy(KnockoutChartServerRpc.class).clickedOnCompetitor(event.getValue());
	}
}
