package com.aubot.agv;

import com.aubot.agv.attributes.Attribute;

import javax.swing.*;
import java.util.List;

public class SelectionConfigPanel extends ConfigurationPanel {

  public SelectionConfigPanel(PropertiesChangeListener listener, Attribute<Object> attribute, List<Object> options) {
    super(listener, attribute, new JComboBox<>());
    getEditorComponent().addItem(options);
  }

  @Override
  public final JComboBox<Object> getEditorComponent() {
    return (JComboBox<Object>) super.getEditorComponent();
  }

  @Override
  public boolean editorToAttribute() {
    attribute.setValue(getEditorComponent().getSelectedItem());

    return true;
  }

  @Override
  protected void attributeToEditor(Object value) {
    getEditorComponent().setSelectedItem(value);
  }
}
