package guiatv.realtime.rtmpspying;

import guiatv.realtime.rtmpspying.serializable.ChannelData;
import guiatv.realtime.rtmpspying.serializable.ListChannelsData;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;

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
import com.fasterxml.jackson.xml.XmlMapper;

@Service
@Scope("singleton")
public class MutexMonitor {
	
	private ListChannelsData listChannelsData;
	
	private int idx = 0;
	
	public MutexMonitor() {
		
	}
	
	@PostConstruct
	public void onPostConstruct() {
		JAXBContext jaxbContext;
		try {
			jaxbContext = JAXBContext.newInstance(ListChannelsData.class);
			Resource resource = new ClassPathResource("META-INF/channelData/listChannelData.xml");
	        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
	        listChannelsData = (ListChannelsData) unmarshaller.unmarshal(resource.getInputStream());
		} catch (JAXBException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public synchronized ChannelData getAnAvailableChannel() {
		List<ChannelData> lChannelsData = listChannelsData.getListChannelData();
		ChannelData chData = lChannelsData.get(idx);
		if (idx <= lChannelsData.size()-1) {
			idx++;
			if (chData.isActive()) {
				return chData;
			}
			else {
				return null;
			}
		}
		else {
			return null;
		}
	}
	
	public synchronized String listChannelsDataToString() {
		return listChannelsData.toString();
	}
}
