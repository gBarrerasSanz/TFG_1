package guiatv.computervision.classificator;

import java.util.ArrayList;
import java.util.List;

import guiatv.computervision.Imshow;

import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;
import org.opencv.imgproc.Moments;

import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;

public class ArffInstanceMoments implements Arff {

	private String dataSetName;
	private int numData;

	private int[] topLeft;
	private int[] botRight;

	private int numRows;
	private int numCols;

//	private FastVector pixelAttVals;
	private FastVector classAttVals;
	private FastVector atts; // Atributos
	private Instances data;
	private Instances dataRel;
	
	private final static int NUM_MOMENTS = 18;

	public ArffInstanceMoments(String dataSetName, int[] topLeft, int[] botRight) {
		this.dataSetName = dataSetName;
		this.numData = 0;
		this.atts = new FastVector();
		this.topLeft = topLeft;
		this.botRight = botRight;

//		this.pixelAttVals = new FastVector(2);
//		this.pixelAttVals.addElement("b");
//		this.pixelAttVals.addElement("w");

		this.classAttVals = new FastVector(2);
		this.classAttVals.addElement("true");
		this.classAttVals.addElement("false");
	}

	public FastVector getAtts() {
		return atts;
	}

	public void addSample(String imgPath, boolean truth) {
		Mat img = getMatFromImgPath(imgPath);
		Imshow im = new Imshow("Image");
		if (numData == 0) {
			setupAtts(img);
		}
		// Extraer contornos
		List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
		Imgproc.findContours(img, contours, new Mat(), Imgproc.RETR_LIST,
				Imgproc.CHAIN_APPROX_SIMPLE);
		// Extraer momentos hu
		Moments mom = new Moments();
		double[] vals = new double[data.numAttributes()];
//		if (contours.size() > 0) {
//			mom = Imgproc.moments(contours.get(0), false);
//			Mat hu = new Mat();
//			
////			Imgproc.HuMoments(mom, hu);
//			for (int i=0; i<hu.rows(); i++) {
////				vals[i] = hu.get(i, 0)[0];
//			}
//			
//		}
//		else { // SI no tiene contornos
//			for (int i=0; i< NUM_MOMENTS; i++) {
//				vals[i] = -1;
//			}
//		}
		mom = Imgproc.moments(img);
		vals[0] = mom.get_m00();
		vals[1] = mom.get_m01();
		vals[2] = mom.get_m02();
		vals[3] = mom.get_m03();
		vals[4] = mom.get_m10();
		vals[5] = mom.get_m11();
		vals[6] = mom.get_m12();
		vals[7] = mom.get_m20();
		vals[8] = mom.get_m21();
		vals[9] = mom.get_m30();
		vals[10] = mom.get_m30();
		vals[11] = mom.get_nu02();
		vals[12] = mom.get_nu03();
		vals[13] = mom.get_nu11();
		vals[14] = mom.get_nu12();
		vals[15] = mom.get_nu20();
		vals[16] = mom.get_nu21();
		vals[17] = mom.get_nu30();
		vals[atts.size() - 1] = classAttVals.indexOf(String.valueOf(truth));
		data.add(new Instance(1.0, vals));
		numData++;
	}

	public Instances getData() {
		return data;
	}

	private void setupAtts(Mat img) {
		for (int h = 0; h < NUM_MOMENTS; h++) {
			atts.addElement(new Attribute("h"+h));
		}
		// Atributo de clase
		Attribute classAtt = new Attribute("class", classAttVals);
		atts.addElement(classAtt);
		data = new Instances("MiRelacion", atts, 0);
		data.setClass(classAtt); // Establece el atributo classAtt como el
									// atributo que indica la clase
	}

	private Mat getMatFromImgPath(String imgPath) {
		Mat src = Highgui.imread(imgPath, Highgui.CV_LOAD_IMAGE_GRAYSCALE);
		Mat dst = src.submat(topLeft[1], botRight[1], topLeft[0], botRight[0]);
//		Imgproc.adaptiveThreshold(dst, dst, 255,
//				Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY_INV, 11,
//				2);
		
//		Imgproc.adaptiveThreshold(dst, dst, 255,
//			Imgproc.ADAPTIVE_THRESH_GAUSSIAN_C, Imgproc.THRESH_BINARY_INV, 11,
//			2);
		
//		Imgproc.Canny(dst, dst, 100, 300, 5, true); 
//		Imgproc.threshold(dst, dst, 255, Imgproc.ADAPTIVE_THRESH_MEAN_C, 
//				Imgproc.THRESH_BINARY_INV);

//		Imgproc.threshold(dst, dst, 255, 255, 
//				Imgproc.THRESH_BINARY_INV);
		
		Imgproc.threshold(dst, dst, 255, 255, 
				Imgproc.THRESH_OTSU);
		
		return dst;
	}

}