package com.aubot.agv;

import com.aubot.agv.attributes.Attribute;

import javax.swing.*;
import java.util.Objects;

public class CheckConfigPanel extends ConfigurationPanel {

  public CheckConfigPanel(PropertiesChangeListener listener, Attribute<Object> attribute) {
    super(listener, attribute, new JCheckBox());
  }

  @Override
  public final JCheckBox getEditorComponent() {
    return (JCheckBox) super.getEditorComponent();
  }

  @Override
  public boolean editorToAttribute() {
    int value = getEditorComponent().isSelected() ? 1 : 0;
    if (!Objects.equals(attribute.getValue(), value)) {
      attribute.setValue(value);
      return true;
    }
    return false;

  }

  @Override
  protected void attributeToEditor(Object value) {
    if (value == null) {
      return;
    }
    getEditorComponent().setSelected((Integer) value > 0);
  }
}
