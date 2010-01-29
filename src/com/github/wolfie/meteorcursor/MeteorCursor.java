package com.github.wolfie.meteorcursor;

import com.github.wolfie.meteorcursor.client.ui.VMeteorCursor;
import com.vaadin.terminal.PaintException;
import com.vaadin.terminal.PaintTarget;
import com.vaadin.ui.AbstractComponent;

@SuppressWarnings("serial")
@com.vaadin.ui.ClientWidget(com.github.wolfie.meteorcursor.client.ui.VMeteorCursor.class)
public class MeteorCursor extends AbstractComponent {
  
  private int gravity = 75;
  private int threshold = 10;
  private int particleLifetime = 1000;
  private double distanceMultiplier = 2.0d;
  
  @Override
  public void paintContent(final PaintTarget target) throws PaintException {
    target.addAttribute(VMeteorCursor.ATTRIBUTE_GRAVITY_INT, gravity);
    target.addAttribute(VMeteorCursor.ATTRIBUTE_THRESHOLD_INT, threshold);
    target.addAttribute(VMeteorCursor.ATTRIBUTE_PART_LIFETIME_INT,
        particleLifetime);
    target.addAttribute(VMeteorCursor.ATTRIBUTE_DISTANCE_DBL,
        distanceMultiplier);
    
    // workaround to Vaadin not always telling whether it's enabled or not
    // TODO: Need to take a closer look at why.
    target.addAttribute(VMeteorCursor.ATTRIBUTE_VAADIN_DISABLED, !isEnabled());
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
  
  public void setGravity(final int gravity) {
    this.gravity = gravity;
    requestRepaint();
  }
  
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
  
  public void setParticleLifetime(final int milliseconds) {
    particleLifetime = milliseconds;
    requestRepaint();
  }
  
  public void setDistanceMultiplier(final double distanceMultiplier) {
    this.distanceMultiplier = distanceMultiplier;
    requestRepaint();
  }
  
  public double getDistanceMultiplier() {
    return distanceMultiplier;
  }
}
