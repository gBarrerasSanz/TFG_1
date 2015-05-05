package guiatv.computervision;

import guiatv.computervision.Classif;

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
        
        Core.rectangle(img, matchLoc, new Point(matchLoc.x + tpt.cols(),
                matchLoc.y + tpt.rows()), new Scalar(0, 255, 0));
        
        Imshow im = new Imshow("Title");
        im.showImage(img);
        // Save the visualized detection.
//        System.out.println("Writing "+ outFile);
//        Highgui.imwrite(outFile, img);
		return null;
	}

}
