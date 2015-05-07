package guiatv.computervision.classificator;

import guiatv.computervision.classificator.Classif;

import org.opencv.core.Core;
import org.opencv.core.Core.MinMaxLocResult;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;


public class TemplateMatchingClassif implements Classif{
	
	private final int MATCH_METHOD = Imgproc.TM_SQDIFF_NORMED;
	///
	private Mat tpt;
	
	public TemplateMatchingClassif(Mat tpt) {
		this.tpt = tpt;
	}
	
	@Override
	public boolean learn() {
		return true;
	}

	@Override
	public ClassifResult classify(Mat img) {
//		Mat result = null;
		int result_cols = img.cols() - tpt.cols() + 1;
        int result_rows = img.rows() - tpt.rows() + 1;
		Mat result = new Mat(result_rows, result_cols, CvType.CV_32FC1);
		Imgproc.matchTemplate(img, tpt, result, MATCH_METHOD);
		MinMaxLocResult mmr = Core.minMaxLoc(result);
		Point matchLoc;
        matchLoc = mmr.minLoc;
        
//        showTemplate(img,  matchLoc);
        
        if (mmr.maxVal == 1) { 	return ClassifResult.PROGRAM; }
        else {					return ClassifResult.ADVERTISEMENT; }
	}
	
	
	private void showTemplate(Mat img, Point matchLoc) {
		 Core.rectangle(img, matchLoc, new Point(matchLoc.x + tpt.cols(),
	                matchLoc.y + tpt.rows()), new Scalar(0, 255, 0));
	        
	        Imshow im = new Imshow("Title");
	        im.showImage(img);
	        
	        // Save Image
	        // Save the visualized detection.
//	        System.out.println("Writing "+ outFile);
//	        Highgui.imwrite(outFile, img);
	}

}
