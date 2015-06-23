 package org.darugna.alessandro.firefly.gui;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractCellEditor;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("serial")
public class ComboboxCellEditor<T> extends AbstractCellEditor
				implements TableCellEditor, ActionListener {
	
	private static Logger s_logger = LoggerFactory.getLogger(ComboboxCellEditor.class);
	
	private T selection = null;
	private final T[] availableValues;
	
	public ComboboxCellEditor(T[] availableValues) {
		this.availableValues = availableValues;
	}
	
	@Override
    public Object getCellEditorValue() {
        return this.selection;
    }
	
	@Override
    public Component getTableCellEditorComponent(JTable table, Object value,
            boolean isSelected, int row, int column) {
        this.selection = (T) value;
         
        JComboBox<T> comboCountry = new JComboBox<T>();
         
        for (T element : availableValues) {
            comboCountry.addItem(element);
        }
         
        comboCountry.setSelectedItem(selection);
        comboCountry.addActionListener(this);
         
        if (isSelected) {
            comboCountry.setBackground(table.getSelectionBackground());
        } else {
            comboCountry.setBackground(table.getSelectionForeground());
        }
         
        return comboCountry;
    }
	
	@SuppressWarnings("unchecked")
	@Override
    public void actionPerformed(ActionEvent event) {
        try {
        	JComboBox<T> comboCountry = (JComboBox<T>) event.getSource();
        	this.selection = (T) comboCountry.getSelectedItem();
        } catch (ClassCastException e) {
        	s_logger.error("Invalid cast of object {}", event.getSource(), e);
        }
    }

}
