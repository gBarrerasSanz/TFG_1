package guiatv.persistence.repository;

import java.util.List;

import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import guiatv.persistence.domain.Channel;
import guiatv.persistence.domain.Programme;
import guiatv.persistence.domain.Schedule;

public interface ScheduleRepository extends JpaRepository<Schedule, Long>,
		JpaSpecificationExecutor<Schedule> {
	
	Schedule findByIdSched(Long idSched);

	List<Schedule> findByChannel(Channel ch);
	
	List<Programme> findByProgramme(Programme prog);
	
}
