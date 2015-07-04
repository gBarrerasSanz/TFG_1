package guiatv.persistence.repository.service;

import java.util.List;

import guiatv.catalog.datatypes.ListProgrammes;
import guiatv.persistence.domain.Channel;
import guiatv.persistence.domain.Programme;
import guiatv.persistence.domain.RtmpSource;
import guiatv.persistence.domain.Schedule;
import guiatv.persistence.repository.ChannelRepository;
import guiatv.persistence.repository.ProgrammeRepository;
import guiatv.persistence.repository.RtmpSourceRepository;
import guiatv.persistence.repository.ScheduleRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProgrammeService {
	
	@Autowired
	ProgrammeRepository progRep;
	
	@Transactional(readOnly = true)
	public ListProgrammes findAll() {
		return progRep.findAll();
	}
	
	@Transactional(readOnly = true)
	public Programme findByNameProg(String nameProg) {
		return progRep.findByNameProg(nameProg);
	}
	
	@Transactional(propagation = Propagation.REQUIRES_NEW)
    public void insertProgrammes(List<Programme> lProg) {
    	progRep.save(lProg);
    }
	
	@Transactional(propagation = Propagation.REQUIRES_NEW)
    public void deleteProgrammes(List<Programme> lProg) {
    	progRep.delete(lProg);
    }
	
}
