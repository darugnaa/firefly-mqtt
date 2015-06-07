package org.darugna.alessandro.firefly;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;

import java.awt.BorderLayout;

import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.Box;

import java.awt.FlowLayout;
import java.awt.Component;
import java.io.IOException;
import java.util.Properties;
import java.util.Set;

import javax.swing.Action;
import javax.swing.DefaultListModel;
import javax.swing.JDialog;
import javax.swing.JMenuItem;
import javax.swing.SpringLayout;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.BoxLayout;
import javax.swing.JTable;
import javax.swing.JList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.log4j.PropertyConfigurator;
import org.darugna.alessandro.firefly.gui.CheckBoxCellRenderer;
import org.darugna.alessandro.firefly.gui.SettingsDialog;
import org.darugna.alessandro.firefly.gui.TableFiller;
import org.darugna.alessandro.firefly.settings.MqttSettings;
import org.darugna.alessandro.firefly.settings.SubscriptionSettings;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttTopic;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.awt.Dialog.ModalExclusionType;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.ListSelectionModel;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JScrollPane;
import javax.swing.table.DefaultTableModel;

import java.awt.Toolkit;
import java.beans.PropertyChangeListener;

import javax.swing.JPopupMenu;

public class MainWindow {

	private static Logger s_logger = LoggerFactory.getLogger(MainWindow.class);
	private MqttClient m_client;
	private Callback1 m_callbackHandler;
	private TopicAutodiscover m_topicAutodiscover;
	
	private JFrame frmFireflyMqtt;
	private JTextField textFieldAddress;
	private JTextField txtSubscribe;
	private JTable table;
	private JTextField textFieldPort;

	/**
	 * Launch the application.
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		// http://stackoverflow.com/questions/2288876/how-to-configure-log4j-with-a-properties-file
		Properties props = new Properties();
		props.load(MainWindow.class.getResourceAsStream("/log4j.properties"));
		PropertyConfigurator.configure(props);

		s_logger.info("Starting Firefly");
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainWindow window = new MainWindow();
					window.frmFireflyMqtt.setVisible(true);
				} catch (Exception e) {
					s_logger.error("Unexpected error", e);
				}
			}
		});
	}

	//
	// Private methods
	//
	
	private void startMqttClient(JButton source) {
		if (m_client == null) {
			try {
				String broker = "tcp://" + textFieldAddress.getText().trim() + ":" + textFieldPort.getText().trim();
				m_client = new MqttClient(broker, "Firefly-6759", new MemoryPersistence());
				m_client.setCallback(m_callbackHandler);
		        MqttConnectOptions connOpts = new MqttConnectOptions();
		        connOpts.setCleanSession(true);
		        connOpts.setUserName(MqttSettings.getSettings().getUserName());
		        connOpts.setPassword(MqttSettings.getSettings().getPassWord());
		        connOpts.setMqttVersion(MqttSettings.getSettings().getMqttVersion());
		        s_logger.info("Connecting to broker {} ", broker);
		        m_client.connect(connOpts);
		        //m_client.connectWithResult(connOpts);
		        //m_client.subscribe("owntracks/#");
		        source.setText("Disconnect");
		        s_logger.info("Connected");
			} catch (MqttException e) {
				s_logger.error("Problem during connection", e);
				m_client = null;
			} catch (NumberFormatException e) {
				s_logger.warn("Invalid port <{}>", textFieldPort.getText().trim());
				m_client = null;
			}
		} else {
			if (m_client.isConnected()) {
				s_logger.debug("Asking to disconnect");
				try {
					m_client.disconnect();
					m_client = null;
					s_logger.info("Disconnected");
					source.setText("Connect");
				} catch (MqttException e) {
					s_logger.error("Disconnect error", e);
				}
			}
		}
	}
	
	
	private boolean subscribeAndUpdateGui(String topic) {
		boolean subscribedOk = false;
		try {
			m_client.subscribe(topic, 2);
			
			SubscriptionSettings subscriptions = SubscriptionSettings.getSettings();
			if (!subscriptions.isKnownTopic(topic)) {
				JCheckBox checkbox = new JCheckBox(topic, true);
				m_topicAutodiscover.getListModel().addElement(checkbox);
			}
				
			subscriptions.setSubscribedStatus(topic, true);
			subscribedOk = true;
			s_logger.info("Subscribed <{}>", topic);
		} catch (MqttException e) {
			s_logger.error("Subscription to <{}> failed", e);
		} catch (IllegalArgumentException e) {
			s_logger.warn("Invalid subscribe topic <{}>", topic);
		}
		return subscribedOk;
	}
	
	
	private boolean unsubscribeAndUpdateGui(String topic) {
		boolean unsubscribedOk = false;
		try {
			m_client.unsubscribe(topic);
			
			SubscriptionSettings subscriptions = SubscriptionSettings.getSettings();
			subscriptions.setSubscribedStatus(topic, false);
			unsubscribedOk = true;
			s_logger.info("Unsubscribed <{}>", topic);
		} catch (MqttException e) {
			s_logger.error("Unsubscription to <{}> failed", e);
		} catch (IllegalArgumentException e) {
			s_logger.warn("Invalid unsubscribe topic <{}>", topic);
		}
		
		return unsubscribedOk;
	}
	
	
	//
	// Methods created by WindowBuilder and customized a little
	//
	
	/**
	 * Create the application.
	 */
	public MainWindow() {
		s_logger.debug("Initializing internal components");
		m_topicAutodiscover = new TopicAutodiscover();
		m_callbackHandler = new Callback1();
		MqttSettings.getSettings();	// TODO Load settings from disk
		SubscriptionSettings.getSettings();
		
		s_logger.debug("Initializing main window");
		initialize();
		
		s_logger.debug("Initializing components relationships");
		TableFiller tf = new TableFiller((DefaultTableModel)table.getModel());
		m_callbackHandler.addMessageHandler(tf);
		// Populate MainWindow components values
		textFieldAddress.setText(MqttSettings.getSettings().getBrokerAddress());
		textFieldPort.setText(MqttSettings.getSettings().getBrokerPort());
		Set<String> subs = SubscriptionSettings.getSettings().getKnownTopics();
		for (String topic : subs) {
			JCheckBox checkbox = new JCheckBox(topic, SubscriptionSettings.getSettings().isSubscribedTo(topic));
			m_topicAutodiscover.getListModel().addElement(checkbox);
		}
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmFireflyMqtt = new JFrame();
		// http://stackoverflow.com/questions/11735770/how-to-put-a-jframe-icon
		frmFireflyMqtt.setIconImage(Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("/icon.png")));
		frmFireflyMqtt.setTitle("Firefly MQTT");
		frmFireflyMqtt.setBounds(100, 100, 600, 387);
		frmFireflyMqtt.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel panel = new JPanel();
		frmFireflyMqtt.getContentPane().add(panel, BorderLayout.NORTH);
		
