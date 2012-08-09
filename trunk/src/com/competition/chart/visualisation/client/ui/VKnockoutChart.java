package com.competition.chart.visualisation.client.ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Touch;
import com.google.gwt.event.dom.client.TouchEvent;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;
import com.vaadin.terminal.gwt.client.ApplicationConnection;
import com.vaadin.terminal.gwt.client.Paintable;
import com.vaadin.terminal.gwt.client.UIDL;
import com.vaadin.terminal.gwt.client.VConsole;
import com.vaadin.terminal.gwt.client.ValueMap;

public class VKnockoutChart extends Widget implements Paintable {

	/** Set the CSS class name to allow styling. */
	public static final String CLASSNAME = "v-sportchart";

	/** The client side widget identifier */
	protected String paintableId;

	/** Reference to the server connection object. */
	ApplicationConnection client;

	private final Integer pixelWidth = null;

	private final Canvas canvas;

	private final VPaintGroup paint;

	private final List<VGroup> groups = new LinkedList<VGroup>();
	private VGroup finalBout, winner;
	private int onLeft, onRight;

	private final List<HTML> names = new LinkedList<HTML>();

	private int offsetLeft = 15;

	private final List<VGroup> drawnGroups = new LinkedList<VGroup>();
	private boolean onlyLeft = false;

	public static int BOX_WIDTH = 150;

	private boolean mouseDown = false;
	private boolean mouseMoved = false;
	private boolean enableDragging = false;

	private int width, height;

	/**
	 * The constructor should first call super() to initialize the component and
	 * then handle any initialization relevant to Vaadin.
	 */
	public VKnockoutChart() {
		setElement(Document.get().createDivElement());
		setWidth("100%");
		setHeight("100%");

		setStyleName(CLASSNAME);

		canvas = Canvas.createIfSupported();
		if (canvas != null) {
			getElement().appendChild(canvas.getElement());
			canvas.setVisible(true);
			canvas.setCoordinateSpaceWidth(50);
			canvas.setCoordinateSpaceHeight(50);
		} else {
			getElement().setInnerHTML("Canvas not supported");
		}

		if (TouchEvent.isSupported()) {
			sinkEvents(Event.TOUCHEVENTS);
		}
		sinkEvents(Event.MOUSEEVENTS);

		paint = new VPaintGroup(canvas, names);
	}

	/**
	 * Called whenever an update is received from the server
	 */
	public void updateFromUIDL(final UIDL uidl, final ApplicationConnection client) {

		if (client.updateComponent(this, uidl, true) || canvas == null) {
			return;
		}

		this.client = client;
		paintableId = uidl.getId();

		if (uidl.hasAttribute("width")) {
			final String widthString = uidl.getStringAttribute("width");
			width = Integer.parseInt(widthString.substring(0, widthString.indexOf("px")));

			// Percentual width
			if (uidl.hasAttribute("widthpercentage")) {
				width = getElement().getParentElement().getClientWidth();
			}

			setCanvasWidth(width);
		}
		if (uidl.hasAttribute("height")) {
			final String heightString = uidl.getStringAttribute("height");
			height = Integer.parseInt(heightString.substring(0, heightString.indexOf("px")));

			// Percentual height
			if (uidl.hasAttribute("heightpercentage")) {
				height = getElement().getParentElement().getClientHeight();
			}

			setCanvasHeight(height);
		}

		if (uidl.hasAttribute("allOnLeft")) {
			if (!onlyLeft) {
				groups.clear();
			}
			onlyLeft = true;
		} else {
			if (onlyLeft) {
				groups.clear();
			}
			onlyLeft = false;
		}

		if (uidl.hasAttribute("enableDrag")) {
			enableDragging = true;
		} else {
			enableDragging = false;
		}

		if (uidl.hasAttribute("boxWidth")) {
			BOX_WIDTH = uidl.getIntAttribute("boxWidth");
			paint.setBoxWidth(BOX_WIDTH);
		}

		boolean newData = false;
		if (uidl.hasAttribute("data")) {
			final ValueMap map = uidl.getMapAttribute("data");

			for (final VGroup group : groups) {
				if (!map.getKeySet().contains(group.getId()) && !newData) {
					newData = true;
					break;
				}
			}
			if (groups.isEmpty() || uidl.hasAttribute("resetPositions")) {
				newData = true;
			}

			if (newData) {
				groups.clear();
			}

			for (final String req2 : map.getKeySet()) {
				final VGroup group = getGroup(req2);

				final String[] persons = map.getString(req2).split(";");

				for (final String s : persons) {
					final String[] personData = s.split("_");
					final VPerson person = new VPerson(Long.parseLong(personData[0]), personData[1], Integer.parseInt(personData[2]));
					group.addName(person);
				}
			}
			Collections.sort(groups, new Comparator<VGroup>() {
				public int compare(final VGroup o1, final VGroup o2) {
					return o1.getNumber() == o2.getNumber() ? 0 : o1.getNumber() < o2.getNumber() ? -1 : 1;
				}
			});

		}

		if (!groups.isEmpty()) {
			if (newData) {
				offsetLeft = 15;
			} else {
				offsetLeft = (int) groups.get(0).getLeftSide();
			}
			if (onlyLeft) {
				winner = null;
				positionsLeft(newData);
				buildChildGroupsFromLeft();
				calculateChildPositions();
				drawLeftChart();
			} else {
				groupPositions(newData);
				buildChildGroups();
				calculateChildPositions();
				drawChart();
			}
		}
	}

