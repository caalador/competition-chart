package com.competition.chart.visualisation.client.ui;

import java.util.List;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.user.client.ui.HTML;

public class VPaintGroup {

	private final Canvas canvas;
	private final List<HTML> names;
	private int BOX_WIDTH = VKnockoutChart.BOX_WIDTH;

	public VPaintGroup(final Canvas canvas, final List<HTML> names) {
		this.canvas = canvas;
		this.names = names;
	}

	public void left(final VGroup group) {
		final Context2d context = canvas.getContext2d();

		context.setStrokeStyle("rgb(0,0,0)");
		context.setLineWidth(1);

		context.beginPath();

		context.setFont("14px Arial");
		context.fillText(group.getName(), (double) group.getLeftSide() + 10, (double) group.getTop() - 5);

		int offsetTop = (int) group.getTop();

		context.moveTo(group.getLeftSide(), offsetTop);
		for (int i = 0; i < group.getNames().size(); i++) {
			final VPerson p = group.getNames().get(i);

			context.fillText(p.getName(), (double) group.getLeftSide() + 5, (double) offsetTop + 15, BOX_WIDTH - 5);

			context.rect(group.getLeftSide(), offsetTop, BOX_WIDTH, 20);

			if (p.advancedTo() >= group.getTier() + 1) {
				context.closePath();
				context.stroke();
				context.setStrokeStyle("rgb(10,255,0)");
				context.beginPath();
			}
			context.moveTo(group.getLeftSide() + BOX_WIDTH, offsetTop + 10);
			context.lineTo(group.getLeftSide() + BOX_WIDTH + 10, offsetTop + 10);

			context.lineTo(group.getLeftSide() + BOX_WIDTH + 10, group.getMiddleOfGroup());

			context.moveTo(group.getLeftSide(), offsetTop);
			context.closePath();
			context.stroke();
			context.setStrokeStyle("rgb(0,0,0)");
			context.beginPath();

			offsetTop += 20;
		}

		/* next tier */
		if (VKnockoutChart.hasAdvance(group.getNames(), group.getTier() + 1)) {
			context.closePath();
			context.stroke();
			context.setStrokeStyle("rgb(10,255,0)");
			context.beginPath();
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
				context.setStrokeStyle("rgb(10,255,0)");
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

	public void right(final VGroup group) {
		final Context2d context = canvas.getContext2d();

		context.setStrokeStyle("rgb(0,0,0)");
		context.setLineWidth(1);
		context.beginPath();

		int offsetTopRight = (int) group.getTop();

		context.setFont("14px Arial");
		context.fillText(group.getName(), (double) group.getLeftSide() + 10, (double) group.getTop() - 5);

		final int middleOfGroup = offsetTopRight + (group.getNames().size() * 20) / 2;

		context.moveTo(group.getLeftSide(), offsetTopRight);
		for (int i = 0; i < group.getNames().size(); i++) {
			final VPerson p = group.getNames().get(i);

			context.fillText(p.getName(), (double) group.getLeftSide() + 5, (double) offsetTopRight + 15, BOX_WIDTH - 5);

			context.rect(group.getLeftSide(), offsetTopRight, BOX_WIDTH, 20);

			if (p.advancedTo() >= group.getTier() + 1) {
				context.closePath();
				context.stroke();
				context.setStrokeStyle("rgb(10,255,0)");
				context.beginPath();
			}
			context.moveTo(group.getLeftSide(), offsetTopRight + 10);
			context.lineTo(group.getLeftSide() - 10, offsetTopRight + 10);

			context.lineTo(group.getLeftSide() - 10, middleOfGroup);

			context.moveTo(group.getLeftSide(), offsetTopRight);
			context.closePath();
			context.stroke();
			context.setStrokeStyle("rgb(0,0,0)");
			context.beginPath();

			offsetTopRight += 20;
		}

		/* next tier */
		if (VKnockoutChart.hasAdvance(group.getNames(), group.getTier() + 1)) {
			context.closePath();
			context.stroke();
			context.setStrokeStyle("rgb(10,255,0)");
			context.beginPath();
		}
		context.moveTo(group.getLeftSide() - 10, middleOfGroup);
		context.lineTo(group.getLeftSide() - 20, middleOfGroup);

		context.lineTo(group.getLeftSide() - 20, group.getChildGroup().getMiddleOfGroup());

		if (group.getParents().size() > 0) {
			context.moveTo(0, 0);
			context.closePath();
			context.stroke();
			if (group.hasCompetitors()) {
				context.setStrokeStyle("rgb(10,255,0)");
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

	public void finalBout(final VGroup group) {
		final Context2d context = canvas.getContext2d();

		context.setStrokeStyle("rgb(0,0,0)");
		context.setLineWidth(1);
		context.beginPath();

		context.setFont("14px Arial");
		context.fillText(group.getName(), (double) group.getLeftSide() + 10, (double) group.getTop() - 5);

		int offsetTop = (int) group.getTop();

		context.moveTo(group.getLeftSide(), offsetTop);
		for (int i = 0; i < group.getNames().size(); i++) {
			final VPerson p = group.getNames().get(i);

			context.fillText(p.getName(), (double) group.getLeftSide() + 5, (double) offsetTop + 15, BOX_WIDTH - 5);

			context.rect(group.getLeftSide(), offsetTop, BOX_WIDTH, 20);

			offsetTop += 20;
		}

		context.moveTo(0, 0);
		context.closePath();
		context.stroke();
	}

	public void winner(final VGroup winner, final VGroup finalBout) {
		final Context2d context = canvas.getContext2d();

		context.setStrokeStyle("rgb(0,0,0)");
		context.setLineWidth(1);
		context.beginPath();

		int offsetTop = (int) winner.getTop();

		context.moveTo(winner.getLeftSide(), offsetTop);
		for (int i = 0; i < winner.getNames().size(); i++) {
			final VPerson p = winner.getNames().get(i);

			context.fillText(p.getName(), (double) winner.getLeftSide() + 5, (double) offsetTop + 15, BOX_WIDTH - 5);

			context.rect(winner.getLeftSide(), offsetTop, BOX_WIDTH, 20);

			offsetTop += 21;
		}
		context.moveTo(0, 0);
		context.closePath();
		context.stroke();

		context.setStrokeStyle("rgb(10,255,0)");
		context.beginPath();
		context.moveTo(winner.getLeftSide() + BOX_WIDTH / 2, offsetTop);
		context.lineTo(winner.getLeftSide() + BOX_WIDTH / 2, finalBout.getTop() + 1);
		context.closePath();
		context.stroke();
	}

	public void setBoxWidth(final int boxWidth) {
		BOX_WIDTH = boxWidth;
	}
}
