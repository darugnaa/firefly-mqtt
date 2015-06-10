package org.darugna.alessandro.firefly.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.factories.FormFactory;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;

import org.darugna.alessandro.firefly.settings.MqttSettings;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.IOException;

@SuppressWarnings("serial")
public class SettingsDialog extends JDialog implements ActionListener {

	private static Logger s_logger = LoggerFactory.getLogger(SettingsDialog.class);
			
	private final JPanel contentPanel = new JPanel();
	private JTextField textFieldBroker;
	private JTextField textFieldPort;
	private JTextField textFieldUsername;
	private JTextField textFieldPassword;
	private JComboBox<String> comboBoxMqttVersion;
	private JTextField textFieldClientId;

	/**
	 * Create the dialog.
	 */
	public SettingsDialog(JFrame parent) {
		super(parent, "Setting", true);
		s_logger.debug("Initializing settings window");
		initialize();
		s_logger.debug("Populating components");
		populateComponents();
	}
	
	/**
	 * Fill the frame with actual values 
	 */
	private void populateComponents() {
		MqttSettings settings = MqttSettings.getSettings();
		textFieldClientId.setText(settings.getClientId());
		if (settings.getMqttVersion() == MqttConnectOptions.MQTT_VERSION_3_1_1) {
			comboBoxMqttVersion.setSelectedIndex(0);
		} else {
			comboBoxMqttVersion.setSelectedIndex(1);
		}
	}
	
	private void initialize() {
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("max(44dlu;default)"),
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),},
			new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,}));
		{
			JLabel lblBroker = new JLabel("Broker");
			contentPanel.add(lblBroker, "2, 2, 2, 1, right, default");
		}
		{
			textFieldBroker = new JTextField();
			contentPanel.add(textFieldBroker, "4, 2, fill, default");
			textFieldBroker.setColumns(10);
		}
		{
			JLabel lblPort = new JLabel("Port");
			contentPanel.add(lblPort, "2, 4, right, default");
		}
		{
			textFieldPort = new JTextField();
			contentPanel.add(textFieldPort, "4, 4, fill, default");
			textFieldPort.setColumns(10);
		}
		{
			JLabel lblUsername = new JLabel("Username");
			contentPanel.add(lblUsername, "2, 6, right, default");
		}
		{
			textFieldUsername = new JTextField();
			contentPanel.add(textFieldUsername, "4, 6, fill, default");
			textFieldUsername.setColumns(10);
		}
		{
			JLabel lblPassword = new JLabel("Password");
			contentPanel.add(lblPassword, "2, 8, right, default");
		}
		{
			textFieldPassword = new JTextField();
			contentPanel.add(textFieldPassword, "4, 8, fill, default");
			textFieldPassword.setColumns(10);
		}
		{
			JLabel lblProtocolVersion = new JLabel("Protocol Version");
			contentPanel.add(lblProtocolVersion, "2, 10, right, default");
		}
		{
			comboBoxMqttVersion = new JComboBox<String>();
			comboBoxMqttVersion.setModel(new DefaultComboBoxModel<String>(new String[] {"3.1.1", "3.1"}));
			contentPanel.add(comboBoxMqttVersion, "4, 10, fill, default");
		}
		{
			JLabel lblClientId = new JLabel("Client Id");
			contentPanel.add(lblClientId, "2, 12, right, default");
		}
		{
			textFieldClientId = new JTextField();
			contentPanel.add(textFieldClientId, "4, 12, fill, default");
			textFieldClientId.setColumns(10);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(this);
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(this);
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		if (arg0.getActionCommand().equals("OK")) {
			try {
				MqttSettings.getSettings().saveToDisk();
				s_logger.info("MqttSettings saved to disk");
			} catch (IOException e) {
				s_logger.error("Unable to persist settings on disk", e);
			}
		}
		
		dispose();
	}

}
