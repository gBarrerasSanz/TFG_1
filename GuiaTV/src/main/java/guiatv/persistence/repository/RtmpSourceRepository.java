//package guiatv.persistence.repository;
//
//import java.util.List;
//
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
//
//import guiatv.persistence.domain.Channel;
//import guiatv.persistence.domain.Programme;
//import guiatv.persistence.domain.Schedule;
//import guiatv.persistence.domain_NOT_USED.LearnedRtmpSource;
//import guiatv.persistence.domain_NOT_USED.RtmpSource;
//
//public interface RtmpSourceRepository extends JpaRepository<RtmpSource, Long>,
//		JpaSpecificationExecutor<RtmpSource> {
//
//	List<RtmpSource> findByChannel(Channel channel);	
//	
//	RtmpSource findByChannelAndRtmpUrl (Channel channel, String rtmpUrl);
//	
//}
