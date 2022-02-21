package com.aubot.agv;

import javax.swing.*;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.TableCellEditor;
import javax.swing.text.*;
import java.awt.*;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.regex.Pattern;

public class UInt32CellEditor extends AbstractCellEditor implements TableCellEditor {

    private JTextField textField;
    private int limitLength;

    public UInt32CellEditor(int limitLength) {
//        NumberFormat nf = new DecimalFormat();
        this.limitLength = limitLength;
//        if (zeroFill) {
//            nf = new DecimalFormat(String.format("%0" + limitLength + "d", 0));
//        }
//        NumberFormatter nft = new NumberFormatter(nf);
//        nft.setMinimum(0);
//        nft.setMaximum(max);
//        nft.setCommitsOnValidEdit(true);
//        textField.setFormatterFactory(new DefaultFormatterFactory(nft));
//        textField.setColumns(limitLength);
        //limitCharacters(textField, limitLength);
    }

    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected,
                                                 int rowIndex, int vColIndex) {

        textField = new JTextField();
        textField.setText(String.valueOf(value));
        limitCharacters(textField,limitLength);
        return textField;
    }

    public Object getCellEditorValue() {
        return textField.getText();
    }

    private void limitCharacters(JTextField textField, final int limit) {
        PlainDocument document = (PlainDocument) textField.getDocument();
        document.setDocumentFilter(new DocumentFilter() {
            @Override
            public void replace(DocumentFilter.FilterBypass fb, int offset,
                                int length, String text, AttributeSet attrs)
                    throws BadLocationException {
                String pattern = "[-0-9]";
                if (!Pattern.matches(pattern, text)) {
                    return;
                }
                String string = fb.getDocument().getText(0,
                        fb.getDocument().getLength())
                        + text;
                if (string.length() <= limit)
                    super.replace(fb, offset, length, text, attrs);
            }

        });
    }


}