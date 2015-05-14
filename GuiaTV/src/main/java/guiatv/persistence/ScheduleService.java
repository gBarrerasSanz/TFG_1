package guiatv.persistence;

import guiatv.domain.Schedule;

import java.util.List;

public interface ScheduleService {
	
	List<Schedule> getClosestSchedules();
}
