package guiatv.realtime.rtmpspying;

import guiatv.catalog.datatypes.ListChannels;
import guiatv.conf.initialization.RtmpSpyingLaunchService;
import guiatv.persistence.domain.Channel;
import guiatv.persistence.domain.MLChannel;
import guiatv.persistence.repository.service.ChannelService;
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
	
	@Autowired
	RtmpSpyingLaunchService spyLaunchServ;
	
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
	        	if (chData.isActive() ) { 
	        		chPool.put(chData.getIdChBusiness(), chData);
	        		Channel ch = new Channel();
	        		ch.setIdChBusiness(chData.getIdChBusiness());
	        		ch.computeHashIdChBusiness();
	        		ch.setNameCh(chData.getNameProg());
	        		lCh.add(ch);
	        		// Añadir hashIdChBusiness al objeto ChannelData
	        		chData.setHashIdChBusiness(ch.getHashIdChBusiness());
	        	}
	        }
        }
        chServ.save(lCh);
        spyLaunchServ.loadAndSpyChannels();
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
	public synchronized ChannelData acquireNonBusyChannel() {
		for (Object value : chPool.values()) {
			ChannelData chData = (ChannelData)value;
			if (chData.getUrl() != null && chData.isActive() && ! chData.isBusy()) {
				chData.setBusy(true);
				return chData;
			}
		}
		return null;
	}
	
	public synchronized void releaseBusyChannel(ChannelData chData) {
		chData.setBusy(false);
	}
	
	/*
	 * METODOS QUE MODIFICAN EL ESTADO DE ACTIVE 
	 */
	public synchronized void activateChannel(Channel channel) {
		ChannelData chData = chPool.get(channel.getIdChBusiness());
		if (chData != null) {
			chData.setActive(true);
		}
	}
	
	public synchronized void deactivateChannel(Channel channel) {
		ChannelData chData = chPool.get(channel.getIdChBusiness());
		if (chData != null) {
			chData.setActive(false);
		}
	}
	
	public synchronized boolean checkActiveChannel(Channel channel) {
		ChannelData chData = chPool.get(channel.getIdChBusiness());
		if (chData != null && chData.isActive()) {
			return true;
		}
		else {
			return false;
		}
	}
	
	public synchronized boolean checkExistentChannel(Channel channel) {
		ChannelData chData = chPool.get(channel.getIdChBusiness());
		if (chData != null) {
			return true;
		}
		else {
			return false;
		}
	}
	
	public void loadAndSpyChannels() {
        if ( ! spiersLaunched) {
			// Lanzar espias
	        spyLaunchServ.loadAndSpyChannels();
        }
	}
	
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
