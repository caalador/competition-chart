/*
 * Copyright 2008-2009 Oliver Zoran
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.competition.chart.visualisation.client.ui.canvas.client.impl;

import com.competition.chart.visualisation.client.ui.canvas.client.Gradient;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.ImageElement;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;

/**
 * The default implementation of the canvas widget.
 * 
 * @see http://www.whatwg.org/specs/web-apps/current-work/#the-canvas
 * @see http://www.w3.org/html/wg/html5/#the-canvas
 * @see http://canvex.lazyilluminati.com/tests/tests/results.html
 */
public class CanvasImpl {

    // ///////////////////////////////////////////////////////////////
    // PRIVATE/PROTECTED MEMBERS/METHODS
    // ///////////////////////////////////////////////////////////////

    protected JavaScriptObject context;

    protected Element element;

    protected String backgroundColor;

    // TODO investigate further
    // protected native void cancelSelections() /*-{
    // try {
    // $wnd.getSelection().removeAllRanges();
    // } catch (e) {
    // // do nothing
    // }
    // }-*/;

    protected native void init() /*-{
                                 this.@com.competition.chart.visualisation.client.ui.canvas.client.impl.CanvasImpl::context = this.@com.competition.chart.visualisation.client.ui.canvas.client.impl.CanvasImpl::element.getContext("2d");
                                 }-*/;

    // ///////////////////////////////////////////////////////////////
    // CONSTRUCTORS AND PUBLIC METHODS
    // ///////////////////////////////////////////////////////////////

    public void init(Element element) {
        this.element = element;
        init();
    }

    public void setBackgroundColor(String color) {
        backgroundColor = color;
        DOM.setStyleAttribute(element, "backgroundColor", backgroundColor);
    }

    public String getBackgroundColor() {
        return backgroundColor;
    }

    public void setWidth(String width) {
        DOM.setElementAttribute(element, "width", width);
    }

    public void setHeight(String height) {
        DOM.setElementAttribute(element, "height", height);
    }

    // public void onMouseDown(Event event) {
    // cancelSelections();
    // DOM.eventPreventDefault(event);
    // }

    // public void onMouseUp() {
    // // method stub to be overridden by IE implementation
    // }

    // ///////////////////////////////////////////////////////////////
    // CANVAS STATE METHODS
    // ///////////////////////////////////////////////////////////////

    public native void restore() /*-{
                                 this.@com.competition.chart.visualisation.client.ui.canvas.client.impl.CanvasImpl::context.restore();
                                 }-*/;

    public native void save() /*-{
                              this.@com.competition.chart.visualisation.client.ui.canvas.client.impl.CanvasImpl::context.save();
                              }-*/;

    public native void rotate(double angle) /*-{
                                            this.@com.competition.chart.visualisation.client.ui.canvas.client.impl.CanvasImpl::context.rotate(angle);
                                            }-*/;

    public native void scale(double x, double y) /*-{
                                                 this.@com.competition.chart.visualisation.client.ui.canvas.client.impl.CanvasImpl::context.scale(x, y);
                                                 }-*/;

    public native void translate(double x, double y) /*-{
                                                     this.@com.competition.chart.visualisation.client.ui.canvas.client.impl.CanvasImpl::context.translate(x, y);
                                                     }-*/;

    // ///////////////////////////////////////////////////////////////
    // WORKING WITH PATHS
    // ///////////////////////////////////////////////////////////////

    public native void arc(double x, double y, double radius,
            double startAngle, double endAngle, boolean antiClockwise) /*-{
                                                                       this.@com.competition.chart.visualisation.client.ui.canvas.client.impl.CanvasImpl::context.arc(x, y, radius, startAngle, endAngle, antiClockwise);
                                                                       }-*/;

    public native void cubicCurveTo(double cp1x, double cp1y, double cp2x,
            double cp2y, double x, double y) /*-{
                                             this.@com.competition.chart.visualisation.client.ui.canvas.client.impl.CanvasImpl::context.bezierCurveTo(cp1x, cp1y, cp2x, cp2y, x, y);
                                             }-*/;

    public native void quadraticCurveTo(double cpx, double cpy, double x,
            double y) /*-{
                      this.@com.competition.chart.visualisation.client.ui.canvas.client.impl.CanvasImpl::context.quadraticCurveTo(cpx, cpy, x, y);
                      }-*/;

    public native void beginPath() /*-{
                                   this.@com.competition.chart.visualisation.client.ui.canvas.client.impl.CanvasImpl::context.beginPath();
                                   }-*/;

    public native void closePath() /*-{
                                   this.@com.competition.chart.visualisation.client.ui.canvas.client.impl.CanvasImpl::context.closePath();
                                   }-*/;

    public native void moveTo(double x, double y) /*-{
                                                  this.@com.competition.chart.visualisation.client.ui.canvas.client.impl.CanvasImpl::context.moveTo(x, y);
                                                  }-*/;

    public native void lineTo(double x, double y) /*-{
                                                  this.@com.competition.chart.visualisation.client.ui.canvas.client.impl.CanvasImpl::context.lineTo(x, y);
                                                  }-*/;

    public native void rect(double x, double y, double w, double h) /*-{
                                                                    this.@com.competition.chart.visualisation.client.ui.canvas.client.impl.CanvasImpl::context.rect(x, y, w, h);
                                                                    }-*/;

    // ///////////////////////////////////////////////////////////////
    // STROKING AND FILLING
    // ///////////////////////////////////////////////////////////////

    public native void clear() /*-{
                               this.@com.competition.chart.visualisation.client.ui.canvas.client.impl.CanvasImpl::context.clearRect(-1e4, -1e4, 2e4, 2e4);
                               }-*/;

