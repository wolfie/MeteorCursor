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

package com.github.wolfie.meteorcursor.client;

import com.google.gwt.animation.client.Animation;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Event.NativePreviewEvent;
import com.google.gwt.user.client.Event.NativePreviewHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

public class MeteorCursorExtension extends Widget implements
    NativePreviewHandler {

  private class ParticleGenerator {
    public Element generateFor(final int x, final int y, final double speed) {
      final Element particleElement = DOM.createDiv();
      particleElement.setClassName(PARTICLE_CLASSNAME);

      final Style style = particleElement.getStyle();
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
            particleElement.setInnerHTML(getImgHTML(size));
          } else {
            particleElement.removeFromParent();
          }
        }

        @Override
        protected double interpolate(final double progress) {
          // ease-out
          return 1.5 * progress - 0.5 * Math.pow(progress, 3);
        }
      }.run(particleLifetimeMillis);

      return particleElement;
    }

    private String getImgHTML(final int size) {
      return "<img src='" + getParticleImageURI() + "' height=" + size
          + " width=" + size + "/>";
    }
  }

  /** Set the CSS class name to allow styling. */
  public static final String CLASSNAME = "v-meteorcursor";
  public static final String PARTICLE_CLASSNAME = CLASSNAME + "-particle";

  private static final int PARTICLE_SIZE = 15;
  private static final int PATICLE_DELAY_MILLIS = 50;

  private int previousMouseY = -1;
  private int previousMouseX = -1;

  private int gravity;
  private int threshold;
  private int particleLifetimeMillis;
  private double distanceMultiplier;
  private String particleImage;

  private final HandlerRegistration registration;

  private final ParticleGenerator generator = new ParticleGenerator();

  /**
   * The constructor should first call super() to initialize the component and
   * then handle any initialization relevant to Vaadin.
   */
  public MeteorCursorExtension() {
    registration = Event.addNativePreviewHandler(this);
  }

  public void unregister() {
    registration.removeHandler();
  }

  public void onPreviewNativeEvent(final NativePreviewEvent event) {
    final String type = event.getNativeEvent().getType();

    if ("mousemove".equals(type)) {
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
              final Element particle = generator.generateFor(mouseX, mouseY,
                  speed);
              RootPanel.get().getElement().appendChild(particle);
              // RootPanel.get().add(new Particle(mouseX, mouseY, speed));
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

  private String getParticleImageURI() {
    return particleImage != null ? particleImage : "";
  }

  public void setGravity(final int gravity) {
    this.gravity = gravity;
  }

  public void setParticleImageUrl(final String particleImage) {
    this.particleImage = particleImage;
  }

  public void setThreshold(final int threshold) {
    this.threshold = threshold;
  }

  public void setParticleLifetime(final int particleLifetimeMillis) {
    this.particleLifetimeMillis = particleLifetimeMillis;
  }

  public void setDistanceMultiplier(final double distanceMultiplier) {
    this.distanceMultiplier = distanceMultiplier;
  }
}
