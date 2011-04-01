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
import com.google.gwt.user.client.Event;
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

    private List<HTML> names = new LinkedList<HTML>();

    private int offsetLeft = 15;

    private List<VGroup> drawnGroups = new LinkedList<VGroup>();
    private boolean onlyLeft;

    boolean mouseDown = false;
    boolean enableDragging = false;

    private int width, height;

    /**
     * The constructor should first call super() to initialize the component and
     * then handle any initialization relevant to Vaadin.
     */
    public VSportChart() {
        setElement(Document.get().createDivElement());
        setWidth("100%");
        setHeight("100%");

        sinkEvents(Event.MOUSEEVENTS);

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

        if (uidl.hasAttribute("allOnLeft")) {
            onlyLeft = true;
        } else {
            onlyLeft = false;
        }

        if (uidl.hasAttribute("enableDrag")) {
            enableDragging = true;
        } else {
            enableDragging = false;
        }

        if (uidl.hasAttribute("data")) {
            groups.clear();
            final ValueMap map = uidl.getMapAttribute("data");

            for (final String req2 : map.getKeySet()) {
                final VGroup group = new VGroup(req2);

                final String[] persons = map.getString(req2).split(";");

                for (final String s : persons) {
                    final String[] personData = s.split("_");
                    final VPerson person = new VPerson(
                            Long.parseLong(personData[0]), personData[1],
                            Integer.parseInt(personData[2]));
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
            if (onlyLeft) {
                positionsLeft();
                buildChildGroupsFromLeft();
                calculateChildPositions();
                drawLeftChart();
            } else {
                groupPositions();
                buildChildGroups();
                calculateChildPositions();
                drawChart();
            }
        }
    }

    private void groupPositions() {
        float top = 35;

        onLeft = groups.size() / 2 + groups.size() % 2;
        onRight = groups.size() - onLeft;

        for (int i = 0; i < groups.size(); i++) {
            VGroup group = groups.get(i);
            if (i == onLeft) {
                top = 35;
            }
            top = group.calculatePosition(top);
            group.setLeftSide(offsetLeft);
        }
    }

    private void positionsLeft() {
        float top = 35;

        for (int i = 0; i < groups.size(); i++) {
            VGroup group = groups.get(i);
            top = group.calculatePosition(top);
            group.setLeftSide(offsetLeft);
        }
    }

    private void buildChildGroupsFromLeft() {
        int nextID = groups.get(groups.size() - 1).getNumber() + 1;
        int n = groups.size();
        int tier = 1;
        int parent = 0;
        VGroup lastChild = groups.get(0);
        List<VGroup> targetGroup = groups;
        List<VGroup> childGroup = new LinkedList<VGroup>();

        while (n > 1) {
            offsetLeft += 130;
            n = n / 2 + n % 2;
            for (int i = 0; i < n; i++) {
                final VGroup child = new VGroup(nextID + "_ _" + tier);
                targetGroup.get(parent).setChildGroup(child);
                addAdvanced(child, targetGroup.get(parent).getNames());
                child.addParent(targetGroup.get(parent++));
                if (groups.size() > parent) {
                    targetGroup.get(parent).setChildGroup(child);
                    addAdvanced(child, targetGroup.get(parent).getNames());
                    child.addParent(targetGroup.get(parent++));
                }
                child.setLeftSide(offsetLeft);
                fillGroup(child);
                childGroup.add(child);
                lastChild = child;
                nextID++;
            }
            targetGroup = childGroup;
            childGroup = new LinkedList<VGroup>();
            parent = 0;
            tier++;
        }
        if (n == 1) {
            offsetLeft += 120;
            finalBout = new VGroup(nextID + "_ _" + tier);
            lastChild.setChildGroup(finalBout);
            finalBout.addParent(lastChild);
            addAdvanced(finalBout, lastChild.getNames());
            finalBout.setLeftSide(offsetLeft);
        }
        fillGroup(finalBout);
    }

    private void buildChildGroups() {
        int nextID = groups.get(groups.size() - 1).getNumber() + 1;

        int n = onLeft;
        int tier = 1;
        int parent = 0;
        VGroup lastChild = groups.get(0);
        List<VGroup> targetGroup = groups;
        List<VGroup> childGroup = new LinkedList<VGroup>();

        while (n > 1) {
            offsetLeft += 130;
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
                child.setLeftSide(offsetLeft);
                fillGroup(child);
                childGroup.add(child);
                lastChild = child;
                nextID++;
            }
            targetGroup = childGroup;
            childGroup = new LinkedList<VGroup>();
            parent = 0;
            tier++;
        }
        if (n == 1) {
            offsetLeft += 120;
            finalBout = new VGroup(nextID + "_ _" + tier);
            lastChild.setChildGroup(finalBout);
            finalBout.addParent(lastChild);
            addAdvanced(finalBout, lastChild.getNames());
            finalBout.setLeftSide(offsetLeft);
        }

        offsetLeft += tier * 130;
        offsetLeft -= 10;
        n = onRight;
        parent = onLeft;
        tier = 1;
        targetGroup = groups;
        childGroup = new LinkedList<VGroup>();
        for (int i = parent; i < groups.size(); i++) {
            targetGroup.get(i).setLeftSide(offsetLeft);
        }

        while (n > 1) {
            offsetLeft -= 130;
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
                child.setLeftSide(offsetLeft);
                fillGroup(child);
                childGroup.add(child);
                lastChild = child;
                nextID++;
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
        if (hasAdvance(finalBout.getNames(), tier + 1)) {
            winner = new VGroup("99_ _" + (tier + 1));
            addAdvanced(winner, finalBout.getNames());
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
                float bottom = Float.MAX_VALUE;
                float top = 0;
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
                    winner.setLeftSide(finalBout.getLeftSide());
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

        for (int j = 0; j < onLeft; j++) {
            final VGroup group = groups.get(j);
            group.setLeftSide(offsetLeft);
            paint.left(group);
            drawnGroups.add(group);
            drawChild(group.getChildGroup());
        }
        offsetLeft = finalLeft + 90;
        drawParent(finalBout);
    }

    private void drawLeftChart() {
        for (final HTML name : names) {
            displayPanel.remove(name);
        }
        drawnGroups.clear();
        names.clear();

        canvas.clear();

        for (VGroup group : groups) {
            paint.left(group);
            drawnGroups.add(group);
            if (group.getChildGroup() != null) {
                drawChild(group.getChildGroup());
            }
        }
    }

    private int finalLeft;
    Map<Integer, Boolean> drawTier = new HashMap<Integer, Boolean>();

    private void drawChild(final VGroup childGroup) {
        if (drawnGroups.contains(childGroup)) {
            return;
        }

        if (childGroup == finalBout) {
            paint.finalBout(finalBout);
            if (winner != null) {
                paint.winner(winner, finalBout);
            }
        } else {
            paint.left(childGroup);
        }
        drawnGroups.add(childGroup);
        if (childGroup.getChildGroup() != null) {
            drawChild(childGroup.getChildGroup());
        }
    }

    private void drawParent(final VGroup childGroup) {
        offsetLeft += 30;
        for (final VGroup parent : childGroup.getParents()) {

            if (!drawnGroups.contains(parent)) {
                if (childGroup.getParents().size() == 2
                        && childGroup != finalBout) {
                    paint.right(parent);

                } else {
                    paint.right(parent);
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
            group.addName(new VPerson(-1, "", 0));
        }
        if (group.getNames().size() == 1 && group != finalBout) {
            group.addName(new VPerson(-1, "", 0));
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
        setWidth(width + "px");
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
        setHeight(height + "px");
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

    int xDown = 0, yDown = 0;

    @Override
    public void onBrowserEvent(Event event) {
        if (paintableId == null || client == null || !enableDragging) {
            return;
        }

        switch (DOM.eventGetType(event)) {
        case Event.ONMOUSEDOWN: {
            mouseDown = true;
            xDown = event.getClientX();
            yDown = event.getClientY();
            break;
        }
        case Event.ONMOUSEMOVE: {
            if (!mouseDown) {
                break;
            }
            int xChange = event.getClientX() - xDown;
            int yChange = event.getClientY() - yDown;

            xDown = event.getClientX();
            yDown = event.getClientY();
            for (VGroup group : drawnGroups) {
                group.calculatePosition(group.getTop() + yChange);
                group.setLeftSide(group.getLeftSide() + xChange);
            }
            if (onlyLeft) {
                drawLeftChart();
            } else {
                drawChart();
            }
            break;
        }
        case Event.ONMOUSEOUT:
        case Event.ONMOUSEUP: {
            mouseDown = false;
        }
        }
    }
}
