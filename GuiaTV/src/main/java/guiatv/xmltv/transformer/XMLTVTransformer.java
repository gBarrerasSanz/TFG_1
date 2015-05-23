package guiatv.xmltv.transformer;

import guiatv.common.CommonUtility;
import guiatv.domain.Channel;
import guiatv.domain.Event;
import guiatv.domain.Programme;
import guiatv.domain.Schedule;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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
	public Message<List<Schedule>> transform(Message<?> message) {
		Message<List<Schedule>> listSchedulesResult = null;
		if (message.getPayload() instanceof File) {
			File file = null;
			try{
				file = (File) message.getPayload();
				DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
//				utils.printFile(file);
				Document doc = dBuilder.parse(file);
				JXPathContext jxpathCtx = JXPathContext.newContext(doc);
				// Iterar sobre la lista de channels
				HashMap<String,Channel> mapCh = new HashMap<String,Channel>();
				@SuppressWarnings("unchecked")
				List<Node> listChNodes = jxpathCtx.selectNodes("/tv/channel");
				@SuppressWarnings("unchecked")
				Iterator<String> itCh = jxpathCtx.iterate("/tv/channel");
				NamedNodeMap chNodeMap = null; Channel ch = null; Node chNode = null;
				int chItIdx = 0;
				while (itCh.hasNext()) {
					chNode = listChNodes.get(chItIdx);
					chNodeMap = chNode.getAttributes();
					ch = new Channel();
					ch.setNomIdCh(chNodeMap.getNamedItem("id").getNodeValue()); // Coger id
					ch.setNomCh(itCh.next()); // Coger nombre
					mapCh.put(ch.getNomIdCh(), ch); // Meter channel en el mapa hash
					chItIdx++;
				}
				// Iterar sobre la lista de programmes
				HashMap<String,Programme> mapProg = new HashMap<String,Programme>();
				@SuppressWarnings("unchecked")
				List<Node> listProgNodes = jxpathCtx.selectNodes("/tv/programme");
				@SuppressWarnings("unchecked")
				Iterator<String> itProg = jxpathCtx.iterate("/tv/programme");
				NamedNodeMap progNodeMap = null; Programme prog = null; Node progNode = null;
				int progItIdx = 0;
				while (itProg.hasNext()) {
					progNode = listProgNodes.get(progItIdx);
					progNodeMap = progNode.getAttributes();
					prog = new Programme();
					prog.setNomProg(itProg.next()); // Coger el nombre del programa
					mapProg.put(prog.getNomProg(), prog); // Meter programme en el mapa hash
					progItIdx++;
				}
				
				// Iterar sobre la lista de schedules
				@SuppressWarnings("unchecked")
				List<Node> listSchedNodes = jxpathCtx.selectNodes("/tv/programme");
				@SuppressWarnings("unchecked")
				Iterator<String> itSched = jxpathCtx.iterate("/tv/programme");
				List<Schedule> lSched = new ArrayList<Schedule>();
				NamedNodeMap schedNodeMap = null; Schedule sched = null; Node schedNode = null;
				int schedItIdx = 0;
				while (itSched.hasNext()) {
					schedNode = listSchedNodes.get(schedItIdx);
					schedNodeMap= schedNode.getAttributes();
					sched = new Schedule();
					String nombreProg = itSched.next();
					String nomIdChannel = schedNodeMap.getNamedItem("channel").getNodeValue();
					sched.setChannel(mapCh.get(nomIdChannel));
					sched.setProgramme(mapProg.get(nombreProg));
					sched.setStart(CommonUtility.strToDate(schedNodeMap.getNamedItem("start").getNodeValue()));
					sched.setEnd(CommonUtility.strToDate(schedNodeMap.getNamedItem("stop").getNodeValue()));
					lSched.add(sched);
					schedItIdx++;
				}
//				log.debug("listProgNodes.size() = "+String.valueOf(listProgNodes.size()));
//				log.debug("itIdx = "+String.valueOf(itIdx));
//				assert(listProgNodes.size() == schedItIdx);
				listSchedulesResult = MessageBuilder.withPayload(lSched).copyHeaders(message.getHeaders()).build();
			}
			catch(Exception e) {
				e.printStackTrace();
				log.debug("**** File content: ****");
				log.debug(CommonUtility.getFileString(file));
				return null;
			}
		}
		else {
			log.debug("Not an instance of File");
		}
		return listSchedulesResult;
	}
}
