package xmltv.transformer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.jxpath.JXPathContext;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.integration.transformer.Transformer;
import org.springframework.messaging.Message;

import xmltv.datatypes.Evento;

public class XMLTVTransformer implements Transformer {

	@Override
	public Message<?> transform(Message<?> message) {
		if (message.getPayload() instanceof JXPathContext) {
			JXPathContext jxpathCtx = (JXPathContext) message.getPayload();
			// TODO: Sé obtener los valores de los nodos con el iterator, pero no se
			// cómo obtener sus atributos utilizando solo JXPath. Supongo que si obtengo
			// el iterador de nodos dom, podre obtener los atributos, pero es un poco follón
			// mezclar las dos cosas
//			List<?> listProg = jxpathCtx.selectNodes("/tv/programme");
			Iterator<?> itProgrammes = jxpathCtx.iterate("/tv/programme");
//			Iterator<JXPathContext> itProgrammes = jxpathCtx.iteratePointers("/tv/programme");
			List<Evento> lEvt = new ArrayList<Evento>();
			while (itProgrammes.hasNext()) {
				System.out.println(itProgrammes.next());
			}
			Message<?> result = MessageBuilder.
					withPayload(lEvt).build();
			return result;
		}
		else {
			return null;
		}
	}

}
