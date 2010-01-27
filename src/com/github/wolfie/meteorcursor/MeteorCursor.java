package com.github.wolfie.meteorcursor;

import com.vaadin.terminal.PaintException;
import com.vaadin.terminal.PaintTarget;
import com.vaadin.ui.AbstractComponent;

/**
 * Server side component for the VMeteorCursor widget.
 */
@SuppressWarnings("serial")
@com.vaadin.ui.ClientWidget(com.github.wolfie.meteorcursor.client.ui.VMeteorCursor.class)
public class MeteorCursor extends AbstractComponent {
  
  @Override
  public void paintContent(final PaintTarget target) throws PaintException {
    super.paintContent(target);
    
    // TODO Paint any component specific content by setting attributes
    // These attributes can be read in updateFromUIDL in the widget.
  }
  
}
