package guiatv.computervision.classificator;


import guiatv.computervision.Imshow;

import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;

import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;

public class ArffInstance implements Arff {
	
	private String 			dataSetName;
	private int 			numData;
	
	private int[] 			topLeft;
	private int[] 			botRight;
	
	private int 			numRows;
	private int				numCols;
	
	private FastVector 		pixelAttVals;
	private FastVector		classAttVals;
	private FastVector      atts; // Atributos
	private Instances 		data;
	private Instances       dataRel;
	
	public ArffInstance(String dataSetName, int[] topLeft, int[] botRight) {
		this.dataSetName = dataSetName;
		this.numData = 0;
		this.atts = new FastVector();
		this.topLeft = topLeft;
		this.botRight = botRight;
		
		this.pixelAttVals = new FastVector(2);
		this.pixelAttVals.addElement("b");
		this.pixelAttVals.addElement("w");
		
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
//		im.showImage(img); // mostrar imagen
		if (numData == 0) {
			setupAtts(img);
		}
		byte bytsImg[] = new byte[(int) (img.total() * img.channels())];
		img.get(0, 0, bytsImg);
		
		double[] vals = new double[data.numAttributes()];
		for (int i=0; i < bytsImg.length; i++) {
			String val = (bytsImg[i] == 0) ? "b" : "w";
			vals[i] = pixelAttVals.indexOf(val);
		}
//		String truthVal = (truth == true) ? "true" : "false";
		vals[atts.size()-1] = classAttVals.indexOf(String.valueOf(truth));
		data.add(new Instance(1.0, vals));
		numData++;
		
		
//		Instance dataInstance = new Instance(bytsImg.length + 1);
//		for (int i=0; i < bytsImg.length; i++) {
//			int val = bytsImg[i];
//			// Create the instance
//			 dataInstance.setValue((Attribute)atts.elementAt(i), val);
//		}
//		String truthVal = (truth == true) ? "true" : "false";
//		dataInstance.setValue((Attribute)atts.elementAt(atts.size()-1), truthVal);
//		data.add(dataInstance);
//		numData++;
	}
	
	public Instances getData() {
		return data;
	}
	
	private void setupAtts(Mat img) {
		for (int r=0; r<img.rows(); r++) {
			for (int c=0; c<img.cols(); c++) {
				atts.addElement(new Attribute(r+"_"+c, pixelAttVals));
			}
		}
		// Atributo de clase		
		Attribute classAtt = new Attribute("class", classAttVals);
		atts.addElement(classAtt);
//		for (int i=0; i<atts.capacity(); i++) {
//			for (int j=0; j<atts.capacity(); j++) {
//				if (atts.elements(i).equals(atts.elements(j))) {
//					System.err.println(atts.elements(i)+", "+atts.elements(j));
//				}
//			}
//		}
		data = new Instances("MiRelacion", atts, 0);
		data.setClass(classAtt); // Establece el atributo classAtt como el atributo que indica la clase
	}
	
	private Mat getMatFromImgPath(String imgPath) {
		Mat src = Highgui.imread(imgPath, Highgui.CV_LOAD_IMAGE_GRAYSCALE);
    	Mat dst = src.submat(topLeft[1], botRight[1], topLeft[0], botRight[0]);

	Imgproc.adaptiveThreshold(dst, dst, 255,
		Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY_INV, 11,
		2);

//	Imgproc.adaptiveThreshold(dst, dst, 255,
//		Imgproc.ADAPTIVE_THRESH_GAUSSIAN_C, Imgproc.THRESH_BINARY_INV, 11,
//		2);
	
	//Imgproc.Canny(dst, dst, 100, 300, 5, true); 
	//Imgproc.threshold(dst, dst, 255, Imgproc.ADAPTIVE_THRESH_MEAN_C, 
	//		Imgproc.THRESH_BINARY_INV);
	
//	Imgproc.threshold(dst, dst, 255, 255, 
//			Imgproc.THRESH_BINARY_INV);
	
//	Imgproc.threshold(dst, dst, 255, 255, 
//			Imgproc.THRESH_OTSU);

    return dst;
	
	}
	
}