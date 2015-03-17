package xmltv.transformer;

import java.util.Iterator;

import org.apache.commons.jxpath.JXPathContext;
import org.springframework.integration.transformer.Transformer;
import org.springframework.messaging.Message;

public class XMLTVTransformer implements Transformer {

	@Override
	public Message<?> transform(Message<?> message) {
		JXPathContext context = JXPathContext.newContext(message.getPayload());
		Iterator<?> itProgrammes = context.iterate("/tv/programme");
		while (itProgrammes.hasNext()) {
			System.out.println(itProgrammes.next());
		}
		return null;
	}

}
