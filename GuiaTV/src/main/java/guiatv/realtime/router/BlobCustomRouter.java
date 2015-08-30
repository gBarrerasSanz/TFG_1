package guiatv.realtime.router;

import guiatv.computervision.CvUtils;
import guiatv.persistence.domain.Blob;
import guiatv.persistence.domain.Channel;
import guiatv.persistence.repository.service.MyChService;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.annotation.Router;
import org.springframework.messaging.MessageChannel;

public class BlobCustomRouter {
	
	private static final Logger logger = Logger.getLogger("debugLog");
	
	@Autowired
	MyChService myChServ;
	
    @Router
    public String routeBlob(Blob blob) {
    	// TODO: Implementar
//        boolean trained = learnedChRepImpl.isTrained(frame.getChannel(), frame.getRtmp());
        // DEBUG: Mostrar imagen
//    	CvUtils.showBlob(blob);
    	
    	assert(blob != null);
    	boolean trained = blob.getMyCh().getTrainedModel().isTrained();
        if (trained) {
        	return "classificationChIn";
        }
        else {
        	return "trainingChIn";
        }
    }

}