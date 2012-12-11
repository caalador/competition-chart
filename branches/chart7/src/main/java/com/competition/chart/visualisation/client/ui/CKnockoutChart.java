package com.competition.chart.visualisation.client.ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.competition.chart.visualisation.client.Competitor;
import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Touch;
import com.google.gwt.event.dom.client.TouchEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Widget;
import com.vaadin.client.VConsole;

public class CKnockoutChart extends Widget {

	/** Set the CSS class name to allow styling. */
	public static final String CLASSNAME = "v-sportchart";

	/** The client side widget identifier */
	protected String paintableId;

	private final Integer pixelWidth = null;

	private final Canvas canvas;

	private final CPaintGroup paint;

	private final List<CGroup> groups = new LinkedList<CGroup>();
	private CGroup finalBout, winner;
	private int onLeft, onRight;

	private int offsetLeft = 15;

	private final List<CGroup> drawnGroups = new LinkedList<CGroup>();
	private boolean onlyLeft = false;

	public static int BOX_WIDTH = 150;

	private boolean mouseDown = false;
	private boolean mouseMoved = false;
	private boolean enableDragging = false;

	private final int width = 0;
	private final int height = 0;

	/**
	 * The constructor should first call super() to initialize the component and
	 * then handle any initialization relevant to Vaadin.
	 */
	public CKnockoutChart() {
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

		paint = new CPaintGroup(canvas);
	}

	public void resetPositions() {
		groups.clear();
	}

