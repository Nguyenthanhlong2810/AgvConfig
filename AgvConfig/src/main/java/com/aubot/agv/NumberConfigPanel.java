package com.aubot.agv;

import com.aubot.agv.attributes.Attribute;

import javax.swing.*;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class NumberConfigPanel extends ConfigurationPanel {

  private final NumberFormatter numberFormatter = new NumberFormatter();

  public NumberConfigPanel(Attribute<Object> attribute, int min, int max) {
    super(attribute, new JFormattedTextField());
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
  public void editorToAttribute() {
    Object value = getEditorComponent().getValue();
    attribute.setValue(Integer.parseInt(value == null ? "0" : value.toString()));
  }

  @Override
  protected void attributeToEditor(Object value) {
    getEditorComponent().setText(value == null ? "0" : value.toString());
  }
}