	/**
	 * Get group if exists or create new one if one doesn't
	 * 
	 * @param id
	 * @return
	 */
	private VGroup getGroup(final String id) {
		for (final VGroup g : groups) {
			if (g.getId().equals(id)) {
				return g;
			}
		}

		final VGroup group = new VGroup(id);
		groups.add(group);

		return group;
	}

	private void groupPositions(final boolean calculateTop) {
		float top = 35;

		onLeft = groups.size() / 2 + groups.size() % 2;
		onRight = groups.size() - onLeft;

		for (int i = 0; i < groups.size(); i++) {
			final VGroup group = groups.get(i);
			if (i == onLeft) {
				if (onLeft > onRight) {
					top = 25 + getLeftRightDifference();
					if (onLeft % 2 == 1) {
						final int names = groups.get(onLeft - 1).getNames().size();
						if (names > 2) {
							top -= (names - 2) * 15;
						} else if (names == 1) {
							top += 15;
						}
					}
				} else {
					top = 35;
				}
			}
			if (calculateTop) {
				top = group.calculatePosition(top);
			}
			group.setLeftSide(offsetLeft);
		}
	}

	// gets the difference between left and right side so that we get the right
	// side top offset to match left side center
	private int getLeftRightDifference() {
		int left = 0;
		int right = 0;
		for (int i = 0; i < groups.size(); i++) {
			if (i < onLeft) {
				left++;
				left += groups.get(i).getNames().size();
			} else {
				right++;
				right += groups.get(i).getNames().size();
			}
		}
		return 20 * (left - right);
	}

