package com.competition.chart.visualisation.client.ui;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import com.competition.chart.visualisation.client.ui.canvas.client.Canvas;
import com.google.gwt.dom.client.Document;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;
import com.vaadin.terminal.gwt.client.ApplicationConnection;
import com.vaadin.terminal.gwt.client.Paintable;
import com.vaadin.terminal.gwt.client.UIDL;
import com.vaadin.terminal.gwt.client.ValueMap;

public class VSportChart extends Widget implements Paintable {

    /** Set the CSS class name to allow styling. */
    public static final String CLASSNAME = "v-sportchart";

    /** The client side widget identifier */
    protected String paintableId;

    /** Reference to the server connection object. */
    ApplicationConnection client;

    private Integer pixelWidth = null;

    private AbsolutePanel displayPanel;
    private Canvas canvas;

    private List<VGroup> groups = new LinkedList<VGroup>();
    private VGroup finalBout, winner;
    // private String winner = null;
    private int onLeft, onRight;

    /**
     * The constructor should first call super() to initialize the component and
     * then handle any initialization relevant to Vaadin.
     */
    public VSportChart() {
        // TODO Example code is extending GWT Widget so it must set a root
        // element.
        // Change to proper element or remove if extending another widget
        setElement(Document.get().createDivElement());
        setWidth("100%");
        setHeight("100%");
        // This method call of the Paintable interface sets the component
        // style name in DOM tree
        setStyleName(CLASSNAME);
        displayPanel = new AbsolutePanel();
        getElement().appendChild(displayPanel.getElement());
        DOM.setStyleAttribute(displayPanel.getElement(), "position", "relative");
        DOM.setStyleAttribute(displayPanel.getElement(), "zIndex", "9000");

        canvas = new Canvas(100, 100);
        displayPanel.add(canvas, 0, 0);
        setCanvasWidth(700);
        setCanvasHeight(500);
    }

    /**
     * Called whenever an update is received from the server
     */
    public void updateFromUIDL(UIDL uidl, ApplicationConnection client) {
        // This call should be made first.
        // It handles sizes, captions, tooltips, etc. automatically.
        if (client.updateComponent(this, uidl, true)) {
            // If client.updateComponent returns true there has been no changes
            // and we
            // do not need to update anything.
            return;
        }

        // Save reference to server connection object to be able to send
        // user interaction later
        this.client = client;

        // Save the client side identifier (paintable id) for the widget
        paintableId = uidl.getId();

        // StringBuilder str = new StringBuilder();

        if (uidl.hasAttribute("data")) {
            groups.clear();
            ValueMap map = uidl.getMapAttribute("data");

            for (String req2 : map.getKeySet()) {
                VGroup group = new VGroup(req2);

                String[] persons = map.getString(req2).split(";");
                for (String s : persons) {
                    String[] personData = s.split("_");
                    VPerson person = new VPerson(personData[0],
                            personData.length == 2);
                    group.addName(person);
                }
                groups.add(group);
            }
            Collections.sort(groups, new Comparator<VGroup>() {
                public int compare(VGroup o1, VGroup o2) {
                    return o1.getNumber() == o2.getNumber() ? 0 : o1
                            .getNumber() < o2.getNumber() ? -1 : 1;
                }
            });
        }

        if (!groups.isEmpty()) {
            buildGroups();
            drawChart();
        }
        if (uidl.hasAttribute("winner")) {
            // winner = uidl.getStringAttribute("winner");
        }
    }

