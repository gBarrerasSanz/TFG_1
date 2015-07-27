package guiatv.persistence.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import guiatv.computervision.CvUtils;
import guiatv.computervision.Imshow;

import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;

import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;

@Entity(name = "arffobject")
public class ArffObject implements Serializable {
	
	@Id
    @Column(name = "idArffPersistence", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idArffPersistence;
	
	@Column(name = "atts", nullable = true)
	private FastVector atts; // Atributos
	
//	@Column(name = "data", nullable = true)
	private Instances data; // Muestras
	
	private static FastVector pixelAttVals;
	private static FastVector classAttVals;
	
	public ArffObject() {
		pixelAttVals = new FastVector(2);
		pixelAttVals.addElement("b");
		pixelAttVals.addElement("w");
		
		classAttVals = new FastVector(2);
		classAttVals.addElement("true");
		classAttVals.addElement("false");
	}
	
//	/**
//	 * blob: imagen de 3 canales correspondiente al Roi
//	 * truth: Clasificación humana del blob ( true => Emisión; false => Anuncios)
//	 */
//	public void addSample(Blob blob, boolean truth) {
//		// Se saca el objecto mat del blob
//		Mat img = CvUtils.getMatFromByteArray(blob.getBlob(), blob.getBlobCols(), blob.getBlobRows());
//		// Se binariza la imagen 
//		Mat binImg = CvUtils.thresholdImg(img);
//		// Se obtiene el array de bytes de la imagen binarizada
//		byte[] binBlob = CvUtils.getByteArrayFromMat(binImg);
//		
//		if (data.numInstances() == 0) {
//			setupAtts(img);
//		}
//		
//		double[] vals = new double[data.numAttributes()];
//		for (int i=0; i < blob.getBlob().length; i++) {
//			String val = (binBlob[i] == 0) ? "b" : "w";
//			vals[i] = pixelAttVals.indexOf(val);
//		}
//		vals[atts.size()-1] = classAttVals.indexOf(String.valueOf(truth));
//		data.add(new Instance(1.0, vals));
//	}
	
	public Instances getData() {
		return data;
	}
	
	public void setupAtts(Blob blob) {
		// Se saca el objecto mat del blob
		Mat img = CvUtils.getMatFromByteArray(blob.getBlob(), blob.getBlobCols(), blob.getBlobRows());
//		// Se binariza la imagen 
//		Mat binImg = CvUtils.thresholdImg(img);
//		// Se obtiene el array de bytes de la imagen binarizada
//		byte[] binBlob = CvUtils.getByteArrayFromMat(binImg);
		for (int r=0; r<img.rows(); r++) {
			for (int c=0; c<img.cols(); c++) {
				atts.addElement(new Attribute(r+"_"+c, pixelAttVals));
			}
		}
		// Atributo de clase		
		Attribute classAtt = new Attribute("class", classAttVals);
		atts.addElement(classAtt);
		data = new Instances("MiRelacion", atts, 0);
		data.setClass(classAtt); // Establece el atributo classAtt como el atributo que indica la clase
	}
	
    /**********************************************************
     * 					GETTERS / SETTERS
     *********************************************************/
	
	public FastVector getAtts() {
		return atts;
	}

	public void setAtts(FastVector atts) {
		this.atts = atts;
	}

	public static FastVector getPixelAttVals() {
		return pixelAttVals;
	}

	public static void setPixelAttVals(FastVector pixelAttVals) {
		ArffObject.pixelAttVals = pixelAttVals;
	}

	public static FastVector getClassAttVals() {
		return classAttVals;
	}

	public static void setClassAttVals(FastVector classAttVals) {
		ArffObject.classAttVals = classAttVals;
	}

	public Long getIdArffPersistence() {
		return idArffPersistence;
	}

	public void setData(Instances data) {
		this.data = data;
	}
	
	
	
}
