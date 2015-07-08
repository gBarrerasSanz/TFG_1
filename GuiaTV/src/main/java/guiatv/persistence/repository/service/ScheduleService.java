package guiatv.persistence.repository.service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import guiatv.persistence.domain.Channel;
import guiatv.persistence.domain.Programme;
import guiatv.persistence.domain.RtmpSource;
import guiatv.persistence.domain.Schedule;
import guiatv.persistence.repository.ChannelRepository;
import guiatv.persistence.repository.ProgrammeRepository;
import guiatv.persistence.repository.RtmpSourceRepository;
import guiatv.persistence.repository.ScheduleRepository;
import guiatv.persistence.repository.ScheduleRepositoryImpl;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ScheduleService {
	
	@Autowired
	ScheduleRepository schedRep;
	@Autowired
	ChannelRepository chRep;
	@Autowired
	ProgrammeRepository progRep;
	
	@Autowired
	ScheduleRepositoryImpl schedRepImpl;
	
	@Transactional(readOnly = true)
	public List<Schedule> findAll(boolean refs) {
		List<Schedule> lSched = schedRep.findAll();
		if (refs){
			for (Schedule sched: lSched) {
				Hibernate.initialize(sched.getChannel());
				Hibernate.initialize(sched.getProgramme());
			}
		}
		return lSched;
	}
	
	@Transactional(readOnly = true)
	public List<Schedule> findByChannel(Channel ch, boolean refs) {
		List<Schedule> lSched = schedRep.findByChannel(ch);
		if (refs){
			for (Schedule sched: lSched) {
				Hibernate.initialize(sched.getChannel());
				Hibernate.initialize(sched.getProgramme());
			}
		}
		return lSched;
	}
	
	@Transactional(readOnly = true)
	public List<Schedule> findByChannelAndProgramme(Channel ch, Programme prog) {
		return schedRep.findByChannelAndProgramme(ch, prog);
	}
	
	@Transactional(readOnly = true)
	public List<Schedule> findByChannelAndProgrammeAndStartGreaterOrEqualThan(Channel ch, Programme prog, Timestamp start, boolean refs) {
		List<Schedule> lSched = schedRep.findByChannelAndProgrammeAndStartGreaterOrEqualThan(ch, prog, start);
		if (refs){
			for (Schedule sched: lSched) {
				Hibernate.initialize(sched.getChannel());
				Hibernate.initialize(sched.getProgramme());
			}
		}
		return lSched;
	}
	
	@Transactional(readOnly = true)
	public List<Schedule> findByChannelAndProgrammeAndEndLessThan(Channel ch, Programme prog, Timestamp end, boolean refs) {
		List<Schedule> lSched = schedRep.findByChannelAndProgrammeAndEndLessThan(ch, prog, end);
		if (refs){
			for (Schedule sched: lSched) {
				Hibernate.initialize(sched.getChannel());
				Hibernate.initialize(sched.getProgramme());
			}
		}
		return lSched;
	}
	
	@Transactional(readOnly = true)
	public List<Schedule> findBySecondsFromStart(int secsFromStart, boolean refs) {
		Timestamp now = new Timestamp(new Date().getTime());
		Timestamp afterStart = new Timestamp(now.getTime() + (long)(1000 * secsFromStart));
		List<Schedule> lSched = schedRep.findByStartBetween(now, afterStart);
		if (refs){
			for (Schedule sched: lSched) {
				Hibernate.initialize(sched.getChannel());
				Hibernate.initialize(sched.getProgramme());
			}
		}
		return lSched;
	}
	
	@Transactional(propagation = Propagation.REQUIRES_NEW)
    public Schedule findByChannelAndProgrammeAndStartAndEnd(Channel ch, 
    		Programme prog, Timestamp start, Timestamp end)
 	{
		return schedRep.findByChannelAndProgrammeAndStartAndEnd(ch, 
				prog, start, end);
    }
	
	@Transactional(propagation = Propagation.REQUIRES_NEW)
    public void insertSchedules(List<Schedule> lSched) {
		for (Schedule sched: lSched) {
			try {
				Channel ch = chRep.findByIdChBusiness(sched.getChannel().getIdChBusiness());
				Programme prog = progRep.findByNameProg(sched.getProgramme().getNameProg());
				if (ch != null) { // Ya existe canal
					sched.setChannel(ch);
				}
				else { // No existe canal
					chRep.saveAndFlush(sched.getChannel());
				}
				if (prog != null) { // Ya existe programa
					sched.setProgramme(prog);
				}
				else { // No existe programa
					progRep.saveAndFlush(sched.getProgramme());
				}
				Schedule schedIn = schedRep.findByChannelAndProgrammeAndStartAndEnd(
						sched.getChannel(), sched.getProgramme(), sched.getStart(), sched.getEnd());
				if (schedIn == null) {
					schedRep.saveAndFlush(sched);
				}
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
    }
	
	@Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveSchedules(List<Schedule> lSched){
		
		//schedRep.save(lSched);
    }
	
	@Transactional(propagation = Propagation.REQUIRES_NEW)
    public void insertSchedule(Schedule sched){
    	schedRep.save(sched);
    }
	
	@Transactional(propagation = Propagation.REQUIRES_NEW)
    public void deleteSchedules(List<Schedule> lSched) {
    	schedRep.delete(lSched);
    }
}
