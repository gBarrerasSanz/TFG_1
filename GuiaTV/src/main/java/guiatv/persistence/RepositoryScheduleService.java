package guiatv.persistence;

import guiatv.domain.Schedule;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

public class RepositoryScheduleService implements ScheduleService {
	
	@Autowired
	ScheduleRepository schedRep;
	
	@Override
	public List<Schedule> getClosestSchedules() {
		return schedRep.findAll();
	}
	
}
