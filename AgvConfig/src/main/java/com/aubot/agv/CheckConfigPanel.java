package com.aubot.agv;

import com.aubot.agv.attributes.Attribute;

import javax.swing.*;

public class CheckConfigPanel extends ConfigurationPanel {

  public CheckConfigPanel(Attribute<Object> attribute) {
    super(attribute, new JCheckBox());
  }

  @Override
  public final JCheckBox getEditorComponent() {
    return (JCheckBox) super.getEditorComponent();
  }

  @Override
  public void editorToAttribute() {
    attribute.setValue(getEditorComponent().isSelected() ? 1 : 0);
  }

  @Override
  protected void attributeToEditor(Object value) {
    if (value == null) {
      return;
    }
    getEditorComponent().setSelected((Integer) value > 0);
  }
}
