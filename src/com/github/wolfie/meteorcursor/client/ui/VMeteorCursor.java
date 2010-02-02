//   
// Copyright 2010 Henrik Paul
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//

package com.github.wolfie.meteorcursor.client.ui;

import com.google.gwt.animation.client.Animation;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Event.NativePreviewEvent;
import com.google.gwt.user.client.Event.NativePreviewHandler;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.vaadin.terminal.gwt.client.ApplicationConnection;
import com.vaadin.terminal.gwt.client.BrowserInfo;
import com.vaadin.terminal.gwt.client.Paintable;
import com.vaadin.terminal.gwt.client.UIDL;

public class VMeteorCursor extends Widget implements Paintable,
    NativePreviewHandler {
  
  private class Particle extends HTML {
    public Particle(final int x, final int y, final double speed) {
      super("<img/>");
      
      final Element e = getElement();
      e.setClassName(PARTICLE_CLASSNAME);
      
      final Style style = e.getStyle();
      style.setPropertyPx("top", y);
      style.setPropertyPx("left", x);
      
      new Animation() {
        private double deltaTop = -2;
        private double deltaLeft = -2;
        
        @Override
        protected void onUpdate(final double progress) {
          if (deltaTop == -2 && deltaLeft == -2) {
            deltaTop = Math.random() * 2 - 1;
            deltaLeft = Math.random() * 2 - 1;
          }
          
          if (progress < 1) {
            final int top = Double.valueOf(
                y + (speed * distanceMultiplier * progress * deltaTop)
                    + (gravity * progress * progress)).intValue();
            final int left = Double.valueOf(
                x + (speed * distanceMultiplier * progress * deltaLeft))
                .intValue();
            
            style.setPropertyPx("top", top);
            style.setPropertyPx("left", left);
            
            final int size = Double.valueOf(
                Math.ceil(PARTICLE_SIZE - (PARTICLE_SIZE * progress)))
                .intValue();
            
            /*
             * The HTML needs to be set as a HTML widget instead of
             * DOM.createImg() since changing the dimensions in GWT just crops
             * the image, and the image is not re-scaled to fit 100% into the
             * Element.
             */
            setHTML(getImgHTML(size, progress));
            
          } else {
            removeFromParent();
          }
        }
        
        @Override
        protected double interpolate(final double progress) {
          // ease-out
          return 1.5 * progress - 0.5 * Math.pow(progress, 3);
        }
      }.run(particleLifetimeMillis);
    }
    
    private String getImgHTML(final int size, final double progress) {
      final int frameNumber = Double.valueOf(Math.floor(frames * progress))
          .intValue();
      final String image = particleImages[frameNumber];
      
      return "<img src='" + image + "' height=" + size + " width=" + size
          + "/>";
    }
  }
  
  /** Set the CSS class name to allow styling. */
  public static final String CLASSNAME = "v-meteorcursor";
  public static final String PARTICLE_CLASSNAME = CLASSNAME + "-particle";
  
  public static final String ATTRIBUTE_VAADIN_DISABLED = "disabled";
  
  public static final String ATTRIBUTE_GRAVITY_INT = "gr";
  public static final String ATTRIBUTE_THRESHOLD_INT = "th";
  public static final String ATTRIBUTE_PART_LIFETIME_INT = "pl";
  public static final String ATTRIBUTE_DISTANCE_DBL = "di";
  public static final String ATTRIBUTE_IMAGE_RSRC = "im";
  public static final String ATTRIBUTE_FRAMES_INT = "fr";
  
  private static final int PARTICLE_SIZE = 15;
  private static final int PATICLE_DELAY_MILLIS = 50;
  
  /** The client side widget identifier */
  protected String paintableId;
  
  /** Reference to the server connection object. */
  ApplicationConnection client;
  
  private int previousMouseY = -1;
  private int previousMouseX = -1;
  
  private int gravity;
  private int threshold;
  private int particleLifetimeMillis;
  private double distanceMultiplier;
  private int frames;
  private String[] particleImages;
  
  private boolean disabled = false;
  
  /**
   * The constructor should first call super() to initialize the component and
   * then handle any initialization relevant to Vaadin.
   */
  public VMeteorCursor() {
    Event.addNativePreviewHandler(this);
    
    // we need a div so that Vaadin doesn't throw a fit.
    setElement(Document.get().createDivElement());
    if (BrowserInfo.get().isIE6()) {
      getElement().getStyle().setProperty("overflow", "hidden");
      getElement().getStyle().setProperty("height", "0");
    }
  }
  
  /**
   * Called whenever an update is received from the server
   */
  public void updateFromUIDL(final UIDL uidl, final ApplicationConnection client) {
    if (client.updateComponent(this, uidl, true)) {
      return;
    }
    
    if (uidl.hasAttribute(ATTRIBUTE_VAADIN_DISABLED)) {
      disabled = uidl.getBooleanAttribute(ATTRIBUTE_VAADIN_DISABLED);
    }
    
    if (uidl.hasAttribute(ATTRIBUTE_GRAVITY_INT)) {
      gravity = uidl.getIntAttribute(ATTRIBUTE_GRAVITY_INT);
    }
    
    if (uidl.hasAttribute(ATTRIBUTE_THRESHOLD_INT)) {
      threshold = uidl.getIntAttribute(ATTRIBUTE_THRESHOLD_INT);
    }
    
    if (uidl.hasAttribute(ATTRIBUTE_PART_LIFETIME_INT)) {
      particleLifetimeMillis = uidl
          .getIntAttribute(ATTRIBUTE_PART_LIFETIME_INT);
    }
    
    if (uidl.hasAttribute(ATTRIBUTE_DISTANCE_DBL)) {
      distanceMultiplier = uidl.getDoubleAttribute(ATTRIBUTE_DISTANCE_DBL);
    }
    
    if (uidl.hasAttribute(ATTRIBUTE_FRAMES_INT)) {
      frames = uidl.getIntAttribute(ATTRIBUTE_FRAMES_INT);
    }
    
    if (uidl.hasAttribute(ATTRIBUTE_IMAGE_RSRC)) {
      final String particleImage = client.translateVaadinUri(uidl
          .getStringAttribute(ATTRIBUTE_IMAGE_RSRC));
      
      final int elements = Math.max(1, frames);
      particleImages = new String[elements];
      
      for (int i = 0; i < elements; i++) {
        particleImages[i] = particleImage.replace('?', String.valueOf(i)
            .toCharArray()[0]);
      }
    }
    
    this.client = client;
    paintableId = uidl.getId();
  }
  
  public void onPreviewNativeEvent(final NativePreviewEvent event) {
    final String type = event.getNativeEvent().getType();
    
    if ("mousemove".equals(type) && !disabled) {
      final int mouseX = event.getNativeEvent().getClientX();
      final int mouseY = event.getNativeEvent().getClientY();
      
      final double speed = getDistanceTravelled(mouseX, mouseY, previousMouseX,
          previousMouseY);
      
      // ignore the first cursor move
      if (previousMouseX != -1 && previousMouseY != -1 && speed > threshold) {
        
        // delay the particle a little, so that it doesn't come directly
        // underneath the cursor.
        new Timer() {
          @Override
          public void run() {
            // for each double exceeding of the threshold, paint one particle
            int particleThresholdCounter = threshold;
            while (particleThresholdCounter < speed) {
              RootPanel.get().add(new Particle(mouseX, mouseY, speed));
              particleThresholdCounter += threshold * 2;
            }
          }
        }.schedule(PATICLE_DELAY_MILLIS);
        
      }
      
      previousMouseX = mouseX;
      previousMouseY = mouseY;
    }
  }
  
  private double getDistanceTravelled(final int x, final int y,
      final int previousX, final int previousY) {
    return Math.sqrt(Math.pow(x - previousX, 2) + Math.pow(y - previousY, 2));
  }
}
