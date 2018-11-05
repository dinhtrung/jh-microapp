package com.ft.smpp;

import com.cloudhopper.smpp.pdu.BaseSm;
import com.cloudhopper.smpp.pdu.PduResponse;

public interface SmppMessageService {

	PduResponse received(OutboundClient client, BaseSm request);

}
