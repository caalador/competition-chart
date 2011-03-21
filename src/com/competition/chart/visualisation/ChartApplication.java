package com.competition.chart.visualisation;

import java.util.LinkedList;
import java.util.List;

import com.vaadin.Application;
import com.vaadin.ui.Window;

public class ChartApplication extends Application {
    @Override
    public void init() {
        final Window mainWindow = new Window("Chart Application");

        List<String> group1 = new LinkedList<String>();
        group1.add("J. Kulmala_advance");
        group1.add("W. Lindberg");

        List<String> group2 = new LinkedList<String>();
        group2.add("J. Lehtinen");
        group2.add("O. Nieminen");

        final List<String> group3 = new LinkedList<String>();
        group3.add("T. Lindros");
        group3.add("S. Liipo_advance");

        final List<String> group4 = new LinkedList<String>();
        group4.add("T. Tarvakainen");
        group4.add("S. Litz");

        final SportChart chart1 = new SportChart();
        chart1.addGroup(1, "A sarja", 0, group1);
        chart1.addGroup(2, "B sarja", 0, group2);
        chart1.addGroup(3, "C sarja", 0, group3);
        chart1.addGroup(4, "D sarja", 0, group4);

        mainWindow.addComponent(chart1);

        group1 = new LinkedList<String>();
        group1.add("J. Kulmala_advance");
        group1.add("W. Lindberg");
        group1.add("T. Lindros");

        group2 = new LinkedList<String>();
        group2.add("J. Lehtinen");
        group2.add("O. Nieminen");
        group2.add("S. Liipo");

        final SportChart chart2 = new SportChart();
        chart2.addGroup(1, "name 1", 0, group1);
        chart2.addGroup(2, "name second", 0, group2);

        mainWindow.addComponent(chart2);

        setMainWindow(mainWindow);
    }
}
