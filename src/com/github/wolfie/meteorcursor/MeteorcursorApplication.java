package com.github.wolfie.meteorcursor;

import com.vaadin.Application;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.validator.DoubleValidator;
import com.vaadin.data.validator.IntegerValidator;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

@SuppressWarnings("serial")
public class MeteorcursorApplication extends Application {
  @Override
  public void init() {
    final Window mainWindow = new Window("Meteorcursor Application");
    mainWindow.setContent(new HorizontalLayout());
    setMainWindow(mainWindow);
    
    final MeteorCursor meteorCursor = new MeteorCursor();
    mainWindow.addComponent(meteorCursor);
    mainWindow.addComponent(new Button("Disable", new ClickListener() {
      private static final long serialVersionUID = 7162981081807888955L;
      
      public void buttonClick(final ClickEvent event) {
        final boolean enabled = meteorCursor.isEnabled();
        meteorCursor.setEnabled(!enabled);
        event.getButton().setCaption(enabled ? "Enable" : "Disable");
      }
    }));
    
    final TextField gravityTextField = new TextField();
    gravityTextField.setValue(meteorCursor.getGravity());
    gravityTextField.addValidator(new IntegerValidator("Must be an integer."));
    gravityTextField.setImmediate(true);
    gravityTextField.setCaption("Gravity");
    gravityTextField.addListener(new ValueChangeListener() {
      public void valueChange(final ValueChangeEvent event) {
        try {
          meteorCursor.setGravity(Integer.parseInt((String) event.getProperty()
              .getValue()));
        } catch (final NumberFormatException e) {
          // validation is fixed
        }
      }
    });
    mainWindow.addComponent(gravityTextField);
    
    final TextField thresholdTextField = new TextField();
    thresholdTextField.setValue(meteorCursor.getThreshold());
    thresholdTextField
        .addValidator(new IntegerValidator("Must be an integer."));
    thresholdTextField.setImmediate(true);
    thresholdTextField.setCaption("Threshold");
    thresholdTextField.addListener(new ValueChangeListener() {
      public void valueChange(final ValueChangeEvent event) {
        try {
          meteorCursor.setThreshold(Integer.parseInt((String) event
              .getProperty().getValue()));
        } catch (final NumberFormatException e) {
          // validation is fixed
        }
      }
    });
    mainWindow.addComponent(thresholdTextField);
    
    final TextField lifetimeTextField = new TextField();
    lifetimeTextField.setValue(meteorCursor.getParticleLifetime());
    lifetimeTextField.addValidator(new IntegerValidator("Must be an integer."));
    lifetimeTextField.setImmediate(true);
    lifetimeTextField.setCaption("Particle Lifetime (ms)");
    lifetimeTextField.addListener(new ValueChangeListener() {
      public void valueChange(final ValueChangeEvent event) {
        try {
          meteorCursor.setParticleLifetime(Integer.parseInt((String) event
              .getProperty().getValue()));
        } catch (final NumberFormatException e) {
          // validation is fixed
        }
      }
    });
    mainWindow.addComponent(lifetimeTextField);
    
    final TextField distanceTextField = new TextField();
    distanceTextField.setValue(meteorCursor.getDistanceMultiplier());
    distanceTextField.addValidator(new DoubleValidator("Must be a double."));
    distanceTextField.setImmediate(true);
    distanceTextField.setCaption("Distance multiplier");
    distanceTextField.addListener(new ValueChangeListener() {
      public void valueChange(final ValueChangeEvent event) {
        try {
          meteorCursor.setDistanceMultiplier(Double.parseDouble((String) event
              .getProperty().getValue()));
        } catch (final NumberFormatException e) {
          // validation is fixed
        }
      }
    });
    mainWindow.addComponent(distanceTextField);
  }
}
