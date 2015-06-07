package org.darugna.alessandro.firefly.gui;

import java.io.IOException;
import java.util.Date;

import javax.swing.table.DefaultTableModel;

import org.darugna.alessandro.firefly.IMessageHandler;
import org.eclipse.kura.KuraInvalidMessageException;
import org.eclipse.kura.core.cloud.CloudPayloadProtoBufDecoderImpl;
import org.eclipse.kura.message.KuraPayload;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TableFiller implements IMessageHandler {

	private static final Logger s_logger = LoggerFactory.getLogger(TableFiller.class);
	private static final String CHECKMARK = "✓";
	private static final String EMPTY = "";
	private final DefaultTableModel m_tableModel;
	
	public TableFiller(DefaultTableModel tableModel) {
		m_tableModel = tableModel;
	}
	
	@Override
	public void handleMessage(String topic, MqttMessage message) {
		try {
			// try to decode the message into an KuraPayload					
			KuraPayload payload = (new CloudPayloadProtoBufDecoderImpl(message.getPayload())).buildFromByteArray();
			Object[] row = { CHECKMARK,
						payload.getTimestamp(),
			 			topic,
			 			payload.metrics().size() > 0 ? payload.metrics().toString() : EMPTY,
			 			payload.getBody(),
			 			Integer.valueOf(message.getQos()),
			 			message.isRetained() ? CHECKMARK  : EMPTY
			};
			m_tableModel.addRow(row);
			
		} catch (KuraInvalidMessageException | IOException e) {
			// Wrap the received bytes payload into an KuraPayload					
			s_logger.debug("Received message on topic <{}> that could not be decoded. Threating it as plain text", topic);
			Object[] row = { EMPTY,
					new Date().toString(),
					topic,
					new String(message.getPayload()),
					EMPTY,
					Integer.valueOf(message.getQos()),
					message.isRetained() ? CHECKMARK : EMPTY
			};
			m_tableModel.addRow(row);
		}
	}

}
