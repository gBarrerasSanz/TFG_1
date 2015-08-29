package guiatv.realtime.rtmpspying;

import guiatv.catalog.datatypes.ListChannels;
import guiatv.conf.initialization.RtmpSpyingLaunchService;
import guiatv.persistence.domain.Channel;
import guiatv.persistence.domain.MLChannel;
import guiatv.persistence.repository.service.ChannelService;
import guiatv.persistence.repository.service.MLChannelService;
import guiatv.realtime.rtmpspying.serializable.ChannelData;
import guiatv.realtime.rtmpspying.serializable.ListChannelsData;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import javax.annotation.PostConstruct;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;



@Service
@Scope("singleton")
public class MutexMonitor {
	
	private static final Logger logger = Logger.getLogger("debugLog");
	
	@Autowired
	RtmpSpyingLaunchService spyLaunchServ;
	
	@Autowired
	MLChannelService mlChServ;
	@Autowired
	ChannelService chServ;
	
	private boolean spiersLaunched;
//	BlockingQueue<ChannelData> chPool;
	private HashMap<String, ChannelData> chPool;
	
//	List<MLChannel> lMlChannelLive = new ArrayList<MLChannel>();
	
	public MutexMonitor() {

	}
	
	@PostConstruct
	public void onPostConstruct() {
		ListChannelsData listChannelsData = initializeListChannelsData();
		ListChannels lCh = new ListChannels();
        if (listChannelsData != null) {
			chPool = new HashMap<String, ChannelData>(listChannelsData.getListChannelData().size());
	        for (ChannelData chData: listChannelsData.getListChannelData()) {
	        	// Si está activo
	        	if (chData.isActive() || chData.isAdminVisible()) { 
	        		Channel ch = new Channel();
	        		ch.setIdChBusiness(chData.getIdChBusiness());
	        		ch.computeHashIdChBusiness();
	        		ch.setNameCh(chData.getNameProg());
	        		lCh.add(ch);
	        		// Añadir hashIdChBusiness al objeto ChannelData
	        		chData.setHashIdChBusiness(ch.getHashIdChBusiness());
	        		// Si es autostart pero no está activo -> Poner activo a falso
	        		if ( ! chData.isActive() ) {
	        			chData.setActive(false);
	        		}
	        		// Añadir objeto ChannelData al hashmap
	        		chPool.put(chData.getHashIdChBusiness(), chData);
	        	}
	        }
        }
        chServ.save(lCh);
        // Por cada chData (entranado o no), crear el MLChannel correspondiente y guardarlo en chData
        for (Object value : chPool.values()) {
        	ChannelData chData = (ChannelData)value;
        	spyLaunchServ.loadMLChannel(chData);
        }
        // Lanzar los spiers
        for (Object value : chPool.values()) {
        	ChannelData chData = (ChannelData)value;
        	if (spyLaunchServ.launchChannelSpying(chData)) {
    			chData.setSpied(true);
    			logger.debug("Channel ["+chData.getIdChBusiness()+"] is now BEING SPIED");
    		}
    		else {
    			logger.debug("Channel ["+chData.getIdChBusiness()+"] CANNOT BE SPIED at this moment");
    		}
        }
	}
	
	private ListChannelsData initializeListChannelsData() {
		JAXBContext jaxbContext;
		try {
			jaxbContext = JAXBContext.newInstance(ListChannelsData.class);
			Resource resource = new ClassPathResource("META-INF/channelData/listChannelData.xml");
	        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
	        ListChannelsData listChannelsData = (ListChannelsData) unmarshaller.unmarshal(resource.getInputStream());
	        return listChannelsData;
		} catch (JAXBException | IOException e1) {
			e1.printStackTrace();
			return null;
		}
	}
	
	/* 
	 * MÉTODO READONLY DEL HASH DE ChannelData
	 */
	public synchronized List<ChannelData> readListChannelData() {
		List<ChannelData> lChData = new ArrayList<ChannelData>();
		for (Object value : chPool.values()) {
			lChData.add((ChannelData)value);
		}
		return lChData;
	}
	
