package guiatv.computervision;

import java.io.IOException;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;

public class CvUtils {

	/** CONFIGURATION PARAMS */
	private final static int imgCols = 1280;
	private final static int imgRows = 720;
	
	
	public static Mat getMatFromByteArray(byte[] byteArr) {
		Mat dataMat = new Mat(imgCols, imgRows, CvType.CV_8UC1);
		dataMat.put(0, 0, byteArr);
		Mat mat = Highgui.imdecode(dataMat, 1);
		return mat;
	}
	
	public static byte[] getByteArrayFromMat(Mat mat) {
		byte byteArr[] = new byte[(int) (mat.total() * mat.channels())];
		mat.get(0, 0, byteArr);
		return byteArr;
	}
	
	public static Mat getRoiFromMat(Mat mat, int[] topLeft, int[] botRight) {
		Mat dst = mat.submat(topLeft[1], botRight[1], topLeft[0], botRight[0]);
		return dst;
	}
	
	public static Mat thresholdImg(Mat img) {
		Mat dst = new Mat(img.rows(), img.cols(), img.type());
		Imgproc.adaptiveThreshold(img, dst, 255,
		         Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY_INV, 11, 2);
		return dst;
	}
	
	public static Mat loadMat(Resource resource) {
		try {
			return Highgui.imread(resource.getFile().getAbsolutePath(), Highgui.CV_LOAD_IMAGE_GRAYSCALE);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
}
