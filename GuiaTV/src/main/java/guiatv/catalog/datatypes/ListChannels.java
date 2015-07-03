package guiatv.catalog.datatypes;

import guiatv.persistence.domain.Channel;
import guiatv.persistence.domain.Programme;
import guiatv.persistence.domain.Schedule;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ListChannels extends ArrayList<Channel>{
	
	@JsonProperty(value="listChannels")
	public ArrayList<Channel> lCh;
	
	public ListChannels(){
		lCh = new ArrayList<Channel>();
	}
	
//	public ArrayList<Channel> get(){
//		return lCh;
//	}
	
}
