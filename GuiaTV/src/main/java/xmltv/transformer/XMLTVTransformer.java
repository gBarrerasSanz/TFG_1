package xmltv.transformer;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import org.apache.commons.jxpath.JXPathContext;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.integration.transformer.Transformer;
import org.springframework.messaging.Message;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import xmltv.datatypes.Evento;

public class XMLTVTransformer implements Transformer {

	@Override
	public Message<?> transform(Message<?> message) {
		if (message.getPayload() instanceof JXPathContext) {
			try{
				JXPathContext jxpathCtx = (JXPathContext) message.getPayload();
				@SuppressWarnings("unchecked")
				List<Node> listProgNodes = jxpathCtx.selectNodes("/tv/programme");
				@SuppressWarnings("unchecked")
				Iterator<String> itProg = jxpathCtx.iterate("/tv/programme");
				List<Evento> lEvt = new ArrayList<Evento>();
				NamedNodeMap nodeMap = null; Evento evt = null; Node n = null;
				int itIdx = 0;
				while (itProg.hasNext()) {
					n = listProgNodes.get(itIdx);
					nodeMap= n.getAttributes();
					evt = new Evento();
					evt.setTitle(itProg.next());
					evt.setChannel(nodeMap.getNamedItem("channel").getNodeValue());
					evt.setStart(strToDate(nodeMap.getNamedItem("start").getNodeValue()));
					evt.setEnd(strToDate(nodeMap.getNamedItem("stop").getNodeValue()));
					lEvt.add(evt);
					itIdx++;
				}
				assert(listProgNodes.size() - 1 == itIdx);
				Message<?> result = MessageBuilder.
						withPayload(lEvt).build();
				return result;
			}
			catch(Exception e) {
				e.printStackTrace();
				return null;
			}
		}
		else {
			return null;
		}
	}

	private static Date strToDate(String str) {
		final Locale SPAIN_LOCALE = new Locale("es","ES");
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddhhmmss Z", SPAIN_LOCALE);
		try {
			return format.parse(str);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}
}
