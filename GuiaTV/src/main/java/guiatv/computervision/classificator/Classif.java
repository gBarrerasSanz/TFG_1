package guiatv.computervision.classificator;

import org.opencv.core.Mat;

public interface Classif {
	
	public static enum ClassifResult {
		ADVERTISEMENT, PROGRAM
	}
	
	public boolean train(Mat tpt);
	
	public ClassifResult classify(Mat img);  
	
}
