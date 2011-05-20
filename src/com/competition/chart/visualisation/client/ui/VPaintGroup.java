package com.competition.chart.visualisation.client.ui;

import java.util.List;

import com.competition.chart.visualisation.client.ui.canvas.client.Canvas;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.HTML;

public class VPaintGroup {

    private final AbsolutePanel displayPanel;
    private final Canvas canvas;
    private final List<HTML> names;
    private int BOX_WIDTH = VKnockoutChart.BOX_WIDTH;

    public VPaintGroup(Canvas canvas, AbsolutePanel displayPanel,
            List<HTML> names) {
        this.canvas = canvas;
        this.displayPanel = displayPanel;
        this.names = names;
    }

    public void left(VGroup group) {
        canvas.setStrokeStyle("rgb(0,0,0)");
        canvas.setLineWidth(1);
        canvas.beginPath();

        HTML name = new HTML(group.getName());
        displayPanel.add(name, (int) group.getLeftSide() + 10,
                (int) group.getTop() - 15);
        names.add(name);

        int offsetTop = (int) group.getTop();

        canvas.moveTo(group.getLeftSide(), offsetTop);
        for (int i = 0; i < group.getNames().size(); i++) {
            final VPerson p = group.getNames().get(i);

            name = new HTML(p.getName());
            displayPanel
                    .add(name, (int) group.getLeftSide() + 5, offsetTop + 5);
            names.add(name);

            canvas.rect(group.getLeftSide(), offsetTop, BOX_WIDTH, 20);

            if (p.advancedTo() >= group.getTier() + 1) {
                canvas.closePath();
                canvas.stroke();
                canvas.setStrokeStyle("rgb(10,255,0)");
                canvas.beginPath();
            }
            canvas.moveTo(group.getLeftSide() + BOX_WIDTH, offsetTop + 10);
            canvas.lineTo(group.getLeftSide() + BOX_WIDTH + 10, offsetTop + 10);

            canvas.lineTo(group.getLeftSide() + BOX_WIDTH + 10,
                    group.getMiddleOfGroup());

            canvas.moveTo(group.getLeftSide(), offsetTop);
            canvas.closePath();
            canvas.stroke();
            canvas.setStrokeStyle("rgb(0,0,0)");
            canvas.beginPath();

            offsetTop += 20;
        }

        /* next tier */
        if (VKnockoutChart.hasAdvance(group.getNames(), group.getTier() + 1)) {
            canvas.closePath();
            canvas.stroke();
            canvas.setStrokeStyle("rgb(10,255,0)");
            canvas.beginPath();
        }

        if (group.getChildGroup() != null) {
            canvas.moveTo(group.getLeftSide() + BOX_WIDTH + 10,
                    group.getMiddleOfGroup());
            canvas.lineTo(group.getLeftSide() + BOX_WIDTH + 20,
                    group.getMiddleOfGroup());

            canvas.lineTo(group.getLeftSide() + BOX_WIDTH + 20, group
                    .getChildGroup().getMiddleOfGroup());
        }

        if (group.getParents().size() > 0) {
            canvas.moveTo(0, 0);
            canvas.closePath();
            canvas.stroke();
            if (group.hasCompetitors()) {
                canvas.setStrokeStyle("rgb(10,255,0)");
                canvas.beginPath();
            } else {
                canvas.beginPath();
            }
            canvas.moveTo(group.getLeftSide(), group.getMiddleOfGroup());
            canvas.lineTo(group.getLeftSide() - 10, group.getMiddleOfGroup());

        }
        canvas.moveTo(0, 0);
        canvas.closePath();
        canvas.stroke();
    }

