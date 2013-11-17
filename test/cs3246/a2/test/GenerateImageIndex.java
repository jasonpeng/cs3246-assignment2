package cs3246.a2.test;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import cs3246.a2.ColorCoherenceVector;
import cs3246.a2.ColorHist;
import cs3246.a2.Constant;
import cs3246.a2.SobelOperator;
import cs3246.a2.Util;
import cs3246.a2.db.ImageIndexSqliteHelper;
import cs3246.a2.model.ImageIndex;

public class GenerateImageIndex {

	public static void main(String[] args) throws ClassNotFoundException {
		ImageIndexSqliteHelper helper = new ImageIndexSqliteHelper(Constant.INDEX_DB_FILENAME);
		helper.createTable();
		
		List<ImageIndex> indexList = new ArrayList<ImageIndex>();
		String imageFolder = args[0];

		try {
			File folder;
			BufferedImage bi;
			
			folder = new File(imageFolder);
			File[] imageFiles = folder.listFiles(new FilenameFilter() {
			    public boolean accept(File dir, String name) {
			    	String lowerName = name.toLowerCase();
			    	boolean matched = lowerName.matches("^\\d+\\.(jpg|jpeg|png)$");
			    	return matched;
			    }
			});
			
			for (File file : imageFiles) {
				System.out.println("Indexing " + file.getName());
				bi = ImageIO.read(file);
				// convert image to 
				bi = Util.convertColorspace(bi, BufferedImage.TYPE_INT_RGB);
				
				int id = Integer.parseInt(file.getName().split("\\.")[0]);
				
				ColorHist hist = new ColorHist();
				double[] histogramResult = hist.getFeature(bi);
	
				SobelOperator edge = new SobelOperator();
				double[] edgeDirectionResult = edge.getFeature(bi);
	
				ColorCoherenceVector ccv = new ColorCoherenceVector();
				double[] ccvResult = ccv.getFeature(bi);
	
				ImageIndex imageIndex = new ImageIndex(id, ccvResult, histogramResult,
						edgeDirectionResult);
				indexList.add(imageIndex);
			}
			
			helper.persist(indexList);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
