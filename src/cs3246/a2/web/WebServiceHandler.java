package cs3246.a2.web;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.google.gson.Gson;

import cs3246.a2.AdjacentSimilarity;
import cs3246.a2.ColorCoherenceVector;
import cs3246.a2.ColorHist;
import cs3246.a2.Constant;
import cs3246.a2.NormalizedSimilarity;
import cs3246.a2.Result;
import cs3246.a2.SobelOperator;
import cs3246.a2.Util;
import cs3246.a2.db.ImageIndexSqliteHelper;
import cs3246.a2.db.ProductSqliteHelper;
import cs3246.a2.model.Command;
import cs3246.a2.model.ImageIndex;
import cs3246.a2.model.Product;

public class WebServiceHandler {
	
	private static final double weightHist = 0.5;
	private static final double weightEdge = 0.25;
	private static final double weightCCV = 0.25;
	
	public static void main(String[] args) throws IOException, ClassNotFoundException {
		Gson gson = new Gson();
		Command cmd = gson.fromJson(args[0], Command.class);
		if(cmd.commandType.equals("searchWithImage")){
			BufferedImage bi = ImageIO.read(new File(cmd.queryImage));
			bi = Util.convertColorspace(bi, BufferedImage.TYPE_INT_RGB);
			
			if(cmd.isCropped){
				bi = bi.getSubimage(cmd.cropX, cmd.cropY, cmd.cropW, cmd.cropH);
			}
			queryWithImage(bi, cmd.numberOfResult, cmd.category);
		}else if(cmd.commandType.equals("searchWithColor")){
			int colorValue = Integer.parseInt(cmd.queryColor);
			
		}
		
	}
	
	private static void queryWithImage(BufferedImage bi, int number, String category)
			throws ClassNotFoundException {
		// Extract features of the query and create query classes:
		ColorHist hist = new ColorHist();
		double[] histogramResult = hist.getFeature(bi);
		
		SobelOperator edge = new SobelOperator();
		double[] edgeDirectionResult = edge.getFeature(bi);
		
		ColorCoherenceVector ccv = new ColorCoherenceVector();
		double[] ccvResult = ccv.getFeature(bi);
		
		// Read the pre-computed image features:
		ImageIndexSqliteHelper indexHelper = new ImageIndexSqliteHelper(Constant.INDEX_DB_FILENAME);
		ProductSqliteHelper productHelper = new ProductSqliteHelper(Constant.PRODUCT_DB_FILENAME);
		List<Product> products = productHelper.getFromCategoryName(category);
		List<ImageIndex> imageIndexList = indexHelper.getFromProducts(products);
		
		// Create similarity classes:
		NormalizedSimilarity nSim = new NormalizedSimilarity();
		//AdjacentSimilarity aSim = new AdjacentSimilarity();
		
		ArrayList<Result> resultList = new ArrayList<Result>();
		for (int i = 0; i < number; i++){
			ImageIndex imageIndex = imageIndexList.get(i);
			
			double[] similarityHist = imageIndex.getSimilarityHist();
			double[] similarityEdge = imageIndex.getSimilarityEdge();
			double[] similarityCCV = imageIndex.getSimilarityCCV();
			
			double scoreHist = hist.computeSimilarity(similarityHist, nSim);
			double scoreEdge = edge.computeSimilarity(similarityEdge, nSim);
			double scoreCCV = ccv.computeSimilarity(similarityCCV, nSim);
			
			double normalizedScoreHist = scoreHist * 1;
			double normalizedScoreEdge = scoreEdge / 10;
			double normalizedScoreCCV = scoreCCV * 1;
			
			double finalScore = normalizedScoreHist * weightHist + 
					normalizedScoreEdge * weightEdge + 
					normalizedScoreCCV * weightCCV;
			
			Result result = new Result(productHelper.getById(imageIndex.getId()), finalScore);
			resultList.add(result);
		}
		
		Collections.sort(resultList);
		
		Gson gson = new Gson();
		System.out.print(gson.toJson(resultList));
	}
	
	private static void queryWithColor(int colorValue, int number, String category){
		BufferedImage bi = null;
	}

}


