package guiatv.persistence.repository;

import java.util.List;

import guiatv.persistence.domain.Channel;
import guiatv.persistence.domain.Schedule;

public interface ScheduleRepositoryCustom {

	void merge(List<Schedule> lSched);
}
