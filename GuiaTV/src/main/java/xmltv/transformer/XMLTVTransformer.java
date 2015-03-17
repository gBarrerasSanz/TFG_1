package xmltv.transformer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.jxpath.JXPathContext;
import org.springframework.integration.transformer.Transformer;
import org.springframework.messaging.Message;

import xmltv.datatypes.Evento;

public class XMLTVTransformer implements Transformer {

	@Override
	public Message<?> transform(Message<?> message) {
		System.out.println(message.getPayload().toString());
		JXPathContext context = JXPathContext.newContext(message.getPayload());
		Iterator<?> itProgrammes = context.iterate("/tv/programme");
		List<Evento> lEvt = new ArrayList<Evento>();
		while (itProgrammes.hasNext()) {
			System.out.println(itProgrammes.next());
		}
		return null;
	}

}
