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

    private static final long serialVersionUID = -1405328596263886664L;

    Map<String, List<Competitor>> data = new HashMap<String, List<Competitor>>();
    String winner;

    public SportChart() {
    }

    public void addGroup(int groupNumber, String groupName,
            List<Competitor> participants) {
        String id = groupNumber + "_" + groupName;
        data.put(id, participants);
    }

    public void addWinner(String name) {
        winner = name;
    }

    @Override
    public void paintContent(PaintTarget target) throws PaintException {
        super.paintContent(target);

        Map<String, String> mmap = new HashMap<String, String>();
        for (String key : data.keySet()) {
            List<Competitor> persons = data.get(key);
            StringBuilder values = new StringBuilder();

            for (Competitor person : persons) {
                values.append(person.getName() + "_" + person.advancedTo());
                values.append(";");
            }

            mmap.put(key, values.toString());
        }

        if (mmap.size() > 0) {
            target.addAttribute("data", mmap);
        }

        if (winner != null) {
            target.addAttribute("winner", winner);
        }
    }

}