    public native void fill() /*-{
                              this.@com.competition.chart.visualisation.client.ui.canvas.client.impl.CanvasImpl::context.fill();
                              }-*/;

    public native void stroke() /*-{
                                this.@com.competition.chart.visualisation.client.ui.canvas.client.impl.CanvasImpl::context.stroke();
                                }-*/;

    public native void fillRect(double x, double y, double w, double h) /*-{
                                                                        this.@com.competition.chart.visualisation.client.ui.canvas.client.impl.CanvasImpl::context.fillRect(x, y, w, h);
                                                                        }-*/;

    public native void strokeRect(double x, double y, double w, double h) /*-{
                                                                          this.@com.competition.chart.visualisation.client.ui.canvas.client.impl.CanvasImpl::context.strokeRect(x, y, w, h);
                                                                          }-*/;

    // ///////////////////////////////////////////////////////////////
    // GRADIENT STYLES
    // ///////////////////////////////////////////////////////////////

    public Gradient createLinearGradient(double x0, double y0, double x1,
            double y1) {
        return new LinearGradientImpl(x0, y0, x1, y1, context);
    }

    public Gradient createRadialGradient(double x0, double y0, double r0,
            double x1, double y1, double r1) {
        return new RadialGradientImpl(x0, y0, r0, x1, y1, r1, context);
    }

    // ///////////////////////////////////////////////////////////////
    // DRAWING IMAGES
    // ///////////////////////////////////////////////////////////////

    public native void drawImage(ImageElement image, double sx, double sy,
            double sWidth, double sHeight, double dx, double dy, double dWidth,
            double dHeight) /*-{
                            this.@com.competition.chart.visualisation.client.ui.canvas.client.impl.CanvasImpl::context.drawImage(image, sx, sy, sWidth, sHeight, dx, dy, dWidth, dHeight);
                            }-*/;

    // ///////////////////////////////////////////////////////////////
    // SETTERS AND GETTERS
    // ///////////////////////////////////////////////////////////////

    public native void setGlobalAlpha(double globalAlpha) /*-{
                                                          this.@com.competition.chart.visualisation.client.ui.canvas.client.impl.CanvasImpl::context.globalAlpha = globalAlpha;
                                                          }-*/;

    public native double getGlobalAlpha() /*-{
                                          return this.@com.competition.chart.visualisation.client.ui.canvas.client.impl.CanvasImpl::context.globalAlpha;
                                          }-*/;

    public native void setGlobalCompositeOperation(
            String globalCompositeOperation) /*-{
                                             this.@com.competition.chart.visualisation.client.ui.canvas.client.impl.CanvasImpl::context.globalCompositeOperation = globalCompositeOperation;
                                             }-*/;

    public native String getGlobalCompositeOperation() /*-{
                                                       return this.@com.competition.chart.visualisation.client.ui.canvas.client.impl.CanvasImpl::context.globalCompositeOperation;
                                                       }-*/;

    public native void setStrokeStyle(String strokeStyle) /*-{
                                                          this.@com.competition.chart.visualisation.client.ui.canvas.client.impl.CanvasImpl::context.strokeStyle = strokeStyle;
                                                          }-*/;

    public native String getStrokeStyle() /*-{
                                          return this.@com.competition.chart.visualisation.client.ui.canvas.client.impl.CanvasImpl::context.strokeStyle;
                                          }-*/;

    public native void setFillStyle(Gradient fillStyle) /*-{
                                                        this.@com.competition.chart.visualisation.client.ui.canvas.client.impl.CanvasImpl::context.fillStyle =
                                                        fillStyle.@com.competition.chart.visualisation.client.ui.canvas.client.impl.GradientImpl::gradient;
                                                        }-*/;

    public native void setFillStyle(String fillStyle) /*-{
                                                      this.@com.competition.chart.visualisation.client.ui.canvas.client.impl.CanvasImpl::context.fillStyle = fillStyle;
                                                      }-*/;

    public native String getFillStyle() /*-{
                                        return this.@com.competition.chart.visualisation.client.ui.canvas.client.impl.CanvasImpl::context.fillStyle;
                                        }-*/;

    public native void setLineWidth(double lineWidth) /*-{
                                                      this.@com.competition.chart.visualisation.client.ui.canvas.client.impl.CanvasImpl::context.lineWidth = lineWidth;
                                                      }-*/;

    public native double getLineWidth() /*-{
                                        return this.@com.competition.chart.visualisation.client.ui.canvas.client.impl.CanvasImpl::context.lineWidth;
                                        }-*/;

    public native void setLineCap(String lineCap) /*-{
                                                  this.@com.competition.chart.visualisation.client.ui.canvas.client.impl.CanvasImpl::context.lineCap = lineCap;
                                                  }-*/;

    public native String getLineCap() /*-{
                                      return this.@com.competition.chart.visualisation.client.ui.canvas.client.impl.CanvasImpl::context.lineCap;
                                      }-*/;

    public native void setLineJoin(String lineJoin) /*-{
                                                    this.@com.competition.chart.visualisation.client.ui.canvas.client.impl.CanvasImpl::context.lineJoin = lineJoin;
                                                    }-*/;

    public native String getLineJoin() /*-{
                                       return this.@com.competition.chart.visualisation.client.ui.canvas.client.impl.CanvasImpl::context.lineJoin;
                                       }-*/;

    public native void setMiterLimit(double miterLimit) /*-{
                                                        this.@com.competition.chart.visualisation.client.ui.canvas.client.impl.CanvasImpl::context.miterLimit = miterLimit;
                                                        }-*/;

    public native double getMiterLimit() /*-{
                                         return this.@com.competition.chart.visualisation.client.ui.canvas.client.impl.CanvasImpl::context.miterLimit;
                                         }-*/;
}