	private void positionsLeft(final boolean calculateTop) {
		float top = 35;

		for (int i = 0; i < groups.size(); i++) {
			final VGroup group = groups.get(i);
			if (calculateTop) {
				top = group.calculatePosition(top);
			}
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
			offsetLeft += BOX_WIDTH + 30;
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
		if (n == 1) {
			offsetLeft += BOX_WIDTH + 20;
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
		int maxTier = 1;
		int tier = 1;
		int parent = 0;
		VGroup lastChild = groups.get(0);
		List<VGroup> targetGroup = groups;
		List<VGroup> childGroup = new LinkedList<VGroup>();

		while (n > 1) {
			offsetLeft += BOX_WIDTH + 30;
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
			offsetLeft += BOX_WIDTH + 20;
			finalBout = new VGroup(nextID + "_ _" + tier);
			lastChild.setChildGroup(finalBout);
			finalBout.addParent(lastChild);
			addAdvanced(finalBout, lastChild.getNames());
			finalBout.setLeftSide(offsetLeft);
		}

		maxTier = tier;
		int tiers = tier;
		if (onRight == 2) {
			tiers--;
		}
		offsetLeft += tiers * (BOX_WIDTH + 30);
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
			offsetLeft -= BOX_WIDTH + 30;
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
		// if right side has less groups than left side.
		if (maxTier != tier) {
			// for (VGroup parentGroup : finalBout.getParents()) {
			if (lastChild.getLeftSide() > finalBout.getLeftSide()) {
				for (final VPerson p : lastChild.getNames()) {
					if (p.advancedTo() == tier) {
						p.advancedToTier++;
					} else if (p.advancedTo() == tier + 1) {
						p.advancedToTier += 2;
					}
				}
			}
		}
		// }
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
		if (hasAdvance(finalBout.getNames(), maxTier + 1)) {
			winner = new VGroup("99_ _" + (maxTier + 1));
			addAdvanced(winner, finalBout.getNames());
		}
		fillGroup(finalBout);
	}

	private void calculateChildPositions() {
		final List<VGroup> allGroups = new LinkedList<VGroup>();

		for (final VGroup group : groups) {
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

	private boolean calculatePosition(final List<VGroup> allGroups) {
		final List<VGroup> remove = new ArrayList<VGroup>();

		for (final VGroup group : allGroups) {
			boolean allParentsHavePosition = true;
			for (final VGroup parent : group.getParents()) {
				if (!parent.hasPosition()) {
					allParentsHavePosition = false;
				}
			}
			if (allParentsHavePosition) {
				float bottom = Float.MAX_VALUE;
				float top = 0;
				for (final VGroup parent : group.getParents()) {
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
					winner.setLeftSide(group.getLeftSide());
					remove.add(winner);
				}
				remove.add(group);
			}
		}

		allGroups.removeAll(remove);

		return allGroups.isEmpty();
	}

	private void drawChart() {
		drawnGroups.clear();
		names.clear();

		canvas.getContext2d().clearRect(0.0, 0.0, canvas.getCoordinateSpaceWidth(), canvas.getCoordinateSpaceWidth());

		for (int j = 0; j < onLeft; j++) {
			final VGroup group = groups.get(j);
			paint.left(group);
			drawnGroups.add(group);
			drawChild(group.getChildGroup());
		}
		drawParent(finalBout);
	}

	private void drawLeftChart() {
		drawnGroups.clear();
		names.clear();

		canvas.getContext2d().clearRect(0.0, 0.0, canvas.getCoordinateSpaceWidth(), canvas.getCoordinateSpaceWidth());

		for (final VGroup group : groups) {
			paint.left(group);
			drawnGroups.add(group);
			if (group.getChildGroup() != null) {
				drawChild(group.getChildGroup());
			}
		}
	}

	Map<Integer, Boolean> drawTier = new HashMap<Integer, Boolean>();

	private void drawChild(final VGroup childGroup) {
		if (drawnGroups.contains(childGroup)) {
			return;
		}

		if (childGroup == finalBout) {
			paint.finalBout(finalBout);
			if (winner != null) {
				paint.winner(winner, finalBout);
				drawnGroups.add(winner);
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
				if (childGroup.getParents().size() == 2 && childGroup != finalBout) {
					paint.right(parent);
				} else {
					paint.right(parent);
				}
				if (parent.getParents() != null) {
					offsetLeft += 100;
					drawParent(parent);
					offsetLeft -= 130;
				}
				drawnGroups.add(parent);
			}
		}
	}

	public static boolean hasAdvance(final List<VPerson> persons, final int toTier) {
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

	private void fillGroup(final VGroup group) {
		if (group.getNames().isEmpty()) {
			group.addName(new VPerson(-1, "", 0));
		}
		if (group.getNames().size() == 1 && group != finalBout && group.getParents().size() > 1) {
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
		VConsole.log("Setting width: " + width + "px");
		canvas.setCoordinateSpaceWidth(width);
		VConsole.log("Canvas width: " + canvas.getCoordinateSpaceWidth());
	}

	/**
	 * Sets the canvas height
	 * 
	 * @param height
	 *            The height in pixels
	 */
	public void setCanvasHeight(final int height) {
		VConsole.log("Setting height: " + height + "px");
		canvas.setCoordinateSpaceHeight(height);
		VConsole.log("Canvas height: " + canvas.getCoordinateSpaceHeight());
	}

	public int getWidgetWidth() {
		if (pixelWidth != null) {
			return pixelWidth;
		}
		try {
			final int width = Integer.parseInt(DOM.getAttribute(getElement(), "width").replaceAll("px", ""));
			return width;
		} catch (final Exception e) {
			try {
				final int width = Integer.parseInt(DOM.getStyleAttribute(getElement(), "width").replaceAll("px", ""));
				return width;
			} catch (final Exception f) {
				return getOffsetWidth();
			}
		}
	}

	float xDown = 0, yDown = 0;

	@Override
	public void onBrowserEvent(final Event event) {
		if (paintableId == null || client == null || !enableDragging) {
			return;
		}

		switch (DOM.eventGetType(event)) {
		case Event.ONMOUSEDOWN:
			mouseDown = true;
			xDown = event.getClientX();
			yDown = event.getClientY();
			Event.setCapture(getElement());
			break;
		case Event.ONMOUSEMOVE:
			if (!mouseDown) {
				break;
			}
			mouseMoved = true;
			float xChange = event.getClientX() - xDown;
			float yChange = event.getClientY() - yDown;

			xDown = event.getClientX();
			yDown = event.getClientY();
			for (final VGroup group : drawnGroups) {
				group.updatePosition(yChange);
				group.setLeftSide(group.getLeftSide() + xChange);
			}
			if (onlyLeft) {
				drawLeftChart();
			} else {
				drawChart();
			}
			break;
		case Event.ONMOUSEUP:
			if (!mouseMoved) {
				mouseMoved = false;
				mouseDown = false;
				final int x = event.getClientX() - getAbsoluteLeft();
				final int y = event.getClientY() - getAbsoluteTop();
				for (final VGroup group : drawnGroups) {
					if (group.getLeftSide() < x && x < group.getLeftSide() + BOX_WIDTH && group.getTop() < y && y < group.getBottom()) {
						try {
							final VPerson person = group.getNames().get((int) (y - group.getTop()) / 20);
							valueSelect(Long.toString(person.getId()));
						} catch (final IndexOutOfBoundsException ioob) {
							Window.alert("Problem finding person");
							// Failed to find user
						}
						break;
					}
				}
			}
			Event.releaseCapture(getElement());
			mouseMoved = false;
			mouseDown = false;
			break;
		case Event.ONTOUCHSTART:
			Touch touch = event.getTargetTouches().get(0);
			xDown = touch.getPageX();
			yDown = touch.getPageY();
			break;
		case Event.ONTOUCHMOVE:
			mouseMoved = true;

			touch = event.getTargetTouches().get(0);
			xChange = touch.getPageX() - xDown;
			yChange = touch.getPageY() - yDown;

			xDown = touch.getPageX();
			yDown = touch.getPageY();

			for (final VGroup group : drawnGroups) {
				group.updatePosition(yChange);
				group.setLeftSide(group.getLeftSide() + xChange);
			}

			if (onlyLeft) {
				drawLeftChart();
			} else {
				drawChart();
			}
			break;
		case Event.ONTOUCHCANCEL:
		case Event.ONTOUCHEND:
			mouseMoved = false;
			break;
		}
	}

	public void valueSelect(final String value) {
		client.updateVariable(paintableId, "selected", value, true);
	}

	// public void onDragEnd(final TouchEvent e) {
	// }
	//
	// public void onDragMove(final TouchEvent e) {

	// }
	//
	// public void onDragStart(final TouchEvent e) {
	// }
	//
	// public void onTouchEnd(final TouchEvent e) {
	//
	// }
	//
	// public boolean onTouchStart(final TouchEvent e) {
	// // Returning true here indicates that we are accepting a drag sequence.
	// return true;
	// }
}
