package org.darugna.alessandro.firefly.gui;

import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("serial")
public class PublishPanelKura extends JTable {

    private static Logger s_logger = LoggerFactory.getLogger(PublishPanelKura.class);
    
    private final DefaultCellEditor comboBoxTypesCellEditor;

	/**
	 * Create the panel.
	 */
	public PublishPanelKura() {
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
		
        final JComboBox<String> comboBox = new JComboBox<>();
        comboBox.addItem("");
        comboBox.addItem("String");
        comboBox.addItem("Integer");
        comboBox.addItem("Boolean");
        comboBox.addItem("Float");
        comboBoxTypesCellEditor = new DefaultCellEditor(comboBox);

	}
	
	public TableCellEditor getCellEditor(int row, int column) {
        s_logger.debug("getCellEditor({},{})", row, column);
        if (column == 1) {
            return comboBoxTypesCellEditor;
        } else {
            return super.getCellEditor(row, column);
        }

	}

}
