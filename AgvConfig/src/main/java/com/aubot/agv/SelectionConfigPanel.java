package com.aubot.agv;

import com.aubot.agv.attributes.Attribute;

import javax.swing.*;
import java.util.List;

public class SelectionConfigPanel extends ConfigurationPanel {

  public SelectionConfigPanel(Attribute<Object> attribute, List<Object> options) {
    super(attribute, new JComboBox<>());
    getEditorComponent().addItem(options);
  }

  @Override
  public final JComboBox<Object> getEditorComponent() {
    return (JComboBox<Object>) super.getEditorComponent();
  }

  @Override
  public void editorToAttribute() {
    attribute.setValue(getEditorComponent().getSelectedItem());
  }

  @Override
  protected void attributeToEditor(Object value) {
    getEditorComponent().setSelectedItem(value);
  }
}
