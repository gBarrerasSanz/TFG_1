package guiatv.realtime.rtmpspying;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;

import guiatv.common.CommonUtility;
import guiatv.common.datatypes.Frame;
import guiatv.persistence.domain.Event_old;
import guiatv.persistence.domain.Schedule;
import guiatv.realtime.service.CapturedFramesGateway;
import guiatv.xmltv.transformer.XMLTVTransformer_old1;

public class RtmpSpyingOutDummy {
	
	private static final Logger logger = Logger.getLogger("debugLog");
	
	public List<Frame> receiveDummy(Message<List<Frame>> listFrameMsg) {
		return listFrameMsg.getPayload();
	}
}
