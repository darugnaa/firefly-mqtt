package org.darugna.alessandro.firefly.gui.actions;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JTable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class CopyElementActionListener implements ActionListener {

	private static Logger s_logger = LoggerFactory.getLogger(CopyElementActionListener.class);
			
	public static final int COPY_TOPIC = 1;
	public static final int COPY_PAYLOAD = 2;
	public static final int COPY_TOPIC_AND_PAYLOAD = 3;
	public static final int COPY_BODY = 4;
	
	
	private final JTable m_table;
	private int m_copyTarget;
	
	public CopyElementActionListener(final JTable table, int copyTarget) {
		m_table = table;
		m_copyTarget = copyTarget;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		int selectedRowIndex = m_table.getSelectedRow();
		if (selectedRowIndex < 0) {
			s_logger.debug("No rows selected");
			return;
		}
		s_logger.debug("Performing copy action targeting {}", m_copyTarget);
		// topic 2
		// payload 3
		// body 4
		String valueToCopy = null;
		switch (m_copyTarget) {
		case COPY_TOPIC:
			valueToCopy = (String) m_table.getModel().getValueAt(selectedRowIndex, 2);
			break;
		case COPY_PAYLOAD:
			valueToCopy = (String) m_table.getModel().getValueAt(selectedRowIndex, 3);
			break;
		case COPY_TOPIC_AND_PAYLOAD:
			valueToCopy = (String) m_table.getModel().getValueAt(selectedRowIndex, 2) +
							" " +
							(String) m_table.getModel().getValueAt(selectedRowIndex, 3);
			break;
		case COPY_BODY:
			valueToCopy = (String) m_table.getModel().getValueAt(selectedRowIndex, 4);
			break;
		}
		
		// http://stackoverflow.com/questions/6710350/copying-text-to-the-clipboard-using-java
		if (valueToCopy != null) {
			StringSelection stringSelection = new StringSelection(valueToCopy);
			Clipboard clpbrd = Toolkit.getDefaultToolkit().getSystemClipboard();
			clpbrd.setContents(stringSelection, null);
			s_logger.debug("Copy to clipboard ok");
		} else {
			s_logger.debug("Nothing to copy");
		}
	}

}
