package eventproductor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;

import eventmanager.EventService;
import xmltv.datatypes.Event;

public class EventProductorPoller {
	
	@Autowired
	EventService evService;
	
	public Message<?> askForEvents() {
		List<Event> lEvt = evService.getCloserEvents(new Date());
		if (lEvt == null) {
			lEvt = new ArrayList<Event>();
		}
		Message<?> resultMsg = MessageBuilder.
				withPayload(lEvt).build();
		return resultMsg;
	}
}
