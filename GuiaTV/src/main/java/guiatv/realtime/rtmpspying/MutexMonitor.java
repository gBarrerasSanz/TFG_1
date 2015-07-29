package guiatv.realtime.rtmpspying;

import guiatv.persistence.domain.MLChannel;
import guiatv.realtime.rtmpspying.serializable.ChannelData;
import guiatv.realtime.rtmpspying.serializable.ListChannelsData;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
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
	
	
	BlockingQueue<ChannelData> queue;
	
//	List<MLChannel> lMlChannelLive = new ArrayList<MLChannel>();
	
	public MutexMonitor() {

	}
	
	@PostConstruct
	public void onPostConstruct() {
		JAXBContext jaxbContext;
		try {
			jaxbContext = JAXBContext.newInstance(ListChannelsData.class);
			Resource resource = new ClassPathResource("META-INF/MLChannels/channelData/listChannelData.xml");
	        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
	        ListChannelsData listChannelsData = (ListChannelsData) unmarshaller.unmarshal(resource.getInputStream());
	        
	        queue = new ArrayBlockingQueue<ChannelData>(listChannelsData.getListChannelData().size());
	        for (ChannelData chData: listChannelsData.getListChannelData()) {
	        	if (chData.isActive()) {
	        		queue.add(chData);
	        	}
	        }
		} catch (JAXBException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public synchronized ChannelData acquireChannel() {
		return queue.poll();
	}
	
	public synchronized void releaseChannel(ChannelData chData) {
		queue.add(chData);
	}
	
//	public synchronized void addMlChannel(MLChannel mlChannel) {
//		lMlChannelLive.add(mlChannel);
//	}
//	
//	public synchronized MLChannel getMlChannel() {
//		return lMlChannelLive;
//	}
}
