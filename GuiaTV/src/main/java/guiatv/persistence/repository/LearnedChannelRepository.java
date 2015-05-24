package guiatv.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import guiatv.persistence.domain.Channel;
import guiatv.persistence.domain.LearnedChannel;
import guiatv.persistence.domain.Programme;
import guiatv.persistence.domain.RtmpSource;
import guiatv.persistence.domain.Schedule;

public interface LearnedChannelRepository extends JpaRepository<LearnedChannel, Long>,
		JpaSpecificationExecutor<LearnedChannel> {

	LearnedChannel findByChannelAndRtmpSource(Channel ch, RtmpSource rtmp);
	
	byte[] findTemplateImgByChannelAndRtmpSource(Channel ch, RtmpSource rtmp);
	
	
//	Schedule findByIdSched(Long idSched);
//
//	List<Schedule> findByChannel(Channel ch);
//	
//	List<Programme> findByProgramme(Programme prog);
	
}
