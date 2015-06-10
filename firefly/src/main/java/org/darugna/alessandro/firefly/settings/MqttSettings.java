package org.darugna.alessandro.firefly.settings;

import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;

/**
 * Holds MQTT options. NOT SYNCHRONIZED.
 * @author alessandro.darugna@gmail.com
 *
 */
public class MqttSettings {

	private static Logger s_logger = LoggerFactory.getLogger(MqttSettings.class);
			
	private static final Object s_lock = new Object();
	private static MqttSettings s_instance;
	private static final String SETTINGS_FILE_NAME = "settings.json";
	
	private static final String SETTINGS_BROKER_ADDRESS     = "brokerAddress";
	private static final String SETTINGS_BROKER_PORT        = "brokerPort";
	private static final String SETTINGS_BROKER_USERNAME    = "brokerUsername";
	private static final String SETTINGS_BROKER_PASSWORD    = "brokerPassword";
	private static final String SETTINGS_MQTT_VERSION       = "mqttVersion";
	private static final String SETTINGS_CLIENT_ID          = "clientId";
	
	private String m_brokerAddress;
	private String m_brokerPort;
	private String m_brokerUsername;
	private char[] m_brokerPassword;
	private int m_mqttVersion;
	private String m_clientId;
	
	private MqttSettings() {
		try {
			byte[] content = Files.readAllBytes(Paths.get(SETTINGS_FILE_NAME));
			JSONObject jsonSettings = new JSONObject(new String(content, "UTF-8"));
			m_brokerAddress = jsonSettings.getString(SETTINGS_BROKER_ADDRESS);
			m_brokerPort = jsonSettings.getString(SETTINGS_BROKER_PORT);
			m_brokerUsername = jsonSettings.getString(SETTINGS_BROKER_USERNAME);
			//m_brokerPassword = jsonSettings.getJSONArray(SETTINGS_BROKER_PASSWORD);
			JSONArray pass = jsonSettings.getJSONArray(SETTINGS_BROKER_PASSWORD);
			m_brokerPassword = new char[pass.length()];
			for (int i = 0; i < pass.length(); ++i) {
				String character = pass.getString(i);
				if (character.length() != 1) {
					throw new RuntimeException("Cannot decode password");
				}
				m_brokerPassword[i] = character.charAt(0);
			}
			pass = null;
			// Mqtt protocol version is sanitized after reading.
			m_mqttVersion = jsonSettings.getInt(SETTINGS_MQTT_VERSION);
			if (m_mqttVersion != MqttConnectOptions.MQTT_VERSION_3_1 &&
					m_mqttVersion != MqttConnectOptions.MQTT_VERSION_3_1_1 &&
					m_mqttVersion != MqttConnectOptions.MQTT_VERSION_DEFAULT) {
				s_logger.warn("Invalid MQTT protocol version in settings <{}>, defaulting to 3.1.1", m_mqttVersion);
				m_mqttVersion = MqttConnectOptions.MQTT_VERSION_3_1_1;
			}
			m_clientId = jsonSettings.getString(SETTINGS_CLIENT_ID);
			s_logger.info("Settings loaded from {}", SETTINGS_FILE_NAME);
		} catch (IOException e) {
			s_logger.warn("Unable to load settings file {}, using default ones", e.getMessage());
			loadDefaultSettings();
		} catch (Throwable t) {
			// This catch is only for debug purposes
			s_logger.error("UNEXPECTED ERROR", t);
			loadDefaultSettings();
		}
	}
	
	private void loadDefaultSettings() {
		m_brokerAddress = "iot.eclipse.org";
		m_brokerPort = "1883";
		m_brokerUsername = "username";
		m_brokerPassword = new char[] {'p', 'a','s','s','w','o','r','d'};
		m_mqttVersion = MqttConnectOptions.MQTT_VERSION_3_1_1;
		m_clientId = "Firefly" + String.valueOf((new Date()).getTime());
		if (m_clientId.length() > 22) {
			m_clientId = m_clientId.substring(0, 22);
			s_logger.debug("Shortened Client Id to {}");
		}
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
	
	public void saveToDisk() throws IOException {
		JSONObject jsonSettings = new JSONObject();
		jsonSettings.put(SETTINGS_BROKER_ADDRESS, m_brokerAddress);
		jsonSettings.put(SETTINGS_BROKER_PORT, m_brokerPort);
		jsonSettings.put(SETTINGS_BROKER_USERNAME, m_brokerUsername);
		jsonSettings.put(SETTINGS_BROKER_PASSWORD, m_brokerPassword);
		jsonSettings.put(SETTINGS_MQTT_VERSION, Integer.valueOf(m_mqttVersion));
		jsonSettings.put(SETTINGS_CLIENT_ID, m_clientId);
		
		Files.write(Paths.get(SETTINGS_FILE_NAME), jsonSettings.toString(1).getBytes("UTF-8"));
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
		return m_brokerUsername;
	}

	public char[] getPassWord() {
		return m_brokerPassword;
	}

	public void setPassWord(char[] passWord) {
		if (passWord == null) {
			for (int i = 0; i < m_brokerPassword.length; ++i) {
				m_brokerPassword[i] = 0;
			}
		}
		this.m_brokerPassword = passWord;
	}
	
	public int getMqttVersion() {
		return m_mqttVersion;
	}
	
	public void setMqttVersion(int mqttVersion) {
		m_mqttVersion = mqttVersion;
	}
	
	public String getClientId() {
		return m_clientId;
	}
	
	public void setClientId(String clientId) {
		m_clientId = clientId;
	}
}
