package cs3246.a2.web;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.google.gson.Gson;

import cs3246.a2.AdjacentSimilarity;
import cs3246.a2.ColorCoherenceVector;
import cs3246.a2.ColorHist;
import cs3246.a2.Constant;
import cs3246.a2.Document;
import cs3246.a2.NormalizedSimilarity;
import cs3246.a2.SobelOperator;
import cs3246.a2.Util;
import cs3246.a2.model.Command;
import cs3246.a2.model.ImageIndex;
import cs3246.a2.model.ImageIndex;

public class WebServiceHandler {
	
	private static final int NUM_OF_RESULTS_RETURNED = 20;
	private static final String IMAGE_PATH = "./image/";
	private static final String IMAGE_UPLOAD_PATH = "./";
	private static final int NUM_OF_FILES = 400;
	private static final double weightHist = 0.5;
	private static final double weightEdge = 0.25;
	private static final double weightCCV = 0.25;
	
	public static void main(String[] args) throws IOException{
		Gson gson = new Gson();
		Command cmd = gson.fromJson(args[0], Command.class);
		if(cmd.commandType.equals("searchWithImage")){
			BufferedImage bi = ImageIO.read(new File(cmd.queryImage));
			if(cmd.isCropped){
				bi = bi.getSubimage(cmd.cropX, cmd.cropY, cmd.cropW, cmd.cropH);
			}
			queryWithImage(bi, cmd.numberOfResult);
				
		}else if(cmd.commandType.equals("searchWithColor")){
			
		}
		
	}
	
	private static boolean queryWithImage(BufferedImage bi, int number){
		bi = Util.convertColorspace(bi, BufferedImage.TYPE_INT_RGB);
				
		// Extract features of the query and create query classes:
		ColorHist hist = new ColorHist();
		double[] histogramResult = hist.getFeature(bi);
		
		SobelOperator edge = new SobelOperator();
		double[] edgeDirectionResult = edge.getFeature(bi);
		
		ColorCoherenceVector ccv = new ColorCoherenceVector();
		double[] ccvResult = ccv.getFeature(bi);
		
		// Read the pre-computed image features:
		ImageDBReader dbReader = new ImageDBReader(Constant.DB_FILE_NAME);
		List<ImageIndex> images = dbReader.getImages();
		
		// Create similarity classes:
		NormalizedSimilarity nSim = new NormalizedSimilarity();
		AdjacentSimilarity aSim = new AdjacentSimilarity();
		
		ArrayList<Document> list = new ArrayList<Document>();
		
		for (ImageIndex image : images){
			
			double[] similarityHist = image.getSimilarityHist();
			double[] similarityEdge = image.getSimilarityEdge();
			double[] similarityCCV = image.getSimilarityCCV();
			
			double scoreHist = hist.computeSimilarity(similarityHist, nSim);
			double scoreEdge = edge.computeSimilarity(similarityEdge, nSim);
			double scoreCCV = ccv.computeSimilarity(similarityCCV, nSim);
			
			double normalizedScoreHist = scoreHist * 1;
			double normalizedScoreEdge = scoreEdge / 10;
			double normalizedScoreCCV = scoreCCV * 200;
			
			double finalScore = normalizedScoreHist * weightHist + normalizedScoreEdge * weightEdge + normalizedScoreCCV * weightCCV;
			
			// System.out.println("Final, hist, edge, score\t" + finalScore + "\t\t" + normalizedScoreHist + "\t\t" + normalizedScoreEdge + "\t\t" + normalizedScoreCCV);
			
			Document doc = new Document(image.getFilename(), finalScore);
    		list.add(doc);
		}
		
		Collections.sort(list);
    	
		int size = (number < list.size())?number:list.size();
		
		System.out.print("{\"length\":"+size);
		for ( int i = 0; i < size ; i++){
				System.out.print(", "+'"'+i+'"'+":"+'"'+list.get(i).getFileName()+'"');
		}
		System.out.println("}");
	}
}
