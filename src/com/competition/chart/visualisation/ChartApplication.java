package com.competition.chart.visualisation;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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

    private Map<Integer, Competitor> participants = new HashMap<Integer, Competitor>();
    private KnockoutChart knockoutChart;

    @Override
    public void init() {
        final Window mainWindow = new Window("Chart Application");

        participants.put(1, new Competitor(1, "Vepsäläinen Johan", 1));
        participants.put(2, new Competitor(2, "Otto Meri", 0));
        participants.put(3, new Competitor(3, "Moilanen Joonas", 0));
        participants.put(4, new Competitor(4, "Hellsten Simo", 0));
        participants.put(5, new Competitor(5, "Vilkama Voitto", 0));
        participants.put(6, new Competitor(6, "Roininen Tuomas", 1));
        participants.put(7, new Competitor(7, "Rupponen Pasi", 1));
        participants.put(8, new Competitor(8, "Korhonen Markku", 0));
        participants.put(9, new Competitor(9, "Malm Janne", 0));
        participants.put(10, new Competitor(10, "Marjola Kalle", 0));
        participants.put(11, new Competitor(11, "Shibata Minako", 0));
        participants.put(12, new Competitor(12, "Nieminen Vesa", 1));

        List<Competitor> group1 = new LinkedList<Competitor>();
        group1.add(participants.get(1));

        List<Competitor> group2 = new LinkedList<Competitor>();
        group2.add(participants.get(2));
        group2.add(participants.get(3));

        List<Competitor> group3 = new LinkedList<Competitor>();
        group3.add(participants.get(4));
        group3.add(participants.get(5));

        List<Competitor> group4 = new LinkedList<Competitor>();
        group4.add(participants.get(6));

        List<Competitor> group5 = new LinkedList<Competitor>();
        group5.add(participants.get(7));

        List<Competitor> group6 = new LinkedList<Competitor>();
        group6.add(participants.get(8));
        group6.add(participants.get(9));

        knockoutChart = new KnockoutChart();
        knockoutChart.addGroup(1, "A sarja", group1);
        knockoutChart.addGroup(2, "B sarja", group2);
        knockoutChart.addGroup(3, "C sarja", group3);
        knockoutChart.addGroup(4, "D sarja", group4);
        // knockoutChart.addGroup(5, "E sarja", group5);
        // knockoutChart.addGroup(6, "F sarja", group6);
        knockoutChart.addGroup(7, "G sarja", participants.get(10),
                participants.get(11));
        // knockoutChart.addGroup(8, "H sarja", participants.get(12));
        knockoutChart.setPanningEnabled(true);
        knockoutChart.addListener(vcl);

        final CheckBox left = new CheckBox("all on left");
        left.setImmediate(true);
        left.addListener(new ValueChangeListener() {
            private static final long serialVersionUID = -347103891605131202L;

            public void valueChange(ValueChangeEvent event) {
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

    private ValueSelectListener vcl = new ValueSelectListener() {
        private static final long serialVersionUID = 1835799531600547425L;

        public void valueSelect(ValueSelectEvent event) {
            Competitor c = participants.get(Integer.parseInt(Long
                    .toString(event.getKey())));
            c.addAdvanced();
            knockoutChart.requestRepaint();
        }
    };
}
