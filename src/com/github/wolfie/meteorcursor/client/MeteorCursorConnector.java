package com.github.wolfie.meteorcursor.client;

import com.github.wolfie.meteorcursor.MeteorCursor;
import com.vaadin.client.ServerConnector;
import com.vaadin.client.communication.StateChangeEvent;
import com.vaadin.client.extensions.AbstractExtensionConnector;
import com.vaadin.shared.ui.Connect;

@Connect(MeteorCursor.class)
public class MeteorCursorConnector extends AbstractExtensionConnector {

  private static final long serialVersionUID = 7052045686682997020L;
  private MeteorCursorExtension meteorCursor;

  @Override
  protected void init() {
    meteorCursor = new MeteorCursorExtension();
    super.init();
  }

  @Override
  public void onUnregister() {
    meteorCursor.unregister();
    super.onUnregister();
  }

  @Override
  public MeteorCursorState getState() {
    return (MeteorCursorState) super.getState();
  }

  @Override
  public void onStateChanged(final StateChangeEvent stateChangeEvent) {
    meteorCursor.setGravity(getState().gravity);
    meteorCursor.setParticleLifetime(getState().particleLifetime);
    meteorCursor.setThreshold(getState().threshold);
    meteorCursor.setDistanceMultiplier(getState().distanceMultiplier);
    final String particleImageUrl = getResourceUrl(MeteorCursorState.PARTICLE_IMAGE_RESOURCE_KEY);
    meteorCursor.setParticleImageUrl(particleImageUrl);
  }

  @Override
  protected void extend(final ServerConnector target) {
    // nothing to extend
  }
}
