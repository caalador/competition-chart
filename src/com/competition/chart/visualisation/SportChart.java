package com.competition.chart.visualisation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.vaadin.terminal.PaintException;
import com.vaadin.terminal.PaintTarget;
import com.vaadin.ui.AbstractComponent;

/**
 * Server side component for the VSportChart widget.
 */
@com.vaadin.ui.ClientWidget(com.competition.chart.visualisation.client.ui.VSportChart.class)
public class SportChart extends AbstractComponent {

    Map<Integer, List<String>> data = new HashMap<Integer, List<String>>();

    public SportChart(Map<Integer, List<String>> data) {
        this.data = data;
    }

    @Override
    public void paintContent(PaintTarget target) throws PaintException {
        super.paintContent(target);

        Map<Integer, String> mmap = new HashMap<Integer, String>();
        for (int i = 1; i <= data.size(); i++) {
            List<String> names = data.get(i);
            StringBuilder values = new StringBuilder();

            for (String value : names) {
                values.append(value);
                values.append(";");
            }

            mmap.put(i, values.toString());
        }

        if (mmap.size() > 0) {
            target.addAttribute("data", mmap);
        }
    }

}
