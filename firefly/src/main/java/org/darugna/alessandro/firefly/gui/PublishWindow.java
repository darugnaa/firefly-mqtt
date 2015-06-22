package org.darugna.alessandro.firefly.gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import javax.swing.JButton;
import javax.swing.JComboBox;
import java.awt.Insets;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JRadioButton;
import javax.swing.BoxLayout;

public class PublishWindow extends JFrame {

	private JPanel contentPane;

	/**
	 * Create the frame.
	 */
	public PublishWindow() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.NORTH);
		
		JRadioButton rdbtnKura = new JRadioButton("Kura");
		rdbtnKura.setSelected(true);
		panel.add(rdbtnKura);
		
		JRadioButton rdbtnJson = new JRadioButton("Json");
		rdbtnJson.setEnabled(false);
		panel.add(rdbtnJson);
		
		JRadioButton rdbtnNewRadioButton = new JRadioButton("Plain Text");
		rdbtnNewRadioButton.setEnabled(false);
		panel.add(rdbtnNewRadioButton);
		
		JPanel panel_1 = new JPanel();
		contentPane.add(panel_1, BorderLayout.SOUTH);
		
		JComboBox comboBox = new JComboBox();
		panel_1.add(comboBox);
		comboBox.setModel(new DefaultComboBoxModel(new String[] {"0", "1", "2"}));
		
		JButton btnNewButton = new JButton("Publish");
		panel_1.add(btnNewButton);
		
		PublishPanelKura ppk = new PublishPanelKura();
		contentPane.add(ppk, BorderLayout.CENTER);
	}

}
