package com.competition.chart.visualisation.client.ui;

import java.util.LinkedList;
import java.util.List;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.DOM;
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

    private List<VGroup> groups = new LinkedList<VGroup>();
    private String winner = null;

    private int tiers = 0;

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
        }

        if (!groups.isEmpty()) {
            buildChart();
        }
        if (uidl.hasAttribute("winner")) {
            winner = uidl.getStringAttribute("winner");
        }
    }

    int leftGroups;
    int rightGroups;

    private void buildChart() {
        for (int i = getElement().getChildCount(); i > 0; i--) {
            getElement().removeChild(getElement().getChild(i - 1));
        }

        int half = (int) Math.ceil(groups.size() / 2.0);

        Element leftGroup = DOM.createDiv();
        leftGroup.setClassName("float_left");
        
        getElement().appendChild(leftGroup);
        
        Element winner = DOM.createDiv();
        winner.setClassName("float_left");
        Element rightGroup = DOM.createDiv();
        rightGroup.setClassName("float_left");

        Element winnername = DOM.createDiv();
        winnername.setClassName("competition");
        winnername.addClassName("top");
        if (this.winner != null) {
            winnername.setInnerHTML(this.winner);
        }
        winner.appendChild(winnername);

        int left, right;
        left = right = 0;

        for (VGroup g : groups) {
            double groupHeight = g.getNames().size() * 20 + 20;

            Element group = DOM.createDiv();
            group.setInnerHTML(g.getName());
            group.getStyle().setHeight(groupHeight, Unit.PX);

            Element persons = DOM.createDiv();
            Element connects = DOM.createDiv();
            connects.setClassName("float_" + (half > 0 ? "left" : "right"));
            persons.setClassName("float_" + (half > 0 ? "left" : "right"));

            Element lastConnect = null;
            int groupSize = g.getNames().size();
            for (int i = 0; i < groupSize; i++) {
                VPerson person = g.getNames().get(i);
                Element name = DOM.createDiv();
                name.setClassName("competition");
                name.setInnerHTML(person.getName());
                Element connect = DOM.createDiv();
                if (person.hasAdvanced) {
                    connect.setClassName("advanced");
                } else {
                    connect.setClassName("connect");
                }
                if (half > 0) {
                    connect.addClassName("left");
                } else {
                    connect.addClassName("right");
                    // name.addClassName("right");
                }
                if (i == 0) {
                    name.addClassName("first");
                    connect.addClassName("top");
                }

                if (i > 0 && i < groupSize - 1) {
                    if (person.hasAdvanced) {
                        connect.setClassName("advanced-middle");
                    } else {
                        connect.setClassName("connect-middle");
                    }
                    if (half > 0) {
                        connect.addClassName("left");
                    } else {
                        connect.addClassName("right");
                    }
                    lastConnect.getStyle().setHeight(20, Unit.PX);
                }
                lastConnect = connect;
                if (i == groupSize - 1 && groupSize > 2) {
                    connect.getStyle().setHeight(20, Unit.PX);
                }
                persons.appendChild(name);
                connects.appendChild(connect);
            }

            Element wrap = DOM.createDiv();
            wrap.appendChild(persons);
            wrap.appendChild(connects);
            group.appendChild(wrap);
            if (half > 0) {
                leftGroup.appendChild(group);
                left += g.getNames().size() * 20 + 20;
            } else {
                group.addClassName("right");
                rightGroup.appendChild(group);
                right += g.getNames().size() * 20 + 20;
            }
            half--;
            if (half == 0) {
                // Draw Left Tier 2
                int leftGroups = (int) Math.ceil(groups.size() / 2.0);
                if (leftGroups > 1) {
                    Element top = DOM.createDiv();
                    top.getStyle().setHeight(
                            10 + groups.get(0).getNames().size() * 10, Unit.PX);
                    top.getStyle().setTop(
                            20 + groups.get(0).getNames().size() * 10
                                    - groups.get(0).getNames().size(), Unit.PX);
                    top.addClassName("connect");
                    top.addClassName("left");
                    top.addClassName("top");

                    Element bottom = DOM.createDiv();
                    bottom.getStyle().setHeight(
                            10 + groups.get(0).getNames().size() * 10, Unit.PX);
                    bottom.getStyle().setTop(
                            20 + groups.get(0).getNames().size() * 10
                                    - groups.get(0).getNames().size(), Unit.PX);
                    bottom.addClassName("connect");
                    bottom.addClassName("left");
                    Element wrapper = DOM.createDiv();
                    wrapper.appendChild(top);
                    wrapper.appendChild(bottom);
                    wrapper.setClassName("float_left");
                    getElement().appendChild(wrapper);
                }
            }
        }

        if (left > right) {
            rightGroup.getStyle().setPaddingTop((left - right) / 2, Unit.PX);
        }
        winner.getStyle().setPaddingTop(left / 2, Unit.PX);

        getElement().appendChild(winner);
        getElement().appendChild(rightGroup);
    }
}
