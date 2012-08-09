package com.competition.chart.demo;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.competition.chart.visualisation.Competitor;
import com.competition.chart.visualisation.KnockoutChart;
import com.competition.chart.visualisation.KnockoutChart.ValueSelectEvent;
import com.competition.chart.visualisation.KnockoutChart.ValueSelectListener;
import com.competition.chart.visualisation.KnockoutChart.VisualisationMode;
import com.vaadin.Application;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Window;

public class ChartApplication extends Application {

	private static final long serialVersionUID = -5045489868340290821L;

	private final Map<Integer, Competitor> participants = new HashMap<Integer, Competitor>();
	private KnockoutChart knockoutChart;
	private final Random rand = new Random(System.currentTimeMillis());

	@Override
	public void init() {
		final Window mainWindow = new Window("Chart Application");

		participants.put(1, new Competitor(1, getName(), 1));
		participants.put(2, new Competitor(2, getName(), 0));
		participants.put(3, new Competitor(3, getName(), 0));
		participants.put(4, new Competitor(4, getName(), 0));
		participants.put(5, new Competitor(5, getName(), 0));
		participants.put(6, new Competitor(6, getName(), 1));
		participants.put(7, new Competitor(7, getName(), 1));
		participants.put(8, new Competitor(8, getName(), 0));
		participants.put(9, new Competitor(9, getName(), 0));
		participants.put(10, new Competitor(10, getName(), 0));
		participants.put(11, new Competitor(11, getName(), 0));
		participants.put(12, new Competitor(12, getName(), 1));
		participants.put(13, new Competitor(13, getName(), 0));
		participants.put(14, new Competitor(14, getName(), 0));
		participants.put(15, new Competitor(15, getName(), 0));
		participants.put(16, new Competitor(16, getName(), 0));
		participants.put(17, new Competitor(17, getName(), 0));
		participants.put(18, new Competitor(18, getName(), 0));
		participants.put(19, new Competitor(19, getName(), 0));
		participants.put(20, new Competitor(20, getName(), 0));

		final List<Competitor> group1 = new LinkedList<Competitor>();
		group1.add(participants.get(1));

		final List<Competitor> group2 = new LinkedList<Competitor>();
		group2.add(participants.get(2));
		group2.add(participants.get(3));

		final List<Competitor> group3 = new LinkedList<Competitor>();
		group3.add(participants.get(4));
		group3.add(participants.get(5));
		group3.add(participants.get(12));

		final List<Competitor> group4 = new LinkedList<Competitor>();
		group4.add(participants.get(6));

		final List<Competitor> group5 = new LinkedList<Competitor>();
		group5.add(participants.get(7));

		final List<Competitor> group6 = new LinkedList<Competitor>();
		group6.add(participants.get(8));
		group6.add(participants.get(9));

		knockoutChart = new KnockoutChart();
		knockoutChart.addGroup(1, "A sarja", group1);
		knockoutChart.addGroup(2, "B sarja", group2);
		knockoutChart.addGroup(3, "C sarja", group3);
		knockoutChart.addGroup(4, "D sarja", group4);
		knockoutChart.addGroup(5, "E sarja", group5);
		knockoutChart.addGroup(6, "F sarja", group6);
		knockoutChart.addGroup(7, "G sarja", participants.get(10), participants.get(11));
		knockoutChart.addGroup(8, "H sarja", participants.get(13), participants.get(14));
		// knockoutChart.addGroup(9, "I sarja", participants.get(15),
		// participants.get(16));
		knockoutChart.setPanningEnabled(true);
		knockoutChart.addListener(vcl);

		final CheckBox left = new CheckBox("all on left");
		left.setImmediate(true);
		left.addListener(new ValueChangeListener() {
			private static final long serialVersionUID = -347103891605131202L;

			public void valueChange(final ValueChangeEvent event) {
				if (left.booleanValue()) {
					knockoutChart.setChartMode(VisualisationMode.LEFT_ONLY);
				} else {
					knockoutChart.setChartMode(VisualisationMode.LEFT_RIGHT);
				}
				knockoutChart.requestRepaint();
			}
		});

		mainWindow.addComponent(left);
		mainWindow.addComponent(knockoutChart);

		setMainWindow(mainWindow);
	}

	private String getName() {
		final StringBuilder name = new StringBuilder();
		name.append(Names.surNames[rand.nextInt(Names.surNames.length)]);
		name.append(" ");
		name.append(Names.firstNames[rand.nextInt(Names.firstNames.length)]);

		return name.toString();
	}

	private final ValueSelectListener vcl = new ValueSelectListener() {
		private static final long serialVersionUID = 1835799531600547425L;

		public void valueSelect(final ValueSelectEvent event) {
			final Competitor c = participants.get(Integer.parseInt(Long.toString(event.getKey())));
			c.addAdvanced();
			knockoutChart.requestRepaint();
		}
	};
}
