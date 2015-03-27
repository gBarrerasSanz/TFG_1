package xmltv.transformer;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.jxpath.JXPathContext;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.integration.transformer.Transformer;
import org.springframework.messaging.Message;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import common.CommonUtility;
import xmltv.datatypes.Event;

public class XMLTVTransformer implements Transformer {
	
	@Autowired
	CommonUtility utils;
	
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
				List<Event> lEvt = new ArrayList<Event>();
				NamedNodeMap nodeMap = null; Event evt = null; Node n = null;
				int itIdx = 0;
				while (itProg.hasNext()) {
					n = listProgNodes.get(itIdx);
					nodeMap= n.getAttributes();
					evt = new Event();
					evt.setTitle(itProg.next());
					evt.setChannel(nodeMap.getNamedItem("channel").getNodeValue());
					evt.setStart(utils.strToDate(nodeMap.getNamedItem("start").getNodeValue()));
					evt.setEnd(utils.strToDate(nodeMap.getNamedItem("stop").getNodeValue()));
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
}
