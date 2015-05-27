package guiatv.realtime.router;

import guiatv.common.datatypes.Frame;
import guiatv.cv.common.OpenCvUtils;
import guiatv.persistence.domain.Channel;
import guiatv.persistence.domain.RtmpSource;
import guiatv.persistence.repository.LearnedChannelRepository;
import guiatv.persistence.repository.LearnedChannelRepositoryImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.annotation.Router;
import org.springframework.messaging.MessageChannel;

public class FrameCustomRouter {
	
	@Autowired
	MessageChannel classificationChBridgeIn;
	
	@Autowired
	MessageChannel trainingChBridgeIn;
	
	@Autowired
	LearnedChannelRepositoryImpl learnedChRepImpl;
	
    @Router
    public MessageChannel processFrame(Frame frame) {
    	// TODO: Implementar
        boolean trained = learnedChRepImpl.isTrained(frame.getChannel(), frame.getRtmp());
        
        if (trained) {
        	return classificationChBridgeIn;
        }
        else {
        	return trainingChBridgeIn;
        }
    }

}