package com.ft.smpp;

import org.joda.time.DateTimeZone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cloudhopper.commons.charset.CharsetUtil;
import com.cloudhopper.smpp.SmppConstants;
import com.cloudhopper.smpp.pdu.BaseSm;

/*
 * #%L
 * ch-smpp
 * %%
 * Copyright (C) 2009 - 2014 Cloudhopper by Twitter
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */


import com.cloudhopper.smpp.pdu.PduResponse;
import com.cloudhopper.smpp.tlv.Tlv;
import com.cloudhopper.smpp.util.DeliveryReceipt;
import com.cloudhopper.smpp.util.DeliveryReceiptException;
import com.ft.service.SmppProcessService;

public class SmppClientMessageService {

	private final Logger log = LoggerFactory.getLogger(SmppClientMessageService.class);

    private SmppProcessService sessionService;

    public SmppClientMessageService(SmppProcessService service) {
            this.sessionService = service;
    }

	/** delivery receipt, or MO */
	public PduResponse received(OutboundClient client, BaseSm deliverSm) {
		// Handling MO SMS
		String shortMsg = null;
		try {
			Tlv messagePayload = deliverSm.getOptionalParameter(SmppConstants.TAG_MESSAGE_PAYLOAD);
			if (messagePayload != null){
				shortMsg = messagePayload.getValueAsString();
			} else {
				shortMsg = CharsetUtil.decode(deliverSm.getShortMessage(), CharsetUtil.CHARSET_GSM);
			}
			log.info("Got DeliverSM: {}", shortMsg);
		} catch (Exception e){
			log.error("Cannot parse message {}", e);
		}
//		try {
//			Tlv deliveryReceipt = deliverSm.getOptionalParameter(SmppConstants.TAG_RECEIPTED_MSG_ID);
//			if (deliveryReceipt != null){
//				DeliveryReceipt dlr = new DeliveryReceipt(deliveryReceipt.getValueAsString(), submitCount, deliveredCount, submitDate, doneDate, state, errorCode, text)
//			}
//		} catch (Exception e){
//
//		}
        try {
                DeliveryReceipt dlr = DeliveryReceipt.parseShortMessage(shortMsg, DateTimeZone.getDefault(), false);
                log.info("Got DLR: " + dlr);
                if (dlr.getMessageId() == null) throw new DeliveryReceiptException("Message ID is null!");
                sessionService.processDLR(dlr);
        } catch (DeliveryReceiptException e) {
                sessionService.processMO(deliverSm);
        }
        PduResponse response = deliverSm.createResponse();
        response.setResultMessage("OK");
        return response;
	}

}
