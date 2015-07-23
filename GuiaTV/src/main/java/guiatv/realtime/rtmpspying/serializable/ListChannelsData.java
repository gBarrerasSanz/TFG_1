package guiatv.realtime.rtmpspying.serializable;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement(name="listChannelsData")
public class ListChannelsData {
	
	ArrayList<ChannelData> listChannelData;
	
	public ListChannelsData() {
	}
	
	@XmlElement(name="channelData")
	public ArrayList<ChannelData> getListChannelData() {
		return listChannelData;
	}

	public void setListChannelData(ArrayList<ChannelData> listChannelData) {
		this.listChannelData = listChannelData;
	}
	
	
}