	public void allOnLeft(final boolean allOnLeft) {
		if (allOnLeft) {
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
	}

	public void setEnableDragging(final boolean enableDragging) {
		this.enableDragging = enableDragging;
	}

	public static void setBoxWidth(final int boxWidth) {
		BOX_WIDTH = boxWidth;
	}

	public void checkGroups(final Map<String, List<Competitor>> competitorsByGroup) {
		boolean newData = false;
		for (final CGroup group : groups) {
			if (!competitorsByGroup.keySet().contains(group.getId()) && !newData) {
				newData = true;
				break;
			}
		}
		if (groups.isEmpty()) {
			newData = true;
		}
		if (newData) {
			groups.clear();
		}

		for (final String req2 : competitorsByGroup.keySet()) {
			final CGroup group = getGroup(req2);

			final List<Competitor> persons = competitorsByGroup.get(req2);

			for (final Competitor person : persons) {
				group.addName(person);
			}
		}
		Collections.sort(groups, new Comparator<CGroup>() {
			@Override
			public int compare(final CGroup o1, final CGroup o2) {
				return o1.getNumber() == o2.getNumber() ? 0 : o1.getNumber() < o2.getNumber() ? -1 : 1;
			}
		});

		buildGroups(newData);
	}

	private void buildGroups(final boolean newData) {
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
	private CGroup getGroup(final String id) {
		for (final CGroup g : groups) {
			if (g.getId().equals(id)) {
				return g;
			}
		}

		final CGroup group = new CGroup(id);
		groups.add(group);

		return group;
	}

	private void groupPositions(final boolean calculateTop) {
		float top = 35;

		onLeft = groups.size() / 2 + groups.size() % 2;
		onRight = groups.size() - onLeft;

		for (int i = 0; i < groups.size(); i++) {
			final CGroup group = groups.get(i);
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
			final CGroup group = groups.get(i);
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
		CGroup lastChild = groups.get(0);
		List<CGroup> targetGroup = groups;
		List<CGroup> childGroup = new LinkedList<CGroup>();

		while (n > 1) {
			offsetLeft += BOX_WIDTH + 30;
			n = n / 2 + n % 2;
			for (int i = 0; i < n; i++) {
				final CGroup child = new CGroup(nextID + "_ _" + tier);
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
			childGroup = new LinkedList<CGroup>();
			parent = 0;
			tier++;
		}
		if (n == 1) {
			offsetLeft += BOX_WIDTH + 20;
			finalBout = new CGroup(nextID + "_ _" + tier);
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
		CGroup lastChild = groups.get(0);
		List<CGroup> targetGroup = groups;
		List<CGroup> childGroup = new LinkedList<CGroup>();

		while (n > 1) {
			offsetLeft += BOX_WIDTH + 30;
			n = n / 2 + n % 2;
			for (int i = 0; i < n; i++) {
				final CGroup child = new CGroup(nextID + "_ _" + tier);
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
			childGroup = new LinkedList<CGroup>();
			parent = 0;
			tier++;
		}
		if (n == 1) {
			offsetLeft += BOX_WIDTH + 20;
			finalBout = new CGroup(nextID + "_ _" + tier);
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
		childGroup = new LinkedList<CGroup>();
		for (int i = parent; i < groups.size(); i++) {
			targetGroup.get(i).setLeftSide(offsetLeft);
		}

		while (n > 1) {
			offsetLeft -= BOX_WIDTH + 30;
			n = n / 2 + n % 2;
			for (int i = 0; i < n; i++) {
				final CGroup child = new CGroup(nextID + "_ _" + tier);
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
			childGroup = new LinkedList<CGroup>();
			parent = 0;
			tier++;
		}
		// if right side has less groups than left side.
		if (maxTier != tier) {
			// for (VGroup parentGroup : finalBout.getParents()) {
			if (lastChild.getLeftSide() > finalBout.getLeftSide()) {
				for (final Competitor p : lastChild.getNames()) {
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
				final CGroup parentGroup = groups.get(parent);
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
			winner = new CGroup("99_ _" + (maxTier + 1));
			addAdvanced(winner, finalBout.getNames());
		}
		fillGroup(finalBout);
	}

	private void calculateChildPositions() {
		final List<CGroup> allGroups = new LinkedList<CGroup>();

		for (final CGroup group : groups) {
			CGroup child = group;
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

	private boolean calculatePosition(final List<CGroup> allGroups) {
		final List<CGroup> remove = new ArrayList<CGroup>();

		for (final CGroup group : allGroups) {
			boolean allParentsHavePosition = true;
			for (final CGroup parent : group.getParents()) {
				if (!parent.hasPosition()) {
					allParentsHavePosition = false;
				}
			}
			if (allParentsHavePosition) {
				float bottom = Float.MAX_VALUE;
				float top = 0;
				for (final CGroup parent : group.getParents()) {
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

		canvas.getContext2d().clearRect(0.0, 0.0, canvas.getCoordinateSpaceWidth(), canvas.getCoordinateSpaceWidth());

		for (int j = 0; j < onLeft; j++) {
			final CGroup group = groups.get(j);
			paint.left(group);
			drawnGroups.add(group);
			drawChild(group.getChildGroup());
		}
		drawParent(finalBout);
	}

	private void drawLeftChart() {
		drawnGroups.clear();

		canvas.getContext2d().clearRect(0.0, 0.0, canvas.getCoordinateSpaceWidth(), canvas.getCoordinateSpaceWidth());

		for (final CGroup group : groups) {
			paint.left(group);
			drawnGroups.add(group);
			if (group.getChildGroup() != null) {
				drawChild(group.getChildGroup());
			}
		}
	}

	Map<Integer, Boolean> drawTier = new HashMap<Integer, Boolean>();

	private void drawChild(final CGroup childGroup) {
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

	private void drawParent(final CGroup childGroup) {
		offsetLeft += 30;
		for (final CGroup parent : childGroup.getParents()) {

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

	public static boolean hasAdvance(final List<Competitor> persons, final int toTier) {
		for (final Competitor p : persons) {
			if (p.advancedTo() >= toTier) {
				return true;
			}
		}
		return false;
	}

	private void addAdvanced(final CGroup child, final List<Competitor> persons) {
		for (final Competitor p : persons) {
			if (p.advancedTo() >= child.getTier()) {
				child.addName(p);
			}
		}
	}

	private void fillGroup(final CGroup group) {
		if (group.getNames().isEmpty()) {
			group.addName(new Competitor(-1, "", 0));
		}
		if (group.getNames().size() == 1 && group != finalBout && group.getParents().size() > 1) {
			group.addName(new Competitor(-1, "", 0));
		}
	}

	/**
	 * Sets the canvas width
	 * 
	 * @param width
	 *            The width in pixels
	 */
	public void setCanvasWidth(final int width) {
		if (width != this.width) {
			VConsole.log("Setting width: " + width + "px");
			canvas.setCoordinateSpaceWidth(width);
			VConsole.log("Canvas width: " + canvas.getCoordinateSpaceWidth());
		}
	}

	/**
	 * Sets the canvas height
	 * 
	 * @param height
	 *            The height in pixels
	 */
	public void setCanvasHeight(final int height) {
		if (height != this.height) {
			VConsole.log("Setting height: " + height + "px");
			canvas.setCoordinateSpaceHeight(height);
			VConsole.log("Canvas height: " + canvas.getCoordinateSpaceHeight());
		}
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
		if (!enableDragging) {
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
			for (final CGroup group : drawnGroups) {
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
				for (final CGroup group : drawnGroups) {
					if (group.getLeftSide() < x && x < group.getLeftSide() + BOX_WIDTH && group.getTop() < y && y < group.getBottom()) {
						try {
							final Competitor person = group.getNames().get((int) (y - group.getTop()) / 20);
							valueSelect(person.getId());
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

			for (final CGroup group : drawnGroups) {
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

	public void valueSelect(final long value) {
		fireEvent(new SelectionEvent(value));
	}

	public HandlerRegistration addSelectionEventHandler(final SelectionEventHandler selectionEventHandler) {
		return addHandler(selectionEventHandler, SelectionEvent.getType());
	}

}
