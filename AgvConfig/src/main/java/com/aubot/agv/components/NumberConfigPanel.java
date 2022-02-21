package com.aubot.agv.components;

import com.aubot.agv.attributes.Attribute;

import javax.swing.*;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;
import java.util.Objects;

public class NumberConfigPanel extends ConfigurationPanel {

  private final NumberFormatter numberFormatter = new NumberFormatter();

  public NumberConfigPanel(PropertiesChangeListener listener, Attribute<Object> attribute, int min, int max) {
    super(listener, attribute, new JFormattedTextField());
    numberFormatter.setMinimum(min);
    numberFormatter.setMaximum(max);
    numberFormatter.setCommitsOnValidEdit(true);
    getEditorComponent().setFormatterFactory(new DefaultFormatterFactory(numberFormatter));
  }

  @Override
  public final JFormattedTextField getEditorComponent() {
    return (JFormattedTextField) super.getEditorComponent();
  }

  @Override
  public boolean editorToAttribute() {
    Object value = getEditorComponent().getValue();
    if (!Objects.equals(attribute.getValue(), value)) {
      attribute.setValue(Integer.parseInt(value == null ? "0" : value.toString()));
      return true;
    }
    return false;
  }

  @Override
  protected void attributeToEditor(Object value) {
    getEditorComponent().setText(value == null ? "0" : value.toString());
  }
}
