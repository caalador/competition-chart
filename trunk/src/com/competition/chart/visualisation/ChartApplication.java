package com.competition.chart.visualisation;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.competition.chart.visualisation.SportChart.ValueSelectEvent;
import com.competition.chart.visualisation.SportChart.ValueSelectListener;
import com.competition.chart.visualisation.SportChart.VisualisationMode;
import com.vaadin.Application;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Window;

public class ChartApplication extends Application {

    private static final long serialVersionUID = -5045489868340290821L;

    private Map<Long, Competitor> participants = new HashMap<Long, Competitor>();
    private SportChart chart1;

    @Override
    public void init() {
        final Window mainWindow = new Window("Chart Application");

        participants.put(1l, new Competitor(1, "Vepsäläinen Johan", 1));
        participants.put(2l, new Competitor(2, "Otto Meri", 0));
        participants.put(3l, new Competitor(3, "Moilanen Joonas", 0));
        participants.put(4l, new Competitor(4, "Hellsten Simo", 0));
        participants.put(5l, new Competitor(5, "Vilkama Voitto", 0));
        participants.put(6l, new Competitor(6, "Roininen Tuomas", 1));
        participants.put(7l, new Competitor(7, "Rupponen Pasi", 1));
        participants.put(8l, new Competitor(8, "Korhonen Markku", 0));
        participants.put(9l, new Competitor(9, "Malm Janne", 0));
        participants.put(10l, new Competitor(10, "Marjola Kalle", 0));
        participants.put(11l, new Competitor(11, "Shibata Minako", 0));
        participants.put(12l, new Competitor(12, "Nieminen Vesa", 1));

        List<Competitor> group1 = new LinkedList<Competitor>();
        group1.add(participants.get(1l));

        List<Competitor> group2 = new LinkedList<Competitor>();
        group2.add(participants.get(2l));
        group2.add(participants.get(3l));

        List<Competitor> group3 = new LinkedList<Competitor>();
        group3.add(participants.get(4l));
        group3.add(participants.get(5l));

        List<Competitor> group4 = new LinkedList<Competitor>();
        group4.add(participants.get(6l));

        List<Competitor> group5 = new LinkedList<Competitor>();
        group5.add(participants.get(7l));

        List<Competitor> group6 = new LinkedList<Competitor>();
        group6.add(participants.get(8l));
        group6.add(participants.get(9l));

        List<Competitor> group7 = new LinkedList<Competitor>();
        group7.add(participants.get(10l));
        group7.add(participants.get(11l));

        List<Competitor> group8 = new LinkedList<Competitor>();
        group8.add(participants.get(12l));

        chart1 = new SportChart();
        chart1.addGroup(1, "A sarja", group1);
        chart1.addGroup(2, "B sarja", group2);
        chart1.addGroup(3, "C sarja", group3);
        chart1.addGroup(4, "D sarja", group4);
        chart1.addGroup(5, "E sarja", group5);
        chart1.addGroup(6, "F sarja", group6);
        chart1.addGroup(7, "G sarja", group7);
        chart1.addGroup(8, "H sarja", group8);
        // chart1.setChartMode(VisualisationMode.LEFT_ONLY);
        chart1.setPanningEnabled(true);
        chart1.addListener(vcl);

        final CheckBox left = new CheckBox("all on left");
        left.setImmediate(true);
        left.addListener(new ValueChangeListener() {
            public void valueChange(ValueChangeEvent event) {
                if (left.booleanValue()) {
                    chart1.setChartMode(VisualisationMode.LEFT_ONLY);
                } else {
                    chart1.setChartMode(VisualisationMode.LEFT_RIGHT);
                }
                chart1.requestRepaint();
            }
        });

        mainWindow.addComponent(left);
        mainWindow.addComponent(chart1);

        // group1 = new LinkedList<String>();
        // group1.add("J. Kulmala_1");
        // group1.add("W. Lindberg_0");
        // group1.add("T. Lindros_0");
        //
        // group2 = new LinkedList<String>();
        // group2.add("J. Lehtinen_0");
        // group2.add("O. Nieminen_0");
        // group2.add("S. Liipo_0");
        //
        // final SportChart chart2 = new SportChart();
        // chart2.addGroup(1, "name 1", group1);
        // chart2.addGroup(2, "name second", group2);
        //
        // mainWindow.addComponent(chart2);

        setMainWindow(mainWindow);
    }

    private ValueSelectListener vcl = new ValueSelectListener() {
        private static final long serialVersionUID = 1835799531600547425L;

        public void valueSelect(ValueSelectEvent event) {
            Competitor c = participants.get(event.getKey());
            c.addAdvanced();
            chart1.requestRepaint();
        }
    };
}