		textFieldAddress = new JTextField();
		panel.add(textFieldAddress);
		textFieldAddress.setColumns(20);
		
		JButton btnConnect = new JButton("Connect");
		btnConnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				startMqttClient((JButton)arg0.getSource());
			}
		});
		
		textFieldPort = new JTextField();
		panel.add(textFieldPort);
		textFieldPort.setColumns(10);
		panel.add(btnConnect);
		
		JButton btnOptions = new JButton("Options");
		btnOptions.addActionListener(new ActionListener() {
			
			// http://stackoverflow.com/questions/1481405/how-to-make-a-jframe-modal-in-swing-java
			public void actionPerformed(ActionEvent arg0) {
				final JDialog settingsFrame = new SettingsDialog(frmFireflyMqtt);
				settingsFrame.setVisible(true);
			}
		});
		panel.add(btnOptions);
		
		JButton btnClearTable = new JButton("Clear Table");
		btnClearTable.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (table == null) {
					s_logger.warn("Table component is null");
					return;
				}
				((DefaultTableModel)table.getModel()).getDataVector().clear();
				table.repaint();
			}
		});
		panel.add(btnClearTable);
		
		JPanel panel_r = new JPanel();
		frmFireflyMqtt.getContentPane().add(panel_r, BorderLayout.EAST);
		
		Box verticalBox = Box.createVerticalBox();
		
		Box verticalBox_2 = Box.createVerticalBox();
		
		txtSubscribe = new JTextField();
		verticalBox_2.add(txtSubscribe);
		txtSubscribe.setText("Subscribe");
		txtSubscribe.setColumns(10);
		
		JButton btnSubscribe = new JButton("Subscribe");
		btnSubscribe.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (m_client == null) {
					s_logger.warn("Not connected");
					//TODO fix the enable of this button
					return;
				}
				String topicString = txtSubscribe.getText();
				if (subscribeAndUpdateGui(topicString)) {
					txtSubscribe.setText(null);
				}
			}
		});
		verticalBox_2.add(btnSubscribe);
		panel_r.setLayout(new BorderLayout(0, 0));
		panel_r.add(verticalBox, BorderLayout.CENTER);
		
		final JList<JCheckBox> list = new JList<JCheckBox>();
		list.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (!list.isEnabled()) {
					return;
				}
				
				int index = list.locationToIndex(e.getPoint());

				if (index != -1) {
					JCheckBox checkbox = list.getModel().getElementAt(index);
					// Ignore middle and right clicks
					if (e.getButton() != MouseEvent.BUTTON1) {
						list.setSelectedIndex(index);
						return;
					}
					s_logger.debug("Changing Selected for element {}", index);
					String topic = checkbox.getText();
					if (checkbox.isSelected()) {
						// SELECTED -> Unsubscribe and clear
						if (unsubscribeAndUpdateGui(topic)) {
							checkbox.setSelected(false);
						}
					} else {
						// UNSELECTED -> Subscribe and set
						if (subscribeAndUpdateGui(topic)) {
							checkbox.setSelected(true);
						}
					}
					list.repaint();
				}
			}
		});
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setCellRenderer(new CheckBoxCellRenderer());
		list.setModel(m_topicAutodiscover.getListModel());
		// http://stackoverflow.com/questions/13621261/add-a-jlist-to-a-jscrollpane
		JScrollPane scrollPane = new JScrollPane(list);
		
		//
		// Popum menu in topic list
		//
		JPopupMenu popupMenu = new JPopupMenu();
		JMenuItem removeMenuItem = new JMenuItem("Remove");
		removeMenuItem.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				JCheckBox checkbox = list.getSelectedValue();
				if (unsubscribeAndUpdateGui(checkbox.getText())) {
					m_topicAutodiscover.getListModel().removeElement(checkbox);
				}
			}
		});
		popupMenu.add(removeMenuItem);
		addPopup(list, popupMenu);
		verticalBox.add(scrollPane);
		panel_r.add(verticalBox_2, BorderLayout.SOUTH);
		
		JButton btnTopicAutodiscover = new JButton("Topic Autodiscover");
		btnTopicAutodiscover.setMnemonic('a');
		btnTopicAutodiscover.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JButton btn = (JButton)arg0.getSource();
				try {
					if (btn.getText().equals("Topic Autodiscover")) {
						s_logger.debug("Adding Autodiscover handler");
						m_callbackHandler.addMessageHandler(m_topicAutodiscover);
						m_client.subscribe(new String[] {"#", "$EDC/#", "$/#"},
											new int[] {2, 2, 2});
						btn.setText("Stop");
					} else {
						s_logger.debug("Removing Autodiscover handler");
						m_callbackHandler.removeMessageHandler(m_topicAutodiscover);
						m_client.unsubscribe(new String[] {"#", "$EDC/#", "$/#"});
						btn.setText("Topic Autodiscover");
					}
				} catch (MqttException e) {
					s_logger.error("Azz", e);
				}
			}
		});
		panel_r.add(btnTopicAutodiscover, BorderLayout.NORTH);
		

		
		table = new JTable();
		table.setFillsViewportHeight(true);
		// TODO Fix by hand
		table.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"Kura", "Timestamp", "Topic", "Payload", "Body", "QoS", "Retained"
			}
		) {
			Class[] columnTypes = new Class[] {
				String.class, Object.class, String.class, String.class, Object.class, Integer.class, String.class
			};
			public Class getColumnClass(int columnIndex) {
				return columnTypes[columnIndex];
			}
			boolean[] columnEditables = new boolean[] {
				false, true, true, true, true, true, true
			};
			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		});
		table.getColumnModel().getColumn(0).setResizable(false);
		table.getColumnModel().getColumn(0).setPreferredWidth(40);
		table.getColumnModel().getColumn(0).setMinWidth(35);
		table.getColumnModel().getColumn(0).setMaxWidth(40);
		table.getColumnModel().getColumn(5).setResizable(false);
		table.getColumnModel().getColumn(5).setPreferredWidth(60);
		table.getColumnModel().getColumn(5).setMinWidth(60);
		table.getColumnModel().getColumn(5).setMaxWidth(60);
		table.getColumnModel().getColumn(6).setResizable(false);
		table.getColumnModel().getColumn(6).setPreferredWidth(60);
		table.getColumnModel().getColumn(6).setMinWidth(60);
		table.getColumnModel().getColumn(6).setMaxWidth(60);
		
		JScrollPane scrollPaneTable = new JScrollPane(table);
		frmFireflyMqtt.getContentPane().add(scrollPaneTable, BorderLayout.CENTER);
	}

	private static void addPopup(Component component, final JPopupMenu popup) {
		component.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}
			public void mouseReleased(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}
			private void showMenu(MouseEvent e) {
				popup.show(e.getComponent(), e.getX(), e.getY());
			}
		});
	}
}