    public void right(VGroup group) {
        canvas.setStrokeStyle("rgb(0,0,0)");
        canvas.setLineWidth(1);
        canvas.beginPath();

        int offsetTopRight = (int) group.getTop();

        HTML name = new HTML(group.getName());
        displayPanel.add(name, (int) group.getLeftSide() + 10,
                offsetTopRight - 15);
        names.add(name);

        final int middleOfGroup = offsetTopRight
                + (group.getNames().size() * 20) / 2;

        canvas.moveTo(group.getLeftSide(), offsetTopRight);
        for (int i = 0; i < group.getNames().size(); i++) {
            final VPerson p = group.getNames().get(i);

            name = new HTML(p.getName());
            displayPanel.add(name, (int) group.getLeftSide() + 5,
                    offsetTopRight + 5);
            names.add(name);

            canvas.rect(group.getLeftSide(), offsetTopRight, BOX_WIDTH, 20);

            if (p.advancedTo() >= group.getTier() + 1) {
                canvas.closePath();
                canvas.stroke();
                canvas.setStrokeStyle("rgb(10,255,0)");
                canvas.beginPath();
            }
            canvas.moveTo(group.getLeftSide(), offsetTopRight + 10);
            canvas.lineTo(group.getLeftSide() - 10, offsetTopRight + 10);

            canvas.lineTo(group.getLeftSide() - 10, middleOfGroup);

            canvas.moveTo(group.getLeftSide(), offsetTopRight);
            canvas.closePath();
            canvas.stroke();
            canvas.setStrokeStyle("rgb(0,0,0)");
            canvas.beginPath();

            offsetTopRight += 20;
        }

        /* next tier */
        if (VKnockoutChart.hasAdvance(group.getNames(), group.getTier() + 1)) {
            canvas.closePath();
            canvas.stroke();
            canvas.setStrokeStyle("rgb(10,255,0)");
            canvas.beginPath();
        }
        canvas.moveTo(group.getLeftSide() - 10, middleOfGroup);
        canvas.lineTo(group.getLeftSide() - 20, middleOfGroup);

        canvas.lineTo(group.getLeftSide() - 20, group.getChildGroup()
                .getMiddleOfGroup());

        if (group.getParents().size() > 0) {
            canvas.moveTo(0, 0);
            canvas.closePath();
            canvas.stroke();
            if (group.hasCompetitors()) {
                canvas.setStrokeStyle("rgb(10,255,0)");
                canvas.beginPath();
            } else {
                canvas.beginPath();
            }
            canvas.moveTo(group.getLeftSide() + BOX_WIDTH, middleOfGroup);
            canvas.lineTo(group.getLeftSide() + BOX_WIDTH + 10, middleOfGroup);
        }

        canvas.moveTo(0, 0);
        canvas.closePath();
        canvas.stroke();
    }

    public void finalBout(VGroup group) {

        canvas.setStrokeStyle("rgb(0,0,0)");
        canvas.setLineWidth(1);
        canvas.beginPath();

        HTML name = new HTML(group.getName());
        displayPanel.add(name, (int) group.getLeftSide() + 10,
                (int) group.getTop() - 15);
        names.add(name);

        int offsetTop = (int) group.getTop();

        canvas.moveTo(group.getLeftSide(), offsetTop);
        for (int i = 0; i < group.getNames().size(); i++) {
            final VPerson p = group.getNames().get(i);

            name = new HTML(p.getName());
            displayPanel.add(name, (int) group.getLeftSide() + 10,
                    offsetTop + 5);
            names.add(name);

            canvas.rect(group.getLeftSide(), offsetTop, BOX_WIDTH, 20);

            offsetTop += 20;
        }

        canvas.moveTo(0, 0);
        canvas.closePath();
        canvas.stroke();
    }

    public void winner(VGroup winner, VGroup finalBout) {

        canvas.setStrokeStyle("rgb(0,0,0)");
        canvas.setLineWidth(1);
        canvas.beginPath();

        int offsetTop = (int) winner.getTop();

        canvas.moveTo(winner.getLeftSide(), offsetTop);
        for (int i = 0; i < winner.getNames().size(); i++) {
            final VPerson p = winner.getNames().get(i);

            HTML name = new HTML(p.getName());
            displayPanel.add(name, (int) winner.getLeftSide() + 10,
                    offsetTop + 5);
            names.add(name);

            canvas.rect(winner.getLeftSide(), offsetTop, BOX_WIDTH, 20);

            offsetTop += 21;
        }
        canvas.moveTo(0, 0);
        canvas.closePath();
        canvas.stroke();

        canvas.setStrokeStyle("rgb(10,255,0)");
        canvas.beginPath();
        canvas.moveTo(winner.getLeftSide() + BOX_WIDTH / 2, offsetTop);
        canvas.lineTo(winner.getLeftSide() + BOX_WIDTH / 2,
                finalBout.getTop() + 1);
        canvas.closePath();
        canvas.stroke();
    }

    public void setBoxWidth(int boxWidth) {
        BOX_WIDTH = boxWidth;
    }
}
