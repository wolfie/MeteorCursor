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

import com.github.wolfie.meteorcursor.client.ui.VMeteorCursor;
import com.vaadin.terminal.PaintException;
import com.vaadin.terminal.PaintTarget;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.AbstractComponent;

@SuppressWarnings("serial")
@com.vaadin.ui.ClientWidget(com.github.wolfie.meteorcursor.client.ui.VMeteorCursor.class)
public class MeteorCursor extends AbstractComponent {
  
  private int gravity = 75;
  private int threshold = 10;
  private int particleLifetime = 1000;
  private int frames = 0;
  private double distanceMultiplier = 2.0d;
  private ThemeResource particleImage;
  
  @Override
  public void paintContent(final PaintTarget target) throws PaintException {
    target.addAttribute(VMeteorCursor.ATTRIBUTE_GRAVITY_INT, gravity);
    target.addAttribute(VMeteorCursor.ATTRIBUTE_THRESHOLD_INT, threshold);
    target.addAttribute(VMeteorCursor.ATTRIBUTE_PART_LIFETIME_INT,
        particleLifetime);
    target.addAttribute(VMeteorCursor.ATTRIBUTE_DISTANCE_DBL,
        distanceMultiplier);
    target.addAttribute(VMeteorCursor.ATTRIBUTE_IMAGE_RSRC, particleImage);
    target.addAttribute(VMeteorCursor.ATTRIBUTE_FRAMES_INT, frames);
    
    // workaround to Vaadin not always telling whether it's enabled or not
    // TODO: Need to take a closer look at why.
    target.addAttribute(VMeteorCursor.ATTRIBUTE_VAADIN_DISABLED, !isEnabled());
  }
  
  /**
   * Create a new MeteorCursor without a particle image.
   */
  public MeteorCursor() {
  }
  
  /**
   * Create a new MeteorCursor with a defined static particle image.
   * 
   * @param particleImageThemeId
   *          The theme resource id for the particle image.
   * @see #setParticleImage(String)
   */
  public MeteorCursor(final String particleImageThemeId) {
    setParticleImage(particleImageThemeId);
  }
  
  /**
   * Create a new MeteorCursor with a defined animated particle image.
   * 
   * @param particleImageThemeId
   *          The theme resource id for the particle image, with '?' as the
   *          frame number placeholder.
   * @param frames
   *          The total number of frames in the animation.
   * @see #setParticleImage(String, int)
   */
  public MeteorCursor(final String particleImageThemeId, final int frames) {
    setParticleImage(particleImageThemeId, frames);
  }
  
  /**
   * Enable or disable the rendering.
   * 
   * @param enabled
   *          <tt>true</tt> if you want have the {@link MeteorCursor} to render,
   *          <tt>false</tt> if you want to disable it.
   */
  @Override
  public void setEnabled(final boolean enabled) {
    // Overriding only for JavaDoc
    super.setEnabled(enabled);
  }
  
  public int getGravity() {
    return gravity;
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
    this.gravity = gravity;
    requestRepaint();
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
    this.threshold = threshold;
    requestRepaint();
  }
  
  public int getThreshold() {
    return threshold;
  }
  
  public int getParticleLifetime() {
    return particleLifetime;
  }
  
  /**
   * Sets the lifetime of a particle.
   * 
   * @param milliseconds
   *          How many milliseconds it takes for a particle to complete its
   *          animation cycle.
   */
  public void setParticleLifetime(final int milliseconds) {
    particleLifetime = milliseconds;
    requestRepaint();
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
    this.distanceMultiplier = distanceMultiplier;
    requestRepaint();
  }
  
  public double getDistanceMultiplier() {
    return distanceMultiplier;
  }
  
  /**
   * Set the image to be shown as the particle.
   * 
   * @param themeResourceId
   *          An image in your theme.
   * @return <tt>true</tt> if <tt>themeResourceId</tt> is an accepted type.
   *         <tt>false</tt> otherwise.
   */
  public boolean setParticleImage(final String themeResourceId) {
    return setParticleImage(themeResourceId, 0);
  }
  
  /**
   * Set animation of images to be shown as the particle.
   * 
   * @param themeResourceId
   *          A series of images in your theme. Use the character '<tt>?</tt>'
   *          as a placeholder for the one-digit zero-based frame number. The
   *          placeholder character may not be used more than once.
   * @param frames
   *          The number of images, or frames, in the animation series. Must be
   *          between 0 and 10 inclusive. If <tt>frames</tt> is 0, the
   *          placeholder in <tt>themeResourceId</tt> will be used as-is and
   *          thus will not be used as the frame number indicator.
   * @return <tt>true</tt> if <tt>themeResourceId</tt> is an accepted type,
   *         contains no more than one placeholder, and <tt>frames</tt> is in
   *         between 0 and 10, inclusive. <tt>false</tt> otherwise.
   */
  public boolean setParticleImage(final String themeResourceId, final int frames) {
    if (themeResourceId == null || frames > 10 || frames < 0) {
      return false;
    }
    
    final ThemeResource particleImage = new ThemeResource(themeResourceId);
    
    if (mimeTypeIsValid(particleImage) && filenameIsSane(themeResourceId)) {
      this.particleImage = particleImage;
      this.frames = frames;
      requestRepaint();
      return true;
    } else {
      return false;
    }
  }
  
  private boolean filenameIsSane(final String themeResourceId) {
    final boolean hasSeveralTokens = themeResourceId.replaceFirst("\\?", "")
        .contains("?");
    return !hasSeveralTokens;
  }
  
  private boolean mimeTypeIsValid(final ThemeResource particleImage2) {
    // borrowed from Embedded
    final String mt = particleImage2.getMIMEType();
    return mt.substring(0, mt.indexOf("/")).equalsIgnoreCase("image");
  }
  
  public ThemeResource getParticleImage() {
    return particleImage;
  }
  
  public boolean isAnimated() {
    return frames > 1;
  }
}
