package guiatv.xmltv.transformer;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;

import guiatv.domain.Event;
import guiatv.eventmanager.EventService;

public class EventCreator {
	
	@Autowired
	EventService evService;
	
	public void createEvents(Message<List<Event>> msg) {
		evService.createMultipleEvents(msg.getPayload());
	}
}
