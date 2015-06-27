package org.darugna.alessandro.firefly.gui;

import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.event.ChangeEvent;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("serial")
public class PublishPanelKura extends JTable {

    private static Logger s_logger = LoggerFactory.getLogger(PublishPanelKura.class);
    
    private final DefaultCellEditor comboBoxTypesCellEditor;
    private final KuraPayloadTableModel kuraPayloadTableModel;

	/**
	 * Create the panel.
	 */
	public PublishPanelKura() {
		super();
		setFillsViewportHeight(true);
		
		kuraPayloadTableModel = new KuraPayloadTableModel();
		setModel(kuraPayloadTableModel);
		
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
	
	@Override
	public void tableChanged(javax.swing.event.TableModelEvent e) {
		super.tableChanged(e);
		s_logger.debug("tableChanged");
		if (e.getType() == TableModelEvent.UPDATE && e.getFirstRow() >= 0) {
			if (kuraPayloadTableModel.hasEmptyRow()) {
				kuraPayloadTableModel.addEmptyRow();
			}
		}
	};
	
	/*
	@Override
	public void editingStopped(ChangeEvent e) {
		super.editingStopped(e);
		s_logger.debug("Editing stopped}");
		if (!kuraPayloadTableModel.hasEmptyRow()) {
			kuraPayloadTableModel.addEmptyRow();
		}
	}
	*/

}
