package guiatv.eventproducer;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;

import guiatv.eventmanager.ImgProcessingGateway;
import guiatv.persistence.domain.Event;

public class EventProducerPoller {
	
	@Autowired
	ImgProcessingGateway evService;
	
	public Message<List<Event>> askForEvents() {
		List<Event> lEvt = evService.getCloserEvents(new Date());
		// TODO: Corregir
		// Crear List<Event> inutil para evitar Nullpointer exception
		if (lEvt == null) {
			lEvt = new ArrayList<Event>(); 
		}
		Message<List<Event>> replyMsg = MessageBuilder.
				withPayload(lEvt).build();
		return replyMsg;
	}
}
