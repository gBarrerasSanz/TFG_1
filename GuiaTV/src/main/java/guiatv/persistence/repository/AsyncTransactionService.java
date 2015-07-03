package guiatv.persistence.repository;

import java.util.List;

import guiatv.catalog.datatypes.ListChannels;
import guiatv.catalog.datatypes.ListProgrammes;
import guiatv.persistence.domain.Channel;
import guiatv.persistence.domain.Programme;
import guiatv.persistence.domain.RtmpSource;
import guiatv.persistence.domain.Schedule;

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
	
	@Autowired
	RtmpSourceRepository rtmpRep;
	
//	@Transactional(propagation=Propagation.REQUIRES_NEW)
	@Transactional(readOnly = true)
	public RtmpSource getRtmpSourceFromNameIdChAndRtmpUrl(String idChBusiness, String rtmpUrl) {
		Channel ch = chRep.findByIdChBusiness(idChBusiness);
		RtmpSource rtmpSource = rtmpRep.findByChannelAndRtmpUrl(ch, rtmpUrl);
		return rtmpSource;
	}
	
	@Transactional(propagation = Propagation.REQUIRES_NEW)
    public void insertRtmpSources(List<RtmpSource> ListRtmpSources) {
    	rtmpRep.save(ListRtmpSources);
    }
	
	@Transactional(propagation = Propagation.REQUIRES_NEW)
    public void insertSchedules(List<Schedule> lSchedules) {
    	schedRep.save(lSchedules);
    }
	
	@Transactional(propagation = Propagation.REQUIRES_NEW)
    public void insertProgrammes(ListProgrammes lProg) {
    	progRep.save(lProg);
    }
	
	@Transactional(propagation = Propagation.REQUIRES_NEW)
    public void insertChannels(ListChannels lCh) {
    	chRep.save(lCh);
    }
}
