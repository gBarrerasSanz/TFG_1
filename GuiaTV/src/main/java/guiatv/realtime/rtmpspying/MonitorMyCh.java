package guiatv.realtime.rtmpspying;

import guiatv.catalog.datatypes.ListChannels;
import guiatv.common.CommonUtility;
import guiatv.persistence.domain.BlobFrame;
import guiatv.persistence.domain.Channel;
import guiatv.persistence.domain.MyCh;
import guiatv.persistence.domain.MyChState;
import guiatv.persistence.domain.StreamSource;
import guiatv.persistence.domain.TrainedModel;
import guiatv.persistence.domain.helper.ArffHelper;
import guiatv.persistence.repository.service.ChannelService;
import guiatv.realtime.rtmpspying.serializable.DeserializedChannel;
import guiatv.realtime.rtmpspying.serializable.ListDeserializedChannel;

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

import weka.core.Instance;

import com.fasterxml.jackson.databind.ObjectMapper;



@Service
@Scope("singleton")
public class MonitorMyCh {
	
	private static final Logger logger = Logger.getLogger("debugLog");
	
//	@Autowired
//	RtmpSpyingLaunchService spyLaunchServ;
//	@Autowired
//	StreamSourceService streamSrcServ;
	@Autowired
	ChannelService chServ;
//	@Autowired
//	TrainedModelService trainedModelServ;
	@Autowired
	RtmpSpyingService rtmpSpyingServ;
//	@Autowired
//	MyChService myChServ;
//	
	private HashMap<String, MyCh> myChMap;
	
//	List<MLChannel> lMlChannelLive = new ArrayList<MLChannel>();
	
	public MonitorMyCh() {
	}
	
	@PostConstruct
	public void onPostConstruct() {
		ListDeserializedChannel listDeserializedChannel = initializeListDeserializedChannel();
//		ListChannels lCh = new ListChannels();
        if (listDeserializedChannel != null) {
        	List<DeserializedChannel> listDestChannel = listDeserializedChannel.getListDeserializedChannel();
        	myChMap = new HashMap<String, MyCh>();
	        for (DeserializedChannel desChannel: listDestChannel) {
	        	if (desChannel.isEnabled()) { // Si está enabled 
	        		Channel ch = new Channel(desChannel.getIdChBusiness(), desChannel.getNameChGiven());
	        		StreamSource streamSrc = new StreamSource(desChannel.getUrl(),
	        				desChannel.getCols(), desChannel.getRows(),
	        				desChannel.getTopLeft(), desChannel.getBotRight());
	        		TrainedModel trainedModel = new TrainedModel(desChannel.getIdChBusiness(),
	        				desChannel.getBatchGoodSamplesUri(), desChannel.getBatchBadSamplesUri());
	        		MyChState myChState = new MyChState(desChannel.isActive(), 
	        				desChannel.getSamplesToFalse(), desChannel.getSamplesToTrue());
	        		MyCh myCh = new MyCh(ch, streamSrc, trainedModel, myChState);
	        		// Asignar a las instancias hijas un puntero a su padre
	        		streamSrc.setParent(myCh);
	        		trainedModel.setParent(myCh);
	        		myChState.setParent(myCh);
	        		// Lanzar los espias
	        		if (desChannel.isDoSpying()) {
	        			boolean req = myChState.requestSpying();
	        			if (req) {
	        				rtmpSpyingServ.doSpying(myCh);
	        			}
	        		}
	        		// Añadirlo al mapa de MyCh
	        		myChMap.put(ch.getHashIdChBusiness(), myCh);
	        		// Guardar las instancias hijas en la BD
	        		Channel existentCh = chServ.saveOrGetExistent(ch);
	        		if (existentCh != null) {
	        			myCh.setChannel(existentCh);
	        		}
//	        		streamSrcServ.save(streamSrc);
//	        		trainedModelServ.save(trainedModel);
	        		// Y a continuación guardar la instancia padre
//	        		myChServ.save(myCh);
	        	}
	        }
        }
        else { // ERROR GRAVE: NO SE HA PODIDO DESERIALIZAR EL XML
        	logger.debug("SEVERE ERROR: Could not deserialize ListDeserializedChannel from XML file");
        }
	}
	
	private ListDeserializedChannel initializeListDeserializedChannel() {
		JAXBContext jaxbContext;
		try {
			jaxbContext = JAXBContext.newInstance(ListDeserializedChannel.class);
			Resource resource = new ClassPathResource("META-INF/channelData/listChannelData.xml");
	        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
	        ListDeserializedChannel listDeserializedChannel = 
	        		(ListDeserializedChannel) unmarshaller.unmarshal(resource.getInputStream());
	        return listDeserializedChannel;
		} catch (JAXBException | IOException e1) {
			e1.printStackTrace();
			return null;
		}
	}
	
	public List<MyCh> getListMyCh() {
		List<MyCh> lMyCh = new ArrayList<MyCh>();
		for (Object value : myChMap.values()) {
			lMyCh.add((MyCh)value);
        }
		return lMyCh;
	}
	
	public MyCh getByChannel(Channel ch) {
		return myChMap.get(ch.getHashIdChBusiness());
	}
	
	public MyCh getByHashIdChBusiness(String hashIdChBusiness) {
		return myChMap.get(hashIdChBusiness);
	}
	
//	public boolean checkIfExistsByChannel(Channel ch) {
//		if (myChMap.get(ch.getHashIdChBusiness())!= null) {
//			return true;
//		}
//		else {
//			return false;
//		}
//	}
	
//	public boolean checkIfActiveByChannel(Channel ch) {
//		MyCh myCh = myChMap.get(ch.getHashIdChBusiness());
//		if (myCh != null && myCh.getMyChState().isActive()) {
//			return true;
//		}
//		else {
//			return false;
//		}
//	}
}
