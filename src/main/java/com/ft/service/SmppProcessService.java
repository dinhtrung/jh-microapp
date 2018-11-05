package com.ft.service;

import java.time.ZonedDateTime;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import com.cloudhopper.commons.charset.CharsetUtil;
import com.cloudhopper.smpp.SmppConstants;
import com.cloudhopper.smpp.pdu.BaseSm;
import com.cloudhopper.smpp.tlv.Tlv;
import com.cloudhopper.smpp.util.DeliveryReceipt;
import com.ft.config.ApplicationProperties;
import com.ft.service.dto.SmsDTO;

@Service
@Scope("singleton")
public class SmppProcessService {

	private static final Logger log = LoggerFactory.getLogger(SmppProcessService.class);

	@Autowired
	private ApplicationProperties props;

	@Autowired
	MessageChannel output;

	/**
	 * Handling delivery report
	 */
	public void processDLR(DeliveryReceipt dlr) {
//		Sms sms = smsMsgRepo.findOneByMessageId(dlr.getMessageId());
//		if (sms != null) {
//			if (dlr.getState() == SmppConstants.STATE_DELIVERED) {
//				sms.setDeliveredAt(ZonedDateTime.now());
//				sms.setStatus(SmsLog.STATE_DELIVERED);
//				sms.setTag(dlr.getText());
//			} else {
//				sms.setDeliveredAt(ZonedDateTime.now());
//				sms.setStatus(SmsLog.STATE_FAILED);
//				sms.setTag(dlr.getText());
//			}
//			smsMsgRepo.save(sms);
//		}
	}

	/**
	 * Convert MO SMS to a web trigger
	 * @throws Exception
	 */
	public void processMO(BaseSm message) {
		String msisdn = message.getSourceAddress().getAddress();
		String shortcode = message.getDestAddress().getAddress();
		String shortMsg = null;
		try {
			Tlv messagePayload = message.getOptionalParameter(SmppConstants.TAG_MESSAGE_PAYLOAD);
			if (messagePayload != null) {
				shortMsg = messagePayload.getValueAsString();
			} else {
				shortMsg = CharsetUtil.decode(message.getShortMessage(), CharsetUtil.CHARSET_GSM);
			}
			log.info("DeliverSM: {} - {} | {}", msisdn, shortcode, shortMsg);
		} catch (Exception e) {
			log.error("Cannot parse message {}", e);
		}
		shortMsg = shortMsg.trim();
		//log.info("DeliverSM: " + msisdn + " --> " + shortcode + " : [" + shortMsg + "]");
		SmsDTO smsReq = new SmsDTO()
				.destination(shortcode)
				.deliveredAt(ZonedDateTime.now())
				.status(SmsDTO.STATE_DELIVERED)
				.source(msisdn)
				.text(shortMsg)
				.tag(message.getServiceType());
		try {
			Tlv ussdOp = message.getOptionalParameter(SmppConstants.TAG_USSD_SERVICE_OP);
			if (ussdOp != null) {
				// FIXME: Should we map it to something more relevant?
				// This will decode the value to something like '18', '32' etc
				smsReq.setOp(ussdOp.getValueAsString());
			}
		} catch (Exception e) {
		}
		log.debug("=== Submit SMS to Broker: {}", smsReq);
		output.send(
				MessageBuilder.withPayload(smsReq)
				.setHeader(MessageHeaders.CONTENT_TYPE, "MO")
				.build()
		);
	}
}
