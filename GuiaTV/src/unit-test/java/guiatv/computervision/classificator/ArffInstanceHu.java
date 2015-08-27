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

public class ArffInstanceHu {

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

	public ArffInstanceHu(String dataSetName, int[] topLeft, int[] botRight) {
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
		if (contours.size() > 0) {
			mom = Imgproc.moments(contours.get(0), false);
			Mat hu = new Mat();
			Imgproc.HuMoments(mom, hu);
			for (int i=0; i<hu.rows(); i++) {
				vals[i] = hu.get(i, 0)[0];
			}
		}
		else { // SI no tiene contornos
			for (int i=0; i<7; i++) {
				vals[i] = -1;
			}
		}
		vals[atts.size() - 1] = classAttVals.indexOf(String.valueOf(truth));
		data.add(new Instance(1.0, vals));
		numData++;
	}

	public Instances getData() {
		return data;
	}

	private void setupAtts(Mat img) {
		for (int h = 0; h < 7; h++) {
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
		Imgproc.adaptiveThreshold(dst, dst, 255,
				Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY_INV, 11,
				2);
		
//		Imgproc.Canny(dst, dst, 100, 300, 5, true); 
//		Imgproc.threshold(dst, dst, 255, Imgproc.ADAPTIVE_THRESH_MEAN_C, 
//				Imgproc.THRESH_BINARY_INV);
		return dst;
	}

}