    private void buildGroups() {
        int nextID = groups.get(groups.size() - 1).getNumber() + 1;
        onLeft = groups.size() / 2 + groups.size() % 2;
        onRight = groups.size() - onLeft;
        int n = onLeft;
        int tier = 1;
        int parent = 0;
        VGroup lastChild = groups.get(0);
        while (n > 1) {
            n = n / 2 + n % 2;
            for (int i = 0; i < n; i++) {
                VGroup child = new VGroup(nextID + "_name_" + tier);
                groups.get(parent).setChildGroup(child);
                addAdvanced(child, groups.get(parent).getNames());
                child.addParent(groups.get(parent++));
                if (onLeft > parent) {
                    groups.get(parent).setChildGroup(child);
                    addAdvanced(child, groups.get(parent).getNames());
                    child.addParent(groups.get(parent++));
                }
                lastChild = child;
                // groups.add(child);
                nextID++;
            }
            tier++;
        }
        if (n == 1) {
            finalBout = new VGroup(nextID + "_final_" + tier);
            lastChild.setChildGroup(finalBout);
            finalBout.addParent(lastChild);
        }

        n = onRight;
        tier = 1;
        while (n > 1) {
            n = n / 2 + n % 2;
            for (int i = 0; i < n; i++) {
                VGroup child = new VGroup(nextID + "_name_" + tier);
                groups.get(parent).setChildGroup(child);
                addAdvanced(child, groups.get(parent).getNames());
                child.addParent(groups.get(parent++));
                if (groups.size() > parent) {
                    groups.get(parent).setChildGroup(child);
                    addAdvanced(child, groups.get(parent).getNames());
                    child.addParent(groups.get(parent++));
                }
                nextID++;
                lastChild = child;
            }
            tier++;
        }
        if (n == 1) {
            finalBout = new VGroup(nextID + "_final_" + tier);
            lastChild.setChildGroup(finalBout);
            finalBout.addParent(lastChild);
        }
    }

    List<HTML> names = new LinkedList<HTML>();
    int offsetLeft = 15;
    int offsetTop = 15;

    private void drawChart() {
        for (HTML name : names) {
            displayPanel.remove(name);
        }
        names.clear();

        canvas.clear();

        offsetLeft = 15;
        offsetTop = 15;
        VGroup lastDrawnChild = null;
        for (int j = 0; j < groups.size(); j++) {
            VGroup g = groups.get(j);
            if (j < onLeft) {
                drawLeft(g, j);
                if (lastDrawnChild == null
                        && lastDrawnChild != g.getChildGroup()) {
                    int oldOffset = offsetLeft;
                    int oldTopsett = offsetTop;
                    VGroup child = g.getChildGroup();
                    int groupSize = (child.getNames().size() * 20) / 2;
                    if (child.getNames().isEmpty()) {
                        groupSize = 20;
                        child.addName(new VPerson("", false));
                        child.addName(new VPerson("", false));
                    }
                    offsetLeft += 130;
                    offsetTop = offsetTop - groupSize - 10;
                    drawLeft(g.getChildGroup(), j);
                    lastDrawnChild = g.getChildGroup();
                    offsetLeft = oldOffset;
                    offsetTop = oldTopsett;
                }
            } else {
                drawRight(lastDrawnChild.getParents().get(0), j);
            }
        }
    }

    private void drawLeft(VGroup g, int j) {
        canvas.setStrokeStyle("rgb(0,0,0)");
        canvas.setLineWidth(1);
        canvas.beginPath();

        HTML name = new HTML(g.getName());
        displayPanel.add(name, offsetLeft + 10, offsetTop + 5);
        names.add(name);

        int groupOffset = offsetTop;

        offsetTop += 20;

        int middleOfGroup = offsetTop + (g.getNames().size() * 20) / 2;
        canvas.moveTo(offsetLeft, offsetTop);
        for (int i = 0; i < g.getNames().size(); i++) {
            VPerson p = g.getNames().get(i);

            name = new HTML(p.getName());
            displayPanel.add(name, offsetLeft + 10, offsetTop + 5);
            names.add(name);

            canvas.rect(offsetLeft, offsetTop, 100, 20);

            if (p.hasAdvanced) {
                canvas.closePath();
                canvas.stroke();
                canvas.setStrokeStyle("rgb(10,255,0)");
                canvas.beginPath();
            }
            canvas.moveTo(offsetLeft + 100, offsetTop + 10);
            canvas.lineTo(offsetLeft + 110, offsetTop + 10);

            canvas.lineTo(offsetLeft + 110, middleOfGroup);

            canvas.moveTo(offsetLeft, offsetTop);
            canvas.closePath();
            canvas.stroke();
            canvas.setStrokeStyle("rgb(0,0,0)");
            canvas.beginPath();

            offsetTop += 20;
        }

        /* next tier */
        if (hasAdvance(g.getNames())) {
            canvas.closePath();
            canvas.stroke();
            canvas.setStrokeStyle("rgb(10,255,0)");
            canvas.beginPath();
        }
        canvas.moveTo(offsetLeft + 110, middleOfGroup);
        canvas.lineTo(offsetLeft + 120, middleOfGroup);

        // line goes up
        if ((j + 1) % 2 == 0) {
            canvas.lineTo(offsetLeft + 120, groupOffset + 10);
            canvas.lineTo(offsetLeft + 130, groupOffset + 10);
        } else if (j != groups.size() - 1 && g.getNumber() != onLeft) {
            canvas.lineTo(offsetLeft + 120, middleOfGroup
                    + (middleOfGroup - groupOffset - 10));
            canvas.lineTo(offsetLeft + 130, middleOfGroup
                    + (middleOfGroup - groupOffset - 10));
        }

        canvas.moveTo(0, 0);
        canvas.closePath();
        canvas.stroke();
    }

