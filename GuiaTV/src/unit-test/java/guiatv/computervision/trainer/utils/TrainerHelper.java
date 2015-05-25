package guiatv.computervision.trainer.utils;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;

public class TrainerHelper {
	
	public static Mat getMatFromByteArray(byte[] byteArray) {
		Mat dataMat = new Mat(512, 512, CvType.CV_8UC1);
		dataMat.put(0, 0, byteArray);
		Mat frameMat = Highgui.imdecode(dataMat, 1);
		return frameMat;
	}
}
