package org.darugna.alessandro.firefly;

import org.eclipse.paho.client.mqttv3.MqttMessage;

public interface IMessageHandler {
	
	public void handleMessage(String topic, MqttMessage message);
	
}
