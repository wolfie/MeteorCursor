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

package com.github.wolfie.meteorcursor;

import com.github.wolfie.meteorcursor.client.MeteorCursorState;
import com.vaadin.server.AbstractExtension;
import com.vaadin.server.ThemeResource;

@SuppressWarnings("serial")
public class MeteorCursor extends AbstractExtension {

  private ThemeResource particleImage;

  @Override
  protected MeteorCursorState getState() {
    return (MeteorCursorState) super.getState();
  }

  public int getGravity() {
    return getState().gravity;
  }

  /**
   * Sets the gravity for the particles.
   * 
   * @param gravity
   *          The gravity, measured by pixels downwards, during the whole
   *          lifetime of a particle. Positive gravity is downwards, negative
   *          gravity is upwards. Zero is no gravity.
   */
  public void setGravity(final int gravity) {
    getState().gravity = gravity;
  }

  /**
   * Sets the threshold to start rendering particles.
   * 
   * @param threshold
   *          The threshold in pixels. If the cursor is moved further than
   *          <tt>threshold</tt> pixels during one frame, a particle is drawn.
   *          For each double exceeding of <tt>treshold</tt>, additional
   *          particles are rendered.
   *          <p/>
   *          <em>Example:</em> threshold is set to 10, and the cursor is moved
   *          55px during one frame. This will render three particles: one at
   *          10px, the second at 30px and the third at 50px.
   */
  public void setThreshold(final int threshold) {
    getState().threshold = threshold;
  }

  public int getThreshold() {
    return getState().threshold;
  }

  public int getParticleLifetime() {
    return getState().particleLifetime;
  }

  /**
   * Sets the lifetime of a particle.
   * 
   * @param milliseconds
   *          How many milliseconds it takes for a particle to complete its
   *          animation cycle.
   */
  public void setParticleLifetime(final int milliseconds) {
    getState().particleLifetime = milliseconds;
  }

  /**
   * Sets the particle distance multiplier, compared to the moved cursor
   * distance.
   * 
   * @param distanceMultiplier
   *          How many times further the particles will fly compared to the
   *          length of the cursor movement.
   */
  public void setDistanceMultiplier(final double distanceMultiplier) {
    getState().distanceMultiplier = distanceMultiplier;
  }

  public double getDistanceMultiplier() {
    return getState().distanceMultiplier;
  }

  /**
   * Set the image to be shown as the particle.
   * 
   * @param particleImage
   *          An image {@link ThemeResource}.
   * @throws IllegalArgumentException
   *           if <code>particleImage</code> is <code>null</code> or not an
   *           image.
   */
  public void setParticleImage(final ThemeResource particleImage)
      throws IllegalArgumentException {
    if (particleImage == null) {
      throw new IllegalArgumentException("ThemeResource may not be null");
    }

    final String mt = particleImage.getMIMEType();

    // borrowed from Embedded
    if (mt.substring(0, mt.indexOf("/")).equalsIgnoreCase("image")) {
      setResource(MeteorCursorState.PARTICLE_IMAGE_RESOURCE_KEY, particleImage);
      this.particleImage = particleImage;
    } else {
      throw new IllegalArgumentException(
          "Given ThemeResource doesn't appear to be an image.");
    }
  }

  public ThemeResource getParticleImage() {
    return particleImage;
  }
}
