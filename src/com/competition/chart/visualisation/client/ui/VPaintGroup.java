package com.competition.chart.visualisation.client.ui;

import java.util.List;

import com.competition.chart.visualisation.client.ui.canvas.client.Canvas;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.HTML;

public class VPaintGroup {

    private final AbsolutePanel displayPanel;
    private final Canvas canvas;
    private final List<HTML> names;

    public VPaintGroup(Canvas canvas, AbsolutePanel displayPanel,
            List<HTML> names) {
        this.canvas = canvas;
        this.displayPanel = displayPanel;
        this.names = names;
    }

    public void left(VGroup group, int offsetLeft) {
        canvas.setStrokeStyle("rgb(0,0,0)");
        canvas.setLineWidth(1);
        canvas.beginPath();

        HTML name = new HTML(group.getName());
        displayPanel.add(name, offsetLeft + 10, group.getTop() - 15);
        names.add(name);

        int offsetTop = group.getTop();

        canvas.moveTo(offsetLeft, offsetTop);
        for (int i = 0; i < group.getNames().size(); i++) {
            final VPerson p = group.getNames().get(i);

            name = new HTML(p.getName());
            displayPanel.add(name, offsetLeft + 10, offsetTop + 5);
            names.add(name);

            canvas.rect(offsetLeft, offsetTop, 100, 20);

            if (p.advancedTo() >= group.getTier() + 1) {
                canvas.closePath();
                canvas.stroke();
                canvas.setStrokeStyle("rgb(10,255,0)");
                canvas.beginPath();
            }
            canvas.moveTo(offsetLeft + 100, offsetTop + 10);
            canvas.lineTo(offsetLeft + 110, offsetTop + 10);

            canvas.lineTo(offsetLeft + 110, group.getMiddleOfGroup());

            canvas.moveTo(offsetLeft, offsetTop);
            canvas.closePath();
            canvas.stroke();
            canvas.setStrokeStyle("rgb(0,0,0)");
            canvas.beginPath();

            offsetTop += 20;
        }

        /* next tier */
        if (VSportChart.hasAdvance(group.getNames(), group.getTier() + 1)) {
            canvas.closePath();
            canvas.stroke();
            canvas.setStrokeStyle("rgb(10,255,0)");
            canvas.beginPath();
        }
        canvas.moveTo(offsetLeft + 110, group.getMiddleOfGroup());
        canvas.lineTo(offsetLeft + 120, group.getMiddleOfGroup());

        canvas.lineTo(offsetLeft + 120, group.getChildGroup()
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
            canvas.moveTo(offsetLeft, group.getMiddleOfGroup());
            canvas.lineTo(offsetLeft - 10, group.getMiddleOfGroup());

        }

        canvas.moveTo(0, 0);
        canvas.closePath();
        canvas.stroke();
    }

    public void right(VGroup group, int offsetLeft) {
        canvas.setStrokeStyle("rgb(0,0,0)");
        canvas.setLineWidth(1);
        canvas.beginPath();

        int offsetTopRight = group.getTop();

        HTML name = new HTML(group.getName());
        displayPanel.add(name, offsetLeft + 10, offsetTopRight - 15);
        names.add(name);

        final int middleOfGroup = offsetTopRight
                + (group.getNames().size() * 20) / 2;

        canvas.moveTo(offsetLeft, offsetTopRight);
        for (int i = 0; i < group.getNames().size(); i++) {
            final VPerson p = group.getNames().get(i);

            name = new HTML(p.getName());
            displayPanel.add(name, offsetLeft + 10, offsetTopRight + 5);
            names.add(name);

            canvas.rect(offsetLeft, offsetTopRight, 100, 20);

            if (p.advancedTo() >= group.getTier() + 1) {
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
        if (VSportChart.hasAdvance(group.getNames(), group.getTier() + 1)) {
            canvas.closePath();
            canvas.stroke();
            canvas.setStrokeStyle("rgb(10,255,0)");
            canvas.beginPath();
        }
        canvas.moveTo(offsetLeft - 10, middleOfGroup);
        canvas.lineTo(offsetLeft - 20, middleOfGroup);

        canvas.lineTo(offsetLeft - 20, group.getChildGroup().getMiddleOfGroup());

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
            canvas.moveTo(offsetLeft + 100, middleOfGroup);
            canvas.lineTo(offsetLeft + 110, middleOfGroup);
        }

        canvas.moveTo(0, 0);
        canvas.closePath();
        canvas.stroke();
    }

    public void finalBout(VGroup group, int offsetLeft) {

        canvas.setStrokeStyle("rgb(0,0,0)");
        canvas.setLineWidth(1);
        canvas.beginPath();

        HTML name = new HTML(group.getName());
        displayPanel.add(name, offsetLeft + 10, group.getTop() - 15);
        names.add(name);

        int offsetTop = group.getTop();

        canvas.moveTo(offsetLeft, offsetTop);
        for (int i = 0; i < group.getNames().size(); i++) {
            final VPerson p = group.getNames().get(i);

            name = new HTML(p.getName());
            displayPanel.add(name, offsetLeft + 10, offsetTop + 5);
            names.add(name);

            canvas.rect(offsetLeft, offsetTop, 100, 20);

            offsetTop += 20;
        }

        canvas.moveTo(0, 0);
        canvas.closePath();
        canvas.stroke();
    }

    public void winner(VGroup winner, VGroup finalBout, int offsetLeft) {

        canvas.setStrokeStyle("rgb(0,0,0)");
        canvas.setLineWidth(1);
        canvas.beginPath();

        int offsetTop = winner.getTop();

        canvas.moveTo(offsetLeft, offsetTop);
        for (int i = 0; i < winner.getNames().size(); i++) {
            final VPerson p = winner.getNames().get(i);

            HTML name = new HTML(p.getName());
            displayPanel.add(name, offsetLeft + 10, offsetTop + 5);
            names.add(name);

            canvas.rect(offsetLeft, offsetTop, 100, 20);

            offsetTop += 21;
        }
        canvas.moveTo(0, 0);
        canvas.closePath();
        canvas.stroke();

        offsetLeft += 50;

        canvas.setStrokeStyle("rgb(10,255,0)");
        canvas.beginPath();
        canvas.moveTo(offsetLeft, offsetTop);
        canvas.lineTo(offsetLeft, finalBout.getTop() + 1);
        canvas.closePath();
        canvas.stroke();
    }
}