	/*
	 * METODOS QUE MODIFICAN EL ESTADO DE BUSY
	 */
//	public synchronized ChannelData acquireNonBusyChannel() {
//		for (Object value : chPool.values()) {
//			ChannelData chData = (ChannelData)value;
//			if (chData.getUrl() != null && chData.isActive() && ! chData.isBusy()) {
//				chData.setBusy(true);
//				return chData;
//			}
//		}
//		return null;
//	}
	
	public synchronized boolean queryChannelForSpying(ChannelData queryChData) {
		ChannelData chData = chPool.get(queryChData.getHashIdChBusiness());
		if ( ! chData.isSpied() ) { // Si NO esta ya siendo espiado
			// Entonces intentar espiarlo
			if (spyLaunchServ.launchChannelSpying(chData)) {
				chData.setSpied(true);
				return true;
			}
			else {
				return false;
			}
		}
		else {
			return false;
		}
	}
	
	public synchronized boolean queryChannelForSpying(Channel ch) {
		ChannelData chData = chPool.get(ch.getHashIdChBusiness());
		return queryChannelForSpying(chData);
	}
	
	public synchronized void releaseBusyChannel(Channel ch) {
		ChannelData chData = chPool.get(ch.getHashIdChBusiness());
		chData.setSpied(false);
	}
	
	/*
	 * METODOS QUE MODIFICAN EL ESTADO DE ACTIVE 
	 */
	public synchronized void activateChannel(Channel channel) {
		ChannelData chData = chPool.get(channel.getHashIdChBusiness());
		if (chData != null) {
			chData.setActive(true);
		}
	}
	
	public synchronized void deactivateChannel(Channel channel) {
		ChannelData chData = chPool.get(channel.getHashIdChBusiness());
		if (chData != null) {
			chData.setActive(false);
			chData.setSpied(false);
		}
	}
	
	public synchronized boolean checkActiveChannel(Channel channel) {
		ChannelData chData = chPool.get(channel.getHashIdChBusiness());
		if (chData != null && chData.isActive()) {
			return true;
		}
		else {
			return false;
		}
	}
	
	public synchronized boolean checkSpiedChannel(Channel channel) {
		ChannelData chData = chPool.get(channel.getHashIdChBusiness());
		if (chData != null && chData.isSpied()) {
			return true;
		}
		else {
			return false;
		}
	}
	
	public synchronized boolean checkExistentChannel(Channel channel) {
		ChannelData chData = chPool.get(channel.getHashIdChBusiness());
		if (chData != null) {
			return true;
		}
		else {
			return false;
		}
	}
	
	public synchronized MLChannel getMlChannelFromHashIdChBusiness(String hashIdChBusiness) {
		ChannelData chData = chPool.get(hashIdChBusiness);
		return chData.getMlChannel();
	}
	
//	public void loadAndSpyChannels() {
//        if ( ! spiersLaunched) {
//			// Lanzar espias
//	        spyLaunchServ.loadAndSpyChannels();
//        }
//	}
	
//	private synchronized void addChDataToPool(ChannelData chData) {
//		chPool.add(chData);
//	}
	
//	public void addNewChToPool(Channel ch) {
//		ListChannelsData listChannelsData = getListChannelsData();
//        if (listChannelsData != null) {
//        	 for (ChannelData chData: listChannelsData.getListChannelData()) {
//        		 if (chData.getIdChBusiness().equals(ch.getIdChBusiness())) {
//        			 if (chData.isActive()) {
//        				 addChDataToPool(chData);
//        			 }
//        		 }
//        	 }
//        }
//	}
	
//	public synchronized void addMlChannel(MLChannel mlChannel) {
//		lMlChannelLive.add(mlChannel);
//	}
//	
//	public synchronized MLChannel getMlChannel() {
//		return lMlChannelLive;
//	}
}
