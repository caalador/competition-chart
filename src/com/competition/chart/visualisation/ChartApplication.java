package com.competition.chart.visualisation;

import java.util.LinkedList;
import java.util.List;

import com.vaadin.Application;
import com.vaadin.ui.Window;

public class ChartApplication extends Application {
    @Override
    public void init() {
        final Window mainWindow = new Window("Chart Application");

        List<Competitor> group1 = new LinkedList<Competitor>();
        group1.add(new Competitor("J. Kulmala", 1));
        group1.add(new Competitor("W. Lindberg", 0));

        List<Competitor> group2 = new LinkedList<Competitor>();
        group2.add(new Competitor("J. Lehtinen", 0));
        group2.add(new Competitor("O. Nieminen", 0));

        List<Competitor> group3 = new LinkedList<Competitor>();
        group3.add(new Competitor("T. Lindros", 0));
        group3.add(new Competitor("S. Liipo", 1));

        List<Competitor> group4 = new LinkedList<Competitor>();
        group4.add(new Competitor("T. Tarvakainen", 3));
        group4.add(new Competitor("S. Litz", 0));

        List<Competitor> group5 = new LinkedList<Competitor>();
        group5.add(new Competitor("J. Lehtinen", 0));
        group5.add(new Competitor("O. Nieminen", 0));

        List<Competitor> group6 = new LinkedList<Competitor>();
        group6.add(new Competitor("J. Lehtinen", 0));
        group6.add(new Competitor("O. Nieminen", 1));

        List<Competitor> group7 = new LinkedList<Competitor>();
        group7.add(new Competitor("J. Lehtinen", 0));
        group7.add(new Competitor("O. Nieminen", 0));

        List<Competitor> group8 = new LinkedList<Competitor>();
        group8.add(new Competitor("J. Lehtinen", 0));
        group8.add(new Competitor("O. Nieminen", 0));

        final SportChart chart1 = new SportChart();
        chart1.addGroup(1, "A sarja", group1);
        chart1.addGroup(2, "B sarja", group2);
        // chart1.addGroup(3, "C sarja", group3);
        // chart1.addGroup(4, "D sarja", group4);
        // chart1.addGroup(5, "A sarja", group5);
        // chart1.addGroup(6, "B sarja", group6);
        // chart1.addGroup(7, "C sarja", group7);
        // chart1.addGroup(8, "D sarja", group8);
        chart1.addWinner("J. Kulmala");
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
}
