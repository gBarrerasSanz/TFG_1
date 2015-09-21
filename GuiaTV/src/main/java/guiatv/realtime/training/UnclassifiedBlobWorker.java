package guiatv.realtime.training;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;

import guiatv.cv.classificator.Classif_old;
import guiatv.persistence.domain.BlobFrame;
import guiatv.persistence.repository.service.BlobService;

import org.apache.log4j.Logger;
import org.opencv.core.Core;
import org.opencv.core.Core.MinMaxLocResult;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;


public class UnclassifiedBlobWorker {
	
	private static final Logger logger = Logger.getLogger("debugLog");
	
	@Autowired
	BlobService blobServ;
	
	public UnclassifiedBlobWorker() {
	}
	
	public void storeBlob(BlobFrame blob) {
		blobServ.save(blob);
		
//		logger.debug("Received blob from channel "+blob.getMlChannel().getChannel().getIdChBusiness());
//		if (blobServ.count() <= 2000) {
//			blobServ.save(blob);
//		}
//		else {
//			logger.debug("Stored 2000 blobs already");
//		}
	}

}
