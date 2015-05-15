package guiatv.xmltv.transformer;

import guiatv.common.CommonUtility;
import guiatv.domain.Event;

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

public class XMLTVTransformer implements Transformer {
	
	static Logger log = Logger.getLogger("debugLog");
	
	@Override
	public Message<List<Event>> transform(Message<?> message) {
		Message<List<Event>> result = null;
		if (message.getPayload() instanceof File) {
			File file = null;
			try{
				file = (File) message.getPayload();
				DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
//				utils.printFile(file);
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
					evt.setStart(CommonUtility.strToDate(nodeMap.getNamedItem("start").getNodeValue()));
					evt.setEnd(CommonUtility.strToDate(nodeMap.getNamedItem("stop").getNodeValue()));
					lEvt.add(evt);
					itIdx++;
				}
//				log.debug("listProgNodes.size() = "+String.valueOf(listProgNodes.size()));
//				log.debug("itIdx = "+String.valueOf(itIdx));
				assert(listProgNodes.size() == itIdx);
				result = MessageBuilder.withPayload(lEvt).copyHeaders(message.getHeaders()).build();
			}
			catch(Exception e) {
				e.printStackTrace();
				log.debug("**** File content: ****");
				log.debug(CommonUtility.getFileString(file));
				return null;
			}
		}
		else {
		}
		return result;
	}
}
