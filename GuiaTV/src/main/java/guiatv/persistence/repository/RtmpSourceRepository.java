package guiatv.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import guiatv.persistence.domain.Channel;
import guiatv.persistence.domain.LearnedRtmpSource;
import guiatv.persistence.domain.Programme;
import guiatv.persistence.domain.RtmpSource;
import guiatv.persistence.domain.Schedule;

public interface RtmpSourceRepository extends JpaRepository<RtmpSource, Long>,
		JpaSpecificationExecutor<RtmpSource> {

	List<RtmpSource> findByChannel(Channel channel);	
	
	RtmpSource findByChannelAndRtmpUrl (Channel channel, String rtmpUrl);
	
}
