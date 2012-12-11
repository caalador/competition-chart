package com.competition.chart.visualisation.client.ui;

import com.competition.chart.visualisation.client.Competitor;
import com.competition.chart.visualisation.client.Group;
import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.Context2d;
import com.vaadin.client.VConsole;

public class CPaintGroup {

	private final static String LIGHT_GREEN = "rgb(10,255,0)";
	private final static String FOREST_GREEN = "rgb(34,139,34)";

	private final static String BLACK = "rgb(0,0,0)";

	private final Canvas canvas;

	private int BOX_WIDTH = CKnockoutChart.BOX_WIDTH;

	public CPaintGroup(final Canvas canvas) {
		this.canvas = canvas;
	}

	public void left(final Group group) {
		final Context2d context = canvas.getContext2d();

		context.setStrokeStyle(BLACK);
		context.setLineWidth(1);

		context.beginPath();

		context.setFont("14px Arial");
		context.fillText(group.getName(), (double) group.getLeftSide() + 10, (double) group.getTop() - 5);

		int offsetTop = (int) group.getTop();

		context.moveTo(group.getLeftSide(), offsetTop);
		for (int i = 0; i < group.getNames().size(); i++) {
			final Competitor p = group.getNames().get(i);

			if (p.advancedTo() >= group.getTier() + 1) {
				changeFillColor(context, FOREST_GREEN);
				VConsole.log("Changed color to forest green");
				context.fillText(p.getName(), (double) group.getLeftSide() + 5, (double) offsetTop + 15, BOX_WIDTH - 5);

				changeFillColor(context, BLACK);
			} else {
				context.fillText(p.getName(), (double) group.getLeftSide() + 5, (double) offsetTop + 15, BOX_WIDTH - 5);
			}

			context.rect(group.getLeftSide(), offsetTop, BOX_WIDTH, 20);

			if (p.advancedTo() >= group.getTier() + 1) {
				changeStrokeColor(context, LIGHT_GREEN);
			}
			context.moveTo(group.getLeftSide() + BOX_WIDTH, offsetTop + 10);
			context.lineTo(group.getLeftSide() + BOX_WIDTH + 10, offsetTop + 10);

			context.lineTo(group.getLeftSide() + BOX_WIDTH + 10, group.getMiddleOfGroup());

			context.moveTo(group.getLeftSide(), offsetTop);
			changeStrokeColor(context, BLACK);

			offsetTop += 20;
		}

		/* next tier */
		if (CKnockoutChart.hasAdvance(group.getNames(), group.getTier() + 1)) {
			changeStrokeColor(context, LIGHT_GREEN);
		}

		if (group.getChildGroup() != null) {
			context.moveTo(group.getLeftSide() + BOX_WIDTH + 10, group.getMiddleOfGroup());
			context.lineTo(group.getLeftSide() + BOX_WIDTH + 20, group.getMiddleOfGroup());

			context.lineTo(group.getLeftSide() + BOX_WIDTH + 20, group.getChildGroup().getMiddleOfGroup());
		}

		if (group.getParents().size() > 0) {
			context.moveTo(0, 0);
			context.closePath();
			context.stroke();
			if (group.hasCompetitors()) {
				context.setStrokeStyle(LIGHT_GREEN);
				context.beginPath();
			} else {
				context.beginPath();
			}
			context.moveTo(group.getLeftSide(), group.getMiddleOfGroup());
			context.lineTo(group.getLeftSide() - 10, group.getMiddleOfGroup());

		}
		context.moveTo(0, 0);
		context.closePath();
		context.stroke();
	}

