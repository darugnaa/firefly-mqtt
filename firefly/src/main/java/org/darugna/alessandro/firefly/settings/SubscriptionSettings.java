package org.darugna.alessandro.firefly.settings;

import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import org.eclipse.paho.client.mqttv3.MqttTopic;

public class SubscriptionSettings {

	private static final Object s_lock = new Object();
	private static SubscriptionSettings s_instance;
	
	// http://stackoverflow.com/questions/663374/java-ordered-map
	private SortedMap<String, Boolean> m_subscriptions;
	
	private SubscriptionSettings() {
		// TODO when realoading settings check all topics
		m_subscriptions = new TreeMap<String, Boolean>();
		m_subscriptions.put("$EDC/#", Boolean.FALSE);
		m_subscriptions.put("$/#", Boolean.FALSE);
		m_subscriptions.put("#", Boolean.FALSE);
	}
	
	public static SubscriptionSettings getSettings() {
		// TODO look for that double lock pattern
		synchronized(s_lock) {
			if (s_instance == null) {
				s_instance = new SubscriptionSettings();
			}
		}
		
		return s_instance;
	}
	
	public void setSubscribedStatus(String topic, boolean subscription) {
		MqttTopic.validate(topic, true);
		m_subscriptions.put(topic, Boolean.valueOf(subscription));
	}
	
	/**
	 * Does <b>NOT</b> do any topic matching
	 * @param topic
	 * @return
	 */
	public boolean isSubscribedTo(String topic) {
		MqttTopic.validate(topic, true);
		if (m_subscriptions.containsKey(topic)) {
			return m_subscriptions.get(topic).booleanValue();
		}
		return false;
	}
	
	public Set<String> getKnownTopics() {
		return m_subscriptions.keySet();
	}
	
	public boolean isKnownTopic(String topic) {
		return m_subscriptions.containsKey(topic);
	}
	
}
