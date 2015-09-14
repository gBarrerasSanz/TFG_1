package guiatv.computervision;

import guiatv.persistence.domain.blobFrame;
import guiatv.persistence.domain.MyCh;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;

public class CvUtils {

	/** CONFIGURATION PARAMS */
//	private final static int imgCols = 1280;
//	private final static int imgRows = 720;
	
	
	public static Mat getGrayMatFromByteArray(byte[] byteArr, int imgCols, int imgRows) {
		Mat dataMat = new Mat(imgRows, imgCols, CvType.CV_8UC1);
		dataMat.put(0, 0, byteArr);
		return dataMat;
//		Mat mat = Highgui.imdecode(dataMat, Highgui.CV_LOAD_IMAGE_GRAYSCALE);
//		return mat;
	}
	
	public static Mat getGrayMatAndDecodeFromByteArray(byte[] byteArr, int imgCols, int imgRows) {
		Mat dataMat = new Mat(imgRows, imgCols, CvType.CV_8UC1);
		dataMat.put(0, 0, byteArr);
		Mat mat = Highgui.imdecode(dataMat, Highgui.CV_LOAD_IMAGE_GRAYSCALE);
		return mat;
	}
	
//	public static byte[] getByteArrayFromMat(Mat mat) {
//		MatOfByte bytemat = new MatOfByte();
//		Highgui.imencode(".png", mat, bytemat);
//		byte[] byteArr = bytemat.toArray();
//		return byteArr;
//	}
	
	public static byte[] convertByteArrayToPngDecodeable(byte[] byteArr, int imgCols, int imgRows) {
		Mat mat = getGrayMatFromByteArray(byteArr, imgCols, imgRows);
		MatOfByte bytemat = new MatOfByte();
		Highgui.imencode(".png", mat, bytemat);
		byte[] pngByteArr = bytemat.toArray();
		return pngByteArr;
	}
	
	/*
	 * ESTA ES LA QUE FUNCIONA HASTA EL MOMENTO
	 */
	public static byte[] getByteArrayFromMat(Mat mat) {
		byte byteArr[] = new byte[(int) (mat.total() * mat.channels())];
		mat.get(0, 0, byteArr);
		return byteArr;
	}
	
	public static Mat getRoiFromMat(Mat mat, int[] topLeft, int[] botRight) {
		Mat dst = mat.submat(topLeft[1], botRight[1], topLeft[0], botRight[0]);
		return dst;
	}
	
	public static Mat getThresholdImg(Mat img) {
		Mat dst = new Mat(img.rows(), img.cols(), img.type());
		Imgproc.adaptiveThreshold(img, dst, 255,
		         Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY_INV, 11, 2);
		return dst;
	}
	
	public static void threshold(Mat img) {
		Imgproc.adaptiveThreshold(img, img, 255,
		         Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY_INV, 11, 2);
	}
	
	public static Mat loadMat(Resource resource) {
		try {
			return Highgui.imread(resource.getFile().getAbsolutePath(), Highgui.CV_LOAD_IMAGE_GRAYSCALE);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static void showBlob(blobFrame blob) {
		Mat blobMat = getGrayMatFromByteArray(blob.getBlobImg(), blob.getBlobCols(), blob.getBlobRows());
		Imshow im = new Imshow("Image");
		im.showImage(blobMat);
	}
	
	public static List<blobFrame> loadFileListGroup(File[] fList, MyCh myCh) {
		List<blobFrame> lBlob = new ArrayList<blobFrame>();
		for (File imgFile: fList) {
			try {
				Mat imgMat = Highgui.imread(imgFile.getAbsolutePath(), Highgui.CV_LOAD_IMAGE_GRAYSCALE);
				byte[] imgData = CvUtils.getByteArrayFromMat(imgMat);
				blobFrame blob = new blobFrame(imgData, myCh);
				lBlob.add(blob);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				return null;
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
    	}
		return lBlob;
	}

}
