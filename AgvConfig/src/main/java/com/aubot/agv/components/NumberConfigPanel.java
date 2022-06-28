package com.aubot.agv.components;

import com.aubot.agv.attributes.Attribute;

import javax.swing.*;
import java.util.Objects;

public class NumberConfigPanel extends ConfigurationPanel {

  public NumberConfigPanel(PropertiesChangeListener listener, Attribute<Object> attribute, int min, int max) {
    super(listener, attribute, new NumberTextField(min, max));
    setAttributeValue(min);
    getEditorComponent().setHorizontalAlignment(SwingConstants.CENTER);
  }

  @Override
  public final NumberTextField getEditorComponent() {
    return (NumberTextField) super.getEditorComponent();
  }

  @Override
  public boolean editorToAttribute() {
    Object value = getEditorComponent().getValue();
    if (!Objects.equals(attribute.getValue(), value)) {
      attribute.setValue(Integer.parseInt(value.toString()));
      return true;
    }
    return false;
  }

  @Override
  protected void attributeToEditor(Object value) {
    getEditorComponent().setText(value == null ? "0" : value.toString());
  }
}
