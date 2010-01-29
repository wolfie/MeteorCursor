package com.github.wolfie.meteorcursor.client.ui;

import com.google.gwt.animation.client.Animation;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Event.NativePreviewEvent;
import com.google.gwt.user.client.Event.NativePreviewHandler;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.vaadin.terminal.gwt.client.ApplicationConnection;
import com.vaadin.terminal.gwt.client.BrowserInfo;
import com.vaadin.terminal.gwt.client.Paintable;
import com.vaadin.terminal.gwt.client.UIDL;

public class VMeteorCursor extends Widget implements Paintable,
    NativePreviewHandler {
  
  private class Particle extends Widget {
    public Particle(final int x, final int y, final double speed) {
      setElement(DOM.createDiv());
      final Element e = getElement();
      e.setClassName(PARTICLE_CLASSNAME);
      
      final Style style = e.getStyle();
      style.setTop(y + EFFECT_TOP_OFFSET, Style.Unit.PX);
      style.setLeft(x + EFFECT_LEFT_OFFSET, Style.Unit.PX);
      style.setBackgroundColor(getBackground((speed - 10) * 10));
      
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
            style.setTop(y + 10
                + (speed * distanceMultiplier * progress * deltaTop)
                + (gravity * progress * progress), Style.Unit.PX);
            style.setLeft(x + 10
                + (speed * distanceMultiplier * progress * deltaLeft),
                Style.Unit.PX);
            
            final double size = Math.ceil(PARTICLE_SIZE
                - (PARTICLE_SIZE * progress));
            style.setHeight(size, Style.Unit.PX);
            style.setWidth(size, Style.Unit.PX);
            
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
    
    private String getBackground(final double speed) {
      final int colorInt = Math.min(Double.valueOf(speed).intValue(), 255);
      String hexString = Integer.toHexString(colorInt);
      hexString = hexString.length() < 2 ? "0" + hexString : hexString;
      return "#" + hexString + "0000";
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
  
  private static final int PARTICLE_SIZE = 15;
  private static final int EFFECT_TOP_OFFSET = 10;
  private static final int EFFECT_LEFT_OFFSET = 10;
  
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
      if (previousMouseX != -1 && previousMouseY != -1) {
        
        // for each double exceeding of the threshold, paint one particle
        int particleThresholdCounter = threshold;
        while (particleThresholdCounter < speed) {
          RootPanel.get().add(new Particle(mouseX, mouseY, speed));
          particleThresholdCounter += threshold * 2;
        }
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
