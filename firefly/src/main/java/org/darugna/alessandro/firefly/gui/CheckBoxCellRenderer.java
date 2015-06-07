package org.darugna.alessandro.firefly.gui;

import java.awt.Component;

import javax.swing.JCheckBox;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

/**
 * Way to show Checkboxes inside a JList
 * http://www.devx.com/tips/Tip/5342
 */
public class CheckBoxCellRenderer implements ListCellRenderer {
	
	private static Border s_noFocusBorder = new EmptyBorder(1, 1, 1, 1);
	
	public Component getListCellRendererComponent(
             JList list, Object value, int index,
             boolean isSelected, boolean cellHasFocus) {
		JCheckBox checkbox = (JCheckBox) value;
		checkbox.setBackground(isSelected ? list.getSelectionBackground() : list.getBackground());
		checkbox.setForeground(isSelected ? list.getSelectionForeground() : list.getForeground());
		checkbox.setEnabled(list.isEnabled());
		checkbox.setFont(list.getFont());
		checkbox.setFocusPainted(false);
		checkbox.setBorderPainted(true);
		checkbox.setBorder(isSelected ? list.getBorder() : s_noFocusBorder);
		return checkbox;
	}
}
