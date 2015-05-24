package guiatv.cv.classificator;

import org.opencv.core.Mat;

public interface Classif_old {
	
	public static enum ClassifResult {
		ADVERTISEMENT, PROGRAM
	}
	
	public boolean train(Mat tpt);
	
	public ClassifResult classify(Mat img);  
	
}
