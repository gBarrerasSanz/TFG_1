package guiatv.cv.common;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;

public class OpenCVUtils {
	
	static {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }
	
	public static byte[] getTemplate(Long idCh, String rtmpUrl) {
		// TODO: Implementar
		return null;
	}
	
	public static boolean isTrained(Long idCh, String rtmpUrl) {
		// TODO: Implementar
		return false;
	}
	
	public static Mat getMatFromByteArray(byte[] byteArray) {
		Mat dataMat = new Mat(512, 512, CvType.CV_8UC1);
		dataMat.put(0, 0, byteArray);
		Mat frameMat = Highgui.imdecode(dataMat, 1);
		return frameMat;
	}
	
}
