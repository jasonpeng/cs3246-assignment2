package cs3246.a2.web;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import cs3246.a2.ColorCoherenceVector;
import cs3246.a2.ColorHist;

public class WebServiceHandler {
	
	private static final int NUM_OF_RESULTS_RETURNED = 20;
	private static final String IMAGE_PATH = "./image/";
	private static final String IMAGE_UPLOAD_PATH = "./uploaded/";
	
	public static String[] query(String fileName) throws IOException{
		
		String[] results = new String[NUM_OF_RESULTS_RETURNED];
			
		BufferedImage bi = ImageIO.read(new File(IMAGE_UPLOAD_PATH + fileName)); 
		
		ColorHist hist = new ColorHist();
		double[] histogramResult = hist.getFeature(bi);
		
		
		//double[] edgeDirectionResult = 
		
		ColorCoherenceVector ccv = new ColorCoherenceVector();
		double[] ccvResult = ccv.getFeature(bi);
		 
		return results;
	}
}
