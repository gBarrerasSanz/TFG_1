package guiatv.catalog.datatypes;


import guiatv.persistence.domain.Channel;
import guiatv.persistence.domain.Programme;
import guiatv.persistence.domain.Schedule;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class ListSchedules_Old extends ArrayList<Schedule>{
	
	@JsonProperty(value="listSchedules")
	private ArrayList<Schedule> lSched;
	
	public ListSchedules_Old(){
		super();
		lSched = new ArrayList<Schedule>();
	}
	
	public ArrayList<Schedule> getlSched() {
		return lSched;
	}

	public void setlSched(ArrayList<Schedule> lSched) {
		this.lSched = lSched;
	}
	
	@Override
	public boolean add(Schedule sched) {
		return lSched.add(sched);
	}
	
	@Override
	public Schedule get(int index) {
		return lSched.get(index);
	}
	
}
