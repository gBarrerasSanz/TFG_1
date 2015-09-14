package guiatv.persistence.repository.service;

import java.util.List;

import guiatv.persistence.domain.Channel;
import guiatv.persistence.domain.Programme;
import guiatv.persistence.domain.Schedule;
import guiatv.persistence.repository.ChannelRepository;
import guiatv.persistence.repository.ProgrammeRepository;
import guiatv.persistence.repository.ScheduleRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AsyncTransactionService {
	
	@Autowired
	ChannelRepository chRep;
	
	@Autowired
	ScheduleRepository schedRep;
	
	@Autowired
	ProgrammeRepository progRep;
	
	
////	@Transactional(propagation=Propagation.REQUIRES_NEW)
//	@Transactional(readOnly = true)
//	public RtmpSource getRtmpSourceFromNameIdChAndRtmpUrl(String idChBusiness, String rtmpUrl) {
//		Channel ch = chRep.findByIdChBusiness(idChBusiness);
//		RtmpSource rtmpSource = rtmpRep.findByChannelAndRtmpUrl(ch, rtmpUrl);
//		return rtmpSource;
//	}
	
//	@Transactional(propagation = Propagation.REQUIRES_NEW)
//    public void insertRtmpSources(List<RtmpSource> ListRtmpSources) {
//    	rtmpRep.save(ListRtmpSources);
//    }
	
//	@Transactional(propagation = Propagation.REQUIRES_NEW)
//    public void insertSchedules(List<Schedule> lSchedules) {
//    	schedRep.save(lSchedules);
//    }
	
	@Transactional(propagation = Propagation.REQUIRES_NEW)
    public void deleteSchedules(List<Schedule> lSchedules) {
    	schedRep.delete(lSchedules);
    }
	
	@Transactional(propagation = Propagation.REQUIRES_NEW)
    public void insertProgrammes(List<Programme> lProg) {
    	progRep.save(lProg);
    }
	
	@Transactional(propagation = Propagation.REQUIRES_NEW)
    public void deleteProgrammes(List<Programme> lProg) {
    	progRep.delete(lProg);
    }
	
	@Transactional(propagation = Propagation.REQUIRES_NEW)
    public void insertChannels(List<Channel> lCh) {
    	chRep.save(lCh);
    }
	
	@Transactional(propagation = Propagation.REQUIRES_NEW)
    public void deleteChannels(List<Channel> lCh) {
    	chRep.delete(lCh);
    }
	
	@Transactional(propagation = Propagation.REQUIRES_NEW)
    public void deleteAllData() {
    	schedRep.deleteAll();
    	chRep.deleteAll();
    	progRep.deleteAll();
    }
}
