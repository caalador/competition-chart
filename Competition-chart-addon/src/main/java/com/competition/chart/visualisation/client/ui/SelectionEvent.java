package com.competition.chart.visualisation.client.ui;

import com.google.gwt.event.shared.GwtEvent;

public class SelectionEvent extends GwtEvent<SelectionEventHandler> {

	private static Type<SelectionEventHandler> TYPE;

	private final long value;

	public SelectionEvent(final long value) {
		this.value = value;
	}

	@Override
	public Type<SelectionEventHandler> getAssociatedType() {
		return getType();
	}

	public static Type<SelectionEventHandler> getType() {
		if (TYPE == null) {
			TYPE = new Type<SelectionEventHandler>();
		}
		return TYPE;
	}

	@Override
	protected void dispatch(final SelectionEventHandler handler) {
		handler.onSelectionEvent(this);
	}

	public long getValue() {
		return value;
	}

}
