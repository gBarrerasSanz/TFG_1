package guiatv.persistence.repository;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import guiatv.persistence.domain.Channel;
import guiatv.persistence.domain.Programme;
import guiatv.persistence.domain.Schedule;

public interface ScheduleRepository extends JpaRepository<Schedule, Long>,
		JpaSpecificationExecutor<Schedule> {
	
	List<Schedule> findAll();
	
	Schedule findByIdSched(Long idSched);

	List<Schedule> findByChannel(Channel ch);
	
	@Query("SELECT s FROM schedule s WHERE s.channel = ?1 ORDER BY s.start ASC")
	List<Schedule> findByChannelEagerly(Channel ch);
	
	@Query("SELECT s FROM schedule s WHERE s.channel = ?1 AND s.programme = ?2 AND s.start >= ?3 ORDER BY s.start ASC")
	List<Schedule> findByChannelAndProgrammeAndStartGreaterOrEqualThan(Channel ch, 
			Programme prog, Timestamp start);
	
	List<Schedule> findByProgramme(Programme prog);
	
	List<Schedule> findByChannelAndProgrammeAndEndLessThanOrderByStartAsc(Channel ch, Programme prog, Timestamp end);
	
//	@Query("Select s from schedule s where s.start >= ?1 and s.end <= ?2")
//	List<Schedule> findCloserSchedules(Timestamp start, Timestamp end);
	
	// Devolver los schedules con start en [start, end] o los schedules con (start < start2 AND end > start2) (schedules en curso)
	List<Schedule> findByStartBetweenOrStartBeforeAndEndAfterOrderByStartAsc(Timestamp start, Timestamp end, Timestamp start2, Timestamp start3);
	
	List<Schedule> findByChannelAndProgrammeOrderByStartAsc(Channel ch, Programme prog);
	
//	CURRENT_TIMESTAMP
//	@Query("Select s from Schedule s where s.channel=?1 and s.programme=?2 and s.start >= CURRENT_TIMESTAMP")
//	List<Schedule> findByChannelAndProgrammeFromNow(Channel ch, Programme prog);
	
	Schedule findByChannelAndProgrammeAndStartAndEnd(Channel ch, Programme prog, Timestamp start, Timestamp end);
	
//	void delete(Schedule sched);
}
