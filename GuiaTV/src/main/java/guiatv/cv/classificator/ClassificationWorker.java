package guiatv.cv.classificator;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Date;

import guiatv.common.datatypes.Frame;
import guiatv.cv.classificator.Classif_old;
import guiatv.cv.classificator.ClassificationHelper;
import guiatv.cv.common.OpenCvUtils;
import guiatv.persistence.domain.RtEvent;
import guiatv.persistence.domain.RtEvent.EventType;
import guiatv.persistence.domain.RtEvent;
import guiatv.persistence.repository.LearnedChannelRepository;
import guiatv.persistence.repository.LearnedChannelRepositoryImpl;

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
	
	@Autowired
	LearnedChannelRepository learnedChRep;
	
	public ClassificationWorker() {
	}	

	public RtEvent classify(Frame frame) {
		RtEvent ev = new RtEvent();
		ev.setChannel(frame.getChannel());
		ev.setStart(new Date());
		ev.setType(EventType.UNKNOWN);
		
		byte[] templateByteArr = learnedChRep.
				findTemplateImgByChannelAndRtmpSource(frame.getChannel(), frame.getRtmp());
		
		if (templateByteArr == null) {
			return ev;
		}
		else {
			EventType type = ClassificationHelper.classifyByTemplateMatching(frame.getFrameImg(), templateByteArr);
			ev.setType(type);
			return ev;
		}
		
	}
	

}
