package guiatv.realtime.router;

import guiatv.computervision.CvUtils;
import guiatv.persistence.domain.BlobFrame;
import guiatv.persistence.domain.Channel;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.annotation.Router;
import org.springframework.messaging.MessageChannel;

public class BlobCustomRouter {
	
	private static final Logger logger = Logger.getLogger("debugLog");
	
//	@Autowired
//	MyChService myChServ;
	
    @Router
    public String routeBlob(BlobFrame blob) {
    	// TODO: Implementar
//        boolean trained = learnedChRepImpl.isTrained(frame.getChannel(), frame.getRtmp());
        // DEBUG: Mostrar imagen
//    	CvUtils.showBlob(blob);
    	
    	assert(blob != null);
    	boolean trained;
    	try {
    		trained = blob.getMyCh().getTrainedModel().isTrained();
    	} catch(IllegalAccessException e) {
    		trained = false;
    	}
        if (trained) {
        	return "classificationChIn";
        }
        else {
        	return "trainingChIn";
        }
    }

}