package guiatv.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import guiatv.domain.Channel;
import guiatv.domain.Programme;
import guiatv.domain.Schedule;

public interface ScheduleRepository extends JpaRepository<Schedule, Long>,
		JpaSpecificationExecutor<Schedule> {

	Schedule findByIdSched(Long idSched);

	List<Schedule> findByIdCh(Long idCh);
	
	List<Programme> findByIdProg(Integer idProg);
	
}
