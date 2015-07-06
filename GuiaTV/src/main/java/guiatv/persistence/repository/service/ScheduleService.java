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
    public void insertSchedules(List<Schedule> lSched) {
		for (Schedule sched: lSched) {
			try {
				Channel ch = chRep.findByIdChBusiness(sched.getChannel().getIdChBusiness());
				Programme prog = progRep.findByNameProg(sched.getProgramme().getNameProg());
				if (ch != null) {
					sched.setChannel(ch);
				}
				else {
					ch = sched.getChannel();
					chRep.saveAndFlush(ch);
				}
				if (prog != null) {
					sched.setProgramme(prog);
				}
				else {
					prog = sched.getProgramme();
					progRep.saveAndFlush(prog);
				}
				Schedule schedIn = schedRep.findByChannelAndProgrammeAndStartAndEnd(
						sched.getChannel(), sched.getProgramme(), sched.getStart(), sched.getEnd());
				if (schedIn == null) {
					ch.addSchedule(sched);
					prog.addSchedule(sched);
					chRep.saveAndFlush(ch);
					progRep.saveAndFlush(prog);
					schedRep.saveAndFlush(sched);
					System.out.println("Done");
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
