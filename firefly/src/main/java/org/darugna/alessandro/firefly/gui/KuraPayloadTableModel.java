package org.darugna.alessandro.firefly.gui;

import java.util.Vector;

import javax.swing.table.DefaultTableModel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KuraPayloadTableModel extends DefaultTableModel {

	private static Logger s_logger = LoggerFactory.getLogger(KuraPayloadTableModel.class);
	
	public KuraPayloadTableModel() {
		super(new Object[][] {
					{null, null, null},
				},
				new Object[] {
					"Metric", "Type", "Value"
				}
			);
	}
	
//	public Class getColumnClass(int columnIndex) {
//		return String.class;
//	};
	
	public boolean hasEmptyRow() {
        if (dataVector.size() == 0) return false;
        Vector dataRow = (Vector) dataVector.get(dataVector.size() - 1);
        s_logger.debug("DATAROW {}: {}", dataRow.getClass(), dataRow);
        if (dataRow.elementAt(0) != null &&
        		dataRow.elementAt(1) != null &&
        		dataRow.elementAt(2) != null )
        {
           return true;
        }
        else return false;
    }

    public void addEmptyRow() {
    	addRow((Vector)null);
    }

}
