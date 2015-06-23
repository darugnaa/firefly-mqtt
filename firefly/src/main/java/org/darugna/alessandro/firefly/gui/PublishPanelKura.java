package org.darugna.alessandro.firefly.gui;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

@SuppressWarnings("serial")
public class PublishPanelKura extends JTable {

	/**
	 * Create the panel.
	 */
	public PublishPanelKura() implements TableModelLister {
		super();
		setFillsViewportHeight(true);
		
		DefaultTableModel kuraTableModel = new DefaultTableModel(
				new Object[][] {
						{null, null, null},
						{null, null, null},
					},
					new String[] {
						"Metric", "Type", "Value"
					}
				) {
					final Class[] columnTypes = new Class[] {
						String.class, Class.class, String.class
					};
					public Class getColumnClass(int columnIndex) {
						return columnTypes[columnIndex];
					};
					 
				};
		
		setModel(kuraTableModel);
		kuraTableModel.addTableModelListener(this);
		
		setDefaultEditor(Class.class,
				new ComboboxCellEditor<Class>(new Class[]{String.class, Integer.class, Float.class, Double.class}));
	}

}
