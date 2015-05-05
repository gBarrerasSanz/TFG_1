package guiatv.computervision;

import org.opencv.core.Mat;

public interface Classif {
	
	public static enum ClassifResult {
		ADVERTISEMENT, PROGRAM
	}
	
	public boolean learn();
	
	public ClassifResult classify(Mat img);  
	
}
