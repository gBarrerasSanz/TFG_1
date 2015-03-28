package eventproductor;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import xmltv.datatypes.Event;
import eventmanager.EventService;

public class EventProductorPoller {
	
	@Autowired
	EventService evService;
	
	public List<Event> askForEvents() {
		List<Event> lEvt = evService.getCloserEvents(new Date());
		return lEvt;
	}
}
