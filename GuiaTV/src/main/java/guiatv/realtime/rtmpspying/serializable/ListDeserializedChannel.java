package guiatv.realtime.rtmpspying.serializable;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement(name="listDeserializedChannel")
public class ListDeserializedChannel {
	
	ArrayList<DeserializedChannel> listDeserializedChannel;
	
	public ListDeserializedChannel() {
	}
	
	@XmlElement(name="deserializedChannel")
	public ArrayList<DeserializedChannel> getListDeserializedChannel() {
		return listDeserializedChannel;
	}

	public void setListDeserializedChannel(
			ArrayList<DeserializedChannel> listDeserializedChannel) {
		this.listDeserializedChannel = listDeserializedChannel;
	}
	
	
	
}
