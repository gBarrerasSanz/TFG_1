package guiatv.persistence.repository;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
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
	
	Schedule findOneByIdSched(Long idSched);
	
	List<Schedule> findByChannelOrderByStartAsc(Channel ch);
	
	List<Schedule> findByChannel(Channel ch);
	
	@Query("SELECT s FROM schedule s WHERE s.channel = ?1 ORDER BY s.start ASC")
	List<Schedule> findByChannelEagerly(Channel ch);
	
	@Query("SELECT s FROM schedule s WHERE s.channel = ?1 AND s.programme = ?2 AND s.start >= ?3 ORDER BY s.start ASC")
	List<Schedule> findByChannelAndProgrammeAndStartGreaterOrEqualThan(Channel ch, 
			Programme prog, Timestamp start);
	
//	@Query("SELECT s.programme FROM schedule s "
//			+ "WHERE s.channel = ?1 AND s.start <= ?2 AND s.end >= ?2")
//	Programme findOneByChannelAndInstant(Channel ch, Timestamp instant);
	
//	Schedule findOneByChannelAndStartBeforeAndEndAfter(
//			Channel ch, Timestamp instant1, Timestamp instant2);
	
	@Query("SELECT s.programme FROM schedule s "
	+ "WHERE s.channel = ?1 AND s.start <= ?2 AND s.end >= ?2")
	List<Schedule> findByChannelAndInstantBetweenStartAndEnd(
			Channel ch, Timestamp instant);
	
	// DEBUG
	Schedule findOneByStartBeforeAndEndAfter(Timestamp instant1, Timestamp instant2);
	
	List<Schedule> findByProgramme(Programme prog);
	
	List<Schedule> findByChannelAndProgrammeAndEndLessThanOrderByStartAsc(Channel ch, Programme prog, Timestamp end);
	
	
//	@Query("Select s from schedule s where s.start >= ?1 and s.end <= ?2")
//	List<Schedule> findCloserSchedules(Timestamp start, Timestamp end);
	
	// Devolver los schedules con start en [start, end] o los schedules con (start < start2 AND end > start2) (schedules en curso)
	
	List<Schedule> findByStartBetweenAndPublishedFalseOrStartBeforeAndEndAfterAndPublishedFalseOrderByStartAsc(Timestamp start, Timestamp end, Timestamp start2, Timestamp start3);
	
//	@Query("SELECT s from schedule s "
//			+ "WHERE s.published >= false AND "
//			+ "s.start <= ?2")
//	List<Schedule> findByPublishedFalseAndInstantOnTime(Timestamp start);
	
	List<Schedule> findByChannelAndProgrammeOrderByStartAsc(Channel ch, Programme prog);
	
//	CURRENT_TIMESTAMP
//	@Query("Select s from Schedule s where s.channel=?1 and s.programme=?2 and s.start >= CURRENT_TIMESTAMP")
//	List<Schedule> findByChannelAndProgrammeFromNow(Channel ch, Programme prog);
	
	Schedule findByChannelAndProgrammeAndStartAndEnd(Channel ch, Programme prog, Timestamp start, Timestamp end);
	
	@Modifying
	@Query("UPDATE schedule s SET s.published = true "
			+ "WHERE s.idSched = ?1")
	int setTruePublishedWhereIdSched(Long idSched);
	
//	void delete(Schedule sched);
}
