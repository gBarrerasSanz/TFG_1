package guiatv.realtime.router;

import guiatv.common.datatypes.Frame;
import guiatv.cv.common.OpenCvUtils;
import guiatv.persistence.domain.Channel;
import guiatv.persistence.domain.RtmpSource;
import guiatv.persistence.repository.LearnedRtmpSourceRepository;
import guiatv.persistence.repository.LearnedRtmpSourceRepositoryImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.annotation.Router;
import org.springframework.messaging.MessageChannel;

public class FrameCustomRouter {
	
	
	@Autowired
	LearnedRtmpSourceRepositoryImpl learnedChRepImpl;
	
    @Router
    public String processFrame(Frame frame) {
    	// TODO: Implementar
//        boolean trained = learnedChRepImpl.isTrained(frame.getChannel(), frame.getRtmp());
        boolean trained = true; // TODO: REMOVE THIS
        if (trained) {
        	return "classificationChIn";
        }
        else {
        	return "trainingChIn";
        }
    }

}