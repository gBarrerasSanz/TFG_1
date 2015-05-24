package guiatv.eventproducer;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;

import guiatv.common.CommonUtility;
import guiatv.eventmanager.ImgProcessingGateway;
import guiatv.persistence.domain.Event;
import guiatv.xmltv.transformer.XMLTVTransformer_old1;

public class EventProducerPollerReply {
	
	static Logger log = Logger.getLogger(XMLTVTransformer_old1.class.getName());
	
	public void receive(Message<List<Event>> lEvtMsg) {
//		log.debug(utils.lEvtToStr(lEvtMsg.getPayload()));
	}
}
