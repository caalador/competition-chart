package com.competition.chart.visualisation;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.competition.chart.visualisation.client.Competitor;
import com.competition.chart.visualisation.client.ui.KnockoutChartClientRpc;
import com.competition.chart.visualisation.client.ui.KnockoutChartServerRpc;
import com.competition.chart.visualisation.client.ui.KnockoutChartState;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Component;

/**
 * Server side component for the VSportChart widget.
 */
public class KnockoutChart extends AbstractComponent {

	private static final long serialVersionUID = -1405328596263886664L;

	public enum VisualisationMode {
		LEFT_RIGHT, LEFT_ONLY
	}

	private final Map<String, List<Competitor>> data = new HashMap<String, List<Competitor>>();
	private boolean left = false;
	private boolean enableDrag = false;
	private int boxWidth;
	// private final boolean sendBoxWidth = false;
	// private final boolean reset = false;
	// private final List<Integer> givenIds = new LinkedList<Integer>();

	KnockoutChartServerRpc rpc = new KnockoutChartServerRpc() {
		private static final long serialVersionUID = -3141658206485027775L;

		@Override
		public void clickedOnCompetitor(final long selectionId) {
			System.out.println("SelectionEvent " + selectionId);
			if (idExists(selectionId)) {
				fireValueSelect(selectionId);
				markAsDirty();
			}
		}
	};

	public KnockoutChart() {
		setWidth("1600px");
		setHeight("1200px");
		getState().canvasWidth = (int) getWidth();
		getState().canvasHeight = (int) getHeight();
		registerRpc(rpc);
	}

	public KnockoutChart(final String width, final String height) {
		setWidth(width);
		setHeight(height);
		getState().canvasWidth = (int) getWidth();
		getState().canvasHeight = (int) getHeight();
		registerRpc(rpc);
	}

	public KnockoutChart(final VisualisationMode mode) {
		setChartMode(mode);
		getState().canvasWidth = (int) getWidth();
		getState().canvasHeight = (int) getHeight();
		registerRpc(rpc);
	}

	public void addGroup(final int groupNumber, final String groupName, final Competitor... participants) {
		final List<Competitor> competitors = new LinkedList<Competitor>();
		for (final Competitor c : participants) {
			competitors.add(c);
		}
		addGroup(groupNumber, groupName, competitors);
	}

	public void addGroup(final int groupNumber, final String groupName, final List<Competitor> participants) {
		final String id = groupNumber + "_" + groupName;
		data.put(id, participants);
	}

	public void setChartMode(final VisualisationMode mode) {
		if (mode == VisualisationMode.LEFT_ONLY) {
			left = true;
		} else {
			left = false;
		}
		getState().allOnLeft = left;
	}

	public void setPanningEnabled(final boolean panning) {
		enableDrag = panning;
		getState().pan = panning;
	}

	public void setNameBoxWidth(final int boxWidth) {
		this.boxWidth = boxWidth;
		getRpcProxy(KnockoutChartClientRpc.class).setNameBoxWidth(boxWidth);
	}

	@Override
	protected KnockoutChartState getState() {
		return (KnockoutChartState) super.getState();
	}

	@Override
	public void beforeClientResponse(final boolean initial) {
		super.beforeClientResponse(initial);
		getState().competitorsByGroup = data;
	}

	public boolean idExists(final Long id) {
		for (final String key : data.keySet()) {
			final List<Competitor> persons = data.get(key);

			for (final Competitor person : persons) {
				if (person.getId() == id) {
					return true;
				}
			}
		}
		return false;
	}

	public void resetChartPositions() {
		getRpcProxy(KnockoutChartClientRpc.class).resetPositions();
	}

	private static final Method VALUE_SELECTION_EVENT;

	static {
		try {
			VALUE_SELECTION_EVENT = ValueSelectListener.class.getDeclaredMethod("valueSelect", new Class[] { ValueSelectEvent.class });
		} catch (final java.lang.NoSuchMethodException e) {
			// This should never happen
			throw new java.lang.RuntimeException("Internal error finding methods in Timeline");
		}
	}

	public class ValueSelectEvent extends Component.Event {

		private static final long serialVersionUID = -4763258406409240871L;

		private Long key;

		public ValueSelectEvent(final Component source) {
			super(source);
		}

		public ValueSelectEvent(final Component source, final Long key) {
			super(source);
			this.key = key;
		}

		public Long getKey() {
			return key;
		}
	}

	public interface ValueSelectListener extends Serializable {

		public void valueSelect(ValueSelectEvent event);
	}

	public void addListener(final ValueSelectListener listener) {
		addListener(ValueSelectEvent.class, listener, VALUE_SELECTION_EVENT);
	}

	public void removeListener(final ValueSelectListener listener) {
		removeListener(ValueSelectEvent.class, listener, VALUE_SELECTION_EVENT);
	}

	protected void fireValueSelect(final Long key) {
		fireEvent(new KnockoutChart.ValueSelectEvent(this, key));
	}
}
