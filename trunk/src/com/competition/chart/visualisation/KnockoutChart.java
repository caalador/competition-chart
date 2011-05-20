package com.competition.chart.visualisation;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.vaadin.terminal.PaintException;
import com.vaadin.terminal.PaintTarget;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Component;

/**
 * Server side component for the VSportChart widget.
 */
@com.vaadin.ui.ClientWidget(com.competition.chart.visualisation.client.ui.VKnockoutChart.class)
public class KnockoutChart extends AbstractComponent {

    private static final long serialVersionUID = -1405328596263886664L;

    public enum VisualisationMode {
        LEFT_RIGHT, LEFT_ONLY
    }

    private Map<String, List<Competitor>> data = new HashMap<String, List<Competitor>>();
    private boolean left = false;
    private boolean enableDrag = false;
    private int boxWidth;
    private boolean sendBoxWidth = false;
    private List<Integer> givenIds = new LinkedList<Integer>();

    public KnockoutChart() {
        setWidth("1600px");
        setHeight("1200px");
    }

    public KnockoutChart(String width, String height) {
        setWidth(width);
        setHeight(height);
    }

    public KnockoutChart(VisualisationMode mode) {
        setChartMode(mode);
    }

    public void addGroup(int groupNumber, String groupName,
            Competitor... participants) {
        List<Competitor> competitors = new LinkedList<Competitor>();
        for (Competitor c : participants) {
            competitors.add(c);
        }
        addGroup(groupNumber, groupName, competitors);
    }

    public void addGroup(int groupNumber, String groupName,
            List<Competitor> participants) {
        String id = groupNumber + "_" + groupName;
        data.put(id, participants);
    }

    public void setChartMode(VisualisationMode mode) {
        if (mode == VisualisationMode.LEFT_ONLY) {
            left = true;
        } else {
            left = false;
        }
    }

    public void setPanningEnabled(boolean panning) {
        enableDrag = panning;
    }

    public void setNameBoxWidth(int boxWidth) {
        this.boxWidth = boxWidth;
        sendBoxWidth = true;
    }

    @Override
    public void paintContent(PaintTarget target) throws PaintException {
        super.paintContent(target);

        Map<String, String> mmap = new HashMap<String, String>();
        for (String key : data.keySet()) {
            List<Competitor> persons = data.get(key);
            StringBuilder values = new StringBuilder();

            for (Competitor person : persons) {
                values.append(person.getId() + "_" + person.getName() + "_"
                        + person.advancedTo());
                values.append(";");
            }

            mmap.put(key, values.toString());
        }

        if (mmap.size() > 0) {
            target.addAttribute("data", mmap);
        }

        if (left) {
            target.addAttribute("allOnLeft", true);
        }

        if (enableDrag) {
            target.addAttribute("enableDrag", true);
        }

        if (sendBoxWidth) {
            target.addAttribute("boxWidth", boxWidth);
        }
    }

    @Override
    public void changeVariables(Object source, Map variables) {
        super.changeVariables(source, variables);

        if (variables.containsKey("selected")) {
            Long selectionId = Long.parseLong((String) variables
                    .get("selected"));
            if (idExists(selectionId)) {
                fireValueSelect(selectionId);
            }
        }
    }

    public boolean idExists(Long id) {
        for (String key : data.keySet()) {
            List<Competitor> persons = data.get(key);

            for (Competitor person : persons) {
                if (person.getId() == id) {
                    return true;
                }
            }
        }
        return false;
    }

    private static final Method VALUE_SELECTION_EVENT;

    static {
        try {
            VALUE_SELECTION_EVENT = ValueSelectListener.class
                    .getDeclaredMethod("valueSelect",
                            new Class[] { ValueSelectEvent.class });
        } catch (final java.lang.NoSuchMethodException e) {
            // This should never happen
            throw new java.lang.RuntimeException(
                    "Internal error finding methods in Timeline");
        }
    }

    public class ValueSelectEvent extends Component.Event {

        private static final long serialVersionUID = -4763258406409240871L;

        private Long key;

        public ValueSelectEvent(Component source) {
            super(source);
        }

        public ValueSelectEvent(Component source, Long key) {
            super(source);
            this.key = key;
        }

        public Long getKey() {
            return key;
        }
    }

    public interface ValueSelectListener extends Serializable {

        public void valueSelect(ValueSelectEvent event);
    }

    public void addListener(ValueSelectListener listener) {
        addListener(ValueSelectEvent.class, listener, VALUE_SELECTION_EVENT);
    }

    public void removeListener(ValueSelectListener listener) {
        removeListener(ValueSelectEvent.class, listener, VALUE_SELECTION_EVENT);
    }

    protected void fireValueSelect(Long key) {
        fireEvent(new KnockoutChart.ValueSelectEvent(this, key));
    }
}
