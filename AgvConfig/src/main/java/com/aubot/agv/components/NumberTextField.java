package com.aubot.agv.components;

import javax.swing.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class NumberTextField extends JTextField {

  private int value;
  private final int min;
  private final int max;

  public NumberTextField(int min, int max) {
    this.min = min;
    this.max = max;

    addFocusListener(new FocusListener() {
      @Override
      public void focusGained(FocusEvent e) {
        NumberTextField.this.selectAll();
      }

      @Override
      public void focusLost(FocusEvent e) {
        validateValue();
      }
    });
  }

  public int getValue() {
    return value;
  }

  public void setValue(int value) {
    this.setText(String.valueOf(value));
    validateValue();
  }

  public void validateValue() {
    String stringValue = this.getText();
    try {
      value = Integer.parseInt(stringValue);
      if (value < min) {
        value = min;
      } else if (value > max) {
        value = max;
      }
    } catch (NumberFormatException ignored) {

    } finally {
      this.setText(String.valueOf(value));
    }
  }
}
