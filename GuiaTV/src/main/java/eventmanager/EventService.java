package eventmanager;

import java.util.List;

import org.springframework.messaging.handler.annotation.Payload;

import xmltv.datatypes.Event;

public interface EventService {

	/**
	 * Creates a {@link Event} instance from the {@link Event} instance passed
	 *
	 * @param person created EventoTV instance, it will contain the generated primary key and the formated name
	 * @return The persisted Entity
	 */
	Event createEvent(Event evt);
	
	
	List<Event> createMultipleEvents(List<Event> listEvt);
	
	/**
	 *
	 * @return the matching {@link Event} record(s)
	 */
	@Payload("new java.util.Date()")
	List<Event> findEvent();

}