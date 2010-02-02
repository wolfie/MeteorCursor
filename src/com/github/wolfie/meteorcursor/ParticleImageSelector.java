package com.github.wolfie.meteorcursor;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.AbstractSelect;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.OptionGroup;

@SuppressWarnings("serial")
public class ParticleImageSelector extends CustomComponent {
  public static final class SelectListener implements ValueChangeListener {
    
    private final MeteorCursor meteorCursor;
    
    public SelectListener(final MeteorCursor meteorCursor) {
      this.meteorCursor = meteorCursor;
    }
    
    public void valueChange(final ValueChangeEvent event) {
      final AbstractSelect select = (AbstractSelect) event.getProperty();
      final String image = (String) select.getItem(select.getValue())
          .getItemProperty(IMAGE_PROPERTY).getValue();
      
      if (!image.contains("?")) {
        meteorCursor.setParticleImage(image);
      } else {
        meteorCursor.setParticleImage(image, 3);
      }
    }
  }
  
  private static final Object IMAGE_PROPERTY = new Object();
  
  public ParticleImageSelector(final MeteorCursor meteorCursor) {
    final AbstractSelect optionGroup = new OptionGroup();
    optionGroup.addListener(new SelectListener(meteorCursor));
    optionGroup.addContainerProperty(IMAGE_PROPERTY, String.class, null);
    optionGroup.setImmediate(true);
    
    optionGroup.addItem("Vaadin Logo").getItemProperty(IMAGE_PROPERTY)
        .setValue("vaadin-logo.png");
    optionGroup.addItem("Spark").getItemProperty(IMAGE_PROPERTY).setValue(
        "spark.png");
    optionGroup.addItem("Valentine's").getItemProperty(IMAGE_PROPERTY)
        .setValue("heart.png");
    optionGroup.addItem("Animated").getItemProperty(IMAGE_PROPERTY).setValue(
        "cross_?.png");
    
    optionGroup.select("Vaadin Logo");
    setCompositionRoot(optionGroup);
  }
}
