package org.darugna.alessandro.firefly.settings;

import org.eclipse.paho.client.mqttv3.MqttConnectOptions;

/**
 * Holds MQTT options. NOT SYNCHRONIZED.
 * @author alessandro.darugna@gmail.com
 *
 */
public class MqttSettings {

	private static final Object s_lock = new Object();
	private static MqttSettings s_instance;
	
	private String m_brokerAddress;
	private String m_brokerPort;
	private String m_userName;
	private char[] m_passWord;
	private int m_mqttVersion;
	
	private MqttSettings() {
		m_brokerAddress = "iot.eclipse.org";
		m_brokerPort = "1883";
		m_userName = "username";
		m_passWord = new char[] {'P', 'a','s','s','w','o','r','d','!','1','2','3'};
		m_mqttVersion = MqttConnectOptions.MQTT_VERSION_3_1_1;
	}
	
	public static MqttSettings getSettings() {
		// TODO look for that double lock pattern
		synchronized(s_lock) {
			if (s_instance == null) {
				s_instance = new MqttSettings();
			}
		}
		
		return s_instance;
	}
	
	public String getBrokerAddress() {
		return m_brokerAddress;
	}
	
	public void setBrokerAddress(String brokerAddress) {
		m_brokerAddress = brokerAddress.trim();
	}
	
	public String getBrokerPort() {
		return m_brokerPort;
	}
	
	public void setBrokerPort(String brokerPort) {
		m_brokerPort = brokerPort.trim();
	}
	
	public String getUserName() {
		return m_userName;
	}

	public char[] getPassWord() {
		return m_passWord;
	}

	public void setPassWord(char[] passWord) {
		if (passWord == null) {
			for (int i = 0; i < m_passWord.length; ++i) {
				m_passWord[i] = 0;
			}
		}
		this.m_passWord = passWord;
	}
	
	public int getMqttVersion() {
		return m_mqttVersion;
	}
	
	public void setMqttVersion(int mqttVersion) {
		m_mqttVersion = mqttVersion;
	}
}
