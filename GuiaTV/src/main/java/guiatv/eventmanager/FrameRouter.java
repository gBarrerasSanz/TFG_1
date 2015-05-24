package guiatv.eventmanager;

import guiatv.common.datatypes.Frame;
import guiatv.cv.common.OpenCVUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.annotation.Router;
import org.springframework.messaging.MessageChannel;

public class FrameRouter {
	
	@Autowired
	MessageChannel classificationChBridgeIn;
	
	@Autowired
	MessageChannel trainingChBridgeIn;
	
    @Router
    public MessageChannel processFrame(Frame frame) {
    	// TODO: Implementar
    	Long idCh = 1L;
        String rtmpUrl = "";
        boolean trained = OpenCVUtils.isTrained(idCh, rtmpUrl);
        
        if (trained) {
        	return classificationChBridgeIn;
        }
        else {
        	return trainingChBridgeIn;
        }
    }

}