    private void drawRight(VGroup g, int j) {
        // if (offsetLeft == 15) {
        // offsetTop = 15;
        // offsetLeft = canvas.getWidth() - 115;
        // }
        canvas.setStrokeStyle("rgb(0,0,0)");
        canvas.setLineWidth(1);
        canvas.beginPath();

        HTML name = new HTML(g.getName());
        displayPanel.add(name, offsetLeft + 10, offsetTop + 5);
        names.add(name);

        int groupOffset = offsetTop;

        offsetTop += 20;

        int middleOfGroup = offsetTop + (g.getNames().size() * 20) / 2;
        canvas.moveTo(offsetLeft, offsetTop);
        for (int i = 0; i < g.getNames().size(); i++) {
            VPerson p = g.getNames().get(i);

            name = new HTML(p.getName());
            displayPanel.add(name, offsetLeft + 10, offsetTop + 5);
            names.add(name);

            canvas.rect(offsetLeft, offsetTop, 100, 20);

            if (p.hasAdvanced) {
                canvas.closePath();
                canvas.stroke();
                canvas.setStrokeStyle("rgb(10,255,0)");
                canvas.beginPath();
            }
            canvas.moveTo(offsetLeft, offsetTop + 10);
            canvas.lineTo(offsetLeft - 10, offsetTop + 10);

            canvas.lineTo(offsetLeft - 10, middleOfGroup);

            canvas.moveTo(offsetLeft, offsetTop);
            canvas.closePath();
            canvas.stroke();
            canvas.setStrokeStyle("rgb(0,0,0)");
            canvas.beginPath();

            offsetTop += 20;
        }

        /* next tier */
        if (hasAdvance(g.getNames())) {
            canvas.closePath();
            canvas.stroke();
            canvas.setStrokeStyle("rgb(10,255,0)");
            canvas.beginPath();
        }
        canvas.moveTo(offsetLeft - 10, middleOfGroup);
        canvas.lineTo(offsetLeft - 20, middleOfGroup);

        // line goes up
        if ((j + onLeft % 2) % 2 == 0) {
            canvas.lineTo(offsetLeft - 20, groupOffset + 10);
        } else if (j != groups.size() - 1 && g.getNumber() != onLeft) {
            canvas.lineTo(offsetLeft - 20, middleOfGroup
                    + (middleOfGroup - groupOffset - 10));
        }

        canvas.moveTo(0, 0);
        canvas.closePath();
        canvas.stroke();
    }

    private boolean hasAdvance(List<VPerson> persons) {
        for (VPerson p : persons) {
            if (p.hasAdvanced) {
                return true;
            }
        }
        return false;
    }

    private void addAdvanced(VGroup child, List<VPerson> persons) {
        for (VPerson p : persons) {
            if (p.hasAdvanced) {
                child.addName(p);
            }
        }
    }

    /**
     * Sets the canvas width
     * 
     * @param width
     *            The width in pixels
     */
    public void setCanvasWidth(int width) {
        canvas.setWidth(width);
        displayPanel.setWidth(width + "px");
    }

    /**
     * Setst the canvas height
     * 
     * @param height
     *            The height in pixels
     */
    public void setCanvasHeight(int height) {
        canvas.setHeight(height);
        displayPanel.setHeight(height + "px");
    }

    public int getWidgetWidth() {
        if (pixelWidth != null) {
            return pixelWidth;
        }
        try {
            int width = Integer.parseInt(DOM
                    .getAttribute(getElement(), "width").replaceAll("px", ""));
            return width;
        } catch (Exception e) {
            try {
                int width = Integer.parseInt(DOM.getStyleAttribute(
                        getElement(), "width").replaceAll("px", ""));
                return width;
            } catch (Exception f) {
                return getOffsetWidth();
            }
        }
    }
}
