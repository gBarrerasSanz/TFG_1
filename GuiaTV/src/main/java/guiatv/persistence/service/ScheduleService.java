package guiatv.persistence.service;

import guiatv.domain.Schedule;

import java.util.List;

public interface ScheduleService {
	
	List<Schedule> getClosestSchedules();
}
