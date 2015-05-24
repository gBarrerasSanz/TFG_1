package guiatv.scheduleproducer;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;

import guiatv.common.CommonUtility;
import guiatv.eventmanager.ImgProcessingGateway;
import guiatv.persistence.domain.Event_old;
import guiatv.xmltv.transformer.XMLTVTransformer_old1;

public class ScheduleProducerPollerReply {
	
	static Logger log = Logger.getLogger(XMLTVTransformer_old1.class.getName());
	
	public void receive(Message<List<Event_old>> lEvtMsg) {
//		log.debug(utils.lEvtToStr(lEvtMsg.getPayload()));
	}
}
