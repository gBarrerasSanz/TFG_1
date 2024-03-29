package guiatv.realtime.classification;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Date;

import guiatv.computervision.CvUtils;
import guiatv.cv.classificator.Classif_old;
import guiatv.persistence.domain.BlobFrame;
import guiatv.persistence.domain.RtSchedule;
import guiatv.persistence.domain.RtSchedule.InstantState;
import guiatv.persistence.domain.Schedule;
import guiatv.persistence.domain.helper.ArffHelper;

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

import weka.core.Instance;


public class ClassificationWorker {
	
	
//	private static final Logger logger = Logger.getLogger("debugLog");
	
	public ClassificationWorker() {
	}	
	
	public RtSchedule classify(BlobFrame blob) {
		RtSchedule rtSched = null;
		try {
	//		logger.debug("Classifying blob from "+blob.getMlChannel().getChannel().getIdChBusiness());
			rtSched = new RtSchedule(blob.getMyCh(), new Date());
//			// DEBUG
//			Highgui.imwrite("img.jpeg", CvUtils.getGrayMatFromByteArray(
//					blob.getBlob(), blob.getBlobCols(), blob.getBlobRows()));
						
			Instance instance = ArffHelper.getUnlabeledInstance(blob.getMyCh().getTrainedModel().getDataSet(), blob);
//			blob.getMlChannel().getArffObject().getUnlabeledInstance(blob);
			double classifyVal = blob.getMyCh().getTrainedModel().getTrainedClassifier().classifyInstance(instance);
//			double classifyVal = blob.getMlChannel().getArffObject().getTrainedClassifier().classifyInstance(instance);
			if (classifyVal == 0) {
				rtSched.setState(InstantState.ON_PROGRAMME);
//				logger.debug(blob.getMlChannel().getChannel().getIdChBusiness()+
//						" -> ON_PROGRAMME");
			}
			else {
				rtSched.setState(InstantState.ON_ADVERTS);
//				logger.debug(blob.getMlChannel().getChannel().getIdChBusiness()+
//						" -> ON_ADVERTS");
			}
//			logger.debug("classifyVal = "+classifyVal);
		} catch(Exception e) {
			e.printStackTrace();
		}
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
