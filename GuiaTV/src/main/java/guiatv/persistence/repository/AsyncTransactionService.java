package guiatv.persistence.repository;

import guiatv.persistence.domain.Channel;
import guiatv.persistence.domain.RtmpSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AsyncTransactionService {
	
	@Autowired
	ChannelRepository chRep;
	
	@Autowired
	RtmpSourceRepository rtmpRep;
	
//	@Transactional(propagation=Propagation.REQUIRES_NEW)
	@Transactional(readOnly = true)
	public RtmpSource getRtmpSourceFromNameIdChAndRtmpUrl(String nameIdCh, String rtmpUrl) {
		Channel ch = chRep.findByNameIdCh(nameIdCh);
		RtmpSource rtmpSource = rtmpRep.findByChannelAndRtmpUrl(ch, rtmpUrl);
		return rtmpSource;
	}
}
