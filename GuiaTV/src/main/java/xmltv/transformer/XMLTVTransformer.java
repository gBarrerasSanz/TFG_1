package xmltv.transformer;

import java.io.File;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.jxpath.Container;
import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.xml.DocumentContainer;
import org.apache.log4j.Logger;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.integration.transformer.Transformer;
import org.springframework.messaging.Message;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import xmltv.datatypes.Evento;

public class XMLTVTransformer implements Transformer {
	
	static Logger log = Logger.getLogger(XMLTVTransformer.class.getName());
	
	@Override
	public Message<?> transform(Message<?> message) {
		Message<?> result = null;
		if (message.getPayload() instanceof File) {
			try{
				File file = (File) message.getPayload();
				DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
				Document doc = dBuilder.parse(file);
				JXPathContext jxpathCtx = JXPathContext.newContext(doc);
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
				log.debug("listProgNodes.size() = "+String.valueOf(listProgNodes.size()));
				log.debug("itIdx = "+String.valueOf(itIdx));
				assert(listProgNodes.size() == itIdx);
				result = MessageBuilder.withPayload(lEvt).copyHeaders(message.getHeaders()).build();
			}
			catch(Exception e) {
				e.printStackTrace();
			}
		}
		else {
		}
		return result;
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
