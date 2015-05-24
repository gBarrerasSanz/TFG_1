package guiatv.cv.common;

import guiatv.persistence.repository.ScheduleRepository;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;
import org.springframework.beans.factory.annotation.Autowired;


public class OpenCvUtils {
	
	public static Mat getMatFromByteArray(byte[] byteArray) {
		Mat dataMat = new Mat(512, 512, CvType.CV_8UC1);
		dataMat.put(0, 0, byteArray);
		Mat frameMat = Highgui.imdecode(dataMat, 1);
		return frameMat;
	}
	
}
