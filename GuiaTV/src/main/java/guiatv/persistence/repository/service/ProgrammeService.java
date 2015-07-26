package guiatv.persistence.repository.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import guiatv.catalog.datatypes.ListProgrammes;
import guiatv.common.CommonUtility;
import guiatv.persistence.domain.Channel;
import guiatv.persistence.domain.Programme;
import guiatv.persistence.domain.Schedule;
import guiatv.persistence.repository.ChannelRepository;
import guiatv.persistence.repository.ProgrammeRepository;
import guiatv.persistence.repository.ScheduleRepository;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProgrammeService {
	
	@Autowired
	ProgrammeRepository progRep;
	
	@Autowired
	ScheduleRepository schedRep;
	
	@Transactional(readOnly = true)
	public ListProgrammes findAll() {
		return progRep.findAll();
	}
	
	@Transactional(readOnly = true)
	public Programme findByNameProg(String nameProg, boolean refs) {
		Programme prog = progRep.findByNameProg(nameProg);
		if (refs) {
			Hibernate.initialize(prog.getListSchedules());
		}
		return prog;
	}
	
	@Transactional(readOnly = true)
	public Programme findByHashNameProg(String hashNameProg, boolean refs) {
		Programme prog = progRep.findByHashNameProg(hashNameProg);
		if (prog != null && refs) {
			Hibernate.initialize(prog.getListSchedules());
		}
		return prog;
	}
	
	@Transactional(readOnly = true)
	public Programme findOneByChannelAndInstant(Channel ch, Timestamp instant) {
//		Programme prog = schedRep.findOneByChannelAndInstant(ch, instant);
		Schedule sched2 = schedRep.findOneByStartBeforeAndEndAfter(instant, instant);
		List<Schedule> lSched2 = schedRep.findByChannelOrderByStartAsc(ch);
		List<Schedule> lSched3 = new ArrayList<Schedule>();
		for (Schedule sc: lSched2) {
			if (CommonUtility.isScheduleBeingEmitedNow(sc)) {
				lSched3.add(sc);
			}
		}
		List<Schedule> lSched = schedRep.findByChannelAndInstantBetweenStartAndEnd(ch, instant);
		return lSched.get(0).getProgramme();
	}
	
	
	@Transactional(propagation = Propagation.REQUIRES_NEW)
    public void deleteProgramme(Programme prog) {
    	progRep.delete(prog);
    }
	
	@Transactional(propagation = Propagation.REQUIRES_NEW)
    public void save(List<Programme> lProg) {
    	progRep.save(lProg);
    }
	
	@Transactional(propagation = Propagation.REQUIRES_NEW)
    public void delete(List<Programme> lProg) {
    	progRep.delete(lProg);
    }
	
}
