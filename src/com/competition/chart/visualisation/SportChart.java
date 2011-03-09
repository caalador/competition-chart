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

    Map<String, List<String>> data = new HashMap<String, List<String>>();

    public SportChart() {
    }

    public void addGroup(int groupNumber, String groupName, int groupTier,
            List<String> participants) {
        String id = groupNumber + "_" + groupName + "_" + groupTier;
        data.put(id, participants);
    }

    @Override
    public void paintContent(PaintTarget target) throws PaintException {
        super.paintContent(target);

        Map<String, String> mmap = new HashMap<String, String>();
        for (String key : data.keySet()) {
            List<String> names = data.get(key);
            StringBuilder values = new StringBuilder();

            for (String value : names) {
                values.append(value);
                values.append(";");
            }

            mmap.put(key, values.toString());
        }

        if (mmap.size() > 0) {
            target.addAttribute("data", mmap);
        }
    }

}
