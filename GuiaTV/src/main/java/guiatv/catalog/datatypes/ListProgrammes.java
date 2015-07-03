package guiatv.catalog.datatypes;


import guiatv.persistence.domain.Channel;
import guiatv.persistence.domain.Programme;
import guiatv.persistence.domain.Schedule;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public class ListProgrammes extends ArrayList<Programme>{
	
	@JsonProperty(value="listProgrammes")
	public ArrayList<Programme> lProg;
	
	
	public ListProgrammes(){
		lProg = new ArrayList<Programme>();
	}
	
//	public ArrayList<Programme> get(){
//		return lProg;
//	}
	
}
