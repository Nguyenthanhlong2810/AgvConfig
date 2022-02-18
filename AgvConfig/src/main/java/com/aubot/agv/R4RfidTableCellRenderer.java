package com.aubot.agv;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class R4RfidTableCellRenderer implements TableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        JLabel component = new JLabel("O");
        component.setHorizontalAlignment(SwingConstants.CENTER);
        if (value != null) {
            component.setText(String.format("O%3s", value).replace(' ', '0'));
        }
        return component;
    }
}
