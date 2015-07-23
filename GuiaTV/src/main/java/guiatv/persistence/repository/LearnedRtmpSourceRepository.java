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
//public interface LearnedRtmpSourceRepository extends JpaRepository<LearnedRtmpSource, Long>,
//		JpaSpecificationExecutor<LearnedRtmpSource> {
//
//	List<LearnedRtmpSource> findByRtmpSource(RtmpSource rtmp);
//	
//	LearnedRtmpSource findByRtmpSourceAndMethodAndLearned(
//			RtmpSource rtmp, String method, boolean learned);
//	
//	LearnedRtmpSource findByRtmpSourceAndLearnedTrue(RtmpSource rtmpSource);
//	
//	LearnedRtmpSource findByRtmpSourceAndLearnedFalse(RtmpSource rtmpSource);
//	
//	byte[] findTemplateImgByRtmpSource(RtmpSource rtmp);
//	
//}
