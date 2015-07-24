package guiatv.realtime.classification;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.Timestamp;
import java.util.Date;

import guiatv.common.datatypes.Frame_OLD;
import guiatv.cv.classificator.Classif_old;
import guiatv.persistence.domain.Blob;
import guiatv.persistence.domain.Schedule;

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
	
	
	public ClassificationWorker() {
	}	
	
	public Schedule classify(Blob blob) {
		return null;
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
