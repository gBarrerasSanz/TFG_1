package guiatv.persistence.service;

import guiatv.persistence.domain.Schedule;
import guiatv.persistence.repository.ScheduleRepository;

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
