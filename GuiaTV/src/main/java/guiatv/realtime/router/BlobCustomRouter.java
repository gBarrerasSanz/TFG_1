package guiatv.realtime.router;

import guiatv.computervision.CvUtils;
import guiatv.persistence.domain.Blob;
import guiatv.persistence.domain.Channel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.annotation.Router;
import org.springframework.messaging.MessageChannel;

public class BlobCustomRouter {
	
	
	
    @Router
    public String routeBlob(Blob blob) {
    	// TODO: Implementar
//        boolean trained = learnedChRepImpl.isTrained(frame.getChannel(), frame.getRtmp());
        // DEBUG: Mostrar imagen
//    	CvUtils.showBlob(blob);
    	
    	boolean trained = true; // TODO: REMOVE THIS
        if (trained) {
        	return "classificationChIn";
        }
        else {
        	return "trainingChIn";
        }
    }

}