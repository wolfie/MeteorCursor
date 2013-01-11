package com.github.wolfie.meteorcursor;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;

@SuppressWarnings("serial")
@Theme("meteorcursor")
@Title("MeteorCursor Demo")
public class MeteorcursorUI extends UI {

  @Override
  protected void init(final VaadinRequest request) {
    final HorizontalLayout layout = new HorizontalLayout();
    setContent(layout);

    final MeteorCursor meteorCursor = new MeteorCursor();
    addExtension(meteorCursor);

    layout.addComponent(new ParticleImageSelector(meteorCursor));

    final TextField gravityTextField = new TextField();
    gravityTextField.setValue(String.valueOf(meteorCursor.getGravity()));
    gravityTextField.setImmediate(true);
    gravityTextField.setCaption("Gravity");
    gravityTextField.addValueChangeListener(new ValueChangeListener() {
      public void valueChange(final ValueChangeEvent event) {
        meteorCursor.setGravity(Integer.parseInt(gravityTextField.getValue()));
      }
    });
    layout.addComponent(gravityTextField);

    final TextField thresholdTextField = new TextField();
    thresholdTextField.setValue(String.valueOf(meteorCursor.getThreshold()));
    thresholdTextField.setImmediate(true);
    thresholdTextField.setCaption("Threshold");
    thresholdTextField.addValueChangeListener(new ValueChangeListener() {
      public void valueChange(final ValueChangeEvent event) {
        meteorCursor.setThreshold(Integer.parseInt(thresholdTextField
            .getValue()));
      }
    });
    layout.addComponent(thresholdTextField);

    final TextField lifetimeTextField = new TextField();
    lifetimeTextField.setValue(String.valueOf(meteorCursor
        .getParticleLifetime()));
    lifetimeTextField.setImmediate(true);
    lifetimeTextField.setCaption("Particle Lifetime (ms)");
    lifetimeTextField.addValueChangeListener(new ValueChangeListener() {
      public void valueChange(final ValueChangeEvent event) {
        meteorCursor.setParticleLifetime(Integer.parseInt(lifetimeTextField
            .getValue()));
      }
    });
    layout.addComponent(lifetimeTextField);

    final TextField distanceTextField = new TextField();
    distanceTextField.setValue(String.valueOf(meteorCursor
        .getDistanceMultiplier()));
    distanceTextField.setImmediate(true);
    distanceTextField.setCaption("Distance multiplier");
    distanceTextField.addValueChangeListener(new ValueChangeListener() {
      public void valueChange(final ValueChangeEvent event) {
        meteorCursor.setDistanceMultiplier(Double.parseDouble(distanceTextField
            .getValue()));
      }
    });
    layout.addComponent(distanceTextField);
  }
}
