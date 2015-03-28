package eventproductor;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;

import common.CommonUtility;

import xmltv.datatypes.Event;
import xmltv.transformer.XMLTVTransformer;
import eventmanager.EventService;

public class EventProductorPollerReply {
	
	@Autowired
	CommonUtility utils;
	
	static Logger log = Logger.getLogger(XMLTVTransformer.class.getName());
	
	public void receive(List<Event> lEvtMsg) {
		log.debug(utils.lEvtToStr(lEvtMsg));
	}
}
