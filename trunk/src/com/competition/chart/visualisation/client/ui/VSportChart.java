package com.competition.chart.visualisation.client.ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.competition.chart.visualisation.client.ui.canvas.client.Canvas;
import com.google.gwt.dom.client.Document;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
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

    private final Integer pixelWidth = null;

    private final AbsolutePanel displayPanel;
    private final Canvas canvas;

    private final VPaintGroup paint;

    private final List<VGroup> groups = new LinkedList<VGroup>();
    private VGroup finalBout, winner;
    // private String winner = null;
    private int onLeft, onRight;
    private final Map<Integer, Integer> amounts = new HashMap<Integer, Integer>();

    private List<HTML> names = new LinkedList<HTML>();
    private int offsetLeft = 15;
    private int offsetTop = 15;
    private List<VGroup> drawnGroups = new LinkedList<VGroup>();

    /**
     * The constructor should first call super() to initialize the component and
     * then handle any initialization relevant to Vaadin.
     */
    public VSportChart() {
        setElement(Document.get().createDivElement());
        setWidth("100%");
        setHeight("100%");

        setStyleName(CLASSNAME);
        displayPanel = new AbsolutePanel();
        getElement().appendChild(displayPanel.getElement());
        DOM.setStyleAttribute(displayPanel.getElement(), "position", "relative");
        DOM.setStyleAttribute(displayPanel.getElement(), "zIndex", "9000");

        canvas = new Canvas(100, 100);
        displayPanel.add(canvas, 0, 0);
        setCanvasWidth(1400);
        setCanvasHeight(700);

        paint = new VPaintGroup(canvas, displayPanel, names);
    }

    /**
     * Called whenever an update is received from the server
     */
    public void updateFromUIDL(final UIDL uidl,
            final ApplicationConnection client) {

        if (client.updateComponent(this, uidl, true)) {
            return;
        }

        this.client = client;
        paintableId = uidl.getId();

        if (uidl.hasAttribute("data")) {
            groups.clear();
            final ValueMap map = uidl.getMapAttribute("data");

            for (final String req2 : map.getKeySet()) {
                final VGroup group = new VGroup(req2);

                final String[] persons = map.getString(req2).split(";");

                for (final String s : persons) {
                    final String[] personData = s.split("_");
                    final VPerson person = new VPerson(personData[0],
                            Integer.parseInt(personData[1]));
                    group.addName(person);
                }
                groups.add(group);
            }
            Collections.sort(groups, new Comparator<VGroup>() {
                public int compare(final VGroup o1, final VGroup o2) {
                    return o1.getNumber() == o2.getNumber() ? 0 : o1
                            .getNumber() < o2.getNumber() ? -1 : 1;
                }
            });
        }

        if (uidl.hasAttribute("winner")) {
            winner = new VGroup("99_ ");
            winner.addName(new VPerson(uidl.getStringAttribute("winner"), 99));
        } else {
            winner = null;
        }

        if (!groups.isEmpty()) {
            groupPositions();
            try {
                buildChildGroups();
            } catch (Exception e) {
                Window.alert("ChildGroups " + e.toString());
            }
            try {
                calculateChildPositions();
            } catch (Exception e) {
                Window.alert("ChiuldPositions " + e.toString());
            }
            try {
                drawChart();
            } catch (Exception e) {
                Window.alert("DRAW" + e.toString());
            }
        }
        if (uidl.hasAttribute("width")) {
            setCanvasWidth(uidl.getIntAttribute("width"));
        }
        if (uidl.hasAttribute("height")) {
            setCanvasHeight(uidl.getIntAttribute("height"));
        }
    }

    private void groupPositions() {
        int top = 35;
        onLeft = groups.size() / 2 + groups.size() % 2;
        for (int i = 0; i < groups.size(); i++) {
            VGroup group = groups.get(i);
            if (i == onLeft) {
                top = 35;
            }
            top = group.calculatePosition(top);
        }
    }

    private void buildChildGroups() {
        amounts.put(0, groups.size());
        int nextID = groups.get(groups.size() - 1).getNumber() + 1;
        onLeft = groups.size() / 2 + groups.size() % 2;
        onRight = groups.size() - onLeft;
        int n = onLeft;
        int tier = 1;
        int parent = 0;
        VGroup lastChild = groups.get(0);
        List<VGroup> targetGroup = groups;
        List<VGroup> childGroup = new LinkedList<VGroup>();
        while (n > 1) {
            n = n / 2 + n % 2;
            for (int i = 0; i < n; i++) {
                final VGroup child = new VGroup(nextID + "_ _" + tier);
                targetGroup.get(parent).setChildGroup(child);
                addAdvanced(child, targetGroup.get(parent).getNames());
                child.addParent(targetGroup.get(parent++));
                if (onLeft > parent) {
                    targetGroup.get(parent).setChildGroup(child);
                    addAdvanced(child, targetGroup.get(parent).getNames());
                    child.addParent(targetGroup.get(parent++));
                }
                fillGroup(child);
                childGroup.add(child);
                lastChild = child;
                nextID++;
                if (amounts.get(tier) == null) {
                    amounts.put(tier, 1);
                } else {
                    amounts.put(tier, amounts.get(tier) + 1);
                }
            }
            targetGroup = childGroup;
            childGroup = new LinkedList<VGroup>();
            parent = 0;
            tier++;
        }
        if (n == 1) {
            finalBout = new VGroup(nextID + "_final_" + tier);
            lastChild.setChildGroup(finalBout);
            finalBout.addParent(lastChild);
            addAdvanced(finalBout, lastChild.getNames());
            amounts.put(tier, 1);
        }
        n = onRight;
        parent = onLeft;
        tier = 1;
        targetGroup = groups;
        childGroup = new LinkedList<VGroup>();
        while (n > 1) {
            n = n / 2 + n % 2;
            for (int i = 0; i < n; i++) {
                final VGroup child = new VGroup(nextID + "_ _" + tier);
                targetGroup.get(parent).setChildGroup(child);
                addAdvanced(child, targetGroup.get(parent).getNames());
                child.addParent(targetGroup.get(parent++));
                if (targetGroup.size() > parent) {
                    targetGroup.get(parent).setChildGroup(child);
                    addAdvanced(child, targetGroup.get(parent).getNames());
                    child.addParent(targetGroup.get(parent++));
                }
                fillGroup(child);
                childGroup.add(child);
                lastChild = child;
                nextID++;
                if (amounts.get(tier) == null) {
                    amounts.put(tier, 1);
                } else {
                    amounts.put(tier, amounts.get(tier) + 1);
                }
            }
            targetGroup = childGroup;
            childGroup = new LinkedList<VGroup>();
            parent = 0;
            tier++;
        }
        if (n == 1 || onRight == 1) {
            if (onRight == 1) {
                final VGroup parentGroup = groups.get(parent);
                parentGroup.setChildGroup(finalBout);
                finalBout.addParent(parentGroup);
                addAdvanced(finalBout, parentGroup.getNames());
            } else {
                lastChild.setChildGroup(finalBout);
                finalBout.addParent(lastChild);
                addAdvanced(finalBout, lastChild.getNames());
            }
        }
        fillGroup(finalBout);
    }

    private void calculateChildPositions() {
        List<VGroup> allGroups = new LinkedList<VGroup>();

        for (VGroup group : groups) {
            VGroup child = group;
            while (child.getChildGroup() != null) {
                child = child.getChildGroup();
                if (!allGroups.contains(child)) {
                    allGroups.add(child);
                }
            }
        }

        while (true) {
            if (calculatePosition(allGroups)) {
                break;
            }
        }

    }

    private boolean calculatePosition(List<VGroup> allGroups) {
        List<VGroup> remove = new ArrayList<VGroup>();

        for (VGroup group : allGroups) {
            boolean allParentsHavePosition = true;
            for (VGroup parent : group.getParents()) {
                if (!parent.hasPosition()) {
                    allParentsHavePosition = false;
                }
            }
            if (allParentsHavePosition) {
                int bottom = Integer.MAX_VALUE;
                int top = 0;
                for (VGroup parent : group.getParents()) {
                    if (parent.getBottom() < bottom) {
                        bottom = parent.getBottom();
                    }
                    if (parent.getTop() > top) {
                        top = parent.getTop();
                    }
                }
                group.calculatePositionFromMiddle(bottom + (top - bottom) / 2);
                if (winner != null && group == finalBout) {
                    winner.calculatePosition(group.getTop() - 40);
                }
                remove.add(group);
            }
        }

        allGroups.removeAll(remove);

        return allGroups.isEmpty();
    }

    private void drawChart() {
        for (final HTML name : names) {
            displayPanel.remove(name);
        }
        drawnGroups.clear();
        names.clear();

        canvas.clear();

        offsetLeft = 15;

        // final VGroup lastDrawnChild = null;
        for (int j = 0; j < onLeft; j++) {
            final VGroup g = groups.get(j);
            paint.left(g, offsetLeft);
            drawnGroups.add(g);
            drawChild(g.getChildGroup());
        }
        offsetLeft = finalLeft + 90;
        offsetTop = finalTop + 20;
        drawParent(finalBout);
        drawnGroups.clear();
    }

    private int finalLeft, finalTop;
    Map<Integer, Boolean> drawTier = new HashMap<Integer, Boolean>();

    private void drawChild(final VGroup childGroup) {
        if (drawnGroups.contains(childGroup)) {
            return;
        }
        final int oldOffset = offsetLeft;
        final int oldTopsett = offsetTop;

        final int groupSize = (childGroup.getNames().size() * 20) / 2;
        offsetLeft += 130;
        offsetTop = offsetTop - groupSize - 10;
        if (childGroup == finalBout) {
            offsetLeft -= 10;
            offsetTop -= 30;
            finalLeft = offsetLeft;
            finalTop = offsetTop;

            paint.finalBout(finalBout, offsetLeft);
            if (winner != null) {
                paint.winner(winner, finalBout, offsetLeft);
            }
        } else {
            paint.left(childGroup, offsetLeft);
        }
        drawnGroups.add(childGroup);
        if (childGroup.getChildGroup() != null) {
            drawChild(childGroup.getChildGroup());
        }
        offsetLeft = oldOffset;
        offsetTop = oldTopsett;
    }

    private void drawParent(final VGroup childGroup) {
        offsetLeft += 30;
        if (childGroup == finalBout) {
            final int groupSize = (childGroup.getNames().size() * 20) / 2;
            offsetTop = offsetTop - groupSize + 20;
        }
        boolean first = true;
        for (final VGroup parent : childGroup.getParents()) {

            if (!drawnGroups.contains(parent)) {
                if (childGroup.getParents().size() == 2
                        && childGroup != finalBout) {
                    int oldTopsett = offsetTop;
                    if (first) {
                        offsetTop -= (parent.getNames().size() - childGroup
                                .getNames().size())
                                * 10
                                + (parent.getNames().size() * 20);
                    }
                    paint.right(parent, offsetLeft);
                    if (first) {
                        offsetTop = oldTopsett + 20;
                    }
                    first = false;
                } else {
                    paint.right(parent, offsetLeft);
                }
                if (parent.getParents() != null) {
                    offsetLeft += 100;
                    drawParent(parent);
                    offsetLeft -= 130;
                }
            }
        }
    }

    public static boolean hasAdvance(final List<VPerson> persons, int toTier) {
        for (final VPerson p : persons) {
            if (p.advancedTo() >= toTier) {
                return true;
            }
        }
        return false;
    }

    private void addAdvanced(final VGroup child, final List<VPerson> persons) {
        for (final VPerson p : persons) {
            if (p.advancedTo() >= child.getTier()) {
                child.addName(p);
            }
        }
    }

    private void fillGroup(VGroup group) {
        if (group.getNames().isEmpty()) {
            group.addName(new VPerson("", 0));
        }
    }

    /**
     * Sets the canvas width
     * 
     * @param width
     *            The width in pixels
     */
    public void setCanvasWidth(final int width) {
        canvas.setWidth(width);
        displayPanel.setWidth(width + "px");
    }

    /**
     * Setst the canvas height
     * 
     * @param height
     *            The height in pixels
     */
    public void setCanvasHeight(final int height) {
        canvas.setHeight(height);
        displayPanel.setHeight(height + "px");
    }

    public int getWidgetWidth() {
        if (pixelWidth != null) {
            return pixelWidth;
        }
        try {
            final int width = Integer.parseInt(DOM.getAttribute(getElement(),
                    "width").replaceAll("px", ""));
            return width;
        } catch (final Exception e) {
            try {
                final int width = Integer.parseInt(DOM.getStyleAttribute(
                        getElement(), "width").replaceAll("px", ""));
                return width;
            } catch (final Exception f) {
                return getOffsetWidth();
            }
        }
    }
}
