package org.darugna.alessandro.firefly;

import java.util.Collections;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.swing.DefaultListModel;
import javax.swing.JCheckBox;

import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TopicAutodiscover implements IMessageHandler {

	private static Logger s_logger = LoggerFactory.getLogger(TopicAutodiscover.class);
			
	private SortedSet<String> m_discoveredTopics;
	
	private DefaultListModel<JCheckBox> m_listModel;
	
	public TopicAutodiscover() {
		m_discoveredTopics = Collections.synchronizedSortedSet(new TreeSet<String>());
		m_listModel = new DefaultListModel<JCheckBox>();
	}
	
	public DefaultListModel<JCheckBox> getListModel() {
		return m_listModel;
	}
	

	@Override
	public void handleMessage(String topic, MqttMessage message) {
		if (m_discoveredTopics.add(topic)) {
			JCheckBox checkBox = new JCheckBox(topic);
			m_listModel.addElement(checkBox);
			s_logger.debug("Added {} to the autodiscovered topics", topic);
		};
	}
	
}
