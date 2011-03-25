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
        group1.add("J. Kulmala_1");
        group1.add("W. Lindberg_0");

        List<String> group2 = new LinkedList<String>();
        group2.add("J. Lehtinen_0");
        group2.add("O. Nieminen_0");

        List<String> group3 = new LinkedList<String>();
        group3.add("T. Lindros_0");
        group3.add("S. Liipo_1");

        List<String> group4 = new LinkedList<String>();
        group4.add("T. Tarvakainen_2");
        group4.add("S. Litz_0");

        List<String> group5 = new LinkedList<String>();
        group5.add("J. Lehtinen_0");
        group5.add("O. Nieminen_0");

        List<String> group6 = new LinkedList<String>();
        group6.add("J. Lehtinen_0");
        group6.add("O. Nieminen_0");

        List<String> group7 = new LinkedList<String>();
        group7.add("J. Lehtinen_0");
        group7.add("O. Nieminen_0");

        List<String> group8 = new LinkedList<String>();
        group8.add("J. Lehtinen_0");
        group8.add("O. Nieminen_0");

        final SportChart chart1 = new SportChart();
        chart1.addGroup(1, "A sarja", group1);
        chart1.addGroup(2, "B sarja", group2);
        chart1.addGroup(3, "C sarja", group3);
        chart1.addGroup(4, "D sarja", group4);
        chart1.addGroup(5, "A sarja", group5);
        chart1.addGroup(6, "B sarja", group6);
        chart1.addGroup(7, "C sarja", group7);
        chart1.addGroup(8, "D sarja", group8);

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
