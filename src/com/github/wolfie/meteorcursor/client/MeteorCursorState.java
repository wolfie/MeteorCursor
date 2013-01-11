package com.github.wolfie.meteorcursor.client;

import com.vaadin.shared.annotations.DelegateToWidget;
import com.vaadin.shared.communication.SharedState;

public class MeteorCursorState extends SharedState {
  private static final long serialVersionUID = -7284069613044534935L;

  public static final String PARTICLE_IMAGE_RESOURCE_KEY = "particleImage";

  @DelegateToWidget
  public int gravity = 75;

  @DelegateToWidget
  public int threshold = 10;

  @DelegateToWidget
  public int particleLifetime = 1000;

  @DelegateToWidget
  public double distanceMultiplier = 2.0d;
}
