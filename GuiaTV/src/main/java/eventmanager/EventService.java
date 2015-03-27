package eventmanager;

import java.util.Date;
import java.util.List;

import org.springframework.messaging.handler.annotation.Payload;

import xmltv.datatypes.Event;

public interface EventService {


	Event createEvent(Event evt);
	
	List<Event> createMultipleEvents(List<Event> listEvt);
	
	// Si no se pone el Payload, no funciona
	@Payload("new java.util.Date()")
	List<Event> getAllEvents();
	
	List<Event> getCloserEvents(Date now);

}