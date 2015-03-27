package eventmanager;

import java.util.Date;
import java.util.List;
import xmltv.datatypes.Event;

public interface EventService {


	Event createEvent(Event evt);
	
	List<Event> createMultipleEvents(List<Event> listEvt);
	
	List<Event> getAllEvents();
	
	List<Event> getCloserEvents(Date now);

}