	public void right(final Group group) {
		final Context2d context = canvas.getContext2d();

		context.setStrokeStyle(BLACK);
		context.setLineWidth(1);
		context.beginPath();

		int offsetTopRight = (int) group.getTop();

		context.setFont("14px Arial");
		context.fillText(group.getName(), (double) group.getLeftSide() + 10, (double) group.getTop() - 5);

		final int middleOfGroup = offsetTopRight + (group.getNames().size() * 20) / 2;

		context.moveTo(group.getLeftSide(), offsetTopRight);
		for (int i = 0; i < group.getNames().size(); i++) {
			final Competitor p = group.getNames().get(i);

			if (p.advancedTo() >= group.getTier() + 1) {
				changeFillColor(context, FOREST_GREEN);
				VConsole.log("Changed color to forest green");
				context.fillText(p.getName(), (double) group.getLeftSide() + 5, (double) offsetTopRight + 15, BOX_WIDTH - 5);

				changeFillColor(context, BLACK);
			} else {
				context.fillText(p.getName(), (double) group.getLeftSide() + 5, (double) offsetTopRight + 15, BOX_WIDTH - 5);
			}

			context.rect(group.getLeftSide(), offsetTopRight, BOX_WIDTH, 20);

			if (p.advancedTo() >= group.getTier() + 1) {
				changeStrokeColor(context, LIGHT_GREEN);
			}
			context.moveTo(group.getLeftSide(), offsetTopRight + 10);
			context.lineTo(group.getLeftSide() - 10, offsetTopRight + 10);

			context.lineTo(group.getLeftSide() - 10, middleOfGroup);

			context.moveTo(group.getLeftSide(), offsetTopRight);
			changeStrokeColor(context, BLACK);

			offsetTopRight += 20;
		}

		/* next tier */
		if (CKnockoutChart.hasAdvance(group.getNames(), group.getTier() + 1)) {
			changeStrokeColor(context, LIGHT_GREEN);
		}
		context.moveTo(group.getLeftSide() - 10, middleOfGroup);
		context.lineTo(group.getLeftSide() - 20, middleOfGroup);

		context.lineTo(group.getLeftSide() - 20, group.getChildGroup().getMiddleOfGroup());

		if (group.getParents().size() > 0) {
			context.moveTo(0, 0);
			context.closePath();
			context.stroke();
			if (group.hasCompetitors()) {
				context.setStrokeStyle(LIGHT_GREEN);
				context.beginPath();
			} else {
				context.beginPath();
			}
			context.moveTo(group.getLeftSide() + BOX_WIDTH, middleOfGroup);
			context.lineTo(group.getLeftSide() + BOX_WIDTH + 10, middleOfGroup);
		}

		context.moveTo(0, 0);
		context.closePath();
		context.stroke();
	}

	public void finalBout(final Group group) {
		final Context2d context = canvas.getContext2d();

		context.setStrokeStyle(BLACK);
		context.setLineWidth(1);
		context.beginPath();

		context.setFont("14px Arial");
		context.fillText(group.getName(), (double) group.getLeftSide() + 10, (double) group.getTop() - 5);

		int offsetTop = (int) group.getTop();

		context.moveTo(group.getLeftSide(), offsetTop);
		for (int i = 0; i < group.getNames().size(); i++) {
			final Competitor p = group.getNames().get(i);

			if (p.advancedTo() >= group.getTier() + 1) {
				changeFillColor(context, FOREST_GREEN);
				VConsole.log("Changed color to forest green");
				context.fillText(p.getName(), (double) group.getLeftSide() + 5, (double) offsetTop + 15, BOX_WIDTH - 5);

				changeFillColor(context, BLACK);
			} else {
				context.fillText(p.getName(), (double) group.getLeftSide() + 5, (double) offsetTop + 15, BOX_WIDTH - 5);
			}

			context.rect(group.getLeftSide(), offsetTop, BOX_WIDTH, 20);

			offsetTop += 20;
		}

		context.moveTo(0, 0);
		context.closePath();
		context.stroke();
	}

	public void winner(final Group winner, final Group finalBout) {
		final Context2d context = canvas.getContext2d();

		context.setStrokeStyle(BLACK);
		context.setLineWidth(1);
		context.beginPath();

		int offsetTop = (int) winner.getTop();

		context.moveTo(winner.getLeftSide(), offsetTop);
		for (int i = 0; i < winner.getNames().size(); i++) {
			final Competitor p = winner.getNames().get(i);

			context.fillText(p.getName(), (double) winner.getLeftSide() + 5, (double) offsetTop + 15, BOX_WIDTH - 5);

			context.rect(winner.getLeftSide(), offsetTop, BOX_WIDTH, 20);

			offsetTop += 21;
		}
		context.moveTo(0, 0);
		context.closePath();
		context.stroke();

		context.setStrokeStyle(LIGHT_GREEN);
		context.beginPath();
		context.moveTo(winner.getLeftSide() + BOX_WIDTH / 2, offsetTop);
		context.lineTo(winner.getLeftSide() + BOX_WIDTH / 2, finalBout.getTop() + 1);
		context.closePath();
		context.stroke();
	}

	public void setBoxWidth(final int boxWidth) {
		BOX_WIDTH = boxWidth;
	}

	private void changeStrokeColor(final Context2d context, final String rgb) {
		context.closePath();
		context.stroke();
		context.setStrokeStyle(rgb);
		context.beginPath();
	}

	private void changeFillColor(final Context2d context, final String rgb) {
		context.closePath();
		context.stroke();
		context.setFillStyle(rgb);
		context.beginPath();
	}
}
