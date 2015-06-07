package org.darugna.alessandro.firefly;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class Callback1 implements MqttCallback {

	private static final Logger s_logger = LoggerFactory.getLogger(Callback1.class);
	
	private static final List<IMessageHandler> m_listMessageHandlers = Collections.synchronizedList(new ArrayList<IMessageHandler>());
	
	@Override
	public void connectionLost(Throwable arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deliveryComplete(IMqttDeliveryToken arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void messageArrived(String topic, MqttMessage message) throws Exception {
		s_logger.info("Message <{}> content: {}", topic, message);
		for (IMessageHandler mh : m_listMessageHandlers) {
			try {
				mh.handleMessage(topic, message);
			} catch (Throwable t) {
				s_logger.error("Unexpected error", t);
			}
		}
	}
	
	public void addMessageHandler(IMessageHandler messageHandler) {
		m_listMessageHandlers.add(messageHandler);
	}
	
	public void removeMessageHandler(IMessageHandler messageHandler) {
		m_listMessageHandlers.remove(messageHandler);
	}
	
}
