package com.competition.chart.visualisation;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.vaadin.Application;
import com.vaadin.ui.Window;

public class ChartApplication extends Application {
    @Override
    public void init() {
        Window mainWindow = new Window("Chart Application");

        List<String> group1 = new LinkedList<String>();
        group1.add("J. Kulmala_advance");
        group1.add("W. Lindberg");
        List<String> group2 = new LinkedList<String>();
        group2.add("J. Lehtinen");
        group2.add("O. Nieminen");

        List<String> group3 = new LinkedList<String>();
        group3.add("T. Lindros");
        group3.add("S. Liipo");

        Map<Integer, List<String>> map = new HashMap<Integer, List<String>>();
        map.put(1, group1);
        map.put(2, group2);
        map.put(3, group3);

        mainWindow.addComponent(new SportChart(map));

        group1 = new LinkedList<String>();
        group1.add("J. Kulmala_advance");
        group1.add("W. Lindberg");
        group1.add("T. Lindros");

        group2 = new LinkedList<String>();
        group2.add("J. Lehtinen");
        group2.add("O. Nieminen");
        group2.add("S. Liipo");

        map = new HashMap<Integer, List<String>>();
        map.put(1, group1);
        map.put(2, group2);

        mainWindow.addComponent(new SportChart(map));

        setMainWindow(mainWindow);
    }
}
