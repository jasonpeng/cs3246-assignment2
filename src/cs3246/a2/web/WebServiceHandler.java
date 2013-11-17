package cs3246.a2.web;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.awt.Color;
import java.awt.Graphics2D;
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
	
	private static final double weightHist = 0; // 0.5
	private static final double weightEdge = 0; // 0.25
	private static final double weightCCV = 1; // 0.25
	
	public static void main(String[] args) throws IOException, ClassNotFoundException {
		Gson gson = new Gson();
		Command cmd = gson.fromJson(args[0], Command.class);
		if(cmd.commandType.equals("searchWithImage")){
			BufferedImage bi = ImageIO.read(new File(cmd.queryImage));
			bi = Util.convertColorspace(bi, BufferedImage.TYPE_INT_RGB);
			
			if(cmd.isCropped){
				bi = bi.getSubimage(cmd.cropX, cmd.cropY, cmd.cropW, cmd.cropH);
			}
			queryWithImage(bi, cmd.numberOfResult, cmd.category, 0.4, 0.4, 0.2);
		}else if(cmd.commandType.equals("searchWithColor")){
//			int colorValue = Integer.parseInt(cmd.queryColor);
			queryWithColor(cmd.queryColor, cmd.numberOfResult, cmd.category);
		}
		else if(cmd.commandType.equals("searchWithPattern")){
			BufferedImage bi = ImageIO.read(new File(cmd.queryImage));
			bi = Util.convertColorspace(bi, BufferedImage.TYPE_INT_RGB);
			queryWithImage(bi, cmd.numberOfResult, cmd.category, 0.2, 0.2, 0.6);
		}
		
	}
	
	private static void queryWithImage(BufferedImage bi, int number, String category, double hisW, double ccvW, double edgeW)
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
		for (ImageIndex imageIndex : imageIndexList){
			double[] similarityHist = imageIndex.getSimilarityHist();
			double[] similarityEdge = imageIndex.getSimilarityEdge();
			double[] similarityCCV = imageIndex.getSimilarityCCV();
			
			double scoreHist = (hisW<0.001)?0: hist.computeSimilarity(similarityHist, nSim);
			double scoreEdge = (edgeW<0.001)?0:edge.computeSimilarity(similarityEdge, nSim);
			double scoreCCV = (ccvW<0.001)?0:ccv.computeSimilarity(similarityCCV, nSim);
			
			double normalizedScoreHist = scoreHist * 1;
			double normalizedScoreEdge = scoreEdge / 10;
			double normalizedScoreCCV = scoreCCV * 1;
			
			double finalScore = normalizedScoreHist * hisW + 
					normalizedScoreEdge * edgeW + 
					normalizedScoreCCV * ccvW;
			
			Result result = new Result(productHelper.getById(imageIndex.getId()), finalScore);
			resultList.add(result);
		}
		
		Collections.sort(resultList);
		
		Gson gson = new Gson();
		System.out.print(gson.toJson(resultList.subList(0, number)));
	}
	
	private static void queryWithColor(int colorValue, int number, String category) throws ClassNotFoundException{
		int width = 200;
		int height = 200;
		BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		int[] data = new int[width*height];
		for(int i = 0; i < width*height; i++)
			data[i] = colorValue;
		bi.setRGB(0, 0, width, height, data, 0, width);
		
		queryWithImage(bi, number, category, 1, 0, 0);
	}

}


