package guiatv.persistence.repository.service;

import java.util.Date;
import java.util.List;

import guiatv.common.CommonUtility;
import guiatv.persistence.domain.Channel;
import guiatv.persistence.domain.Programme;
import guiatv.persistence.domain.Schedule;
import guiatv.persistence.repository.ChannelRepository;
import guiatv.persistence.repository.ProgrammeRepository;
import guiatv.persistence.repository.ScheduleRepository;
import guiatv.persistence.repository.ScheduleRepositoryImpl;

import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ScheduleService {
	
	private static Logger logger = Logger.getLogger("debugLog");
	
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
	public Schedule findOneByIdSched(Long idSched, boolean refs) {
		Schedule sched = schedRep.findOneByIdSched(idSched);
		if (refs) {
			Hibernate.initialize(sched.getChannel());
			Hibernate.initialize(sched.getProgramme());
		}
		return sched;
	}
	
	
	@Transactional(readOnly = true)
	public List<Schedule> findByChannel(Channel ch, boolean refs) {
		List<Schedule> lSched = schedRep.findByChannel(ch);
		if (refs){
			Hibernate.initialize(lSched);
			for (Schedule sched: lSched) {
				// Lo siguiente lo hago porque Hibernate.initialize(sched.getProgramme()) NO hace
				// lo que debería hacer
				Programme prog = new Programme();
				prog.setIdProgPersistence(sched.getProgramme().getIdProgPersistence());
				prog.setNameProg(sched.getProgramme().getNameProg());
				prog.computeHashNameProg();
				sched.setProgramme(prog);
//				Hibernate.initialize(sched.getProgramme());
				Hibernate.initialize(sched.getChannel());
			}
		}
		return lSched;
	}
	
	/*
	 CUIDADO: ESTE MÉTODO NO TIENE UNO EN EL REPOSITORY ASOCIADO, 
	 SINO QUE LLAMA A schedRep.findByChannelOrderByStartAsc() y se queda solo
	 con el primer elemento
	 */
	@Transactional(readOnly = true)
	public Schedule findOneByChannelOrderByStartAsc(Channel ch, boolean refs) {
		List<Schedule> lSched = schedRep.findByChannelOrderByStartAsc(ch);
		if (lSched == null || lSched.size() == 0) {
			return null;
		}
		else {
			Schedule sched = lSched.get(0);
			if (refs) {
				Hibernate.initialize(sched);
				Hibernate.initialize(sched.getChannel());
				Hibernate.initialize(sched.getProgramme());
			}
			return sched;
		}
	}
	
	@Transactional(readOnly = true)
	public List<Schedule> findByChannelOrderByStartAsc(Channel ch, boolean refs) {
		List<Schedule> lSched = schedRep.findByChannelOrderByStartAsc(ch);
		if (refs){
			Hibernate.initialize(lSched);
			for (Schedule sched: lSched) {
				// Lo siguiente lo hago porque Hibernate.initialize(sched.getProgramme()) NO hace
				// lo que debería hacer
				Programme prog = new Programme();
				prog.setIdProgPersistence(sched.getProgramme().getIdProgPersistence());
				prog.setNameProg(sched.getProgramme().getNameProg());
				prog.computeHashNameProg();
				sched.setProgramme(prog);
//				Hibernate.initialize(sched.getProgramme());
				Hibernate.initialize(sched.getChannel());
			}
		}
		return lSched;
	}
	
//	@Transactional(readOnly = true)
//	public int setTruePublishedWhereIdSched(Long idSched) {
//		return schedRep.setTruePublishedWhereIdSched(idSched);
//	}
	
	@Transactional(readOnly = true)
	public int setTruePublished(List<Schedule> lSched) {
		int retVal = 0;
		for (Schedule sched: lSched) {
			retVal += schedRep.setTruePublishedWhereIdSched(sched.getIdSched());
		}
		return retVal;
	}
	
	@Transactional(readOnly = true)
	public int setFalsePublished(List<Schedule> lSched) {
		int retVal = 0;
		for (Schedule sched: lSched) {
			retVal += schedRep.setFalsePublishedWhereIdSched(sched.getIdSched());
		}
		return retVal;
	}
	
	@Transactional(readOnly = true)
	public List<Schedule> findByChannelEagerly(Channel ch) {
		List<Schedule> lSched = schedRep.findByChannelEagerly(ch);
		return lSched;
	}
	
	@Transactional(readOnly = true)
	public List<Schedule> findByChannelAndProgramme(Channel ch, Programme prog) {
		return schedRep.findByChannelAndProgrammeOrderByStartAsc(ch, prog);
	}
	
	@Transactional(readOnly = true)
	public List<Schedule> findByChannelAndProgrammeAndStartGreaterOrEqualThan(Channel ch, Programme prog, Date start, boolean refs) {
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
	public List<Schedule> findByProgramme(Programme prog, boolean refs) {
		List<Schedule> lSched = schedRep.findByProgramme(prog);
		if (refs){
			for (Schedule sched: lSched) {
				Hibernate.initialize(sched.getChannel());
				Hibernate.initialize(sched.getProgramme());
			}
		}
		return lSched;
	}
	
	@Transactional(readOnly = true)
	public List<Schedule> findByChannelAndProgrammeAndEndLessThan(Channel ch, Programme prog, Date end, boolean refs) {
		List<Schedule> lSched = schedRep.findByChannelAndProgrammeAndEndLessThanOrderByStartAsc(ch, prog, end);
		if (refs){
			for (Schedule sched: lSched) {
				Hibernate.initialize(sched.getChannel());
				Hibernate.initialize(sched.getProgramme());
			}
		}
		return lSched;
	}
	
	/**
	 * Devuelve de la base de datos, los schedules que empiezan secsFromStart
	 * segundos después del momento actual, y aquellos schedules en curso
	 */
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public List<Schedule> findByPublishedFalseAndSecondsFromStart(int secsFromStart) {
		Date now = new Date();
		Date afterStart = new Date(now.getTime() + ((long)(1000 * secsFromStart)));
		List<Schedule> lSched = 
				schedRep.findByStartBetweenAndPublishedFalseOrStartBeforeAndEndAfterAndPublishedFalseOrderByStartAsc(
						now, afterStart, now, now);
		for (Schedule sched: lSched) {
			Hibernate.initialize(sched.getChannel());
			Hibernate.initialize(sched.getProgramme());
			// Poner published a True
			if (sched.isPublished()) {
//				lSched.remove(sched);
			}
			else {
				schedRep.setTruePublishedWhereIdSched(sched.getIdSched());
			}
		}
		return lSched;
	}
	
	@Transactional(readOnly = true)
    public Schedule findByChannelAndProgrammeAndStartAndEnd(Channel ch, 
    		Programme prog, Date start, Date end) {
		return schedRep.findByChannelAndProgrammeAndStartAndEnd(ch, 
				prog, start, end);
    }
	
	@Transactional(propagation = Propagation.REQUIRES_NEW)
    public void delete(List<Schedule> lSched) {
    	schedRep.delete(lSched);
    }
	
	@Transactional(propagation = Propagation.REQUIRES_NEW)
    public int save(List<Schedule> lSched) {
		int numSched = 0;
		for (Schedule sched: lSched) {
			// Si el final del schedule NO es posterior al momento actual -> Saltarse el schedule
			if ( ! CommonUtility.isScheduleOnTime(sched)) {
				continue;
			}
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
					progRep.save(sched.getProgramme());
				}
				Schedule schedIn = schedRep.findByChannelAndProgrammeAndStartAndEnd(
						sched.getChannel(), sched.getProgramme(), sched.getStart(), sched.getEnd());
				if (schedIn == null) {
					schedRep.save(sched);
					numSched++;
				}
				else {
//					logger.debug("Schedule REPETIDO: "+schedIn);
				}
				
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		return numSched;
    }
	
	@Transactional(propagation = Propagation.REQUIRES_NEW)
    public void save(Schedule sched){
    	schedRep.save(sched);
    }
}
