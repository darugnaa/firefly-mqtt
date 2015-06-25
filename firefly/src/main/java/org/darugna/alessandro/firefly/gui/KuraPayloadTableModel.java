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
        Object dataRow = dataVector.get(dataVector.size() - 1);
        s_logger.debug("DATAROW: {}", dataRow);
//        if (dataRow.elementAt(0) != null &&
//        		dataRow.elementAt(1) != null &&
//        		dataRow.elementAt(2) != null )
//        {
//           return true;
//        }
//        else return false;
        
        return false;
    }

    public void addEmptyRow() {
        dataVector.add(new String[]{null, null, null});
    }

}
