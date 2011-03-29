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

    private final List<VGroup> groups = new LinkedList<VGroup>();
    private VGroup finalBout, winner;
    // private String winner = null;
    private int onLeft, onRight;
    private final Map<Integer, Integer> amounts = new HashMap<Integer, Integer>();

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
        setCanvasWidth(1400);
        setCanvasHeight(700);
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
            drawChart();
        }
        if (uidl.hasAttribute("winner")) {
            // winner = uidl.getStringAttribute("winner");
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
                remove.add(group);
            }
        }

        allGroups.removeAll(remove);

        return allGroups.isEmpty();
    }

    List<HTML> names = new LinkedList<HTML>();
    int offsetLeft = 15;
    int offsetTop = 15;
    List<VGroup> drawnGroups = new LinkedList<VGroup>();

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
            drawLeft(g, j % 2 != 0, j % 2 == 0);
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
        }

        if (drawTier.get(childGroup.getTier()) == null
                && (amounts.get(childGroup.getTier()) / 2) > 1) {
            drawTier.put(childGroup.getTier(), false);
        }

        if (drawTier.get(childGroup.getTier()) != null) {
            drawLeft(childGroup, drawTier.get(childGroup.getTier()),
                    !drawTier.get(childGroup.getTier()));
        } else {
            drawLeft(childGroup, false, false);
        }
        drawnGroups.add(childGroup);
        if (childGroup.getChildGroup() != null) {
            drawChild(childGroup.getChildGroup());
        }
        offsetLeft = oldOffset;
        offsetTop = oldTopsett;
    }

    private void drawParent(final VGroup childGroup) {
        // final int oldTopsett = offsetTop;

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
                    drawRight(parent, !first, first);
                    if (first) {
                        offsetTop = oldTopsett + 20;
                    }
                    first = false;
                } else {
                    drawRight(parent, false, false);
                }
                if (parent.getParents() != null) {
                    offsetLeft += 100;
                    drawParent(parent);
                    offsetLeft -= 130;
                }
            }
        }
        // offsetTop = oldTopsett;
    }

    private void drawLeft(final VGroup g, final boolean connectUp,
            final boolean connectDown) {
        // drawTier.put(g.getTier(), connectDown);
        canvas.setStrokeStyle("rgb(0,0,0)");
        canvas.setLineWidth(1);
        canvas.beginPath();

        HTML name = new HTML(g.getName());
        displayPanel.add(name, offsetLeft + 10, g.getTop() - 15);
        names.add(name);

        offsetTop = g.getTop();

        canvas.moveTo(offsetLeft, offsetTop);
        for (int i = 0; i < g.getNames().size(); i++) {
            final VPerson p = g.getNames().get(i);

            name = new HTML(p.getName());
            displayPanel.add(name, offsetLeft + 10, offsetTop + 5);
            names.add(name);

            canvas.rect(offsetLeft, offsetTop, 100, 20);

            if (g != finalBout) {
                if (p.advancedTo() >= g.getTier() + 1) {
                    canvas.closePath();
                    canvas.stroke();
                    canvas.setStrokeStyle("rgb(10,255,0)");
                    canvas.beginPath();
                }
                canvas.moveTo(offsetLeft + 100, offsetTop + 10);
                canvas.lineTo(offsetLeft + 110, offsetTop + 10);

                canvas.lineTo(offsetLeft + 110, g.getMiddleOfGroup());

                canvas.moveTo(offsetLeft, offsetTop);
                canvas.closePath();
                canvas.stroke();
                canvas.setStrokeStyle("rgb(0,0,0)");
                canvas.beginPath();
            }
            offsetTop += 20;
        }

        if (g != finalBout) {
            /* next tier */
            if (hasAdvance(g.getNames(), g.getTier() + 1)) {
                canvas.closePath();
                canvas.stroke();
                canvas.setStrokeStyle("rgb(10,255,0)");
                canvas.beginPath();
            }
            canvas.moveTo(offsetLeft + 110, g.getMiddleOfGroup());
            canvas.lineTo(offsetLeft + 120, g.getMiddleOfGroup());

            canvas.lineTo(offsetLeft + 120, g.getChildGroup()
                    .getMiddleOfGroup());

            if (g.getParents().size() > 0) {
                canvas.moveTo(0, 0);
                canvas.closePath();
                canvas.stroke();
                if (g.hasCompetitors()) {
                    canvas.setStrokeStyle("rgb(10,255,0)");
                    canvas.beginPath();
                } else {
                    canvas.beginPath();
                }
                canvas.moveTo(offsetLeft, g.getMiddleOfGroup());
                canvas.lineTo(offsetLeft - 10, g.getMiddleOfGroup());
            }
        }

        canvas.moveTo(0, 0);
        canvas.closePath();
        canvas.stroke();
    }

    private void drawRight(final VGroup g, final boolean connectUp,
            final boolean connectDown) {
        canvas.setStrokeStyle("rgb(0,0,0)");
        canvas.setLineWidth(1);
        canvas.beginPath();

        // offsetTop = g.getTop();

        int offsetTopRight = g.getTop();
        // offsetTop
        // - (g.getNames().size() - g.getChildGroup().getNames().size())
        // * 10;

        HTML name = new HTML(g.getName());
        displayPanel.add(name, offsetLeft + 10, offsetTopRight - 15);
        names.add(name);

        // final int groupOffset = offsetTopRight;
        // offsetTop -= 20;

        final int middleOfGroup = offsetTopRight + (g.getNames().size() * 20)
                / 2;

        canvas.moveTo(offsetLeft, offsetTopRight);
        for (int i = 0; i < g.getNames().size(); i++) {
            final VPerson p = g.getNames().get(i);

            name = new HTML(p.getName());
            displayPanel.add(name, offsetLeft + 10, offsetTopRight + 5);
            names.add(name);

            canvas.rect(offsetLeft, offsetTopRight, 100, 20);

            if (p.advancedTo() >= g.getTier() + 1) {
                canvas.closePath();
                canvas.stroke();
                canvas.setStrokeStyle("rgb(10,255,0)");
                canvas.beginPath();
            }
            canvas.moveTo(offsetLeft, offsetTopRight + 10);
            canvas.lineTo(offsetLeft - 10, offsetTopRight + 10);

            canvas.lineTo(offsetLeft - 10, middleOfGroup);

            canvas.moveTo(offsetLeft, offsetTopRight);
            canvas.closePath();
            canvas.stroke();
            canvas.setStrokeStyle("rgb(0,0,0)");
            canvas.beginPath();

            offsetTopRight += 20;
        }

        /* next tier */
        if (hasAdvance(g.getNames(), g.getTier() + 1)) {
            canvas.closePath();
            canvas.stroke();
            canvas.setStrokeStyle("rgb(10,255,0)");
            canvas.beginPath();
        }
        canvas.moveTo(offsetLeft - 10, middleOfGroup);
        canvas.lineTo(offsetLeft - 20, middleOfGroup);

        // line goes up
        canvas.lineTo(offsetLeft - 20, g.getChildGroup().getMiddleOfGroup());

        if (g.getParents().size() > 0) {
            canvas.moveTo(0, 0);
            canvas.closePath();
            canvas.stroke();
            if (g.hasCompetitors()) {
                canvas.setStrokeStyle("rgb(10,255,0)");
                canvas.beginPath();
            } else {
                canvas.beginPath();
            }
            canvas.moveTo(offsetLeft + 100, middleOfGroup);
            canvas.lineTo(offsetLeft + 110, middleOfGroup);
        }

        canvas.moveTo(0, 0);
        canvas.closePath();
        canvas.stroke();
    }

    private boolean hasAdvance(final List<VPerson> persons, int toTier) {
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
            group.addName(new VPerson("", 0));
        }
        if (group.getNames().size() == 1) {
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
