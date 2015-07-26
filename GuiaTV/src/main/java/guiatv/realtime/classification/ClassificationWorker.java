package guiatv.realtime.classification;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.Timestamp;
import java.util.Date;

import guiatv.common.datatypes.Frame_OLD;
import guiatv.cv.classificator.Classif_old;
import guiatv.persistence.domain.Blob;
import guiatv.persistence.domain.MLChannel;
import guiatv.persistence.domain.RtSchedule;
import guiatv.persistence.domain.Schedule;

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


public class ClassificationWorker {
	
	private static final Logger logger = Logger.getLogger("debugLog");
	
	public ClassificationWorker() {
	}	
	
	public RtSchedule classify(Blob blob) {
		assert(blob != null);
		assert(blob.getMlChannel() != null);
		assert(blob.getMlChannel().getChannel() != null);
		assert(blob.getMlChannel().getChannel().getIdChBusiness() != null);
//		logger.debug("Classifying blob from "+blob.getMlChannel().getChannel().getIdChBusiness());
		
		RtSchedule rtSched = new RtSchedule(blob.getMlChannel().getChannel(), 
								new Timestamp(new Date().getTime()));
		return rtSched;
	}
//	public RtEvent classify(Frame frame) {
//		RtEvent ev = new RtEvent();
//		ev.setChannel(frame.getRtmp().getChannel());
//		ev.setStart(new Timestamp(new Date().getTime()));
//		ev.setType(EventType.UNKNOWN);
//		
//		byte[] templateByteArr = learnedChRep.
//				findTemplateImgByRtmpSource(frame.getRtmp());
//		
//		if (templateByteArr == null) {
//			return ev;
//		}
//		else {
//			EventType type = ClassificationHelper.classifyByTemplateMatching(frame.getFrameImg(), templateByteArr);
//			ev.setType(type);
//			return ev;
//		}
//		
//	}
	

}
